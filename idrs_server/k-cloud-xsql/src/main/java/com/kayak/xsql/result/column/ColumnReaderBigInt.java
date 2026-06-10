package com.kayak.xsql.result.column;

import java.sql.ResultSet;

public class ColumnReaderBigInt implements ColumnReader {
	@Override
	public Long read(ResultSet rs, int column) throws Exception {
		long n = rs.getLong(column);
		if (rs.wasNull())
			return null;

		return n;
	}
}
