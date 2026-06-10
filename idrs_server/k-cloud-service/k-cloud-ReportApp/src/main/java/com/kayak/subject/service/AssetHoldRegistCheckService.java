package com.kayak.subject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.dao.AssetHoldRegistCheckDao;
import com.kayak.subject.model.AssetHoldRegistCheck;

@Service
@APIDefine(desc = "资产持仓登记校验表服务", model = AssetHoldRegistCheck.class)
public class AssetHoldRegistCheckService {

	@Autowired
	private AssetHoldRegistCheckDao assetHoldRegistCheckDao;

	@API(desc = "查询资产持仓登记校验表信息", auth = APIAuth.YES)
	public SqlResult<AssetHoldRegistCheck> findAssetHoldRegistChecks(SqlParam<AssetHoldRegistCheck> params) throws Exception {
		SqlResult<AssetHoldRegistCheck> newR =  assetHoldRegistCheckDao.findAssetHoldRegistChecks(params);
		return newR;
	}

	@API(desc = "添加资产持仓登记校验表", params = "id,prod_code,prod_reg_enc,tot_assets,assets_jn,org_assets,org_debt,org_assets_jn,org_assets_ce,org_assets_cerate,org_assets_jnce,org_assets_jncerate,otc_all_assets,buttom_debt,otc_debt,otc_assets,otc_assets_jn,otc_assets_ce,otc_assets_cerate,otc_assets_jnce,otc_assets_jncerate,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,report_date", auth = APIAuth.NO)
	public int addAssetHoldRegistCheck(SqlParam<AssetHoldRegistCheck> params) throws Exception {
		return assetHoldRegistCheckDao.addAssetHoldRegistCheck(params).getEffect();
	}
	
	@API(desc = "修改资产持仓登记校验表", params = "id,prod_code,prod_reg_enc,tot_assets,assets_jn,org_assets,org_debt,org_assets_jn,org_assets_ce,org_assets_cerate,org_assets_jnce,org_assets_jncerate,otc_all_assets,buttom_debt,otc_debt,otc_assets,otc_assets_jn,otc_assets_ce,otc_assets_cerate,otc_assets_jnce,otc_assets_jncerate,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,report_date", auth = APIAuth.NO)
	public int updateAssetHoldRegistCheck(SqlParam<AssetHoldRegistCheck> params) throws Exception {
		return assetHoldRegistCheckDao.updateAssetHoldRegistCheck(params).getEffect();
	}
	
	@API(desc = "删除资产持仓登记校验表", params = "id,prod_code,prod_reg_enc,tot_assets,assets_jn,org_assets,org_debt,org_assets_jn,org_assets_ce,org_assets_cerate,org_assets_jnce,org_assets_jncerate,otc_all_assets,buttom_debt,otc_debt,otc_assets,otc_assets_jn,otc_assets_ce,otc_assets_cerate,otc_assets_jnce,otc_assets_jncerate,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,report_date", auth = APIAuth.NO)
	public int deleteAssetHoldRegistCheck(SqlParam<AssetHoldRegistCheck> params) throws Exception {
		return assetHoldRegistCheckDao.deleteAssetHoldRegistCheck(params).getEffect();
	}

}
