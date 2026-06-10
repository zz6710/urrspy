package com.kayak.xsql.convert;

public class LongConverter extends Converter<Long> {
	private boolean nullable;

	public LongConverter(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	protected Long convert(Object o) {
		if (o == null || o.toString().isEmpty())
			return nullable ? null : 0L;

		if (o instanceof Number)
			return ((Number) o).longValue();

		return new Long(o.toString());
	}
}
