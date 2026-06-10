package com.kayak.clear.service.rpt;

import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.config.constants.STGConstants;
import com.kayak.dps.app.service.DealBaseReportResultService;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 批处理-报表报送结果表数据入库
 * @author zhaojie
 * @date 2023/2/27
 */
@Component
@Scope("prototype")
public class BaseReportResultService extends BaseTaskService<PubReq, PubResp> {

    private static Logger log = LoggerFactory.getLogger(BaseReportResultService.class);

    @Autowired
    public CreateTaskService createTaskService;

    @Autowired
    private DealBaseReportResultService dealBaseReportResultService;

    String workDate = "";

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
        log.info("---------- 监管系统报送结果表自动生成数据处理 Start -----------");
        log.info(request.getTaskId() + " Request: {}", request);
        Map<String, Object> params =new HashMap<>();
        params.put("dealDate", request.getTaskDate());
        params.put("dealType", STGConstants.STG_DATA_HANDLE_DAYS);
        if(StringUtils.isNotEmpty(request.getTaskId())){
            params.put("pId", request.getTaskId());
        }
        dealBaseReportResultService.dealAllPortInfo(params);
        log.info("---------- 监管系统报送结果表自动生成数据处理 End -----------");
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
