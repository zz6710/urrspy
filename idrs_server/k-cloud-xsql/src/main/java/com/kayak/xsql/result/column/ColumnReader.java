package com.kayak.xsql.result.column;

import java.sql.ResultSet;

/**
 * 处理返回数据的一列
 * 
 * @author zuojie
 * 
 */
public interface ColumnReader {
	/** 读取结果集中指定列的数据 */
	public abstract Object read(ResultSet rs, int column) throws Exception;
}
