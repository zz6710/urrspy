package com.kayak.pms.opFlow.engine.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.helper.JsonHelper;
import com.kayak.helper.StringHelper;
import com.kayak.utils.DateHelper;
import com.kayak.pms.opFlow.dao.OpSqlConfigDao;
import com.kayak.pms.opFlow.dao.OpfLogDao;
import com.kayak.pms.opFlow.engine.constant.ActionType;
import com.kayak.pms.opFlow.engine.constant.OperationTypeConstant;
import com.kayak.pms.opFlow.engine.constant.ProcessInstanceConstant;
import com.kayak.pms.opFlow.engine.constant.ProcessInstanceStatus;
import com.kayak.pms.opFlow.engine.dao.*;
import com.kayak.pms.opFlow.engine.entity.Process;
import com.kayak.pms.opFlow.engine.entity.*;
import com.kayak.pms.opFlow.engine.entity.vo.TaskNodeModelVo;
import com.kayak.pms.opFlow.engine.model.*;
import com.kayak.pms.opFlow.engine.parser.ModelParser;
import com.kayak.pms.opFlow.model.OpSqlCheckConfig;
import com.kayak.pms.opFlow.model.OpSqlConfig;
import com.kayak.pms.opFlow.model.OpfLog;
import com.kayak.pms.opFlow.sql.SqlCheck;
import com.kayak.pms.opFlow.sql.SqlConfig;
import com.kayak.pms.opFlow.sql.XmlSqlDao2;
import com.kayak.pms.prodLiquidation.dao.ProdFlowDao;
import com.kayak.pms.prodLiquidation.model.ProdFlow;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by daniel on 20/03/2017.
 */
