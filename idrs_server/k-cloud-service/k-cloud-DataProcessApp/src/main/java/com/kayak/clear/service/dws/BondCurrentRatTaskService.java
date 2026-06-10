package com.kayak.clear.service.dws;

import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.check.service.ReportDataValidateService;
import com.kayak.dps.dws.service.BondNewestRatCompService;
import com.kayak.dps.ods.service.DealPortFileService;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 批处理-所有债券最新评级数据处理逻辑
 */
@Component
@Slf4j
@Scope("prototype")
public class BondCurrentRatTaskService extends BaseTaskService<PubReq, PubResp> {

    @Autowired
    public CreateTaskService createTaskService;
    @Autowired
    public ComnDao comnDao;
    @Autowired
    private BondNewestRatCompService bondNewestRatCompService;

    String workDate = "";

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
     * 债券最新评级处理任务
     * @param request
     * @throws Exception
     */
    @StepNo(stepNo = 1)
    protected void step1Process(PubReq request) throws Exception {
        String task_id = request.getTaskId();
        workDate=request.getTaskDate();

        if("".equals(workDate)||workDate==null){
            throw new Exception("处理日期不能为空。");
        }
        log.info("---------- " + task_id + "债券最新评级处理任务 开始执行 -----------");
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("deal_date", workDate);
        bondNewestRatCompService.DwsBondCurrentRatInfoGen(params);

        log.info("---------- " + task_id + "债券最新评级处理任务 执行结束 -----------");
    }


}
