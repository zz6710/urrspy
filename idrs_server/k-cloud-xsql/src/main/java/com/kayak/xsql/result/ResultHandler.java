package com.kayak.xsql.result;

import java.sql.ResultSet;

/**
 * 返回 Map 的结果处理器
 */
public abstract class ResultHandler<T> {
	public abstract T build(ResultSet rs) throws Exception;
}
