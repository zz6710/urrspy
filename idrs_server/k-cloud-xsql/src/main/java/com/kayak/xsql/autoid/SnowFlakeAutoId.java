package com.kayak.xsql.autoid;

import com.kayak.core.util.Tools;

public class SnowFlakeAutoId implements AutoId {

	@Override
	public String getAutoId(String table, String pname) throws Exception {
		return Tools.obj2Str(SnowIdUtils.uniqueLong());
	}

	@Override
	public void prepare(String table, String pname) throws Exception {
	}

}
