package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.AppraiseRgInfoErr;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class AppraiseRgInfoErrDao extends ComnDao {

	public SqlResult<AppraiseRgInfoErr> findAppraiseRgInfoErrs(SqlParam<AppraiseRgInfoErr> params) throws Exception {
		String sql = "SELECT bank_code_desc, asset_code_desc, valuation_date_desc, unit_debt_net_desc, unit_debt_full_desc, details_desc, register_serno, " +
				"imp_date, create_date, theory_report_start_date, theory_report_end_date, id, report_date\n" +
				"FROM app_appraise_regist_info_erdesc where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}
		return super.findRows(sql, params);
	}
	public UpdateResult addAppraiseRgInfoErr(SqlParam<AppraiseRgInfoErr> params) throws Exception {
		return super.update("INSERT INTO app_appraise_regist_info_erdesc(bank_code_desc,asset_code_desc,valuation_date_desc,unit_debt_net_desc,unit_debt_full_desc,details_desc,register_serno,imp_date) VALUES($S{bankCodeDesc},$S{assetCodeDesc},$S{valuationDateDesc},$S{unitDebtNetDesc},$S{unitDebtFullDesc},$S{detailsDesc},$S{registerSerno},$S{impDate})",
				params.getModel());
	}
	
	public UpdateResult updateAppraiseRgInfoErr(SqlParam<AppraiseRgInfoErr> params) throws Exception {
		return super.update("UPDATE app_appraise_regist_info_erdesc SET bank_code_desc=$S{bankCodeDesc} ,asset_code_desc=$S{assetCodeDesc} ,valuation_date_desc=$S{valuationDateDesc} ,unit_debt_net_desc=$S{unitDebtNetDesc} ,unit_debt_full_desc=$S{unitDebtFullDesc} ,details_desc=$S{detailsDesc} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteAppraiseRgInfoErr(SqlParam<AppraiseRgInfoErr> params) throws Exception {
		return super.update("DELETE FROM app_appraise_regist_info_erdesc WHERE ",
				params.getModel());
	}

}
