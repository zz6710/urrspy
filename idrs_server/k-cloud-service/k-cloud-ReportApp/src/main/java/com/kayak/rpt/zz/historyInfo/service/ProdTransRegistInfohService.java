package com.kayak.rpt.zz.historyInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.historyInfo.dao.ProdTransRegistInfohDao;
import com.kayak.rpt.zz.historyInfo.model.ProdTransRegistInfoh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "交易信息历史服务", model = ProdTransRegistInfoh.class)
public class ProdTransRegistInfohService {

	@Autowired
	private ProdTransRegistInfohDao prodTransRegistInfohDao;

	@API(desc = "查询交易信息历史信息", auth = APIAuth.YES)
	public SqlResult<ProdTransRegistInfoh> findProdTransRegistInfos(SqlParam<ProdTransRegistInfoh> params) throws Exception {
		params.setMakeSql(true);
		return prodTransRegistInfohDao.findProdTransRegistInfohs(params);
	}

	@API(desc = "添加交易信息历史", params = "bank_code,prod_code,trans_code,asset_code,cur,amt,convert_rmb,quantity,method_asset_measure,cash_type,detail_cash_type,trade_date,trade_counter,counter_type,unit_price_full,unit_price_net,rate_annual_return,trans_ident_code,details,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int addProdTransRegistInfo(SqlParam<ProdTransRegistInfoh> params) throws Exception {
		return prodTransRegistInfohDao.addProdTransRegistInfoh(params).getEffect();
	}
	
	@API(desc = "修改交易信息历史", params = "bank_code,prod_code,trans_code,asset_code,cur,amt,convert_rmb,quantity,method_asset_measure,cash_type,detail_cash_type,trade_date,trade_counter,counter_type,unit_price_full,unit_price_net,rate_annual_return,trans_ident_code,details,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int updateProdTransRegistInfo(SqlParam<ProdTransRegistInfoh> params) throws Exception {
		return prodTransRegistInfohDao.updateProdTransRegistInfoh(params).getEffect();
	}
	
	@API(desc = "删除交易信息历史", params = "bank_code,prod_code,trans_code,asset_code,cur,amt,convert_rmb,quantity,method_asset_measure,cash_type,detail_cash_type,trade_date,trade_counter,counter_type,unit_price_full,unit_price_net,rate_annual_return,trans_ident_code,details,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int deleteProdTransRegistInfo(SqlParam<ProdTransRegistInfoh> params) throws Exception {
		return prodTransRegistInfohDao.deleteProdTransRegistInfoh(params).getEffect();
	}

}
