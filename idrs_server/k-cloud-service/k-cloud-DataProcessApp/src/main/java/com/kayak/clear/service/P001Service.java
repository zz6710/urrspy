package com.kayak.clear.service;

import com.kayak.clear.req.P001Req;
import com.kayak.clear.resp.P001Resp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import com.kayakwise.kcloud.db.Dbtrans;
import com.kayakwise.kcloud.db.util.ParamMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 批处理-步骤Service
 * @author xiamh
 * @date 2022/6/15 16:39
 */
@Component
@Scope("prototype")
public class P001Service extends BaseTaskService<P001Req, P001Resp> {

    private static Logger log = LoggerFactory.getLogger(P001Service.class);

    @Autowired
    public CreateTaskService createTaskService;


    @Override
    protected void doCheckParams(P001Req request) throws Exception {
        log.info(" ###### 参数校验 ");
    }

    @Override
    protected void doCheckBusiness(P001Req request) throws Exception {
        log.info(" ###### 业务校验 ");
    }

    @Override
    protected RegistClearTaskPojo taskRegist(TaskRegeditReq taskRegeditReq) throws Exception {
        log.info(" ###### 任务注册");
        return createTaskService.createSystemTask(taskRegeditReq);
    }

    @StepNo(stepNo = 1)
    protected void process(P001Req request) {
        log.info("---------- P001 Step1 Start -----------");
        log.info(" P001 Request: {}", request);
        log.info("---------- P001 Step1 End -----------");
    }

    @StepNo(stepNo = 2)
    protected void process2(P001Req request) {
        log.info("---------- P001 Step 2 Start -----------");
        log.info(" P002 Request: {}", request);
        log.info("---------- P001 Step 2 End -----------");
    }
}
