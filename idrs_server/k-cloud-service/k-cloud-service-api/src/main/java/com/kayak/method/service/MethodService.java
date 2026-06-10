package com.kayak.method.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.method.dao.MethodDao;
import com.kayak.method.model.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "获取服务", model = Method.class)
public class MethodService {

    @Autowired
    private MethodDao methodDao;

    @API(desc = "服务列表查询", operation = APIOperation.SELECT, auth = APIAuth.YES)
    public SqlResult<Method> findMethodList(SqlParam<Method> params) throws Exception {
        params.setMakeSql(true);
        return methodDao.findMethodList(params);
    }
}
