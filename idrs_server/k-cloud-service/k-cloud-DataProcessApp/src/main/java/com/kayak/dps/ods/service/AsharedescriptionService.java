package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.model.AsharedescriptionModel;
import com.kayak.dps.app.model.NonStandInfoModel;
import com.kayak.dps.ods.dao.AsharedescriptionDao;
import com.kayak.dps.ods.dao.NonStandInfoModelDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@APIDefine(desc = "股票信息服务", model = AsharedescriptionModel.class)
public class AsharedescriptionService {

	@Autowired
	private AsharedescriptionDao asharedescriptionDao;

	private static final int ZERO = 0;

	@API(desc = "股票信息查询", auth = APIAuth.YES)
	public SqlResult<AsharedescriptionModel> findAsharedesriptionInfo(SqlParam<AsharedescriptionModel> params) throws Exception {
		params.setMakeSql(true);
		return asharedescriptionDao.findAsharedesriptionInfo(params);
	}


	@API(desc = "查询股票信息id与名称", auth = APIAuth.NO)
	public SqlResult<AsharedescriptionModel> findAshareInfoIdAndNm(SqlParam<AsharedescriptionModel> params) throws Exception {
		params.setMakeSql(false);
		return asharedescriptionDao.findAshareInfoIdAndNm(params);
	}

	@API(desc = "股票信息补录",  auth = APIAuth.YES)
	public String addAsharedescriptionInfo(SqlParam<AsharedescriptionModel> params) throws Exception {
		asharedescriptionDao.addAsharedescriptionInfo(params).getEffect();
		return RequestSupport.updateReturnJson(true,  "补录成功！", null).toString();
	}

}
