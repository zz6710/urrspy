package com.kayak.pms.opFlow.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.opFlow.dao.OpBusiFormDao;
import com.kayak.pms.opFlow.model.OpBusiForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "功能表单关系", model = OpBusiForm.class)
public class OpBusiFormService {
    @Autowired
    private OpBusiFormDao opBusiFormDao;

    @API(desc = "保存功能表单", auth = APIAuth.NO)
    public String save(SqlParam<OpBusiForm> params) throws Exception {
        // 插入
        opBusiFormDao.save(params);
        return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
    }

    @API(desc = "查询功能表单", auth = APIAuth.NO)
    public SqlResult<OpBusiForm> find(SqlParam<OpBusiForm> params) throws Exception {
        return opBusiFormDao.find(params);
    }
}
