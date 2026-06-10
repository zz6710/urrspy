package com.kayak.pms.T85.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

import java.util.List;

/**
 * 文件名: TaClearTaskSetList.java
 * 描述:   清算任务配置数组对象
 * 创建人: zengzt
 * 创建时间:2020年5月13日下午2:46:36
 */
@GraphQLModel(fetcher = "t8ClearTaskSetListService", table = "T8_CLEAR_TASK_SET")
public class T8ClearTaskSetList {

	@GraphQLField(label = "组任务列表",field = "taskSetList")
	private List<T8ClearTaskSet> taskSetList;
	
	@GraphQLField(label = "清算组代码",field = "taskGroup")
	private String taskGroup;

	public List<T8ClearTaskSet> getTaskSetList() {
		return taskSetList;
	}

	public void setTaskSetList(List<T8ClearTaskSet> taskSetList) {
		this.taskSetList = taskSetList;
	}

	public String getTaskGroup() {
		return taskGroup;
	}

	public void setTaskGroup(String taskGroup) {
		this.taskGroup = taskGroup;
	}
	
	@Override
	public String toString() {
		return "TaClearTaskSetList [taskSetList=" + taskSetList + "]";
	}

}
