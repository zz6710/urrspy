package com.kayak.xsql.result.column;

import java.sql.Blob;
import java.sql.ResultSet;

public class ColumnReaderBlob implements ColumnReader {
	@Override
	public byte[] read(ResultSet rs, int column) throws Exception {
		Blob blob = rs.getBlob(column);
		if (blob == null)
			return null;

		return blob.getBytes(1, (int) blob.length());
	}
}
