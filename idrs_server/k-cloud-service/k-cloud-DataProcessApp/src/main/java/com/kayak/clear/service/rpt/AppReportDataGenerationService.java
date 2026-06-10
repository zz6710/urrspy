package com.kayak.clear.service.rpt;


import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.clear.utils.TaskUtil;
import com.kayak.dps.app.service.ModelDataToGenService;
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
 * 报送任务-人行1-1报表数据生成
 */
@Component
@Scope("prototype")
public class AppReportDataGenerationService extends BaseTaskService<PubReq, PubResp> {

    private static Logger log = LoggerFactory.getLogger(AppReportDataGenerationService.class);

    @Autowired
    public CreateTaskService createTaskService;
    @Autowired
    public ModelDataToGenService modelDataToGenService;
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
     * 坐标类报表应用层数据加工(月度/季度报表)
     * @param request
     * @throws Exception
     */
    @StepNo(stepNo = 1)
    protected void step1Process(PubReq request) throws Exception {
        String task_id = request.getTaskId();
        log.info("---------- " + task_id + "报表数据生成:任务 开始执行 -----------");
        String base_date = request.getTaskDate();
        modelDataToGenService.DwdToAppDataProcessGenMethod(task_id, base_date);

        log.info("---------- " + task_id + "报表数据生成:任务 执行结束 -----------");
    }



}
