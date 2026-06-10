package com.kayak.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

/**
 * 文件名: TaClearTaskStep.java
 * 描述: 
 * 创建人: zengzt
 * 创建时间:2020年6月6日下午4:27:06
 */
@GraphQLModel(fetcher = "kbatchTaskStepService", table = "kbatch_task_step")
public class KbatchTaskStep {

	@GraphQLField(kkhtml = "KFieldText",label = "任务ID",sql = " task_id = $S{taskId} ",field = "taskId")
	private String taskId;
	@GraphQLField(label = "步骤号",field = "stepNo")
	private String stepNo;
	@GraphQLField(label = "步骤名称",field = "stepName")
	private String stepName;
	@GraphQLField(label = "是否可重复执行",field = "canReplay")
	private String canReplay;
	@GraphQLField(label = "是否可跳过",field = "canSkip")
	private String canSkip;
	@GraphQLField(label = "是否分片任务",field = "isSlice")
	private String isSlice;
	@GraphQLField(label = "分片任务服务处理类",field = "sliceServiceClass")
	private String sliceServiceClass;
	@GraphQLField(label = "分片任务请求参数类",field = "sliceReqClass")
	private String sliceReqClass;
	
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
	public String getCanReplay() {
		return canReplay;
	}
	public void setCanReplay(String canReplay) {
		this.canReplay = canReplay;
	}
	public String getCanSkip() {
		return canSkip;
	}
	public void setCanSkip(String canSkip) {
		this.canSkip = canSkip;
	}
	public String getIsSlice() {
		return isSlice;
	}
	public void setIsSlice(String isSlice) {
		this.isSlice = isSlice;
	}
	public String getSliceServiceClass() {
		return sliceServiceClass;
	}
	public void setSliceServiceClass(String sliceServiceClass) {
		this.sliceServiceClass = sliceServiceClass;
	}
	public String getSliceReqClass() {
		return sliceReqClass;
	}
	public void setSliceReqClass(String sliceReqClass) {
		this.sliceReqClass = sliceReqClass;
	}
}
