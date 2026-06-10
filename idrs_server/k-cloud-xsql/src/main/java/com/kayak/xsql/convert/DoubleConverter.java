package com.kayak.xsql.convert;

import com.kayak.core.util.Tools;

public class DoubleConverter extends Converter<Double> {
	private boolean nullable;

	public DoubleConverter(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	protected Double convert(Object o) {
		if (o == null || o.toString().isEmpty())
			return nullable ? null : 0.0;

		if (o instanceof Number)
			return ((Number) o).doubleValue();

		return new Double(o.toString());
	}
}
