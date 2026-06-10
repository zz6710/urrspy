package com.kayak.rpt.zz.historyInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.historyInfo.model.SubseqSubscrRegistInfoh;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class SubseqSubscrRegistInfohDao extends ComnDao {

	public SqlResult<SubseqSubscrRegistInfoh> findSubseqSubscrRegistInfohs(SqlParam<SubseqSubscrRegistInfoh> params) throws Exception {
		String sql = "SELECT  bank_code, prod_code, initial_nav, nav, aggregate_nav, nav_cur, convert_rmb_nav, convert_rmb_agg_nav, realized_annual_return, expected_annual_return, inconme_bank, business_start_date, business_end_date, subscribed_latest_vol, redeemed_latest_vol, units_bonus, cash_bonus, prod_amt, prod_vol, details, register_serno, imp_date, register_date, register_status, convert_initial_nav, ccy_and_pch_rdm, nav_dt, create_date, theory_report_start_date, theory_report_end_date  FROM app_subseq_subscr_regist_info_h where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(register_Date) >= DATE($S{startDate}) and DATE(register_Date) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getBusStartDate())) {
			sql += " and DATE(business_end_date) >= DATE($S{busStartDate}) and DATE(business_end_date) <= DATE($S{busEndDate})";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addSubseqSubscrRegistInfoh(SqlParam<SubseqSubscrRegistInfoh> params) throws Exception {
		return super.update("INSERT INTO app_subseq_subscr_regist_info_h(bank_code,prod_code,initial_nav,nav,aggregate_nav,nav_cur,convert_rmb_nav,convert_rmb_agg_nav,realized_annual_return,expected_annual_return,inconme_bank,business_start_date,business_end_date,subscribed_latest_vol,redeemed_latest_vol,units_bonus,cash_bonus,prod_amt,prod_vol,details,register_serno,imp_date,register_date,register_status,convert_initial_nav) VALUES($S{bankCode},$S{prodCode},$S{initialNav},$S{nav},$S{aggregateNav},$S{navCur},$S{convertRmbNav},$S{convertRmbAggNav},$S{realizedAnnualReturn},$S{expectedAnnualReturn},$S{inconmeBank},$S{businessStartDate},$S{businessEndDate},$S{subscribedLatestVol},$S{redeemedLatestVol},$S{unitsBonus},$S{cashBonus},$S{prodAmt},$S{prodVol},$S{details},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus},$S{convertInitialNav})",
				params.getModel());
	}
	
	public UpdateResult updateSubseqSubscrRegistInfoh(SqlParam<SubseqSubscrRegistInfoh> params) throws Exception {
		return super.update("UPDATE app_subseq_subscr_regist_info_h SET bank_code=$S{bankCode} ,prod_code=$S{prodCode} ,initial_nav=$S{initialNav} ,nav=$S{nav} ,aggregate_nav=$S{aggregateNav} ,nav_cur=$S{navCur} ,convert_rmb_nav=$S{convertRmbNav} ,convert_rmb_agg_nav=$S{convertRmbAggNav} ,realized_annual_return=$S{realizedAnnualReturn} ,expected_annual_return=$S{expectedAnnualReturn} ,inconme_bank=$S{inconmeBank} ,business_start_date=$S{businessStartDate} ,business_end_date=$S{businessEndDate} ,subscribed_latest_vol=$S{subscribedLatestVol} ,redeemed_latest_vol=$S{redeemedLatestVol} ,units_bonus=$S{unitsBonus} ,cash_bonus=$S{cashBonus} ,prod_amt=$S{prodAmt} ,prod_vol=$S{prodVol} ,details=$S{details} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,convert_initial_nav=$S{convertInitialNav}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteSubseqSubscrRegistInfoh(SqlParam<SubseqSubscrRegistInfoh> params) throws Exception {
		return super.update("DELETE FROM app_subseq_subscr_regist_info_h WHERE ",
				params.getModel());
	}

}
