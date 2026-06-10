package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.AssetRegist;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class AssetRegistDao extends ComnDao {

	public SqlResult<AssetRegist> findAssetRegists(SqlParam<AssetRegist> params) throws Exception {
		String sql = "SELECT * FROM app_asset_regist_info_remark where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(create_date) >= DATE($S{startDate}) and DATE(create_date) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getReportStartDate())) {
			sql += " and DATE(report_date) >= DATE($S{reportStartDate}) and DATE(report_date) <= DATE($S{reportEndDate})";
		}
		sql += " order by create_date desc,create_time desc ";
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addAssetRegist(AssetRegist AssetRegist) throws Exception {
		return super.update("INSERT INTO app_asset_regist_info_remark(account_code,asset_code,bank_code,cny,create_date,create_time,details,fair_value,fair_value_cny,fl_valuation,holding_date,holding_type,imp_date,invested_amount,invested_amount_cny,invested_asset,mezzanine_asset_code,mezzanine_number,net_valuation,op_type,prod_reg_enc,quantity,register_date,register_serno,register_status,summit_user,report_date) VALUES($S{accountCode},$S{assetCode},$S{bankCode},$S{cny},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{details},$D{fairValue},$D{fairValueCny},$D{flValuation},$S{holdingDate},$S{holdingType},$S{impDate},$D{investedAmount},$D{investedAmountCny},$S{investedAsset},$S{mezzanineAssetCode},$S{mezzanineNumber},$D{netValuation},$S{opType},$S{prodRegEnc},$D{quantity},$S{registerDate},$S{registerSerno},$S{registerStatus},$S{summitUser},$S{reportDate})",
				DataSourceProperty.PUB,AssetRegist);
	}

	public UpdateResult addImportAssetRegist(AssetRegist AssetRegist) throws Exception {
		return super.update("INSERT INTO app_asset_regist_info_remark(account_code,asset_code,bank_code,cny,create_date,create_time,details,fair_value,fair_value_cny,fl_valuation,holding_date,holding_type,imp_date,invested_amount,invested_amount_cny,invested_asset,mezzanine_asset_code,mezzanine_number,net_valuation,op_type,prod_reg_enc,quantity,register_date,register_serno,register_status,summit_user,report_date)VALUES($S{accountCode},$S{assetCode},$S{bankCode},$S{cny},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{details},$D{fairValue},$D{fairValueCny},$D{flValuation},$S{holdingDate},$S{holdingType},date_format(CURDATE(),'%Y%m%d'),$D{investedAmount},$D{investedAmountCny},$S{investedAsset},$S{mezzanineAssetCode},$S{mezzanineNumber},$D{netValuation},$S{opType},$S{prodRegEnc},$D{quantity},$S{registerDate},$S{registerSerno},$S{registerStatus},$S{summitUser},$S{reportDate})",
				DataSourceProperty.PUB,AssetRegist);
	}
}
