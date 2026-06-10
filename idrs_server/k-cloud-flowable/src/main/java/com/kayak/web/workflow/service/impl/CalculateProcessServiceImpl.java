package com.kayak.web.workflow.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.kayak.cmd.ExpressionCmd;
import com.kayak.common.constant.FieldConstants;
import com.kayak.common.constant.ProcessConstants;
import com.kayak.common.constant.TaskConstants;
import com.kayak.common.exception.WorkflowException;
import com.kayak.factory.SystemServiceFactory;
import com.kayak.utils.BeanCopyUtils;
import com.kayak.utils.StringUtils;
import com.kayak.utils.flow.ModelUtils;
import com.kayak.web.workflow.domain.bo.TaskApproverBo;
import com.kayak.web.workflow.domain.bo.WfTaskBo;
import com.kayak.web.workflow.domain.vo.CalculatTaskVo;
import com.kayak.web.workflow.domain.vo.WfViewerVo;
import com.kayak.web.workflow.service.ICalculateProcessService;
import com.kayak.web.workflow.service.IWfParamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.*;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuanjinqiao
 * @description
 * @create 2023-03-07 16:37
 **/
@RequiredArgsConstructor
@Service
@Slf4j
@Primary
public class CalculateProcessServiceImpl implements ICalculateProcessService {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    SystemServiceFactory systemServiceFactory;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    ManagementService managementService;

    @Autowired
    HistoryService historyService;

    @Autowired
    ProcessEngineConfigurationImpl processEngineConfiguration;

    @Autowired
    IWfParamService wfParamService;

