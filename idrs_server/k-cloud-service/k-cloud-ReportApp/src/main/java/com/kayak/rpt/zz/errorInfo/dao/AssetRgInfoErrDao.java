package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.AssetRgInfoErr;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class AssetRgInfoErrDao extends ComnDao {

	public SqlResult<AssetRgInfoErr> findAssetRgInfos(SqlParam<AssetRgInfoErr> params) throws Exception {
		String sql = "SELECT BANK_CODE_DESC, PROD_REG_ENC_DESC, HOLDING_TYPE_DESC, ASSET_CODE_DESC, INVESTED_ASSET_DESC, MEZZANINE_NUMBER_DESC, MEZZANINE_ASSET_CODE_DESC, ACCOUNT_CODE_DESC, INVESTED_AMOUNT_DESC, INVESTED_AMOUNT_CNY_DESC, FAIR_VALUE_DESC, FAIR_VALUE_CNY_DESC, NET_VALUATION_DESC, FL_VALUATION_DESC, QUANTITY_DESC, CNY_DESC, HOLDING_DATE_DESC, DETAILS, register_serno, imp_date, register_date, register_status, create_date, theory_report_start_date, theory_report_end_date, id, report_date\n" +
				"FROM app_asset_regist_info_erdesc  where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addAssetRgInfo(SqlParam<AssetRgInfoErr> params) throws Exception {
		return super.update("INSERT INTO app_asset_regist_info_erdesc(account_code_desc,asset_code_desc,bank_code_desc,cny_desc,fair_value_cny_desc,fair_value_desc,fl_valuation_desc,holding_date_desc,holding_type_desc,id,imp_date,invested_amount_cny_desc,invested_amount_desc,invested_asset_desc,mezzanine_asset_code_desc,mezzanine_number_desc,net_valuation_desc,prod_reg_enc_desc,quantity_desc) VALUES($S{accountCodeDesc},$S{assetCodeDesc},$S{bankCodeDesc},$S{cnydesc},$S{fairValueCnyv},$S{fairValueDesc},$S{flValuationDesc} ,$S{holdingDateDesc},$S{holdingTypeDesc},$S{impDate} ,$S{investedAmountCnyDesc},$S{investedAmountDesc},$S{investedAssetDesc},$S{mezzanineAssetCodeDesc},$S{mezzanineNumberDesc},$S{netValuationDesc},prod_reg_enc_desc = $S{prodRegEncDesc} ,quantity_desc = $S{quantityDesc})",
				params.getModel());
	}
	
	public UpdateResult updateAssetRgInfo(SqlParam<AssetRgInfoErr> params) throws Exception {
		return super.update("UPDATE app_asset_regist_info_erdesc SET account_code_desc = $S{accountCodeDesc},asset_code_desc = $S{assetCodeDesc},bank_code_desc = $S{bankCodeDesc},cny_desc = $S{cnydesc},fair_value_cny_desc = $S{fairValueCnyv},fair_value_desc = $S{fairValueDesc},fl_valuation_desc = $S{flValuationDesc} ,holding_date_desc = $S{holdingDateDesc},holding_type_desc = $S{holdingTypeDesc},imp_date = $S{impDate} ,invested_amount_cny_desc = $S{investedAmountCnyDesc},invested_amount_desc = $S{investedAmountDesc},invested_asset_desc = $S{investedAssetDesc},mezzanine_asset_code_desc = $S{mezzanineAssetCodeDesc},mezzanine_number_desc = $S{mezzanineNumberDesc},net_valuation_desc = $S{netValuationDesc},prod_reg_enc_desc = $S{prodRegEncDesc} ,quantity_desc = $S{quantityDesc} WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteAssetRgInfo(SqlParam<AssetRgInfoErr> params) throws Exception {
		return super.update("DELETE FROM app_asset_regist_info_erdesc WHERE ",
				params.getModel());
	}

}
