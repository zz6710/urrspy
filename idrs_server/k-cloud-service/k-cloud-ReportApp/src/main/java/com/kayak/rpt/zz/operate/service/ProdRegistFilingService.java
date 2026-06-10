package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.AppraiseRegistInfo;
import com.kayak.rpt.zz.manage.model.ProdRegistFilingInfo;
import com.kayak.rpt.zz.operate.model.AppraiseRegist;
import com.kayak.rpt.zz.operate.model.ProdIssuanceRegist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.operate.dao.ProdRegistFilingDao;
import com.kayak.rpt.zz.operate.model.ProdRegistFiling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "产品报告登记操作记录服务", model = ProdRegistFiling.class)
public class ProdRegistFilingService {

	@Autowired
	private ProdRegistFilingDao prodRegistFilingDao;

	@API(desc = "查询产品报告登记操作记录信息", auth = APIAuth.YES)
	public SqlResult<ProdRegistFiling> findProdRegistFilings(SqlParam<ProdRegistFiling> params) throws Exception {
		params.setMakeSql(true);
		return prodRegistFilingDao.findProdRegistFilings(params);
	}

	@API(desc = "添加产品报告登记操作记录", params = "prod_name,ident_code,prod_brand,prod_term_no,bank_code,approver_id_code,designer_id_code,manager_id_code,contact_name,contact_telphone,contact_mobile,contact_email,type_collect,prod_retrun_type,prod_term,fiancial_exclusive,invert_region,invert_country,service_mode,operation_mode,min_hold_period,min_hold_day,option_redempt_period,cash_manager,asset_ac_method,prod_mana_mode,ac_mana_name,price_method,invest_type,cooperate_mode,cooperator,invest_type_ratio,prod_benchmark,risk_level,prod_sales_region,fund_cur,principal_cur,income_cur,invest_threshold,plan_fund_amt,start_date_earliest,start_date_latest,principal_due_date,income_due_date,sales_commission_rate,manage_fee_rate,dc_cd_name,dc_cd_ident_code,seas_cd_nation,seas_cd_name,cd_fee_rate,risk_rate,early_tn_option,invest_rdm_option,prod_crt_enhance,crt_ins_type,prod_crt_method,details,register_serno,imp_date,register_date,register_status,main_doc,feasy_ass_report,inter_audit_doc,due_diligencr_doc,legal_doc_sifned,prod_sale_doc,prod_specifi,prod_mark_doc,other_doc,new_prod,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int addOldProdRegistFiling(ProdRegistFilingInfo params, String opType) throws Exception {
		ProdRegistFiling prodRegistFiling = BeanUtil.copyProperties(params, ProdRegistFiling.class);
		prodRegistFiling.setOpType(opType);
		prodRegistFiling.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return prodRegistFilingDao.addProdRegistFiling(prodRegistFiling).getEffect();
	}

	@API(desc = "添加产品报告登记操作记录", params = "prod_name,ident_code,prod_brand,prod_term_no,bank_code,approver_id_code,designer_id_code,manager_id_code,contact_name,contact_telphone,contact_mobile,contact_email,type_collect,prod_retrun_type,prod_term,fiancial_exclusive,invert_region,invert_country,service_mode,operation_mode,min_hold_period,min_hold_day,option_redempt_period,cash_manager,asset_ac_method,prod_mana_mode,ac_mana_name,price_method,invest_type,cooperate_mode,cooperator,invest_type_ratio,prod_benchmark,risk_level,prod_sales_region,fund_cur,principal_cur,income_cur,invest_threshold,plan_fund_amt,start_date_earliest,start_date_latest,principal_due_date,income_due_date,sales_commission_rate,manage_fee_rate,dc_cd_name,dc_cd_ident_code,seas_cd_nation,seas_cd_name,cd_fee_rate,risk_rate,early_tn_option,invest_rdm_option,prod_crt_enhance,crt_ins_type,prod_crt_method,details,register_serno,imp_date,register_date,register_status,main_doc,feasy_ass_report,inter_audit_doc,due_diligencr_doc,legal_doc_sifned,prod_sale_doc,prod_specifi,prod_mark_doc,other_doc,new_prod,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int addProdRegistFiling(SqlParam<ProdRegistFilingInfo> params, String opType) throws Exception {
		ProdRegistFiling prodRegistFiling = BeanUtil.copyProperties(params.getModel(), ProdRegistFiling.class);
		prodRegistFiling.setOpType(opType);
		prodRegistFiling.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return prodRegistFilingDao.addProdRegistFiling(prodRegistFiling).getEffect();
	}



