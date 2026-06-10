package com.kayak.xsql.convert;

public class IntegerConverter extends Converter<Integer> {
	private boolean nullable;

	public IntegerConverter(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	protected Integer convert(Object o) {
		if (o == null || o.toString().isEmpty())
			return nullable ? null : 0;

		if (o instanceof Number)
			return ((Number) o).intValue();

		if (o instanceof Boolean)
			return ((Boolean) o).booleanValue() ? 1 : 0;

		return new Integer(o.toString());
	}
}
