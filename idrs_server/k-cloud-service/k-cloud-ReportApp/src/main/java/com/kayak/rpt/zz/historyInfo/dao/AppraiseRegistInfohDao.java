package com.kayak.rpt.zz.historyInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.historyInfo.model.AppraiseRegistInfoh;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class AppraiseRegistInfohDao extends ComnDao {

	public SqlResult<AppraiseRegistInfoh> findAppraiseRegistInfohs(SqlParam<AppraiseRegistInfoh> params) throws Exception {
		String sql = "SELECT bank_code, asset_code, valuation_date, unit_debt_net, unit_debt_full, details, register_serno, imp_date, register_date, register_status, create_date, theory_report_start_date, theory_report_end_date FROM app_appraise_regist_info_h where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(register_Date) >= DATE($S{startDate}) and DATE(register_Date) <= DATE($S{endDate})";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addAppraiseRegistInfoh(SqlParam<AppraiseRegistInfoh> params) throws Exception {
		return super.update("INSERT INTO app_appraise_regist_info_h(bank_code,asset_code,valuation_date,unit_debt_net,unit_debt_full,details,register_serno,imp_date,register_date,register_status) VALUES($S{bankCode},$S{assetCode},$S{valuationDate},$S{unitDebtNet},$S{unitDebtFull},$S{details},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus})",
				params.getModel());
	}
	
	public UpdateResult updateAppraiseRegistInfoh(SqlParam<AppraiseRegistInfoh> params) throws Exception {
		return super.update("UPDATE app_appraise_regist_info_h SET bank_code=$S{bankCode} ,asset_code=$S{assetCode} ,valuation_date=$S{valuationDate} ,unit_debt_net=$S{unitDebtNet} ,unit_debt_full=$S{unitDebtFull} ,details=$S{details} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteAppraiseRegistInfoh(SqlParam<AppraiseRegistInfoh> params) throws Exception {
		return super.update("DELETE FROM app_appraise_regist_info_h WHERE ",
				params.getModel());
	}

}
