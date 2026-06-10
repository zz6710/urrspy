package com.kayak.dps.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.app.dao.AppBottomDealDao;
import com.kayak.dps.app.model.AppBottomDeal;

/**
 * 底层资产信息表服务
 * axin
 */
@Service
@APIDefine(desc = "底层资产信息表服务", model = AppBottomDeal.class)
public class AppBottomDealService {

	@Autowired
	private AppBottomDealDao appBottomDealDao;

	@API(desc = "查询底层资产信息表信息", auth = APIAuth.NO)
	public SqlResult<AppBottomDeal> findAppBottomDeals(SqlParam<AppBottomDeal> params) throws Exception {
		params.setMakeSql(false);
		return appBottomDealDao.findAppBottomDeals(params);
	}

	@API(desc = "添加底层资产信息表", auth = APIAuth.NO)
	public int addAppBottomDeal(SqlParam<AppBottomDeal> params) throws Exception {
		return appBottomDealDao.addAppBottomDeal(params).getEffect();
	}
	
	@API(desc = "修改底层资产信息表",  auth = APIAuth.NO)
	public int updateAppBottomDeal(SqlParam<AppBottomDeal> params) throws Exception {
		return appBottomDealDao.updateAppBottomDeal(params).getEffect();
	}
	
	@API(desc = "删除底层资产信息表", auth = APIAuth.NO)
	public int deleteAppBottomDeal(SqlParam<AppBottomDeal> params) throws Exception {
		return appBottomDealDao.deleteAppBottomDeal(params).getEffect();
	}

}
