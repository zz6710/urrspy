package com.kayak.clear.service.rpt;

import com.kayak.clear.req.CispComplianceInfo;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.dps.check.service.CispComplianceControlManagerService;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
/**
 * Cisp合规内控监测数据生成服务
 */
public class CispComplianceControlBuildDataService extends BaseTaskService<PubReq, PubResp> {
    private static Logger log = LoggerFactory.getLogger(CispComplianceControlBuildDataService.class);
    String workDate = ""; //工作日

    @Autowired
    public CreateTaskService createTaskService;

    @Autowired
    private CispComplianceControlManagerService cispService;

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
        log.info("---------- Cisp合规内控监测数据生成 Start -----------");
        CispComplianceInfo cispData = cispService.getCispComplianceInfo(workDate);
        cispService.delCispComplianceInfo(cispData);
        cispService.addCispComplianceInfo(cispData);
        log.info("---------- Cisp合规内控监测数据生成 End -----------");
    }



    public void beforeClear(PubReq request) throws Exception{
        //参数初始化
        workDate=request.getTaskDate();
        if("".equals(workDate)||workDate==null){
            throw new Exception("报送工作日不能为空。");
        }
    }
}
