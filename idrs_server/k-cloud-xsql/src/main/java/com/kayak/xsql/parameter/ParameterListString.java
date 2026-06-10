package com.kayak.xsql.parameter;

import com.kayak.core.sql.SqlUtil;
import com.kayak.core.util.Tools;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;

@Slf4j
public class ParameterListString extends Parameter {

	@Override
	public int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception {
		// TODO 后期使用占位符实现
		addParams(sb, params);
		return index;
	}

	@Override
	public void sql(StringBuilder sb, Object params) {
		addParams(sb, params);
	}

	private void addParams(StringBuilder sb, Object params) {
		Object v = getter.get(params);
		String value = Tools.obj2Str(v);

		if(Tools.isSqlInjection(value)) {
			log.error("检测到sql注入[{}]!!!", value);
			throw new RuntimeException("检测到sql注入!!!");
		}

		String[] values = value.split(",");
		StringBuilder _sb = new StringBuilder();
		for (String _value : values) {
			_sb.append(",");
			_sb.append(SqlUtil.str2query(_value));
		}
		value = _sb.toString();
		if (value.length() > 0) {
			value = value.substring(1);
		}

		sb.append(fixed).append(value);
	}

}
