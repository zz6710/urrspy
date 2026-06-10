package com.kayak.pms.T85.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

import java.util.List;

/**
 * 文件名: Ta5002.java
 * 描述:  清算流程
 * 创建人: zengzt
 * 创建时间:2020年5月23日上午10:28:17
 */

@GraphQLModel(fetcher = "t85002Service",table = "T8_CLEAR_TASK_EXEC")
public class T85002 {

	@GraphQLField(label = "工作日", field = "workdate")
	private String workdate;

	@GraphQLField(label = "清算流程信息", field = "flowInfos")
	private List<T85002FlowInfo> flowInfos;

	public String getWorkdate() {
		return workdate;
	}

	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}

	public List<T85002FlowInfo> getFlowInfos() {
		return flowInfos;
	}

	public void setFlowInfos(List<T85002FlowInfo> flowInfos) {
		this.flowInfos = flowInfos;
	}
}
