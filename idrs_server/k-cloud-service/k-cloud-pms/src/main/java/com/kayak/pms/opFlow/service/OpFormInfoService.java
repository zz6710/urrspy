package com.kayak.pms.opFlow.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.opFlow.dao.OpFormInfoDao;
import com.kayak.pms.opFlow.model.OpFormInfo;
import com.kayak.xsql.autoid.DefaultAutoId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "功能表单业务", model = OpFormInfo.class)
public class OpFormInfoService {
    @Autowired
    private OpFormInfoDao opFormInfoDao;

    @API(desc = "查询表单", auth = APIAuth.NO)
    public SqlResult<OpFormInfo> find(SqlParam<OpFormInfo> params) throws Exception {
        params.setMakeSql(true);
        return opFormInfoDao.find(params);
    }

    @API(desc = "查询表单下拉框", auth = APIAuth.NO)
    public SqlResult<OpFormInfo> findAsDict(SqlParam<OpFormInfo> params) throws Exception {
        return opFormInfoDao.find(params);
    }

    @API(desc = "添加表单", auth = APIAuth.NO)
    public String add(SqlParam<OpFormInfo> params) throws Exception {
        // 校验表单名称重复
        boolean exist = opFormInfoDao.existByName(params, false);
        if (exist) {
            return RequestSupport.updateReturnJson(false, "表单名称【" + params.getModel().getFormName() + "】已存在", null).toString();
        }
        // 生成序列id
        DefaultAutoId defaultAutoId = new DefaultAutoId();
        String formId = defaultAutoId.newId("op_form_info", 8);
        params.getModel().setFormId(formId);
        // 插入
        opFormInfoDao.add(params);
        return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
    }

    @API(desc = "更新表单", auth = APIAuth.NO)
    public String update(SqlParam<OpFormInfo> params) throws Exception {
        // 校验表单名称重复
        boolean exist = opFormInfoDao.existByName(params, true);
        if (exist) {
            return RequestSupport.updateReturnJson(false, "表单名称【" + params.getModel().getFormName() + "】已存在", null).toString();
        }
        opFormInfoDao.updateOpFormInfo(params);
        return RequestSupport.updateReturnJson(true, "更新成功", null).toString();
    }

    @API(desc = "删除表单", auth = APIAuth.NO)
    public String delete(SqlParam<OpFormInfo> params) throws Exception {
        opFormInfoDao.delete(params);
        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }

    @API(desc = "查询功能所有表单参数", auth = APIAuth.NO)
    public SqlResult<OpFormInfo> findOpFormParam(SqlParam<OpFormInfo> params) throws Exception {
        return opFormInfoDao.findOpFormParam(params);
    }
}
