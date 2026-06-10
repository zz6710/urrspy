package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.UnderAssetRegist;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class UnderAssetRegistDao extends ComnDao {

	public SqlResult<UnderAssetRegist> findUnderAssetRegists(SqlParam<UnderAssetRegist> params) throws Exception {
		String sql = "SELECT bank_code, asset_manager_code, convert_sum_amt, asset_sum_number, non_invested_amt, under_asset_code, under_asset_sum, under_convert_sum_amt, report_date, register_serno, imp_date, register_date, register_status, summit_user, create_date, create_time, op_type, id FROM app_under_asset_regist_remark where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(create_date) >= DATE($S{startDate}) and DATE(create_date) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getReportStartDate())) {
			sql += " and DATE(report_date) >= DATE($S{reportStartDate}) and DATE(report_date) <= DATE($S{reportEndDate})";
		}
		sql += " order by create_date desc,create_time desc ";
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addUnderAssetRegist(UnderAssetRegist underAssetRegist) throws Exception {
		return super.update("INSERT INTO app_under_asset_regist_remark(bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type) VALUES($S{bankCode},$S{assetManagerCode},$D{convertSumAmt},$D{assetSumNumber},$D{nonInvestedAmt},$S{underAssetCode},$D{underAssetSum},$D{underConvertSumAmt},$S{reportDate},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus},$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType})",
				DataSourceProperty.PUB,underAssetRegist);
	}
	
	public UpdateResult updateUnderAssetRegist(SqlParam<UnderAssetRegist> params) throws Exception {
		return super.update("UPDATE app_under_asset_regist_remark SET bank_code=$S{bankCode} ,asset_manager_code=$S{assetManagerCode} ,convert_sum_amt=$D{convertSumAmt} ,asset_sum_number=$D{assetSumNumber} ,non_invested_amt=$D{nonInvestedAmt} ,under_asset_code=$S{underAssetCode} ,under_asset_sum=$D{underAssetSum} ,under_convert_sum_amt=$D{underConvertSumAmt} ,report_date=$S{reportDate} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,summit_user=$S{summitUser} ,create_date=$S{createDate} ,create_time=$S{createTime} ,op_type=$S{opType}  WHERE id = $S{id}",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteUnderAssetRegist(SqlParam<UnderAssetRegist> params) throws Exception {
		return super.update("DELETE FROM app_under_asset_regist_remark WHERE id = $S{id}",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportUnderAssetRegist(UnderAssetRegist underAssetRegist) throws Exception {
		return super.update("INSERT INTO app_under_asset_regist_remark(bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type) VALUES($S{bankCode},$S{assetManagerCode},$D{convertSumAmt},$D{assetSumNumber},$D{nonInvestedAmt},$S{underAssetCode},$D{underAssetSum},$D{underConvertSumAmt},$S{reportDate},$S{registerSerno},date_format(CURDATE(),'%Y%m%d'),$S{registerDate},$S{registerStatus},$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType})",
				DataSourceProperty.PUB,underAssetRegist);
	}
}
