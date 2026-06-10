package com.kayak.code;

import java.util.HashMap;
import java.util.Map;

public class DbInfoFactory {

	private static Map<String, TableGeter> tableGeterMap = new HashMap<String, TableGeter>();
	private static Map<String, ColumnGeter> columnGeterMap = new HashMap<String, ColumnGeter>();

	static {
		tableGeterMap.put("mysql", new MysqlTableGeter());
		tableGeterMap.put("oracle", new OracleTableGeter());

		columnGeterMap.put("mysql", new MysqlColumnGeter());
		columnGeterMap.put("oracle", new OracleColumnGeter());
	}

	public static TableGeter getTableGeter(String dbType) {
		return tableGeterMap.get(dbType);
	}
	
	public static ColumnGeter getColumnGeter(String dbType) {
		return columnGeterMap.get(dbType);
	}

}
