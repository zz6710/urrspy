package com.kayak.xsql.result;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kayak.xsql.XsqlUtils;
import com.kayak.xsql.convert.Converter;
import com.kayak.xsql.result.column.*;

/**
 * 结果规划器, 用于创建结果处理器
 */
public class ResultPlanner {
	private static Map<Integer, ColumnReader> COLUMN_READERS = new HashMap<Integer, ColumnReader>();
	static {
		COLUMN_READERS.put(Types.BOOLEAN, new ColumnReaderBoolean());
		COLUMN_READERS.put(Types.BIT, new ColumnReaderBoolean());
		COLUMN_READERS.put(Types.DOUBLE, new ColumnReaderDouble());
		COLUMN_READERS.put(Types.REAL, new ColumnReaderDouble());
		COLUMN_READERS.put(Types.TINYINT, new ColumnReaderInteger());
		COLUMN_READERS.put(Types.INTEGER, new ColumnReaderInteger());
		COLUMN_READERS.put(Types.BIGINT, new ColumnReaderBigInt());
		COLUMN_READERS.put(Types.NUMERIC, new ColumnReaderBigDecimal());
		COLUMN_READERS.put(Types.DECIMAL, new ColumnReaderBigDecimal());
		COLUMN_READERS.put(Types.CLOB, new ColumnReaderText());
		COLUMN_READERS.put(Types.NCLOB, new ColumnReaderText());
		COLUMN_READERS.put(Types.VARCHAR, new ColumnReaderVarchar());
		COLUMN_READERS.put(Types.NVARCHAR, new ColumnReaderVarchar());
		COLUMN_READERS.put(Types.LONGVARCHAR, new ColumnReaderVarchar());
		COLUMN_READERS.put(Types.CHAR, new ColumnReaderVarchar());
		COLUMN_READERS.put(Types.NCHAR, new ColumnReaderVarchar());
		COLUMN_READERS.put(Types.DATE, new ColumnReaderDate());
		COLUMN_READERS.put(Types.TIME, new ColumnReaderTimestamp());
		COLUMN_READERS.put(Types.TIMESTAMP, new ColumnReaderTimestamp());
		COLUMN_READERS.put(Types.BLOB, new ColumnReaderBlob());
		COLUMN_READERS.put(Types.BINARY, new ColumnReaderVarchar());
		COLUMN_READERS.put(Types.LONGVARBINARY, new ColumnReaderVarchar());
	}

	private static Set<Class<?>> SINGLE_VALUE_TYPES = new HashSet<>();
	static {
		SINGLE_VALUE_TYPES.add(Integer.class);
		SINGLE_VALUE_TYPES.add(Integer.TYPE);
		SINGLE_VALUE_TYPES.add(Long.class);
		SINGLE_VALUE_TYPES.add(Long.TYPE);
		SINGLE_VALUE_TYPES.add(Double.class);
		SINGLE_VALUE_TYPES.add(Double.TYPE);
		SINGLE_VALUE_TYPES.add(Boolean.class);
		SINGLE_VALUE_TYPES.add(Boolean.TYPE);
		SINGLE_VALUE_TYPES.add(Date.class);
		SINGLE_VALUE_TYPES.add(String.class);
		SINGLE_VALUE_TYPES.add(byte[].class);
	}

	protected ColumnReader getColumnReader(int columnType) {
		return COLUMN_READERS.get(columnType);
	}

	public <T> ResultHandler<T> plan(Class<T> clazz, ResultSetMetaData md) throws Exception {
		List<String> columns = new ArrayList<String>();
		List<ColumnReader> readers = new ArrayList<ColumnReader>();
		int n = md.getColumnCount();
		for (int i = 0; i < n; i++) {
			int columnType = md.getColumnType(i + 1);
			ColumnReader reader = getColumnReader(columnType);
			if (reader == null) throw new Exception("不支持的数据列类型: " + columnType);
			readers.add(reader);

			columns.add(md.getColumnLabel(i + 1));
		}

		// 单值类型
		if (SINGLE_VALUE_TYPES.contains(clazz)) {
			return new ResultHandlerSingle<T>(clazz, readers.get(0), Converter.getConverter(clazz));
		}

		// Map
		if (Map.class.isAssignableFrom(clazz)) {
			return new ResultHandlerMap<T>(clazz, columns, readers);
		}

		// Bean
		List<Field> fields = new ArrayList<>();
		List<Converter<?>> converters = new ArrayList<>();
		for (String name : columns) {
			if ("xsql_rownum".equalsIgnoreCase(name)) continue;

			String name2 = XsqlUtils.sqlToJava(name.toLowerCase());

			Field field = null;
			for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
				try {
					field = c.getDeclaredField(name2);
				} catch (NoSuchFieldException e) {
					continue;
				}
				if (Modifier.isStatic(field.getModifiers())) continue;

				if (!field.isAccessible()) field.setAccessible(true);
				fields.add(field);
				Converter<?> converter = Converter.getConverter(field.getType());
				converters.add(converter);
				break;
			}
			if (field == null) throw new Exception("字段不存在: " + clazz.getName() + "." + name2);
		}
		return new ResultHandlerBean<T>(clazz, fields, readers, converters);
	}

}
