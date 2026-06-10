package com.kayak.xsql.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/** 记录Bean类型信息 */
class ClassInfo {
	private Map<String, Field> fields;

	public static ClassInfo create(Class<?> type) throws Exception {
		Map<String, Field> fields = new HashMap<>();
		for (Class<?> c = type; c != null; c = c.getSuperclass()) {
			for (Field field : c.getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers()))
					continue;
				if (!field.isAccessible())
					field.setAccessible(true);
				fields.put(field.getName(), field);
			}
		}

		ClassInfo ci = new ClassInfo();
		ci.setFields(fields);

		return ci;
	}

	public Map<String, Field> getFields() {
		return fields;
	}

	public void setFields(Map<String, Field> fields) {
		this.fields = fields;
	}

}
