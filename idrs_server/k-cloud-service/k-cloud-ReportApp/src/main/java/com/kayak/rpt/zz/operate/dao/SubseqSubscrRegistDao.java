package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.SubseqSubscrRegist;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class SubseqSubscrRegistDao extends ComnDao {

	public SqlResult<SubseqSubscrRegist> findSubseqSubscrRegists(SqlParam<SubseqSubscrRegist> params) throws Exception {
		String sql = "SELECT bank_code, prod_code, initial_nav, nav, aggregate_nav, nav_cur, convert_rmb_nav, convert_rmb_agg_nav, realized_annual_return, expected_annual_return, inconme_bank, business_start_date, business_end_date, subscribed_latest_vol, redeemed_latest_vol, units_bonus, cash_bonus, prod_amt, prod_vol, details, register_serno, imp_date, register_date, register_status, convert_initial_nav, summit_user, create_date, create_time, op_type, id, CCY_AND_PCH_RDM, nav_dt FROM app_subseq_subscr_regist_remark where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(create_date) >= DATE($S{startDate}) and DATE(create_date) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getBusiStartDate())) {
			sql += " and DATE(business_end_date) >= DATE($S{busiStartDate}) and DATE(business_end_date) <= DATE($S{busiEndDate})";
		}
		sql += " order by create_date desc,create_time desc ";
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addSubseqSubscrRegist(SubseqSubscrRegist subseqSubscrRegist) throws Exception {
		return super.update("INSERT INTO app_subseq_subscr_regist_remark(bank_code,prod_code,initial_nav,nav,aggregate_nav,nav_cur,convert_rmb_nav,convert_rmb_agg_nav,realized_annual_return,expected_annual_return,inconme_bank,business_start_date,business_end_date,subscribed_latest_vol,redeemed_latest_vol,units_bonus,cash_bonus,prod_amt,prod_vol,details,register_serno,imp_date,register_date,register_status,convert_initial_nav,summit_user,create_date,create_time,op_type,ccy_and_pch_rdm,nav_dt,report_date) VALUES($S{bankCode},$S{prodCode},$D{initialNav},$D{nav},$D{aggregateNav},$S{navCur},$D{convertRmbNav},$D{convertRmbAggNav},$D{realizedAnnualReturn},$D{expectedAnnualReturn},$D{inconmeBank},$S{businessStartDate},$S{businessEndDate},$D{subscribedLatestVol},$D{redeemedLatestVol},$D{unitsBonus},$D{cashBonus},$D{prodAmt},$D{prodVol},$S{details},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus},$D{convertInitialNav},$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType},$S{ccyAndPchRdm},$S{navDt},$S{reportDate})",
				DataSourceProperty.PUB,subseqSubscrRegist);
	}
	
	public UpdateResult updateSubseqSubscrRegist(SqlParam<SubseqSubscrRegist> params) throws Exception {
		return super.update("UPDATE app_subseq_subscr_regist_remark SET bank_code=$S{bankCode} ,prod_code=$S{prodCode} ,initial_nav=$D{initialNav} ,nav=$D{nav} ,aggregate_nav=$D{aggregateNav} ,nav_cur=$S{navCur} ,convert_rmb_nav=$D{convertRmbNav} ,convert_rmb_agg_nav=$D{convertRmbAggNav} ,realized_annual_return=$D{realizedAnnualReturn} ,expected_annual_return=$D{expectedAnnualReturn} ,inconme_bank=$D{inconmeBank} ,business_start_date=$S{businessStartDate} ,business_end_date=$S{businessEndDate} ,subscribed_latest_vol=$D{subscribedLatestVol} ,redeemed_latest_vol=$D{redeemedLatestVol} ,units_bonus=$D{unitsBonus} ,cash_bonus=$D{cashBonus} ,prod_amt=$D{prodAmt} ,prod_vol=$D{prodVol} ,details=$S{details} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,convert_initial_nav=$S{convertInitialNav} ,summit_user=$S{summitUser}  ,op_type=$S{opType}  WHERE ",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteSubseqSubscrRegist(SqlParam<SubseqSubscrRegist> params) throws Exception {
		return super.update("DELETE FROM app_subseq_subscr_regist_remark WHERE register_serno=$S{registerSerno} ",
				DataSourceProperty.PUB,params.getModel());
	}

}
