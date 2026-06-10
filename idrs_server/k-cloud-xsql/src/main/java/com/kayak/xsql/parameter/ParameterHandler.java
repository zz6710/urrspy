package com.kayak.xsql.parameter;

import java.sql.PreparedStatement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayak.core.sql.UpdateResult;

public class ParameterHandler {

	private static final Logger log = LoggerFactory.getLogger(ParameterHandler.class);

	private List<Parameter> vars; // SQL中出现的参数
	private String tail; // SQL语句末段
	private UpdateResult updateResult;// 更新sql语句的更新结果

	private FieldGetterAutoId fieldGetterAutoId;

	public ParameterHandler(List<Parameter> vars, String tail, UpdateResult updateResult,
			FieldGetterAutoId fieldGetterAutoId) {
		this.vars = vars;
		this.tail = tail;
		this.updateResult = updateResult;
		this.fieldGetterAutoId = fieldGetterAutoId;
	}

	public StringBuilder setParameters(PreparedStatement ps, Object params) throws Exception {
		return setParameters(ps, params, false);

	}

	public StringBuilder setParametersNoLog(PreparedStatement ps, Object params) throws Exception {
		return setParameters(ps, params, true);

	}

	public StringBuilder setParameters(PreparedStatement ps, Object params, boolean sqlNoLog) throws Exception {
		StringBuilder sb = new StringBuilder();
		int index = 1;
		for (Parameter p : vars) {
			index = p.set(sb, ps, params, index);
		}
		sb.append(tail);

		if (!sqlNoLog) {
			log.info("执行SQL：" + sb.toString());
		}
		return sb;
	}

	public StringBuilder setParameters(Object params) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (Parameter p : vars) {
			p.setParams(sb, params);
		}
		sb.append(tail);
		return sb;
	}

	/** 获取重写后的SQL语句 */
	public String getSql(Object params) {
		StringBuilder sb = new StringBuilder();
		for (Parameter p : vars) {
			p.sql(sb, params);
		}
		sb.append(tail);

		return sb.toString();
	}

	public UpdateResult getUpdateResult() {
		return updateResult;
	}

	public FieldGetterAutoId getFieldGetterAutoId() {
		return fieldGetterAutoId;
	}

}