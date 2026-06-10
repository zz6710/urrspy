package com.kayak.rpt.zz.errorInfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.ProdIssRgInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.ProdIssRgInfoErr;

@Service
@APIDefine(desc = "产品发行登记错误信息服务", model = ProdIssRgInfoErr.class)
public class ProdIssRgInfoErrService {

	@Autowired
	private ProdIssRgInfoErrDao prodIssRgInfoErrDao;

	@API(desc = "查询产品发行登记错误信息信息", auth = APIAuth.YES)
	public SqlResult<ProdIssRgInfoErr> findProdIssRgInfoErrs(SqlParam<ProdIssRgInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return prodIssRgInfoErrDao.findProdIssRgInfoErrs(params);
	}

	@API(desc = "添加产品发行登记错误信息", params = "register_serno,imp_date,register_date,register_status,prod_code_desc,bank_code_desc,prod_ident_code_desc,subscription_start_date_desc,subscription_end_date_desc,prod_value_date_desc,prod_maturity_date_desc,management_method_desc,structured_prod_desc,details_per_rate_desc,opening_mode_desc,up_limit_per_rate_desc,low_limit_per_rate_desc,regular_open_period_desc,other_open_period_desc,disorder_open_period_desc,first_open_day_desc,holiday_open_type_desc,average_open_no_desc,busi_open_period_desc,details_busi_op_period_desc,custody_acct_no_desc,custody_acct_name_desc", auth = APIAuth.NO)
	public int addProdIssRgInfoErr(SqlParam<ProdIssRgInfoErr> params) throws Exception {
		return prodIssRgInfoErrDao.addProdIssRgInfoErr(params).getEffect();
	}
	
	@API(desc = "修改产品发行登记错误信息", params = "register_serno,imp_date,register_date,register_status,prod_code_desc,bank_code_desc,prod_ident_code_desc,subscription_start_date_desc,subscription_end_date_desc,prod_value_date_desc,prod_maturity_date_desc,management_method_desc,structured_prod_desc,details_per_rate_desc,opening_mode_desc,up_limit_per_rate_desc,low_limit_per_rate_desc,regular_open_period_desc,other_open_period_desc,disorder_open_period_desc,first_open_day_desc,holiday_open_type_desc,average_open_no_desc,busi_open_period_desc,details_busi_op_period_desc,custody_acct_no_desc,custody_acct_name_desc", auth = APIAuth.NO)
	public int updateProdIssRgInfoErr(SqlParam<ProdIssRgInfoErr> params) throws Exception {
		return prodIssRgInfoErrDao.updateProdIssRgInfoErr(params).getEffect();
	}
	
	@API(desc = "删除产品发行登记错误信息", params = "register_serno,imp_date,register_date,register_status,prod_code_desc,bank_code_desc,prod_ident_code_desc,subscription_start_date_desc,subscription_end_date_desc,prod_value_date_desc,prod_maturity_date_desc,management_method_desc,structured_prod_desc,details_per_rate_desc,opening_mode_desc,up_limit_per_rate_desc,low_limit_per_rate_desc,regular_open_period_desc,other_open_period_desc,disorder_open_period_desc,first_open_day_desc,holiday_open_type_desc,average_open_no_desc,busi_open_period_desc,details_busi_op_period_desc,custody_acct_no_desc,custody_acct_name_desc", auth = APIAuth.NO)
	public int deleteProdIssRgInfoErr(SqlParam<ProdIssRgInfoErr> params) throws Exception {
		return prodIssRgInfoErrDao.deleteProdIssRgInfoErr(params).getEffect();
	}

}
