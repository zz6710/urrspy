package com.kayak.pms.T85.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

import java.util.List;

/**
 * 文件名: TaClearGroupMemberList.java
 * 描述:   清算组成员数组对象
 * 创建人: zengzt
 * 创建时间:2020年5月8日上午11:10:59
 */
@GraphQLModel(fetcher = "t8ClearGroupMemberListService", table = "T8_CLEAR_GROUP_MEMBER")
public class T8ClearGroupMemberList {

	@GraphQLField(label = "组成员列表",field = "memberList")
	private List<T8ClearGroupMember> memberList;
	
	@GraphQLField(label = "清算组代码",field = "taskGroup")
	private String taskGroup;
	
	public List<T8ClearGroupMember> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<T8ClearGroupMember> memberList) {
		this.memberList = memberList;
	}
	
	public String getTaskGroup() {
		return taskGroup;
	}

	public void setTaskGroup(String taskGroup) {
		this.taskGroup = taskGroup;
	}

	@Override
	public String toString() {
		return "TaClearGroupMemberList [memberList=" + memberList + "]";
	}

}
