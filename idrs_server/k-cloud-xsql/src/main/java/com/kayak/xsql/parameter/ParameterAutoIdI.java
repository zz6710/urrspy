package com.kayak.xsql.parameter;

import java.sql.PreparedStatement;

import com.kayak.xsql.convert.Converter;

public class ParameterAutoIdI extends Parameter {
	private Converter<Integer> converter = Converter.getConverter(Integer.TYPE);

	@Override
	public int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception {
		Object v = getter.get(params);

		int value = converter.convert(v, Integer.class);

		ps.setInt(index, value);

		sb.append(fixed).append(value);
		return index + 1;
	}
}
