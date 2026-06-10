package com.kayak.model.service;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlRow;
import com.kayak.model.dao.ModelDao;
import com.kayak.model.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "对象映射服务", model = Model.class)
public class ModelService {

    @Autowired
    private ModelDao modelDao;

    @API(desc = "映射对象查询", operation = APIOperation.SELECT, auth = APIAuth.YES)
    public SqlRow findModel(SqlParam<Model> params) throws Exception {
        params.setMakeSql(true);
        return modelDao.findMode(params);
    }


}
