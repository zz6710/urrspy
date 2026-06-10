package com.kayak.xsql.parameter;

import com.kayak.core.desensitized.Desensitized;

public abstract class FieldGetter {
	public abstract Object get(Object params);

	public Desensitized getDesensitized() {
		return null;
	};
}
