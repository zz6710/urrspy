package com.kayak.pms.T85.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

/**
 * 文件名: T85002FlowInfo.java
 * 描述:   清算流程对象
 * 创建人: zengzt
 * 创建时间:2020年5月25日下午3:18:24
 */
@GraphQLModel(fetcher = "t85002Service",table = "T8_CLEAR_TASK_EXEC")
public class T85002FlowInfo {

	@GraphQLField(label = "所属清算流程块ID", field = "simpleFlow")
	private String simpleFlow;
	@GraphQLField(label = "成功任务数", field = "successNum")
	private String successNum;
	@GraphQLField(label = "失败任务数", field = "failNum")
	private String failNum;
	@GraphQLField(label = "执行中任务数", field = "executingNum")
	private String executingNum;
	@GraphQLField(label = "未执行任务数", field = "noExecuteNum")
	private String noExecuteNum;
	@GraphQLField(label = "应执行日期", field = "taskDate")
	private String taskDate;
	@GraphQLField(label = "总任务数", field = "clearCount")
	private String clearCount;
	
	public String getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}
	public String getSimpleFlow() {
		return simpleFlow;
	}
	public void setSimpleFlow(String simpleFlow) {
		this.simpleFlow = simpleFlow;
	}
	public String getSuccessNum() {
		return successNum;
	}
	public void setSuccessNum(String successNum) {
		this.successNum = successNum;
	}
	public String getFailNum() {
		return failNum;
	}
	public void setFailNum(String failNum) {
		this.failNum = failNum;
	}
	public String getExecutingNum() {
		return executingNum;
	}
	public void setExecutingNum(String executingNum) {
		this.executingNum = executingNum;
	}
	public String getNoExecuteNum() {
		return noExecuteNum;
	}
	public void setNoExecuteNum(String noExecuteNum) {
		this.noExecuteNum = noExecuteNum;
	}
	public String getClearCount() {
		return clearCount;
	}
	public void setClearCount(String clearCount) {
		this.clearCount = clearCount;
	}
	
}
