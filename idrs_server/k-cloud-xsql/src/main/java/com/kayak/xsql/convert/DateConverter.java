package com.kayak.xsql.convert;

import java.lang.reflect.Method;
import java.sql.Date;

class DateConverter extends Converter<Date> {
	@Override
	protected Date convert(Object o) {
		if (o == null || o.toString().isEmpty())
			return null;

		if (o instanceof Date)
			return (Date) o;

		String s = o.toString();

		Class<?> clazz = o.getClass();
		if (clazz.getName().equals("oracle.sql.TIMESTAMP")) {
			try {
				Method method = clazz.getMethod("timestampValue", (Class<?>[]) null);

				return (Date) method.invoke(o, (Object[]) null);
			} catch (Exception e) {
				throw new RuntimeException("不能转换 '" + s + "' 为日期类型", e);
			}
		}

		return (Date) o;
	}
}
