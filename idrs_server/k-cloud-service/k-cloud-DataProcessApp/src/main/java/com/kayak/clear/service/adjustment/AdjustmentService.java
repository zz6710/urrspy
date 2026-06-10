package com.kayak.clear.service.adjustment;

import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.dps.adjustment.service.DealAdjustmentService;
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
 * 子公司月度报表调差处理
 * @author yangdi
 */
@Component
@Scope("prototype")
public class AdjustmentService extends BaseTaskService<PubReq, PubResp> {
    private static Logger log = LoggerFactory.getLogger(AdjustmentService.class);

    @Autowired
    public CreateTaskService createTaskService;

    @Autowired
    private DealAdjustmentService dealAdjustmentService;

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

    @StepNo(stepNo = 1)
    protected void process(PubReq request) throws Exception {
        log.info("---------- 产品存续情况报表调差处理 Start -----------");
        log.info(request.getTaskId() + " Request: {}", request);
        Map<String, Object> params =new HashMap<>();
        //处理日期
        params.put("dealDate", request.getTaskDate());
        dealAdjustmentService.adjust(params);
        log.info("---------- 产品存续情况报表调差处理 End -----------");
    }
}
