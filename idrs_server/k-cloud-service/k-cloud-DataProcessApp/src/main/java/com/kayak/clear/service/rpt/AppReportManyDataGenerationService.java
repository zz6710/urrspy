package com.kayak.clear.service.rpt;


import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.ExeQuery;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报送任务-人行1-1报表数据生成--多天
 */
@Component
@Scope("prototype")
public class AppReportManyDataGenerationService extends BaseTaskService<PubReq, PubResp> {

    private static Logger log = LoggerFactory.getLogger(AppReportManyDataGenerationService.class);

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
     * 坐标类报表应用层数据加工(月度/季度报表)--多天
     * @param request
     * @throws Exception
     */
    @StepNo(stepNo = 1)
    protected void step1Process(PubReq request) throws Exception {
        String task_id = request.getTaskId();
        Map<String, Object> params =new HashMap<>();
        params.put("deal_date", request.getTaskDate());//处理日期
        log.info("---------- " + task_id + "报表数据生成:任务 开始执行 -----------");
        String sqlCol = ExeQuery.queryExeId("R038DTEU01");
        List<SqlRow> sqlRows = comnDao.findRows(sqlCol,params);
        try {
            for (SqlRow sqlRow : sqlRows) {
                modelDataToGenService.DwdToAppDataProcessRepMethod(task_id, sqlRow.getString("report_dt"));
            }
        }catch (Exception e){
            log.error("报表数据生成:任务执行失败：{}",e.getMessage());
            throw e;
        }
        log.info("---------- " + task_id + "报表数据生成:任务 执行结束 -----------");
    }



}
