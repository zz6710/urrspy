package com.kayak.clear.service.business;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.dps.app.utils.ReportDataGenUtils;
import com.kayak.dps.ods.service.DealPortFileService;
import com.kayak.rpt.Investor.service.InvDataConvertService;
import com.kayak.rpt.datacompare.RptCmpService;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;



@Slf4j
@Component
@Scope("prototype")
public class BusinessBaseTaskLoopService extends BaseTaskService<PubReq, PubResp> {

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

    protected void dataModeLoopConvert(PubReq request) throws Exception{
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
        }

        //计算需要循环的天数
        List<String> rangeList = getRangeDateList(request.getTaskId(), task_date);

        if("0".equals(mark)){
            StringBuffer exeid=new StringBuffer();
            try {
                comnDao.doTrans( () ->{
                    for(String base_date : rangeList){
                        params.put("deal_date", base_date);
                        params.put("theory_report_start_date",base_date);
                        params.put("theory_report_end_date",base_date);

                        /** 判断任务task_id在当前数据日期base_date下是否需要锁表 */
                        if(!ReportDataGenUtils.checkTaskIsLocked(base_date, request.getTaskId())){
                            log.info("清算任务" + request.getTaskId() + "报送数据表已锁定,请解除当前" + base_date + "跑批日期锁定后重试,退出当前清算任务！");
                        } else {
                            for (SqlRow sqlRow:list) {
                                exeid.setLength(0);
                                exeid.append(sqlRow.get("exeid"));
                                log.info("执行语句EXEID[{}]",sqlRow.get("exeid"));
                                comnDao.update(sqlRow.getString("sqlstr"),params);
                            }
                        }
                    }
                });
            }catch (Exception e){
                throw new SQLException("执行SQL["+exeid.toString()+"]报错："+e.getMessage(),e);
            }
        }
        log.info("---------- 任务: " + request.getTaskId() +" 数据加工结束 End-----------");
    }

    /**
     * 清算流程启动之前 调用
     * 验证清算检查前 的 准备工作是否完成
     * 包含数据初始化工作 （axin）
     * @throws Exception
     */
    public void beforeClear(PubReq request) throws Exception {

        //参数初始化
        workDate=request.getTaskDate();

        if("".equals(workDate)||workDate==null){
            throw new Exception("报送工作日不能为空。");
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
     * 根据配置表获取跑批日期List集合
     * @param task_id
     * @param task_date
     * @return
     * @throws Exception
     */
    private List<String> getRangeDateList(String task_id, String task_date) throws Exception {
        List<String> taskDateList = new ArrayList<>();
        String querySql = "";

        String sql = "select * from base_port_sql_loop_config where task_id = '" + task_id + "' ";
        SqlRow res = comnDao.findRow(sql, DataSourceProperty.PUB, null);
        String data_type = res.getString("data_type");//日期类型 01-工作日/02-自然日
        String range_type = res.getString("range_type");//日期范围类型 (01-工作日间、/02-自然日间)
        int range_days = Integer.parseInt(res.getString("range_days"));//范围天数

        if ("01".equals(range_type)) {
            if (range_days == 0) {
                taskDateList.add(task_date);
                return taskDateList;
            }

            String symbol = (range_days < 0)?"<":">";
            String orders = (range_days < 0)?"desc":"asc";
            String selCon = (range_days < 0)?"min":"max";
            if ("02".equals(data_type)) {//工作日范围，选取期间自然日期
                querySql = "select " + selCon + "(b.workday) as work_day from (select workday from sys_workday_set where workday " + symbol + task_date + " order by workday " + orders + " limit " + Math.abs(range_days) + ") b";
                String end_date = comnDao.findRow(querySql, DataSourceProperty.PUB, null).getString("work_day");
                taskDateList = DateUtil.calRangeDaysFromNatureDay(task_date, end_date, data_type);
            } else {
                querySql = "select group_concat(b.workday) as work_day from (select workday from sys_workday_set where workday " + symbol + task_date + " order by workday " + orders + " limit " + Math.abs(range_days) + ") b";
                taskDateList = Arrays.asList(comnDao.findRow(querySql, DataSourceProperty.PUB, null).getString("work_day").split(","));
            }
        } else if ("02".equals(range_type)) {
            querySql = "select date_format(date_sub("+task_date+"), interval " + range_days + " day) as nature_day from dual ";
            String end_date = comnDao.findRow(querySql, DataSourceProperty.PUB, null).getString("nature_day");
            taskDateList = DateUtil.calRangeDaysFromNatureDay(task_date, end_date, data_type);
        }

        return taskDateList;
    }

}