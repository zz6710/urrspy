package com.kayak.pms.opFlow.engine.service;

import com.kayak.core.system.RequestSupport;
import com.kayak.helper.StringHelper;
import com.kayak.utils.DateHelper;
import com.kayak.pms.opFlow.engine.Global;
import com.kayak.pms.opFlow.engine.busi.CommonWorkflowCallback;
import com.kayak.pms.opFlow.engine.constant.ProcessInstanceConstant;
import com.kayak.pms.opFlow.engine.dao.BusinessLabelInfoDao;
import com.kayak.pms.opFlow.engine.dao.HisTaskDao;
import com.kayak.pms.opFlow.engine.dao.ProcessInstanceDao;
import com.kayak.pms.opFlow.engine.entity.Process;
import com.kayak.pms.opFlow.engine.entity.*;
import com.kayak.pms.opFlow.engine.exception.WorkflowException;
import com.kayak.pms.opFlow.engine.handlers.ApplyRefuseHandler;
import com.kayak.pms.opFlow.engine.helper.AssertHelper;
import com.kayak.pms.opFlow.engine.helper.ClassHelper;
import com.kayak.pms.opFlow.engine.model.*;
import com.kayak.pms.opFlow.engine.utils.FormDataUtil;
import com.kayak.pms.opFlow.engine.utils.RemoteInvokeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 20/03/2017.
 */
