package com.kayak.pms.opFlow.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.opFlow.dao.OpBusiInfoDao;
import com.kayak.pms.opFlow.model.OpBusiInfo;
import com.kayak.xsql.autoid.DefaultAutoId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@APIDefine(desc = "功能信息", model = OpBusiInfo.class)
public class OpBusiInfoService {
    @Autowired
    private OpBusiInfoDao opBusiInfoDao;

    @API(desc = "查询功能", auth = APIAuth.NO)
    public SqlResult<OpBusiInfo> find(SqlParam<OpBusiInfo> params) throws Exception {
        SqlResult<OpBusiInfo> result = opBusiInfoDao.find(params);
        result.getRows().forEach(item -> item.setChildren(new ArrayList<>()));
        return result;
    }

   @API(desc = "查询功能下拉框", auth = APIAuth.NO)
    public SqlResult<OpBusiInfo> findAll(SqlParam<OpBusiInfo> params) throws Exception {
        return opBusiInfoDao.findAll(params);
    }

    @API(desc = "添加功能", auth = APIAuth.NO)
    public String add(SqlParam<OpBusiInfo> params) throws Exception {
        // 校验功能名称重复
        boolean exist = opBusiInfoDao.existByName(params, false);
        if (exist) {
            return RequestSupport.updateReturnJson(false, "功能名称【" + params.getModel().getBusiName() + "】已存在", null).toString();
        }
        // 生成序列id
        DefaultAutoId defaultAutoId = new DefaultAutoId();
        String busiId = defaultAutoId.newId("op_busi_info", 8);
        params.getModel().setBusiId(busiId);
        // 插入
        opBusiInfoDao.add(params);
        return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
    }

    @API(desc = "更新功能", auth = APIAuth.NO)
    public String update(SqlParam<OpBusiInfo> params) throws Exception {
        // 校验功能名称重复
        boolean exist = opBusiInfoDao.existByName(params, true);
        if (exist) {
            return RequestSupport.updateReturnJson(false, "功能名称【" + params.getModel().getBusiName() + "】已存在", null).toString();
        }
        opBusiInfoDao.updateOpBusiInfo(params);
        return RequestSupport.updateReturnJson(true, "更新成功", null).toString();
    }

    @API(desc = "删除功能", auth = APIAuth.NO)
    public String delete(SqlParam<OpBusiInfo> params) throws Exception {
        opBusiInfoDao.delete(params);
        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }
}
