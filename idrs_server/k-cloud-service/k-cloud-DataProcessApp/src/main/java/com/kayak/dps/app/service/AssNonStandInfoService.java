package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.dao.AssNetValSPVInfoModelDao;
import com.kayak.dps.app.dao.AssNonStandInfoModelDao;
import com.kayak.dps.app.model.AssNetValSPVInfoModel;
import com.kayak.dps.app.model.AssNonStandInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "非标补录", model = AssNonStandInfoModel.class)
public class AssNonStandInfoService {

    @Autowired
    private AssNonStandInfoModelDao assNonStandInfoModelDao;

    @API(desc = "非标债权补录信息", auth = APIAuth.NO)
    public String addAssNonStandInfoModel(SqlParam<AssNonStandInfoModel> params) throws Exception {
        try {
            //assNonStandInfoModelDao.deleteAssNonStandInfoModel(params);
            assNonStandInfoModelDao.addAssNonStandInfoModel(params);
            return RequestSupport.updateReturnJson(true,  "补录成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }
}
