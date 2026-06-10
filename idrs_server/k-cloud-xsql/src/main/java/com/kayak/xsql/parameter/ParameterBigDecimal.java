package com.kayak.xsql.parameter;

import java.math.BigDecimal;
import java.sql.PreparedStatement;

import com.kayak.xsql.convert.Converter;

public class ParameterBigDecimal extends Parameter {
	private Converter<BigDecimal> converter = Converter.getConverter(BigDecimal.class);

	@Override
	public int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception {
		Object v = getter.get(params);
		
		checkParam(v);

		BigDecimal value = converter.convert(v, BigDecimal.class);

		ps.setBigDecimal(index, value);

		sb.append(fixed).append(value.longValue());
		return index + 1;
	}
}
