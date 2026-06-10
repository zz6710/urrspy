package com.kayak.pms.opFlow.engine.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.spring.SpringContextHolder;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.helper.StringHelper;
import com.kayak.utils.DateHelper;
import com.kayak.pms.opFlow.engine.AbstractService;
import com.kayak.pms.opFlow.engine.busi.CommonWorkflowCallback;
import com.kayak.pms.opFlow.engine.busi.ExtraData;
import com.kayak.pms.opFlow.engine.busi.SaveTaskUpdateBusinessLogService;
import com.kayak.pms.opFlow.engine.constant.ParamConstant;
import com.kayak.pms.opFlow.engine.constant.ProcessInstanceConstant;
import com.kayak.pms.opFlow.engine.constant.ProcessType;
import com.kayak.pms.opFlow.engine.dao.*;
import com.kayak.pms.opFlow.engine.entity.Process;
import com.kayak.pms.opFlow.engine.entity.*;
import com.kayak.pms.opFlow.engine.exception.WorkflowException;
import com.kayak.pms.opFlow.engine.helper.AssertHelper;
import com.kayak.pms.opFlow.engine.model.Execution;
import com.kayak.pms.opFlow.engine.model.ProcessModel;
import com.kayak.pms.opFlow.engine.model.TaskModel;
import com.kayak.pms.opFlow.engine.utils.FormDataUtil;
import com.kayak.pms.opFlow.engine.utils.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by daniel on 17/04/2017.
 */
