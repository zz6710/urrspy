package com.kayak.system.service;

import com.kayak.aspect.annotations.APIAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.dao.SystemOperationLogDao;
import com.kayak.system.model.SystemOperationLog;

import lombok.RequiredArgsConstructor;

@Service
@APIDefine(desc = "系统操作日志服务", model = SystemOperationLog.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SystemOperationLogService {

    private final SystemOperationLogDao systemOperationLogDao;


    @API(desc = "查询系统操作日志列表",auth = APIAuth.YES)
    public SqlResult<SystemOperationLog> find1(SqlParam<SystemOperationLog> params) throws Exception {
        return find(params);
    }

    @API(desc = "查询系统操作日志列表",auth = APIAuth.NO)
    public SqlResult<SystemOperationLog> find(SqlParam<SystemOperationLog> params) throws Exception {
        params.setMakeSql(true);
        return systemOperationLogDao.find(params);
    }
}
