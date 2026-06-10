package com.kayak.pms.opFlow.engine.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.Global;
import com.kayak.pms.opFlow.engine.constant.NodeParserConstant;
import com.kayak.pms.opFlow.engine.constant.NodeTypeConstant;
import com.kayak.pms.opFlow.engine.entity.SelectEntity;
import com.kayak.pms.opFlow.engine.exception.WorkflowException;
import com.kayak.pms.opFlow.engine.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by daniel on 22/03/2017.
 */
public class ModelParser {
    private static final Logger logger = LoggerFactory.getLogger(ModelParser.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static ProcessModel parse(String processData) {
        ProcessModel processModel = new ProcessModel();
        try {
            final JsonNode jsonNode = mapper.readTree(processData);

            processModel.setName(jsonNode.get(NodeParserConstant.ATTR_NAME).textValue());
            processModel.setDisplayName(jsonNode.get(NodeParserConstant.ATTR_DISPLAYNAME).textValue());

            //解析节点
            final JsonNode nodesInfo = jsonNode.get("nodesInfo");
            final Iterator<JsonNode> nodeElements = nodesInfo.elements();
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
            };

            while (nodeElements.hasNext()) {
                final JsonNode next = nodeElements.next();
                Map<String, String> attrCacheMap = mapper.readValue(next.get("attrCache").toString(), typeRef);

                final String type = next.get("type").textValue();
                if (NodeTypeConstant.START.equalsIgnoreCase(type)) {
                    StartModel startModel = parseStartModel(attrCacheMap);
                    processModel.getNodes().add(startModel);
                } else if (NodeTypeConstant.END.equalsIgnoreCase(type)) {
                    EndModel endModel = parseEndModel(attrCacheMap);
                    processModel.getNodes().add(endModel);
                } else if (NodeTypeConstant.FORK.equalsIgnoreCase(type)) {
                    ForkModel forkModel = parseForkModel(attrCacheMap);
                    processModel.getNodes().add(forkModel);
                } else if (NodeTypeConstant.JOIN.equalsIgnoreCase(type)) {
                    JoinModel joinModel = parseJoinModel(attrCacheMap);
                    processModel.getNodes().add(joinModel);
                } else if (NodeTypeConstant.DECISION.equalsIgnoreCase(type)) {
                    DecisionModel decisionModel = parseDecisionModel(attrCacheMap);
                    processModel.getNodes().add(decisionModel);
                } else if (NodeTypeConstant.TASK.equalsIgnoreCase(type)) {
                    TaskModel taskModel = parseTaskModel(attrCacheMap);
                    processModel.getNodes().add(taskModel);
                } else if (NodeTypeConstant.OPERATION.equalsIgnoreCase(type)) {
                    OperationModel operationModel = parseOperationModel(attrCacheMap);
                    processModel.getNodes().add(operationModel);
                }
            }

            //解析Transition
            final JsonNode connectionsInfo = jsonNode.get("connectionsInfo");
            handleConnection(processModel, connectionsInfo);
            validateRejectTaskNum(processModel);
            validateDecisionModels(processModel);
        } catch (IOException e) {
            logger.error("流程模型解析失败:" + e.getMessage());
            throw new WorkflowException("流程解析失败");
        }

        return processModel;
    }

    /**
     * 节点output的数量, 必须必须等于驳回节点的数量 或者是驳回按钮 + 连线的数据
     *
     * @param processModel
     */
    private static void validateRejectTaskNum(ProcessModel processModel) {
        List<TaskModel> taskModels = processModel.getTaskModels();
        for (TaskModel taskModel : taskModels) {
            List<TransitionModel> outputs = taskModel.getOutputs();
            int outputNum = outputs.size();
            if (outputNum > 1) {
                if (taskModel.getRejectTaskSelect() == null || !((taskModel.getRejectTaskSelect().size() + 1) == outputNum)) {
                    throw new WorkflowException("任务【" + taskModel.getDisplayName() + "】的输出连线配置不正确");
                }
            }
        }
    }

