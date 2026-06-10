package com.kayak.xsql.result;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.kayak.core.sql.SqlRow;
import com.kayak.xsql.result.column.ColumnReader;

/**
 * 返回 Map 的结果处理器
 */
public class ResultHandlerMap<T> extends ResultHandler<T> {
	protected Class<T> clazz;

	protected int column; // 列的数量
	protected List<String> columns; // 每列的名称
	protected List<ColumnReader> readers; // 获取每列数据的处理器

	public ResultHandlerMap(Class<T> clazz, List<String> columns, List<ColumnReader> readers) {
		this.clazz = clazz;
		this.column = columns.size();
		this.columns = columns;
		this.readers = readers;
	}

	@Override
	public T build(ResultSet rs) throws Exception {
		Map<String, Object> map = new SqlRow();

		Iterator<String> name = columns.iterator();
		for (int i = 0; i < column; i++) {
			Object o = readers.get(i).read(rs, i + 1);
			map.put(name.next(), o);//
		}

		return clazz.cast(map);
	}
}