    @Override
    public List getCalculateTask(WfTaskBo bo) {
        Map<String, Object> variables = bo.getVariables();
        variables.put(FieldConstants.PROCESS_KEY, bo.getProcKey());
        String procKey = bo.getProcKey();
        String procInsId = bo.getProcInsId();
        String procDefId = null;
        if (StringUtils.isNotEmpty(procInsId)) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInsId).singleResult();
            procDefId = historicProcessInstance.getProcessDefinitionId();
        } else {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(procKey).latestVersion().singleResult();
            procDefId = processDefinition.getId();
        }
        //获取预测审批路径中的所有元素
        List<FlowElement> allFlowElements = calApprovePath(procDefId, procInsId, variables);
        //过滤出用户任务,其他任务不能指定审批人
        List<FlowElement> taskList = allFlowElements.stream().filter(curFlowElement -> curFlowElement instanceof UserTask).collect(Collectors.toList());
        //排序
        List<FlowElement> flowElements = sortCalculateList(taskList);

        List<CalculatTaskVo> retList = new ArrayList<>();
        for (FlowElement flowElement : flowElements) {
            CalculatTaskVo calculatTaskVo = getCalculatTaskVo(variables, procInsId, flowElement);
            retList.add(calculatTaskVo);
        }
        //去掉申请任务，申请任务规定为第一个任务。
        retList.remove(0);
        return retList;
    }

    /**
     * 给预测出来的list排序并去重
     *
     * @param taskList
     * @return
     */
    @Override
    public List<FlowElement> sortCalculateList(List<FlowElement> taskList) {
        //反转
        CollectionUtil.reverse(taskList);
        //去重
        LinkedHashSet<FlowElement> set = new LinkedHashSet<>();
        for (FlowElement flowElement : taskList) {
            set.add(flowElement);
        }
        List<FlowElement> retList = new ArrayList<>();
        for (FlowElement flowElement : set) {
            retList.add(flowElement);
        }
        //还原顺序
        CollectionUtil.reverse(retList);
        return retList;
    }

    @Override
    public String getFlowView(WfTaskBo bo) {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(bo.getProcKey()).latestVersion().singleResult();
            InputStream inputStream = repositoryService.getProcessModel(processDefinition.getId());
            return IoUtil.readUtf8(inputStream);
        } catch (Exception exception) {
            log.info(exception.getMessage(), exception);
            throw new RuntimeException("加载xml文件异常");
        }
    }

    @Override
    public WfViewerVo getFlowViewRun(WfTaskBo bo) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(bo.getProcKey()).latestVersion().singleResult();
        List<FlowElement> flowElements = calApprovePath(processDefinition.getId(), null, bo.getVariables());
        Set<String> finishedTaskSet = new HashSet<>();
        Set<String> finishedSequenceFlowSet = new HashSet<>();
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof SequenceFlow) {
                finishedSequenceFlowSet.add(flowElement.getId());
            } else {
                finishedTaskSet.add(flowElement.getId());
            }
        }
        WfViewerVo wfViewerVo = new WfViewerVo();
        wfViewerVo.setFinishedSequenceFlowSet(finishedSequenceFlowSet);
        wfViewerVo.setFinishedTaskSet(finishedTaskSet);
        return wfViewerVo;
    }

    /**
     * 获取任务视图对象
     *
     * @param variables
     * @param procInsId
     * @param flowElement
     * @return
     */
    @Override
    public CalculatTaskVo getCalculatTaskVo(Map<String, Object> variables, String procInsId, FlowElement flowElement) {
        CalculatTaskVo calculatTaskVo = new CalculatTaskVo();
        calculatTaskVo.setId(flowElement.getId());
        calculatTaskVo.setTaskName(flowElement.getName());
        if (!(flowElement instanceof UserTask)) {
            return calculatTaskVo;
        }
        UserTask userTask = (UserTask) flowElement;
        MultiInstanceLoopCharacteristics loopCharacteristics = userTask.getLoopCharacteristics();
        if (loopCharacteristics == null) {
            calculatTaskVo.setTaskType(null);
        } else {
            if (loopCharacteristics.isSequential()) {
                calculatTaskVo.setTaskType("sequential");
            } else {
                calculatTaskVo.setTaskType("notSequential");
            }
        }
        //判断当前任务是否指定了审批人
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
            calculatTaskVo.setUserIds(taskApprover.getApprovers());
            calculatTaskVo.setRoleIds(new ArrayList<>());
        } else {
            //候选角色
            List<String> candidateGroups = userTask.getCandidateGroups();
            calculatTaskVo.setRoleIds(candidateGroups);
            //候选用户
            List<String> candidateUsers = userTask.getCandidateUsers();
            List<String> userIds = new ArrayList<>(candidateUsers);
            //动态审批人
            String candidateParam = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.CANDIDATE_PARAM);
            if (StringUtils.isNotEmpty(candidateParam)) {
                Object o = wfParamService.parseWfParam(candidateParam, variables, procInsId);
                if (o instanceof List) {
                    userIds.addAll((List<String>) o);
                } else if (o instanceof String) {
                    userIds.add((String) o);
                } else {
                    log.info("----{}", o);
                    throw new WorkflowException("流程参数[" + candidateParam + "]用于动态审批人时,返回值应该为List<String>或者String");
                }
            }
            calculatTaskVo.setUserIds(userIds);
        }
        return calculatTaskVo;
    }

    /**
     * 预测审批路径
     * 1. 首先拿到BpmnModel，所有流程定义信息都可以通过BpmnModel获取；若流程尚未发起，则用modelId查询最新部署的流程定义数据；
     * 若流程已经发起，可以通过流程实例的processDefinitionId查询流程定义的历史数据。
     *
     * @param procInsId
     * @param variableMap 流程变量，用于计算条件分支
     */
    @Override
    public List<FlowElement> calApprovePath(String procDefId, String procInsId, Map<String, Object> variableMap) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
        List<FlowElement> flowElements = new ArrayList<>(bpmnModel.getMainProcess().getFlowElements());
        List<FlowElement> passElements = new ArrayList<>();
        this.dueStartElement(passElements, flowElements, variableMap, procInsId);
        return passElements;
    }

    /**
     * 2. 找到开始节点，通过它的目标节点，然后再不断往下找。
     */
    private void dueStartElement(List<FlowElement> passElements, Collection<FlowElement> flowElements, Map<String, Object> variableMap, String procInsId) {
        Optional<FlowElement> startElementOpt = flowElements.stream().filter(flowElement -> flowElement instanceof StartEvent).findFirst();
        startElementOpt.ifPresent(startElement -> {
            List<SequenceFlow> outgoingFlows = ((StartEvent) startElement).getOutgoingFlows();
            SequenceFlow sequenceFlow = outgoingFlows.get(0);
            String targetRef = sequenceFlow.getTargetRef();
            //开始节点和开始节点的连线
            passElements.add(startElement);
            passElements.add(sequenceFlow);
            // 根据ID找到FlowElement
            FlowElement targetElementOfStartElement = getFlowElement(flowElements, targetRef);
            if (targetElementOfStartElement instanceof UserTask) {
                this.getPassElementList(passElements, flowElements, targetElementOfStartElement, variableMap, procInsId);
            }
        });
    }

    /**
     * 3. 我只用到了Task、ExclusiveGateway、ParallelGateway、InclusiveGateway，所以代码里只列举了这四种，如果用到了其他的，可以再自己补充
     */
    @Override
    public void getPassElementList(List<FlowElement> passElements, Collection<FlowElement> flowElements, FlowElement curFlowElement, Map<String, Object> variableMap, String procInsId) {
        passElements.add(curFlowElement);
        // 任务节点
        if (curFlowElement instanceof UserTask || curFlowElement instanceof ReceiveTask || curFlowElement instanceof ManualTask || curFlowElement instanceof ServiceTask || curFlowElement instanceof ScriptTask) {
            this.dueUserTaskElement(passElements, flowElements, curFlowElement, variableMap, procInsId);
            return;
        }
        // 排他网关
        if (curFlowElement instanceof ExclusiveGateway) {
            this.dueExclusiveGateway(passElements, flowElements, curFlowElement, variableMap, procInsId);
            return;
        }
        // 并行网关
        if (curFlowElement instanceof ParallelGateway) {
            this.dueParallelGateway(passElements, flowElements, curFlowElement, variableMap, procInsId);
        }
        // 包容网关
        if (curFlowElement instanceof InclusiveGateway) {
            this.dueInclusiveGateway(passElements, flowElements, curFlowElement, variableMap, procInsId);
        }
    }

    @Override
    public CalculatTaskVo getCalculateCopy(WfTaskBo bo) {
        CalculatTaskVo calculatTaskVo = new CalculatTaskVo();
        Set<String> userIds = new HashSet<>();
        Set<String> roleIds = new HashSet<>();

        String procKey = bo.getProcKey();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(procKey).latestVersion().singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        UserTask userTask = ModelUtils.getApplyTask(bpmnModel);

        String copyUsers = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.COPY_USERS);
        String copyRoles = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.COPY_ROLES);
        String copyUserParam = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.COPY_USER_PARAM);
        if (StringUtils.isNotEmpty(copyUsers)) {
            userIds.addAll(Arrays.asList(copyUsers.split(",")));
        }
        if (StringUtils.isNotEmpty(copyRoles)) {
            roleIds.addAll(Arrays.asList(copyRoles.split(",")));
        }
        if (StringUtils.isNotEmpty(copyUserParam)) {
            Object copyUserIds = wfParamService.parseWfParam(copyUserParam, new HashMap<>(), null);
            if (copyUserIds instanceof List) {
                userIds.addAll((List<String>) copyUserIds);
            } else if (copyUserIds instanceof String) {
                userIds.add((String) copyUserIds);
            } else {
                throw new WorkflowException("流程参数[" + copyUserParam + "]用于动态审批人时,返回值应该为List<String>或者String");
            }
        }
        calculatTaskVo.setTaskName("抄送");
        calculatTaskVo.setId(userTask.getId());
        calculatTaskVo.setUserIds(new ArrayList<>(userIds));
        calculatTaskVo.setRoleIds(new ArrayList<>(roleIds));
        return calculatTaskVo;
    }

    private void dueUserTaskElement(List<FlowElement> passElements, Collection<FlowElement> flowElements, FlowElement curFlowElement, Map<String, Object> variableMap, String procInsId) {
        List<SequenceFlow> outgoingFlows = ((UserTask) curFlowElement).getOutgoingFlows();
        SequenceFlow sequenceFlow = null;
        if (outgoingFlows.size() > 1) {
            // 找到表达式成立的sequenceFlow
            sequenceFlow = getSequenceFlow(variableMap, outgoingFlows, procInsId);
        }
        if (Objects.isNull(sequenceFlow)) {
            sequenceFlow = outgoingFlows.get(0);
        }
        passElements.add(sequenceFlow);
        // 根据ID找到FlowElement
        FlowElement targetElement = getFlowElement(flowElements, sequenceFlow.getTargetRef());
        this.getPassElementList(passElements, flowElements, targetElement, variableMap, procInsId);
    }

    /**
     * 包容网关找到所有为true的顺序流，如果找不到，就取默认顺序流。
     *
     * @param passElements
     * @param flowElements
     * @param curFlowElement
     * @param variableMap
     * @param procInsId
     */
    private void dueInclusiveGateway(List<FlowElement> passElements, Collection<FlowElement> flowElements, FlowElement curFlowElement, Map<String, Object> variableMap, String procInsId) {
        InclusiveGateway exclusiveGateway = (InclusiveGateway) curFlowElement;
        //网关上所有的顺序流
        List<SequenceFlow> allOutgoingFlows = exclusiveGateway.getOutgoingFlows();
        //排除掉默认顺序流的顺序流。默认顺序流需要特殊处理，当其他顺序流不满足条件时，则使用默认顺序流
        ArrayList<SequenceFlow> outgoingFlows = new ArrayList<>();
        for (SequenceFlow outgoingFlow : allOutgoingFlows) {
            if (!StrUtil.equalsAny(exclusiveGateway.getDefaultFlow(), outgoingFlow.getId())) {
                outgoingFlows.add(outgoingFlow);
            }
        }
        String defaultFlow = ((InclusiveGateway) curFlowElement).getDefaultFlow();
        // 找到表达式成立的sequenceFlow
        List<SequenceFlow> sequenceFlows = getSequenceFlows(variableMap, outgoingFlows, procInsId);
        if (CollectionUtil.isNotEmpty(sequenceFlows)) {
            passElements.addAll(sequenceFlows);
            for (SequenceFlow sequenceFlow : sequenceFlows) {
                // 根据ID找到FlowElement
                FlowElement targetElement = getFlowElement(flowElements, sequenceFlow.getTargetRef());
                this.getPassElementList(passElements, flowElements, targetElement, variableMap, procInsId);
            }
        } else if (StringUtils.isNotEmpty(defaultFlow)) {
            //如果没有找到表达式成立的sequenceFlow，就找默认顺序流
            if (StringUtils.isNotEmpty(defaultFlow)) {
                SequenceFlow sequenceFlow = (SequenceFlow) getFlowElement(flowElements, defaultFlow);
                passElements.add(sequenceFlow);
                // 根据ID找到FlowElement
                FlowElement targetElement = getFlowElement(flowElements, sequenceFlow.getTargetRef());
                this.getPassElementList(passElements, flowElements, targetElement, variableMap, procInsId);
            }
        } else {
            throw new WorkflowException("包容网关【" + exclusiveGateway.getId() + "】找不到对应的出口");
        }
    }

    /**
     * 排他网关找到第一个为true的顺序流，如果找不到，就取默认顺序流。
     *
     * @param passElements
     * @param flowElements
     * @param curFlowElement
     * @param variableMap
     * @param procInsId
     */
    private void dueExclusiveGateway(List<FlowElement> passElements, Collection<FlowElement> flowElements, FlowElement curFlowElement, Map<String, Object> variableMap, String procInsId) {
        // 获取符合条件的sequenceFlow的目标FlowElementcurFlowElement = {ExclusiveGateway@15834}
        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) curFlowElement;
        //网关上所有的顺序流
        List<SequenceFlow> allOutgoingFlows = exclusiveGateway.getOutgoingFlows();
        //排除掉默认顺序流的顺序流。默认顺序流需要特殊处理，当其他顺序流不满足条件时，则使用默认顺序流
        ArrayList<SequenceFlow> outgoingFlows = new ArrayList<>();
        for (SequenceFlow outgoingFlow : allOutgoingFlows) {
            if (!StrUtil.equalsAny(exclusiveGateway.getDefaultFlow(), outgoingFlow.getId())) {
                outgoingFlows.add(outgoingFlow);
            }
        }

        // 找到表达式成立的sequenceFlow
        SequenceFlow sequenceFlow = getSequenceFlow(variableMap, outgoingFlows, procInsId);
        if (Objects.isNull(sequenceFlow)) {
            //如果没有找到表达式成立的sequenceFlow，就找默认顺序流
            String defaultFlow = ((ExclusiveGateway) curFlowElement).getDefaultFlow();
            if (StringUtils.isNotEmpty(defaultFlow)) {
                sequenceFlow = (SequenceFlow) getFlowElement(flowElements, defaultFlow);
            }
        }
        if (Objects.isNull(sequenceFlow)) {
            throw new WorkflowException("排他网关【" + exclusiveGateway.getId() + "】找不到对应的出口");
        }
        passElements.add(sequenceFlow);
        // 根据ID找到FlowElement
        FlowElement targetElement = getFlowElement(flowElements, sequenceFlow.getTargetRef());
        this.getPassElementList(passElements, flowElements, targetElement, variableMap, procInsId);
    }

    /**
     * 并行网关，忽视条件，执行所有顺序流
     *
     * @param passElements
     * @param flowElements
     * @param curFlowElement
     * @param variableMap
     * @param procInsId
     */
    private void dueParallelGateway(List<FlowElement> passElements, Collection<FlowElement> flowElements, FlowElement curFlowElement, Map<String, Object> variableMap, String procInsId) {
        FlowElement targetElement;
        List<SequenceFlow> parallelGatewayOutgoingFlows = ((ParallelGateway) curFlowElement).getOutgoingFlows();
        passElements.addAll(parallelGatewayOutgoingFlows);
        for (SequenceFlow sequenceFlow : parallelGatewayOutgoingFlows) {
            targetElement = getFlowElement(flowElements, sequenceFlow.getTargetRef());
            this.getPassElementList(passElements, flowElements, targetElement, variableMap, procInsId);
        }
    }

    private FlowElement getFlowElement(Collection<FlowElement> flowElements, String targetRef) {
        return flowElements.stream().filter(flowElement -> targetRef.equals(flowElement.getId())).findFirst().orElse(null);
    }

    /**
     * 4. 根据传入的变量，计算出表达式成立的多条SequenceFlow
     *
     * @param variableMap
     * @param outgoingFlows
     * @param procInsId
     * @return
     */
    private List<SequenceFlow> getSequenceFlows(Map<String, Object> variableMap, List<SequenceFlow> outgoingFlows, String procInsId) {
        List<SequenceFlow> list = outgoingFlows.stream().filter(item -> {
            try {
                return this.getElValue(item.getConditionExpression(), variableMap, procInsId);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new WorkflowException("表达式：[ " + item.getConditionExpression() + " ]解析有误，请确认表达式是否正确");
            }
        }).collect(Collectors.toList());
        return list;
    }

    /**
     * 4. 根据传入的变量，计算出表达式成立的那一条SequenceFlow
     *
     * @param variableMap
     * @param outgoingFlows
     * @param procInsId
     * @return
     */
    private SequenceFlow getSequenceFlow(Map<String, Object> variableMap, List<SequenceFlow> outgoingFlows, String procInsId) {
        List<SequenceFlow> sequenceFlows = this.getSequenceFlows(variableMap, outgoingFlows, procInsId);
        if (CollectionUtil.isNotEmpty(sequenceFlows)) {
            return sequenceFlows.get(0);
        } else {
            return null;
        }

    }

    private boolean getElValue(String exp, Map<String, Object> variableMap, String procInsId) {
        return managementService.executeCommand(new ExpressionCmd(processEngineConfiguration, procInsId, exp, variableMap));
    }
}
