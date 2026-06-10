package com.kayak.rpt.zz.historyInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.historyInfo.dao.AppraiseRegistInfohDao;
import com.kayak.rpt.zz.historyInfo.model.AppraiseRegistInfoh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "估值信息历史服务", model = AppraiseRegistInfoh.class)
public class AppraiseRegistInfohService {

	@Autowired
	private AppraiseRegistInfohDao appraiseRegistInfohDao;

	@API(desc = "查询估值信息历史信息", auth = APIAuth.YES)
	public SqlResult<AppraiseRegistInfoh> findAppraiseRegistInfohs(SqlParam<AppraiseRegistInfoh> params) throws Exception {
		params.setMakeSql(true);
		return appraiseRegistInfohDao.findAppraiseRegistInfohs(params);
	}

	@API(desc = "添加估值信息历史", params = "bank_code,asset_code,valuation_date,unit_debt_net,unit_debt_full,details,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int addAppraiseRegistInfoh(SqlParam<AppraiseRegistInfoh> params) throws Exception {
		return appraiseRegistInfohDao.addAppraiseRegistInfoh(params).getEffect();
	}
	
	@API(desc = "修改估值信息历史", params = "bank_code,asset_code,valuation_date,unit_debt_net,unit_debt_full,details,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int updateAppraiseRegistInfoh(SqlParam<AppraiseRegistInfoh> params) throws Exception {
		return appraiseRegistInfohDao.updateAppraiseRegistInfoh(params).getEffect();
	}
	
	@API(desc = "删除估值信息历史", params = "bank_code,asset_code,valuation_date,unit_debt_net,unit_debt_full,details,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int deleteAppraiseRegistInfoh(SqlParam<AppraiseRegistInfoh> params) throws Exception {
		return appraiseRegistInfohDao.deleteAppraiseRegistInfoh(params).getEffect();
	}

}
