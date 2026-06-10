package com.kayak.xsql.result.column;

import java.sql.ResultSet;
import java.util.Date;

public class ColumnReaderDate implements ColumnReader {
	@Override
	public Date read(ResultSet rs, int column) throws Exception {
		return rs.getDate(column);
	}
}
