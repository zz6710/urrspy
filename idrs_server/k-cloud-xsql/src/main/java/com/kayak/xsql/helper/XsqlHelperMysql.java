package com.kayak.xsql.helper;

import java.util.List;

public class XsqlHelperMysql extends XsqlHelper {

	@Override
	protected String prepareSqlForSelect(String table, String include, String exclude, String order, Integer limit, String where)
			throws Exception {
		TableInfo ti = getTableInfo(table);

		List<String> columns = filterColumns(ti, include, exclude);
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		String d = "";
		for (String column : columns) {
			sql.append(d);
			sql.append(column);
			d = ", ";
		}

		sql.append(" from ");
		sql.append(table);

		if (where != null) {
			sql.append(" where ");
			sql.append(where);
		}

		if (order != null) {
			sql.append(" order by ");
			sql.append(order);
		}

		if (limit != null) {
			sql.append(" limit ");
			sql.append(limit);
		}
		return sql.toString();
	}

}
