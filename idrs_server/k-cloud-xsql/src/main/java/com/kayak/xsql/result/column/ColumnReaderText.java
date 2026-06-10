package com.kayak.xsql.result.column;

import java.sql.ResultSet;

public class ColumnReaderText implements ColumnReader {
	@Override
	public String read(ResultSet rs, int column) throws Exception {
		return rs.getString(column);
	}
}
