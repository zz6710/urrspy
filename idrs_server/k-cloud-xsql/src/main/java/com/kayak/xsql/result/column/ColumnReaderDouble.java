package com.kayak.xsql.result.column;

import java.sql.ResultSet;

public class ColumnReaderDouble implements ColumnReader {
	@Override
	public Double read(ResultSet rs, int column) throws Exception {
		double d = rs.getDouble(column);
		if (rs.wasNull())
			return null;

		return d;
	}
}