    public static void validate(ProcessModel processModel) {
        List<OperationModel> models = processModel.getModels(OperationModel.class);
        if (models.size() == 0) {
            throw new WorkflowException("流程至少应该包含功能节点");
        }
        //对于开始节点，必须至少有一个配置表单
//        OperationModel taskModel = models.get(0);
        //对于开始节点，最多只能存在提交按钮
//        if (!StringHelper.isEmpty(taskModel.getBtns()) && (taskModel.getBtns().split(",").length > 1 || !Arrays.asList(taskModel.getBtns().split(",")).contains("0"))) {
//            throw new WorkflowException("流程申请节点最多只能有一个提交按钮");
//        }

        //对于后面的任务节点，至少分配了处理
        for (int i = 1; i < models.size(); i++) {
            OperationModel task = models.get(i);
            if (StringHelper.isEmpty(task.getDisplayName())) {
                throw new WorkflowException("功能节点[" + task.getName() + "]显示名称不能为空");
            }
            if (StringHelper.isEmpty(task.getName())) {
                throw new WorkflowException("功能节点[" + task.getDisplayName() + "]名称不能为空");
            }
            if (StringHelper.isEmpty(task.getBusiId())) {
                throw new WorkflowException("功能节点[" + task.getDisplayName() + "]绑定功能不能为空");
            }
//            if (StringHelper.isEmpty(task.getActorIds())
//                    && StringHelper.isEmpty(task.getRoleIds())
//                    && StringHelper.isEmpty(task.getUserAssignmentHandler())
//                    && StringHelper.isEmpty(task.getRoleAssignmentHandler())
//                    && StringHelper.isEmpty(task.getEnvTask())) {
//                throw new WorkflowException("功能节点[" + task.getDisplayName() + "]至少应该配置审批角色或审批人或任务处理器或者上下文");
//            }
            //任务必须配置按钮
//            if (StringHelper.isEmpty(task.getBtns())) {
//                throw new WorkflowException("功能节点[" + task.getDisplayName() + "]必须配置审批按钮");
//            }
            //任务必须配置按钮
//            String btns = task.getBtns();
//            String[] btnsArr = btns.split(",");
//
//            if (ArrayUtils.contains(btnsArr, ProcessInstanceConstant.REJECT)
//                    && CollectionUtils.isEmpty(task.getRejectTaskSelect())) {
//                throw new WorkflowException("功能节点[" + task.getDisplayName() + "]配置了驳回按钮，未配置驳回节点");
//            }

            //后面的节点，不能包含提交按钮
//            if (StringHelper.isNotEmpty(task.getBtns()) && Arrays.asList(task.getBtns().split(",")).contains("0")) {
//                throw new WorkflowException("功能节点[" + task.getDisplayName() + "]不能包含提交按钮");
//            }

            //如果当前节点启用了指定用户, 那么必须配置角色
//            if ("1".equalsIgnoreCase(task.useSpecifiedUser()) && StringHelper.isEmpty(task.getRoleIds())) {
//                throw new WorkflowException("功能节点[" + task.getDisplayName() + "]启用了指定参与者, 必须配置参与者属于角色");
//            }
        }
        List<TaskModel> approveModels = processModel.getModels(TaskModel.class);
        if (approveModels.isEmpty()) {
            throw new WorkflowException("流程至少应该包含审批节点");
        }
        for (TaskModel approveModel : approveModels) {
            if (StringHelper.isEmpty(approveModel.getWorkflow())) {
                throw new WorkflowException("审批节点审批流不能为空");
            }
            if (approveModel.getInputs().size() > 1) {
                throw new WorkflowException("审批节点的前置节点不能有多个");
            }
        }
    }

