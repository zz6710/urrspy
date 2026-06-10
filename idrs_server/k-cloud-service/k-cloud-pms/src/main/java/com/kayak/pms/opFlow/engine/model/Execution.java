package com.kayak.pms.opFlow.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kayak.pms.opFlow.engine.constant.ParamConstant;
import com.kayak.pms.opFlow.engine.constant.ProcessInstanceConstant;
import com.kayak.pms.opFlow.engine.entity.Process;
import com.kayak.pms.opFlow.engine.entity.ProcessInstance;
import com.kayak.pms.opFlow.engine.entity.Task;
import com.kayak.pms.opFlow.engine.exception.WorkflowException;
import com.kayak.pms.opFlow.engine.service.WorkflowEngine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 流程执行过程中所传递的执行对象，其中包含流程定义、流程模型、流程实例对象、执行参数、返回的任务列表
 * Created by daniel on 19/03/2017.
 */
public class Execution implements Serializable {

    private static final long serialVersionUID = -343313822381707001L;
    @JsonIgnore
    private WorkflowEngine engine;
    /**
     * 流程定义对象
     */
    private Process process;
    /**
     * 流程实例对象
     */
    private ProcessInstance processInstance;
    /**
     * 父流程实例 未实现
     */
    private ProcessInstance parentProcessInstance;
    /**
     * 父流程实例节点名称
     */
    private String parentNodeName;
    /**
     * 子流程实例节点名称 未实现
     */
    private String childProcessInstanceId;
    /**
     * 执行参数,可用于计算流程的走向
     */
    private Map<String, Object> params;

    //原始表单数据 + 修改后的数据(流程中动态修改)
    private Map<String, Object> latestSubmitParams;
    /**
     * 当前执行任务
     */
    private Task task;
    /**
     * 返回的任务列表(已产生的任务),这是为了有事务的时候，不能直接多数据库进行查询
     */
    private List<Task> tasks = new ArrayList<Task>();

    //用于标识是否是开始流程
    private Boolean start = false;

    //这个通常为空，用于标识刚任务需要重新申请的用户id
    private String submitUser;

    //页面指定参与者
    private String specifiedActors;

    //当前用户角色. 可以用于发邮件
    private String currentRoles;

    //用于远程调用传递的参数
    //TODO 后面应该加回来
    @JsonIgnore
    private TaskModel taskModel;
    @JsonIgnore
    private WorkModel workModel;
    /**
     * 流程回调表示, 说明流程是处于什么状态(在拒绝和驳回到申请节点有用)
     *
     * @see ProcessInstanceConstant
     */
    private String businessCallbackFlag;

    private Boolean hasExec = false ;

    private TransitionModel tm;

    private Integer execNums = 0;

    public Execution() {
    }

    public Execution(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    /**
     * @param engine
     * @param process
     * @param processInstance
     * @param params
     */
    public Execution(WorkflowEngine engine, Process process, ProcessInstance processInstance, Map<String, Object> params) {
        if (process == null || processInstance == null) {
            throw new WorkflowException("构造Execution对象失败，process、processInstance不能为空");
        }
        this.engine = engine;
        this.process = process;
        this.params = params;
        this.processInstance = processInstance;
        if (params.get(ParamConstant.SPECIFIED_ACTORS) != null && !"".equals(params.get(ParamConstant.SPECIFIED_ACTORS))) {
            this.specifiedActors = (String) params.get(ParamConstant.SPECIFIED_ACTORS);
        }

        if (params.get(ProcessInstanceConstant.START) != null) {
            this.start = (Boolean) params.get(ProcessInstanceConstant.START);
        }
    }

    /**
     * 是否已合并
     * 针对join节点的处理
     */
    private boolean merged = false;

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public WorkflowEngine getEngine() {
        return engine;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }


    public ProcessInstance getParentProcessInstance() {
        return parentProcessInstance;
    }


    public String getParentNodeName() {
        return parentNodeName;
    }


    public String getChildProcessIntanceId() {
        return childProcessInstanceId;
    }


    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getChildProcessInstanceId() {
        return childProcessInstanceId;
    }

    public void setChildProcessInstanceId(String childProcessInstanceId) {
        this.childProcessInstanceId = childProcessInstanceId;
    }

    /**
     * 添加任务集合
     *
     * @param tasks
     */
    public void addTasks(List<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void setEngine(WorkflowEngine engine) {
        this.engine = engine;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public void setParentProcessInstance(ProcessInstance parentProcessInstance) {
        this.parentProcessInstance = parentProcessInstance;
    }

    public void setParentNodeName(String parentNodeName) {
        this.parentNodeName = parentNodeName;
    }

    public Boolean getStart() {
        return start;
    }

    public void setStart(Boolean start) {
        this.start = start;
    }

    public Map<String, Object> getLatestSubmitParams() {
        return latestSubmitParams;
    }

    public void setLatestSubmitParams(Map<String, Object> latestSubmitParams) {
        this.latestSubmitParams = latestSubmitParams;
    }

    public String getSubmitUser() {
        return submitUser;
    }

    public void setSubmitUser(String submitUser) {
        this.submitUser = submitUser;
    }

    public String getBusinessCallbackFlag() {
        return businessCallbackFlag;
    }

    public void setBusinessCallbackFlag(String businessCallbackFlag) {
        this.businessCallbackFlag = businessCallbackFlag;
    }

    public String getSpecifiedActors() {
        return specifiedActors;
    }

    public void setSpecifiedActors(String specifiedActors) {
        this.specifiedActors = specifiedActors;
    }

    public String getCurrentRoles() {
        return currentRoles;
    }

    public void setCurrentRoles(String currentRoles) {
        this.currentRoles = currentRoles;
    }

    public TaskModel getTaskModel() {
        return taskModel;
    }

    public void setTaskModel(TaskModel taskModel) {
        this.taskModel = taskModel;
    }

    public WorkModel getWorkModel() {
        return workModel;
    }

    public void setWorkModel(WorkModel workModel) {
        this.workModel = workModel;
    }

    public TransitionModel getTm() {
        return tm;
    }

    public void setTm(TransitionModel tm) {
        this.tm = tm;
    }

    public Boolean getHasExec() {
        return hasExec;
    }

    public void setHasExec(Boolean hasExec) {
        this.hasExec = hasExec;
    }

    public Integer getExecNums() {
        return execNums;
    }

    public void setExecNums(Integer execNums) {
        this.execNums = execNums;
    }
}
