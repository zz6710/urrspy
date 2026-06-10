package com.kayak.xsql.convert;

import java.sql.Clob;

public class StringConverter extends Converter<String> {
	@Override
	protected String convert(Object o) {
		if (o == null)
			return null;

		if (o instanceof Clob) {
			Clob clob = (Clob) o;
			try {
				return clob.getSubString(1, (int) clob.length());
			} catch (Exception e) {
				throw new RuntimeException("不能转换 " + o.getClass().getSimpleName() + " 为字符串类型");
			}
		}

		return o.toString();
	}
}
