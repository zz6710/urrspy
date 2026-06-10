package com.kayak.xsql.helper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kayak.core.sql.UpdateResult;
import com.kayak.xsql.Xsql;
import com.kayak.xsql.XsqlUtils;

/**
 * 在Xsql基础之上进行包装，采用面向对象的方式进行访问
 * 
 * @author zuojie
 * 
 */
public abstract class XsqlHelper {
	private static final Map<Class<?>, String> map = new HashMap<>(); // 类型到类型符的映射
	static {
		map.put(String.class, "s");
		map.put(Integer.class, "i");
		map.put(Integer.TYPE, "i");
		map.put(Long.class, "d");
		map.put(Long.TYPE, "d");
		map.put(Double.class, "f");
		map.put(Double.TYPE, "f");
		map.put(BigDecimal.class, "b");
		map.put(java.sql.Date.class, "t");
		map.put(java.util.Date.class, "p");
	}

	private Xsql xsql;

	private Map<String, TableInfo> tables = new ConcurrentHashMap<String, TableInfo>();
	private Map<Class<?>, ClassInfo> classes = new ConcurrentHashMap<Class<?>, ClassInfo>();

	public void setEsql(Xsql xsql) {
		this.xsql = xsql;
	}

	/** 查询对象 */
	public <T> T query(Class<T> type, String table, String include, String exclude, String where, Object... params) throws Exception {
		String sql = prepareSqlForSelect(table, include, exclude, null, null, where);
		return xsql.query(type, sql, params);
	}

	/** 列出对象 */
	public <T> List<T> list(Class<T> type, String table, String include, String exclude, String order, Integer limit, String where,
			Object... params) throws Exception {
		String sql = prepareSqlForSelect(table, include, exclude, order, limit, where);
		return xsql.list(type, sql, params);
	}

	// /** 分页列对象 */
	// public <T> List<T> page(Class<T> type, int offset, int limit, String
	// table, String include, String exclude, String order, String where,
	// Object... parameters) throws Exception {
	// String sql = prepareSqlForSelect(table, include, exclude, order, where);
	// return xsql.page(type, offset, limit, sql, parameters);
	// }

	/** 构造select子句的列名列表 */
	public String columns(String table, String alias, String field, String include, String exclude) throws Exception {
		TableInfo ti = getTableInfo(table);

		StringBuilder sb = new StringBuilder();
		for (String c : filterColumns(ti, include, exclude)) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(alias).append('.').append(c);
			sb.append(' ');
			sb.append('"').append(field).append('.').append(c).append('"');
		}
		return sb.toString();
	}

	/** 插入对象 */
	public UpdateResult insert(Object bean, String table, String include, String exclude) throws Exception {
		TableInfo ti = getTableInfo(table);
		ClassInfo ci = getClassInfo(bean);

		Map<String, Field> fields = ci.getFields();

		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();

		String d = "";
		for (String column : filterColumns(ti, include, exclude)) {
			String name = XsqlUtils.sqlToJava(column);
			Field field = fields.get(name);
			if (field == null) throw new Exception("字段不存在: " + bean.getClass() + ": " + name);

			Object value = field.get(bean);
			if (value != null) {
				columns.append(d);
				columns.append(column);
				values.append(d);
				values.append("$").append(getFieldType(field.getType())).append("{").append(column).append("}");
				d = ", ";
			}
		}

		String sql = String.format("insert into %s(%s) values (%s)", table, columns.toString(), values.toString());

		return xsql.update(sql, bean);
	}

	/** 更新对象 */
	public UpdateResult update(Object bean, String table, String include, String exclude, String where) throws Exception {
		TableInfo ti = getTableInfo(table);
		ClassInfo ci = getClassInfo(bean);

		Map<String, Field> fields = ci.getFields();

		StringBuilder sql = new StringBuilder();
		sql.append("update ");
		sql.append(table);
		sql.append(" set ");

		String d = "";
		for (String column : filterColumns(ti, include, exclude)) {
			String name = XsqlUtils.sqlToJava(column);
			Field field = fields.get(name);
			if (field == null) throw new Exception("字段不存在: " + bean.getClass() + ":" + name);

			sql.append(d);
			sql.append(column).append(" = ");
			sql.append("$").append(getFieldType(field.getType())).append("{").append(column).append("}");
			d = ", ";
		}

		if (where != null) {
			sql.append(" where ");
			sql.append(where);
		}

		return xsql.update(sql.toString(), bean);
	}

	/** 删除对象 */
	public UpdateResult delete(String table, String where, Object... parameters) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ");
		sql.append(table);
		if (where != null) {
			sql.append(" where ");
			sql.append(where);
		}

		return xsql.update(sql.toString(), parameters);
	}

	private String getFieldType(Class<?> type) {
		String arg = map.get(type);
		if (arg == null) {
			if (Enum.class.isAssignableFrom(type)) {
				// 使用String类型
				return map.get(String.class);
			}
		}
		return arg;
	}

	// ==================================

	/** 为查询准备SQL语句 */
	protected abstract String prepareSqlForSelect(String table, String include, String exclude, String order, Integer limit, String where)
			throws Exception;

	/** 根据include, exclude过滤表列 */
	protected final static List<String> filterColumns(TableInfo ti, String include, String exclude) {
		if (include != null) include = include.trim();
		if (exclude != null) exclude = exclude.trim();

		if (include != null && !include.equals("")) {
			return Arrays.asList(include.split("[ \t]*,[ \t]*"));
		}

		if (exclude != null && !exclude.equals("")) {
			String[] ss = exclude.split("[ \t]*,[ \t]*");

			List<String> list = new ArrayList<String>();
			outter: for (String s1 : ti.getColumns()) {
				for (String s2 : ss) {
					if (s1.equals(s2)) continue outter;
				}

				list.add(s1);
			}

			return list;
		}

		return ti.getColumns();
	}

	/** 获取表格信息 */
	protected final synchronized TableInfo getTableInfo(String table) throws Exception {
		TableInfo ti = tables.get(table);
		if (ti == null) {
			ti = TableInfo.create(xsql, table);
			tables.put(table, ti);
		}
		return ti;
	}

	/** 获取类信息 */
	protected final synchronized ClassInfo getClassInfo(Object bean) throws Exception {
		Class<?> type = bean.getClass();
		ClassInfo ci = classes.get(type);
		if (ci == null) {
			ci = ClassInfo.create(type);
			classes.put(type, ci);
		}
		return ci;
	}
}
