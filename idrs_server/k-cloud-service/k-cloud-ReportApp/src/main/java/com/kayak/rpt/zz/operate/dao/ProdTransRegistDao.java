package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.ProdTransRegist;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ProdTransRegistDao extends ComnDao {

	public SqlResult<ProdTransRegist> findProdTransRegists(SqlParam<ProdTransRegist> params) throws Exception {
		String sql = "SELECT BANK_CODE, PROD_CODE, TRANS_CODE, ASSET_CODE, CUR, AMT, CONVERT_RMB, id, QUANTITY, METHOD_ASSET_MEASURE, CASH_TYPE, DETAIL_CASH_TYPE, TRADE_DATE, TRADE_COUNTER, COUNTER_TYPE, UNIT_PRICE_FULL, UNIT_PRICE_NET, RATE_ANNUAL_RETURN, TRANS_IDENT_CODE, DETAILS, REGISTER_SERNO, IMP_DATE, REGISTER_DATE, REGISTER_STATUS, SUMMIT_USER, CREATE_DATE, CREATE_TIME, OP_TYPE, related_party_trans, trans_origin_time, trans_approve_id, trans_approve_name, trader_id, trader_name, TRX_TM FROM app_prod_trans_regist_remark where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(create_date) >= DATE($S{startDate}) and DATE(create_date) <= DATE($S{endDate})";
		}
		sql += " order by create_date desc,create_time desc ";
		return super.findRows(sql,DataSourceProperty.PUB, params);
		}

	public UpdateResult addProdTransRegist(ProdTransRegist prodTransRegist) throws Exception {
		return super.update("INSERT INTO app_prod_trans_regist_remark( trans_approve_id,trans_approve_name,trader_id,trader_name,related_party_trans,trans_origin_time, bank_code,prod_code,trans_code,asset_code,cur,amt,convert_rmb,quantity,method_asset_measure,cash_type,detail_cash_type,trade_date,trade_counter,counter_type,unit_price_full,unit_price_net,rate_annual_return,trans_ident_code,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type,trx_tm) VALUES($S{transApproveId},$S{transApproveName},$S{traderId},$S{traderName},$S{relatedPartyTrans},$S{transOriginTime},$S{bankCode},$S{prodCode},$S{transCode},$S{assetCode},$S{cur},$D{amt},$D{convertRmb},$D{quantity},$S{methodAssetMeasure},$S{cashType},$S{detailCashType},$S{tradeDate},$S{tradeCounter},$S{counterType},$D{unitPriceFull},$D{unitPriceNet},$D{rateAnnualReturn},$S{transIdentCode},$S{details},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus},$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType},$S{trxTm})",
				DataSourceProperty.PUB,prodTransRegist);
	}
	
	public UpdateResult updateProdTransRegist(SqlParam<ProdTransRegist> params) throws Exception {
		return super.update("UPDATE app_prod_trans_regist_remark SET bank_code=$S{bankCode} ,cur=$S{cur} ,amt=$D{amt} ,convert_rmb=$D{convertRmb} ,quantity=$D{quantity} ,method_asset_measure=$S{methodAssetMeasure} ,detail_cash_type=$S{detailCashType} ,trade_date=$S{tradeDate} ,trade_counter=$S{tradeCounter} ,counter_type=$S{counterType} ,unit_price_full=$D{unitPriceFull} ,unit_price_net=$D{unitPriceNet} ,rate_annual_return=$D{rateAnnualReturn} ,related_party_trans=$S{relatedPartyTrans},trans_approve_id=$S{transApproveId},trans_approve_name=$S{transApproveName},trader_id=$S{traderId},trader_name=$S{traderName},trans_origin_time=$S{transOriginTime},trans_ident_code=$S{transIdentCode} ,details=$S{details} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,summit_user=$S{summitUser} ,create_date=$S{createDate} ,create_time=$S{createTime} ,op_type=$S{opType}  WHERE  prod_code=$S{prodCode} AND trans_code=$S{transCode} AND asset_code=$S{assetCode} AND cash_type=$S{cashType} ",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteProdTransRegist(SqlParam<ProdTransRegist> params) throws Exception {
		return super.update("DELETE FROM app_prod_trans_regist_remark WHERE  prod_code=$S{prodCode} AND trans_code=$S{transCode} AND asset_code=$S{assetCode} AND cash_type=$S{cashType} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportProdTransRegist(ProdTransRegist prodTransRegist) throws Exception {
		return super.update("INSERT INTO app_prod_trans_regist_remark( trans_approve_id,trans_approve_name,trader_id,trader_name,related_party_trans,trans_origin_time, bank_code,prod_code,trans_code,asset_code,cur,amt,convert_rmb,quantity,method_asset_measure,cash_type,detail_cash_type,trade_date,trade_counter,counter_type,unit_price_full,unit_price_net,rate_annual_return,trans_ident_code,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type,trx_tm,is_cover) VALUES($S{transApproveId},$S{transApproveName},$S{traderId},$S{traderName},$S{relatedPartyTrans},$S{transOriginTime},$S{bankCode},$S{prodCode},$S{transCode},$S{assetCode},$S{cur},$D{amt},$D{convertRmb},$D{quantity},$S{methodAssetMeasure},$S{cashType},$S{detailCashType},$S{tradeDate},$S{tradeCounter},$S{counterType},$D{unitPriceFull},$D{unitPriceNet},$D{rateAnnualReturn},$S{transIdentCode},$S{details},$S{registerSerno},date_format(CURDATE(),'%Y%m%d'),$S{registerDate},$S{registerStatus},$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType},$S{trxTm},$S{isCover})",
				DataSourceProperty.PUB,prodTransRegist);
	}

}
