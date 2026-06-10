package com.kayak.code.model;

import java.util.HashMap;
import java.util.Map;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "dbTableService")
public class DbTableField {

	public static Map<String, String> typeMap = new HashMap<String, String>();

	static {
		typeMap.put("decimal", "D");
		typeMap.put("varchar", "S");
		typeMap.put("varchar2", "S");
		typeMap.put("char", "S");
		typeMap.put("text", "S");
		typeMap.put("int", "I");
		typeMap.put("integer", "I");
		typeMap.put("bigint", "I");
		typeMap.put("bigint unsigned", "I");
		typeMap.put("datetime", "S");

	}

	@GraphQLField
	private boolean key;
	@GraphQLField
	private String field;
	@GraphQLField
	private String dbField;
	@GraphQLField
	private String fieldUpperCaseFirst;
	@GraphQLField
	private String type;
	@GraphQLField
	private String comment;
	@GraphQLField
	private String table;
	@GraphQLField
	private Integer sharding = 0;
	@GraphQLField
	private String param;

	private String autoIdParam;

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public String getField() {
		return field;
	}

	public String getDbField() {
		return dbField;
	}

	public void setDbField(String dbField) {
		this.dbField = dbField;
	}

	public String getFieldUpperCaseFirst() {
		return fieldUpperCaseFirst;
	}

	public void setFieldUpperCaseFirst(String fieldUpperCaseFirst) {
		this.fieldUpperCaseFirst = fieldUpperCaseFirst;
	}

	public void setField(String field) {
		this.field = field;
		this.fieldUpperCaseFirst = field.substring(0, 1).toUpperCase() + field.substring(1);

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		if (key) {
			this.param = "$AUTOID" + typeMap.get(type) + "{" + field + "}";
			this.autoIdParam = "$" + typeMap.get(type) + "{" + field + "}";
		} else {
			this.param = "$" + typeMap.get(type) + "{" + field + "}";
		}

	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public Integer getSharding() {
		return sharding;
	}

	public void setSharding(Integer sharding) {
		this.sharding = sharding;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getAutoIdParam() {
		return autoIdParam;
	}

	public void setAutoIdParam(String autoIdParam) {
		this.autoIdParam = autoIdParam;
	}

}