    private static DecisionModel parseDecisionModel(Map<String, String> attrCacheMap) {
        DecisionModel decisionModel = new DecisionModel();
        //decisionModel.setExpr(attrCacheMap.get(NodeParserConstant.ATTR_EXPR));
        decisionModel.setHandleClass(attrCacheMap.get(NodeParserConstant.DECISION_HANDLER));
        decisionModel.setEnv(attrCacheMap.get(NodeParserConstant.DECISION_ENV));
        fillCommonProperties(decisionModel, attrCacheMap);
        return decisionModel;
    }

    private static void validateDecisionModels(ProcessModel processModel) {
        List<DecisionModel> models = processModel.getModels(DecisionModel.class);
        models.forEach(ModelParser::validateDecisionModel);
    }

    private static void validateDecisionModel(DecisionModel decisionModel) {
        if (!StringHelper.isEmpty(decisionModel.getHandleClass())) {
            return;
        }
        List<TransitionModel> outputs = decisionModel.getOutputs();

        boolean hasExpr = false;
        boolean hasEnvExp = false;
        for (TransitionModel item : outputs) {
            if (!StringHelper.isEmpty(item.getExpr())) {
                hasExpr = true;
            }
            if (!StringHelper.isEmpty(item.getEnvExp())) {
                hasEnvExp = true;
            }
        }

        // 线全部配置条件
        for (TransitionModel item : outputs) {
            // 有一个则需要全部存在
            if (hasExpr) {
                if (StringHelper.isEmpty(item.getExpr())) {
                    throw new RuntimeException("唯一网关【" + decisionModel.getDisplayName() + "】的条件配置不完整");
                }
            }
            if (hasEnvExp) {
                if (StringHelper.isEmpty(item.getEnvExp())) {
                    throw new RuntimeException("唯一网关【" + decisionModel.getName() + "】的条件配置不完整");
                }
            }
        }
    }

    private static TaskModel parseTaskModel(Map<String, String> attrCacheMap) {
        TaskModel taskModel = new TaskModel();
        taskModel.setFormId(attrCacheMap.get(NodeParserConstant.ATTR_FORM_ID));
        taskModel.setEnvTask(attrCacheMap.get(NodeParserConstant.ATTR_TASK_ENV));
        taskModel.setUpdateEnvTask(attrCacheMap.get(NodeParserConstant.ATTR_TASK_UPDATE_ENV));
        taskModel.setQuartzExpr(attrCacheMap.get(NodeParserConstant.ATTR_QUARTZ_EXPR));
        taskModel.setDynamicFormId(attrCacheMap.get(NodeParserConstant.ATTR_DYNAMIC_FORM_ID));
        taskModel.setFormUrl(attrCacheMap.get(NodeParserConstant.ATTR_FORM_URL));
//        taskModel.setUserAssignmentHandler(attrCacheMap.get(NodeParserConstant.ATTR_USER_ASSIGNEE_HANDLER));
//        taskModel.setRoleAssignmentHandler(attrCacheMap.get(NodeParserConstant.ATTR_ROLE_ASSIGNEE_HANDLER));
        taskModel.setActorIds(attrCacheMap.get(NodeParserConstant.ATTR_ACTORS));
        taskModel.setRoleIds(attrCacheMap.get(NodeParserConstant.ATTR_ROLES));
        taskModel.setBtns(attrCacheMap.get(NodeParserConstant.ATTR_BTNS_IDS));
        taskModel.setActorSql(attrCacheMap.get(NodeParserConstant.ATTR_ACTOR_SQL));
        taskModel.setFields(attrCacheMap.get(NodeParserConstant.FIELDS));
        taskModel.setDetailFieldsText(attrCacheMap.get(NodeParserConstant.DETAIL_FIELDS_TEXT));
        taskModel.setDetailFields(attrCacheMap.get(NodeParserConstant.DETAIL_FIELDS));
        taskModel.setFieldsText(attrCacheMap.get(NodeParserConstant.FIELDSTEXT));
        taskModel.setEnableAttachment(attrCacheMap.get(NodeParserConstant.ENABLE_ATTACHMENT));
        taskModel.setRefuseHandler(attrCacheMap.get(NodeParserConstant.REFUSE_HANDLER));
        taskModel.setApplyRefuseHandler(attrCacheMap.get(NodeParserConstant.APPLY_REFUSE_HANDLER));
        taskModel.setWorkflow(attrCacheMap.get(NodeParserConstant.WORKFLOW));

        String useRole = attrCacheMap.get(NodeParserConstant.USE_ROLE);
        if (StringHelper.isEmpty(useRole)) {
            useRole = "1"; //为了不影响以前的流程, 默认值设置为启用
        }
        taskModel.setUseRole(useRole);

        String specifiedUser = attrCacheMap.get(NodeParserConstant.SPECIFIED_USER);
        if (StringHelper.isEmpty(specifiedUser)) {
            specifiedUser = "0"; //为了不影响以前的流程, 默认值设置为不启用
        }
        taskModel.setSpecifiedUser(specifiedUser);

        //taskModel.setRejectTaskSelect();
        fillCommonProperties(taskModel, attrCacheMap);
        //解析当前任务可以驳回的任务
        String rejectTasknames = attrCacheMap.get(NodeParserConstant.REJECT_TASKNAMES);
        if (StringHelper.isNotEmpty(rejectTasknames)) {
            String[] rejectTasknamesArr = rejectTasknames.split(",");
            String[] rejectTaskValuesArr = attrCacheMap.get(NodeParserConstant.REJECTTASKVALUES).split(",");
            List<SelectEntity> selects = new ArrayList<>();
            for (int i = 0; i < rejectTasknamesArr.length; i++) {
                selects.add(new SelectEntity(rejectTasknamesArr[i], rejectTaskValuesArr[i]));
            }
            taskModel.setRejectTaskSelect(selects);
        }
        return taskModel;
    }


