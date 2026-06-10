package com.kayak.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

/**
 * 文件名: TaClearGroupInfo.java
 * 描述:  清算组信息表
 * 创建人: zengzt
 * 创建时间:2020年4月29日下午2:42:12
 */
@GraphQLModel(fetcher = "kbatchGroupInfoService", table = "kbatch_group_info")
public class KbatchGroupInfo {

	@GraphQLField(label = "清算组代码",sql = " task_group = $S{taskGroup}",field = "taskGroup")
	private String taskGroup;
	@GraphQLField(label = "清算组名称",field = "taskGroupName")
	private String taskGroupName;
	@GraphQLField(label = "清算组任务类别",sql = " exec_task_type = $S{execTaskType}",field = "execTaskType")
	private String execTaskType;
	@GraphQLField(label = "跑批顺序",field = "displayOrder")
	private String displayOrder;
	@GraphQLField(label = "前置清算组代码",field = "preTaskGroup")
	private String preTaskGroup;
    @GraphQLField(label = "上一清算组代码",field = "lastTaskGroup")
    private String lastTaskGroup;
	@GraphQLField(label = "跑批日期类型",field = "runningType")
	private String runningType;

	@GraphQLField(label = "应执行时间",field = "shouldExecTime")
	private String shouldExecTime;
	@GraphQLField(label = "未完成报警时间",field = "alarmTime")
	private String alarmTime;

    public String getLastTaskGroup() {
        return lastTaskGroup;
    }

    public void setLastTaskGroup(String lastTaskGroup) {
        this.lastTaskGroup = lastTaskGroup;
    }

    public String getRunningType() {
		return runningType;
	}

	public void setRunningType(String runningType) {
		this.runningType = runningType;
	}

	public String getShouldExecTime() {
		return shouldExecTime;
	}

	public void setShouldExecTime(String shouldExecTime) {
		this.shouldExecTime = shouldExecTime;
	}

	public String getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getTaskGroup() {
		return taskGroup;
	}
	public void setTaskGroup(String taskGroup) {
		this.taskGroup = taskGroup;
	}
	public String getTaskGroupName() {
		return taskGroupName;
	}
	public void setTaskGroupName(String taskGroupName) {
		this.taskGroupName = taskGroupName;
	}
	public String getExecTaskType() {
		return execTaskType;
	}
	public void setExecTaskType(String execTaskType) {
		this.execTaskType = execTaskType;
	}
	public String getPreTaskGroup() {
		return preTaskGroup;
	}
	public void setPreTaskGroup(String preTaskGroup) {
		this.preTaskGroup = preTaskGroup;
	}
	
}
