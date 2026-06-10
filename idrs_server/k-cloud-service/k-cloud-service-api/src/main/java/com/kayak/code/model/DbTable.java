package com.kayak.code.model;

import java.util.List;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "dbTableService")
public class DbTable {

	@GraphQLField(key = true, label = "表名", field = "Name")
	private String name;
	@GraphQLField(key = true, label = "表描述", field = "Comment")
	private String comment;
	@GraphQLField(key = true, label = "数据源编号")
	private Integer sharding = 0;

	private String fields;
	private String insertParams;
	private String updateParams;
	private String keyParams;

	private List<DbTableField> tableFields;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getSharding() {
		return sharding;
	}

	public void setSharding(Integer sharding) {
		this.sharding = sharding;
	}

	public List<DbTableField> getTableFields() {
		return tableFields;
	}

	public void setTableFields(List<DbTableField> tableFields) {
		this.tableFields = tableFields;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getInsertParams() {
		return insertParams;
	}

	public void setInsertParams(String insertParams) {
		this.insertParams = insertParams;
	}

	public String getUpdateParams() {
		return updateParams;
	}

	public void setUpdateParams(String updateParams) {
		this.updateParams = updateParams;
	}

	public String getKeyParams() {
		return keyParams;
	}

	public void setKeyParams(String keyParams) {
		this.keyParams = keyParams;
	}

}
