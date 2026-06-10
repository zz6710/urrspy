package com.kayak.listener.flow;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.common.constant.ProcessConstants;
import com.kayak.common.constant.SurrogateConstants;
import com.kayak.common.constant.TaskConstants;
import com.kayak.common.enums.TaskCommentEnum;
import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.BeanCopyUtils;
import com.kayak.utils.StringUtils;
import com.kayak.utils.SysUtil;
import com.kayak.utils.flow.FlowableUtils;
import com.kayak.utils.flow.ModelUtils;
import com.kayak.web.workflow.domain.bo.TaskApproverBo;
import com.kayak.web.workflow.service.IWfParamService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.impl.event.FlowableEntityEventImpl;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author : yuanjinqiao
 * @title: : GlobalTaskListener
 * @description: 任务创建全局监听
 * @date : 2022/10/11
 */
@Slf4j
@Component
public class GlobalTaskCreatedListener extends AbstractFlowableEngineEventListener {
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IWfParamService wfParamService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private IdentityService identityService;

    @Override
    protected void taskCreated(FlowableEngineEntityEvent event) {
        log.info("进入全局任务创建监听器:{}", "taskCreated");
        BpmnModel bpmnModel = repositoryService.getBpmnModel(event.getProcessDefinitionId());
        FlowableEntityEventImpl flowableEntityEvent = (FlowableEntityEventImpl) event;
        TaskEntityImpl taskEntity = (TaskEntityImpl) flowableEntityEvent.getEntity();
        String taskDefinitionKey = taskEntity.getTaskDefinitionKey();
        FlowElement flowElement = FlowableUtils.findFlowElementByActivityId(bpmnModel, taskDefinitionKey);
        Map<String, Object> variables = taskService.getVariables(taskEntity.getId());
        //暂时只处理用户任务
        if (ObjectUtil.isNotEmpty(flowElement) && flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            UserTask applyTask = ModelUtils.getApplyTask(bpmnModel);
            if (StringUtils.equals(userTask.getId(), applyTask.getId())) {
                //申请任务设置一下审批人
                taskService.setAssignee(taskEntity.getId(),(String) variables.get(TaskConstants.PROCESS_INITIATOR));
            } else {
                //判断当前任务在发起流程时是否指定了审批人
                List<Map> taskApprovers = (List<Map>) variables.getOrDefault(TaskConstants.TASK_APPROVER, new ArrayList<TaskApproverBo>());
                TaskApproverBo taskApprover = null;
                for (Map map : taskApprovers) {
                    TaskApproverBo t = BeanCopyUtils.mapToBean(map, TaskApproverBo.class);
                    if (StringUtils.equals(t.getId(), userTask.getId())) {
                        taskApprover = t;
                        break;
                    }
                }
                //如果指定了审批人，就不用处理动态审批人
                if (Objects.nonNull(taskApprover)) {
                    //处理流程发起时指定审批人
                    handleTaskApprover(taskEntity, taskApprover);
                } else {
                    //处理流程参数
                    handleCandidateParam(taskEntity, variables, userTask);
                }
                //处理否重复审批
                handleRepeatApproved(bpmnModel, taskEntity, variables, userTask);
            }
        }
        log.info("完成全局任务创建监听器:{}", "taskCreated");
    }

    /**
     * 处理流程发起时指定审批人
     * 删除其他候选用户、角色，添加指定的候选人
     *
     * @param taskEntity
     * @param taskApprover
     */
    private void handleTaskApprover(TaskEntityImpl taskEntity, TaskApproverBo taskApprover) {
        Set<IdentityLink> candidates = taskEntity.getCandidates();
        for (IdentityLink candidate : candidates) {
            String userId = candidate.getUserId();
            String groupId = candidate.getGroupId();
            if (StringUtils.isNotEmpty(userId)) {
                //taskEntity.deleteCandidateUser(userId);->该方法不会删除历史数据
                taskService.deleteCandidateUser(taskEntity.getId(), userId);
            }
            if (StringUtils.isNotEmpty(groupId)) {
                taskService.deleteCandidateGroup(taskEntity.getId(), groupId);
            }
        }
        taskEntity.addCandidateUsers(taskApprover.getApprovers());
    }

    /**
     * 处理重复审批
     *
     * @param bpmnModel
     * @param taskEntity
     * @param variables
     * @param userTask
     */
    private void handleRepeatApproved(BpmnModel bpmnModel, TaskEntityImpl taskEntity, Map<String, Object> variables, UserTask userTask) {
        identityService.setAuthenticatedUserId(SysUtil.getCurrentUserId());
        StartEvent startEvent = ModelUtils.getStartEvent(bpmnModel);
        String repeatApproved = startEvent.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.REPEAT_APPROVED);
        if (SurrogateConstants.DISENABLE.equals(repeatApproved)) {
            //审核通过的人
            List<String> passUserList = (List<String>) variables.getOrDefault(TaskConstants.PASS_USER_LIST, new ArrayList<>());
            if (userTask.hasMultiInstanceLoopCharacteristics()) {
                //会签任务指定的审批人
                String assignee = (String) variables.getOrDefault(TaskConstants.ASSIGNEE, "");
                //当前会签任务指定的审批人已经有审批通过的记录
                if (passUserList.contains(assignee)) {
                    //会签时，记录通过人数
                    Integer passCount = (Integer) variables.getOrDefault(TaskConstants.MULTI_PASS_COUNT, 0);
                    runtimeService.setVariable(taskEntity.getExecutionId(), TaskConstants.MULTI_PASS_COUNT, passCount + 1);
                    taskService.addComment(taskEntity.getId(), taskEntity.getProcessInstanceId(), TaskCommentEnum.PASS.getType(), "重复审批，自动通过");
                    taskService.setAssignee(taskEntity.getId(), SysUtil.getCurrentUserId());
                    taskService.complete(taskEntity.getId());
                }
            } else {
                Set<String> candidateUserIds = ModelUtils.getCandidateUserIds(userTask, null, variables, taskEntity.getProcessInstanceId());
                //判断候选用户和已审批通过的用户是否有交集
                List<String> origin = new ArrayList<>();
                origin.addAll(candidateUserIds);
                origin.retainAll(passUserList);
                // 有交集
                if (origin.size() > 0) {
                    taskService.addComment(taskEntity.getId(), taskEntity.getProcessInstanceId(), TaskCommentEnum.PASS.getType(), "重复审批，自动通过");
                    taskService.setAssignee(taskEntity.getId(), SysUtil.getCurrentUserId());
                    taskService.complete(taskEntity.getId());
                }
            }
        }
    }

    /**
     * 处理动态审批人
     *
     * @param taskEntity
     * @param variables
     * @param userTask
     * @return
     */
    private void handleCandidateParam(TaskEntityImpl taskEntity, Map<String, Object> variables, UserTask userTask) {
        if (!userTask.hasMultiInstanceLoopCharacteristics()) {
            //会签的审批人用MultiInstanceHandler.getUserIds处理
            String candidateParam = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.CANDIDATE_PARAM);
            if (StringUtils.isNotEmpty(candidateParam)) {
                Object userIds = wfParamService.parseWfParam(candidateParam, variables, userTask, taskEntity.getProcessInstanceId(), null);
                if (userIds instanceof List) {
                    taskEntity.addCandidateUsers((List<String>) userIds);
                } else if (userIds instanceof String) {
                    taskEntity.addCandidateUser((String) userIds);
                } else {
                    throw new WorkflowException("流程参数[" + candidateParam + "]用于动态审批人时,返回值应该为List<String>或者String");
                }
            }
        }
    }
}
