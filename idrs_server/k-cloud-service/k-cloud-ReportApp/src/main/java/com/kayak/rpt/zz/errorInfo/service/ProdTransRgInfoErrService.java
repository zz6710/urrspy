package com.kayak.rpt.zz.errorInfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.ProdTransRgInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.ProdTransRgInfoErr;

@Service
@APIDefine(desc = "交易信息错误服务", model = ProdTransRgInfoErr.class)
public class ProdTransRgInfoErrService {

	@Autowired
	private ProdTransRgInfoErrDao prodTransRgInfoErrDao;

	@API(desc = "查询交易信息错误信息", auth = APIAuth.YES)
	public SqlResult<ProdTransRgInfoErr> findProdTransRgInfoErrs(SqlParam<ProdTransRgInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return prodTransRgInfoErrDao.findProdTransRgInfoErrs(params);
	}

	@API(desc = "添加交易信息错误", params = "bank_code_desc,prod_code_desc,trans_code_desc,asset_code_desc,cur_desc,amt_desc,convert_rmb_desc,quantity_desc,method_asset_measure_desc,cash_type_desc,detail_cash_type_desc,trade_date_desc,trade_counter_desc,counter_type_desc,unit_price_full_desc,unit_price_net_desc,rate_annual_return_desc,trans_ident_code_desc,details_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int addProdTransRgInfoErr(SqlParam<ProdTransRgInfoErr> params) throws Exception {
		return prodTransRgInfoErrDao.addProdTransRgInfoErr(params).getEffect();
	}
	
	@API(desc = "修改交易信息错误", params = "bank_code_desc,prod_code_desc,trans_code_desc,asset_code_desc,cur_desc,amt_desc,convert_rmb_desc,quantity_desc,method_asset_measure_desc,cash_type_desc,detail_cash_type_desc,trade_date_desc,trade_counter_desc,counter_type_desc,unit_price_full_desc,unit_price_net_desc,rate_annual_return_desc,trans_ident_code_desc,details_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int updateProdTransRgInfoErr(SqlParam<ProdTransRgInfoErr> params) throws Exception {
		return prodTransRgInfoErrDao.updateProdTransRgInfoErr(params).getEffect();
	}
	
	@API(desc = "删除交易信息错误", params = "bank_code_desc,prod_code_desc,trans_code_desc,asset_code_desc,cur_desc,amt_desc,convert_rmb_desc,quantity_desc,method_asset_measure_desc,cash_type_desc,detail_cash_type_desc,trade_date_desc,trade_counter_desc,counter_type_desc,unit_price_full_desc,unit_price_net_desc,rate_annual_return_desc,trans_ident_code_desc,details_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int deleteProdTransRgInfoErr(SqlParam<ProdTransRgInfoErr> params) throws Exception {
		return prodTransRgInfoErrDao.deleteProdTransRgInfoErr(params).getEffect();
	}

}
