package com.kayak.rpt.zz.errorInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.TerminationRgInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.TerminationRgInfoErr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "产品终止登记错误信息服务", model = TerminationRgInfoErr.class)
public class TerminationRgInfoErrService {

	@Autowired
	private TerminationRgInfoErrDao terminationRgInfoErrDao;

	@API(desc = "查询产品终止登记错误信息信息", auth = APIAuth.YES)
	public SqlResult<TerminationRgInfoErr> findTerminationRgInfos(SqlParam<TerminationRgInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return terminationRgInfoErrDao.findTerminationRgInfos(params);
	}

	@API(desc = "添加产品终止登记错误信息", params = "prod_code_desc,bank_code_desc,actual_prod_ter_date_desc,realized_bank_income_desc,interest_payment_desc,payment_desc,delivered_vol_desc,in_custodian_fee_desc,in_manage_fee_desc,in_sales_commision_desc,in_other_prod_fee_desc,other_custodian_fee_desc,other_manage_fee_desc,other_sales_comm_desc,consult_fee_desc,other_prod_fee_desc,annual_return_client_desc,annual_return_prod_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int addTerminationRgInfo(SqlParam<TerminationRgInfoErr> params) throws Exception {
		return terminationRgInfoErrDao.addTerminationRgInfo(params).getEffect();
	}
	
	@API(desc = "修改产品终止登记错误信息", params = "prod_code_desc,bank_code_desc,actual_prod_ter_date_desc,realized_bank_income_desc,interest_payment_desc,payment_desc,delivered_vol_desc,in_custodian_fee_desc,in_manage_fee_desc,in_sales_commision_desc,in_other_prod_fee_desc,other_custodian_fee_desc,other_manage_fee_desc,other_sales_comm_desc,consult_fee_desc,other_prod_fee_desc,annual_return_client_desc,annual_return_prod_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int updateTerminationRgInfo(SqlParam<TerminationRgInfoErr> params) throws Exception {
		return terminationRgInfoErrDao.updateTerminationRgInfo(params).getEffect();
	}
	
	@API(desc = "删除产品终止登记错误信息", params = "prod_code_desc,bank_code_desc,actual_prod_ter_date_desc,realized_bank_income_desc,interest_payment_desc,payment_desc,delivered_vol_desc,in_custodian_fee_desc,in_manage_fee_desc,in_sales_commision_desc,in_other_prod_fee_desc,other_custodian_fee_desc,other_manage_fee_desc,other_sales_comm_desc,consult_fee_desc,other_prod_fee_desc,annual_return_client_desc,annual_return_prod_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int deleteTerminationRgInfo(SqlParam<TerminationRgInfoErr> params) throws Exception {
		return terminationRgInfoErrDao.deleteTerminationRgInfo(params).getEffect();
	}

}
