package com.kayak.web.workflow.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kayak.common.constant.ProcessConstants;
import com.kayak.common.constant.TaskConstants;
import com.kayak.common.enums.MultiInstanceNumberTypeEnum;
import com.kayak.common.enums.ProcessInstanceStatusEnum;
import com.kayak.common.enums.TaskCommentEnum;
import com.kayak.common.exception.WorkflowException;
import com.kayak.factory.FlowServiceFactory;
import com.kayak.factory.SystemServiceFactory;
import com.kayak.flow.CustomProcessDiagramGenerator;
import com.kayak.handler.MultiInstanceHandler;
import com.kayak.scheduled.RefuseCallBackService;
import com.kayak.utils.RegexUtil;
import com.kayak.utils.StringUtils;
import com.kayak.utils.SysUtil;
import com.kayak.utils.flow.FlowableUtils;
import com.kayak.utils.flow.ModelUtils;
import com.kayak.web.system.domain.SysUser;
import com.kayak.web.workflow.domain.WfBusiInfo;
import com.kayak.web.workflow.domain.WfCopy;
import com.kayak.web.workflow.domain.bo.WfTaskBo;
import com.kayak.web.workflow.domain.vo.WfTaskVo;
import com.kayak.web.workflow.domain.vo.WfViewerVo;
import com.kayak.web.workflow.mapper.WfBusiInfoMapper;
import com.kayak.web.workflow.mapper.WfCopyMapper;
import com.kayak.web.workflow.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

