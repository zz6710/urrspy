package com.kayak.core.util;

import java.util.HashMap;

public class ParamsBuilder {
	private HashMap<String, Object> map = new HashMap<String, Object>();

	public ParamsBuilder put(String key, Object value) {
		map.put(key, value);
		return this;
	}

	public HashMap<String, Object> build() {
		return map;
	}
}
