package com.kayak.base.dao;

import com.kayak.base.dao.sql.SqlConfig;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.Map;

public interface XmlSqlOperate {

    public <T> SqlResult<T> query(SqlParam<T> param, String action) throws Exception;

    public <T> SqlResult<T> query(SqlParam<T> param, SqlConfig sqlConfig) throws Exception;

    public SqlResult<Map> query(Map<String, Object> param, SqlConfig sqlConfig) throws Exception;

    public <T> String update(SqlParam<T> param, String action) throws Exception;

    public String update(Map<String, Object> param, SqlConfig sqlConfig) throws Exception;

    public <T> String update(SqlParam<T> param, SqlConfig sqlConfig) throws Exception;
}
