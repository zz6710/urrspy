package com.kayak.core.sql;

import java.util.Map;

public interface SqlParam<T> {

	public int getStart();

	public void setStart(int start);

	public int getLimit();

	public void setLimit(int limit);

	public boolean isPage();

	public Class<T> getModelClass();

	public T getModel();

	public boolean isMakeSql();

	public void setMakeSql(boolean makeSql);

	public Map<String, Object> getParams() throws Exception;

	public Map<String, Object> getParamsDirect() throws Exception;

	public void setSqlNoLog(boolean makeSql);

	public boolean isSqlNoLog();

}
