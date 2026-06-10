package com.kayak.subject.service;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.subject.model.PubReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//数据加工公共类  --axin 20220719

@Slf4j
@Component
@Scope("prototype")
public class RptBusinessBaseTaskService {

    @Autowired
    public ComnDao comnDao;

    String workDate = "";

    public void dataModeExConvert(PubReq request) throws Exception{
        log.info("---------- 任务: " + request.getTaskId() +" 数据加工开始 Start -----------");
        Instant startTime = Instant.now();
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("deal_date", workDate);
        params.put("CRT_DT", DateUtil.getNowDate());//创建日期
        params.put("UPD_DT", DateUtil.getNowDate());//更新日期
        params.put("CRT_TM", DateUtil.getNowTime());
        params.put("UPD_TM", DateUtil.getNowTime());
        params.put("CRT_DT_TM", DateUtil.getTimestamp14());//创建日期
        params.put("UPD_DT_TM", DateUtil.getTimestamp14());//更新日期
        params.put("YESTERDAY",DateUtil.getupdateoneDate(workDate));
        params.put("TOMORROW",DateUtil.getTomorrowDate(workDate));
        params.put("LAST_3D",DateUtil.add(workDate, "yyyyMMdd", -3));
        params.put("LAST_7D",DateUtil.add(workDate, "yyyyMMdd", -7));
        params.put("LAST_15D",DateUtil.add(workDate, "yyyyMMdd", -15));
        params.put("LAST_30D",DateUtil.add(workDate, "yyyyMMdd", -30));
        params.put("MON_START_DT",DateUtil.getFirstDayDateOfMonth(workDate));
        params.put("MON_END_DT",DateUtil.getLastDayOfMonth(workDate));
        params.put("QUA_START_DT",DateUtil.getMaxOrMinDateOfQuarter(workDate,"min"));
        params.put("QUA_END_DT",DateUtil.getMaxOrMinDateOfQuarter(workDate,"max"));
        params.put("theory_report_start_date",workDate);
        params.put("theory_report_end_date",workDate);
        List<SqlRow> list = ExeQuery.queryPortSqlByTaskId(request.getTaskId());
        Map<String,Object> tparam= getTaskBaseType(request.getTaskId());
        String base_type = "";
        String mark = "0";
        if(tparam.containsKey("base_type")){
            base_type = tparam.get("base_type").toString();
            String data_type = tparam.get("data_type").toString();
            String report_table = tparam.get("sys_ref_table").toString();
            int inner_submission_time_require = Integer.parseInt(tparam.get("inner_submission_time_require").toString());
            int supervise_submission_time_require = Integer.parseInt(tparam.get("supervise_submission_time_require").toString());
            if("02".equals(data_type)){
                params.put("theory_report_start_date",DateUtil.add(workDate,"yyyyMMdd",inner_submission_time_require));
                params.put("theory_report_end_date",DateUtil.add(workDate,"yyyyMMdd",supervise_submission_time_require));
            }else{
                params.put("theory_report_start_date",DateUtil.addSysWordDay(workDate,inner_submission_time_require));
                params.put("theory_report_end_date",DateUtil.addSysWordDay(workDate,supervise_submission_time_require));
            }
            if(!"app_prod_regist_filing_info".equals(report_table)){
                mark=getTabSucNum(report_table,params.get("theory_report_start_date").toString());
            }
        }
        if("0".equals(mark)){
            if("06".equals(base_type)){
                if(workDate.equals(DateUtil.getLastDayOfMonth(workDate))){
                    mark = "0";
                }else{
                    mark = "1";
                }
            }else if("07".equals(base_type)) {
                if(workDate.equals(DateUtil.getMaxOrMinDateOfQuarter(workDate,"max"))){
                    mark = "0";
                }else{
                    mark = "1";
                }
            }else {
                mark = "0";
            }
        }
        if("0".equals(mark)){
            StringBuffer exeid=new StringBuffer();
            try {
                // 初始化线程池
                String newMaximumPoolSize = SysUtil.getSystemParamsByParaid("90000030001");
                String newCorePoolSize = SysUtil.getSystemParamsByParaid("90000030002");
                int size = Integer.parseInt(newMaximumPoolSize);
                int coreSize = Integer.parseInt(newCorePoolSize);
                ExecutorService executorService = new ThreadPoolExecutor(coreSize, size, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());
                for (SqlRow sqlRow:list) {
                    exeid.setLength(0);
                    exeid.append(sqlRow.get("exeid"));
                    log.info("执行语句EXEID[{}]",sqlRow.get("exeid"));
                    String sqlstr = sqlRow.getString("sqlstr");
                    Pattern pattern = Pattern.compile("\\$LIST\\{(\\w+)\\}");
                    Matcher matcher = pattern.matcher(sqlstr);
                    if (matcher.find()) {
                        String paramKey = matcher.group(1); //括号内内容
                        Map<String, Object> param = new HashMap<>();
                        param.put("code", paramKey);
                        List<SqlRow> rows = comnDao.findRows("select * from base_port_sql_param_info where code = $S{code}", param); //sql

                        if(StringUtils.isNotBlank(sqlstr) && sqlstr.toLowerCase().contains("delete")){
                            if (!rows.isEmpty()) {
                                String paramSqlstr = rows.get(0).getString("sqlstr");// 参数的sql查询语句
                                List<SqlRow> paramRows = comnDao.findRows(paramSqlstr, params);
                                for (int i = 0; i < paramRows.size(); i++) {
                                    String sqlstr1 = sqlstr.replace("$LIST{" + paramKey + "}", paramRows.get(i).getString(paramKey));
                                    try {
                                        comnDao.update(sqlstr1, params);
                                    } catch (Exception e) {
                                        log.error(e.getMessage(),e);
                                        throw new RuntimeException(e);
                                    }

                                }
                            }
                        } else {
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
                                            log.error(e.getMessage(),e);
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
                        }
                    } else {
                        comnDao.update(sqlstr, params);
                    }
                }
                executorService.shutdown();
            }catch (Exception e){
                log.error(e.getMessage(),e);
                throw new SQLException("执行SQL["+ exeid +"]报错："+e.getMessage(),e);
            }
        }
        // 获取当前系统时间点
        Instant endTime = Instant.now();
        // 计算时间间隔
        Duration duration = Duration.between(startTime, endTime);
        long seconds = duration.getSeconds();
        log.info("任务{}:用时{}秒", request.getTaskId() ,seconds);
        log.info("---------- 任务: " + request.getTaskId() +" 数据加工结束 End-----------");
    }

