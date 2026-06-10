package com.kayak.clear.service.business;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.dps.app.utils.ReportDataGenUtils;
import com.kayak.dps.ods.service.DealPortFileService;
import com.kayak.rpt.Investor.service.InvDataConvertService;
import com.kayak.rpt.Investor.util.InvDataUtils;
import com.kayak.rpt.datacompare.RptCmpService;
import com.kayak.server.ServerUtil;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//数据加工公共类  --axin 20220719

@Slf4j
@Component
@Scope("prototype")
public class BusinessBaseTaskService  extends BaseTaskService<PubReq, PubResp> {

    @Autowired
    public CreateTaskService createTaskService;
    @Autowired
    private RptCmpService rptCmpService;
    @Autowired
    public ComnDao comnDao;
    @Autowired
    private DealPortFileService dealPortFileService;
    @Autowired
    private InvDataConvertService invDataConvertService;

    String workDate = "";
    String nextWorkDate = "";

    @Override
    protected void doCheckParams(PubReq request) throws Exception {
        beforeClear(request);
        log.info(" ###### 参数校验 ");
    }

    @Override
    protected void doCheckBusiness(PubReq request) throws Exception {
        log.info(" ###### 业务校验 ");
    }

    @Override
    protected RegistClearTaskPojo taskRegist(TaskRegeditReq taskRegeditReq) throws Exception {
        log.info(" ###### 任务注册");
        return createTaskService.createSystemTask(taskRegeditReq);
    }

    /**
     * 数据验证
     */
    protected void dataModeCheck(PubReq request) throws Exception{
        log.info("---------- 任务: " + request.getTaskId() +" 数据验证开始 Start -----------");


        log.info("---------- 任务: " + request.getTaskId() +" 数据验证结束 End-----------");
    }


    protected void dataModeConvert(PubReq request) throws Exception{
        log.info("---------- 任务: " + request.getTaskId() +" 数据加工开始 Start -----------");
        Map<String, Object> params=new HashMap<String, Object>();
        String task_date = workDate;
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
            int data_gener_time_require = Integer.parseInt(tparam.get("data_gener_time_require").toString());
            String report_date = DateUtil.calReportDateByDateType(workDate, base_type, data_gener_time_require);//计算报送数据日期
            if("02".equals(data_type)){//日期类型: 01-工作日 02-自然日
                params.put("theory_report_start_date",DateUtil.add(workDate,"yyyyMMdd",inner_submission_time_require));
                params.put("theory_report_end_date",DateUtil.add(workDate,"yyyyMMdd",supervise_submission_time_require));
                workDate = DateUtil.add(workDate,"yyyyMMdd", data_gener_time_require);
            }else{
                params.put("theory_report_start_date",DateUtil.addSysWordDay(workDate,inner_submission_time_require));
                params.put("theory_report_end_date",DateUtil.addSysWordDay(workDate,supervise_submission_time_require));
                workDate = DateUtil.addSysWordDay(workDate, data_gener_time_require);
                nextWorkDate = DateUtil.addSysWordDay(report_date, -1*data_gener_time_require);
            }

            params.put("settle_date", report_date);
            if(!"app_prod_regist_filing_info".equals(report_table)){
                mark=getTabSucNum(report_table,params.get("theory_report_start_date").toString());
            }

        } else {
            /**锁表功能需要用到 settle_date，处理task_date获取最近一个自然日作为数据日期*/
            // TODO 后续可以做配置化处理
            if(workDate.equals(DateUtil.getLastDayOfMonth(workDate))){
                params.put("settle_date", DateUtil.getLastCycleDay(workDate,0,1));
            }else{
                params.put("settle_date", DateUtil.getLastCycleDay(workDate,-1,1));
            }

        }

        /** 判断任务task_id在当前数据日期settle_date下是否需要锁表 */
        if(!ReportDataGenUtils.checkTaskIsLocked(String.valueOf(params.get("settle_date")), request.getTaskId())){
            log.info("清算任务" + request.getTaskId() + "报送数据表已锁定,请解除当前" + params.get("settle_date") + "跑批日期锁定后重试,退出当前清算任务！");
            return;
        }

