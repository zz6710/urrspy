package com.kayak.dps.check.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.check.dao.BaseExSeatDao;
import com.kayak.dps.check.model.BaseExSeatModel;
import com.kayak.dps.check.model.BaseExSeatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "配置服务", model = BaseExSeatModel.class)
public class BaseExSeatService {


    @Autowired
    private BaseExSeatDao baseExSeatDao;

    @API(desc = "查询配置信息", auth = APIAuth.YES)
    public SqlResult<BaseExSeatModel> findBaseExSeats(SqlParam<BaseExSeatModel> params) throws Exception {
        return baseExSeatDao.findBaseExSeats(params);
    }

    @API(desc = "删除配置信息", auth = APIAuth.YES)
    public  String delBaseExSeat(SqlParam<BaseExSeatModel> params) throws Exception {
        baseExSeatDao.delBaseExSeat(params);
        return  RequestSupport.updateReturnJson(true, "删除成功", null).toString();

    }

    @API(desc = "修改配置信息", auth = APIAuth.YES)
    public  int upBaseExSeat(SqlParam<BaseExSeatModel> params) throws Exception {
        return   baseExSeatDao.upBaseExSeat(params);
    }

    @API(desc = "新增配置信息", auth = APIAuth.YES)
    public  int addBaseExSeat(SqlParam<BaseExSeatModel> params) throws Exception {
        return   baseExSeatDao.addBaseExSeat(params);
    }

}
