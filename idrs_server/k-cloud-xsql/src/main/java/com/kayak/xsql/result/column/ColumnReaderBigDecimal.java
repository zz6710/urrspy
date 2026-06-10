package com.kayak.xsql.result.column;

import java.math.BigDecimal;
import java.sql.ResultSet;

public class ColumnReaderBigDecimal implements ColumnReader {
	@Override
	public BigDecimal read(ResultSet rs, int column) throws Exception {
		BigDecimal n = rs.getBigDecimal(column);
		if (rs.wasNull())
			return null;

		return n;
	}
}