    private static OperationModel parseOperationModel(Map<String, String> attrCacheMap) {
        OperationModel operationModel = new OperationModel();
        operationModel.setActorIds(attrCacheMap.get(NodeParserConstant.ATTR_ACTORS));
        operationModel.setRoleIds(attrCacheMap.get(NodeParserConstant.ATTR_ROLES));
        operationModel.setBtns(attrCacheMap.get(NodeParserConstant.ATTR_BTNS_IDS));
        operationModel.setFields(attrCacheMap.get(NodeParserConstant.FIELDS));
        operationModel.setDetailFieldsText(attrCacheMap.get(NodeParserConstant.DETAIL_FIELDS_TEXT));
        operationModel.setDetailFields(attrCacheMap.get(NodeParserConstant.DETAIL_FIELDS));
        operationModel.setFieldsText(attrCacheMap.get(NodeParserConstant.FIELDSTEXT));
        operationModel.setEnableAttachment(attrCacheMap.get(NodeParserConstant.ENABLE_ATTACHMENT));
        operationModel.setRefuseHandler(attrCacheMap.get(NodeParserConstant.REFUSE_HANDLER));
        operationModel.setApplyRefuseHandler(attrCacheMap.get(NodeParserConstant.APPLY_REFUSE_HANDLER));
        operationModel.setBusiId(attrCacheMap.get(NodeParserConstant.BUSI_ID));

        fillCommonProperties(operationModel, attrCacheMap);
        // 解析当前任务可以驳回的任务
        String rejectTasknames = attrCacheMap.get(NodeParserConstant.REJECT_TASKNAMES);
        if (StringHelper.isNotEmpty(rejectTasknames)) {
            String[] rejectTasknamesArr = rejectTasknames.split(",");
            String[] rejectTaskValuesArr = attrCacheMap.get(NodeParserConstant.REJECTTASKVALUES).split(",");
            List<SelectEntity> selects = new ArrayList<>();
            for (int i = 0; i < rejectTasknamesArr.length; i++) {
                selects.add(new SelectEntity(rejectTasknamesArr[i], rejectTaskValuesArr[i]));
            }
            operationModel.setRejectTaskSelect(selects);
        }
        return operationModel;
    }


