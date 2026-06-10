package com.kayak.xsql.result.column;

import java.sql.ResultSet;

public class ColumnReaderInteger implements ColumnReader {
	@Override
	public Integer read(ResultSet rs, int column) throws Exception {
		int n = rs.getInt(column);
		if (rs.wasNull())
			return null;

		return n;
	}
}
