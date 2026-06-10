package com.kayak.xsql.dao;

import javax.sql.DataSource;

public interface DataSourceLoader {
	public DataSource load(String name, String driver, String prefix) throws Exception;
}
