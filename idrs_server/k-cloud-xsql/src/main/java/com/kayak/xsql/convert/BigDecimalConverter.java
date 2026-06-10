package com.kayak.xsql.convert;

import java.math.BigDecimal;

public class BigDecimalConverter extends Converter<BigDecimal> {
	private boolean nullable;

	protected BigDecimalConverter(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	public BigDecimal convert(Object o) {
		if (o == null || o.toString().isEmpty())
			return nullable ? null : new BigDecimal("0");

		if (o instanceof BigDecimal)
			return ((BigDecimal) o);

		return new BigDecimal(o.toString());
	}
}
