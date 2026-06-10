package com.kayak.rpt.zz.historyInfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.historyInfo.dao.ProdRegistFilingInfohDao;
import com.kayak.rpt.zz.historyInfo.model.ProdRegistFilingInfoh;

@Service
@APIDefine(desc = "产品报告登记历史信息服务", model = ProdRegistFilingInfoh.class)
public class ProdRegistFilingInfohService {

	@Autowired
	private ProdRegistFilingInfohDao prodRegistFilingInfohDao;

	@API(desc = "查询产品报告登记历史信息信息", auth = APIAuth.YES)
	public SqlResult<ProdRegistFilingInfoh> findProdRegistFilingInfos(SqlParam<ProdRegistFilingInfoh> params) throws Exception {
		params.setMakeSql(true);
		return prodRegistFilingInfohDao.findProdRegistFilingInfohs(params);
	}

	@API(desc = "添加产品报告登记历史信息", params = "prod_name,ident_code,prod_brand,prod_term_no,bank_code,approver_id_code,designer_id_code,manager_id_code,contact_name,contact_telphone,contact_mobile,contact_email,type_collect,prod_retrun_type,prod_term,fiancial_exclusive,invert_region,invert_country,service_mode,operation_mode,min_hold_period,min_hold_day,option_redempt_period,cash_manager,asset_ac_method,prod_mana_mode,ac_mana_name,price_method,invest_type,cooperate_mode,cooperator,invest_type_ratio,prod_benchmark,risk_level,prod_sales_region,fund_cur,principal_cur,income_cur,invest_threshold,plan_fund_amt,start_date_earliest,start_date_latest,principal_due_date,income_due_date,sales_commission_rate,manage_fee_rate,dc_cd_name,dc_cd_ident_code,seas_cd_nation,seas_cd_name,cd_fee_rate,risk_rate,early_tn_option,invest_rdm_option,prod_crt_enhance,crt_ins_type,prod_crt_method,details,register_serno,imp_date,register_date,register_status,main_doc,feasy_ass_report,inter_audit_doc,due_diligencr_doc,legal_doc_sifned,prod_sale_doc,prod_specifi,prod_mark_doc,other_doc,new_prod", auth = APIAuth.NO)
	public int addProdRegistFilingInfo(SqlParam<ProdRegistFilingInfoh> params) throws Exception {
		return prodRegistFilingInfohDao.addProdRegistFilingInfoh(params).getEffect();
	}
	
	@API(desc = "修改产品报告登记历史信息", params = "prod_name,ident_code,prod_brand,prod_term_no,bank_code,approver_id_code,designer_id_code,manager_id_code,contact_name,contact_telphone,contact_mobile,contact_email,type_collect,prod_retrun_type,prod_term,fiancial_exclusive,invert_region,invert_country,service_mode,operation_mode,min_hold_period,min_hold_day,option_redempt_period,cash_manager,asset_ac_method,prod_mana_mode,ac_mana_name,price_method,invest_type,cooperate_mode,cooperator,invest_type_ratio,prod_benchmark,risk_level,prod_sales_region,fund_cur,principal_cur,income_cur,invest_threshold,plan_fund_amt,start_date_earliest,start_date_latest,principal_due_date,income_due_date,sales_commission_rate,manage_fee_rate,dc_cd_name,dc_cd_ident_code,seas_cd_nation,seas_cd_name,cd_fee_rate,risk_rate,early_tn_option,invest_rdm_option,prod_crt_enhance,crt_ins_type,prod_crt_method,details,register_serno,imp_date,register_date,register_status,main_doc,feasy_ass_report,inter_audit_doc,due_diligencr_doc,legal_doc_sifned,prod_sale_doc,prod_specifi,prod_mark_doc,other_doc,new_prod", auth = APIAuth.NO)
	public int updateProdRegistFilingInfo(SqlParam<ProdRegistFilingInfoh> params) throws Exception {
		return prodRegistFilingInfohDao.updateProdRegistFilingInfoh(params).getEffect();
	}
	
	@API(desc = "删除产品报告登记历史信息", params = "prod_name,ident_code,prod_brand,prod_term_no,bank_code,approver_id_code,designer_id_code,manager_id_code,contact_name,contact_telphone,contact_mobile,contact_email,type_collect,prod_retrun_type,prod_term,fiancial_exclusive,invert_region,invert_country,service_mode,operation_mode,min_hold_period,min_hold_day,option_redempt_period,cash_manager,asset_ac_method,prod_mana_mode,ac_mana_name,price_method,invest_type,cooperate_mode,cooperator,invest_type_ratio,prod_benchmark,risk_level,prod_sales_region,fund_cur,principal_cur,income_cur,invest_threshold,plan_fund_amt,start_date_earliest,start_date_latest,principal_due_date,income_due_date,sales_commission_rate,manage_fee_rate,dc_cd_name,dc_cd_ident_code,seas_cd_nation,seas_cd_name,cd_fee_rate,risk_rate,early_tn_option,invest_rdm_option,prod_crt_enhance,crt_ins_type,prod_crt_method,details,register_serno,imp_date,register_date,register_status,main_doc,feasy_ass_report,inter_audit_doc,due_diligencr_doc,legal_doc_sifned,prod_sale_doc,prod_specifi,prod_mark_doc,other_doc,new_prod", auth = APIAuth.NO)
	public int deleteProdRegistFilingInfo(SqlParam<ProdRegistFilingInfoh> params) throws Exception {
		return prodRegistFilingInfohDao.deleteProdRegistFilingInfoh(params).getEffect();
	}

}
