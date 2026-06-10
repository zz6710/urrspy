package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.dao.LinkedTransMappingDao;
import com.kayak.dps.app.model.DwdLinkedTransMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "关联交易映射表服务", model = DwdLinkedTransMapping.class)
public class LinkedTransMappingService extends BaseController {

    @Autowired
    private LinkedTransMappingDao linkedTransMappingDao;

    @API(desc = "查询关联交易映射信息", auth = APIAuth.YES)
    public SqlResult<DwdLinkedTransMapping> findLinkedTransMappings(SqlParam<DwdLinkedTransMapping> params) throws Exception {
        return linkedTransMappingDao.findLinkedTransMappings(params);
    }

    @API(desc = "新增关联交易映射信息", auth = APIAuth.NO)
    public String addLinkedTransMapping(SqlParam<DwdLinkedTransMapping> params) throws Exception{
        try {
            linkedTransMappingDao.addLinkedTransMapping(params);
            return RequestSupport.updateReturnJson(true,  "新增成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }

    @API(desc = "更新关联交易映射信息", auth = APIAuth.NO)
    public String updateLinkedTransMapping(SqlParam<DwdLinkedTransMapping> params) throws Exception{
        try {
            linkedTransMappingDao.updateLinkedTransMapping(params);
            return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }

    @API(desc = "删除关联交易映射信息", auth = APIAuth.NO)
    public String delLinkedTransMapping(SqlParam<DwdLinkedTransMapping> params) throws Exception{
        try {
            linkedTransMappingDao.delLinkedTransMapping(params);
            return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }
}
