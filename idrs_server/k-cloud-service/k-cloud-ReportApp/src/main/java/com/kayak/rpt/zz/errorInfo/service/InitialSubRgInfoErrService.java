package com.kayak.rpt.zz.errorInfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.InitialSubRgInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.InitialSubRgInfoErr;

@Service
@APIDefine(desc = "产品募集总量错误信息服务", model = InitialSubRgInfoErr.class)
public class InitialSubRgInfoErrService {

	@Autowired
	private InitialSubRgInfoErrDao initialSubRgInfoErrDao;

	@API(desc = "查询产品募集总量错误信息信息", auth = APIAuth.YES)
	public SqlResult<InitialSubRgInfoErr> findInitialSubRgInfoErrs(SqlParam<InitialSubRgInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return initialSubRgInfoErrDao.findInitialSubRgInfoErrs(params);
	}

	@API(desc = "添加产品募集总量错误信息", params = "bank_code_desc,prod_code_desc,number_indiv_invest_desc,number_corpor_invest_desc,number_ucor_invest_desc,subscript_cur_desc,convert_rmb_desc,prod_sales_region_desc,other_distribut_agents_desc,subscript_amt_desc,subscript_amt_region_desc,actual_subscribed_amt_desc,subscribed_vol_desc,amt_other_db_agents_desc,details_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int addInitialSubRgInfoErr(SqlParam<InitialSubRgInfoErr> params) throws Exception {
		return initialSubRgInfoErrDao.addInitialSubRgInfoErr(params).getEffect();
	}
	
	@API(desc = "修改产品募集总量错误信息", params = "bank_code_desc,prod_code_desc,number_indiv_invest_desc,number_corpor_invest_desc,number_ucor_invest_desc,subscript_cur_desc,convert_rmb_desc,prod_sales_region_desc,other_distribut_agents_desc,subscript_amt_desc,subscript_amt_region_desc,actual_subscribed_amt_desc,subscribed_vol_desc,amt_other_db_agents_desc,details_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int updateInitialSubRgInfoErr(SqlParam<InitialSubRgInfoErr> params) throws Exception {
		return initialSubRgInfoErrDao.updateInitialSubRgInfoErr(params).getEffect();
	}
	
	@API(desc = "删除产品募集总量错误信息", params = "bank_code_desc,prod_code_desc,number_indiv_invest_desc,number_corpor_invest_desc,number_ucor_invest_desc,subscript_cur_desc,convert_rmb_desc,prod_sales_region_desc,other_distribut_agents_desc,subscript_amt_desc,subscript_amt_region_desc,actual_subscribed_amt_desc,subscribed_vol_desc,amt_other_db_agents_desc,details_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int deleteInitialSubRgInfoErr(SqlParam<InitialSubRgInfoErr> params) throws Exception {
		return initialSubRgInfoErrDao.deleteInitialSubRgInfoErr(params).getEffect();
	}

}
