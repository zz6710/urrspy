package com.kayak.xsql.helper;

import java.util.List;

public class XsqlHelperOracle extends XsqlHelper {

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
			if (limit != null) {
				sql.append(" and rownum <= ");
				sql.append(limit);
			}
		} else if (limit != null) {
			sql.append(" where rownum <= ");
			sql.append(limit);
		}

		if (order != null) {
			sql.append(" order by ");
			sql.append(order);
		}

		return sql.toString();
	}

}
