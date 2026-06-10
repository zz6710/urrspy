package com.kayak.xsql.parameter;

import java.sql.Date;
import java.sql.PreparedStatement;

import com.kayak.core.desensitized.Desensitized;
import com.kayak.xsql.convert.Converter;

public class ParameterDate extends Parameter {
	private Converter<Date> converter = Converter.getConverter(Date.class);

	@Override
	public int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception {
		Object v = getter.get(params);
		
		checkParam(v);

		Date value = converter.convert(v, Date.class);

		ps.setDate(index, value);

		Desensitized desensitized = getter.getDesensitized();

		if (desensitized != null) {
			sb.append(fixed).append(desensitized.desensitized(value));
		} else {
			sb.append(fixed).append(value);
		}
		return index + 1;
	}
}
