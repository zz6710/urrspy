package com.kayak.xsql.parameter;

import java.sql.PreparedStatement;

import com.kayak.xsql.convert.Converter;

public class ParameterAutoIdS extends Parameter {

	private Converter<String> converter = Converter.getConverter(String.class);

	@Override
	public int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception {
		Object v = getter.get(params);

		String value = converter.convert(v, String.class);

		ps.setString(index, converter.convert(v, String.class));
		
		sb.append(fixed).append("'").append(value).append("'");
		return index + 1;
	}

}
