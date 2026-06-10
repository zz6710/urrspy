package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.dao.AssT8OrgSheetModelDao;
import com.kayak.dps.app.model.AssT8OrgSheetModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "机构信息补录服务", model = AssT8OrgSheetModel.class)
public class AssT8OrgSheetService {

    @Autowired
    private AssT8OrgSheetModelDao assT8OrgSheetModelDao;

    @API(desc = "机构补录信息", auth = APIAuth.NO)
    public String addAssT8OrgSheetModel(SqlParam<AssT8OrgSheetModel> params) throws Exception {
        try {
            //assT8OrgSheetModelDao.deleteT8OrgSheetModel(params);
            assT8OrgSheetModelDao.addT8OrgSheetModel(params);
            return RequestSupport.updateReturnJson(true,  "补录成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }
}
