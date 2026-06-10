package com.kayak.xsql.convert;

import java.sql.Timestamp;
import java.util.Date;

class TimestampConverter extends Converter<Timestamp> {
	@Override
	protected Timestamp convert(Object o) {
		if (o == null || o.toString().isEmpty())
			return null;

		if (o instanceof Date)
			return new Timestamp(((Date) o).getTime());

		try {
			return (Timestamp) o;
		} catch (Exception e) {
			throw new RuntimeException("不能转换 " + o.getClass().getSimpleName() + " 为时间戳类型");
		}
	}
}
