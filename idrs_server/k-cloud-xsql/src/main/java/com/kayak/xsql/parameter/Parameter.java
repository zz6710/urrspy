package com.kayak.xsql.parameter;

import java.sql.PreparedStatement;

import org.springframework.util.Assert;

public abstract class Parameter {
	protected String fixed; // 前半部分的固定文本
	protected FieldGetter getter;
	protected String paramName;

	public void setFixed(String fixed) {
		this.fixed = fixed;
	}

	public void setGetter(FieldGetter getter) {
		this.getter = getter;
	}

	public void sql(StringBuilder sb, Object params) {
		sb.append(fixed).append("?");
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	
	protected void checkParam(Object v) {
//		Assert.notNull(v, "参数：" + paramName + " 不能为空");
	}

	public abstract int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception;
	public void setParams(StringBuilder sb, Object params) throws Exception {
	}
}
