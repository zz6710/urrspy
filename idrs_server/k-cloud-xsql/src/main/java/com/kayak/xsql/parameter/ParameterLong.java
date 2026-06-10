package com.kayak.xsql.parameter;

import java.sql.PreparedStatement;

import com.kayak.core.desensitized.Desensitized;
import com.kayak.xsql.convert.Converter;

public class ParameterLong extends Parameter {
	private Converter<Long> converter = Converter.getConverter(Long.class);

	@Override
	public int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception {
		Object v = getter.get(params);
		
		checkParam(v);

		long value = converter.convert(v, Long.class);

		ps.setLong(index, value);

		Desensitized desensitized = getter.getDesensitized();

		if (desensitized != null) {
			sb.append(fixed).append(desensitized.desensitized(value));
		} else {
			sb.append(fixed).append(value);
		}
		return index + 1;
	}


	@Override
	public void setParams(StringBuilder sb, Object params) throws Exception {
		Object v = getter.get(params);
		long value = converter.convert(v, Long.class);
		sb.append(fixed).append(value);
	}
}
