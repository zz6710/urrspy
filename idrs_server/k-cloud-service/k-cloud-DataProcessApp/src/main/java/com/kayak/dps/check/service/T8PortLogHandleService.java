package com.kayak.dps.check.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.check.dao.T8portInfoDao;
import com.kayak.dps.check.model.PortLogInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@APIDefine(desc = "接口信息管理表服务", model = PortLogInfoModel.class)
public class T8PortLogHandleService {
    @Autowired
    private T8portInfoDao t8portInfoDao;

    @API(desc = "查询接口信息管理表信息", auth = APIAuth.YES)
    public SqlResult<PortLogInfoModel> findPortLogInformation(SqlParam<PortLogInfoModel> params) throws Exception {
        return t8portInfoDao.findPortLogInfo(params);
    }



}