/**
 * @author yuanjinqiao
 * @createTime 2022/3/10 00:12
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class WfTaskServiceImpl extends FlowServiceFactory implements IWfTaskService {

    private final WfCopyMapper wfCopyMapper;

    private final WfBusiInfoMapper wfBusiInfoMapper;

    private final IWfFileUploadService wfFileUploadService;
    @Autowired
    private final IWfProcessService wfProcessService;
    @Autowired
    private final SystemServiceFactory systemServiceFactory;
    @Autowired
    private IWfBusiInfoService busiInfoService;
    @Autowired
    private IWfModelService iWfModelService;
    @Autowired
    private RefuseCallBackService refuseCallBackService;
    @Autowired
    private MultiInstanceHandler multiInstanceHandler;
    @Autowired
    protected IdentityService identityService;

    /**
     * 完成任务
     *
     * @param taskBo 请求实体参数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void complete(WfTaskBo taskBo) throws Exception {
        //移除多余变量。防止影响会签功能
        Map<String, Object> v = taskBo.getVariables();
        if (v != null) {
            v.remove("nrOfActiveInstances");
            v.remove("nrOfCompletedInstances");
            v.remove("nrOfInstances");
            List<String> allConstant = TaskConstants.getAllConstant();
            for (String s : allConstant) {
                v.remove(s);
            }
        }
        identityService.setAuthenticatedUserId(SysUtil.getCurrentUserId());
        SysUser currentUserInfo = SysUtil.getCurrentUserInfo();
        // 当前任务 task
        Task task = checkTaskAndGet(taskBo);
        // 上传文件
        wfFileUploadService.uploadFiles(taskBo);
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            StringBuilder commentSB = new StringBuilder();
            commentSB.append("[").append(currentUserInfo.getUsername()).append("] 完成委派");
            if (StringUtils.isNotEmpty(taskBo.getComment())) {
                commentSB.append(" : ").append(taskBo.getComment());
            }
            taskService.addComment(taskBo.getTaskId(), taskBo.getProcInsId(), TaskCommentEnum.PASS.getType(), commentSB.toString());
            taskService.resolveTask(taskBo.getTaskId());
            taskService.setVariables(taskBo.getTaskId(), taskBo.getVariables());
        } else {
            taskService.addComment(taskBo.getTaskId(), taskBo.getProcInsId(), TaskCommentEnum.PASS.getType(), taskBo.getComment());
            taskService.setAssignee(taskBo.getTaskId(), currentUserInfo.getUserid());
            UserTask userTask = (UserTask) FlowableUtils.findFlowElementByActivityId(task.getProcessDefinitionId(), task.getTaskDefinitionKey(), repositoryService);
            Map<String, Object> variables = taskBo.getVariables();
            if (CollectionUtil.isEmpty(variables)) {
                variables = new HashMap<>();
            }
            Map<String, Object> variable = taskService.getVariables(task.getId());
            //记录审核通过的人
            List<String> passUserList = (List<String>) variable.getOrDefault(TaskConstants.PASS_USER_LIST, new ArrayList<>());
            passUserList.add(SysUtil.getCurrentUserId());
            runtimeService.setVariable(task.getExecutionId(), TaskConstants.PASS_USER_LIST, passUserList);
            if (userTask.hasMultiInstanceLoopCharacteristics()) {
                //会签时，记录通过人数
                Integer passCount = (Integer) variable.getOrDefault(TaskConstants.MULTI_PASS_COUNT, 0);
                runtimeService.setVariable(task.getExecutionId(), TaskConstants.MULTI_PASS_COUNT, passCount + 1);
                taskService.complete(task.getId(), variables);
            } else {
                taskService.complete(taskBo.getTaskId(), variables);
            }
        }
    }

    /**
     * 退回任务
     *
     * @param bo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taskReturn(WfTaskBo bo) {
        identityService.setAuthenticatedUserId(SysUtil.getCurrentUserId());

        SysUser currentUserInfo = SysUtil.getCurrentUserInfo();
        // 当前任务 task
        Task task = checkTaskAndGet(bo);
        // 上传文件
        wfFileUploadService.uploadFiles(bo);
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            //委派任务不能退回，只能增加一条退回的意见
            StringBuilder commentSB = new StringBuilder();
            commentSB.append("[").append(currentUserInfo.getUsername()).append("] 完成委派");
            if (StringUtils.isNotEmpty(bo.getComment())) {
                commentSB.append(" : ").append(bo.getComment());
            }
            taskService.addComment(bo.getTaskId(), bo.getProcInsId(), TaskCommentEnum.RETURN.getType(), commentSB.toString());
            taskService.resolveTask(bo.getTaskId());
            return;
        }
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getMainProcess();

        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 类型为用户节点
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    // 获取节点信息
                    source = flowElement;
                }
            }
        }

        // 目的获取所有跳转到的节点 targetIds
        // 获取当前节点的所有父级用户任务节点
        // 深度优先算法思想：延边迭代深入
        List<UserTask> parentUserTaskList = FlowableUtils.iteratorFindParentUserTasks(source, null, null);
        if (parentUserTaskList == null || parentUserTaskList.size() == 0) {
            throw new RuntimeException("当前节点为初始任务节点，不能退回");
        }
        // 获取活动 ID 即节点 Key
        List<String> parentUserTaskKeyList = new ArrayList<>();
        parentUserTaskList.forEach(item -> parentUserTaskKeyList.add(item.getId()));
        // 获取全部历史节点活动实例，即已经走过的节点历史，数据采用开始时间升序
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).orderByHistoricTaskInstanceStartTime().asc().list();
        // 数据清洗，将回滚导致的脏数据清洗掉
        List<String> lastHistoricTaskInstanceList = FlowableUtils.historicTaskInstanceClean(allElements, historicTaskInstanceList);
        // 此时历史任务实例为倒序，获取最后走的节点
        List<String> targetIds = new ArrayList<>();
        // 循环结束标识，遇到当前目标节点的次数
        int number = 0;
        StringBuilder parentHistoricTaskKey = new StringBuilder();
        for (String historicTaskInstanceKey : lastHistoricTaskInstanceList) {
            // 当会签时候会出现特殊的，连续都是同一个节点历史数据的情况，这种时候跳过
            if (parentHistoricTaskKey.toString().equals(historicTaskInstanceKey)) {
                continue;
            }
            parentHistoricTaskKey = new StringBuilder(historicTaskInstanceKey);
            if (historicTaskInstanceKey.equals(task.getTaskDefinitionKey())) {
                number++;
            }
            // 在数据清洗后，历史节点就是唯一一条从起始到当前节点的历史记录，理论上每个点只会出现一次
            // 在流程中如果出现循环，那么每次循环中间的点也只会出现一次，再出现就是下次循环
            // number == 1，第一次遇到当前节点
            // number == 2，第二次遇到，代表最后一次的循环范围
            if (number == 2) {
                break;
            }
            // 如果当前历史节点，属于父级的节点，说明最后一次经过了这个点，需要退回这个点
            if (parentUserTaskKeyList.contains(historicTaskInstanceKey)) {
                targetIds.add(historicTaskInstanceKey);
            }
        }

        // 目的获取所有需要被跳转的节点 currentIds
        // 取其中一个父级任务，因为后续要么存在公共网关，要么就是串行公共线路
        UserTask oneUserTask = parentUserTaskList.get(0);
        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需驳回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<FlowElement> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(oneUserTask, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));

        // 规定：并行网关之前节点必须需存在唯一用户任务节点，如果出现多个任务节点，则并行网关节点默认为结束节点，原因为不考虑多对多情况
        if (targetIds.size() > 1 && currentIds.size() > 1) {
            throw new RuntimeException("任务出现多对多情况，无法退回");
        }

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        // 设置退回意见
        currentTaskIds.forEach(item -> taskService.addComment(item, task.getProcessInstanceId(), TaskCommentEnum.RETURN.getType(), bo.getComment()));

        try {
            // 如果父级任务多于 1 个，说明当前节点不是并行节点，原因为不考虑多对多情况
            if (targetIds.size() > 1) {
                // 1 对 多任务跳转，currentIds 当前节点(1)，targetIds 跳转到的节点(多)
                runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveSingleActivityIdToActivityIds(currentIds.get(0), targetIds).changeState();
            }
            // 如果父级任务只有一个，因此当前任务可能为网关中的任务
            if (targetIds.size() == 1) {
                // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetIds.get(0) 跳转到的节点(1)
                runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveActivityIdsToSingleActivityId(currentIds, targetIds.get(0)).changeState();
            }
        } catch (FlowableObjectNotFoundException e) {
            throw new RuntimeException("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            throw new RuntimeException("无法取消或开始活动");
        }

    }

    /**
     * 校验任务是否存在，并返回任务
     *
     * @param bo
     * @return
     */
    private Task checkTaskAndGet(WfTaskBo bo) {
        Task task = taskService.createTaskQuery().taskId(bo.getTaskId()).singleResult();
        if (ObjectUtil.isNull(task)) {
            throw new RuntimeException("获取任务信息异常！");
        }
        if (task.isSuspended()) {
            throw new RuntimeException("任务处于挂起状态");
        }
        return task;
    }

    /**
     * 驳回任务
     *
     * @param bo 请求实体参数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void taskReject(WfTaskBo bo) {
        identityService.setAuthenticatedUserId(SysUtil.getCurrentUserId());

        String targetKey = bo.getTargetKey();
        SysUser currentUserInfo = SysUtil.getCurrentUserInfo();
        // 当前任务 task
        Task task = checkTaskAndGet(bo);
        // 上传文件
        wfFileUploadService.uploadFiles(bo);
        // 获取所有节点信息
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowElement targetTask = FlowableUtils.findFlowElementByActivityId(bpmnModel, targetKey);
        //驳回的信息
        StringBuilder rejectComment = new StringBuilder();
        rejectComment.append("由 [").append(currentUserInfo.getUsername()).append("]");
        rejectComment.append(" 驳回到 [").append(targetTask.getName()).append("] ");
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            //委派任务不能退回，只能增加一条退回的意见
            StringBuilder commentSB = new StringBuilder();
            commentSB.append("[").append(currentUserInfo.getUsername()).append("] 完成委派").append("，").append(rejectComment);
            if (StringUtils.isNotEmpty(bo.getComment())) {
                commentSB.append(" : ").append(bo.getComment());
            }
            taskService.addComment(bo.getTaskId(), bo.getProcInsId(), TaskCommentEnum.REJECT.getType(), commentSB.toString());
            taskService.resolveTask(bo.getTaskId());
            return;
        }
        Process process = bpmnModel.getMainProcess();
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        // 获取跳转的节点元素
        FlowElement target = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 当前任务节点元素
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = flowElement;
                }
                // 跳转的节点元素
                if (flowElement.getId().equals(targetKey)) {
                    target = flowElement;
                }
            }
        }

        // 从当前节点向前扫描
        // 如果存在路线上不存在目标节点，说明目标节点是在网关上或非同一路线上，不可跳转
        // 否则目标节点相对于当前节点，属于串行
        Boolean isSequential = FlowableUtils.iteratorCheckSequentialReferTarget(source, targetKey, null, null);
        if (!isSequential) {
            throw new RuntimeException("当前节点相对于目标节点，不属于串行关系，无法回退");
        }

        // 获取所有正常进行的执行任务节点的活动ID，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Execution> runExecutionList = runtimeService.createExecutionQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runActivityIdList = new ArrayList<>();
        runExecutionList.forEach(item -> {
            if (StringUtils.isNotBlank(item.getActivityId())) {
                runActivityIdList.add(item.getActivityId());
            }
        });
        // 需退回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runExecutionList 比对，获取需要撤回的任务
        List<FlowElement> currentFlowElementList = FlowableUtils.iteratorFindChildUserTasks(target, runActivityIdList, null, null);
        currentFlowElementList.forEach(item -> currentIds.add(item.getId()));
        //检查驳回的任务是否有会签任务
        checkCurrentIds(currentIds, bpmnModel);
        // 添加需撤回的节点为网关时，添加网关的删除信息
        AtomicReference<List<HistoricActivityInstance>> tmp = new AtomicReference<>();
        // currentIds 为活动ID列表
        // currentExecutionIds 为执行任务ID列表
        // 需要通过执行任务ID来设置驳回信息，活动ID不行
        Set<String> currentExecutionIds = new HashSet<>();
        currentIds.forEach(currentId -> runExecutionList.forEach(runExecution -> {
            if (StringUtils.isNotBlank(runExecution.getActivityId()) && currentId.equals(runExecution.getActivityId())) {
                currentExecutionIds.add(runExecution.getId());
                // 查询当前节点的执行任务的历史数据
                tmp.set(historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).executionId(runExecution.getId()).activityId(runExecution.getActivityId()).list());
                // 如果这个列表的数据只有 1 条数据
                // 网关肯定只有一条，且为包容网关或并行网关
                // 这里的操作目的是为了给网关在扭转前提前加上删除信息，结构与普通节点的删除信息一样，目的是为了知道这个网关也是有经过跳转的
                if (tmp.get() != null && tmp.get().size() == 1 && StringUtils.isNotBlank(tmp.get().get(0).getActivityType()) && ("parallelGateway".equals(tmp.get().get(0).getActivityType()) || "inclusiveGateway".equals(tmp.get().get(0).getActivityType()))) {
                    // singleResult 能够执行更新操作
                    // 利用 流程实例ID + 执行任务ID + 活动节点ID 来指定唯一数据，保证数据正确
                    historyService.createNativeHistoricActivityInstanceQuery().sql("UPDATE ACT_HI_ACTINST SET DELETE_REASON_ = 'Change activity to " + targetKey + "'  WHERE PROC_INST_ID_='" + task.getProcessInstanceId() + "' AND EXECUTION_ID_='" + runExecution.getId() + "' AND ACT_ID_='" + runExecution.getActivityId() + "'").singleResult();
                }
            }
        }));
        // 设置驳回信息
        StringBuilder otherRejectComment = new StringBuilder(rejectComment.toString());

        if (StringUtils.isNotEmpty(bo.getComment())) {
            rejectComment.append("：").append(bo.getComment());
        }

        AtomicReference<Task> atomicCurrentTask = new AtomicReference<>();
        currentExecutionIds.forEach(item -> {
            atomicCurrentTask.set(taskService.createTaskQuery().executionId(item).singleResult());
            // 类型为网关时，获取用户任务为 null
            if (atomicCurrentTask.get() != null) {
                if (StringUtils.equals(atomicCurrentTask.get().getId(), task.getId())) {
                    taskService.addComment(atomicCurrentTask.get().getId(), task.getProcessInstanceId(), TaskCommentEnum.REJECT.getType(), rejectComment.toString());
                } else {
                    taskService.addComment(atomicCurrentTask.get().getId(), task.getProcessInstanceId(), TaskCommentEnum.REJECT.getType(), otherRejectComment.toString());
                }
            }
        });
        //设置办理人
        taskService.setAssignee(task.getId(), SysUtil.getCurrentUserId());
        try {
            //如果驳回到申请节点，更新流程状态。
            UserTask applyTask = ModelUtils.getApplyTask(bpmnModel);
            if (applyTask.getId().equals(bo.getTargetKey())) {
                //更新流程状态为回到申请节点
                runtimeService.updateBusinessStatus(task.getProcessInstanceId(), ProcessInstanceStatusEnum.BACK_TO_APPLY.getType());
                busiInfoService.updateProcStatus(task.getProcessInstanceId(), ProcessInstanceStatusEnum.BACK_TO_APPLY.getType());
            }
            // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetKey 跳转到的节点(1)
            runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveActivityIdsToSingleActivityId(currentIds, targetKey).changeState();
        } catch (FlowableObjectNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("无法取消或开始活动");
        }
    }

    /**
     * 并行、包容网关存在可能驳回多个任务的情况。
     * 检查驳回的任务是否有会签任务,如果有，抛出异常，会签任务不能退回
     *
     * @param currentIds
     * @param bpmnModel
     */
    private void checkCurrentIds(List<String> currentIds, BpmnModel bpmnModel) {
        for (String currentId : currentIds) {
            FlowElement flowElement = FlowableUtils.findFlowElementByActivityId(bpmnModel, currentId);
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;
                //会签任务不支持驳回
                if (userTask.hasMultiInstanceLoopCharacteristics()) {
                    throw new WorkflowException("当前存在会签节点，不能驳回");
                }
            }
        }
    }

    /**
     * 获取所有可驳回的节点
     *
     * @param bo
     * @return
     */
    @Override
    public List<WfTaskVo> findRejectTaskList(WfTaskBo bo) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(bo.getTaskId()).singleResult();
        UserTask source = (UserTask) FlowableUtils.findFlowElementByActivityId(task.getProcessDefinitionId(), task.getTaskDefinitionKey(), repositoryService);
        //获取配置的可驳回节点
        String rejectTaskStr = source.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.REJECT_TASKS);
        if (StringUtils.isEmpty(rejectTaskStr)) {
            return Collections.EMPTY_LIST;
        } else {
            List<String> configRejectTaskList = Arrays.asList(rejectTaskStr.split(","));
            List<WfTaskVo> rejectTaskList = iWfModelService.getRejectTaskListByTask(source);
            return rejectTaskList.stream().filter(t -> configRejectTaskList.contains(t.getTaskDefKey())).collect(Collectors.toList());
        }
    }

    /**
     * 认领/签收任务
     *
     * @param taskBo 请求实体参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claim(WfTaskBo taskBo) {
        Task task = taskService.createTaskQuery().taskId(taskBo.getTaskId()).singleResult();
        if (Objects.isNull(task)) {
            throw new WorkflowException("任务不存在");
        }
        taskService.claim(taskBo.getTaskId(), SysUtil.getCurrentUserId());
    }

    /**
     * 取消认领/签收任务
     *
     * @param bo 请求实体参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unClaim(WfTaskBo bo) {
        taskService.unclaim(bo.getTaskId());
    }

    /**
     * 委派任务
     *
     * @param bo 请求实体参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taskDelegate(WfTaskBo bo) {
        identityService.setAuthenticatedUserId(SysUtil.getCurrentUserId());

        SysUser userInfo = SysUtil.getCurrentUserInfo();
        // 当前任务 task
        Task task = checkTaskAndGet(bo);
        // 上传文件
        wfFileUploadService.uploadFiles(bo);

        StringBuilder commentBuilder = new StringBuilder();
        commentBuilder.append("由 [").append(userInfo.getUsername()).append("] 委派给 [");
        SysUser user = systemServiceFactory.createService().getUserInfo(bo.getUserId());
        if (ObjectUtil.isNotNull(user)) {
            commentBuilder.append(user.getUsername());
        } else {
            commentBuilder.append(bo.getUserId());
        }
        commentBuilder.append("] ");
        if (StringUtils.isNotBlank(bo.getComment())) {
            commentBuilder.append(" ：").append(bo.getComment());
        }
        // 添加审批意见
        taskService.addComment(bo.getTaskId(), task.getProcessInstanceId(), TaskCommentEnum.DELEGATE.getType(), commentBuilder.toString());
        if (!DelegationState.PENDING.equals(task.getDelegationState())) {
            // 设置办理人为当前登录人
            taskService.setOwner(bo.getTaskId(), userInfo.getUserid());
        }
        // 执行委派
        taskService.delegateTask(bo.getTaskId(), bo.getUserId());

    }

    /**
     * 转办任务
     *
     * @param bo 请求实体参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taskTransfer(WfTaskBo bo) {
        identityService.setAuthenticatedUserId(SysUtil.getCurrentUserId());

        SysUser currentUserInfo = SysUtil.getCurrentUserInfo();
        // 当前任务 task
        Task task = checkTaskAndGet(bo);
        // 上传文件
        wfFileUploadService.uploadFiles(bo);
        //拼接审批意见
        StringBuilder commentBuilder = new StringBuilder();
        commentBuilder.append("由 [").append(currentUserInfo.getUsername()).append("] 转办给 [");
        SysUser user = systemServiceFactory.createService().getUserInfo(bo.getUserId());
        if (ObjectUtil.isNotNull(user)) {
            commentBuilder.append(user.getUsername());
        } else {
            commentBuilder.append(bo.getUserId());
        }
        commentBuilder.append("] ");
        if (StringUtils.isNotBlank(bo.getComment())) {
            commentBuilder.append(" ：").append(bo.getComment());
        }
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            //委派任务不能退回，只能增加一条退回的意见
            StringBuilder commentSB = new StringBuilder();
            commentSB.append("[").append(currentUserInfo.getUsername()).append("] 完成委派，").append(commentBuilder);
            taskService.addComment(bo.getTaskId(), bo.getProcInsId(), TaskCommentEnum.TRANSFER.getType(), commentSB.toString());
            taskService.resolveTask(bo.getTaskId());
            return;
        }
        // 添加审批意见
        taskService.addComment(bo.getTaskId(), task.getProcessInstanceId(), TaskCommentEnum.TRANSFER.getType(), commentBuilder.toString());
        // 设置拥有者为当前登录人
        taskService.setOwner(bo.getTaskId(), currentUserInfo.getUserid());
        // 转办任务
        taskService.setAssignee(bo.getTaskId(), bo.getUserId());
    }

    /**
     * 撤回申请
     *
     * @param bo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revokeProcess(WfTaskBo bo) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().includeProcessVariables().processInstanceId(bo.getProcInsId()).singleResult();
        //执行拒绝回调
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        StartEvent startEvent = ModelUtils.getStartEvent(bpmnModel);
        String callbackParam = startEvent.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.REFUSE_CALLBACK);

        refuseCallBackService.invoke(historicProcessInstance, callbackParam);
        //删除流程实例
        wfProcessService.deleteProcessInstance(bo.getProcInsId(), "撤销申请");
        //删除业务信息
        LambdaQueryWrapper<WfBusiInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(WfBusiInfo::getProcessInstanceId, bo.getProcInsId());
        wfBusiInfoMapper.delete(lqw);
        //删除抄送信息
        LambdaQueryWrapper<WfCopy> WfCopyLqw = Wrappers.lambdaQuery();
        WfCopyLqw.eq(WfCopy::getProcInsId, bo.getProcInsId());
        wfCopyMapper.delete(WfCopyLqw);
    }

    /**
     * 获取流程过程图
     *
     * @param processId
     * @return
     */
    @Override
    public InputStream diagram(String processId) {
        String processDefinitionId;
        // 获取当前的流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        // 如果流程已经结束，则得到结束节点
        if (Objects.isNull(processInstance)) {
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        } else {// 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        }

        // 获得活动的节点
        List<HistoricActivityInstance> highLightedFlowList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processId).orderByHistoricActivityInstanceStartTime().asc().list();

        List<String> highLightedFlows = new ArrayList<>();
        List<String> highLightedNodes = new ArrayList<>();
        //高亮线
        for (HistoricActivityInstance tempActivity : highLightedFlowList) {
            if (BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW.equals(tempActivity.getActivityType())) {
                //高亮线
                highLightedFlows.add(tempActivity.getActivityId());
            } else {
                //高亮节点
                highLightedNodes.add(tempActivity.getActivityId());
            }
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();
        //获取自定义图片生成器
        ProcessDiagramGenerator diagramGenerator = new CustomProcessDiagramGenerator();
        return diagramGenerator.generateDiagram(bpmnModel, "png", highLightedNodes, highLightedFlows, configuration.getActivityFontName(), configuration.getLabelFontName(), configuration.getAnnotationFontName(), configuration.getClassLoader(), 1.0, true);

    }

    /**
     * 获取流程执行过程
     *
     * @param procInsId
     * @return
     */
    @Override
    public WfViewerVo getFlowViewer(String procInsId) {
        // 构建查询条件
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery().processInstanceId(procInsId);
        List<HistoricActivityInstance> allActivityInstanceList = query.list();
        if (CollUtil.isEmpty(allActivityInstanceList)) {
            return new WfViewerVo();
        }
        // 获取流程发布Id信息
        String processDefinitionId = allActivityInstanceList.get(0).getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 查询所有已完成的元素
        List<HistoricActivityInstance> finishedElementList = allActivityInstanceList.stream().filter(item -> ObjectUtil.isNotNull(item.getEndTime())).collect(Collectors.toList());
        // 所有已完成的连线
        Set<String> finishedSequenceFlowSet = new HashSet<>();
        // 所有已完成的任务节点
        Set<String> finishedTaskSet = new HashSet<>();
        finishedElementList.forEach(item -> {
            if (BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW.equals(item.getActivityType())) {
                finishedSequenceFlowSet.add(item.getActivityId());
            } else {
                finishedTaskSet.add(item.getActivityId());
            }
        });
        // 查询所有未结束的节点
        Set<String> unfinishedTaskSet = allActivityInstanceList.stream().filter(item -> ObjectUtil.isNull(item.getEndTime())).map(HistoricActivityInstance::getActivityId).collect(Collectors.toSet());
        // DFS 查询未通过的元素集合
        Set<String> rejectedSet = FlowableUtils.dfsFindRejects(bpmnModel, unfinishedTaskSet, finishedSequenceFlowSet, finishedTaskSet);
        return new WfViewerVo(finishedTaskSet, finishedSequenceFlowSet, unfinishedTaskSet, rejectedSet);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taskRefuse(WfTaskBo bo) {
        SysUser currentUserInfo = SysUtil.getCurrentUserInfo();
        // 当前任务 task
        Task task = checkTaskAndGet(bo);
        // 上传文件
        wfFileUploadService.uploadFiles(bo);

        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (instance == null) {
            throw new WorkflowException("流程实例不存在");
        }
        //若当前任务为委派任务，委派的任务不能直接拒绝
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            StringBuilder commentSB = new StringBuilder();
            commentSB.append("[").append(currentUserInfo.getUsername()).append("] 完成委派");
            if (StringUtils.isNotEmpty(bo.getComment())) {
                commentSB.append(" : ").append(bo.getComment());
            }
            taskService.addComment(bo.getTaskId(), bo.getProcInsId(), TaskCommentEnum.REFUSE.getType(), commentSB.toString());
            taskService.resolveTask(bo.getTaskId());
        } else {
            taskService.setAssignee(bo.getTaskId(), SysUtil.getCurrentUserId());
            taskService.addComment(task.getId(), task.getProcessInstanceId(), TaskCommentEnum.REFUSE.getType(), bo.getComment());
            UserTask userTask = (UserTask) FlowableUtils.findFlowElementByActivityId(task.getProcessDefinitionId(), task.getTaskDefinitionKey(), repositoryService);
            Map<String, Object> variables = bo.getVariables();
            if (CollectionUtil.isEmpty(variables)) {
                variables = new HashMap<>();
            }
            if (userTask.hasMultiInstanceLoopCharacteristics()) {
                String completionCondition = userTask.getLoopCharacteristics().getCompletionCondition();
                //解析数字和数字类型
                String number = RegexUtil.getNumber(completionCondition);
                String numberType = "";
                MultiInstanceNumberTypeEnum[] values = MultiInstanceNumberTypeEnum.values();
                for (MultiInstanceNumberTypeEnum value : values) {
                    if (completionCondition.contains(value.getType())) {
                        numberType = value.getType();
                        break;
                    }
                }

                //会签时，完成任务，记录拒绝人数
                Map<String, Object> map = taskService.getVariables(task.getId());
                Object o = map.get(TaskConstants.MULTI_REFUSE_COUNT);
                int refuseNum = o == null ? 0 + 1 : (Integer) o + 1;
                //拒绝人数+1后，判断会签是否完成。
                Integer nrOfActiveInstances = (Integer) map.get("nrOfActiveInstances") - 1;
                Integer nrOfCompletedInstances = (Integer) map.get("nrOfCompletedInstances") + 1;
                Boolean compute = multiInstanceHandler.compute(numberType, number, map, refuseNum, nrOfActiveInstances, nrOfCompletedInstances);
                if (compute) {
                    //拒绝人数+1,会签完成，会签不通过
                    this.refuseProcessInstance(bo.getComment(), task.getProcessInstanceId());
                } else {
                    runtimeService.setVariable(task.getExecutionId(), TaskConstants.MULTI_REFUSE_COUNT, refuseNum);
                    taskService.complete(task.getId());
                }
            } else {
                this.refuseProcessInstance(bo.getComment(), task.getProcessInstanceId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refuseProcessInstance(String comment, String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().includeProcessVariables().processInstanceId(processInstanceId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        StartEvent startEvent = ModelUtils.getStartEvent(bpmnModel);
        String callbackUrl = startEvent.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.REFUSE_CALLBACK);
        //执行拒绝回调
        refuseCallBackService.invoke(historicProcessInstance, callbackUrl);
        //更新流程状态
        runtimeService.updateBusinessStatus(processInstanceId, ProcessInstanceStatusEnum.REFUSE.getType());
        busiInfoService.updateProcStatus(processInstanceId, ProcessInstanceStatusEnum.REFUSE.getType());
        //删除流程
        runtimeService.deleteProcessInstance(processInstanceId, comment);
    }

    @Override
    public List<String> getBtns(String procDefId, String taskDefKey) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
        FlowElement flowElement = FlowableUtils.findFlowElementByActivityId(bpmnModel, taskDefKey);
        if (flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            ArrayList<String> btns = new ArrayList<>();
            String attributeValue = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.BTNS);
            if (StringUtils.isNotEmpty(attributeValue)) {
                String[] split = attributeValue.split(",");
                for (String s : split) {
                    btns.add(s);
                }
            }
            //会签任务不支持驳回
            if (userTask.hasMultiInstanceLoopCharacteristics()) {
                btns.remove("reject");
            }
            return btns;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public void triggerReceiveTask(String procInsId, String receiveTaskDefKey) {
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(procInsId).activityId(receiveTaskDefKey).singleResult();
        assertNotNull("接收任务id[" + receiveTaskDefKey + "]不存在", execution);
        runtimeService.trigger(execution.getId());
    }
}
