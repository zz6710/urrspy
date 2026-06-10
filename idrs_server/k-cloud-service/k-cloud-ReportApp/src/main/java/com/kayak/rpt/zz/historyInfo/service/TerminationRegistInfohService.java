package com.kayak.rpt.zz.historyInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.historyInfo.dao.TerminationRegistInfohDao;
import com.kayak.rpt.zz.historyInfo.model.TerminationRegistInfoh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "产品终止登记历史信息服务", model = TerminationRegistInfoh.class)
public class TerminationRegistInfohService {

	@Autowired
	private TerminationRegistInfohDao terminationRegistInfohDao;

	@API(desc = "查询产品终止登记历史信息信息", auth = APIAuth.YES)
	public SqlResult<TerminationRegistInfoh> findTerminationRegistInfos(SqlParam<TerminationRegistInfoh> params) throws Exception {
		params.setMakeSql(true);
		return terminationRegistInfohDao.findTerminationRegistInfohs(params);
	}

	@API(desc = "添加产品终止登记历史信息", params = "prod_code,bank_code,actual_prod_ter_date,interest_payment,register_serno,imp_date,register_date,register_status,realized_bank_income,payment,delivered_vol,in_custodian_fee,in_manage_fee,in_sales_commision,in_other_prod_fee,other_custodian_fee,other_manage_fee,other_sales_comm,consult_fee,other_prod_fee,annual_return_client,annual_return_prod", auth = APIAuth.NO)
	public int addTerminationRegistInfo(SqlParam<TerminationRegistInfoh> params) throws Exception {
		return terminationRegistInfohDao.addTerminationRegistInfoh(params).getEffect();
	}
	
	@API(desc = "修改产品终止登记历史信息", params = "prod_code,bank_code,actual_prod_ter_date,interest_payment,register_serno,imp_date,register_date,register_status,realized_bank_income,payment,delivered_vol,in_custodian_fee,in_manage_fee,in_sales_commision,in_other_prod_fee,other_custodian_fee,other_manage_fee,other_sales_comm,consult_fee,other_prod_fee,annual_return_client,annual_return_prod", auth = APIAuth.NO)
	public int updateTerminationRegistInfo(SqlParam<TerminationRegistInfoh> params) throws Exception {
		return terminationRegistInfohDao.updateTerminationRegistInfoh(params).getEffect();
	}
	
	@API(desc = "删除产品终止登记历史信息", params = "prod_code,bank_code,actual_prod_ter_date,interest_payment,register_serno,imp_date,register_date,register_status,realized_bank_income,payment,delivered_vol,in_custodian_fee,in_manage_fee,in_sales_commision,in_other_prod_fee,other_custodian_fee,other_manage_fee,other_sales_comm,consult_fee,other_prod_fee,annual_return_client,annual_return_prod", auth = APIAuth.NO)
	public int deleteTerminationRegistInfo(SqlParam<TerminationRegistInfoh> params) throws Exception {
		return terminationRegistInfohDao.deleteTerminationRegistInfoh(params).getEffect();
	}

}