    /**
     * 解析connection,并且把对应的关系设置到processModel.nodes的input和output里
     *
     * @param processModel
     * @param connectionsInfo
     */
    private static void handleConnection(ProcessModel processModel, JsonNode connectionsInfo) {
        final Iterator<JsonNode> TransitionElems = connectionsInfo.elements();
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
        };
        try {
            while (TransitionElems.hasNext()) {
                final JsonNode next = TransitionElems.next();

                Map<String, String> attrCacheMap = mapper.readValue(next.get("attrCache").toString(), typeRef);

                TransitionModel transitionModel = new TransitionModel();
                String from = next.get("from").textValue();
                NodeModel fromModel = processModel.getNode(from);
                String to = next.get("to").textValue();
                NodeModel toModel = processModel.getNode(to);

                transitionModel.setName(attrCacheMap.get(NodeParserConstant.ATTR_NAME));
                transitionModel.setDisplayName(attrCacheMap.get(NodeParserConstant.ATTR_DISPLAYNAME));
                transitionModel.setEnvExp(attrCacheMap.get(NodeParserConstant.TRANSITION_ENV_EXPR));
                //transitionModel.setExpr(attrCacheMap.get(NodeParserConstant.ATTR_EXPR));//线上的连接条件
                transitionModel.setExprType(attrCacheMap.get(NodeParserConstant.ATTR_EXPR_TYPE));
                transitionModel.setExprKey(attrCacheMap.get(NodeParserConstant.ATTR_EXPR_KEY));
                transitionModel.setExprValue(attrCacheMap.get(NodeParserConstant.ATTR_EXPR_VALUE));
                transitionModel.setExprCondition(attrCacheMap.get(NodeParserConstant.ATTR_EXPR_CONDITION));
                transitionModel.setSource(fromModel);
                transitionModel.setTarget(toModel);
                transitionModel.setTo(to);

                fromModel.getOutputs().add(transitionModel);
                toModel.getInputs().add(transitionModel);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static StartModel parseStartModel(Map<String, String> attrCacheMap) {
        StartModel startModel = new StartModel();
        fillCommonProperties(startModel, attrCacheMap);
        return startModel;
    }

    private static EndModel parseEndModel(Map<String, String> attrCacheMap) {
        EndModel endModel = new EndModel();
        fillCommonProperties(endModel, attrCacheMap);
        //如果是微服务，那么必须配置回调
        if ("true".equalsIgnoreCase(Global.getGlobalConf("IS_SERVICE"))) {
            String busiUrl = attrCacheMap.get(NodeParserConstant.BUSI_URL);
            if (StringHelper.isEmpty(busiUrl)) {
                throw new WorkflowException("结束节点回调处理器不能为空");
            }
            endModel.setBusiUrl(busiUrl);
        }

        return endModel;
    }

    private static ForkModel parseForkModel(Map<String, String> attrCacheMap) {
        ForkModel forkModel = new ForkModel();
        forkModel.setForkHandler(attrCacheMap.get(NodeParserConstant.FORK_HANDLER));
        fillCommonProperties(forkModel, attrCacheMap);
        return forkModel;
    }

    private static JoinModel parseJoinModel(Map<String, String> attrCacheMap) {
        JoinModel joinModel = new JoinModel();
        fillCommonProperties(joinModel, attrCacheMap);
        return joinModel;
    }

    private static void fillCommonProperties(NodeModel nodeModel, Map<String, String> attrCacheMap) {
        nodeModel.setName(attrCacheMap.get(NodeParserConstant.ATTR_NAME));
        nodeModel.setDisplayName(attrCacheMap.get(NodeParserConstant.ATTR_DISPLAYNAME));
        nodeModel.setPreInterceptors(attrCacheMap.get(NodeParserConstant.ATTR_PREINTERCEPTORS));
        nodeModel.setPostInterceptors(attrCacheMap.get(NodeParserConstant.ATTR_POSTINTERCEPTORS));
    }

}
