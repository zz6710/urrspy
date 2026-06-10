package com.kayak.pms.opFlow.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.opFlow.dao.OpSqlConfigDao;
import com.kayak.pms.opFlow.model.OpSqlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "sql配置服务", model = OpSqlConfig.class)
public class OpSqlConfigService {
    @Autowired
    private OpSqlConfigDao opSqlConfigDao;

    @API(desc = "保存sql配置", auth = APIAuth.NO)
    public String save(SqlParam<OpSqlConfig> params) throws Exception {
        // 插入
        opSqlConfigDao.save(params);
        return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
    }

    @API(desc = "查询功能表单", auth = APIAuth.NO)
    public SqlResult<OpSqlConfig> find(SqlParam<OpSqlConfig> params) throws Exception {
        return opSqlConfigDao.find(params);
    }
}
