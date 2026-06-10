package com.kayak.clear.service.rpt;

import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.dps.app.service.BuildReportResultService;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 构建报表配置执行结果服务
 */
@Component
@Scope("prototype")
public class BuildReportConfigExecResultService extends BaseTaskService<PubReq, PubResp> {

    private static Logger log = LoggerFactory.getLogger(BuildReportConfigExecResultService.class);
    String workDate = "";

    @Autowired
    public CreateTaskService createTaskService;

    @Autowired
    protected BuildReportResultService buildReportResultService;

    @Override
    protected void doCheckParams(PubReq request) throws Exception {
        beforeClear(request);
        log.info(" ###### 参数校验 ");
    }

    @StepNo(stepNo = 1)
    protected void process(PubReq request) throws Exception {
        log.info("---------- 监管系统报送根据配置进行结果表自动生成数据处理 Start -----------");
        String dealDate = request.getTaskDate();  //处理日期
        buildReportResultService.buildResult(dealDate); //构建执行结果
        log.info("---------- 监管系统报送根据配置进行结果表自动生成数据处理 End -----------");
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

    public void beforeClear(PubReq request) throws Exception{
        //参数初始化
        workDate=request.getTaskDate();
        if("".equals(workDate)||workDate==null){
            throw new Exception("报送工作日不能为空。");
        }
    }
}
