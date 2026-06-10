package com.kayak.clear.dto;

import com.kayakwise.kcloud.batch.model.entity.KbatchTaskInfo;

/**
 *
 */
public class KbatchClearTaskInfoDto extends KbatchTaskInfo {

    private String taskGroup;
    private String alarmTime;
    private String taskId;
    private String taskName;
    private String moduleid;
    private String taskType;
    private String canAgain;
    private String preTaskId;
    private String runningType;
    private String taskParams;
    private String simpleFlow;
    private String execOrder;

    private String shouldExecTime;
    private String serviceClass;
    private String reqClass;
    private String inClass;
    private String taskModel;

    @Override
    public String toString() {
        return "KbatchClearTaskInfoDto{" +
                "taskGroup='" + taskGroup + '\'' +
                ", alarmTime='" + alarmTime + '\'' +
                ", taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", moduleid='" + moduleid + '\'' +
                ", taskType='" + taskType + '\'' +
                ", canAgain='" + canAgain + '\'' +
                ", preTaskId='" + preTaskId + '\'' +
                ", runningType='" + runningType + '\'' +
                ", taskParams='" + taskParams + '\'' +
                ", simpleFlow='" + simpleFlow + '\'' +
                ", execOrder='" + execOrder + '\'' +
                ", shouldExecTime='" + shouldExecTime + '\'' +
                ", serviceClass='" + serviceClass + '\'' +
                ", reqClass='" + reqClass + '\'' +
                ", inClass='" + inClass + '\'' +
                ", taskModel='" + taskModel + '\'' +
                '}';
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getModuleid() {
        return moduleid;
    }

    public void setModuleid(String moduleid) {
        this.moduleid = moduleid;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getCanAgain() {
        return canAgain;
    }

    public void setCanAgain(String canAgain) {
        this.canAgain = canAgain;
    }

    public String getPreTaskId() {
        return preTaskId;
    }

    public void setPreTaskId(String preTaskId) {
        this.preTaskId = preTaskId;
    }

    public String getRunningType() {
        return runningType;
    }

    public void setRunningType(String runningType) {
        this.runningType = runningType;
    }

    public String getTaskParams() {
        return taskParams;
    }

    public void setTaskParams(String taskParams) {
        this.taskParams = taskParams;
    }

    public String getSimpleFlow() {
        return simpleFlow;
    }

    public void setSimpleFlow(String simpleFlow) {
        this.simpleFlow = simpleFlow;
    }

    public String getExecOrder() {
        return execOrder;
    }

    public void setExecOrder(String execOrder) {
        this.execOrder = execOrder;
    }

    public String getShouldExecTime() {
        return shouldExecTime;
    }

    public void setShouldExecTime(String shouldExecTime) {
        this.shouldExecTime = shouldExecTime;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getReqClass() {
        return reqClass;
    }

    public void setReqClass(String reqClass) {
        this.reqClass = reqClass;
    }

    public String getInClass() {
        return inClass;
    }

    public void setInClass(String inClass) {
        this.inClass = inClass;
    }

    public String getTaskModel() { return taskModel; }

    public void setTaskModel(String taskModel) { this.taskModel = taskModel; }
}

