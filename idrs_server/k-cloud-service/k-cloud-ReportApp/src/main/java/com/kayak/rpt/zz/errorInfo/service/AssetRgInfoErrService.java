package com.kayak.rpt.zz.errorInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.AssetRgInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.AssetRgInfoErr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "资产持仓错误信息服务", model = AssetRgInfoErr.class)
public class AssetRgInfoErrService {

	@Autowired
	private AssetRgInfoErrDao AssetRgInfoErrDao;

	@API(desc = "查询资产持仓错误信息信息", auth = APIAuth.YES)
	public SqlResult<AssetRgInfoErr> findAssetRgInfos(SqlParam<AssetRgInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return AssetRgInfoErrDao.findAssetRgInfos(params);
	}

	@API(desc = "添加资产持仓错误信息", params = "account_code_desc,asset_code_desc,bank_code_desc,cny_desc,create_date,details,fair_value_cny_desc,fair_value_desc,fl_valuation_desc,holding_date_desc,holding_type_desc,id,imp_date,invested_amount_cny_desc,invested_amount_desc,invested_asset_desc,mezzanine_asset_code_desc,mezzanine_number_desc,net_valuation_desc,prod_reg_enc_desc,quantity_desc,register_date,register_serno,register_status,theory_report_end_date,theory_report_start_date", auth = APIAuth.NO)
	public int addAssetRgInfo(SqlParam<AssetRgInfoErr> params) throws Exception {
		return AssetRgInfoErrDao.addAssetRgInfo(params).getEffect();
	}
	
	@API(desc = "修改资产持仓错误信息", params = "account_code_desc,asset_code_desc,bank_code_desc,cny_desc,create_date,details,fair_value_cny_desc,fair_value_desc,fl_valuation_desc,holding_date_desc,holding_type_desc,id,imp_date,invested_amount_cny_desc,invested_amount_desc,invested_asset_desc,mezzanine_asset_code_desc,mezzanine_number_desc,net_valuation_desc,prod_reg_enc_desc,quantity_desc,register_date,register_serno,register_status,theory_report_end_date,theory_report_start_date", auth = APIAuth.NO)
	public int updateAssetRgInfo(SqlParam<AssetRgInfoErr> params) throws Exception {
		return AssetRgInfoErrDao.updateAssetRgInfo(params).getEffect();
	}
	
	@API(desc = "删除资产持仓错误信息", params = "account_code_desc,asset_code_desc,bank_code_desc,cny_desc,create_date,details,fair_value_cny_desc,fair_value_desc,fl_valuation_desc,holding_date_desc,holding_type_desc,id,imp_date,invested_amount_cny_desc,invested_amount_desc,invested_asset_desc,mezzanine_asset_code_desc,mezzanine_number_desc,net_valuation_desc,prod_reg_enc_desc,quantity_desc,register_date,register_serno,register_status,theory_report_end_date,theory_report_start_date", auth = APIAuth.NO)
	public int deleteAssetRgInfo(SqlParam<AssetRgInfoErr> params) throws Exception {
		return AssetRgInfoErrDao.deleteAssetRgInfo(params).getEffect();
	}

}
