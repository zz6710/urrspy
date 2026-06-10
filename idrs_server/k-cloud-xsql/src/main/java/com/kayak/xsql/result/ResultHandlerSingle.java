package com.kayak.xsql.result;

import java.sql.ResultSet;

import com.kayak.xsql.convert.Converter;
import com.kayak.xsql.result.column.ColumnReader;

/**
 * 返回 Map 的结果处理器
 */
public class ResultHandlerSingle<T> extends ResultHandler<T> {
	protected Class<T> clazz;

	private ColumnReader reader; // 获取每列数据的处理器
	private Converter<T> converter; // 类型转换器

	public ResultHandlerSingle(Class<T> clazz, ColumnReader reader, Converter<T> converter) throws Exception {
		this.clazz = clazz;
		this.reader = reader;
		this.converter = converter;
	}

	@Override
	public T build(ResultSet rs) throws Exception {
		Object o = reader.read(rs, 1);
		return converter.convert(o, clazz);
	}
}
