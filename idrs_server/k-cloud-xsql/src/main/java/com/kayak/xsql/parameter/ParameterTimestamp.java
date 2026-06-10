package com.kayak.xsql.parameter;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

import com.kayak.xsql.convert.Converter;

public class ParameterTimestamp extends Parameter {
	private Converter<Timestamp> converter = Converter.getConverter(Timestamp.class);

	@Override
	public int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception {
		Object v = getter.get(params);
		
		checkParam(v);

		Timestamp value = converter.convert(v, Timestamp.class);

		ps.setTimestamp(index, value);

		sb.append(fixed).append(value);
		return index + 1;
	}
}
