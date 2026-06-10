package com.kayak.rpt.zz.historyInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.historyInfo.model.InitialSubRegistInfoh;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class InitialSubRegistInfohDao extends ComnDao {

	public SqlResult<InitialSubRegistInfoh> findInitialSubRegistInfohs(SqlParam<InitialSubRegistInfoh> params) throws Exception {
		String sql = "SELECT bank_code, prod_code, number_indiv_invest, number_corpor_invest, number_ucor_invest, other_distribut_agents, details, register_serno, imp_date, register_date, register_status, actual_subscribed_amt, subscribed_vol, amt_other_db_agents, FND_TRST_ACT_NBR, FND_TRST_ACT, zon_clc_amt, prod_ccy, FOUND_DT, create_date, theory_report_start_date, theory_report_end_date FROM app_initial_sub_regist_info_h where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(register_Date) >= DATE($S{startDate}) and DATE(register_Date) <= DATE($S{endDate})";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addInitialSubRegistInfoh(SqlParam<InitialSubRegistInfoh> params) throws Exception {
		return super.update("INSERT INTO app_initial_sub_regist_info_h(bank_code,prod_code,number_indiv_invest,number_corpor_invest,number_ucor_invest,other_distribut_agents,details,register_serno,imp_date,register_date,register_status,actual_subscribed_amt,subscribed_vol,amt_other_db_agents) VALUES($S{bankCode},$S{prodCode},$S{numberIndivInvest},$S{numberCorporInvest},$S{numberUcorInvest},$S{otherDistributAgents},$S{details},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus},$S{actualSubscribedAmt},$S{subscribedVol},$S{amtOtherDbAgents})",
				params.getModel());
	}
	
	public UpdateResult updateInitialSubRegistInfoh(SqlParam<InitialSubRegistInfoh> params) throws Exception {
		return super.update("UPDATE app_initial_sub_regist_info_h SET bank_code=$S{bankCode} ,prod_code=$S{prodCode} ,number_indiv_invest=$S{numberIndivInvest} ,number_corpor_invest=$S{numberCorporInvest} ,number_ucor_invest=$S{numberUcorInvest} ,other_distribut_agents=$S{otherDistributAgents} ,details=$S{details} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,actual_subscribed_amt=$S{actualSubscribedAmt} ,subscribed_vol=$S{subscribedVol} ,amt_other_db_agents=$S{amtOtherDbAgents}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteInitialSubRegistInfoh(SqlParam<InitialSubRegistInfoh> params) throws Exception {
		return super.update("DELETE FROM app_initial_sub_regist_info_h WHERE ",
				params.getModel());
	}

}
