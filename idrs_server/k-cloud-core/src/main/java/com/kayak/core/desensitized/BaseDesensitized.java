package com.kayak.core.desensitized;

public class BaseDesensitized implements Desensitized {

	@Override
	public String desensitized(Object value) {
		return "******";
	}

}
