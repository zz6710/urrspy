package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.UnderAssetRgInfoErr;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class UnderAssetRgInfoErrDao extends ComnDao {

	public SqlResult<UnderAssetRgInfoErr> findUnderAssetRgInfos(SqlParam<UnderAssetRgInfoErr> params) throws Exception {
		String sql = "SELECT bank_code, asset_manager_code, convert_sum_amt, asset_sum_number, non_invested_amt, under_asset_code, under_asset_sum, under_convert_sum_amt, report_date, register_serno, imp_date, register_date, register_status, create_date, theory_report_start_date, theory_report_end_date, id\n" +
				"FROM app_under_asset_regist_info_erdesc  where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}

		return super.findRows(sql, params);
	}

	public UpdateResult addUnderAssetRgInfo(SqlParam<UnderAssetRgInfoErr> params) throws Exception {
		return super.update("INSERT INTO app_under_asset_regist_info_erdesc(bank_code_desc,asset_manager_code_desc,convert_sum_amt_desc,asset_sum_number_desc,non_invested_amt_desc,under_asset_code_desc,under_asset_sum_desc,under_convert_sum_amt_desc,report_date_desc,register_serno,imp_date) VALUES($S{bankCodeDesc},$S{assetManagerCodeDesc},$S{convertSumAmtDesc},$S{assetSumNumberDesc},$S{nonInvestedAmtDesc},$S{underAssetCodeDesc},$S{underAssetSumDesc},$S{underConvertSumAmtDesc},$S{reportDateDesc},$S{registerSerno},$S{impDate})",
				params.getModel());
	}
	
	public UpdateResult updateUnderAssetRgInfo(SqlParam<UnderAssetRgInfoErr> params) throws Exception {
		return super.update("UPDATE app_under_asset_regist_info_erdesc SET bank_code_desc=$S{bankCodeDesc} ,asset_manager_code_desc=$S{assetManagerCodeDesc} ,convert_sum_amt_desc=$S{convertSumAmtDesc} ,asset_sum_number_desc=$S{assetSumNumberDesc} ,non_invested_amt_desc=$S{nonInvestedAmtDesc} ,under_asset_code_desc=$S{underAssetCodeDesc} ,under_asset_sum_desc=$S{underAssetSumDesc} ,under_convert_sum_amt_desc=$S{underConvertSumAmtDesc} ,report_date_desc=$S{reportDateDesc} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteUnderAssetRgInfo(SqlParam<UnderAssetRgInfoErr> params) throws Exception {
		return super.update("DELETE FROM app_under_asset_regist_info_erdesc WHERE ",
				params.getModel());
	}

}
