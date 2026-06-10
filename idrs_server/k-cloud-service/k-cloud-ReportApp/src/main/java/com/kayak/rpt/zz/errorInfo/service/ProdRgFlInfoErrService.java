package com.kayak.rpt.zz.errorInfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.ProdRgFlInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.ProdRgFlInfoErr;

@Service
@APIDefine(desc = "产品报告登记错误信息服务", model = ProdRgFlInfoErr.class)
public class ProdRgFlInfoErrService {

	@Autowired
	private ProdRgFlInfoErrDao prodRgFlInfoErrDao;

	@API(desc = "查询产品报告登记错误信息信息", auth = APIAuth.YES)
	public SqlResult<ProdRgFlInfoErr> findProdRgFlInfoErrs(SqlParam<ProdRgFlInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return prodRgFlInfoErrDao.findProdRgFlInfoErrs(params);
	}

	@API(desc = "添加产品报告登记错误信息", params = "prod_name_desc,ident_code_desc,prod_brand_desc,prod_term_no_desc,bank_code_desc,approver_id_code_desc,designer_id_code_desc,manager_id_code_desc,contact_name_desc,contact_telphone_desc,contact_mobile_desc,contact_email_desc,type_collect_desc,prod_retrun_type_desc,prod_term_desc,fiancial_exclusive_desc,invert_region_desc,invert_country_desc,service_mode_desc,operation_mode_desc,min_hold_period_desc,min_hold_day_desc,option_redempt_period_desc,cash_manager_desc,asset_ac_method_desc,prod_mana_mode_desc,ac_mana_name_desc,price_method_desc,invest_type_desc,cooperate_mode_desc,cooperator_desc,invest_type_ratio_desc,prod_benchmark_desc,prod_sales_region_desc,fund_cur_desc,principal_cur_desc,income_cur_desc,invest_threshold_desc,plan_fund_amt_desc,start_date_earliest_desc,start_date_latest_desc,principal_due_date_desc,income_due_date_desc,sales_commission_rate_desc,manage_fee_rate_desc,dc_cd_name_desc,dc_cd_ident_code_desc,seas_cd_nation_desc,seas_cd_name_desc,cd_fee_rate_desc,risk_level_desc,early_tn_option_desc,invest_rdm_option_desc,prod_crt_enhance_desc,crt_ins_type_desc,prod_crt_method_desc,details_desc,register_serno,imp_date,main_doc_desc,feasy_ass_report_desc,inter_audit_doc_desc,due_diligencr_doc_desc,legal_doc_sifned_desc,prod_sale_doc_desc,prod_specifi_desc,prod_mark_doc_desc,other_doc_desc,risk_rate_desc,new_prod_desc", auth = APIAuth.NO)
	public int addProdRgFlInfoErr(SqlParam<ProdRgFlInfoErr> params) throws Exception {
		return prodRgFlInfoErrDao.addProdRgFlInfoErr(params).getEffect();
	}
	
	@API(desc = "修改产品报告登记错误信息", params = "prod_name_desc,ident_code_desc,prod_brand_desc,prod_term_no_desc,bank_code_desc,approver_id_code_desc,designer_id_code_desc,manager_id_code_desc,contact_name_desc,contact_telphone_desc,contact_mobile_desc,contact_email_desc,type_collect_desc,prod_retrun_type_desc,prod_term_desc,fiancial_exclusive_desc,invert_region_desc,invert_country_desc,service_mode_desc,operation_mode_desc,min_hold_period_desc,min_hold_day_desc,option_redempt_period_desc,cash_manager_desc,asset_ac_method_desc,prod_mana_mode_desc,ac_mana_name_desc,price_method_desc,invest_type_desc,cooperate_mode_desc,cooperator_desc,invest_type_ratio_desc,prod_benchmark_desc,prod_sales_region_desc,fund_cur_desc,principal_cur_desc,income_cur_desc,invest_threshold_desc,plan_fund_amt_desc,start_date_earliest_desc,start_date_latest_desc,principal_due_date_desc,income_due_date_desc,sales_commission_rate_desc,manage_fee_rate_desc,dc_cd_name_desc,dc_cd_ident_code_desc,seas_cd_nation_desc,seas_cd_name_desc,cd_fee_rate_desc,risk_level_desc,early_tn_option_desc,invest_rdm_option_desc,prod_crt_enhance_desc,crt_ins_type_desc,prod_crt_method_desc,details_desc,register_serno,imp_date,main_doc_desc,feasy_ass_report_desc,inter_audit_doc_desc,due_diligencr_doc_desc,legal_doc_sifned_desc,prod_sale_doc_desc,prod_specifi_desc,prod_mark_doc_desc,other_doc_desc,risk_rate_desc,new_prod_desc", auth = APIAuth.NO)
	public int updateProdRgFlInfoErr(SqlParam<ProdRgFlInfoErr> params) throws Exception {
		return prodRgFlInfoErrDao.updateProdRgFlInfoErr(params).getEffect();
	}
	
	@API(desc = "删除产品报告登记错误信息", params = "prod_name_desc,ident_code_desc,prod_brand_desc,prod_term_no_desc,bank_code_desc,approver_id_code_desc,designer_id_code_desc,manager_id_code_desc,contact_name_desc,contact_telphone_desc,contact_mobile_desc,contact_email_desc,type_collect_desc,prod_retrun_type_desc,prod_term_desc,fiancial_exclusive_desc,invert_region_desc,invert_country_desc,service_mode_desc,operation_mode_desc,min_hold_period_desc,min_hold_day_desc,option_redempt_period_desc,cash_manager_desc,asset_ac_method_desc,prod_mana_mode_desc,ac_mana_name_desc,price_method_desc,invest_type_desc,cooperate_mode_desc,cooperator_desc,invest_type_ratio_desc,prod_benchmark_desc,prod_sales_region_desc,fund_cur_desc,principal_cur_desc,income_cur_desc,invest_threshold_desc,plan_fund_amt_desc,start_date_earliest_desc,start_date_latest_desc,principal_due_date_desc,income_due_date_desc,sales_commission_rate_desc,manage_fee_rate_desc,dc_cd_name_desc,dc_cd_ident_code_desc,seas_cd_nation_desc,seas_cd_name_desc,cd_fee_rate_desc,risk_level_desc,early_tn_option_desc,invest_rdm_option_desc,prod_crt_enhance_desc,crt_ins_type_desc,prod_crt_method_desc,details_desc,register_serno,imp_date,main_doc_desc,feasy_ass_report_desc,inter_audit_doc_desc,due_diligencr_doc_desc,legal_doc_sifned_desc,prod_sale_doc_desc,prod_specifi_desc,prod_mark_doc_desc,other_doc_desc,risk_rate_desc,new_prod_desc", auth = APIAuth.NO)
	public int deleteProdRgFlInfoErr(SqlParam<ProdRgFlInfoErr> params) throws Exception {
		return prodRgFlInfoErrDao.deleteProdRgFlInfoErr(params).getEffect();
	}

}
