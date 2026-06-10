package com.kayak.xsql.parameter;

import java.sql.PreparedStatement;

import com.kayak.core.desensitized.Desensitized;
import com.kayak.xsql.convert.Converter;

public class ParameterString extends Parameter {
	private Converter<String> converter = Converter.getConverter(String.class);

	@Override
	public int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception {
		Object v = getter.get(params);

//		checkParam(v);

		if (v == null) {
			v = "";
		}

		String value = converter.convert(v, String.class);

		ps.setString(index, value);

		Desensitized desensitized = getter.getDesensitized();

		if (desensitized != null) {
			value = desensitized.desensitized(value);
		}

		sb.append(fixed).append("'").append(value).append("'");
		return index + 1;
	}

	@Override
	public void setParams(StringBuilder sb, Object params) throws Exception {
		Object v = getter.get(params);
		if (v == null) {
			v = "";
		}
		String value = converter.convert(v, String.class);
		sb.append(fixed).append("'").append(value).append("'");
	}
}
