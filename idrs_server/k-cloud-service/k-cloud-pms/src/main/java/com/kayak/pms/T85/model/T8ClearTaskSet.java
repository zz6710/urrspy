package com.kayak.pms.T85.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

/**
 * 文件名: TaClearTaskSet.java
 * 描述:  清算组任务配置表
 * 创建人: zengzt
 * 创建时间:2020年5月6日下午4:01:52
 */
@GraphQLModel(fetcher = "t8ClearTaskSetService", table = "T8_CLEAR_TASK_SET")
public class T8ClearTaskSet {

	@GraphQLField(label = "清算组代码",field = "taskGroup")
	private String taskGroup;
	@GraphQLField(label = "任务ID",field = "taskId")
	private String taskId;
	@GraphQLField(label = "系统模块ID",field = "moduleid")
	private String moduleid;
	@GraphQLField(label = "销售商文件批次号",field = "distributorBatch")
	private String distributorBatch;
	@GraphQLField(label = "执行时间",field = "execTime")
	private String execTime;
	@GraphQLField(label = "完成报警时间",field = "alarmTime")
	private String alarmTime;
	@GraphQLField(label = "处理的流水开始时间",field = "procStartTime")
	private String procStartTime;
	@GraphQLField(label = "处理的流水截止时间",field = "procEndTime")
	private String procEndTime;
	@GraphQLField(label = "前置任务校验格式",field = "preTaskId")
	private String preTaskId;
	@GraphQLField(label = "任务参数 JSON 格式",field = "taskParams")
	private String taskParams;
	@GraphQLField(label = "界面显示排序",field = "displayOrder")
	private String displayOrder;
	@GraphQLField(label = "简易视图所属流程图标",field = "simpleFlow")
	private String simpleFlow;
	@GraphQLField(label = "执行优先级，优先级越高，数值越小",field = "execOrder")
	private String execOrder;
	
	/**
	 * 页面展示使用，非配置表字段
	 */
	@GraphQLField(label = "清算任务名称",field = "taskName")
	private String taskName;
	
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
	public String getModuleid() {
		return moduleid;
	}
	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}
	public String getDistributorBatch() {
		return distributorBatch;
	}
	public void setDistributorBatch(String distributorBatch) {
		this.distributorBatch = distributorBatch;
	}
	public String getExecTime() {
		return execTime;
	}
	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}
	public String getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getProcStartTime() {
		return procStartTime;
	}
	public void setProcStartTime(String procStartTime) {
		this.procStartTime = procStartTime;
	}
	public String getProcEndTime() {
		return procEndTime;
	}
	public void setProcEndTime(String procEndTime) {
		this.procEndTime = procEndTime;
	}
	public String getPreTaskId() {
		return preTaskId;
	}
	public void setPreTaskId(String preTaskId) {
		this.preTaskId = preTaskId;
	}
	public String getTaskParams() {
		return taskParams;
	}
	public void setTaskParams(String taskParams) {
		this.taskParams = taskParams;
	}
	public String getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
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
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
}
