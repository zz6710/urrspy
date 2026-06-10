package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.dao.AssNetValSPVInfoModelDao;
import com.kayak.dps.app.model.AssNetValSPVInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "资管补录", model = AssNetValSPVInfoModel.class)
public class AssNetValSPVInfoService {

    @Autowired
    private AssNetValSPVInfoModelDao assNetValSPVInfoModelDao;

        @API(desc = "资产管理补录信息", auth = APIAuth.NO)
    public String addAssNetValSPVInfoModel(SqlParam<AssNetValSPVInfoModel> params) throws Exception {
        try {
           // assNetValSPVInfoModelDao.deleteAssNetValSPVInfoModel(params);
            assNetValSPVInfoModelDao.addAssNetValSPVInfoModel(params);
            return RequestSupport.updateReturnJson(true,  "补录成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }
}
