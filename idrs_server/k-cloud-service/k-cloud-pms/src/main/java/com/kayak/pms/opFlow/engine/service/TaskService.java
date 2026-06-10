package com.kayak.pms.opFlow.engine.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.spring.SpringContextHolder;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.helper.StringHelper;
import com.kayak.utils.DateHelper;
import com.kayak.pms.opFlow.engine.constant.ActorTypeConstant;
import com.kayak.pms.opFlow.engine.constant.ParamConstant;
import com.kayak.pms.opFlow.engine.constant.ProcessInstanceConstant;
import com.kayak.pms.opFlow.engine.constant.TaskPerformTypeEnum;
import com.kayak.pms.opFlow.engine.dao.*;
import com.kayak.pms.opFlow.engine.entity.Process;
import com.kayak.pms.opFlow.engine.entity.*;
import com.kayak.pms.opFlow.engine.entity.vo.BootstrapTableVO;
import com.kayak.pms.opFlow.engine.entity.vo.ReApplyProcess;
import com.kayak.pms.opFlow.engine.exception.WorkflowException;
import com.kayak.pms.opFlow.engine.handlers.Completion;
import com.kayak.pms.opFlow.engine.handlers.RoleAssignmentHandler;
import com.kayak.pms.opFlow.engine.handlers.UserAssignmentHandler;
import com.kayak.pms.opFlow.engine.handlers.impl.GeneralCompletion;
import com.kayak.pms.opFlow.engine.helper.AssertHelper;
import com.kayak.pms.opFlow.engine.model.*;
import com.kayak.pms.opFlow.engine.utils.RegexUtil;
import com.kayak.pms.opFlow.engine.utils.RemoteInvokeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by daniel on 20/03/2017.
 */
