package com.kayak.clear;

import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.service.business.BusinessBaseTaskService;
import com.kayak.config.constants.STGConstants;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.dps.ods.service.DealPortFileService;
import com.kayak.server.ServerUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 任务注册定时器
 *
 */
@Configuration
@RefreshScope
public class TaskRegistMonitorRedo {

    private final Logger log = LoggerFactory.getLogger(TaskRegistMonitorRedo.class);

    @Autowired
    private ApplicationContext ac;
    @Value("${redis.lock.waitTime:5}")

    private String redissonKeyA = "{Dps}:TaskRegistMonitorRedo:KeyA";

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ComnDao comnDao;
    @Autowired
    private DealPortFileService dealPortFileService;
    @Autowired
    private BusinessBaseTaskService businessBaseTaskService;
    
    @Scheduled(cron = "${pms.scheduled.regist.redo001}")
    public void doRegistProcess() throws Exception {
        // 获取任务注册业务处理 service，内部成员变量多，service不通过@Autowired注入
        String sqlStg = "select paravalue from sys_param where paraid = '90000051703' ";//T100
        List<SqlRow> rsStg = comnDao.findRows(sqlStg);
        String[] stgTaskId = new String[0];
        String sqlModules = "select paravalue from sys_param where paraid = '90000051704' ";//S068
        List<SqlRow> rsModules = comnDao.findRows(sqlModules);
        String[] modulesTaskId =new String[0];
        String sqlSpecial = "select paravalue from sys_param where paraid = '90000051705' ";//S074
        List<SqlRow> rsSpecial = comnDao.findRows(sqlSpecial);
        String[] SpecialTaskId =new String[0];
        // 多机部署的话需要增加分布式锁，否则会有并发问题
        RLock lock = redissonClient.getLock(redissonKeyA);
        try {
            if (lock.tryLock()) {
                log.info("---------- TaskRegistMonitorRedo分布式加锁：" + redissonKeyA + " --------------");
                String isRedo = SysUtil.getSystemParamsByParaid("90000051707");//产品端实时任务是否启用
                if("1".equals(isRedo)){
                //实时清算任务刷新
                for (SqlRow row : rsStg) {//T100
                    if (!"".equals(row.getString("paravalue"))) {
                        stgTaskId = row.getString("paravalue").split(",");
                    }
                    if (stgTaskId.length > 0) {
                        PubReq request = new PubReq();
                        request.setTaskDate(DateUtil.getBeforeDate());
                        for (String taskId : stgTaskId) {
                            log.info("---------- 数据中台STG层数据处理 Start -----------");
                            Map<String, Object> params = new HashMap<>();
                            params.put("portType", STGConstants.STG_PORT_TYPE_DC);
                            params.put("portDir", STGConstants.STG_DATA_DIR_RCV);
                            params.put("dealDate", request.getTaskDate());//处理日期
                            params.put("dealType", STGConstants.STG_DATA_HANDLE_DAYS);//按天处理
                            if (StringUtils.isNotEmpty(taskId)) {
                                params.put("pId", taskId);//关联任务id
                                request.setTaskId(taskId);
                            }
                            try {
                                dealPortFileService.dealAllPortInfo(params);
                                //处理增量数据
                                dealPortFileService.historyIncrementDataHandler(request, request.getTaskDate());
                            } catch (Exception e) {
                                log.error("[URRS-ERROR-H]{}日任务{}执行失败：{}", request.getTaskDate(), request.getTaskId(), e.getMessage());
                                throw e;
                            }
                            log.info("---------- 数据中台STG层数据处理 End -----------");
                        }
                    }
                }
                for (SqlRow row : rsModules) {//S068
                    if (!"".equals(row.getString("paravalue"))) {
                        modulesTaskId = row.getString("paravalue").split(",");
                    }
                    PubReq request = new PubReq();
                    request.setTaskDate(DateUtil.getBeforeDate());
                    if (modulesTaskId.length > 0) {
                        for (String taskId : modulesTaskId) {
                            if (StringUtils.isNotEmpty(taskId)) {
                                request.setTaskId(taskId);
                            }
                            try {
                                log.info("---------- 任务: " + request.getTaskId() + " 数据加工开始 Start -----------");
                                Instant startTime = Instant.now();
                                Map<String, Object> params = new HashMap<String, Object>();
                                params.put("deal_date", DateUtil.getBeforeDate());
                                params.put("CRT_DT", DateUtil.getNowDate());//创建日期
                                params.put("UPD_DT", DateUtil.getNowDate());//更新日期
                                params.put("CRT_TM", DateUtil.getNowTime());
                                params.put("UPD_TM", DateUtil.getNowTime());
                                params.put("CRT_DT_TM", DateUtil.getTimestamp14());//创建日期
                                params.put("UPD_DT_TM", DateUtil.getTimestamp14());//更新日期
                                params.put("YESTERDAY", DateUtil.getupdateoneDate(DateUtil.getBeforeDate()));
                                params.put("TOMORROW", DateUtil.getTomorrowDate(DateUtil.getBeforeDate()));
                                params.put("MON_START_DT", DateUtil.getFirstDayDateOfMonth(DateUtil.getBeforeDate()));
                                params.put("MON_END_DT", DateUtil.getLastDayOfMonth(DateUtil.getBeforeDate()));
                                params.put("QUA_START_DT", DateUtil.getMaxOrMinDateOfQuarter(DateUtil.getBeforeDate(), "min"));
                                params.put("QUA_END_DT", DateUtil.getMaxOrMinDateOfQuarter(DateUtil.getBeforeDate(), "max"));
                                params.put("theory_report_start_date", DateUtil.getBeforeDate());
                                params.put("theory_report_end_date", DateUtil.getBeforeDate());
                                List<SqlRow> list = ExeQuery.queryPortSqlByTaskId(request.getTaskId());
                                Map<String, Object> tparam = getTaskBaseType(request.getTaskId());
                                String base_type = "";
                                String mark = "0";
                                if (tparam.containsKey("base_type")) {
                                    base_type = tparam.get("base_type").toString();
                                    String data_type = tparam.get("data_type").toString();
                                    String report_table = tparam.get("sys_ref_table").toString();
                                    int inner_submission_time_require = Integer.parseInt(tparam.get("inner_submission_time_require").toString());
                                    int supervise_submission_time_require = Integer.parseInt(tparam.get("supervise_submission_time_require").toString());
                                    if ("02".equals(data_type)) {
                                        params.put("theory_report_start_date", DateUtil.add(DateUtil.getBeforeDate(), "yyyyMMdd", inner_submission_time_require));
                                        params.put("theory_report_end_date", DateUtil.add(DateUtil.getBeforeDate(), "yyyyMMdd", supervise_submission_time_require));
                                    } else {
                                        params.put("theory_report_start_date", DateUtil.addSysWordDay(DateUtil.getBeforeDate(), inner_submission_time_require));
                                        params.put("theory_report_end_date", DateUtil.addSysWordDay(DateUtil.getBeforeDate(), supervise_submission_time_require));
                                    }
                                    if (!"app_prod_regist_filing_info".equals(report_table)) {
                                        mark = getTabSucNum(report_table, params.get("theory_report_start_date").toString());
                                    }
                                }
                                if ("0".equals(mark)) {
                                    if ("06".equals(base_type)) {
                                        if (DateUtil.getBeforeDate().equals(DateUtil.getLastDayOfMonth(DateUtil.getBeforeDate()))) {
                                            mark = "0";
                                        } else {
                                            mark = "1";
                                        }
                                    } else if ("07".equals(base_type)) {
                                        if (DateUtil.getBeforeDate().equals(DateUtil.getMaxOrMinDateOfQuarter(DateUtil.getBeforeDate(), "max"))) {
                                            mark = "0";
                                        } else {
                                            mark = "1";
                                        }
                                    } else {
                                        mark = "0";
                                    }
                                }
                                if ("0".equals(mark)) {
                                    StringBuffer exeid = new StringBuffer();
                                    try {
                                        // 初始化线程池
                                        String newMaximumPoolSize = SysUtil.getSystemParamsByParaid("90000030001");
                                        String newCorePoolSize = SysUtil.getSystemParamsByParaid("90000030002");
                                        int size = Integer.parseInt(newMaximumPoolSize);
                                        int coreSize = Integer.parseInt(newCorePoolSize);
                                        ExecutorService executorService = new ThreadPoolExecutor(coreSize, size, 0L, TimeUnit.MILLISECONDS,
                                                new LinkedBlockingQueue<>());
                                        for (SqlRow sqlRow : list) {
                                            exeid.setLength(0);
                                            exeid.append(sqlRow.get("exeid"));
                                            log.info("执行语句EXEID[{}]", sqlRow.get("exeid"));
                                            String sqlstr = sqlRow.getString("sqlstr");
                                            Pattern pattern = Pattern.compile("\\$LIST\\{(\\w+)\\}");
                                            Matcher matcher = pattern.matcher(sqlstr);
                                            if (matcher.find()) {
                                                String paramKey = matcher.group(1); //括号内内容
                                                Map<String, Object> param = new HashMap<>();
                                                param.put("code", paramKey);
                                                List<SqlRow> rows = comnDao.findRows("select * from base_port_sql_param_info where code = $S{code}", param); //sql
                                                if (!rows.isEmpty()) {
                                                    String paramSqlstr = rows.get(0).getString("sqlstr");// 参数的sql查询语句
                                                    List<SqlRow> paramRows = comnDao.findRows(paramSqlstr, params);
                                                    List<Future> futureList = new ArrayList<>();
                                                    for (int i = 0; i < paramRows.size(); i++) {
                                                        int index = i;
                                                        Runnable runnable = () -> {
                                                            String sqlstr1 = sqlstr.replace("$LIST{" + paramKey + "}", paramRows.get(index).getString(paramKey));
                                                            try {
                                                                comnDao.update(sqlstr1, params);
                                                            } catch (Exception e) {
                                                                log.error(e.getMessage(), e);
                                                                throw new RuntimeException(e);
                                                            }
                                                        };
                                                        Future<?> future = executorService.submit(runnable);
                                                        futureList.add(future);
                                                    }

                                                    for (Future future : futureList) {
                                                        try {
                                                            future.get();
                                                        } catch (Exception e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                    }
                                                }
                                            } else {
                                                comnDao.update(sqlstr, params);
                                            }
                                        }
                                        executorService.shutdown();
                                    } catch (Exception e) {
                                        log.error(e.getMessage(), e);
                                        throw new SQLException("执行SQL[" + exeid + "]报错：" + e.getMessage(), e);
                                    }
                                }
                                // 获取当前系统时间点
                                Instant endTime = Instant.now();
                                // 计算时间间隔
                                Duration duration = Duration.between(startTime, endTime);
                                long seconds = duration.getSeconds();
                                log.info("任务{}:用时{}秒", request.getTaskId(), seconds);
                                log.info("---------- 任务: " + request.getTaskId() + " 数据加工结束 End-----------");
                            } catch (Exception e) {
                                log.error("[URRS-ERROR-H]{}日任务{}执行失败：{}", request.getTaskDate(), request.getTaskId(), e.getMessage());
                                throw e;
                            }
                        }
                    }
                }
                for (SqlRow row : rsSpecial) {//S074
                    if (!"".equals(row.getString("paravalue"))) {
                        SpecialTaskId = row.getString("paravalue").split(",");
                    }
                    PubReq request = new PubReq();
                    if (SpecialTaskId.length > 0) {
                        for (String taskId : SpecialTaskId) {
                            if (StringUtils.isNotEmpty(taskId)) {
                                request.setTaskId(taskId);
                            }
                            log.info("---------- 任务: " + request.getTaskId() +" 自动审批流程开始 Start-----------");
                            try {
                                // 查询比较语句
                                List<SqlRow> sqlStrs = ExeQuery.queryPortSqlByTaskId(request.getTaskId());

                                for (SqlRow sqlStr : sqlStrs) {
                                    List<SqlRow> sqlRows = comnDao.findRows(sqlStr.getString("sqlstr"));
                                    String sqlCol = ExeQuery.queryExeId(sqlStr.getString("exeid").replaceAll("EU", "FLOW"));

                                    for (SqlRow sqlRow : sqlRows) {
                                        try {
                                            Map<String, Object> params = new HashMap<>();
                                            Map<String, Object> paramsOld = new HashMap<>();

                                            if (StringUtils.isNotEmpty(sqlCol)) {
                                                String[] columns = sqlCol.split(",");

                                                for (String column : columns) {
                                                    if (StringUtils.isNotEmpty(column)) {
                                                        column = column.trim();

                                                        if (column.endsWith("_9o9")) {
                                                            paramsOld.put(column.replaceAll("_9o9", ""), sqlRow.getString(column));
                                                        } else {
                                                            params.put(column, sqlRow.getString(column));
                                                        }
                                                    }
                                                }
                                            }

                                            params.put("oldData", new JSONObject(paramsOld));
                                            ServerUtil.requestPost("DpsApp", "com.kayak.dps.app.model.ProdInfoOds", "updateProdInfo", params);
                                        } catch (Exception e) {
                                            log.error("---------- 任务: " + request.getTaskId() +" 自动审批流程失败: "+ e.getMessage() +" Continue-----------");
                                            e.printStackTrace();
                                            continue;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                log.error("---------- 任务: " + request.getTaskId() +" 自动审批流程失败: "+ e.getMessage() +" End-----------");
                                e.getMessage();
                            }
                            log.info("---------- 任务: " + request.getTaskId() +" 自动审批流程结束 End-----------");
                        }
                    }
                }
                }
            }
        } catch (Exception e) {
            log.error("实时定时任务出错！错误信息:[{}]", e.getMessage(), e);
        } finally {
            // 加了分布式锁则需要释放锁
            if (lock.isHeldByCurrentThread()) {
                log.info("---------- TaskRegistMonitorRedo分布式解锁：" + redissonKeyA + " --------------");
                Optional.of(lock).ifPresent(RLock::unlock);
            }
        }
    }

    /**
     * 获取报送时点信息
     * @param task_id
     * @return
     * @throws Exception
     */
    private Map<String,Object> getTaskBaseType(String task_id) throws Exception{
        Map<String,Object> params = new HashMap<>();
        String sql = "select k.base_type,k.data_type,ifnull(k.inner_submission_time_require,0) as inner_submission_time_require," +
                "ifnull(k.supervise_submission_time_require,0) as supervise_submission_time_require,ifnull(k.data_gener_time_require,0) as data_gener_time_require,k1.sys_ref_table " +
                "from base_submission_time_config k left join base_report_info k1 on k.report_table=k1.report_table " +
                "where k1.task_id='"+task_id+"'";
        List<SqlRow> list = comnDao.findRows(sql);
        if (list.size()>0){
            params.put("base_type",list.get(0).getString("base_type"));
            params.put("data_type",list.get(0).getString("data_type"));
            params.put("sys_ref_table",list.get(0).getString("sys_ref_table"));
            params.put("inner_submission_time_require",list.get(0).getInteger("inner_submission_time_require"));
            params.put("supervise_submission_time_require",list.get(0).getInteger("supervise_submission_time_require"));
        }
        return params;
    }

    /**
     * 获取报送成功条数
     * @param report_table
     * @param theory_report_start_date
     * @return
     * @throws Exception
     */
    private String getTabSucNum(String report_table,String theory_report_start_date) throws Exception{
        String sql = "select count(1) data_num from "+report_table+" where register_status='3' and theory_report_start_date='"+theory_report_start_date+"'";
        int data_num = comnDao.findRows(sql).get(0).getInteger("data_num");
        if (data_num>0){
            return "1";
        }
        return "0";
    }
}