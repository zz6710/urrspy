package com.kayak.method.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.method.model.Method;
import org.springframework.stereotype.Repository;


//异步加载的例子
@Repository
public class MethodDao extends ComnDao {

    public SqlResult<Method> findMethodList(SqlParam<Method> params) throws Exception {
        return super.findRows("select * from sys_server_method ", params);
    }

}
