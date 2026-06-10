package com.kayak.xsql.result.column;

import java.sql.ResultSet;

public class ColumnReaderVarchar implements ColumnReader {
	@Override
	public String read(ResultSet rs, int column) throws Exception {
		return rs.getString(column) == null ? null : rs.getString(column);
	}
}