@Service
@APIDefine(desc = "任务服务", model = Task.class)
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private static final String START = "start";
    @Autowired
    TaskDao taskDao;

    @Autowired
    HisTaskDao hisTaskDao;

    @Autowired
    TaskActorDao taskActorDao;

    @Autowired
    WorkflowEngine workflowEngine;

    @Autowired
    ProcessDao processDao;

    @Autowired
    ProcessInstanceDao processInstanceDao;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    CommonDao commonDao;

    @API(desc = "任务列表", auth = APIAuth.NO)
    public SqlResult<Task> listActiveTasks(SqlParam<Task> param) throws Exception {
        List<String> taskIdList = authorityService.listTaskIdsByCurrentUser();
        String taskIds = String.join("','", taskIdList);
        Map<String, Object> paramsDirect = param.getParamsDirect();
        paramsDirect.put("taskIds", taskIds);
        return taskDao.listActiveTasks(param, paramsDirect);
    }

    /**
     * 创建审批任务
     * @param taskModel
     * @param execution
     * @return
     * @throws Exception
     */
    public List<Task> createApproveTask(TaskModel taskModel, Execution execution) throws Exception {
        List<Task> tasks = new ArrayList<>();
        Task task = createBaseTask(taskModel, execution);
        task.setTaskType("1");

        // 待创建任务的参与者
        List<TaskActor> taskActors = getTaskActors(taskModel, execution, task.getId());
        // 任务执行方式为参与者中任何一个执行即可驱动流程继续流转，该方法只产生一个task
        saveTask(task, taskActors);
        execution.setTask(task);
        tasks.add(task);
        return tasks;
    }


    /**
     * 创建功能任务
     * @param operationModel
     * @param execution
     * @return
     * @throws Exception
     */
    public List<Task> createOperationTask(OperationModel operationModel, Execution execution) throws Exception {
        List<Task> tasks = new ArrayList<>();
        Task task = createBaseTask(operationModel, execution);
        task.setBusiId(operationModel.getBusiId());

        // 待创建任务的参与者
        List<TaskActor> taskActors = getTaskActors(operationModel, execution, task.getId());
        ProcessInstance processInstance = execution.getProcessInstance();
        SqlRow sqlRow = taskDao.findRow("SELECT id FROM t8_prod_info WHERE prod_code =" +
                "( SELECT prod_code FROM t8_prod_flow WHERE op_process_id='"+processInstance.getProcessInstanceId()+"' )",processInstance.getProcessInstanceId());
        if (sqlRow != null) {
            String id = sqlRow.getString("id");
            String userIds = "";
            String taskIdTemp = "";
            for (TaskActor taskActor : taskActors) {
                if ("1".equals(taskActor.getActorType())) {//配置的角色
                    String roleId = taskActor.getActorId();
                    SqlRow row = taskActorDao.findRow("SELECT GROUP_CONCAT(userid_a) userids FROM t8_prod_user WHERE role_id = '"+roleId+"' AND t8_prod_info_id ='" + id + "'", id);
                    if (row!=null) {
                        userIds = row.getString("userids");
                        taskIdTemp = taskActor.getTaskId();
//                        taskActor.setActorType("2");
//                        taskActor.setActorId(useridA);
                    }
                }
            }
            if (StringUtils.isNotEmpty(userIds)) {
                taskActors.clear();
                String[] idList = userIds.split(",");
                for (String userid : idList) {
                    TaskActor taskActor = new TaskActor();
                    taskActor.setId(StringHelper.getPrimaryKey());
                    taskActor.setActorType("2");
                    taskActor.setActorId(userid);
                    taskActor.setTaskId(taskIdTemp);
                    taskActors.add(taskActor);
                }
            }
        }
        // 任务执行方式为参与者中任何一个执行即可驱动流程继续流转，该方法只产生一个task
        saveTask(task, taskActors);
        execution.setTask(task);
        tasks.add(task);
        return tasks;
    }

    //针对清盘流程关联产品
    public List<Task> createOperationTaskNew(OperationModel operationModel, Execution execution,String id) throws Exception {
        List<Task> tasks = new ArrayList<>();
        Task task = createBaseTask(operationModel, execution);
        task.setBusiId(operationModel.getBusiId());

        // 待创建任务的参与者
        List<TaskActor> taskActors = getTaskActors(operationModel, execution, task.getId());
        String userIds = "";
        String taskIdTemp = "";
        for (TaskActor taskActor : taskActors) {
            if ("1".equals(taskActor.getActorType())) {//配置的角色
                String roleId = taskActor.getActorId();
                SqlRow row = taskActorDao.findRow("SELECT GROUP_CONCAT(userid_a) userids FROM t8_prod_user WHERE role_id = '"+roleId+"' AND t8_prod_info_id ='" + id + "'", id);
                if (row!=null) {
                    userIds = row.getString("userids");
                    taskIdTemp = taskActor.getTaskId();
//                    taskActor.setActorType("2");
//                    taskActor.setActorId(useridA);
                }
            }
        }
        if (StringUtils.isNotEmpty(userIds)) {
            taskActors.clear();
            String[] idList = userIds.split(",");
            for (String userid : idList) {
                TaskActor taskActor = new TaskActor();
                taskActor.setId(StringHelper.getPrimaryKey());
                taskActor.setActorType("2");
                taskActor.setActorId(userid);
                taskActor.setTaskId(taskIdTemp);
                taskActors.add(taskActor);
            }
        }
        // 任务执行方式为参与者中任何一个执行即可驱动流程继续流转，该方法只产生一个task
        saveTask(task, taskActors);
        execution.setTask(task);
        tasks.add(task);
        return tasks;
    }

    public Task createOperationTaskFromParent(OperationModel operationModel, String parentTaskId) throws Exception {
        // 查出上一个任务，然后生成新id插入
        Task preTask = hisTaskDao.getHisTaskById(parentTaskId);
        String submitUser = preTask.getSubmitUser();
        logger.info("获取上一个任务操作的人员{}",submitUser);
        preTask.setId(StringHelper.getPrimaryKey());
        taskDao.createTask(preTask);
        List<TaskActor> taskActors = this.getTaskActors(operationModel, preTask.getId());
        //若上一任务操作人员不为空，则给上一个人员推待办
        if (StringUtils.isNotBlank(submitUser)) {
            TaskActor taskActor = new TaskActor();
            logger.info("任务id{}",preTask.getTaskId());
            taskActor.setId(StringHelper.getPrimaryKey());
            taskActor.setTaskId(preTask.getId());
            taskActor.setActorType("2");
            taskActor.setActorId(submitUser);
//            taskActors.add(taskActor);
            taskActorDao.saveTaskActor(taskActor);
        }else {
            for (TaskActor taskActor : taskActors) {
                taskActorDao.saveTaskActor(taskActor);
            }
        }
        return preTask;
    }

    public Task createOperationTaskFromParentInstance(OperationModel operationModel, String processInstanceId) throws Exception {
        // 查出上一个任务，然后生成新id插入
        Task preTask = hisTaskDao.getLastHisTaskByInstance(processInstanceId);
        preTask.setId(StringHelper.getPrimaryKey());
        taskDao.createTask(preTask);
        List<TaskActor> taskActors = this.getTaskActors(operationModel, preTask.getTaskId());
        for (TaskActor taskActor : taskActors) {
            taskActorDao.saveTaskActor(taskActor);
        }
        return preTask;
    }

    /**
     * 根据模型、执行对象、任务类型构建基本的task对象
     * @param model     模型
     * @param execution 执行对象
     * @return Task任务对象
     */
    private Task createBaseTask(WorkModel model, Execution execution) {
        Task task = new Task();
        task.setId(StringHelper.getPrimaryKey());
        if (StringHelper.isEmpty(execution.getProcessInstance().getProcessInstanceId())) {
            task.setProcessInstanceId(StringHelper.getPrimaryKey());
        } else {
            task.setProcessInstanceId(execution.getProcessInstance().getProcessInstanceId());
        }
        task.setName(model.getName());
        task.setDisplayName(model.getDisplayName());
        task.setCreateDate(DateHelper.getCurrentDate());
        task.setCreateTime(DateHelper.getCurrentTime());
        task.setParentTaskId(execution.getTask() == null ? START : execution.getTask().getId());
        if (START.equalsIgnoreCase(task.getParentTaskId())) {
            task.setSubmitUser(SysUtil.getLoginUserid());
        } else {
            task.setSubmitUser(execution.getSubmitUser());
        }
        task.setOperator(SysUtil.getLoginUserid());
        task.setProcessId(execution.getProcessInstance().getProcessId());
        return task;
    }

    private List<TaskActor> getTaskActors(WorkModel taskModel, Execution execution, String taskId) {
        List<TaskActor> taskActors = new ArrayList<>();
        //处理器指定人员
        try {
            String userIdsByAssignmentHandler = getUserIdsByAssignmentHandler(taskModel, execution);
            taskActors.addAll(getTaskActorsByType(userIdsByAssignmentHandler, taskId, ActorTypeConstant.USER_IDS_BY_ASSIGNMENT_HANDLER));


            //选择指定人
            taskActors.addAll(getTaskActorsByType(taskModel.getActorIds(), taskId, ActorTypeConstant.ORDINARY));

            // 通过指定上下文, 如果是定时任务, 则不需要计算上下文
            if (StringHelper.isEmpty(taskModel.getQuartzExpr())) {
                taskActors.addAll(getTaskActorsByEnvTask(taskModel, taskId, execution));
            }

            // 如果不启用角色  或者 指定用户  则角色不参与计算
            if (!("0".equalsIgnoreCase(taskModel.getUseRole()) || "1".equalsIgnoreCase(taskModel.getSpecifiedUser()))) {
                //处理器指定角色
                String roleIdsByAssignmentHandler = getRoleIdsByAssignmentHandler(taskModel, execution);
                taskActors.addAll(getTaskActorsByType(roleIdsByAssignmentHandler, taskId, ActorTypeConstant.ROLE_IDS_BY_ASSIGNMENT_HANDLER));

                //选择指定角色
                taskActors.addAll(getTaskActorsByType(taskModel.getRoleIds(), taskId, ActorTypeConstant.ROLE));
            }

            // 如果指定用户
            if ("1".equalsIgnoreCase(taskModel.getSpecifiedUser()) && !isApplyTask(taskModel)) {
                if (StringHelper.isEmpty(execution.getSpecifiedActors())) {
                    throw new WorkflowException("必须指定任务审核人");
                }
                taskActors.addAll(getTaskActorsByType(execution.getSpecifiedActors(), taskId, ActorTypeConstant.ORDINARY));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        /**
         * 如果没有按钮，或者按钮为0，则说明是申请节点
         * 申请节点不需要校验
         */
      /*  if (taskActors.size() == 0 && !(taskModel.getBtns() == null || "0".equals(taskModel.getBtns()))) {
            throw new WorkflowException("当前任务【" + taskModel.getDisplayName() + "】没有指定参与者");
        }*/
        return taskActors;
    }
    public List<TaskActor> getTaskActors(WorkModel taskModel, String taskId) {
        List<TaskActor> taskActors = new ArrayList<>();
        // 选择指定人
        taskActors.addAll(getTaskActorsByType(taskModel.getActorIds(), taskId, ActorTypeConstant.ORDINARY));
        // 选择指定角色
        taskActors.addAll(getTaskActorsByType(taskModel.getRoleIds(), taskId, ActorTypeConstant.ROLE));
        return taskActors;
    }

    private boolean isApplyTask(WorkModel taskModel) {
        return taskModel.getBtns() == null || "0".equals(taskModel.getBtns());
    }

    private List<TaskActor> getTaskActorsByType(String taskActorsString, String taskId, String type) {

        List<TaskActor> taskActors = new ArrayList<TaskActor>();
        if (StringHelper.isNotEmpty(taskActorsString)) {
            String[] actors = taskActorsString.split(",");
            for (String actor : actors) {
                TaskActor taskActor = new TaskActor();
                taskActor.setId(StringHelper.getPrimaryKey());
                taskActor.setTaskId(taskId);
                taskActor.setActorId(actor);
                taskActor.setActorType(type);
                taskActors.add(taskActor);
            }
        }
        return taskActors;
    }

    private List<TaskActor> getTaskActorsByEnvTask(WorkModel taskModel, String taskId, Execution execution) {
        List<TaskActor> taskActors = new ArrayList<TaskActor>();
        // 这里是选择框, 所有sqlKey为上下文的键
        String sqlKey = taskModel.getEnvTask();
        if (StringHelper.isNotEmpty(sqlKey)) {
            // 执行sql 返回值
            EnvItemDao envItemDao = SpringContextHolder.getBean("envItemDao");
            String sql = envItemDao.getSqlByItemKey(execution.getProcess().getId(), sqlKey);
            Map<String, Object> envParams = execution.getLatestSubmitParams();
            // 如果是报销, 则获取实际消费人作为 employee_no
            String expenseSerno = (String) envParams.get("expense_serno");
            if (StringHelper.isNotEmpty(expenseSerno)) {
                ComnDao comnDao = SysBeans.getBean("comnDao");
                try {
                    List<SqlRow> rows = comnDao.findRows("SELECT employee_no FROM expense_log WHERE expense_serno=$S{expense_serno}", envParams);
                    // 实际消费人
                    String realEmployeeNo = rows.get(0).getString("employee_no");
                    envParams.put("employee_no", realEmployeeNo);
                    envParams.put("current_date", Tools.getCurrentDate());

                } catch (Exception e) {
                    logger.error("报销单号 {} 实际消费人不存", expenseSerno);
                }
            } else {
                envParams.putAll(SysUtil.getUserInfo());
                envParams.put("current_date", Tools.getCurrentDate());
            }
            envParams.put("process_instance_id", execution.getProcessInstance().getProcessInstanceId());
            sql = RegexUtil.getSql(sql, envParams);
            logger.info("任务英文名【{}】中文名【{}】的上下文为【{}】, 执行的 sql 为 【{}】", taskModel.getName(), taskModel.getDisplayName(), sqlKey, sql);
            List<String> actors = commonDao.listBySql(sql);

            for (String actor : actors) {
                if (StringHelper.isEmpty(actor)) {
                    continue;
                }
                TaskActor taskActor = new TaskActor();
                taskActor.setId(StringHelper.getPrimaryKey());
                taskActor.setTaskId(taskId);
                taskActor.setActorId(actor);
                taskActor.setActorType(ActorTypeConstant.ORDINARY);
                taskActors.add(taskActor);
            }
        }
        return taskActors;
    }


    /**
     * 完成指定任务
     * 该方法仅仅结束活动任务，并不能驱动流程继续执行
     *
     * @param taskId 任务id
     * @param params 参数集合
     * @return
     */
    public Task complete(String taskId, Map<String, Object> params) throws Exception {
        Task task = getTask(taskId);
        AssertHelper.notNull(task, "指定的任务[id=" + taskId + "]不存在");
        boolean isStart = params.get(ProcessInstanceConstant.START) != null && (Boolean) params.get(ProcessInstanceConstant.START);
        //不是开始流程，需要校验任务是否运行被执行
        //自动审批跳过
        if (!(isStart || "true".equals(params.get(ParamConstant.WF_AUTO_APPROVAL)))) {
            if (!isAllowed(task, params)) {
                throw new WorkflowException("当前参与者[" + SysUtil.getLoginUserid() + "]不允许执行任务[taskId=" + taskId + "]");
            }
        }
        task.setFinishDate(DateHelper.getCurrentDate());
        task.setFinishTime(DateHelper.getCurrentTime());

        hisTaskDao.saveHisTask(task);
        taskDao.deleteTaskById(task.getId());
        //TODO 删除任务参与者 在fork join的时候，要考虑是否删除
        taskActorDao.removeTaskActorByTaskId(taskId);
        //这里做一些简单的日志
        Completion completion = getCompletion();
        completion.complete(task);
        return task;
    }

    public Task complete(Task task) throws Exception {
        logger.info("===================完成任务，记录到历史任务");
        task.setFinishDate(DateHelper.getCurrentDate());
        task.setFinishTime(DateHelper.getCurrentTime());
        task.setSubmitUser(SysUtil.getLoginUserid());

        hisTaskDao.saveHisTask(task);
        taskDao.deleteTaskById(task.getId());
        // TODO 删除任务参与者 在fork join的时候，要考虑是否删除
        taskActorDao.removeTaskActorByTaskId(task.getId());
        //这里做一些简单的日志
        Completion completion = getCompletion();
        completion.complete(task);
        return task;
    }

    /**
     * 先从spring获取自定义的完成接口,如果没有则获取默认
     *
     * @return
     */
    private Completion getCompletion() {
        return new GeneralCompletion();
    }

    public boolean isAllowed(Task task, Map<String, Object> params) throws Exception {
        List<String> currentUserTaskIds = authorityService.listTaskIdsByCurrentUser();
        Set<String> taskIds = new HashSet<>(currentUserTaskIds);
        List<String> surrogateTaskIds = authorityService.listTaskIdsBySurrogateUser();
        taskIds.addAll(surrogateTaskIds);
        return taskIds.contains(task.getId());
    }

    public List<Task> listActiveTasksByProcessInstanceId(String processInstanceId) throws Exception {
        return taskDao.listActiveTasksByProcessInstanceId(processInstanceId);
    }

    public List<Task> listActiveTasksByParentProcessInstanceId(String parentProcessInstanceId) throws Exception {
        return taskDao.listActiveTasksByParentProcessInstanceId(parentProcessInstanceId);
    }

    public List<Task> listActiveTasksByParentId(String taskId) {
        return taskDao.listActiveTasksByParentId(taskId);
    }

    public Task rejectTask(ProcessModel model, Task currentTask, Map<String, Object> params) throws Exception {
        String parentTaskId = currentTask.getParentTaskId();
        if (StringHelper.isEmpty(parentTaskId) || parentTaskId.equals(START)) {
            throw new WorkflowException("上一步任务ID为空，无法驳回至上一步处理");
        }
        NodeModel current = model.getNode(currentTask.getName());

        //根据当前节点去查询历史任务，获取对应的上一个节点
        Task preTask = hisTaskDao.getHisTaskById(parentTaskId);


        NodeModel parent = model.getNode(preTask.getName());
        if (!NodeModel.canRejected(current, parent)) {
            throw new WorkflowException("无法驳回至上一步处理，请确认上一步骤并非fork、join、subprocess");
        }

        //根据历史任务来产生一个回退任务
        Task task = undoTask(preTask);

        taskDao.createTask(task);
        //任务关联者的保存
        Execution execution = getExecution(currentTask, params);
        List<TaskActor> taskActors = getTaskActors((TaskModel) parent, execution, task.getId());
        for (TaskActor taskActor : taskActors) {
            taskActorDao.saveTaskActor(taskActor);
        }
        return task;
    }

    private Execution getExecution(Task currentTask, Map<String, Object> params) throws Exception {
        ProcessInstance processInstance = processInstanceDao.getProcessInstanceById(currentTask.getProcessInstanceId());
        Process process = processDao.getProcessByMaxVersion(processInstance.getProcessId());
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        return new Execution(workflowEngine, process, processInstance, params);
    }

    /**
     * 根据历史任务来产生一个回退任务,只修改它的对应关系即可
     */

    private Task undoTask(Task preTask) {
        Task task = new Task();
        task.setDisplayName(preTask.getDisplayName());
        task.setName(preTask.getName());
        task.setCreateDate(DateHelper.getCurrentDate());
        task.setCreateTime(DateHelper.getCurrentTime());
        task.setPerformType(preTask.getPerformType());
        task.setProcessInstanceId(preTask.getProcessInstanceId());
        task.setParentTaskId(preTask.getParentTaskId());
        task.setId(StringHelper.getPrimaryKey());
        return task;
    }

    public BootstrapTableVO<HisCompleteTask> listHisCompleteTasks(Map<String, Object> queryCriteria) {
//        PageHelper.offsetPage((Integer) queryCriteria.get("offset"), (Integer) queryCriteria.get("limit"));
        List<HisCompleteTask> tasks = hisTaskDao.listHisCompleteTasks(queryCriteria);
//        PageInfo page = new PageInfo<HisCompleteTask>(tasks);
        return new BootstrapTableVO<HisCompleteTask>(tasks, tasks.size());
    }

    public void deleteTaskById(String taskId) throws Exception {
        taskDao.deleteTaskById(taskId);
    }

    public List<Task> getLatestTask(String processInstanceId) {
        return taskDao.getLatestTask(processInstanceId);
    }

    public void deleteTaskByProcessInstanceId(String processInstanceId) {
        taskDao.deleteTaskByProcessInstanceId(processInstanceId);
    }

    public Task getTask(String taskId) throws Exception {
        return taskDao.getTaskById(taskId);
    }

    private Task saveTask(Task task, List<TaskActor> taskActors) throws Exception {
        task.setPerformType(TaskPerformTypeEnum.ANY.ordinal());
        taskDao.createTask(task);
        if (taskActors.size() > 0) {//开始节点，并没有参与者
            for (TaskActor taskActor : taskActors) {
                String actorId = taskActor.getActorId();
                taskActorDao.saveTaskActor(taskActor);
                logger.info("创建任务:{},任务的参与者:{}", task, taskActor);
            }
        } else {
            logger.info("创建申请任务:{}", task);
        }
        return task;
    }

    /**
     * 这个是获取用户自定义的特殊参与者。用于决定哪个人可以查看该任务(当前任务的特殊参与人)
     *
     * @param model
     * @param execution
     * @return
     */
    private String getUserIdsByAssignmentHandler(WorkModel model, Execution execution) {
        UserAssignmentHandler handler = model.getUserAssignmentHandlerObject();
        if (handler != null) {
            List<String> specialActors = handler.assign(model, execution);
            return StringUtils.join(specialActors, ",");
        }
//        if ("true".equalsIgnoreCase(Global.getGlobalConf("IS_SERVICE")) &&
//                StringHelper.isNotEmpty(model.getUserAssignmentHandler())) {
//            execution.setTaskModel(model);
//            Object userIds = RemoteInvokeUtil.restInvoke(model.getUserAssignmentHandler(), execution);
//            if (!(userIds instanceof List)) {
//                throw new WorkflowException("远程调用[" + model.getUserAssignmentHandler() + "]返回值应该为集合");
//            }
//            return StringUtils.join((List) userIds, ",");
//        }
        return null;
    }

    /**
     * 获取处理器指定角色
     *
     * @param model
     * @param execution
     * @return
     */
    private String getRoleIdsByAssignmentHandler(WorkModel model, Execution execution) {
        RoleAssignmentHandler handler = model.getRoleAssignmentHandlerObject();
        if (handler != null) {
            List<String> specialActors = handler.assign(model, execution);
            return StringUtils.join(specialActors, ",");
        }
        if (StringHelper.isNotEmpty(model.getRoleAssignmentHandler())) {
            execution.setWorkModel(model);
            Object roleIds = RemoteInvokeUtil.restInvoke(model.getRoleAssignmentHandler(), execution);
            if (!(roleIds instanceof List)) {
                throw new WorkflowException("远程调用[" + model.getRoleAssignmentHandler() + "]返回值应该为集合");
            }
            return StringUtils.join((List) roleIds, ",");
        }
        return null;
    }

    public List<ReApplyProcess> listReApply(SqlParam<ReApplyProcess> param) {
        return taskDao.listReApply(param);
    }

    public List<String> getApprovalUserNameByTaskId(String taskId) {
        return taskDao.getApprovalUserNameByTaskId(taskId);
    }

    public List<String> getApprovalRoleNameByTaskId(String taskId) {
        return taskDao.getApprovalRoleNameByTaskId(taskId);
    }

}
