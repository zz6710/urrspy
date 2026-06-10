package com.kayak.pms.prod.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.prod.dao.DistrictDao;
import com.kayak.pms.prod.model.District;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "区域信息", model = District.class)
public class DistrictService {
    @Autowired
    private DistrictDao DistrictDao;

    @API(desc = "查询所有省", auth = APIAuth.YES)
    public SqlResult<District> findAllProvince(SqlParam<District> params) throws Exception {
        return DistrictDao.findAllProvince(params);
    }

    @API(desc = "通过pid查找城市", auth = APIAuth.YES)
    public SqlResult<District> findCityByPId(SqlParam<District> params) throws Exception {
        return DistrictDao.findCityByPId(params);
    }

    @API(desc = "通过id查找区域", auth = APIAuth.YES)
    public SqlResult<District> findById(SqlParam<District> params) throws Exception {
        return DistrictDao.findById(params);
    }

}
