package com.kayak.xsql.parameter;

public class FieldGetterConst<T> extends FieldGetter {
	private T value;

	public FieldGetterConst(T value) {
		this.value = value;
	}

	@Override
	public Object get(Object params) {
		return this.value;
	}
}
