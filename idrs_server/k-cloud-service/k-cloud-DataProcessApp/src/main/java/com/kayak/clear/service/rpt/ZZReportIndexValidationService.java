package com.kayak.clear.service.rpt;


import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.clear.utils.TaskUtil;
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
 * 中债一二期报送指标校验
 */
@Component
@Scope("prototype")
public class ZZReportIndexValidationService extends BaseTaskService<PubReq, PubResp> {

    private static Logger log = LoggerFactory.getLogger(ZZReportIndexValidationService.class);

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
     * 02-中债一二期报送指标校验
     * @param request
     * @throws Exception
     */
    @StepNo(stepNo = 1)
    protected void step1Process(PubReq request) throws Exception {
        String task_id = request.getTaskId();
        log.info("---------- " + task_id + "中债一二期报送指标校验:任务 开始执行 -----------");
        String base_date = request.getTaskDate();
        String shouldExecDate = request.getShouldExecDate();
        Map<String, String> params = new HashMap<>();
        params.put("reportType", "02");
        params.put("isBatch", "01");
        params.put("report_freq", "");
        params.put("shouldExecDate", shouldExecDate);
        reportDataValidateService.execute(base_date, params);

        log.info("---------- " + task_id + "中债一二期报送指标校验:任务 执行结束 -----------");
    }



}
