package com.kayak.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

/**
 * 文件名: Ta5004.java
 * 描述:  清算日志
 * 创建人: zengzt
 * 创建时间:2020年4月27日上午10:22:15
 */
@GraphQLModel(fetcher = "kbatchLogService",table = "kbatch_log")
public class KbatchLog {

	@GraphQLField(kkhtml = "KFieldDate",label = "清算业务日期", sql = "TASK_DATE >= $S{taskDateStart}" ,field = "task_date_start",
			kkhtmlExt = "{'data-type':'daterange', endDateFeild:'taskDateEnd'}")
	private String taskDateStart;
	@GraphQLField(label = "清算业务结束日期", sql = "TASK_DATE <= $S{taskDateEnd}" ,field = "task_date_end", kkhtmlExt="{\"data-min-value\":\"{taskDateStart}\"}")
	private String taskDateEnd;
	
	@GraphQLField(kkhtml = "KFieldText",label = "日志号", sql = "LOG_SERNO like '%$U{logSerno}%'" ,field = "logSerno")
	private String logSerno;
	@GraphQLField(kkhtml = "KFieldText",label = "任务执行ID", sql = "TASK_EXECID like '%$U{taskExecid}%'" ,field = "taskExecid")
	private String taskExecid;
	@GraphQLField(kkhtml = "KFieldText",label = "分组ID", sql = "TASK_GROUP like '%$U{taskGroup}%'" ,field = "taskGroup")
	private String taskGroup;
	@GraphQLField(kkhtml = "KFieldText",label = "任务ID", sql = "TASK_ID like '%$U{taskId}%'" ,field = "taskId")
	private String taskId;

	@GraphQLField(kkhtml = "KFieldSelect",label = "执行状态", field = "execStatus", sql = "EXEC_STATUS = $S{execStatus}",
										kkhtmlExt="{\"data-dict\":\"batch_task_status\",\"data-displaykeyvalue\":\"true\"}")
	private String execStatus;
	@GraphQLField(kkhtml = "KFieldSelect",label = "产品名称", sql = "PROD_CODE = $S{prodCode}" ,field = "prodCode", kkhtmlExt="{\"data-action\":\"TaDict.findTaProdInfos\",\"data-display-field\":\"prodCode,prodName\",\"data-value-field\":\"prodCode\"}")
	private String prodCode;

	@GraphQLField(label = "模块ID",field = "moduleid")
	private String moduleid;

	@GraphQLField(label = "步骤号", field = "stepNo")
	private String stepNo;
	@GraphQLField(label = "任务日期",field = "taskDate" ,sql = "task_Date = $S{taskDate}")
	private String taskDate;
	@GraphQLField(label = "执行日期",field = "execDate")
	private String execDate;
	@GraphQLField(label = "应执行日期",field = "shouldExecDate")
	private String shouldExecDate;
	@GraphQLField(label = "目标代码",field = "targetCode")
	private String targetCode;
	@GraphQLField(label = "执行开始时间",field = "startTime")
	private String startTime;
	@GraphQLField(label = "执行结束时间",field = "endTime")
	private String endTime;
	@GraphQLField(label = "线程UUID",field = "threadUuid")
	private String threadUuid;
	@GraphQLField(label = "服务名称",field = "serverName")
	private String serverName;
	@GraphQLField(label = "服务IP",field = "serverIp")
	private String serverIp;
	@GraphQLField(label = "返回码",field = "rtnCode")
	private String rtnCode;
	@GraphQLField(label = "返回信息",field = "rtnDesc")
	private String rtnDesc;
	@GraphQLField(label = "更新日期",field = "updDate")
	private String updDate;
	@GraphQLField(label = "更新时间",field = "updTime")
	private String updTime;
	@GraphQLField(kkhtml = "KFieldSelect",label = "所属模块",sql = " task_model = $S{taskModel} ", field = "taskmodel",kkhtmlExt="{\"data-dict\":\"task_model\"}")
	private String taskModel;
	@GraphQLField(label = "模块名称",field = "taskName")
	private String taskName;

	public String getTaskDateStart() {
		return taskDateStart;
	}
	public void setTaskDateStart(String taskDateStart) {
		this.taskDateStart = taskDateStart;
	}
	public String getTaskDateEnd() {
		return taskDateEnd;
	}
	public void setTaskDateEnd(String taskDateEnd) {
		this.taskDateEnd = taskDateEnd;
	}
	public String getLogSerno() {
		return logSerno;
	}
	public void setLogSerno(String logSerno) {
		this.logSerno = logSerno;
	}
	public String getTaskExecid() {
		return taskExecid;
	}
	public void setTaskExecid(String taskExecid) {
		this.taskExecid = taskExecid;
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
	public String getExecStatus() {
		return execStatus;
	}
	public void setExecStatus(String execStatus) {
		this.execStatus = execStatus;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getModuleid() {
		return moduleid;
	}
	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}
	public String getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}
	public String getExecDate() {
		return execDate;
	}
	public void setExecDate(String execDate) {
		this.execDate = execDate;
	}
	public String getShouldExecDate() {
		return shouldExecDate;
	}
	public void setShouldExecDate(String shouldExecDate) {
		this.shouldExecDate = shouldExecDate;
	}
	public String getTargetCode() {
		return targetCode;
	}
	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
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
	public String getThreadUuid() {
		return threadUuid;
	}
	public void setThreadUuid(String threadUuid) {
		this.threadUuid = threadUuid;
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
	public String getUpdDate() {
		return updDate;
	}
	public void setUpdDate(String updDate) {
		this.updDate = updDate;
	}
	public String getUpdTime() {
		return updTime;
	}
	public void setUpdTime(String updTime) {
		this.updTime = updTime;
	}

	public String getTaskModel() {
		return taskModel;
	}

	public void setTaskModel(String taskModel) {
		this.taskModel = taskModel;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
}
