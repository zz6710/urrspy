package com.kayak.clear;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kayak.clear.dataSync.dao.TtrdInstitutionDao;
import com.kayak.clear.dataSync.dao.ZgSonProductInfoDao;
import com.kayak.clear.service.monitor.ClearTaskRegistServiceSpdb;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.core.util.PublicUtils;
import com.kayak.dps.check.service.ReportDataValidateService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * 任务注册定时器
 *
 * @author zhangz1
 * @date 2022/6/17 10:34
 */
@Configuration
public class TaskRegistMonitorSpdb {

    private final Logger log = LoggerFactory.getLogger(TaskRegistMonitorSpdb.class);

    @Value("${zg.dblink.url}")
    private String url;
    @Value("${zg.dblink.user}")
    private String user;
    @Value("${zg.dblink.password}")
    private String password;

    @Autowired
    private ApplicationContext ac;
    @Value("${redis.lock.waitTime:5}")

    private String redissonKeyA = "{Dps}:TaskRegistMonitorSpdb:KeyA";

    private String redissonKeyB = "{Dps}:TaskRegistMonitorSpdb:KeyB";

    private String redissonKeyC = "{Dps}:TaskRegistMonitorSpdb:KeyC";

    private String redissonKeyD = "{Dps}:TaskRegistMonitorSpdb:KeyD";

    @Autowired
    private RedissonClient redissonClient;

    @Scheduled(cron = "${pms.scheduled.regist.cispeq001}")
    public void doRegistProcess() {
        // 获取任务注册业务处理 service，内部成员变量多，service不通过@Autowired注入
        ClearTaskRegistServiceSpdb clearTaskRegistServiceSpdb = ac.getBean(ClearTaskRegistServiceSpdb.class);

        // 多机部署的话需要增加分布式锁，否则会有并发问题
        RLock lock = redissonClient.getLock(redissonKeyA);
        try {
            if (lock.tryLock()) {
                log.info("---------- TaskRegistMonitorSpdb分布式加锁：" + redissonKeyA + " --------------");
                //实时清算任务刷新
                clearTaskRegistServiceSpdb.registProcess();

            }
        } catch (Exception e) {
            log.error("CISPEQ001定时任务出错！错误信息:[{}]", e.getMessage(), e);
        } finally {
            // 加了分布式锁则需要释放锁
            if (lock.isHeldByCurrentThread()) {
                log.info("---------- TaskRegistMonitorSpdb分布式解锁：" + redissonKeyA + " --------------");
                Optional.of(lock).ifPresent(RLock::unlock);
            }
        }
    }

    @Scheduled(cron = "${pms.scheduled.regist.taskRegistRefresh}")
    public void doTaskRegistRefresh() {
        // 获取任务注册业务处理 service，内部成员变量多，service不通过@Autowired注入
        TtrdInstitutionDao ttrdInstitutionDao = ac.getBean(TtrdInstitutionDao.class);
        // 多机部署的话需要增加分布式锁，否则会有并发问题
        RLock lock = redissonClient.getLock(redissonKeyB);
        try {
            if (lock.tryLock()) {
                log.info("---------- TaskRegistMonitorSpdb分布式加锁：" + redissonKeyB + " --------------");
                List<String> updSqlList = new ArrayList<>(Arrays.asList("TASKREGEU001", "TASKREGEU002"));
                Map<String, Object> params = new HashMap<>();
                params.put("deal_date", PublicUtils.getSysWordDay());
                ttrdInstitutionDao.executeUpdSqls(updSqlList, params);
            }
        } catch (Exception e) {
            log.error("TaskRegistRefresh定时任务出错！错误信息:[{}]", e.getMessage(), e);
        } finally {
            // 加了分布式锁则需要释放锁
            if (lock.isHeldByCurrentThread()) {
                log.info("---------- TaskRegistMonitorSpdb分布式解锁：" + redissonKeyB + " --------------");
                Optional.of(lock).ifPresent(RLock::unlock);
            }
        }
    }

