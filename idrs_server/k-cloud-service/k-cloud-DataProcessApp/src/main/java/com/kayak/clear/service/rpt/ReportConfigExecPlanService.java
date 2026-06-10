package com.kayak.clear.service.rpt;

import com.kayak.base.dao.ComnDao;
import com.kayak.clear.chain.FilterChain;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.req.ReportTimeExecPlanInput;
import com.kayak.clear.req.ReportTimeExecPlanOutput;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.clear.service.rpt.execPlan.*;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
public class ReportConfigExecPlanService extends BaseTaskService<PubReq, PubResp> {

    private static Logger log = LoggerFactory.getLogger(ReportConfigExecPlanService.class);
    String workDate = ""; //工作日

    @Autowired
    public CreateTaskService createTaskService;

    @Autowired
    private ComnDao comnDao;

    @Autowired
    private ReportExecPlanFilterFactory filterFactory;

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

    @StepNo(stepNo = 1)
    protected void process(PubReq request) throws Exception {
        log.info("---------- 监管系统报送根据报送时点配置 生成统计执行计划任务 Start -----------");
        String dealDate = request.getTaskDate();
        String nextWorkDate = DateUtil.getAfterDay(dealDate); //获取下一个自然日，因为该清算批量在日切后，而日切后还是使用该工作日，因为要取下一个自然日
        List<ReportTimeExecPlanInput> inputList = queryReportTimeExecPlanList(nextWorkDate); //报送时点配置信息列表
        List<ReportTimeExecPlanOutput> outputList = getExecPlanOutputList(inputList); //计算获取执行计划结果
        int size = commitReportTimeExecPlanList(outputList, nextWorkDate);
        log.info("---------- 监管系统报送根据报送时点配置 生成统计执行计划任务 数量为：" +String.valueOf(size)+" End -----------");
    }

    public void beforeClear(PubReq request) throws Exception{
        //参数初始化
        workDate=request.getTaskDate();
        if("".equals(workDate)||workDate==null){
            throw new Exception("报送工作日不能为空。");
        }
    }

    /**
     * 提交执行计划
     * @param outList 结果集
     * @param workDate
     * @return
     */
    public int commitReportTimeExecPlanList(List<ReportTimeExecPlanOutput> outList, String workDate) throws Exception{
        int result = 0;
        if(outList != null && outList.size() >0){
            String strSql = "delete from report_statistical_exec_plan_info where work_date = $S{workDate}";
            comnDao.update(strSql, workDate);
            for (ReportTimeExecPlanOutput out: outList){
                strSql = "insert into report_statistical_exec_plan_info(report_type,report_table, report_table_name, work_date, base_line_date, start_date, end_date, exec_status, create_time) values " +
                        "($S{reportType}, $S{reportTable}, $S{reportTableName}, $S{workDate}, $S{baseLineDate}, $S{startDate}, $S{endDate}, $S{execStatus}, now())";
                result +=  comnDao.update(strSql, out).getEffect();
            }
        }
        return result;
    }

    public List<ReportTimeExecPlanOutput> getExecPlanOutputList(List<ReportTimeExecPlanInput> reqList) throws Exception{
        FilterChain<ReportTimeExecPlanInput, List<ReportTimeExecPlanOutput>> chains = filterFactory.getFilterChainListFactory();
        List<ReportTimeExecPlanOutput> result = new ArrayList<>();
        if(reqList != null && reqList.size() > 0){
            for(ReportTimeExecPlanInput input: reqList){
                chains.doFilter(input, result,chains);
                chains.reset();
            }
        }
        return result;
    }

    /**
     * 查询报表配置执行计划列表
     * @param workDate 工作日
     * @return
     */
    public List<ReportTimeExecPlanInput> queryReportTimeExecPlanList(String workDate) throws Exception {
        List<ReportTimeExecPlanInput> result = new ArrayList<>();
        String strSql = "select tb1.report_type,tb1.report_table, tb2.table_name report_table_name, tb1.base_type,tb1.data_type,tb1.supervise_submission_time_require,tb1.data_gener_time_require,tb1.inner_submission_time_require,tb1.time_type from base_submission_time_config tb1 inner join base_report_info tb2 on tb1.report_table = tb2.report_table";
        List<SqlRow> rows = comnDao.findRows(strSql);
        if (rows != null && rows.size() > 0) {
            for (SqlRow sqlRow : rows) {
                ReportTimeExecPlanInput item = new ReportTimeExecPlanInput();
                item.setReportType(sqlRow.getString("report_type"));
                item.setReportTable(sqlRow.getString("report_table"));
                item.setReportTableName(sqlRow.getString("report_table_name"));
                item.setBaseType(sqlRow.getString("base_type"));
                item.setDataType(sqlRow.getString("data_type"));
                item.setSuperviseSubmissionTime(sqlRow.getInteger("supervise_submission_time_require")); //监管报送时点要求（天）
                item.setDataGenerTimeRequire(sqlRow.getInteger("data_gener_time_require")); //报送数据生成日期(工作日)
                item.setInnerSubmissionTimeRequire(sqlRow.getInteger("inner_submission_time_require")); //行内报送时点要求（天）
                item.setWorkDate(workDate);
                item.setMacDate(DateUtil.getAfterDay(workDate));
                item.setTimeType(sqlRow.getString("time_type"));
                result.add(item);
            }
        }

        return result;
    }
}
