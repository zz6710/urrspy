package com.kayak.xsql.result.column;

import java.sql.ResultSet;

public class ColumnReaderBoolean implements ColumnReader {
	@Override
	public Boolean read(ResultSet rs, int column) throws Exception {
		boolean b = rs.getBoolean(column);
		if (rs.wasNull())
			return null;

		return b;
	}
}
