package com.kayak.clear.dataSync.service;

import com.kayak.aspect.annotations.APIDefine;
import com.kayak.clear.dataSync.dao.TtrdInstitutionDao;
import com.kayak.clear.dataSync.model.TtrdInstitution;
import com.kayak.core.util.ExeQuery;
import com.kayak.core.util.PublicUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@APIDefine(desc = "机构信息", model = TtrdInstitution.class)
public class TtrdInstitutionService {

    protected static final Logger log = LoggerFactory.getLogger(TtrdInstitutionService.class);

    private static volatile int CONNECTION_TIMES = 0;

    @Value("${zg.dblink.url}")
    private String url;
    @Value("${zg.dblink.user}")
    private String user;
    @Value("${zg.dblink.password}")
    private String password;
    @Value("${zg.query.length}")
    private int max_length;
    @Value("${zg.query.reflect.sql}")
    private String reflectExeId;
    @Value("${zg.query.org_info.sql}")
    private String orgInfoExeId;
    @Value("${zg.query.org_list.sql}")
    private String orgListExeId;
    @Value("${zg.query.reflect.code}")
    private String reflectPortCode;
    @Value("${zg.query.org_info.code}")
    private String orgInfoPortCode;
    @Value("${zg.query.reflect.upd}")
    private String reflectUpdSql;
    @Value("${zg.query.org_info.upd}")
    private String orgInfoUpdSql;
    @Value("${zg.query.asset_info.upd}")
    private String assetInfoUpdSql;
    @Value("${zg.query.memo.upd1}")
    private String detailsUpdSql1;
    @Value("${zg.query.memo.upd2}")
    private String detailsUpdSql2;


    @Autowired
    private TtrdInstitutionDao ttrdInstitutionDao;

    @Autowired
    private RedissonClient redissonClientZg;

    private String redissonKeyA = "{Dps}:TtrdInstitutionService:KeyA";
    private String redissonKeyB = "{Dps}:TtrdInstitutionService:KeyB";

    /**
     * 同步资管表接口数据
     * @throws Exception
     */
    public void syncZGTradeRelationInfo() throws Exception {
        String cur_port = "";//当前处理接口
        String workday = "";
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            connection= DriverManager.getConnection(url, user, password);
            int CUR_TIMES = ++CONNECTION_TIMES;
            log.info("连接资管库信息:"+connection.getClientInfo().toString()+",当前连接次数序号:"+(CUR_TIMES));

            statement = connection.createStatement();
            workday = PublicUtils.getSysWordDay();
            Map<String, Object> params = new HashMap<>();
            params.put("workday", workday);
            if(CONNECTION_TIMES == CUR_TIMES) {//仅当当前执行序数等于全局序数时执行操作
                String condition = ttrdInstitutionDao.getQueryCondition(orgListExeId, params);

                List<String> tableList = new ArrayList<>(Arrays.asList(reflectExeId,orgInfoExeId));
                List<String> portList = new ArrayList<>(Arrays.asList(reflectPortCode,orgInfoPortCode));
                params.put("condition", condition);

                if(condition.length() > 0){
                    //入库资管今日交易对手数据
                    for (int i=0; i < tableList.size(); i++) {
                        cur_port = portList.get(i);
                        ResultSet resultSet = ttrdInstitutionDao.getZgOrgInfo(statement, ExeQuery.queryExeId(tableList.get(i)), params);
                        List<String> fieldList = ttrdInstitutionDao.getPortManageColumn(cur_port);
                        // 多机部署的话需要增加分布式锁，否则会有并发问题
                        RLock lock = redissonClientZg.getLock(redissonKeyA);
                        try {
                            if (lock.tryLock()) {
                                log.info("---------- TtrdInstitutionService分布式加锁：" + redissonKeyA + " --------------");
                                ttrdInstitutionDao.putPortManageColumn(cur_port, resultSet, fieldList, max_length);
                            } else {
                                log.info("日期："+workday+",同步资管" + cur_port + "接口已锁，入库步骤跳过");
                            }
                        } finally {
                            if (lock.isHeldByCurrentThread()) {
                                log.info("---------- TtrdInstitutionService分布式加解锁：" + redissonKeyA + " --------------");
                                Optional.of(lock).ifPresent(RLock::unlock);
                            }
                        }
                        log.info("日期："+workday+",同步资管" + cur_port + "接口数据完成");
                    }
                    //updateTradeInfoReportData(params);
                }
            }
            log.info("同步交易资管数据完成");
        } catch (Exception e){
            log.error("日期："+workday+",同步资管" + cur_port + "接口数据异常：", e);
        } finally {
            statement.close();
            connection.close();
        }

    }

    /**
     * 更新交易登记报表
     * @throws Exception
     */
    public void updateTradeInfoReportData () throws Exception {
        String workday = PublicUtils.getSysWordDay();
        List<String> updSqlList = new ArrayList<>(Arrays.asList(reflectUpdSql,orgInfoUpdSql,assetInfoUpdSql,detailsUpdSql1,detailsUpdSql2));
        Map<String, Object> params = new HashMap<>();
        params.put("workday", workday);

        // 多机部署的话需要增加分布式锁，否则会有并发问题
        RLock lock = redissonClientZg.getLock(redissonKeyB);
        try {
            if (lock.tryLock()) {
                log.info("---------- TtrdInstitutionService分布式加锁：" + redissonKeyB + " --------------");
                ttrdInstitutionDao.executeUpdSqls(updSqlList, params);
            }
        } finally {
            // 加了分布式锁则需要释放锁
            if (lock.isHeldByCurrentThread()) {
                log.info("---------- TtrdInstitutionService分布式解锁：" + redissonKeyB + " --------------");
                Optional.of(lock).ifPresent(RLock::unlock);
            }
        }

    }




}
