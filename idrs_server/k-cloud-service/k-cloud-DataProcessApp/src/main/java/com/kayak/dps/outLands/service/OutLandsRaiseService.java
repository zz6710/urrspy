package com.kayak.dps.outLands.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.app.model.T8AffiliatedPerson;
import com.kayak.dps.outLands.dao.OutLandsDao;
import com.kayak.dps.outLands.model.OutLandsCash;
import com.kayak.dps.outLands.model.OutLandsRaise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@APIDefine(desc = "OutLandsRaise服务", model = OutLandsRaise.class)
public class OutLandsRaiseService {

    @Autowired
    private OutLandsDao outLandsDao;

    @API(desc = "查询境外募集余额信息", auth = APIAuth.YES)
    public SqlResult<OutLandsRaise> findOutLandsRaise(SqlParam<OutLandsRaise> params) throws Exception {
        return outLandsDao.findOutLandsRaise(params);
    }

    @API(desc = "境外募集余额导入", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public void importOutLandsRaise(SqlParam<OutLandsRaise> outLandsRaise){
        //此方法只作用于权限控制
    }


}
