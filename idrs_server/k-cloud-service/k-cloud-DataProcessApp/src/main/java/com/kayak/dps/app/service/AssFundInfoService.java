package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.dao.AssFundInfoModelDao;
import com.kayak.dps.app.model.AssFundInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "基金补录服务", model = AssFundInfoModel.class)
public class AssFundInfoService {

    @Autowired
    private AssFundInfoModelDao assFundInfoModelDao;

    @API(desc = "基金补录信息", auth = APIAuth.NO)
    public String addAssFundInfoModel(SqlParam<AssFundInfoModel> params) throws Exception {
        try {
            //assFundInfoModelDao.deleteAssFundInfoModel(params);
            assFundInfoModelDao.addAssFundInfoModel(params);
            return RequestSupport.updateReturnJson(true,  "补录成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }
}
