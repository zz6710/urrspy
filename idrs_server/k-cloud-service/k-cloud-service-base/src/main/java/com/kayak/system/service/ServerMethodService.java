package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.dao.ServerMethodDao;
import com.kayak.system.model.ServerMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "接口方法数据信息", model = ServerMethod.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ServerMethodService {

    private final ServerMethodDao serverMethodDao;

    @API(desc = "查询接口方法数据信息",auth = APIAuth.NO)
    public SqlResult<ServerMethod> find(SqlParam<ServerMethod> params) throws Exception {
        params.setMakeSql(true);
        return serverMethodDao.list(params);
    }
}
