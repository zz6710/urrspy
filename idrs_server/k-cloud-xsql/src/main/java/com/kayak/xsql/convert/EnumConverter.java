package com.kayak.xsql.convert;

/**
 * 枚举类型转换器
 * 
 * @author zuojie
 * @param <E>
 *
 */
class EnumConverter extends Converter<Enum<?>> {
	@Override
	protected Enum<?> convert(Object o) {
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Enum<?> convert(Object o, Class<?> clazz) {
		if (o == null || o.toString().isEmpty()) // 数据库定义为default ''的情况
			return null;

		if (clazz.isAssignableFrom(o.getClass()))
			return (Enum<?>) o;

		return Enum.valueOf((Class<Enum>) clazz, o.toString());
	}

}
