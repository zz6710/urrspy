package com.kayak.clear.service.rpt;


import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.clear.utils.DateUtils;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.check.service.ReportDataValidateService;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 人行报表报送指标校验
 */
@Component
@Scope("prototype")
public class ZyTwoReportIndexValidationService extends BaseTaskService<PubReq, PubResp> {

    private static Logger log = LoggerFactory.getLogger(ZyTwoReportIndexValidationService.class);

    public static String DELAYED_TYPE = "0";//顺延类型,0-自然日 1-工作日

    @Autowired
    public CreateTaskService createTaskService;
    @Autowired
    public ReportDataValidateService reportDataValidateService;
    @Autowired
    public ComnDao comnDao;

    @Override
    protected void doCheckParams(PubReq request) throws Exception {
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
     * 05-人行自营报表指标校验
     * @param request
     * @throws Exception
     */
    @StepNo(stepNo = 1)
    protected void step1Process(PubReq request) throws Exception {
        String task_id = request.getTaskId();
        log.info("---------- " + task_id + "人行自营报表指标校验:任务 开始执行 -----------");
        String base_date = request.getTaskDate();
        String deal_date = base_date;
        Map<String, String> params = new HashMap<>();
        params.put("isBatch", "01");
        params.put("reportType", "08");//08-自营报表
        params.put("report_freq", "2");//2-二维报表
        //上个月最后一天
        Boolean isLastDayOfMonth = DateUtils.isLastDayOfMonth(base_date);
        if(!isLastDayOfMonth){
            base_date = DateUtils.getLastDayOfLastMonth(base_date);
        }

        int delay_days = Integer.parseInt(SysUtil.getSystemParamsByParaid("zy_delay_days"));
        //校验当前任务日期是否跑批,人行报送数据日期为第一个工作日时
        if (reportDataValidateService.isFitTheDelayDay(base_date, delay_days, deal_date, DELAYED_TYPE)) {
            reportDataValidateService.execute(base_date, params);
        }

        log.info("---------- " + task_id + "人行自营报表指标校验:任务 执行结束 -----------");
    }

}
