package com.kayak.xsql.result;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.kayak.xsql.convert.Converter;
import com.kayak.xsql.result.column.ColumnReader;

/**
 * 返回 Map 的结果处理器
 */
public class ResultHandlerBean<T> extends ResultHandler<T> {
	protected Class<T> clazz;

	protected int column; // 列的数量
	private List<Field> fields;
	private List<Class<?>> fieldClasses = new ArrayList<>();
	private List<ColumnReader> readers; // 获取每列数据的处理器
	private List<Converter<?>> converters; // 数据转换

	public ResultHandlerBean(Class<T> clazz, List<Field> fields, List<ColumnReader> readers,
			List<Converter<?>> converters) throws Exception {
		this.clazz = clazz;
		this.column = fields.size();
		this.fields = fields;
		this.readers = readers;
		this.converters = converters;

		for (Field f : fields) {
			Class<?> type = f.getType();
			fieldClasses.add(type);
		}
	}

	@Override
	public T build(ResultSet rs) throws Exception {
		T bean = clazz.newInstance();

		for (int i = 0; i < column; i++) {
			Field field = fields.get(i);
			Object v = readers.get(i).read(rs, i + 1);
			Converter<?> converter = converters.get(i);
			if (converter != null)
				v = converter.convert(v, field.getType());
			field.set(bean, v);
		}

		return bean;
	}
}
