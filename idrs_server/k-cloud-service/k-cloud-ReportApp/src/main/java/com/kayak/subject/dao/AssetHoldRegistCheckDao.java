package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.AssetHoldRegistCheck;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class AssetHoldRegistCheckDao extends ComnDao {

	public SqlResult<AssetHoldRegistCheck> findAssetHoldRegistChecks(SqlParam<AssetHoldRegistCheck> params) throws Exception {
		String sql = "SELECT id,prod_code,prod_reg_enc,tot_assets,assets_jn," +
				"org_assets,org_debt,org_assets_jn,org_assets_ce," +
				"case when org_assets_cerate > 0 then org_assets_cerate else '0.00000000' end org_assets_cerate,org_assets_jnce," +
				"case when org_assets_jncerate > 0 then org_assets_jncerate else '0.00000000' end org_assets_jncerate," +
				"otc_all_assets,buttom_debt,otc_debt,otc_assets,otc_assets_jn, otc_assets_ce," +
				"case when otc_assets_cerate > 0 then otc_assets_cerate else '0.00000000' end  otc_assets_cerate,otc_assets_jnce," +
				"case when otc_assets_jncerate > 0 then otc_assets_jncerate else '0.00000000' end  otc_assets_jncerate,create_date," +
				"theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status," +
				"sys_data_version,sys_data_source,report_date FROM app_asset_hold_regist_check where 1=1 ";
		if (Tools.isNotEmpty(params.getModel().getReportDate())) {
			sql += " and report_date like '" + params.getModel().getReportDate() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getProdCode())) {
			sql += " and prod_code like '%" + params.getModel().getProdCode() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getProdRegEnc())) {
			sql += " and prod_reg_enc like '%" + params.getModel().getProdRegEnc() +"%'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addAssetHoldRegistCheck(SqlParam<AssetHoldRegistCheck> params) throws Exception {
		return super.update("INSERT INTO app_asset_hold_regist_check(id,prod_code,prod_reg_enc,tot_assets,assets_jn,org_assets,org_debt,org_assets_jn,org_assets_ce,org_assets_cerate,org_assets_jnce,org_assets_jncerate,otc_all_assets,buttom_debt,otc_debt,otc_assets,otc_assets_jn,otc_assets_ce,otc_assets_cerate,otc_assets_jnce,otc_assets_jncerate,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,report_date) VALUES($AUTOIDI{id},$S{prodCode},$S{prodRegEnc},$D{totAssets},$D{assetsJn},$D{orgAssets},$D{orgDebt},$D{orgAssetsJn},$D{orgAssetsCe},$D{orgAssetsCerate},$D{orgAssetsJnce},$D{orgAssetsJncerate},$D{otcAllAssets},$D{buttomDebt},$D{otcDebt},$D{otcAssets},$D{otcAssetsJn},$D{otcAssetsCe},$D{otcAssetsCerate},$D{otcAssetsJnce},$D{otcAssetsJncerate},$S{createDate},$S{theoryReportStartDate},$S{theoryReportEndDate},$S{impDate},$S{registerStatus},$S{sysDataStatus},$S{sysDataVersion},$S{sysDataSource},$S{reportDate})",
				params.getModel());
	}
	
	public UpdateResult updateAssetHoldRegistCheck(SqlParam<AssetHoldRegistCheck> params) throws Exception {
		return super.update("UPDATE app_asset_hold_regist_check SET prod_code=$S{prodCode} ,prod_reg_enc=$S{prodRegEnc} ,tot_assets=$D{totAssets} ,assets_jn=$D{assetsJn} ,org_assets=$D{orgAssets} ,org_debt=$D{orgDebt} ,org_assets_jn=$D{orgAssetsJn} ,org_assets_ce=$D{orgAssetsCe} ,org_assets_cerate=$D{orgAssetsCerate} ,org_assets_jnce=$D{orgAssetsJnce} ,org_assets_jncerate=$D{orgAssetsJncerate} ,otc_all_assets=$D{otcAllAssets} ,buttom_debt=$D{buttomDebt} ,otc_debt=$D{otcDebt} ,otc_assets=$D{otcAssets} ,otc_assets_jn=$D{otcAssetsJn} ,otc_assets_ce=$D{otcAssetsCe} ,otc_assets_cerate=$D{otcAssetsCerate} ,otc_assets_jnce=$D{otcAssetsJnce} ,otc_assets_jncerate=$D{otcAssetsJncerate} ,create_date=$S{createDate} ,theory_report_start_date=$S{theoryReportStartDate} ,theory_report_end_date=$S{theoryReportEndDate} ,imp_date=$S{impDate} ,register_status=$S{registerStatus} ,sys_data_status=$S{sysDataStatus} ,sys_data_version=$S{sysDataVersion} ,sys_data_source=$S{sysDataSource} ,report_date=$S{reportDate}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteAssetHoldRegistCheck(SqlParam<AssetHoldRegistCheck> params) throws Exception {
		return super.update("DELETE FROM app_asset_hold_regist_check WHERE  id=$I{id} ",
				params.getModel());
	}

}
