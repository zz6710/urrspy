package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.ProdRgFlInfoErr;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ProdRgFlInfoErrDao extends ComnDao {

	public SqlResult<ProdRgFlInfoErr> findProdRgFlInfoErrs(SqlParam<ProdRgFlInfoErr> params) throws Exception {
		String sql = "SELECT PROD_NAME_DESC, IDENT_CODE_DESC, PROD_BRAND_DESC, PROD_TERM_NO_DESC, BANK_CODE_DESC, APPROVER_ID_CODE_DESC, " +
				" DESIGNER_ID_CODE_DESC, MANAGER_ID_CODE_DESC, CONTACT_NAME_DESC, CONTACT_TELPHONE_DESC, CONTACT_MOBILE_DESC, " +
				"CONTACT_EMAIL_DESC, TYPE_COLLECT_DESC, PROD_RETRUN_TYPE_DESC, PROD_TERM_DESC, FIANCIAL_EXCLUSIVE_DESC, " +
				"INVERT_REGION_DESC, INVERT_COUNTRY_DESC, SERVICE_MODE_DESC, OPERATION_MODE_DESC, MIN_HOLD_PERIOD_DESC, " +
				"MIN_HOLD_DAY_DESC, OPTION_REDEMPT_PERIOD_DESC, CASH_MANAGER_DESC, ASSET_AC_METHOD_DESC, PROD_MANA_MODE_DESC, " +
				"AC_MANA_NAME_DESC, PRICE_METHOD_DESC, INVEST_TYPE_DESC, COOPERATE_MODE_DESC, COOPERATOR_DESC, INVEST_TYPE_RATIO_DESC, " +
				"PROD_BENCHMARK_DESC, PROD_SALES_REGION_DESC, FUND_CUR_DESC, PRINCIPAL_CUR_DESC, INCOME_CUR_DESC, " +
				"INVEST_THRESHOLD_DESC, PLAN_FUND_AMT_DESC, START_DATE_EARLIEST_DESC, START_DATE_LATEST_DESC, " +
				"PRINCIPAL_DUE_DATE_DESC, INCOME_DUE_DATE_DESC, SALES_COMMISSION_RATE_DESC, MANAGE_FEE_RATE_DESC, DC_CD_NAME_DESC, " +
				"DC_CD_IDENT_CODE_DESC, SEAS_CD_NATION_DESC, SEAS_CD_NAME_DESC, CD_FEE_RATE_DESC, RISK_LEVEL_DESC, EARLY_TN_OPTION_DESC, " +
				"INVEST_RDM_OPTION_DESC, PROD_CRT_ENHANCE_DESC, CRT_INS_TYPE_DESC, PROD_CRT_METHOD_DESC, DETAILS_DESC, REGISTER_SERNO, " +
				"IMP_DATE, MAIN_DOC_DESC, FEASY_ASS_REPORT_DESC, INTER_AUDIT_DOC_DESC, DUE_DILIGENCR_DOC_DESC, LEGAL_DOC_SIFNED_DESC, " +
				"PROD_SALE_DOC_DESC, PROD_SPECIFI_DESC, PROD_MARK_DOC_DESC, OTHER_DOC_DESC, RISK_RATE_DESC, NEW_PROD_DESC, create_date, " +
				"theory_report_start_date, theory_report_end_date, report_date FROM app_prod_regist_filing_info_erdesc where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}

		return super.findRows(sql, params);
	}

	public UpdateResult addProdRgFlInfoErr(SqlParam<ProdRgFlInfoErr> params) throws Exception {
		return super.update("INSERT INTO app_prod_regist_filing_info_erdesc(prod_name_desc,ident_code_desc,prod_brand_desc,prod_term_no_desc,bank_code_desc,approver_id_code_desc,designer_id_code_desc,manager_id_code_desc,contact_name_desc,contact_telphone_desc,contact_mobile_desc,contact_email_desc,type_collect_desc,prod_retrun_type_desc,prod_term_desc,fiancial_exclusive_desc,invert_region_desc,invert_country_desc,service_mode_desc,operation_mode_desc,min_hold_period_desc,min_hold_day_desc,option_redempt_period_desc,cash_manager_desc,asset_ac_method_desc,prod_mana_mode_desc,ac_mana_name_desc,price_method_desc,invest_type_desc,cooperate_mode_desc,cooperator_desc,invest_type_ratio_desc,prod_benchmark_desc,prod_sales_region_desc,fund_cur_desc,principal_cur_desc,income_cur_desc,invest_threshold_desc,plan_fund_amt_desc,start_date_earliest_desc,start_date_latest_desc,principal_due_date_desc,income_due_date_desc,sales_commission_rate_desc,manage_fee_rate_desc,dc_cd_name_desc,dc_cd_ident_code_desc,seas_cd_nation_desc,seas_cd_name_desc,cd_fee_rate_desc,risk_level_desc,early_tn_option_desc,invest_rdm_option_desc,prod_crt_enhance_desc,crt_ins_type_desc,prod_crt_method_desc,details_desc,register_serno,imp_date,main_doc_desc,feasy_ass_report_desc,inter_audit_doc_desc,due_diligencr_doc_desc,legal_doc_sifned_desc,prod_sale_doc_desc,prod_specifi_desc,prod_mark_doc_desc,other_doc_desc,risk_rate_desc,new_prod_desc) VALUES($S{prodNameDesc},$S{identCodeDesc},$S{prodBrandDesc},$S{prodTermNoDesc},$S{bankCodeDesc},$S{approverIdCodeDesc},$S{designerIdCodeDesc},$S{managerIdCodeDesc},$S{contactNameDesc},$S{contactTelphoneDesc},$S{contactMobileDesc},$S{contactEmailDesc},$S{typeCollectDesc},$S{prodRetrunTypeDesc},$S{prodTermDesc},$S{fiancialExclusiveDesc},$S{invertRegionDesc},$S{invertCountryDesc},$S{serviceModeDesc},$S{operationModeDesc},$S{minHoldPeriodDesc},$S{minHoldDayDesc},$S{optionRedemptPeriodDesc},$S{cashManagerDesc},$S{assetAcMethodDesc},$S{prodManaModeDesc},$S{acManaNameDesc},$S{priceMethodDesc},$S{investTypeDesc},$S{cooperateModeDesc},$S{cooperatorDesc},$S{investTypeRatioDesc},$S{prodBenchmarkDesc},$S{prodSalesRegionDesc},$S{fundCurDesc},$S{principalCurDesc},$S{incomeCurDesc},$S{investThresholdDesc},$S{planFundAmtDesc},$S{startDateEarliestDesc},$S{startDateLatestDesc},$S{principalDueDateDesc},$S{incomeDueDateDesc},$S{salesCommissionRateDesc},$S{manageFeeRateDesc},$S{dcCdNameDesc},$S{dcCdIdentCodeDesc},$S{seasCdNationDesc},$S{seasCdNameDesc},$S{cdFeeRateDesc},$S{riskLevelDesc},$S{earlyTnOptionDesc},$S{investRdmOptionDesc},$S{prodCrtEnhanceDesc},$S{crtInsTypeDesc},$S{prodCrtMethodDesc},$S{detailsDesc},$S{registerSerno},$S{impDate},$S{mainDocDesc},$S{feasyAssReportDesc},$S{interAuditDocDesc},$S{dueDiligencrDocDesc},$S{legalDocSifnedDesc},$S{prodSaleDocDesc},$S{prodSpecifiDesc},$S{prodMarkDocDesc},$S{otherDocDesc},$S{riskRateDesc},$S{newProdDesc})",
				params.getModel());
	}
	
	public UpdateResult updateProdRgFlInfoErr(SqlParam<ProdRgFlInfoErr> params) throws Exception {
		return super.update("UPDATE app_prod_regist_filing_info_erdesc SET prod_name_desc=$S{prodNameDesc} ,ident_code_desc=$S{identCodeDesc} ,prod_brand_desc=$S{prodBrandDesc} ,prod_term_no_desc=$S{prodTermNoDesc} ,bank_code_desc=$S{bankCodeDesc} ,approver_id_code_desc=$S{approverIdCodeDesc} ,designer_id_code_desc=$S{designerIdCodeDesc} ,manager_id_code_desc=$S{managerIdCodeDesc} ,contact_name_desc=$S{contactNameDesc} ,contact_telphone_desc=$S{contactTelphoneDesc} ,contact_mobile_desc=$S{contactMobileDesc} ,contact_email_desc=$S{contactEmailDesc} ,type_collect_desc=$S{typeCollectDesc} ,prod_retrun_type_desc=$S{prodRetrunTypeDesc} ,prod_term_desc=$S{prodTermDesc} ,fiancial_exclusive_desc=$S{fiancialExclusiveDesc} ,invert_region_desc=$S{invertRegionDesc} ,invert_country_desc=$S{invertCountryDesc} ,service_mode_desc=$S{serviceModeDesc} ,operation_mode_desc=$S{operationModeDesc} ,min_hold_period_desc=$S{minHoldPeriodDesc} ,min_hold_day_desc=$S{minHoldDayDesc} ,option_redempt_period_desc=$S{optionRedemptPeriodDesc} ,cash_manager_desc=$S{cashManagerDesc} ,asset_ac_method_desc=$S{assetAcMethodDesc} ,prod_mana_mode_desc=$S{prodManaModeDesc} ,ac_mana_name_desc=$S{acManaNameDesc} ,price_method_desc=$S{priceMethodDesc} ,invest_type_desc=$S{investTypeDesc} ,cooperate_mode_desc=$S{cooperateModeDesc} ,cooperator_desc=$S{cooperatorDesc} ,invest_type_ratio_desc=$S{investTypeRatioDesc} ,prod_benchmark_desc=$S{prodBenchmarkDesc} ,prod_sales_region_desc=$S{prodSalesRegionDesc} ,fund_cur_desc=$S{fundCurDesc} ,principal_cur_desc=$S{principalCurDesc} ,income_cur_desc=$S{incomeCurDesc} ,invest_threshold_desc=$S{investThresholdDesc} ,plan_fund_amt_desc=$S{planFundAmtDesc} ,start_date_earliest_desc=$S{startDateEarliestDesc} ,start_date_latest_desc=$S{startDateLatestDesc} ,principal_due_date_desc=$S{principalDueDateDesc} ,income_due_date_desc=$S{incomeDueDateDesc} ,sales_commission_rate_desc=$S{salesCommissionRateDesc} ,manage_fee_rate_desc=$S{manageFeeRateDesc} ,dc_cd_name_desc=$S{dcCdNameDesc} ,dc_cd_ident_code_desc=$S{dcCdIdentCodeDesc} ,seas_cd_nation_desc=$S{seasCdNationDesc} ,seas_cd_name_desc=$S{seasCdNameDesc} ,cd_fee_rate_desc=$S{cdFeeRateDesc} ,risk_level_desc=$S{riskLevelDesc} ,early_tn_option_desc=$S{earlyTnOptionDesc} ,invest_rdm_option_desc=$S{investRdmOptionDesc} ,prod_crt_enhance_desc=$S{prodCrtEnhanceDesc} ,crt_ins_type_desc=$S{crtInsTypeDesc} ,prod_crt_method_desc=$S{prodCrtMethodDesc} ,details_desc=$S{detailsDesc} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,main_doc_desc=$S{mainDocDesc} ,feasy_ass_report_desc=$S{feasyAssReportDesc} ,inter_audit_doc_desc=$S{interAuditDocDesc} ,due_diligencr_doc_desc=$S{dueDiligencrDocDesc} ,legal_doc_sifned_desc=$S{legalDocSifnedDesc} ,prod_sale_doc_desc=$S{prodSaleDocDesc} ,prod_specifi_desc=$S{prodSpecifiDesc} ,prod_mark_doc_desc=$S{prodMarkDocDesc} ,other_doc_desc=$S{otherDocDesc} ,risk_rate_desc=$S{riskRateDesc} ,new_prod_desc=$S{newProdDesc}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteProdRgFlInfoErr(SqlParam<ProdRgFlInfoErr> params) throws Exception {
		return super.update("DELETE FROM app_prod_regist_filing_info_erdesc WHERE ",
				params.getModel());
	}

}
