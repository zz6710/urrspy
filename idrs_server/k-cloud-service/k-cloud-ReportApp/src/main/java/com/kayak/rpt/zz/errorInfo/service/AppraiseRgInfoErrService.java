package com.kayak.rpt.zz.errorInfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.AppraiseRgInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.AppraiseRgInfoErr;

@Service
@APIDefine(desc = "估值信息错误服务", model = AppraiseRgInfoErr.class)
public class AppraiseRgInfoErrService {

	@Autowired
	private AppraiseRgInfoErrDao appraiseRgInfoErrDao;

	@API(desc = "查询估值信息错误信息", auth = APIAuth.YES)
	public SqlResult<AppraiseRgInfoErr> findAppraiseRgInfoErrs(SqlParam<AppraiseRgInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return appraiseRgInfoErrDao.findAppraiseRgInfoErrs(params);
	}

	@API(desc = "添加估值信息错误", params = "bank_code_desc,asset_code_desc,valuation_date_desc,unit_debt_net_desc,unit_debt_full_desc,details_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int addAppraiseRgInfoErr(SqlParam<AppraiseRgInfoErr> params) throws Exception {
		return appraiseRgInfoErrDao.addAppraiseRgInfoErr(params).getEffect();
	}
	
	@API(desc = "修改估值信息错误", params = "bank_code_desc,asset_code_desc,valuation_date_desc,unit_debt_net_desc,unit_debt_full_desc,details_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int updateAppraiseRgInfoErr(SqlParam<AppraiseRgInfoErr> params) throws Exception {
		return appraiseRgInfoErrDao.updateAppraiseRgInfoErr(params).getEffect();
	}
	
	@API(desc = "删除估值信息错误", params = "bank_code_desc,asset_code_desc,valuation_date_desc,unit_debt_net_desc,unit_debt_full_desc,details_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int deleteAppraiseRgInfoErr(SqlParam<AppraiseRgInfoErr> params) throws Exception {
		return appraiseRgInfoErrDao.deleteAppraiseRgInfoErr(params).getEffect();
	}

}
