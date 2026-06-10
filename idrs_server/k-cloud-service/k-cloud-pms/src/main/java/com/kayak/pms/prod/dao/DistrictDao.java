package com.kayak.pms.prod.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.prod.model.District;
import org.springframework.stereotype.Repository;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/01/03 14:36
 */
@Repository
public class DistrictDao extends ComnDao {
    public SqlResult<District> findAllProvince(SqlParam<District> params) throws Exception {
        return super.findRows("select * from t8_district where pid = 1",params);
    }

    public SqlResult<District> findCityByPId(SqlParam<District> params) throws Exception {
        return super.findRows("select * from t8_district where pid = $S{pid}",params);
    }

    public SqlResult<District> findById(SqlParam<District> params) throws Exception {
        return super.findRows("select * from t8_district where id = $S{openAccountProvince}",params);
    }
}
