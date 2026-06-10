package com.kayak.clear.service.pub;

import com.kayak.clear.constants.BatchTaskStatus;
import com.kayak.clear.constants.GlobalContents;
import com.kayak.clear.constants.RunningTypeContents;
import com.kayak.clear.dto.KbatchClearTaskInfoDto;
import com.kayakwise.kcloud.batch.exception.TransException;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.entity.KbatchTaskExec;
import com.kayakwise.kcloud.batch.model.entity.KbatchTaskStep;
import com.kayakwise.kcloud.batch.model.entity.KbatchTaskStepExec;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.db.Dbop;
import com.kayakwise.kcloud.db.SqlResult;
import com.kayakwise.kcloud.db.util.ParamMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建批量任务执行表数据的相关操作
 * @author xiamh
 * @date 2022/6/17 15:40
 */
@Service
public class CreateTaskService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Dbop dbop;

    /**
     * 创建系统任务
     * @param  taskRegeditReq
     * @return RegistClearTaskPojo
     */
    public RegistClearTaskPojo createSystemTask(TaskRegeditReq taskRegeditReq) throws Exception {
        String taskId = taskRegeditReq.getTaClearTaskInfo().getTaskId();
        KbatchClearTaskInfoDto taskInfo = (KbatchClearTaskInfoDto)taskRegeditReq.getTaClearTaskInfo();

        // 询批量任务子步骤信息
        List<KbatchTaskStep> taskSteps = this.queryTaskStepByTaskid(taskId);
        if (taskSteps == null || taskSteps.size() < 1) {
            throw new TransException("CTS001", String.format("未查询到批量子步骤任务信息，task_id:[%s]", taskId));
        }

        // 组装任务信息
        return createTasksPojo(taskRegeditReq, taskInfo, taskSteps, null, null);
    }

    /**
     * 查询子步骤信息
     *
     * @param taskid
     * @return
     * @throws Exception
     */
    public List<KbatchTaskStep> queryTaskStepByTaskid(String taskid) throws Exception {
        //查询当前task_id下的所有step任务
        SqlResult sr = dbop.select("SysBatchRegedit-Q03", new ParamMap().on("task_id", taskid));
        if(sr.getRowSize()==0){
            throw new TransException("A20501", "查询清算任务子步骤出错, taskid = " + taskid);
        }

        List<KbatchTaskStep> taskStepList = new ArrayList<>();
        while (sr.next()) {
            KbatchTaskStep kbatchTaskStep = new KbatchTaskStep();
            kbatchTaskStep.setTaskId(sr.getString("task_id"));
            kbatchTaskStep.setStepNo(sr.getInteger("step_no"));
            kbatchTaskStep.setIsSlice(sr.getString("is_slice"));
            kbatchTaskStep.setCanReplay(sr.getString("can_replay"));
            kbatchTaskStep.setStepName(sr.getString("step_name"));
            kbatchTaskStep.setCanSkip(sr.getString("can_skip"));
            kbatchTaskStep.setSliceReqClass(sr.getString("slice_req_class"));
            kbatchTaskStep.setSliceServiceClass(sr.getString("slice_service_class"));
            taskStepList.add(kbatchTaskStep);
        }
        return taskStepList;
    }

    /**
     * 构建任务注册返回对象
     * @param taskRegeditReq
     * @param taskInfo
     * @param taskSteps
     * @param targetCode
     * @param prodCode
     * @return RegistClearTaskPojo
     */
    public RegistClearTaskPojo createTasksPojo(TaskRegeditReq taskRegeditReq, KbatchClearTaskInfoDto taskInfo, List<KbatchTaskStep> taskSteps, String targetCode, String prodCode) throws Exception {
        // 转换步骤应执行日期
        String shouldExecDate = taskRegeditReq.getCurrWorkdate();
        if (RunningTypeContents.isT(taskInfo.getRunningType())) {//T 实际是T+1
            shouldExecDate = taskRegeditReq.getT1WorkDate();
        } else if (RunningTypeContents.isTPre1(taskInfo.getRunningType())) {//T-1 实际是T
            shouldExecDate = taskRegeditReq.getCurrWorkdate();
        } else {
            String val = taskInfo.getRunningType() == null ? "null" : taskInfo.getRunningType();
            throw new TransException("BRG001", String.format("清算应执行日期类型参数错误:[%s]", val));
        }

        //批量任务执行数据对象
        KbatchTaskExec taskExec = new KbatchTaskExec();
        taskExec.setModuleid(taskInfo.getModuleid());                     // 模块ID
        taskExec.setTaskId(taskInfo.getTaskId());                       // 任务ID
        taskExec.setShouldExecDate(shouldExecDate);                            // 应执行日期
        taskExec.setShouldExecTime(taskInfo.getShouldExecTime());     // 应执行时间
        taskExec.setAlarmTime(taskInfo.getAlarmTime());                 // 未完成报警时间
        taskExec.setTargetCode(targetCode);                                     // 目标代码
        taskExec.setProdCode(prodCode);                                         // 目标产品代码
        taskExec.setExecStatus(BatchTaskStatus.NON_EXECUTION);                  // 任务执行状态 未执行
        taskExec.setTaskDate(taskRegeditReq.getCurrWorkdate());                 // 清算业务日期
        taskExec.setTaskGroup(taskInfo.getTaskGroup());                 // 任务组
        taskExec.setPreTaskId(taskInfo.getPreTaskId());               // 前置任务
        taskExec.setTaskParams(taskInfo.getTaskParams());               // 任务参数
        taskExec.setSimpleFlow(taskInfo.getSimpleFlow());               //
        taskExec.setAutoExec(taskRegeditReq.getAutoexec());                     // 任务自动执行参数
        taskExec.setExecOrder(taskInfo.getExecOrder());                 // 执行顺序
        taskExec.setDistributorBatch(taskRegeditReq.getDistributorBatch());    // 销售商文件批次号

        //循环构建批量子步骤执行数据对象
        List<KbatchTaskStepExec> taskStepExecList = new ArrayList<>();
        for (KbatchTaskStep taskStep : taskSteps) {
            KbatchTaskStepExec taskStepExec = new KbatchTaskStepExec();
            BeanUtils.copyProperties(taskStep, taskStepExec);

            taskStepExec.setModuleid(taskInfo.getModuleid());        // 模块ID
            taskStepExec.setShouldExecDate(shouldExecDate);               // 应执行日期
            taskStepExec.setTargetCode(targetCode);                        // 目标代码
            taskStepExec.setProdCode(prodCode);                            // 目标产品代码
            taskStepExec.setExecStatus(BatchTaskStatus.NON_EXECUTION);     // 执行状态
            taskStepExec.setTaskDate(taskRegeditReq.getCurrWorkdate());    // 清算业务日期
            taskStepExec.setTaskGroup(taskInfo.getTaskGroup());    // 任务组
            taskStepExec.setIsNeglect(GlobalContents.NO);                  //
            taskStepExec.setIsSkip(GlobalContents.NO);                     //
            taskStepExec.setIsReplay(GlobalContents.NO);                   //
            taskStepExec.setIsStop(GlobalContents.NO);                     //
            taskStepExecList.add(taskStepExec);
        }

        //返回注册对象
        RegistClearTaskPojo registClearTaskPojo = new RegistClearTaskPojo(true);
        registClearTaskPojo.setTaClearTaskExec(taskExec);
        registClearTaskPojo.setTaClearTaskStepExecList(taskStepExecList);

        return registClearTaskPojo;
    }

}
