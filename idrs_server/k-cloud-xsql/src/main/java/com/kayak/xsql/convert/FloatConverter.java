package com.kayak.xsql.convert;

class FloatConverter extends Converter<Float> {
	private boolean nullable;

	public FloatConverter(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	protected Float convert(Object o) {
		if (o == null || o.toString().isEmpty())
			return nullable ? null : 0.0f;

		if (o instanceof Number)
			return ((Number) o).floatValue();

		return new Float(o.toString());
	}
}