    @Scheduled(cron = "${pms.scheduled.regist.zgson01}")
    public void doZgSonProductInfo() {
        Connection connection = null;
        Statement statement = null;

        // 多机部署的话需要增加分布式锁，否则会有并发问题
        RLock lock = redissonClient.getLock(redissonKeyC);

        try {
            if (lock.tryLock()) {
                log.info("---------- TaskRegistMonitorSpdb分布式加锁：" + redissonKeyC + " --------------");
                Class.forName("com.ibm.db2.jcc.DB2Driver");
                connection= DriverManager.getConnection(url, user, password);
                log.info("连接资管库信息:"+connection.getClientInfo().toString());
                statement = connection.createStatement();

                // 获取任务注册业务处理，内部成员变量多，不通过@Autowired注入
                ZgSonProductInfoDao zgSonProductInfoDao = ac.getBean(ZgSonProductInfoDao.class);
                ReportDataValidateService reportDataValidateService = ac.getBean(ReportDataValidateService.class);

                // 查询资管子产品信息
                ResultSet resultSet = zgSonProductInfoDao.getZgSonProductInfo(statement, ExeQuery.queryExeId("ZGSONEQ001"));

                if (!resultSet.next()) {
                    log.info("---------- 资管产品信息表没有数据 --------------");
                    return;
                }

                // 插入产品临时表
                ResultSet resultSet1 = zgSonProductInfoDao.getZgSonProductInfo(statement, ExeQuery.queryExeId("ZGSONEQ001"));
                List<SqlRow> sqlRows = zgSonProductInfoDao.insTempOdsProdBaseInfo(resultSet1);

                if (CollectionUtil.isEmpty(sqlRows)) {
                    log.info("---------- 资管产品信息和ods子产品到期日和产品状态无差异 --------------");
                    return;
                }

                // 更新子产品信息登记数据，不调用清算处理
                log.info("---------- 差异的子产品信息 --------------" + sqlRows);

                for (SqlRow sqlRow : sqlRows) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("deal_date", DateUtil.getBeforeDate());
                    params.put("prod_code", sqlRow.getString("prod_code"));
                    zgSonProductInfoDao.runSonShareDelInfo(params);
                }

                // 子份额信息登记指标校验
                log.info("---------- 子份额信息登记指标校验 Start -----------");
                //将计算的数据日期进行覆盖
                Map<String, String> params = new HashMap<>();
                params.put("reportType", "02");//报表大类
                params.put("reportTable", "app_son_share_info_reg");//报表名称
                params.put("indexType", null);//指标类型
                params.put("indexCode", null);
                params.put("reportDate", null);//报送日期
                params.put("dealDate", DateUtil.getBeforeDate());//数据日期

                String deal_date = reportDataValidateService.calDataDateByReportDate(params);
                params.put("dealDate", deal_date);//数据日期
                reportDataValidateService.execute(deal_date, params);
                log.info("---------- 子份额信息登记指标校验 End--------------");

                // 子份额信息删除指标校验
                log.info("---------- 子份额信息删除指标校验 Start -----------");
                //将计算的数据日期进行覆盖
                Map<String, String> params1 = new HashMap<>();
                params1.put("reportType", "02");//报表大类
                params1.put("reportTable", "app_son_share_del_reg");//报表名称
                params1.put("indexType", null);//指标类型
                params1.put("indexCode", null);
                params1.put("reportDate", null);//报送日期
                params1.put("dealDate", DateUtil.getBeforeDate());//数据日期

                String deal_date1 = reportDataValidateService.calDataDateByReportDate(params1);
                params1.put("dealDate", deal_date1);//数据日期
                reportDataValidateService.execute(deal_date1, params1);
                log.info("---------- 子份额信息删除指标校验 End--------------");
            }
        } catch (Exception e) {
            log.error("同步资管子产品信息失败：", e);
        } finally {
            // 加了分布式锁则需要释放锁
            if (lock.isHeldByCurrentThread()) {
                log.info("---------- TaskRegistMonitorSpdb分布式解锁：" + redissonKeyC + " --------------");
                Optional.of(lock).ifPresent(RLock::unlock);
            }
            // 关闭查询资管数据库资源
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(cron = "${pms.scheduled.regist.checkException:0 0/30 6-18 * * ?}")
    public void doCheckException() {
        // 多机部署的话需要增加分布式锁，否则会有并发问题
        RLock lock = redissonClient.getLock(redissonKeyD);
        try {
            if (lock.tryLock()) {
                log.info("---------- TaskRegistMonitorSpdb分布式加锁：" + redissonKeyD + " --------------");

                StringBuilder stringBuilder = new StringBuilder();
                List<SqlRow> sqlStrs = ExeQuery.queryPortSqlByTaskId("R999");

                for (SqlRow sqlStr : sqlStrs) {
                    List<SqlRow> sqlResults = ExeQuery.query(sqlStr.getString("sqlstr"));
                    if (CollectionUtil.isNotEmpty(sqlResults)) {
                        for (SqlRow sqlResult : sqlResults) {
                            stringBuilder.append(sqlResult.get("error_info"));
                        }
                    }
                }

                if (ObjectUtil.isNotEmpty(stringBuilder)) {
                    log.error("[URRS-ERROR-H]系统异常监控提醒：{}", stringBuilder);
                }
            }
        } catch (Exception e) {
            log.error("[URRS-ERROR-H]系统异常监控定时任务出错！错误信息:[{}]", e.getMessage(), e);
        } finally {
            // 加了分布式锁则需要释放锁
            if (lock.isHeldByCurrentThread()) {
                log.info("---------- TaskRegistMonitorSpdb分布式解锁：" + redissonKeyD + " --------------");
                Optional.of(lock).ifPresent(RLock::unlock);
            }
        }
    }
}