package com.kayak.pms.T85.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

/**
 * 文件名: TaClearGroupMember.java
 * 描述:  清算组产品销售商配置表实体类
 * 创建人: zengzt
 * 创建时间:2020年5月6日下午3:59:49
 */
@GraphQLModel(fetcher = "t8ClearGroupMemberService", table = "T8_CLEAR_GROUP_MEMBER")
public class T8ClearGroupMember {

	@GraphQLField(label = "清算组代码",sql = " mem.task_group = $S{taskGroup}",field = "taskGroup")
	private String taskGroup;

	@GraphQLField(label = "清算组代码",field = "oldTaskGroup")
	private String oldTaskGroup;

	@GraphQLField(label = "组清算任务类别",field = "execTaskType", sql = " mem.exec_task_type = $S{execTaskType}")
	private String execTaskType;

	@GraphQLField(label = "组成员名称",field = "groupMemberName")
	private String groupMemberName;

	@GraphQLField(label = "是否已经组成员",field = "isGroupMember")
	private String isGroupMember;
	
	@GraphQLField(label = "组成员代码",field = "groupMember",sql = " mem.group_member = $S{groupMember}")
	private String groupMember;

	@GraphQLField(label = "更新时间",field = "uptDate")
	private String uptDate;

	@GraphQLField(label = "创建时间",field = "crtDate")
	private String crtDate;

	public String getOldTaskGroup() {
		return oldTaskGroup;
	}

	public void setOldTaskGroup(String oldTaskGroup) {
		this.oldTaskGroup = oldTaskGroup;
	}

	public String getTaskGroup() {
		return taskGroup;
	}

	public void setTaskGroup(String taskGroup) {
		this.taskGroup = taskGroup;
	}

	public String getExecTaskType() {
		return execTaskType;
	}

	public void setExecTaskType(String execTaskType) {
		this.execTaskType = execTaskType;
	}

	public String getGroupMemberName() {
		return groupMemberName;
	}

	public void setGroupMemberName(String groupMemberName) {
		this.groupMemberName = groupMemberName;
	}

	public String getGroupMember() {
		return groupMember;
	}

	public void setGroupMember(String groupMember) {
		this.groupMember = groupMember;
	}

	public String getIsGroupMember() {
		return isGroupMember;
	}

	public void setIsGroupMember(String isGroupMember) {
		this.isGroupMember = isGroupMember;
	}

	public String getUptDate() {
		return uptDate;
	}

	public void setUptDate(String uptDate) {
		this.uptDate = uptDate;
	}

	public String getCrtDate() {
		return crtDate;
	}

	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}

	@Override
	public String toString() {
		return "TaClearGroupMember [taskGroup=" + taskGroup + ", execTaskType="
				+ execTaskType + ", groupMemberName=" + groupMemberName
				+ ", isGroupMember=" + isGroupMember + ", groupMember="
				+ groupMember + "]";
	}

}
