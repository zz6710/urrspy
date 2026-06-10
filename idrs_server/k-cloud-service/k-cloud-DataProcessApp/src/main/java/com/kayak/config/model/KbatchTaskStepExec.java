package com.kayak.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "kbatchTaskStepExecService", table = "kbatch_task_step_exec")
public class KbatchTaskStepExec {

    @GraphQLField(label = "执行ID",field = "taskExecid")
    private String taskExecid;
    @GraphQLField(label = "清算业务日期",field = "taskDate")
    private String taskDate;
    @GraphQLField(label = "任务组",field = "taskGroup")
    private String taskGroup;
    @GraphQLField(label = "任务ID",field = "taskId")
    private String taskId;
    @GraphQLField(label = "执行步骤",field = "stepNo")
    private String stepNo;
    @GraphQLField(label = "执行步骤名称",field = "stepName")
    private String stepName;

    @GraphQLField(label = "系统模块ID",field = "moduleid")
    private String moduleid;
    @GraphQLField(label = "目标代码",field = "targetCode")
    private String targetCode;
    @GraphQLField(label = "产品代码",field = "prodCode")
    private String prodCode;
    @GraphQLField(label = "执行状态",field = "execStatus")
    private String execStatus;
    @GraphQLField(label = "系统工作日",field = "shouldExecDate")
    private String shouldExecDate;
    @GraphQLField(label = "执行日期",field = "execDate")
    private String execDate;
    @GraphQLField(label = "执行开始时间",field = "startTime")
    private String startTime;
    @GraphQLField(label = "执行结束时间",field = "endTime")
    private String endTime;
    @GraphQLField(label = "是否忽略不应该存在的对账不平",field = "isNeglect")
    private String isNeglect;
    @GraphQLField(label = "是否跳过执行该步骤",field = "isSkip")
    private String isSkip;
    @GraphQLField(label = "是否重新执行该步骤",field = "isReplay")
    private String isReplay;
    @GraphQLField(label = "是否断点",field = "isStop")
    private String isStop;
    @GraphQLField(label = "是否分片任务",field = "isSlice")
    private String isSlice;
    @GraphQLField(label = "任务执行ID",field = "threadUuid")
    private String threadUuid;
    @GraphQLField(label = "服务器节点号",field = "serverNode")
    private String serverNode;
    @GraphQLField(label = "服务器主机名",field = "serverName")
    private String serverName;
    @GraphQLField(label = "服务器IP",field = "serverIp")
    private String serverIp;
    @GraphQLField(label = "返回码",field = "rtnCode")
    private String rtnCode;
    @GraphQLField(label = "返回信息",field = "rtnDesc")
    private String rtnDesc;
    @GraphQLField(label = "销售商文件批次号",field = "distributorBatch")
    private String distributorBatch;
    @GraphQLField(label = "创建日期",field = "crtTime")
    private String crtTime;
    @GraphQLField(label = "更新日期",field = "updTime")
    private String updTime;

    public String getTaskExecid() {
        return taskExecid;
    }

    public void setTaskExecid(String taskExecid) {
        this.taskExecid = taskExecid;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getStepNo() {
        return stepNo;
    }

    public void setStepNo(String stepNo) {
        this.stepNo = stepNo;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getModuleid() {
        return moduleid;
    }

    public void setModuleid(String moduleid) {
        this.moduleid = moduleid;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getExecStatus() {
        return execStatus;
    }

    public void setExecStatus(String execStatus) {
        this.execStatus = execStatus;
    }

    public String getShouldExecDate() {
        return shouldExecDate;
    }

    public void setShouldExecDate(String shouldExecDate) {
        this.shouldExecDate = shouldExecDate;
    }

    public String getExecDate() {
        return execDate;
    }

    public void setExecDate(String execDate) {
        this.execDate = execDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIsNeglect() {
        return isNeglect;
    }

    public void setIsNeglect(String isNeglect) {
        this.isNeglect = isNeglect;
    }

    public String getIsSkip() {
        return isSkip;
    }

    public void setIsSkip(String isSkip) {
        this.isSkip = isSkip;
    }

    public String getIsReplay() {
        return isReplay;
    }

    public void setIsReplay(String isReplay) {
        this.isReplay = isReplay;
    }

    public String getIsStop() {
        return isStop;
    }

    public void setIsStop(String isStop) {
        this.isStop = isStop;
    }

    public String getIsSlice() {
        return isSlice;
    }

    public void setIsSlice(String isSlice) {
        this.isSlice = isSlice;
    }

    public String getThreadUuid() {
        return threadUuid;
    }

    public void setThreadUuid(String threadUuid) {
        this.threadUuid = threadUuid;
    }

    public String getServerNode() {
        return serverNode;
    }

    public void setServerNode(String serverNode) {
        this.serverNode = serverNode;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnDesc() {
        return rtnDesc;
    }

    public void setRtnDesc(String rtnDesc) {
        this.rtnDesc = rtnDesc;
    }

    public String getDistributorBatch() {
        return distributorBatch;
    }

    public void setDistributorBatch(String distributorBatch) {
        this.distributorBatch = distributorBatch;
    }

    public String getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(String crtTime) {
        this.crtTime = crtTime;
    }

    public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
        this.updTime = updTime;
    }
}
