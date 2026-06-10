package com.kayak.xsql.parameter;

import java.sql.PreparedStatement;
import java.sql.Types;

import com.kayak.core.desensitized.Desensitized;
import com.kayak.xsql.convert.Converter;

public class ParameterDouble extends Parameter {
	private Converter<Double> converter = Converter.getConverter(Double.class);

	@Override
	public int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception {
		Object v = getter.get(params);
		
		checkParam(v);

		Double value = converter.convert(v, Double.class);

		if (value != null) {
			ps.setDouble(index, value);
		} else {
			ps.setNull(index, Types.DOUBLE);
		}

		Desensitized desensitized = getter.getDesensitized();

		if (desensitized != null) {
			sb.append(fixed).append(desensitized.desensitized(value));
		} else {
			sb.append(fixed).append(value);
		}
		return index + 1;
	}
}
