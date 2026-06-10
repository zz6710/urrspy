package com.kayak.xsql.result.column;

import java.sql.ResultSet;

public class ColumnReaderNumeric implements ColumnReader {
	@Override
	public Number read(ResultSet rs, int column) throws Exception {
		return rs.getBigDecimal(column);
	}
}