    /**
     * Rpt服务数据库分布式锁更新方法
     * @param params
     * @return
     * @throws Exception
     */
    public int upTaskStatus(Map<String, Object> params) throws Exception {
        String sqlstr = "update sys_param set paravalue = $S{paravalue} where paraid = $S{paraid} and paravalue = $S{oldParavalue} ";
        return comnDao.update(sqlstr, params).getEffect();
    }

    /**
     * Rpt服务数据库分布式锁查询方法
     * @param params
     * @return
     * @throws Exception
     */
    public List<SqlRow> quTaskStatus(Map<String, Object> params) throws Exception {
        String sqlstr = "select paravalue from sys_param where paraid = $S{paraid} ";
        return comnDao.findRows(sqlstr, params);
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
                "ifnull(k.supervise_submission_time_require,0) as supervise_submission_time_require," +
                "ifnull(k.data_gener_time_require,0) as data_gener_time_require,k1.sys_ref_table " +
                "from base_submission_time_config k left join base_report_info k1 on k.report_table=k1.report_table " +
                "where k1.task_id='"+task_id+"'";
        List<SqlRow> list = comnDao.findRows(sql);
        if (list.size()>0){
            params.put("base_type",list.get(0).getString("base_type"));
            params.put("data_type",list.get(0).getString("data_type"));
            params.put("sys_ref_table",list.get(0).getString("sys_ref_table"));
            params.put("inner_submission_time_require",list.get(0).getInteger("inner_submission_time_require"));
            params.put("supervise_submission_time_require",list.get(0).getInteger("supervise_submission_time_require"));
            params.put("data_gener_time_require",list.get(0).getInteger("data_gener_time_require"));
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

    /**
     * 清算流程启动之前 调用
     * 验证清算检查前 的 准备工作是否完成
     * 包含数据初始化工作 （axin）
     * @throws Exception
     */
    public void beforeClear(PubReq request) throws Exception{

        //参数初始化
        workDate=request.getTaskDate();

        if("".equals(workDate)||workDate==null){
            throw new Exception("报送工作日不能为空。");
        }

    }
}