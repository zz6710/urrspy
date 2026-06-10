package com.kayak.base.dao.sql;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;

public interface SqlInterceptor<T> {

    default public void beforeExecute(SqlParam<T> sqlParam) throws Exception {};

    default public void afterExecute(SqlParam<T> sqlParam, SqlResult<T> sqlResult, UpdateResult updateResult ) throws Exception {};
}
