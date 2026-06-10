package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.ProdTransRgInfoErr;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ProdTransRgInfoErrDao extends ComnDao {

	public SqlResult<ProdTransRgInfoErr> findProdTransRgInfoErrs(SqlParam<ProdTransRgInfoErr> params) throws Exception {
		String sql = "SELECT BANK_CODE_DESC, PROD_CODE_DESC, TRANS_CODE_DESC, ASSET_CODE_DESC, CUR_DESC, AMT_DESC, CONVERT_RMB_DESC, QUANTITY_DESC, METHOD_ASSET_MEASURE_DESC, CASH_TYPE_DESC, DETAIL_CASH_TYPE_DESC, TRADE_DATE_DESC, TRADE_COUNTER_DESC, COUNTER_TYPE_DESC, UNIT_PRICE_FULL_DESC, UNIT_PRICE_NET_DESC, RATE_ANNUAL_RETURN_DESC, TRANS_IDENT_CODE_DESC, DETAILS_DESC, REGISTER_SERNO, IMP_DATE, create_date, theory_report_start_date, theory_report_end_date, ID, report_date\n" +
				"FROM app_prod_trans_regist_info_erdesc  where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}

		return super.findRows(sql, params);
	}

	public UpdateResult addProdTransRgInfoErr(SqlParam<ProdTransRgInfoErr> params) throws Exception {
		return super.update("INSERT INTO app_prod_trans_regist_info_erdesc(bank_code_desc,prod_code_desc,trans_code_desc,asset_code_desc,cur_desc,amt_desc,convert_rmb_desc,quantity_desc,method_asset_measure_desc,cash_type_desc,detail_cash_type_desc,trade_date_desc,trade_counter_desc,counter_type_desc,unit_price_full_desc,unit_price_net_desc,rate_annual_return_desc,trans_ident_code_desc,details_desc,register_serno,imp_date) VALUES($S{bankCodeDesc},$S{prodCodeDesc},$S{transCodeDesc},$S{assetCodeDesc},$S{curDesc},$S{amtDesc},$S{convertRmbDesc},$S{quantityDesc},$S{methodAssetMeasureDesc},$S{cashTypeDesc},$S{detailCashTypeDesc},$S{tradeDateDesc},$S{tradeCounterDesc},$S{counterTypeDesc},$S{unitPriceFullDesc},$S{unitPriceNetDesc},$S{rateAnnualReturnDesc},$S{transIdentCodeDesc},$S{detailsDesc},$S{registerSerno},$S{impDate})",
				params.getModel());
	}
	
	public UpdateResult updateProdTransRgInfoErr(SqlParam<ProdTransRgInfoErr> params) throws Exception {
		return super.update("UPDATE app_prod_trans_regist_info_erdesc SET bank_code_desc=$S{bankCodeDesc} ,prod_code_desc=$S{prodCodeDesc} ,trans_code_desc=$S{transCodeDesc} ,asset_code_desc=$S{assetCodeDesc} ,cur_desc=$S{curDesc} ,amt_desc=$S{amtDesc} ,convert_rmb_desc=$S{convertRmbDesc} ,quantity_desc=$S{quantityDesc} ,method_asset_measure_desc=$S{methodAssetMeasureDesc} ,cash_type_desc=$S{cashTypeDesc} ,detail_cash_type_desc=$S{detailCashTypeDesc} ,trade_date_desc=$S{tradeDateDesc} ,trade_counter_desc=$S{tradeCounterDesc} ,counter_type_desc=$S{counterTypeDesc} ,unit_price_full_desc=$S{unitPriceFullDesc} ,unit_price_net_desc=$S{unitPriceNetDesc} ,rate_annual_return_desc=$S{rateAnnualReturnDesc} ,trans_ident_code_desc=$S{transIdentCodeDesc} ,details_desc=$S{detailsDesc} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteProdTransRgInfoErr(SqlParam<ProdTransRgInfoErr> params) throws Exception {
		return super.update("DELETE FROM app_prod_trans_regist_info_erdesc WHERE ",
				params.getModel());
	}

}
