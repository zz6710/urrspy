package com.kayak.xsql.result.column;

import java.sql.Clob;
import java.sql.ResultSet;

public class ColumnReaderClob implements ColumnReader {
	@Override
	public String read(ResultSet rs, int column) throws Exception {
		Clob clob = rs.getClob(column);
		if (clob == null)
			return null;

		return clob.getSubString(1, (int) clob.length());
	}
}
