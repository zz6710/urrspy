package com.kayak.core.sql;

import com.kayak.core.exception.SqlException;
import com.kayak.core.util.Tools;

/**
 * 多数据类型SQL兼容语句基础类
 *
 * @author liuyg
 *
 */
public class Sql {

	private String defaultSql;
	private String mysqlSql;

	private String oracleSql;

	private String db2Sql;

	public String getSql(String dbType) throws Exception {
		String sql = null;
		if (dbType.equals("mysql")) {
			sql = mysqlSql;
		} else if (dbType.equals("oracle")) {
			sql = oracleSql;
		} else if (dbType.equals("db2")) {
			sql = db2Sql;
		}

		if (Tools.strIsEmpty(sql)) {
			if (Tools.strIsEmpty(sql)) {
				sql = defaultSql;
			} else {
				throw new SqlException("不支持的数据库类型");
			}
		}
		return sql;
	}

	public Sql defaultSql(String sql) {
		defaultSql = sql;
		return this;
	}

	public Sql mysqlSql(String sql) {
		mysqlSql = sql;
		return this;
	}

	public Sql oracleSql(String sql) {
		oracleSql = sql;
		return this;
	}

	public Sql db2Sql(String sql) {
		db2Sql = sql;
		return this;
	}

	public static Sql build() {
		return new Sql();
	}

}
