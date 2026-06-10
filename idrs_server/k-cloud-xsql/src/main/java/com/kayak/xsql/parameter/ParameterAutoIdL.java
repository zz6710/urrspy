package com.kayak.xsql.parameter;

import java.sql.PreparedStatement;

import com.kayak.xsql.convert.Converter;

public class ParameterAutoIdL extends Parameter {
	private Converter<Long> converter = Converter.getConverter(Long.TYPE);

	@Override
	public int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception {
		Object v = getter.get(params);

		long value = converter.convert(v, Long.class);

		ps.setLong(index, value);

		sb.append(fixed).append(value);
		return index + 1;
	}
}