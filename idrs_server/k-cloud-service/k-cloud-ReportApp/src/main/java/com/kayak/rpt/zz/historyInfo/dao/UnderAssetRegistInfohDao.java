package com.kayak.rpt.zz.historyInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.historyInfo.model.UnderAssetRegistInfoh;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class UnderAssetRegistInfohDao extends ComnDao {

	public SqlResult<UnderAssetRegistInfoh> findUnderAssetRegistInfohs(SqlParam<UnderAssetRegistInfoh> params) throws Exception {
		String sql = "SELECT bank_code, asset_manager_code, convert_sum_amt, asset_sum_number, non_invested_amt, under_asset_code, under_asset_sum, under_convert_sum_amt, report_date, register_serno, imp_date, register_date, register_status, create_date, theory_report_start_date, theory_report_end_date FROM app_under_asset_regist_info_h where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(register_Date) >= DATE($S{startDate}) and DATE(register_Date) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getReportStartDate())) {
			sql += " and DATE(report_date) >= DATE($S{reportStartDate}) and DATE(report_date) <= DATE($S{reportEndDate})";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addUnderAssetRegistInfoh(SqlParam<UnderAssetRegistInfoh> params) throws Exception {
		return super.update("INSERT INTO app_under_asset_regist_info_h(bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status) VALUES($S{bankCode},$S{assetManagerCode},$S{convertSumAmt},$S{assetSumNumber},$S{nonInvestedAmt},$S{underAssetCode},$S{underAssetSum},$S{underConvertSumAmt},$S{reportDate},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus})",
				params.getModel());
	}
	
	public UpdateResult updateUnderAssetRegistInfoh(SqlParam<UnderAssetRegistInfoh> params) throws Exception {
		return super.update("UPDATE app_under_asset_regist_info_h SET bank_code=$S{bankCode} ,asset_manager_code=$S{assetManagerCode} ,convert_sum_amt=$S{convertSumAmt} ,asset_sum_number=$S{assetSumNumber} ,non_invested_amt=$S{nonInvestedAmt} ,under_asset_code=$S{underAssetCode} ,under_asset_sum=$S{underAssetSum} ,under_convert_sum_amt=$S{underConvertSumAmt} ,report_date=$S{reportDate} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteUnderAssetRegistInfoh(SqlParam<UnderAssetRegistInfoh> params) throws Exception {
		return super.update("DELETE FROM app_under_asset_regist_info_h WHERE ",
				params.getModel());
	}

}
