package com.kayak.xsql.parameter;

import com.kayak.core.util.Tools;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;

@Slf4j
public class ParameterListNumber extends Parameter {

	@Override
	public int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception {
		// TODO 后期使用占位符实现
		String value = getValue(params);
		sb.append(fixed).append(value);
		return index;
	}

	@Override
	public void sql(StringBuilder sb, Object params) {
		String value = getValue(params);
		sb.append(fixed).append(value);
	}



	private String getValue(Object params) {
		Object v = getter.get(params);
		String value = Tools.obj2Str(v);

		if (Tools.isSqlInjection(value)) {
			log.error("检测到sql注入[{}]!!!", value);
			throw new RuntimeException("检测到sql注入!!!");
		}
		return value;
	}
}
