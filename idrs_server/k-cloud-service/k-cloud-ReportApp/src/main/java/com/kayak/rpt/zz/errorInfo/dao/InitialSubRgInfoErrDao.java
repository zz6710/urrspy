package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.InitialSubRgInfoErr;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class InitialSubRgInfoErrDao extends ComnDao {

	public SqlResult<InitialSubRgInfoErr> findInitialSubRgInfoErrs(SqlParam<InitialSubRgInfoErr> params) throws Exception {
		String sql = "SELECT bank_code_desc, prod_code_desc, number_indiv_invest_desc, number_corpor_invest_desc, number_ucor_invest_desc, subscript_cur_desc, convert_rmb_desc, prod_sales_region_desc, other_distribut_agents_desc, subscript_amt_desc, subscript_amt_region_desc, actual_subscribed_amt_desc, subscribed_vol_desc, amt_other_db_agents_desc, details_desc, register_serno, imp_date, create_date, theory_report_start_date, theory_report_end_date, id, report_date\n" +
				"FROM app_initial_sub_regist_info_erdesc  where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}

		return super.findRows(sql, params);
	}

	public UpdateResult addInitialSubRgInfoErr(SqlParam<InitialSubRgInfoErr> params) throws Exception {
		return super.update("INSERT INTO app_initial_sub_regist_info_erdesc(bank_code_desc,prod_code_desc,number_indiv_invest_desc,number_corpor_invest_desc,number_ucor_invest_desc,subscript_cur_desc,convert_rmb_desc,prod_sales_region_desc,other_distribut_agents_desc,subscript_amt_desc,subscript_amt_region_desc,actual_subscribed_amt_desc,subscribed_vol_desc,amt_other_db_agents_desc,details_desc,register_serno,imp_date) VALUES($S{bankCodeDesc},$S{prodCodeDesc},$S{numberIndivInvestDesc},$S{numberCorporInvestDesc},$S{numberUcorInvestDesc},$S{subscriptCurDesc},$S{convertRmbDesc},$S{prodSalesRegionDesc},$S{otherDistributAgentsDesc},$S{subscriptAmtDesc},$S{subscriptAmtRegionDesc},$S{actualSubscribedAmtDesc},$S{subscribedVolDesc},$S{amtOtherDbAgentsDesc},$S{detailsDesc},$S{registerSerno},$S{impDate})",
				params.getModel());
	}
	
	public UpdateResult updateInitialSubRgInfoErr(SqlParam<InitialSubRgInfoErr> params) throws Exception {
		return super.update("UPDATE app_initial_sub_regist_info_erdesc SET bank_code_desc=$S{bankCodeDesc} ,prod_code_desc=$S{prodCodeDesc} ,number_indiv_invest_desc=$S{numberIndivInvestDesc} ,number_corpor_invest_desc=$S{numberCorporInvestDesc} ,number_ucor_invest_desc=$S{numberUcorInvestDesc} ,subscript_cur_desc=$S{subscriptCurDesc} ,convert_rmb_desc=$S{convertRmbDesc} ,prod_sales_region_desc=$S{prodSalesRegionDesc} ,other_distribut_agents_desc=$S{otherDistributAgentsDesc} ,subscript_amt_desc=$S{subscriptAmtDesc} ,subscript_amt_region_desc=$S{subscriptAmtRegionDesc} ,actual_subscribed_amt_desc=$S{actualSubscribedAmtDesc} ,subscribed_vol_desc=$S{subscribedVolDesc} ,amt_other_db_agents_desc=$S{amtOtherDbAgentsDesc} ,details_desc=$S{detailsDesc} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteInitialSubRgInfoErr(SqlParam<InitialSubRgInfoErr> params) throws Exception {
		return super.update("DELETE FROM app_initial_sub_regist_info_erdesc WHERE ",
				params.getModel());
	}

}
