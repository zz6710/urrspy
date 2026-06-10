package com.kayak.rpt.zz.historyInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.historyInfo.model.ProdTransRegistInfoh;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class ProdTransRegistInfohDao extends ComnDao {

	public SqlResult<ProdTransRegistInfoh> findProdTransRegistInfohs(SqlParam<ProdTransRegistInfoh> params) throws Exception {
		String sql = "SELECT BANK_CODE, PROD_CODE, TRANS_CODE, ASSET_CODE, CUR, AMT, CONVERT_RMB, QUANTITY, METHOD_ASSET_MEASURE, CASH_TYPE, DETAIL_CASH_TYPE, TRADE_DATE, TRADE_COUNTER, COUNTER_TYPE, UNIT_PRICE_FULL, UNIT_PRICE_NET, RATE_ANNUAL_RETURN, TRANS_IDENT_CODE, DETAILS, register_serno, IMP_DATE, REGISTER_DATE, REGISTER_STATUS, create_date, theory_report_start_date, theory_report_end_date, related_party_trans, trans_origin_time, trans_approve_id, trans_approve_name, trader_id, trader_name, TRX_TM FROM app_prod_trans_regist_info_h where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(register_Date) >= DATE($S{startDate}) and DATE(register_Date) <= DATE($S{endDate})";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addProdTransRegistInfoh(SqlParam<ProdTransRegistInfoh> params) throws Exception {
		return super.update("INSERT INTO app_prod_trans_regist_info_h(bank_code,prod_code,trans_code,asset_code,cur,amt,convert_rmb,quantity,method_asset_measure,cash_type,detail_cash_type,trade_date,trade_counter,counter_type,unit_price_full,unit_price_net,rate_annual_return,trans_ident_code,details,register_serno,imp_date,register_date,register_status) VALUES($S{bankCode},$S{prodCode},$S{transCode},$S{assetCode},$S{cur},$S{amt},$S{convertRmb},$S{quantity},$S{methodAssetMeasure},$S{cashType},$S{detailCashType},$S{tradeDate},$S{tradeCounter},$S{counterType},$S{unitPriceFull},$S{unitPriceNet},$S{rateAnnualReturn},$S{transIdentCode},$S{details},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus})",
				params.getModel());
	}
	
	public UpdateResult updateProdTransRegistInfoh(SqlParam<ProdTransRegistInfoh> params) throws Exception {
		return super.update("UPDATE app_prod_trans_regist_info_h SET bank_code=$S{bankCode} ,prod_code=$S{prodCode} ,trans_code=$S{transCode} ,asset_code=$S{assetCode} ,cur=$S{cur} ,amt=$S{amt} ,convert_rmb=$S{convertRmb} ,quantity=$S{quantity} ,method_asset_measure=$S{methodAssetMeasure} ,cash_type=$S{cashType} ,detail_cash_type=$S{detailCashType} ,trade_date=$S{tradeDate} ,trade_counter=$S{tradeCounter} ,counter_type=$S{counterType} ,unit_price_full=$S{unitPriceFull} ,unit_price_net=$S{unitPriceNet} ,rate_annual_return=$S{rateAnnualReturn} ,trans_ident_code=$S{transIdentCode} ,details=$S{details} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteProdTransRegistInfoh(SqlParam<ProdTransRegistInfoh> params) throws Exception {
		return super.update("DELETE FROM app_prod_trans_regist_info_h WHERE ",
				params.getModel());
	}

}