	@API(desc = "修改产品报告登记操作记录", params = "prod_name,ident_code,prod_brand,prod_term_no,bank_code,approver_id_code,designer_id_code,manager_id_code,contact_name,contact_telphone,contact_mobile,contact_email,type_collect,prod_retrun_type,prod_term,fiancial_exclusive,invert_region,invert_country,service_mode,operation_mode,min_hold_period,min_hold_day,option_redempt_period,cash_manager,asset_ac_method,prod_mana_mode,ac_mana_name,price_method,invest_type,cooperate_mode,cooperator,invest_type_ratio,prod_benchmark,risk_level,prod_sales_region,fund_cur,principal_cur,income_cur,invest_threshold,plan_fund_amt,start_date_earliest,start_date_latest,principal_due_date,income_due_date,sales_commission_rate,manage_fee_rate,dc_cd_name,dc_cd_ident_code,seas_cd_nation,seas_cd_name,cd_fee_rate,risk_rate,early_tn_option,invest_rdm_option,prod_crt_enhance,crt_ins_type,prod_crt_method,details,register_serno,imp_date,register_date,register_status,main_doc,feasy_ass_report,inter_audit_doc,due_diligencr_doc,legal_doc_sifned,prod_sale_doc,prod_specifi,prod_mark_doc,other_doc,new_prod,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int updateProdRegistFiling(SqlParam<ProdRegistFiling> params) throws Exception {
		return prodRegistFilingDao.updateProdRegistFiling(params).getEffect();
	}
	
	@API(desc = "删除产品报告登记操作记录", params = "prod_name,ident_code,prod_brand,prod_term_no,bank_code,approver_id_code,designer_id_code,manager_id_code,contact_name,contact_telphone,contact_mobile,contact_email,type_collect,prod_retrun_type,prod_term,fiancial_exclusive,invert_region,invert_country,service_mode,operation_mode,min_hold_period,min_hold_day,option_redempt_period,cash_manager,asset_ac_method,prod_mana_mode,ac_mana_name,price_method,invest_type,cooperate_mode,cooperator,invest_type_ratio,prod_benchmark,risk_level,prod_sales_region,fund_cur,principal_cur,income_cur,invest_threshold,plan_fund_amt,start_date_earliest,start_date_latest,principal_due_date,income_due_date,sales_commission_rate,manage_fee_rate,dc_cd_name,dc_cd_ident_code,seas_cd_nation,seas_cd_name,cd_fee_rate,risk_rate,early_tn_option,invest_rdm_option,prod_crt_enhance,crt_ins_type,prod_crt_method,details,register_serno,imp_date,register_date,register_status,main_doc,feasy_ass_report,inter_audit_doc,due_diligencr_doc,legal_doc_sifned,prod_sale_doc,prod_specifi,prod_mark_doc,other_doc,new_prod,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int deleteProdRegistFiling(SqlParam<ProdRegistFiling> params) throws Exception {
		return prodRegistFilingDao.deleteProdRegistFiling(params).getEffect();
	}

	@API(desc = "添加产品报告登记要素导入操作记录", auth = APIAuth.NO)
	public int addImportProdRegistFiling(ProdRegistFilingInfo prodRegistFilingInfo, String opType) throws Exception {
		ProdRegistFiling prodRegistFiling = BeanUtil.copyProperties(prodRegistFilingInfo, ProdRegistFiling.class);
		prodRegistFiling.setOpType(opType);
		prodRegistFiling.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return prodRegistFilingDao.addImportProdRegistFiling(prodRegistFiling).getEffect();
	}

	@API(desc = "请求产品品牌的字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> loadProdBrandDict(SqlParam<ProdRegistFiling> param) throws Exception {
		Map<String, Object> params = new HashMap<>();
		List<SqlRow> tempTypeByDocType = prodRegistFilingDao.loadProdBrandDict(params);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

}