        if("0".equals(mark)){
            if("06".equals(base_type)){
                if(nextWorkDate.equals(task_date)){
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
                comnDao.doTrans( () ->{
                    for (SqlRow sqlRow:list) {
                        exeid.setLength(0);
                        exeid.append(sqlRow.get("exeid"));
                        log.info("执行语句EXEID[{}]",sqlRow.get("exeid"));
                        comnDao.update(sqlRow.getString("sqlstr"),params);
                    }
                });
            }catch (Exception e){
                throw new SQLException("执行SQL["+exeid.toString()+"]报错："+e.getMessage(),e);
            }
        }
        log.info("---------- 任务: " + request.getTaskId() +" 数据加工结束 End-----------");
    }

    protected void dataModeExConvert(PubReq request) throws Exception{
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

                        if(StringUtils.isNotBlank(sqlstr) && (sqlstr.toLowerCase().contains("delete") || sqlstr.toLowerCase().contains("update"))){
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

    // 子份额-净值日历表
    public void dataModeNavCalConvert(PubReq request) throws Exception{
        log.info("---------- 任务: " + request.getTaskId() +" 生成净值日历表开始 Start-----------");
        String taskDate = request.getTaskDate().replace("-","");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(df.parse(taskDate));
        cal1.add(Calendar.DATE, 1);
        String sysDate = taskDate;
        String sysDateAdd1 = df.format(cal1.getTime());
        // 删除当前日期净值数据
        String sqlDel = "DELETE FROM dwd_nav_cal_info WHERE nav_date=" + sysDate;
        comnDao.update(sqlDel);
        String sqlExecId = "select exeid, sqlStr from base_port_sql_info where exeid  = 'TEMP07'";
        List<SqlRow> rsExecId = comnDao.findRows(sqlExecId);
        String sqlProd = null;
        if (rsExecId != null && rsExecId.size() > 0) {
            sqlProd = rsExecId.get(0).getString("sqlStr");
        }
        Map<String, Object> params1 = new HashMap<>();
        params1.put("deal_date", taskDate);
        List<SqlRow> rsProd = comnDao.findRows(sqlProd, params1);
        // T-1 上一工作日
        String sqlLastWorkDay = "select workday from (select row_number() over (order by workday desc) as rn, workday from sys_workday_set  where pgmno = '001' and workday < " + sysDate + "  ) t where rn = 1";
        List<SqlRow> rsLastWorkDay = comnDao.findRows(sqlLastWorkDay);
        String lastWorkDay = rsLastWorkDay.get(0).getString("workday");
        // T+1, T+2 工作日
        String sqlTn = "select workday from (select row_number() over (order by workday asc) as rn, workday from sys_workday_set  where pgmno = '001' and workday > " + sysDate + "  ) t where rn < 3";
        List<SqlRow> rsT1 = comnDao.findRows(sqlTn);
        String workDayT1 = rsT1.get(0).getString("workday");
        String workDayT2 = rsT1.get(1).getString("workday");
        try {
            comnDao.doTrans(()->{
                // 判断sysDate是否是工作日
                String sqlWorkday = "SELECT workday FROM sys_workday_set WHERE workday =" + sysDate;
                List<SqlRow> rsWorkday = comnDao.findRows(sqlWorkday);
                String workDay = null;
                if (rsWorkday != null && rsWorkday.size() > 0) {
                    workDay = rsWorkday.get(0).getString("workday");
                }
                // 后一天是否是工作日
                Calendar cal = Calendar.getInstance();
                cal.setTime(df.parse(sysDate));
                cal.add(Calendar.DATE, 1);
                String nextDay = df.format(cal.getTime());
                String sqlNextDay = "SELECT workday FROM sys_workday_set WHERE workday =" + nextDay;
                List<SqlRow> rsNextDay = comnDao.findRows(sqlNextDay);
                String isLastHoliday = "0";
                if (rsNextDay != null && rsNextDay.size() > 0) {
                    isLastHoliday = "1";
                }
                log.info("---------- 产品条数prodSize: "+ rsProd.size() +"--------------------------------------------------------");
                for (SqlRow row : rsProd) {
                    log.info("---------- for任务开始: --------------------------------------------------------");
                    // 产品类型
                    String prodType = row.getString("OPN_TYP");
                    String baseDateProd = null;
                    if ("2".equals(prodType)) { // 定开
                        // 产品基准日,应该在当前系统工作日到下一个工作日区间找到工作日
                        String sqlBaseDate = "select PRD_CD, DT_STR  from dwd_pty_prod_cal_sub where PRD_CD = '" + row.getString("PROD_CODE") + "' and DT_STR <= '" + workDayT1 + "' order by DT_STR desc limit 10";
                        List<SqlRow> rsBaseDate = comnDao.findRows(sqlBaseDate);
                        if (rsBaseDate != null && rsBaseDate.size() > 0) {
                            String cfDateProd = rsBaseDate.get(0).getString("DT_STR");
                            String sqlBaseDateDay = "select workday from (select row_number() over (order by workday desc) as rn, workday from sys_workday_set  where pgmno = '001' and workday < " + cfDateProd + "  ) t where rn = 1";
                            List<SqlRow> rsBaseDateDay = comnDao.findRows(sqlBaseDateDay);
                            if (rsBaseDateDay!=null && rsBaseDateDay.size() > 0) {
                                baseDateProd = rsBaseDateDay.get(0).getString("workday");
                            }
                        }
                    }
                    log.info("---------- for任务-产品基准日: " + baseDateProd + "--------------------------------------------------------");
                    Map<String, Object> params = new HashMap<>();
                    params.put("sonShareCode", row.getString("PROD_CODE"));
                    params.put("navDate", sysDate); // 净值日期
                    params.put("lst_wkd", lastWorkDay); // 净值日期的上一工作日
                    String tsf_dt = row.getString("transfer_dt");
                    params.put("tsf_dt", tsf_dt); //迁移日期
                    params.put("is_prod_transfer", row.getString("is_prod_transfer")); //是否迁移产品
                    params.put("establishDate", row.getString("ESTABLISH_DATE")); // 产品成立日
                    params.put("endDate", row.getString("END_DATE")); // 产品到期日
                    //清算1
                    //select prod_code_s,hold_date,sum(hold_vol) as hold_vol from app_cust_vol_register_sub_info where hold_date= '' group by prod_code_s,hold_date
                    //app_prod_sub_vol_info ：： prod_code_s,hold_date ,hold_vol
                    //计算迁移日上一个工作日
                    if(tsf_dt.length() > 1) {
                        String date_query = "select max(workday) as lst_tsf_dt from sys_workday_set where workday < '" + tsf_dt + "'";
                        params.put("lst_tsf_dt", comnDao.findRow(date_query, DataSourceProperty.PUB, null).getString("lst_tsf_dt"));
                    }

                    //select * from app_prod_sub_vol_info where prod_code_s = '' and hold_date = '成立日0'--不存在，则把这个子产品代码+成立日的记录写入 dwd_subprod_0vol_remark ：：子产品代码、成立日、成立日份额
                    if(StringUtils.isNotBlank(row.getString("ESTABLISH_DATE"))){
                        if(taskDate.equals(row.getString("ESTABLISH_DATE").replace("-",""))){
                            String sqlSubVol = "select hold_vol from app_prod_sub_vol_info where prod_code_s = '" + row.getString("PROD_CODE") + "' and hold_date = '" + row.getString("ESTABLISH_DATE") + "'";
                            List<SqlRow> rsSubVol = comnDao.findRows(sqlSubVol);
                            boolean is0 = false;
                            if (rsSubVol != null && rsSubVol.size() > 0) {
                                if(String.valueOf(rsSubVol.get(0).get("hold_vol")).compareTo("0") == 0){
                                    is0 = true;
                                }
                            }else{
                                is0 = true;
                            }
                            if(is0){
                                Map<String, Object> paramsVol = new HashMap<>();
                                paramsVol.put("prodCodeS", row.getString("PROD_CODE"));
                                paramsVol.put("establishDate", row.getString("ESTABLISH_DATE"));
                                paramsVol.put("holdVol", BigDecimal.valueOf(0));
                                String sql = "replace into dwd_subprod_0vol_remark (prod_code_s, establish_date, hold_vol) values " +
                                        " ($S{prodCodeS}, $S{establishDate}, $D{holdVol}) ";
                                comnDao.update(sql, paramsVol);
                            }
                        }
                    }


                    // 产品类型（OPN_TYP）：1-现金管理类 2-定开 3-固定持有期 5-封闭式 7-T+1定开 9-最短持有期*/
                    // 估值依据（navCalType）：01 当日估值表结果02 取上一估值日结果
                    if ("5".equals(prodType)) { // 封闭式
                        if (workDay == null) { // 节假日
                            params.put("evaDate", lastWorkDay);
                            params.put("navCalType", "02");
                        } else {
                            params.put("evaDate", sysDate);
                            params.put("navCalType", "01");
                        }
                        if (sysDate.equals(row.getString("END_DATE"))) {  // 产品到期日：T+1
                            params.put("reportDate", workDayT1);
                        } else {
                            params.put("reportDate", workDayT2);
                        }
                    } else if ("2".equals(prodType)) { // 定开
                        if (workDay == null) { // 节假日
                            params.put("evaDate", lastWorkDay);
                            params.put("navCalType", "02");
                        } else {
                            params.put("evaDate", sysDate);
                            params.put("navCalType", "01");
                        }
                        if (params.get("evaDate").equals(baseDateProd)) {  // 基准日：T+1
                            params.put("reportDate", workDayT1);
                        } else {
                            params.put("reportDate", workDayT2);
                        }
                        params.put("jzDate", baseDateProd);
                    } else if ("7".equals(prodType) || "9".equals(prodType)) { // T+1:7-T+1定开   9-最短持有期
                        if (workDay == null) { // 节假日
                            params.put("evaDate", lastWorkDay);
                            params.put("navCalType", "02");
                        } else {
                            params.put("evaDate", sysDate);
                            params.put("navCalType", "01");
                        }
                        params.put("reportDate", workDayT1);
                    } else if ("3".equals(prodType)) { // 固定持有期
                        if (workDay != null || (workDay == null && "1".equals(isLastHoliday))) { // 工作日或节假日最后一天
                            params.put("evaDate", sysDate);
                            params.put("navCalType", "01");
                        } else {
                            params.put("evaDate", lastWorkDay);
                            params.put("navCalType", "02");
                        }
                        params.put("reportDate", workDayT1);
                    } else if ("1".equals(prodType)) { // 现金管理类
                        params.put("evaDate", sysDate);
                        params.put("navCalType", "01");
                        params.put("reportDate", workDayT1);
                    }
                    // 月末最后一个自然日，估值依据为01
                    String settle_date = DateUtil.getPreviousMonthLastDay(sysDate, 1);
                    if (sysDate.equals(settle_date)) {
                        params.put("evaDate", sysDate);
                        params.put("navCalType", "01");
                    }

                    String sqlNav = "select sum(tot_lot) as tot_lot from (" +
                            " select  ifnull(e.tot_lot,0) as tot_lot  from dwd_prd_prd_nav_inf e where nav_dt=" + sysDate + " and prod_cd='"+row.getString("PROD_CODE") +"' " +
                            " union" +
                            " select  ifnull(f.tot_lot,0) as tot_lot  from dwd_prd_prd_nav_inf_sub f where nav_dt=" + sysDate + " and prod_cd='"+row.getString("PROD_CODE") +"' ) t1 ";

                    List<SqlRow> rsNav00 = comnDao.findRows(sqlNav);
                    boolean isExistsVol = true;
                    if (rsNav00 != null && rsNav00.size() > 0) {
                        if (rsNav00.get(0).getBigDecimal("tot_lot").compareTo(BigDecimal.ZERO) == 0) {
                            isExistsVol = false;
                        }else {
                            isExistsVol = true;
                        }
                    }else{
                        isExistsVol = false;
                    }

                    if(!isExistsVol){
                        String navTable = "dwd_prd_prd_nav_inf";
                        if (row.containsKey("mother_fund_flag") && row.getString("mother_fund_flag").equals("2")) {
                            navTable = "dwd_prd_prd_nav_inf_sub";
                        }
                        String sqlNav1 = "select prod_cd, nav_dt, UNT_NAV_RH, ACM_NAV_RH, TOT_LOT from " + navTable + " where nav_dt<=" + sysDate + " and TOT_LOT>0 and prod_cd='"+row.getString("PROD_CODE") +"' order by nav_dt desc limit 1";
                        List<SqlRow> rsNav1 = comnDao.findRows(sqlNav1);
                        if (rsNav1 != null && rsNav1.size() > 0) {
                            params.put("navCalType", "02");
                            params.put("evaDate", rsNav1.get(0).get("nav_dt"));
                        }else{
                            if (row.containsKey("ESTABLISH_DATE") && StringUtils.isNotBlank(row.getString("ESTABLISH_DATE")) ) {
                                if(row.getString("ESTABLISH_DATE").equals(sysDate)){
                                     params.put("navCalType", "01");
                                }else{
                                     params.put("navCalType", "02");
                                }
                                params.put("evaDate", row.getString("ESTABLISH_DATE"));
                            }
                        }
                    }



                    log.info("---------- for任务-prodType判断结束: " + prodType + "--------------------------------------------------------");
                    // OPN_TYP in ('5','2','7','3','1','9') 产品类型
                    if ("527319".contains(prodType)) {
                        params.put("opnTyp", prodType);
                        // 插入净值日历表
                        String sql = "insert into dwd_nav_cal_info (son_share_code,nav_date,eva_date,report_date,nav_cal_type,establish_date,end_date, jz_date, opn_typ, lst_wkd, is_prod_transfer, tsf_dt, lst_tsf_dt) values " +
                                " ($S{sonShareCode}, $S{navDate}, $S{evaDate}, $S{reportDate}, $S{navCalType}, $S{establishDate}, $S{endDate}, $S{jzDate}, $S{opnTyp}, $S{lst_wkd}, $S{is_prod_transfer}, $S{tsf_dt}, $S{lst_tsf_dt}) ";
                        comnDao.update(sql, params);
                    }
                }
                log.info("---------- for任务-end: --------------------------------------------------------");
            });
        } catch (Exception e) {
            log.error("---------- 任务: " + request.getTaskId() +" 生成净值日历表失败: "+ e.getMessage() +" Continue-----------");
        }
        log.info("---------- 任务: " + request.getTaskId() +" 生成净值日历表结束 End-----------");
    }
    // 生成净值信息
    public void dataModeNavInfoConvert(PubReq request) throws Exception{
        log.info("---------- 任务: 生成净值信息表开始 Start-----------");
        String taskDate = request.getTaskDate().replace("-","");
        try {
            comnDao.doTrans(()-> {
                Map<String, Object> params = new HashMap<>();
                params.put("deal_date", taskDate);
                // 任务sql配置数据
                String sqlTask = "select exeid, sqlStr, exe_order from base_port_sql_info where task_id = 'R121' order by exe_order asc";
                List<SqlRow> rsTask = comnDao.findRows(sqlTask);
                if (rsTask != null && rsTask.size() > 0) {
                    for (SqlRow row : rsTask) {
                        String sqlTaskStr = row.getString("sqlStr");
                        comnDao.update(sqlTaskStr, params);
                        log.info("---------- 任务: "+row.getString("exeid")+" "+sqlTaskStr+"-----------");
                    }
                }
            });
        } catch (Exception e) {
            log.error("---------- 任务:  生成净值信息失败: "+ e.getMessage() +" Continue-----------");
        }
    }
    // 复权净值
    public void dataModeFqNavConvert(PubReq request) throws Exception{
        log.info("---------- 任务: " + request.getTaskId() +" 生成净复权净值开始 Start-----------");
        try {
            String taskDate = request.getTaskDate().replace("-","");
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(df.parse(taskDate));
            cal1.add(Calendar.DATE, 1);
            String sysDate = df.format(cal1.getTime());
            String sqlProd = "select a.son_share_code, a.nav_date, b.eva_date, a.report_date, a.nav_cal_type, a.share, a.total_nav, c.mother_fund_flag, d.opn_typ " +
                    " , c.establish_date, c.real_end_date as end_date, ifnull(ifnull(e.tot_lot,f.tot_lot),0) as tot_lot " +
                    " from app_nav_info_reg a " +
                    "left join dwd_nav_cal_info b on a.son_share_code=b.son_share_code " +
                    "left join ods_prod_base_info c on a.son_share_code=c.prod_code " +
                    "left join stg_t02_prdc_infr d on a.son_share_code=d.prdc_cd  " +
                    "left join dwd_prd_prd_nav_inf e on a.son_share_code=e.PROD_CD and e.NAV_DT = a.nav_date " +
                    "left join dwd_prd_prd_nav_inf_sub f on a.son_share_code=f.PROD_CD and f.NAV_DT = a.nav_date " +
                    "where a.nav_date=b.nav_date and a.report_date="+sysDate;

            List<SqlRow> rsProd = comnDao.findRows(sqlProd);
            comnDao.doTrans(()->{
                for (SqlRow row : rsProd) {
                    log.info("---------- 产品: " + row.getString("son_share_code") +" 查询复权净值开始 Start-----------");
                    Map<String, Object> params = new HashMap<>();
                    String sonShareCode = row.getString("son_share_code");
                    String navDate = row.getString("nav_date");
                    String evaDate = row.getString("eva_date"); // 估值日期
                    String navCalType = row.getString("nav_cal_type"); // 估值依据 01 当日估值 02 取上一日估值
                    BigDecimal totalNav = null;
                    if (!StringUtils.isBlank(row.getString("total_nav"))) {
                        totalNav = row.getBigDecimal("total_nav");
                    }
                    params.put("sonShareCode", sonShareCode);
                    params.put("navDate", navDate);
                    params.put("evaDate", evaDate);

                    params.put("establishDate", row.getString("establish_date"));
                    params.put("endDate", row.getString("end_date"));

                    // 分级产品标志
                    String motherFunFlag = row.getString("mother_fund_flag");
                    String navTable = "dwd_prd_prd_nav_inf";
                    if (motherFunFlag.equals("2")) {
                        navTable = "dwd_prd_prd_nav_inf_sub";
                    }

                    //代码增加判断0份额产品的逻辑：
                    //根据sonShareCode + navDate 查询净值数据，查询结果 <= navDate的无数据或都是0  同时存在 在dwd_subprod_vol_remark中，conntinue;
                    //select * from  dwd_prd_prd_nav_inf where sonShareCode ='' and 净值表中的<= navDate  and 净值 >0 or
                    //select * from  dwd_prd_prd_nav_inf_sub where sonShareCode ='' and 净值表中的<= navDate  and 净值 >0  找不到记录的产品代码+日期，去dwd_subprod_0vol_remark查询，有记录，直接删除这一笔净值记录
                    if (row.getBigDecimal("tot_lot").compareTo(BigDecimal.ZERO) == 0) {
                        String sqlNav1 = "select prod_cd, nav_dt, UNT_NAV_RH, ACM_NAV_RH, TOT_LOT from " + navTable + " where nav_dt<=" + navDate + " and TOT_LOT>0 and prod_cd='"+sonShareCode+"' order by nav_dt desc";
                        List<SqlRow> rsNav1 = comnDao.findRows(sqlNav1);
                        if (rsNav1 != null && rsNav1.size() > 0) {
                            params.put("vol0Flag", "02");
                            //有净值之后的0份额，不删除数据，估值依据为02 估值日期=非0的最大一条，且净值数据字段都=非0最大一条，披露日期为空
//                            navCalType = "02";
//                            String sqlUpd = "update app_nav_info_reg set nav_cal_type=$S{navCalType},nav=$D{nav},rmb_nav=nav,total_nav=$D{totalNav}," +
//                                    "rmb_total_nav=total_nav,share=$D{share},remain_bal=$D{remainBal},rmb_remain_bal=remain_bal " +
//                                    "  where son_share_code=$S{sonShareCode} and nav_date=$S{navDate}";
//                            Map<String, Object> paramsUpd = new HashMap<>();
//                            paramsUpd.put("navDate", navDate);
//                            paramsUpd.put("navCalType", navCalType);
//                            paramsUpd.put("sonShareCode", sonShareCode);
//                            paramsUpd.put("nav", rsNav1.get(0).getBigDecimal("UNT_NAV_RH"));
//                            paramsUpd.put("totalNav", rsNav1.get(0).getBigDecimal("ACM_NAV_RH"));
//                            paramsUpd.put("share", rsNav1.get(0).getBigDecimal("TOT_LOT"));
//                            paramsUpd.put("remainBal", rsNav1.get(0).getBigDecimal("UNT_NAV_RH").multiply(rsNav1.get(0).getBigDecimal("TOT_LOT")));
//                            totalNav =  rsNav1.get(0).getBigDecimal("ACM_NAV_RH");
//                            comnDao.update(sqlUpd, paramsUpd);
                        } else {
                            String sqlVol0 = "select prod_code_s, establish_date, hold_vol from dwd_subprod_0vol_remark where prod_code_s='" + sonShareCode + "'";
                            List<SqlRow> rsVol0 = comnDao.findRows(sqlVol0);
                            if (rsVol0 != null && rsVol0.size() > 0) {
                                params.put("vol0Flag", "01");
                                // String sqlDel = "delete from app_nav_info_reg where son_share_code='" + sonShareCode + "' and nav_date=" + navDate;
                                // comnDao.update(sqlDel);
                                // continue;
                            }
                        }
                    } else {
                        params.put("vol0Flag", "03"); // 01-产品成立0份额 02-产品存续0份额 03-产品非0份额
                    }

                    // 披露日期
//                    if (navCalType != null && "02".equals(navCalType)) {
//                        params.put("disclosureDate", null);
//                    } else {
//                        String sqlPl = "select t.prdc_cd, t.rls_dt, t.MA_DIS_DT  from (select  PRDC_CD,MA_DIS_DT,rls_dt, row_number() over (partition by PRDC_CD order by MA_DIS_DT asc) rn from dwd_t05_tb_prd_dly where rls_dt="+ evaDate +" and prdc_cd='"+sonShareCode+"') t where rn=1";
//                        List<SqlRow> rspl = comnDao.findRows(sqlPl);
//                        if (rspl != null && rspl.size() > 0) {
//                            params.put("disclosureDate", rspl.get(0).getString("MA_DIS_DT"));
//                        }
//                    }
                    // 产品类型 1-现金管理类 2-定开 3-固定持有期 5-封闭式 7-T+1定开 9-最短持有期
                    String prodType = row.getString("opn_typ");

                    String sqlShare = "select prdc_cd, CNFR_DT, PER_UNT_DVDN from dwd_idx_div_detl_prdc_lat where CNFR_DT <= '" + evaDate + "' and prdc_cd = '" + sonShareCode +"'";
                    List<SqlRow> rsShare = comnDao.findRows(sqlShare);

                    BigDecimal fqNav = BigDecimal.valueOf(1);
                    Boolean fqNavInit = true;

                    if ("1".equals(prodType)) {
                        fqNav = BigDecimal.valueOf(1);
                        fqNavInit = false;
                    } else if ("25".contains(prodType)) {
                        if (rsShare != null && rsShare.size() > 0) {
                            for (SqlRow row1 : rsShare) {
                                // 第n次分红除权前单位净值：使用早于第n次分红确认日的最近 一个“01当日估值表结果”的净值数据
                                String cnDt = row1.getString("CNFR_DT");
                                //                            String sqlNavN = "select t.son_share_code, t.nav_date, t.eva_date from (select son_share_code, nav_date, eva_date," +
                                //                                    " row_number() over (partition by son_share_code order by eva_date desc) rn" +
                                //                                    " from dwd_nav_cal_info where son_share_code='" + sonShareCode + "' and eva_date <=" + cnDt + " and nav_cal_type = '01') t where rn=1";
                                //                            List<SqlRow> rsNavN = comnDao.findRows(sqlNavN);
                                //                            // 查询净值
                                //                            if (rsNavN != null && rsNavN.size() > 0) {

                                String sqlNav = "select prod_cd, nav_dt, UNT_NAV_RH from "+ navTable +" where prod_cd='"+sonShareCode+"' " +
                                        " and nav_dt<="+cnDt + " order by nav_dt desc limit 1";
                                List<SqlRow> rsNav = comnDao.findRows(sqlNav);
                                if (rsNav != null && rsNav.size() > 0) {
                                    BigDecimal nav = rsNav.get(0).getBigDecimal("UNT_NAV_RH"); // 净值
                                    BigDecimal perUntDvdn = row1.getBigDecimal("PER_UNT_DVDN"); // 分红
                                    Map<String, Object> params1 = new HashMap<>();
                                    params1.put("perUntDvdn", perUntDvdn);
                                    params1.put("nav", nav);
                                    BigDecimal fqFactor = nav.divide(nav.subtract(perUntDvdn), 20, RoundingMode.HALF_UP);
                                    params1.put("fqFactor", fqFactor);
                                    fqNav = fqNav.multiply(fqFactor);
                                    fqNavInit = false;
                                    log.info("---------- 单位分红: " + perUntDvdn + "----------");
                                    log.info("------------- 净值: " + nav + "----------");
                                    log.info("------- 单个复权因子: " + fqFactor + " nav/(nav-perUntDvdn)----------");
                                }
                                log.info("---------- 复权净值: " + fqNav +"----------");
                            }
                        } else {
                            fqNav = totalNav;
                            if (totalNav != null) {
                                fqNavInit = false;
                            }
                        }
                    } else {
                        fqNav = totalNav;
                        if (totalNav != null) {
                            fqNavInit = false;
                        }
                    }
                    if (fqNavInit == true) {
                        params.put("fqNav", null);
                    } else {
                        // 产品类型（OPN_TYP）：1-现金管理类 2-定开 3-固定持有期 5-封闭式 7-T+1定开 9-最短持有期*/
                        if ("35".contains(prodType)) {
                            fqNav = fqNav.setScale(6, BigDecimal.ROUND_HALF_UP);
                        } else {
                            fqNav = fqNav.setScale(4, BigDecimal.ROUND_HALF_UP);
                        }
                        params.put("fqNav", fqNav);
                    }
                    log.info("---------- 产品: " + row.getString("son_share_code") +" 查询复权净值结束 End-----------");
                    // 更新复权净值，披露日期
                    String sql = "update app_nav_info_reg set FQ_NAV=$D{fqNav}, RMB_FQ_NAV=$D{fqNav}, vol_zero_flag=$S{vol0Flag} where son_share_code=$S{sonShareCode} and nav_date=$S{navDate}";
                    comnDao.update(sql, params);
                }
            });
        } catch (Exception e) {
            log.error("---------- 任务: " + request.getTaskId() +" 生成复权净值失败: "+ e.getMessage() +" Continue-----------");
        }
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
    public void doFlowConvert(PubReq request) {
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

    /**
     * 投资者三期数据核对
     * @throws Exception
     */
    public void doInvConvert(PubReq request) {
        log.info("---------- 任务: " + request.getTaskId() +" 清算任务开始 Start-----------");

        new Thread(() -> {
            try {
                log.info("---------- 任务: " + request.getTaskId() +" 投资三期数据核对开始 Start-----------");

                String tables = "app_cust_register_info";
                String report_date = SysUtil.getSystemParamsByParaid("10004");

                if ("R106".equals(request.getTaskId())) {
                    tables = "app_cust_vol_register_info,app_cust_trans_info";
                }
                if (StringUtils.isNotEmpty(tables) && StringUtils.isNotEmpty(report_date)) {
                    String[] tableArr = tables.split(",");
                    for (String table_name : tableArr) {
                        rptCmpService.investorCompare(table_name, report_date);
                    }
                }
            } catch (Exception e) {
                log.error("---------- 任务: " + request.getTaskId() +" 投资三期数据核对失败: "+ e.getMessage() +" End-----------");
                e.getMessage();
            } finally {
                log.info("---------- 任务: " + request.getTaskId() +" 投资三期数据核对结束 End-----------");
            }
        }).start();

        log.info("---------- 任务: " + request.getTaskId() +" 清算任务结束 Start-----------");
    }

    /**
     * 处理投资者身份数据生僻字处理
     */
    public List<Map<String, String>> doRareCharacterHandle () {
        Map<String, Object> params = new HashMap<>();
        params.put("deal_date", workDate);
        List<Map<String,String>> invList = null;
        try {
            invList = invDataConvertService.getInvDataMap(params);
            return InvDataUtils.getNewChinese(invList);
        } catch (Exception e) {
            log.info("处理投资者三期身份信息生僻字异常：" + e.getMessage());
            return invList;
        }
    }

    /**
     * 更新投资者生僻字
     * @param newInvList
     */
    public void doRareCharacterUpdate (List<Map<String, String>> newInvList) {
        try{
            for (Map<String, String> invMap : newInvList) {
                invDataConvertService.doUpdateInvName(invMap);
            }
        } catch (Exception e) {
            log.info("更新投资者生僻字异常：" + e.getMessage());
        }
    }

}