@Service("processInstanceService")
@APIDefine(desc = "流程实例执行服务", model = ProcessInstance.class)
public class ProcessInstanceService extends ComnDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProcessInstanceDao processInstanceDao;
    @Autowired
    ProcessDao processDao;
    @Autowired
    TaskDao taskDao;
    @Autowired
    OpSqlConfigDao opSqlConfigDao;
    @Autowired
    SubmitParamsDao submitParamsDao;
    @Autowired
    OpfLogDao opfLogDao;
    @Autowired
    XmlSqlDao2 xmlSqlDao2;

    @Autowired
    FormDataService formDataService;
    @Autowired
    TaskService taskService;
    @Autowired
    WorkflowEngine workflowEngine;
    @Autowired
    private ProdFlowDao prodFlowDao;
    @Autowired
    private TaskActorDao taskActorDao;

    @API(desc = "查询我发起的流程", auth = APIAuth.NO)
    public SqlResult<ProcessInstance> findByCreator(SqlParam<ProcessInstance> params) throws Exception {
        return processInstanceDao.findByCreator(params);
    }

    @API(desc = "查询我参与的流程", auth = APIAuth.NO)
    public SqlResult<ProcessInstance> findByJoin(SqlParam<ProcessInstance> params) throws Exception {
        return processInstanceDao.findByJoin(params);
    }



    @API(desc = "查询是否有待完成的任务",auth = APIAuth.NO)
    public String existsTask(SqlParam<ProcessInstance> param) throws Exception {
        Map<String, Object> map = new HashMap<>();
        // 默认是要申请审批，前端已经判断了下一个节点（跳过网关节点）就是审批节点
        map.put("exists", false);
        ProcessInstance processInstance = param.getModel();
        // 判断实例有没有创建，实例有创建，则进一步判断；实例没创建，但下个节点又是审批节点，默认就是要申请审批
        if (StringHelper.isNotEmpty(param.getModel().getProcessInstanceId()) && param.getModel().getProcessInstanceId().length()==32) {
            Process processByVersion = processDao.getProcessByVersion(processInstance.getProcessId(), processInstance.getProcessVersion().toString());
            ProcessModel processModel = ModelParser.parse(processByVersion.getJson());
            NodeModel node = processModel.getNode(processInstance.getCurrentNode());
            List<NodeModel> nextModels = node.getDirectNextModels(NodeModel.class);
            NodeModel nextNode = processModel.getNode(processInstance.getNextNode());
            // 节点层面判断，下一个直接节点是不是审批节点，是则需要申请审批；否则再从子实例任务层面判断
            // 直连：判断审批节点input有多个； 间接连接：判断中间网关节点input有多个； 然后才判断子任务是否完成
            if ((node.nextIs(nextNode) && nextNode.getInputs().size() > 1) || nextModels.get(0).getInputs().size() > 1) {
                // 存在则查询数据库
                processInstance = processInstanceDao.getProcessInstanceById(param.getModel().getProcessInstanceId());
                // 如果是主实例，则直接查下一个审批节点，如果是子实例，则查询未完成的子流程实例为0才查下一个审批节点（不包含本身）
                if (StringHelper.isNotEmpty(processInstance.getMasterId()) && processInstanceDao.countByNotFinish(processInstance) > 0) {
                    map.put("exists", true);
                }
            }
        }
        return RequestSupport.updateReturnJson(true, "", map).toString();
    }

    @API(desc = "执行任务的保存", auth = APIAuth.NO)
    public String save(SqlParam<ProcessInstance> params) throws Exception {
        Map<String, Object> paramsDirect = params.getParamsDirect();
        ProcessInstance processInstance = params.getModel();
        // 解析表单数据
        Map<String, Object> formDataMap = JsonHelper.fromJson(processInstance.getSubmitParams(), Map.class);
        // 根据流程id+版本号查询流程图
        Process process = processDao.getProcessByVersion(processInstance.getProcessId(), processInstance.getProcessVersion());
        if (process == null) {
            return RequestSupport.updateReturnJson(false, "找不到该流程，创建流程实例失败", null).toString();
        }
        logger.info("=================开始执行保存，当前流程实例：{}", processInstance.getProcessInstanceId());
        
        try {
            // 开始事务
            doTrans(() -> {
                // 生成任务
                TaskNodeModelVo taskNodeModelVo = this.getTask(process, processInstance, formDataMap, paramsDirect);
                // 保存表单数据
                SubmitParams submitParams = this.saveFormData(processInstance, formDataMap, taskNodeModelVo.getTaskList().get(0).getId());
                this.doExecSql(taskNodeModelVo.getTaskList().get(0).getBusiId(), ActionType.SAVE, formDataMap);

                // 更新状态（包含更新时间、更新者）
                processInstance.setCurrentStatus(ProcessInstanceStatus.RUNNING);
                processInstanceDao.updateStatus(processInstance);
                // 获取后续节点名称
                String nextNode = taskNodeModelVo.getOperationModel().getAllNextModels(WorkModel.class).stream().map(BaseModel::getDisplayName).collect(Collectors.joining("|"));
                if (StringHelper.isEmpty(nextNode)) {
                    nextNode = taskNodeModelVo.getOperationModel().getDisplayName();
                } else {
                    nextNode = taskNodeModelVo.getOperationModel().getDisplayName() + "|" + nextNode;
                }
                //更新流程实例id到 t8_prod_flow表中
                String prodCode = (String) paramsDirect.get("prodCode");
                if (StringUtils.isNotEmpty(prodCode)) {
                    processInstanceDao.update("UPDATE t8_prod_flow SET op_process_id = '"+processInstance.getProcessInstanceId()+"' WHERE type != '2' AND prod_code = '"+prodCode+"'",prodCode);
                }
                // 记录操作日志
                opfLogDao.addLog(this.buildOpfLog(OperationTypeConstant.SAVE, "", processInstance, taskNodeModelVo.getTaskList().get(0), submitParams.getId(), nextNode));
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RequestSupport.updateReturnJson(false, "保存失败："+e.getMessage(), null).toString();
        }

        return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
    }

    @API(desc = "执行任务的转交", auth = APIAuth.NO)
    public String transfer(SqlParam<ProcessInstance> params) throws Exception {
        Map<String, Object> paramsDirect = params.getParamsDirect();
        ProcessInstance processInstance = params.getModel();
        // 解析表单数据
        Map<String, Object> formDataMap = JsonHelper.fromJson(processInstance.getSubmitParams(), Map.class);
        // 根据流程id+版本号查询流程图
        Process process = processDao.getProcessByVersion(processInstance.getProcessId(), processInstance.getProcessVersion());
        if (process == null) {
            return RequestSupport.updateReturnJson(false, "找不到该流程，创建流程实例失败", null).toString();
        }
        logger.info("=================开始执行转交，当前流程实例：{}", processInstance.getProcessInstanceId());
        try {
            // 开始事务
            doTrans(() -> {
                // 生成任务，有则获取
                TaskNodeModelVo taskNodeModelVo = this.getTask(process, processInstance, formDataMap, paramsDirect);
                Task task = taskNodeModelVo.getTaskList().get(0);
                // 保存表单数据
                SubmitParams submitParams = this.saveFormData(processInstance, formDataMap, task.getId());
                //获取转交用户id
                String transferUserid = (String) paramsDirect.get("transferUserid");
                logger.info("被转交的用户id：{}",transferUserid);
                //删除转交人员的待办
                taskActorDao.removeTaskActorByTaskId(task.getId());
                //生成被转交人员的待办
                if (StringUtils.isNotBlank(transferUserid)) {
                    TaskActor taskActor = new TaskActor();
                    logger.info("转交的任务id：{}",task.getId());
                    taskActor.setId(StringHelper.getPrimaryKey());
                    taskActor.setTaskId(task.getId());
                    taskActor.setActorType("2");
                    taskActor.setActorId(transferUserid);
                    taskActorDao.saveTaskActor(taskActor);
                }
                //更新流程状态为已发起
                String prodCode = (String) paramsDirect.get("prodCode");
                if (StringUtils.isNotEmpty(prodCode)) {
                    prodFlowDao.updateProdFlowStatusAndProcess("2",processInstance.getProcessInstanceId(),prodCode);
                }
                //获取用户信息
                SqlRow row = opfLogDao.findUsernameByUserid(transferUserid);
                String username = row != null ? row.getString("username") : "";
                // 记录操作日志
                opfLogDao.addLog(this.buildOpfLog(OperationTypeConstant.TRANSFER, "转交给：" + username, processInstance, task, null, task.getDisplayName()));
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RequestSupport.updateReturnJson(false, "转交任务失败："+e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "转交任务成功", null).toString();
    }


    @API(desc = "执行任务的提交", auth = APIAuth.NO)
    public String submit(SqlParam<ProcessInstance> params) throws Exception {
        Map<String, Object> paramsDirect = params.getParamsDirect();
        ProcessInstance processInstance = params.getModel();
        // 解析表单数据
        Map<String, Object> formDataMap = JsonHelper.fromJson(processInstance.getSubmitParams(), Map.class);
        // 根据流程id+版本号查询流程图
        Process process = processDao.getProcessByVersion(processInstance.getProcessId(), processInstance.getProcessVersion());
        if (process == null) {
            return RequestSupport.updateReturnJson(false, "找不到该流程", null).toString();
        }
        logger.info("=================开始执行提交，当前流程实例：{}", processInstance.getProcessInstanceId());
        
        // 开始事务
        try {
            doTrans(() -> {
                // 生成任务，有则获取
                TaskNodeModelVo taskNodeModelVo = this.getTask(process, processInstance, formDataMap, paramsDirect);
                Task task = taskNodeModelVo.getTaskList().get(0);
                // 保存表单数据
                SubmitParams submitParams = this.saveFormData(processInstance, formDataMap, task.getId());
                // 执行表单配置的sql
                this.doExecSql(task.getBusiId(), ActionType.SUBMIT, formDataMap);
                // 完成当前任务（移入历史表）
                taskService.complete(task);
                // 流程实例流转
                this.startNext(taskNodeModelVo.getOperationModel(), processInstance, taskNodeModelVo.getExecution());
                // 获取下个节点
                String nextNode = taskNodeModelVo.getOperationModel().getAllNextModels(WorkModel.class).stream().map(BaseModel::getDisplayName).collect(Collectors.joining("|"));
                // 记录操作日志
                opfLogDao.addLog(this.buildOpfLog(OperationTypeConstant.SUBMIT, "", processInstance, taskNodeModelVo.getTaskList().get(0), submitParams.getId(), nextNode));
                processInstance.setCurrentStatus("1");//处理因审批拒绝的状态
                processInstanceDao.updateStatus(processInstance);//更新状态为进行中
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RequestSupport.updateReturnJson(false, "提交失败：" + e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "提交成功", null).toString();
    }

    @API(desc = "执行任务的终止", auth = APIAuth.NO)
    public String end(SqlParam<ProcessInstance> params) throws Exception {
        Map<String, Object> paramsDirect = params.getParamsDirect();
        ProcessInstance processInstance = params.getModel();
        // 解析表单数据
        Map<String, Object> formDataMap = JsonHelper.fromJson(processInstance.getSubmitParams(), Map.class);
        String taskId = (String) paramsDirect.get("taskId");
        logger.info("=================开始执行终止，当前流程实例：{}", processInstance.getProcessInstanceId());
        //1.获取当前流程的实例id(清盘)
        String processInstanceId =processInstance.getProcessInstanceId();
        try {
            // 开始事务，保存表单数据，执行sql
            doTrans(() -> {
                // 查询任务
                Task task = taskDao.getTaskById(taskId);
                if (task == null) {
                    throw new PromptException("任务不存在或已执行完成，请勿重复提交");
                };
                //2.根据流程实例ID查询清盘流程表数据
                if (StringUtils.isNotBlank(processInstanceId)) {
                    ProdFlow prodFlow = prodFlowDao.findProdFlowByProcessId(processInstanceId);
                    if (prodFlow != null){
                        //3.更新流程状态为已终止 5且 复制一份数据type为2
                        prodFlowDao.updateProdFlowById(prodFlow.getOpProcessId());
                        prodFlow.setType("2");//用于流程追踪列表查询展示
                        prodFlowDao.addProdFlowLiquidation1(prodFlow);
                    }
                }
                // 保存表单数据
                SubmitParams submitParams = this.saveFormData(processInstance, formDataMap, taskId);
                // 执行表单对应sql
                this.doExecSql(task.getBusiId(), ActionType.END, formDataMap);
                // 当前任务不执行，状态变为终止
                taskService.complete(task);
                // 流程实例状态也变成终止
                processInstanceDao.terminal(processInstance);
                // 记录操作日志
                opfLogDao.addLog(this.buildOpfLog(OperationTypeConstant.END, "", processInstance, task, submitParams.getId(), null));

            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RequestSupport.updateReturnJson(false, "任务终止失败：" + e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "任务终止成功", null).toString();
    }

    @API(desc = "执行任务的回退", auth = APIAuth.NO)
    public String back(SqlParam<ProcessInstance> params) throws Exception {
        Map<String, Object> paramsDirect = params.getParamsDirect();
        ProcessInstance processInstance = params.getModel();
        // 解析表单数据
        Map<String, Object> formDataMap = JsonHelper.fromJson(processInstance.getSubmitParams(), Map.class);
        String taskId = (String) paramsDirect.get("taskId");
        // 根据流程id+版本号查询流程图
        Process process = processDao.getProcessByVersion(processInstance.getProcessId(), processInstance.getProcessVersion());
        if (process == null) {
            return RequestSupport.updateReturnJson(false, "找不到该流程", null).toString();
        }
        ProcessInstance processInstanceById = processInstanceDao.getProcessInstanceById(processInstance.getProcessInstanceId());
        if (processInstanceById == null) {
            return RequestSupport.updateReturnJson(false, "流程实例不存在", null).toString();
        }
        processInstance.setParentProcessId(processInstanceById.getParentProcessId());
        logger.info("=================开始执行回退，当前流程实例：{}", processInstance.getProcessInstanceId());
        
        try {
            // 开始事务，保存表单数据，执行sql
            doTrans(() -> {
                // 查询任务
                List<Task> taskList = taskDao.getTasksByProcessInstanceId(processInstance.getProcessInstanceId());
                Map<String, List<Task>> taskMap = taskList.stream().collect(Collectors.groupingBy(Task::getId));
                if (taskMap.get(taskId) == null || taskMap.get(taskId).isEmpty()) {
                    throw new PromptException("任务不存在或已执行完成，请勿重复提交");
                }
                if (taskMap.size() > 1) {
                    throw new PromptException("当前流程存在其他进行中的任务！回退失败");
                }
                Task task = taskMap.get(taskId).get(0);
                // 保存表单数据
                SubmitParams submitParams = this.saveFormData(processInstance, formDataMap, taskId);
                // 执行表单对应sql
                this.doExecSql(task.getBusiId(), ActionType.BACK, formDataMap);
                // 当前任务回退(删除任务，移入历史表)
                taskService.complete(task);
                // 流程状态置为回退
                processInstanceDao.back(processInstance);
                // 生成上一个节点的待办任务
                OperationModel operationModel = this.createPrevTask(process, processInstance, paramsDirect, task);
                String nextNode = operationModel.getAllNextModels(WorkModel.class).stream().map(BaseModel::getDisplayName).collect(Collectors.joining("|"));
                // 记录操作日志
                opfLogDao.addLog(this.buildOpfLog(OperationTypeConstant.BACK, "", processInstance, task, submitParams.getId(), nextNode));
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RequestSupport.updateReturnJson(false, "回退上一步失败：上一步为审批任务或" + e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "回退上一步成功", null).toString();
    }

    /**
     * 生成任务或查询任务 并返回
     * @param process
     * @param processInstance
     * @param formDataMap
     * @param paramsDirect
     * @return
     * @throws Exception
     */
    private TaskNodeModelVo getTask(Process process, ProcessInstance processInstance, Map<String, Object> formDataMap, Map<String, Object> paramsDirect) throws Exception {
        // 节点或任务名，生成任务和查找任务时用到
        String nodeName = (String) paramsDirect.get("nodeName");
        String taskId = (String) paramsDirect.get("taskId");
        logger.info("====================开始查找任务，节点名：{}，任务id：{}", nodeName, taskId);
        // 解析流程图节点json串
        ProcessModel processModel = ModelParser.parse(process.getJson());
        // 注意，解析并没有把节点加载到列表，这里get时就会加载
        OperationModel operationModel = processModel.getOperationNode(nodeName);
        process.setProcessModel(processModel);
        Execution execution;
        List<Task> taskList = new ArrayList<>();
        // 判断是否生成过流程实例（首次保存肯定没有生成流程实例,大于32位是因为首次提交时前端生成的），没有则生成流程实例
        if (StringHelper.isEmpty(processInstance.getProcessInstanceId()) || processInstance.getProcessInstanceId().length()>32) {
            // 生成流程实例
            execution = this.createInstanceAndExecution(processInstance, formDataMap, process);
            String processInstanceId = processInstance.getProcessInstanceId();
            //根据流程id查询产品代码
            SqlRow sqlRow = submitParamsDao.findRow("SELECT id FROM t8_prod_info WHERE prod_code =" +
                    "( SELECT prod_code FROM t8_prod_flow WHERE op_process_id='"+processInstanceId+"' )",processInstanceId);
            if (sqlRow != null) {
                String id = sqlRow.getString("id");
                //生成与产品相关的待办任务
                taskList.addAll(taskService.createOperationTaskNew(operationModel,execution,id));
            }else {
                // 生成一条待办任务
                taskList.addAll(taskService.createOperationTask(operationModel, execution));
            }
//            taskList.addAll(taskService.createOperationTask(operationModel, execution));
        } else {
            ProcessInstance processInstanceById = processInstanceDao.getProcessInstanceById(processInstance.getProcessInstanceId());
            if (processInstanceById != null) {
                processInstance.setParentProcessId(processInstanceById.getParentProcessId());
                processInstance.setCurrentNode(processInstanceById.getCurrentNode());
                processInstance.setLastNode(processInstanceById.getLastNode());
                processInstance.setNextNode(processInstanceById.getNextNode());
            }
            execution = new Execution(workflowEngine, process, processInstance, paramsDirect);
            // 判断是否生成过任务
            if (StringHelper.isNotEmpty(taskId)) {
                // 从数据库查询出任务
                Task task = taskDao.getTaskById(taskId);
                if (task == null) {
                    throw new PromptException("任务不存在或已执行完成，请勿重复提交");
                }
                taskList.add(task);
                execution.setTask(task);
            } else {
                //根据流程id查询产品代码
                String processInstanceId = processInstance.getProcessInstanceId();
                SqlRow sqlRow = submitParamsDao.findRow("SELECT id FROM t8_prod_info WHERE prod_code =" +
                        "( SELECT prod_code FROM t8_prod_flow WHERE op_process_id='"+processInstanceId+"' )",processInstanceId);
                if (sqlRow != null) {
                    String id = sqlRow.getString("id");
                    //生成与产品相关的待办任务
                    taskList.addAll(taskService.createOperationTaskNew(operationModel,execution,id));
                }else {
                    // 生成一条待办任务
                    taskList.addAll(taskService.createOperationTask(operationModel, execution));
                }
                // 生成一条待办任务
//                taskList.addAll(taskService.createOperationTask(operationModel, execution));
            }
        }
        return new TaskNodeModelVo(taskList, operationModel, execution);
    }

    private OperationModel createPrevTask(Process process, ProcessInstance processInstance, Map<String, Object> paramsDirect, Task task) throws Exception {
        logger.info("===================因回退而生成上一节点的任务，当前节点为【{}】", paramsDirect.get("nodeName"));
        String nodeName = (String) paramsDirect.get("nodeName");
        // 解析流程图节点json串
        ProcessModel processModel = ModelParser.parse(process.getJson());
        OperationModel operationModel = processModel.getOperationNode(nodeName);
        if (operationModel != null) {
            // 获取上个节点
            List<OperationModel> prevModels = operationModel.getPrevModels(OperationModel.class);
            if (prevModels.isEmpty()) {
                throw new PromptException("上一任务不存在！");
            } else if (prevModels.size() == 1) {
                OperationModel prevModel = prevModels.get(0);
                // 如果上一节点只有一个，则要判断上一节点之下有没有多个分支节点
                List<OperationModel> nextModels = prevModel.getDirectNextModels(OperationModel.class);
                List<OperationModel> prevPrevModels = prevModel.getPrevModels(OperationModel.class);
                // 如果有多个分支节点，则需要每个分支节点的任务都回退了，才能生成上一节点的任务
                if (nextModels.size()>1 && processInstanceDao.countByNotBack(processInstance) == 0) {
                    // 创建前一个任务
                    Task preTask = taskService.createOperationTaskFromParent(prevModel, task.getParentTaskId());
                    // 顺便将表单也复制生成一份
                    this.saveFormDataByFind(preTask.getId(), task.getParentTaskId());
                    // 上级流程实例流转
                    this.updateInstanceNode(prevPrevModels.stream().map(OperationModel::getDisplayName).collect(Collectors.joining(",")),
                            prevModel.getDisplayName(),
                            nextModels.stream().map(OperationModel::getDisplayName).collect(Collectors.joining(",")),
                            processInstance.getParentProcessId());
                } else if (nextModels.size()==1) {
                    // 只有一个分支节点，则直接回退
                    // 创建前一个任务
                    Task preTask = taskService.createOperationTaskFromParent(prevModel, task.getParentTaskId());
                    // 顺便将表单也复制生成一份
                    this.saveFormDataByFind(preTask.getId(), task.getParentTaskId());
                    // 流转流程实例节点
                    this.updateInstanceNode(prevModel.getInputs().stream().map(transitionModel -> transitionModel.getSource().getDisplayName()).collect(Collectors.joining(",")),
                            prevModel.getDisplayName(),
                            operationModel.getDisplayName(),
                            processInstance.getProcessInstanceId());
                }
            } else {
                // 上级节点有多个，则生成多个task
                for (OperationModel prevModel : prevModels) {
                    ProcessInstance subInstance = processInstanceDao.getByParent(processInstance.getProcessInstanceId(), prevModel.getDisplayName());
                    if (subInstance != null) {
                        // 创建前一个任务
                        Task preTask = taskService.createOperationTaskFromParentInstance(prevModel, subInstance.getProcessInstanceId());
                        // 将表单也复制生成一份
                        this.saveFormDataByFind(preTask.getId(), preTask.getOldId());
                        // 流转子实例的节点
                        this.updateInstanceNode(prevModel.getInputs().stream().map(transitionModel -> transitionModel.getSource().getDisplayName()).collect(Collectors.joining(",")),
                                prevModel.getDisplayName(),
                                operationModel.getDisplayName(),
                                subInstance.getProcessInstanceId());
                    }
                }
                // 查找分支开始节点
                NodeModel prevModel = prevModels.get(0).getInputs().get(0).getSource();
                while (prevModel.getOutputs().size()==1) {
                    prevModel = prevModel.getInputs().get(0).getSource();
                }
                // 流转主流程实例节点
                this.updateInstanceNode(prevModel.getDisplayName(),
                        prevModels.stream().map(OperationModel::getDisplayName).collect(Collectors.joining(",")),
                        operationModel.getDisplayName(),
                        processInstance.getProcessInstanceId());
            }
        }
        return operationModel;
    }

    /**
     * 执行sql
     * @param busiId 功能id
     * @param actionType 按钮类型：0-保存、1-提交、2-流程终止、3-回退上一步
     * @param formData 各表单的提交数据
     * @throws Exception
     */
    private void doExecSql(String busiId, String actionType, Map<String, Object> formData) throws Exception {
        logger.info("===================执行表单SQL，对应按钮：{}(0-保存，1-提交，2-终止，3-回退上一步)", actionType);
        // 根据功能id，查询出里面的所有表单的sql配置
        List<OpSqlConfig> opSqlConfigList = opSqlConfigDao.findByBusiId(busiId, actionType);
        // 按每个表单分组，依次执行每个表单的sql
        Map<String, List<OpSqlConfig>> map = opSqlConfigList.stream().collect(Collectors.groupingBy(OpSqlConfig::getFormId));
        for (Map.Entry<String, List<OpSqlConfig>> entry : map.entrySet()) {
            // 取到每个表单参数
            Map<String, Object> dataMap = (Map<String, Object>) formData.get(entry.getKey());
            SqlConfig sqlConfig = new SqlConfig("", "", "1", "0", true, 0, null, null);
            for (OpSqlConfig opSqlConfig : entry.getValue()) {
                sqlConfig.addSql(opSqlConfig.getSqlStatement(), opSqlConfig.getDbType());
                List<SqlCheck> sqlChecks = new ArrayList<>();
                for (OpSqlCheckConfig checkData : opSqlConfig.getCheckData()) {
                    sqlChecks.add(new SqlCheck(
                            checkData.getCheckName(),
                            "2".equals(checkData.getCheckType())?checkData.getCheckSql():"",
                            checkData.getCheckField(),
                            checkData.getCheckTarget(),
                            checkData.getCheckSign(),
                            checkData.getCheckMsg(),
                            true,
                            Integer.parseInt(opSqlConfig.getDatasource())
                    ));
                }
                sqlConfig.setChecks(sqlChecks);
                // 依次执行sql，并执行里面的校验
//                opSqlConfigDao.execSql(opSqlConfig, dataMap);
                xmlSqlDao2.update(dataMap, sqlConfig);
            }
        }
    }

    /**
     * 流转到下个节点
     * @param workModel
     * @param processInstance
     * @param execution
     * @throws Exception
     */
    private void startNext(WorkModel workModel, ProcessInstance processInstance, Execution execution) throws Exception {
        // 获取当前节点后面的直连节点(OperationModel + TaskModel)
        List<WorkModel> nextModels = workModel.getNextModels(WorkModel.class);
        // 获取当前节点后面的直连节点(EndModel)
        List<EndModel> endModels = workModel.getDirectNextModels(EndModel.class);
        // 如果有下一个节点，则流转任务到下一个节点，并生成下一个节点任务，流程实例流转到下一个节点；否则结束流程实例
        if (!endModels.isEmpty()) {
            // 设置最后操作节点
            processInstance.setLastNode(workModel.getDisplayName());
            // 如果下个节点直接是end节点，则表示终止，需要更改流程实例对象状态为完成
            processInstanceDao.finish(processInstance);
        } else if (nextModels.size() == 1) {
            // 处理单个节点
            this.dealNextModel(nextModels.get(0), processInstance, execution, workModel);
        } else if (nextModels.size() > 1)  {
            // 处理多个节点
            this.dealNextModels(nextModels, processInstance, execution, workModel);
        }
    }

    private void dealNextModels(List<WorkModel> nextModels, ProcessInstance processInstance, Execution execution, WorkModel workModel) throws Exception {
        ProcessInstance copyInstance = ProcessInstance.copy(processInstance);
        // 生成下个节点的任务，这里将id置为null，是为了在生成分支任务时，分别生成不同的流程实例
        copyInstance.setProcessInstanceId(null);
        execution.setProcessInstance(copyInstance);
        // execute会根据当前节点output生成后续任务
        workModel.execute(execution);
        // 拿到生成的后续任务，与子流程实例作绑定
        List<Task> tasks = execution.getTasks();
        if (tasks.size() != nextModels.size()) {
            throw new PromptException("生成任务数"+tasks.size()+" 与子节点数" + nextModels.size()+" 不符");
        }
        // 如果有多个节点，生成每个节点的任务,并流转当前流程实例
        for (int i = 0; i < nextModels.size(); i++) {
            WorkModel nextModel = nextModels.get(i);
            copyInstance = ProcessInstance.copy(processInstance, true);
            // 利用生成任务时生成的流程实例id，作为创建流程实例的id
            copyInstance.setProcessInstanceId(tasks.get(i).getProcessInstanceId());
            copyInstance.setCurrentNode(nextModel.getDisplayName());
            copyInstance.setLastNode(processInstance.getCurrentNode());
            List<WorkModel> nextModels1 = nextModel.getNextModels(WorkModel.class);
            if (nextModels1.size() == 1) {
                copyInstance.setNextNode(nextModels1.get(0).getDisplayName());
            } else if (nextModels1.size() > 1) {
                copyInstance.setNextNode(nextModels1.stream().map(WorkModel::getDisplayName).collect(Collectors.joining(",")));
            } else {
                copyInstance.setNextNode(null);
            }
            // 插入一条流程实例
            processInstanceDao.createProcessInstance(copyInstance);
            // TODO 如果下个节点是审批节点，则要调用工作流那一套，发起一个审批任务
        }
        // 更新流转节点
        this.updateInstanceNode(processInstance.getCurrentNode(), nextModels.stream().map(NodeModel::getDisplayName).collect(Collectors.joining(",")), null, processInstance.getProcessInstanceId());
    }

    private void dealNextModel(WorkModel nextModel, ProcessInstance processInstance, Execution execution, WorkModel workModel) throws Exception {
        // 获取输入
        List<TransitionModel> inputs;
        // 假定不会有连续的网关节点，判断workModel和nextModel之间是否存在一个网关节点
        if (workModel.getOutputs().stream().noneMatch(transitionModel -> nextModel.equals(transitionModel.getTarget()))) {
            // 表示workModel后面间接是nextModel，则取workModel的下一直接节点的输入
            inputs = workModel.getOutputs().get(0).getTarget().getInputs();
        } else {
            // 表示workModel后面就是nextModel，直接取nextModel的输入就好了
            inputs = nextModel.getInputs();
        }
        // 表示下一节点有多各个输入节点，则需要这多个节点都提交了，才生成下一节点的任务
        if (inputs.size() > 1) {
            // 更新当前子流程实例为完成
            processInstanceDao.finish(processInstance);
            // 查询未完成的子流程实例
            if (processInstanceDao.countByNotFinish(processInstance) == 0) {
                // 流转主流程状态
                this.updateInstanceNode(workModel.getDisplayName(), nextModel.getDisplayName(), nextModel.getNextModels(WorkModel.class).stream().map(WorkModel::getDisplayName).collect(Collectors.joining(",")), processInstance.getParentProcessId());
                // 回到主流程实例，生成下个节点的任务
                ProcessInstance parentInstance = new ProcessInstance();
                parentInstance.setProcessInstanceId(processInstance.getParentProcessId());
                parentInstance.setProcessId(processInstance.getProcessId());
                execution.setProcessInstance(parentInstance);
                workModel.execute(execution);
            }
        } else {
            processInstance.setCurrentNode(nextModel.getDisplayName());
            String nextNode = null;
            List<WorkModel> nextModels1 = nextModel.getNextModels(WorkModel.class);
            if (nextModels1.size() == 1) {
                nextNode = nextModel.getDisplayName();
            } else if (nextModels1.size() > 1) {
                nextNode = nextModels1.stream().map(WorkModel::getDisplayName).collect(Collectors.joining(","));
            }
            execution.setProcessInstance(processInstance);
            // 生成下个节点的任务
            workModel.execute(execution);
            // 流转当前流程
            this.updateInstanceNode(workModel.getDisplayName(), nextModel.getDisplayName(), nextNode, processInstance.getProcessInstanceId());
        }
        // TODO 如果下个节点是审批节点，则要调用工作流那一套，发起一个审批任务
    }


    private void updateInstanceNode(String lastNode, String currNode, String nextNode, String processInstanceId) throws Exception {
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setProcessInstanceId(processInstanceId);
        processInstance.setLastNode(lastNode);
        processInstance.setCurrentNode(currNode);
        processInstance.setNextNode(nextNode);
        processInstanceDao.updateNode(processInstance);
    }

    private SubmitParams saveFormData(ProcessInstance processInstance, Map<String, Object> formDataMap, String taskId) throws Exception {
        logger.info("===================保存表单数据");
        // 字段和值一行一行保存到数据库中
        formDataService.saveFormData(formDataMap, processInstance.getProcessInstanceId(), taskId);
        // 这里保存一份数据。为再次发起申请做准备
        SubmitParams submitParams = new SubmitParams();
        submitParams.setId(StringHelper.getPrimaryKey());
        submitParams.setProcessId(processInstance.getProcessId());
        submitParams.setProcessInstanceId(processInstance.getProcessInstanceId());
        submitParams.setTaskId(taskId);
        submitParams.setSubmitParams(processInstance.getSubmitParams());
        submitParams.setCreateDate(DateHelper.getCurrentDate());
        submitParams.setCreateTime(DateHelper.getCurrentTime());
        // 根据taskId删除
        submitParamsDao.delete(submitParams);
        submitParamsDao.save(submitParams);
        return submitParams;
    }

    private void saveFormDataByFind(String taskId, String oldTaskId) throws Exception {
        formDataService.getFormDataDao().saveByFind(taskId, oldTaskId);
        submitParamsDao.saveByFind(taskId, oldTaskId);
    }

    private Execution createInstanceAndExecution(ProcessInstance processInstance, Map<String, Object> params, Process process) throws Exception {
        logger.info("==================生成流程实例，对应流程：【{}】-【{}】", process.getName(), process.getDisplayName());
//        ProcessInstance processInstance = new ProcessInstance();
        if (StringHelper.isNotEmpty(processInstance.getProcessInstanceId())) {
            processInstance.setProcessInstanceId(processInstance.getProcessInstanceId().substring(0, 32));
        } else {
            processInstance.setProcessInstanceId(StringHelper.getPrimaryKey());
        }
        processInstance.setProcessId(process.getProcessId());
        processInstance.setCreator(SysUtil.getLoginUserid());
        processInstance.setCreateDate(DateHelper.getCurrentDate());
        processInstance.setCreateTime(DateHelper.getCurrentTime());
        processInstance.setCurrentStatus(ProcessInstanceConstant.RUNNING);
        List<WorkModel> nextWorkModels = process.getProcessModel().getStart().getNextModels(WorkModel.class);
        if (nextWorkModels.size() == 1) {
            processInstance.setMasterFlag("0");
            WorkModel operationModel = nextWorkModels.get(0);
            processInstance.setCurrentNode(operationModel.getDisplayName());
            processInstance.setLastNode(operationModel.getDisplayName());
            processInstance.setNextNode(operationModel.getNextModels(WorkModel.class).stream().map(WorkModel::getDisplayName).collect(Collectors.joining(",")));
        } else if (nextWorkModels.size() > 1) {
            processInstance.setCurrentNode(nextWorkModels.stream().map(WorkModel::getDisplayName).collect(Collectors.joining(",")));
            WorkModel joinModel = nextWorkModels.get(0).getJoinModel(WorkModel.class);
            if (joinModel != null) {
                processInstance.setNextNode(joinModel.getDisplayName());
            }
            processInstance.setLastNode(processInstance.getCurrentNode());
        }
        // 插入一条流程实例
        processInstanceDao.createProcessInstance(processInstance);
        return new Execution(workflowEngine, process, processInstance, params);
    }

    private OpfLog buildOpfLog(String operationType, String remark, ProcessInstance processInstance, Task task, String formDataId, String nextNode) {
        OpfLog opfLog = new OpfLog();
        opfLog.setId(StringHelper.getPrimaryKey());
        opfLog.setOperator(SysUtil.getLoginUserid());
        opfLog.setOperationType(operationType);
        opfLog.setCreateDate(DateHelper.getCurrentDateTime());
        // 判断是主流程实例还是子流程实例
        if (StringHelper.isEmpty(processInstance.getParentProcessId())) {
            opfLog.setProcessInstanceId(processInstance.getProcessInstanceId());
        } else {
            opfLog.setProcessInstanceId(processInstance.getParentProcessId());
            opfLog.setChildrenProcessInstanceId(processInstance.getProcessInstanceId());
        }
        opfLog.setTaskId(task.getId());
        opfLog.setBusiId(task.getBusiId());
        opfLog.setRemark(remark);
        opfLog.setDisplayName(task.getDisplayName());
        opfLog.setFormDataId(formDataId);
        opfLog.setNextNode(nextNode);
        return opfLog;
    }

    /**
     * 审批通过回调：流转到下一个任务
     * @param processInstance
     */
    public void approvePass(ProcessInstance processInstance, String remark) throws Exception {
        this.dealProcessInstanceId(processInstance);
        // 根据流程id+版本号查询流程图
        Process process = processDao.getProcessByVersion(processInstance.getProcessId(), processInstance.getProcessVersion());
        if (process == null) {
            throw new PromptException("找不到该流程");
        }
        String currentNode = processInstance.getCurrentNode();
        // 解析流程图节点并获取当前审批节点
        ProcessModel processModel = ModelParser.parse(process.getJson());
        process.setProcessModel(processModel);
        TaskModel taskModel = processModel.getTaskNode(processInstance.getCurrentNode());
        // 如果当前审批涉及多个功能节点，则表示要回到主流程实例
        if (taskModel.getInputs().size()>1) {
            ProcessInstance parentProcessInstance = processInstanceDao.getParentProcessInstance(processInstance.getProcessInstanceId());
            if (parentProcessInstance == null) {
                throw new PromptException("找不到该流程实例");
            }
            ProcessInstance.copy(parentProcessInstance, processInstance);
        } else {
            ProcessInstance processInstance1 = processInstanceDao.getProcessInstanceById(processInstance.getProcessInstanceId());
            if (processInstance1 == null) {
                throw new PromptException("找不到该流程实例");
            }
            ProcessInstance.copy(processInstance1, processInstance);
        }
        // 查询操作流审批任务
        Task task = taskDao.getApproveTask(processInstance.getProcessInstanceId(), currentNode);
        Execution execution = new Execution(workflowEngine, process, processInstance, new HashMap<>());
        execution.setTask(task);

        String nextNode = taskModel.getAllNextModels(WorkModel.class).stream().map(BaseModel::getDisplayName).collect(Collectors.joining("|"));

        logger.info("=================执行审批节点的通过，当前流程实例：{}", processInstance.getProcessInstanceId());
        // 开始事务
        doTrans(() -> {
            // 完成当前任务（移入历史表）
            taskService.complete(task);
            // 流程实例流转
            this.startNext(taskModel, processInstance, execution);
            // 记录操作日志
            opfLogDao.addLog(this.buildOpfLog(OperationTypeConstant.APPROVE_PASS, remark, processInstance, task, null, nextNode));
        });
    }

    /**
     * 审批拒绝回调：回退到上一个任务（只考虑前面只有一个功能节点的情况）
     * @param processInstance
     */
    public void approveReject(ProcessInstance processInstance, String remark) throws Exception {
        this.dealProcessInstanceId(processInstance);
        String currentNode = processInstance.getCurrentNode();
        // 根据流程id+版本号查询流程图
        Process process = processDao.getProcessByVersion(processInstance.getProcessId(), processInstance.getProcessVersion());
        if (process == null) {
            throw new PromptException("找不到该流程");
        }
        // 解析流程图节点并获取当前审批节点
        ProcessModel processModel = ModelParser.parse(process.getJson());
        process.setProcessModel(processModel);
        TaskModel taskModel = processModel.getTaskNode(processInstance.getCurrentNode());
        ProcessInstance processInstanceById = processInstanceDao.getProcessInstanceById(processInstance.getProcessInstanceId());
        if (processInstanceById == null) {
            throw new PromptException("流程实例不存在");
        }
        processInstance.setParentProcessId(processInstanceById.getParentProcessId());
        // 查询操作流审批任务
        Task task = taskDao.getApproveTask(processInstance.getProcessInstanceId(), currentNode);

        String nextNode = taskModel.getAllNextModels(WorkModel.class).stream().map(BaseModel::getDisplayName).collect(Collectors.joining("|"));

        logger.info("=================开始执行回退，当前流程实例：{}", processInstance.getProcessInstanceId());

        // 开始事务，保存表单数据，执行sql
        doTrans(() -> {
            // 当前审批任务(删除任务，移入历史表)
            taskService.complete(task);
            logger.info("当前任务数据{}",task);
            // 流程状态置为拒绝
            processInstanceDao.refuse(processInstance);
            // 生成上一个节点的待办任务
            Task preTask = taskService.createOperationTaskFromParent((OperationModel) taskModel.getInputs().get(0).getSource(), task.getParentTaskId());
            // 顺便将表单也复制生成一份
            this.saveFormDataByFind(preTask.getId(), task.getParentTaskId());
            // 记录操作日志
            opfLogDao.addLog(this.buildOpfLog(OperationTypeConstant.APPROVE_REFUSE, remark, processInstance, task, null, nextNode));
        });
    }

    private void dealProcessInstanceId(ProcessInstance processInstance) {
        if (processInstance.getProcessInstanceId().length() > 32) {
            processInstance.setProcessInstanceId(processInstance.getProcessInstanceId().substring(0, 32));
        }
    }
}
