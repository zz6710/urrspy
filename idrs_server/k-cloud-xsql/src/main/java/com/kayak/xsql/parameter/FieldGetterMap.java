package com.kayak.xsql.parameter;

import java.util.Map;

public class FieldGetterMap extends FieldGetter {
	private String name;

	public FieldGetterMap(String name) {
		this.name = name;
	}

	@Override
	public Object get(Object params) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) params;
		return map.get(name);
	}
}