@Service
@APIDefine(desc = "操作流审核", model = Approval.class)
public class ApprovalService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalService.class);

    @Autowired
    TaskService taskService;

    @Autowired
    WorkflowEngine workflowEngine;

    @Autowired
    ApprovalDao approvalDao;
    @Autowired
    AttachmentDao attachmentDao;

    @Autowired
    ProcessInstanceDao processInstanceDao;
    @Autowired
    FormDataService formDataService;

    @Autowired
    SubmitParamsService submitParamsService;

    @Autowired
    ProcessService processService;

    @Autowired
    BusinessExtendService businessExtendService;

    public void saveApproval(Approval approval) throws Exception {
        approvalDao.saveApproval(approval);
    }

    private void refuseProcessInstance(Approval approval) throws Exception {
        fillApproval(approval);
        //设置流程实例状态为拒绝。并且把对应流程的任务全部放入历史任务当中
        ProcessInstance processInstance = processInstanceDao.getProcessInstanceById(approval.getProcessInstanceId());
        processInstance.setCurrentStatus(ProcessInstanceConstant.REFUSE);
        processInstance.setFinishDate(DateHelper.getCurrentDate());
        processInstance.setFinishTime(DateHelper.getCurrentTime());
        processInstanceDao.complete(processInstance);

        Map<String, Object> params = new HashMap<String, Object>();
        //查询对应流程实例的所有任务
        List<Task> tasks = taskService.listActiveTasksByProcessInstanceId(approval.getProcessInstanceId());
        //完成当前任务，删除其它任务
        for (Task task : tasks) {
            if (approval.getTaskId().equals(task.getId())) {//完成当前任务
                taskService.complete(task.getId(), params);
            } else {//删除其它任务
                taskService.deleteTaskById(task.getId());
            }
        }
        // 保存审批历史
        saveApproval(approval);

        //当前流程配置有拒绝回调
        Process process = processService.getProcessById(processInstance.getProcessId());
        TaskModel applyModel = process.getProcessModel().getTaskModels().get(0);
        if (StringHelper.isNotEmpty(applyModel.getApplyRefuseHandler())) {
            businessCallBack(processInstance, applyModel.getApplyRefuseHandler());
        }

        // 拒绝回调
        businessCallBack(processInstance, null);

    }

    /**
     * 可以考虑只传递一个标识
     *
     * @param processInstance
     */
    private void businessCallBack(ProcessInstance processInstance, String callbackClassName) {
        BusinessCallBackService businessCallBackService = new BusinessCallBackService(processInstance, callbackClassName);
        businessCallBackService.invoke(ProcessInstanceConstant.REFUSE, FormDataUtil.formData2Map(formDataService.listLatestFormData(processInstance.getProcessInstanceId())));
    }

    private void rejectTask(Approval approval) throws Exception {
        fillApproval(approval);

        // 驳回请求，需要删除并行审批下其他任务
        //查询对应流程实例的所有任务
        List<Task> tasks = taskService.listActiveTasksByParentId(approval.getTaskId());
        //完成当前任务，删除其它任务
        if(tasks!=null && tasks.size()>0){
            for (Task task : tasks) {
                taskService.deleteTaskById(task.getId());
            }
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("taskId", approval.getTaskId()); //由于nodeName不存在, 所以驳回到上一个节点
        params.put("taskName", approval.getTaskName());
        workflowEngine.executeAndJumpTask(params);

        saveApproval(approval);//保存审批历史
    }

    private void passTask(Approval approval) throws Exception {
        fillApproval(approval);

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringHelper.isNotEmpty(approval.getModifiedData())) {
            //更新表单数据
            updateFormData(approval);
            putModifiedDataToCtxParams(approval.getModifiedData(), params);
        }

        // 修改业务表数据
        Task task = taskService.getTask(approval.getTaskId());
        approval.setTaskName(task.getName());
        approval.setTaskDisplayName(task.getDisplayName());
        updateBusiData(approval);

        // 更新审核附件列表
        updateApprovalIds(approval);

        params.put(ParamConstant.SPECIFIED_ACTORS, RequestSupport.getRequestParamValue(ParamConstant.SPECIFIED_ACTORS));
        // 执行任务
        workflowEngine.executeTask(approval.getTaskId(), params);

        Map<String, Object> parameters = RequestSupport.getParameters();
        // 流程通过, 并且不是结束节点, 那么就需要更新流程状态为审批中
        if (!ProcessInstanceConstant.FINISH.equals(parameters.get("process_status"))) {
            CommonWorkflowCallback.updateProcessStatus(approval.getProcessInstanceId(), ProcessInstanceConstant.APPROVALING);
        }
        saveApproval(approval);//保存审批历史
    }

    public void updateBusiData(Approval approval) throws Exception {
        String processInstanceId = approval.getProcessInstanceId();
        ProcessInstance processInstance = processInstanceDao.getProcessInstanceById(processInstanceId);
        String type = processInstance.getType();
        if (ProcessType.PASSIVE.equals(type)) {
            businessExtendService.updateBusinessData(approval);
        } else {
            updateAggresiveBusiData(approval);
        }
    }

    public void updateAggresiveBusiData(Approval approval) throws Exception {
        final ProcessService processService = SpringContextHolder.getBean("processService");
        final FormDataService formDataService = SpringContextHolder.getBean("formDataService");
        Process process = processService.getProcessById(approval.getProcessId());
        ProcessModel processModel = process.getProcessModel();
        TaskModel taskModel = (TaskModel) processModel.getNode(approval.getTaskName());
        String evnItemKeyStr = taskModel.getUpdateEnvTask();
        if (StringHelper.isNotEmpty(evnItemKeyStr)) {
            CommonDao commonDao = SpringContextHolder.getBean("commonDao");
            EnvItemDao envItemDao = SpringContextHolder.getBean("envItemDao");
            String[] envItemKeys = evnItemKeyStr.split(",");
            // 当前用户和当前表主键
            List<FormData> formData = formDataService.listLatestFormData(approval.getProcessInstanceId());
            Map<String, Object> params = FormDataUtil.formData2Map(formData);
            params.putAll(SysUtil.getUserInfo());
            if (StringHelper.isNotEmpty(approval.getExtraData())) {
                List<ExtraData> extraData = JSONArray.parseArray(approval.getExtraData(), ExtraData.class);

                params.putAll(extraData.stream().collect(Collectors.toMap(ExtraData::getName, ExtraData::getValue)));
                params.put("process_instance_id", approval.getProcessInstanceId());
                params.put("current_date", Tools.getCurrentDate());
                for (String envItemKey : envItemKeys) {
                    String sql = envItemDao.getSqlByItemKey(approval.getProcessId(), envItemKey);
                    sql = RegexUtil.getSql(sql, params);
                    commonDao.updateBySql(sql);
                }

                SaveTaskUpdateBusinessLogService saveTaskUpdateBusinessLogService = new SaveTaskUpdateBusinessLogService();
                saveTaskUpdateBusinessLogService.saveLog(approval,
                        () -> extraData.stream()
                                .map(o -> new SaveTaskUpdateBusinessLogService.UpdateDataElement(
                                        o.getName(), o.getValue(), o.getDisplayName()
                                ))
                                .collect(Collectors.toList()));
            }
        }
    }


    /**
     * 流程通过, 则修改状态为审批中
     *
     * @param processInstanceId
     */
    private void updateProcessInstanceStatus(String processInstanceId) {
        List<FormData> formData = formDataService.listLatestFormData(processInstanceId);
        String busiTableName = "";
        for (FormData item : formData) {
            if (ProcessInstanceConstant.BUSI_TABLE_NAME.equals(item.getFieldName())) {
                busiTableName = item.getFieldValue();
            }
        }

        if (StringHelper.isEmpty(busiTableName)) {
            throw new RuntimeException("流程实例: " + processInstanceId + " 表单不存在表明, 无法更新业务表流程状态为审批中");
        }
        // 可以用类似于上面的方法, 做特殊的数据处理或者重新开启工作流
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("table_name", busiTableName);
        updateParams.put("process_instance_id", processInstanceId);
        updateParams.put("process_status", ProcessInstanceConstant.APPROVALING);

//        ComnDao comnDao = SysBeans.getComnDao();
//        try {
//            comnDao.doUpdateBySqlid("updateProcessStatus", updateParams);
//        } catch (KPromptException e) {
//            throw new RuntimeException("更新业务表流程状态失败", e);
//        } catch (KSystemException e) {
//            throw new RuntimeException("更新业务表流程状态失败", e);
//        } catch (SQLException e) {
//            throw new RuntimeException("更新业务表流程状态失败", e);
//        } catch (KSqlException e) {
//            throw new RuntimeException("更新业务表流程状态失败", e);
//        }
    }

    private void putModifiedDataToCtxParams(String modifiedDataStr, Map<String, Object> params) {
        List<ModifiedData> modifiedDatas = JSON.parseArray(modifiedDataStr, ModifiedData.class);
        for (ModifiedData modifiedData : modifiedDatas) {
            params.put(modifiedData.getName(), modifiedData.getAfterValue());
        }
    }

    /**
     * 动态修改表单数据时，更新数据
     *
     * @param approval
     */
    private void updateFormData(Approval approval) {
        //这里是审批者在审批过程中。动态修改表单数据,并且动态更新wf_form_data，用于动态查询
        formDataService.batchUpdateDynamicFormData(approval);
    }

    private void updateApprovalIds(Approval approval) {
        Map<String, Object> params = RequestSupport.getParameters();
        String attachmentIds = (String) params.get("attachmentIds");
        if (StringHelper.isNotEmpty(attachmentIds)) {
            String[] attachmentIdsArr = attachmentIds.split(",");
            Map<String, Object> map = new HashMap<String, Object>();
            for (String attachmentId : attachmentIdsArr) {
                map.put("attachmentId", attachmentId);
                map.put("approvalId", approval.getId());
                attachmentDao.updateApprovalIds(map);
            }
        }
    }

    public void autoApproval(String taskId, String operator, Map<String, Object> params) throws Exception {

        Approval approval = new Approval();
        approval.setResult(ProcessInstanceConstant.PASS);
        approval.setTaskId(taskId);
        approval.setOperator(operator);
        approval.setOpinion("批量审批通过");
        approval.setType("0");
        fillApproval(approval);

        params.put(ParamConstant.WF_AUTO_APPROVAL, "true");
        workflowEngine.executeTask(taskId, params);

        saveApproval(approval);//保存审批历史
    }

    public void batchApprovalForExpense(Set<String> taskIds, String operator, Map<String, Object> params, Map<String, Task> allTaskMap) throws Exception {
        for (String taskId : taskIds) {
            final Task task = allTaskMap.get(taskId);
            Approval approval = new Approval();
            approval.setProcessId(task.getProcessId());
            approval.setTaskName(task.getName());
            approval.setProcessInstanceId(task.getProcessInstanceId());

            updateBusiData(approval);
            autoApproval(taskId, operator, params);
        }
    }

    //自动审批


    private Execution execute(String taskId, Map<String, Object> params) throws Exception {
        if (params == null) params = new HashMap<String, Object>();
        params.put(ProcessInstanceConstant.START, true);
        //完成指定任务，并不能驱动流程继续向下执行
        Task task = taskService.complete(taskId, params);
        if (logger.isDebugEnabled()) {
            logger.debug("任务[taskId={}, taskName={}]已完成", task.getId(), task.getDisplayName());
        }

        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = processInstanceDao.getProcessInstanceById(processInstanceId);
        AssertHelper.notNull(processInstance, "指定的流程实例[id=" + processInstanceId + "]已完成或不存在");

        Process process = processService.getProcessById(processInstance.getProcessId());
        Execution execution = new Execution(workflowEngine, process, processInstance, params);
        execution.setTask(task);
        //给执行对象设置最新的数据
        execution.setLatestSubmitParams(formData2Map(formDataService.listLatestFormData(processInstance.getProcessInstanceId())));
        return execution;
    }

    private Map<String, Object> formData2Map(List<FormData> formDatas) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (formDatas != null) {
            for (FormData formData : formDatas) {
                result.put(formData.getFieldName(), formData.getFieldValue());
            }
        }
        return result;
    }

    private void fillApproval(Approval approval) throws Exception {
        Task task = taskService.getTask(approval.getTaskId());
        if (task == null) {//任务已经被审批了
            throw new WorkflowException("任务实例不存在或已经审批");
        }
        ProcessInstance processInstance = processInstanceDao.getProcessInstanceById(task.getProcessInstanceId());
        approval.setProcessId(processInstance.getProcessId());
        approval.setId(StringHelper.getPrimaryKey());
        approval.setProcessInstanceId(task.getProcessInstanceId());
        approval.setCreateDate(DateHelper.getCurrentDate());
        approval.setCreateTime(DateHelper.getCurrentTime());
        List<SubmitParams> submitParamses = submitParamsService.getSubmitParamsByProcessInstanceId(processInstance.getProcessInstanceId());
        approval.setSubmitParamsId(submitParamses.get(0).getId());
    }
}
