package com.kayak.pms.T85.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

/**
 * 文件名: T8ProdTaskSet.java
 * 描述:  产品清算任务配置表
 * 创建人: zengzt
 * 创建时间:2020年5月6日下午4:05:47
 */
@GraphQLModel(fetcher = "t8ProdTaskSetService", table = "T8_PROD_TASK_SET")
public class T8ProdTaskSet {
	
	@GraphQLField(label = "产品形态",sql = " prod_mode = $S{prodMode}",field = "prodMode" )
	private String prodMode;
	@GraphQLField(label = "任务ID",field = "taskId")
	private String taskId;
	@GraphQLField(label = "系统模块ID",field = "moduleid")
	private String moduleid;
	@GraphQLField(label = "执行时间",field = "execTime")
	private String execTime;
	@GraphQLField(label = "未完成报警时间",field = "alarmTime")
	private String alarmTime;
	@GraphQLField(label = "前置任务校验格式",field = "preTaskId")
	private String preTaskId;
	@GraphQLField(label = "任务参数 JSON 格式",field = "taskParams")
	private String taskParams;
	@GraphQLField(label = "界面显示排序",field = "displayOrder")
	private String displayOrder;
	@GraphQLField(label = "简易视图所属流程图标",field = "simpleFlow")
	private String simpleFlow;
	@GraphQLField(label = "执行优先级",field = "execOrder")
	private String execOrder;
	
	@GraphQLField(label = "任务名称",field = "taskName")
	private String taskName;
	@GraphQLField(label = "产品清算所属的生命周期时段（01-募集、02-成立、03-存续、04-到期）",field = "lifecycleType")
	private String lifecycleType;
	
	public String getLifecycleType() {
		return lifecycleType;
	}
	public void setLifecycleType(String lifecycleType) {
		this.lifecycleType = lifecycleType;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getProdMode() {
		return prodMode;
	}
	public void setProdMode(String prodMode) {
		this.prodMode = prodMode;
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
	
}