@Service
@Transactional
public class WorkflowEngine {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowEngine.class);
    /**
     * 流程定义业务类
     */
    @Autowired
    ProcessService processService;
    @Autowired
    protected ProcessInstanceDao processInstanceDao;
    /**
     * 任务业务类
     */
    @Autowired
    protected TaskService taskService;

    @Autowired
    FormDataService formDataService;

    @Autowired
    SubmitParamsService submitParamsService;

    @Autowired
    HisTaskDao hisTaskDao;

    @Autowired
    BusinessLabelInfoDao businessLabelInfoDao;

    public ProcessService process() {
        AssertHelper.notNull(processService);
        return processService;
    }

    public TaskService task() {
        AssertHelper.notNull(taskService);
        return taskService;
    }

    /*-----------开始流程相关start-----------*/
    public Execution startProcess(ProcessInstance processInstance) throws Exception {
        return startProcess(processInstance, new HashMap<>());
    }

    public Execution startProcess(ProcessInstance processInstance, Map<String, Object> params) throws Exception {
        Execution execution = createInstanceAndReturnExecution(processInstance, params);
        Process process = execution.getProcess();
        if (process != null) {
            StartModel start = process.getProcessModel().getStart();
            AssertHelper.notNull(start, "流程定义[name=" + process.getName() + "]没有开始节点");
            start.execute(execution);
        }
        return execution;
    }

    /**
     * 创建流程实例，返回执行对象
     *
     * @param processInstance
     * @param params
     * @return
     */
    private Execution createInstanceAndReturnExecution(ProcessInstance processInstance, Map<String, Object> params) throws Exception {
        Process process = process().getProcessById(processInstance.getProcessId());
        processInstanceDao.createProcessInstance(processInstance);

        String labelInfo = (String) params.remove(ProcessInstanceConstant.LABEL_INFO);
        if (labelInfo != null) {
            businessLabelInfoDao.add(BusinessLabelInfo.builder()
                    .id(StringHelper.getPrimaryKey())
                    .processInstanceId(processInstance.getProcessInstanceId())
                    .data(labelInfo).build());
        }

        formDataService.saveFormData(processInstance.getSubmitParams(), processInstance.getProcessInstanceId());//字段和值一行一行保存到数据库中
        //这里保存一份数据。为再次发起申请做准备
        SubmitParams submitParams = new SubmitParams();
        submitParams.setId(StringHelper.getPrimaryKey());
        submitParams.setCreateDate(DateHelper.getCurrentDate());
        submitParams.setCreateTime(DateHelper.getCurrentTime());
        submitParams.setProcessInstanceId(processInstance.getProcessInstanceId());
        submitParams.setSubmitParams(processInstance.getSubmitParams());
        submitParamsService.save(submitParams);
        logger.info("创建流程实例对象:" + processInstance);
        return new Execution(this, process, processInstance, params);
    }

    public void startAndExecute(ProcessInstance processInstance) throws Exception {
        //开始流程
        Execution execution = startProcess(processInstance);
        //执行流程的第一个申请节点

        /**
         * 这里不能通过
         *  List<Task> tasks = task().listActiveTasksByProcessInstanceId(processInstance.getProcessId());
         List<Task> tasks = execution.getTasks();
         if (tasks != null && tasks.size() > 0) {
         Task task = tasks.get(0);
         executeTask(task.getId(), new HashMap<>());
         }*/
    }

    /*-----------开始流程相关end-----------*/

    /*-----------执行任务相关start-----------*/
    public List<Task> executeTask(String taskId) throws Exception {
        return executeTask(taskId, new HashMap<>());
    }

    public List<Task> executeTask(String taskId, Map<String, Object> params) throws Exception {

        //完成任务，并且构造执行对象
        Execution execution = execute(taskId, params);
        ProcessModel model = execution.getProcess().getProcessModel();
        if (model != null) {
            NodeModel nodeModel = model.getNode(execution.getTask().getName());
            //将执行对象交给该任务对应的节点模型执行
            nodeModel.execute(execution);
        }
        return execution.getTasks();
    }

    /**
     * 根据任务主键ID，完成任务。并且构造执行对象
     *
     * @param taskId 任务id
     * @return Execution
     */
    private Execution execute(String taskId, Map<String, Object> params) throws Exception {
        if (params == null) params = new HashMap<String, Object>();
        //完成指定任务，并不能驱动流程继续向下执行
        Task task = task().complete(taskId, params);
        if (logger.isDebugEnabled()) {
            logger.debug("任务[taskId={}, taskName={}]已完成", task.getId(), task.getDisplayName());
        }

        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = processInstanceDao.getProcessInstanceById(processInstanceId);
        AssertHelper.notNull(processInstance, "指定的流程实例[id=" + processInstanceId + "]已完成或不存在");

        Process process = process().getProcessById(processInstance.getProcessId());
        Execution execution = new Execution(this, process, processInstance, params);
        execution.setTask(task);
        //给执行对象设置最新的数据
        execution.setLatestSubmitParams(FormDataUtil.formData2Map(formDataService.listLatestFormData(processInstance.getProcessInstanceId())));
        return execution;
    }

    /*-----------执行任务相关end-----------*/

    public List<Task> executeAndJumpTask(Map<String, Object> params) throws Exception {
        String taskId = (String) params.get("taskId");
        String taskName = (String) params.get("taskName");

        Execution execution = execute(taskId, params);
        ProcessModel model = execution.getProcess().getProcessModel();
        AssertHelper.notNull(model, "当前任务未找到流程定义模型");

        if (handleRejectToApplyNode(model, taskName, execution)) {//如果已经作为重新发起申请请求处理则中断程序执行
            // 回调修改状态
            Map<String, Object> otehrs = new HashMap<>();
            otehrs.put("execution", execution);
            RequestSupport.setUserParameters(otehrs);
            CommonWorkflowCallback commonWorkflowCallback = (CommonWorkflowCallback) ClassHelper.newInstance("com.kayak.opFlow.busi.CommonWorkflowCallback");
            commonWorkflowCallback.busiRejectToApplyProcessCallback();
            return new ArrayList<Task>();
        }

        if (StringHelper.isEmpty(taskName)) {
            Task newTask = task().rejectTask(model, execution.getTask(), params);
            execution.addTask(newTask);
        } else {
            NodeModel nodeModel = model.getNode(taskName);
            AssertHelper.notNull(nodeModel, "根据节点名称[" + taskName + "]无法找到节点模型");
            //动态创建转移对象，由转移对象执行execution实例
            TransitionModel tm = new TransitionModel();
            tm.setTarget(nodeModel);
            tm.setEnabled(true);
            tm.execute(execution);
        }
        return execution.getTasks();
    }

    /**
     * 如果是驳回到申请节点，就只生成一个任务
     *
     * @param model
     * @param taskName
     * @param execution
     * @return
     */
    private boolean handleRejectToApplyNode(ProcessModel model, String taskName, Execution execution) throws Exception {
        List<TaskModel> models = model.getModels(TaskModel.class);
        boolean flag = false;
        if (StringHelper.isEmpty(taskName)) {//如果是直接驳回上一个节点
            Task currentTask = execution.getTask();
            String parentTaskId = currentTask.getParentTaskId();
            Task preTask = hisTaskDao.getHisTaskById(parentTaskId);
            if (preTask.getName().equals(models.get(0).getName())) {//如果是申请节点
                flag = true;
            }
        } else {
            if (taskName.equals(models.get(0).getName())) {//如果是驳回到申请节点
                flag = true;
            }
        }
        if (flag) {
            //产生一个申请任务即可
            StartModel start = model.getStart();
            execution.setStart(true);
            execution.setSubmitUser(execution.getProcessInstance().getCreator());
            start.execute(execution);
            //回调配置在申请节点的处理器
            String applyRefuseHandler = models.get(0).getApplyRefuseHandler();
            ProcessInstance processInstance = execution.getProcessInstance();
            if (StringHelper.isNotEmpty(models.get(0).getApplyRefuseHandler())) {
                Class<?> clazz = null;
                try {
                    if ("true".equalsIgnoreCase(Global.getGlobalConf("IS_SERVICE")) && StringHelper.isNotEmpty(applyRefuseHandler)) {
                        RemoteInvokeUtil.restInvoke(applyRefuseHandler, execution);
                    } else {
                        clazz = Class.forName(applyRefuseHandler);
                        Execution executionToPass = new Execution(processInstance); //只需要传递出流程实例即可
                        ApplyRefuseHandler instance = (ApplyRefuseHandler) clazz.newInstance();
                        instance.handle(executionToPass);
                    }
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    logger.error(e.getMessage(), e);
                    throw new WorkflowException("驳回到申请节点的回调函数处理失败");
                }
            }
        }
        return flag;
    }

    public ProcessInstance startInstanceByExecution(Execution execution) {
        return null;
    }

    public List<Task> createFreeTask(String orderId, String operator, TaskModel model) {
        return null;
    }

    public ProcessService getProcessService() {
        return processService;
    }

    public void setProcessService(ProcessService processService) {
        this.processService = processService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}
