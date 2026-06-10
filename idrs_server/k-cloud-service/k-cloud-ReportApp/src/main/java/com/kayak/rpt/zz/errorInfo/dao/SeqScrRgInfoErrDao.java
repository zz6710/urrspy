package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.SeqScrRgInfoErr;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class SeqScrRgInfoErrDao extends ComnDao {

	public SqlResult<SeqScrRgInfoErr> findSeqScrRgInfos(SqlParam<SeqScrRgInfoErr> params) throws Exception {
		String sql = "SELECT bank_code, prod_code, initial_nav, nav, aggregate_nav, nav_cur, convert_rmb_nav, convert_rmb_agg_nav, realized_annual_return, expected_annual_return, inconme_bank, business_start_date, business_end_date, subscribed_latest_vol, redeemed_latest_vol, units_bonus, cash_bonus, prod_amt, prod_vol, details, register_serno, imp_date, register_date, register_status, convert_initial_nav, create_date, theory_report_start_date, theory_report_end_date, id, report_date\n" +
				"FROM app_subseq_subscr_regist_info_erdesc  where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}

		return super.findRows(sql, params);
	}

	public UpdateResult addSeqScrRgInfo(SqlParam<SeqScrRgInfoErr> params) throws Exception {
		return super.update("INSERT INTO app_subseq_subscr_regist_info_erdesc(bank_code_desc,prod_code_desc,initial_nav_desc,nav_desc,aggregate_nav_desc,nav_cur_desc,convert_rmb_nav_desc,convert_rmb_agg_nav_desc,realized_annual_return_desc,expected_annual_return_desc,inconme_bank_desc,business_start_date_desc,business_end_date_desc,cur_desc,sub_amt_lass_period_desc,cur_principal_period_desc,cur_pay_period_desc,subscribed_latest_vol_desc,redeemed_latest_vol_desc,units_bonus_desc,cash_bonus_desc,prod_amt_desc,prod_vol_desc,details_desc,convert_initial_nav_desc,register_serno,imp_date) VALUES($S{bankCodeDesc},$S{prodCodeDesc},$S{initialNavDesc},$S{navDesc},$S{aggregateNavDesc},$S{navCurDesc},$S{convertRmbNavDesc},$S{convertRmbAggNavDesc},$S{realizedAnnualReturnDesc},$S{expectedAnnualReturnDesc},$S{inconmeBankDesc},$S{businessStartDateDesc},$S{businessEndDateDesc},$S{curDesc},$S{subAmtLassPeriodDesc},$S{curPrincipalPeriodDesc},$S{curPayPeriodDesc},$S{subscribedLatestVolDesc},$S{redeemedLatestVolDesc},$S{unitsBonusDesc},$S{cashBonusDesc},$S{prodAmtDesc},$S{prodVolDesc},$S{detailsDesc},$S{convertInitialNavDesc},$S{registerSerno},$S{impDate})",
				params.getModel());
	}
	
	public UpdateResult updateSeqScrRgInfo(SqlParam<SeqScrRgInfoErr> params) throws Exception {
		return super.update("UPDATE app_subseq_subscr_regist_info_erdesc SET bank_code_desc=$S{bankCodeDesc} ,prod_code_desc=$S{prodCodeDesc} ,initial_nav_desc=$S{initialNavDesc} ,nav_desc=$S{navDesc} ,aggregate_nav_desc=$S{aggregateNavDesc} ,nav_cur_desc=$S{navCurDesc} ,convert_rmb_nav_desc=$S{convertRmbNavDesc} ,convert_rmb_agg_nav_desc=$S{convertRmbAggNavDesc} ,realized_annual_return_desc=$S{realizedAnnualReturnDesc} ,expected_annual_return_desc=$S{expectedAnnualReturnDesc} ,inconme_bank_desc=$S{inconmeBankDesc} ,business_start_date_desc=$S{businessStartDateDesc} ,business_end_date_desc=$S{businessEndDateDesc} ,cur_desc=$S{curDesc} ,sub_amt_lass_period_desc=$S{subAmtLassPeriodDesc} ,cur_principal_period_desc=$S{curPrincipalPeriodDesc} ,cur_pay_period_desc=$S{curPayPeriodDesc} ,subscribed_latest_vol_desc=$S{subscribedLatestVolDesc} ,redeemed_latest_vol_desc=$S{redeemedLatestVolDesc} ,units_bonus_desc=$S{unitsBonusDesc} ,cash_bonus_desc=$S{cashBonusDesc} ,prod_amt_desc=$S{prodAmtDesc} ,prod_vol_desc=$S{prodVolDesc} ,details_desc=$S{detailsDesc} ,convert_initial_nav_desc=$S{convertInitialNavDesc} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteSeqScrRgInfo(SqlParam<SeqScrRgInfoErr> params) throws Exception {
		return super.update("DELETE FROM app_subseq_subscr_regist_info_erdesc WHERE ",
				params.getModel());
	}

}
