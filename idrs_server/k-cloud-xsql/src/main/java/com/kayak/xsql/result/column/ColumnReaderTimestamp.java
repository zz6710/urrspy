package com.kayak.xsql.result.column;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ColumnReaderTimestamp implements ColumnReader {
	@Override
	public Date read(ResultSet rs, int column) throws Exception {
		Date d=rs.getTimestamp(column, Calendar.getInstance());
		if (d == null)
		{
			return null;
		}
		return d;
	}
}
