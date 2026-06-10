package com.kayak.pms.opFlow.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.opFlow.dao.OpFormParamRelationDao;
import com.kayak.pms.opFlow.model.OpFormParamRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "功能表单参数联动关系业务", model = OpFormParamRelation.class)
public class OpFormParamRelationService {
    @Autowired
    private OpFormParamRelationDao opFormParamRelationDao;

    @API(desc = "查询表单参数关系", auth = APIAuth.NO)
    public SqlResult<OpFormParamRelation> findOpFormParamRelations(SqlParam<OpFormParamRelation> params) throws Exception {
        return opFormParamRelationDao.findOpFormParamRelations(params);
    }

    @API(desc = "查询多个表单参数关系", auth = APIAuth.NO)
    public SqlResult<OpFormParamRelation> findRelationsByFormId(SqlParam<OpFormParamRelation> params) throws Exception {
        return opFormParamRelationDao.findRelationsByFormId(params);
    }
}
