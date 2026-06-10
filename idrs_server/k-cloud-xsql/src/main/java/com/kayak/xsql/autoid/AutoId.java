package com.kayak.xsql.autoid;

public interface AutoId {
	public String getAutoId(String table, String pname) throws Exception;

	public void prepare(String table, String pname) throws Exception;
}
