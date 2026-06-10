package com.kayak.xsql.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kayak.xsql.Xsql;
import com.kayak.xsql.XsqlUtils;

/**
 * 数据库表的字段信息
 * 
 * 如果有id字段，将其排在第一个位置，如果有parent_id字段，排在第二个位置
 */
class TableInfo {
	private List<String> columns;

	public static TableInfo create(Xsql xsql, String table) throws Exception {
		String sql = xsql.getTableInfoSql(table);

		Connection db = xsql.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = db.prepareStatement(sql);
			rs = ps.executeQuery();

			// 如果有id字段，将其排在第一个位置，如果有parent_id字段，排在第二个位置
			boolean id = false; // 是否包含id字段
			boolean parentId = false; // 是否包含parent_id字段

			ResultSetMetaData md = rs.getMetaData();
			List<String> columns = new ArrayList<String>();
			Map<String, Integer> types = new HashMap<String, Integer>();
			Map<String, Boolean> nullables = new HashMap<String, Boolean>();
			for (int i = 1; i <= md.getColumnCount(); i++) {
				String column = md.getColumnName(i).toLowerCase();
				types.put(column, md.getColumnType(i));

				boolean nullable = ResultSetMetaData.columnNullable == md.isNullable(i);
				nullables.put(column, nullable);

				if (column.equals("id")) {
					id = true;
					continue;
				}
				if (column.equals("parent_id")) {
					parentId = true;
					continue;
				}

				columns.add(column);
			}

			Collections.sort(columns);

			if (parentId)
				columns.add(0, "parent_id");
			if (id)
				columns.add(0, "id");

			TableInfo ti = new TableInfo();
			ti.setColumns(columns);

			return ti;
		} finally {
			XsqlUtils.close(rs);
			XsqlUtils.close(ps);
			xsql.releaseConnection();
		}
	}

	public List<String> getColumns() {
		return columns;
	}

	private void setColumns(List<String> columns) {
		this.columns = columns;
	}
}
