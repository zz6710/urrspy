package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.ProdIssuanceRegistInfo;
import com.kayak.rpt.zz.manage.model.ProdRegistFilingInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProdRegistFilingInfoDao extends ComnDao {

	public SqlResult<ProdRegistFilingInfo> findProdRegistFilingInfos(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		String sql = "SELECT t.id,t.create_date,t.prod_name,t.ident_code,t.prod_brand,t.prod_term_no,t.bank_code,t.PROD_APRV_NM,t.approver_id_code,t.PROD_DSN_NM,t.designer_id_code,t.INV_MNG_NM,t.manager_id_code," +
				"t.contact_name,t.contact_telphone,t.contact_mobile,t.contact_email,t.type_collect,t.prod_retrun_type,t.prod_term,t.fiancial_exclusive,t.invert_region,t.invert_country,t.service_mode,t.operation_mode," +
				"t.min_hold_period,t.min_hold_day,t.option_redempt_period,t.cash_manager,t.asset_ac_method,t.prod_mana_mode,t.ac_mana_name,t.price_method,t.invest_type,t.cooperate_mode,t.cooperator,t.invest_type_ratio," +
				"t.prod_benchmark,t.risk_level,t.prod_sales_region,t.fund_cur,t.principal_cur,t.income_cur,t.invest_threshold,t.plan_fund_amt,t.start_date_earliest,t.start_date_latest,t.principal_due_date,t.income_due_date," +
				"t.sales_commission_rate,t.manage_fee_rate,t.dc_cd_name,t.dc_cd_ident_code,t.seas_cd_nation,t.seas_cd_name,t.cd_fee_rate,t.risk_rate,t.early_tn_option,t.invest_rdm_option,t.prod_crt_enhance,t.crt_ins_type," +
				"t.prod_crt_method,t.details,t.register_serno,t.imp_date,t.register_date,t.register_status,t.main_doc,t.feasy_ass_report,t.inter_audit_doc,t.due_diligencr_doc,t.legal_doc_sifned,t.prod_sale_doc," +
				"t.prod_specifi,t.prod_mark_doc,t.other_doc,t.new_prod,ifnull(ARS.audit_status,0) audit_status,prod_esp_prpt,t.sys_data_version " +
				"FROM app_prod_regist_filing_info t\n" +
				"LEFT JOIN base_report_data_audit_results ARS ON t.report_date=ARS.report_date and ARS.table_id = 'app_prod_regist_filing_info' where t.sys_data_status ='1' \n";
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql = sql + " and  t.create_date >= '" + params.getModel().getBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql = sql + " and  t.create_date <= '" + params.getModel().getQueryDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdName())) {
			sql = sql + " and  t.prod_name like '%" + params.getModel().getProdName() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getIdentCode())) {
			sql = sql + " and  t.ident_code like '%" + params.getModel().getIdentCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  t.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}

		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  t.register_serno like '%" + params.getModel().getRegisterSerno() + "%'";
		}

		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public int findProdRegistFilingInfosCount(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		String sql = "SELECT count(1) FROM app_prod_regist_filing_info t\n" +
				"LEFT JOIN base_report_data_audit_results ARS ON t.report_date=ARS.report_date and ARS.table_id = 'app_prod_regist_filing_info' where t.sys_data_status ='1' \n";
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql = sql + " and  t.create_date >= '" + params.getModel().getBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql = sql + " and  t.create_date <= '" + params.getModel().getQueryDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdName())) {
			sql = sql + " and  t.prod_name like '%" + params.getModel().getProdName() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getIdentCode())) {
			sql = sql + " and  t.ident_code like '%" + params.getModel().getIdentCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  t.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}

		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  t.register_serno like '%" + params.getModel().getRegisterSerno() + "%'";
		}

		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public SqlResult<ProdRegistFilingInfo> findProdIssuanceRegistInfosByprod(String prods, SqlParam<ProdRegistFilingInfo> params) throws Exception {
		params.setStart(0);
		params.setLimit(2000);

		String sql = "SELECT t.id,t.create_date,t.prod_name,t.ident_code,t.prod_brand,t.prod_term_no,t.bank_code,t.prod_aprv_nm,t.approver_id_code,t.prod_dsn_nm,t.designer_id_code,t.inv_mng_nm,t.manager_id_code," +
				"t.contact_name,t.contact_telphone,t.contact_mobile,t.contact_email,t.type_collect,t.prod_retrun_type,t.prod_term,t.fiancial_exclusive,t.invert_region,t.invert_country,t.service_mode,t.operation_mode," +
				"t.min_hold_period,t.min_hold_day,t.option_redempt_period,t.cash_manager,t.asset_ac_method,t.prod_mana_mode,t.ac_mana_name,t.price_method,t.invest_type,t.cooperate_mode,t.cooperator,t.invest_type_ratio," +
				"t.prod_benchmark,t.risk_level,t.prod_sales_region,t.fund_cur,t.principal_cur,t.income_cur,t.invest_threshold,t.plan_fund_amt,t.start_date_earliest,t.start_date_latest,t.principal_due_date,t.income_due_date," +
				"t.sales_commission_rate,t.manage_fee_rate,t.dc_cd_name,t.dc_cd_ident_code,t.seas_cd_nation,t.seas_cd_name,t.cd_fee_rate,t.risk_rate,t.early_tn_option,t.invest_rdm_option,t.prod_crt_enhance,t.crt_ins_type," +
				"t.prod_crt_method,t.details,t.register_serno,t.imp_date,t.register_date,t.register_status,t.main_doc,t.feasy_ass_report,t.inter_audit_doc,t.due_diligencr_doc,t.legal_doc_sifned,t.prod_sale_doc," +
				"t.prod_specifi,t.prod_mark_doc,t.other_doc,t.new_prod,ARS.audit_status,prod_esp_prpt,t.sys_data_version " +
				"FROM app_prod_regist_filing_info t " +
				"LEFT JOIN base_report_data_audit_results ARS ON ARS.table_id = 'app_prod_regist_filing_info' " +
				" where t.sys_data_status ='1'  ";
		if(StringUtils.isNotBlank(prods)){
			sql = sql + " and  t.ident_code in (" + prods + ")";
		}
		SqlResult<ProdRegistFilingInfo> r1 = super.findRows(sql, DataSourceProperty.PUB, params);
		return r1;
	}

	public int findProdRegistFilingInfoFailStatus(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		String sql = "SELECT count(1) FROM app_prod_regist_filing_info t\n" +
				"LEFT JOIN base_report_data_audit_results ARS ON ARS.table_id = 'app_prod_regist_filing_info' where sys_data_status ='1'  " +
				"and t.register_status in (0,1) \n";
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql = sql + " and  t.create_date >= '" + params.getModel().getBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql = sql + " and  t.create_date <= '" + params.getModel().getQueryDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdName())) {
			sql = sql + " and  t.prod_name like '%" + params.getModel().getProdName() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getIdentCode())) {
			sql = sql + " and  t.ident_code like '%" + params.getModel().getIdentCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  t.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  t.register_serno like '%" + params.getModel().getRegisterSerno() + "%'";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public UpdateResult addProdRegistFilingInfo(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		return super.update("INSERT INTO app_prod_regist_filing_info(prod_name,ident_code,prod_brand,prod_term_no,bank_code,approver_id_code,designer_id_code,manager_id_code,contact_name,contact_telphone,contact_mobile,contact_email,type_collect,prod_retrun_type,prod_term,fiancial_exclusive,invert_region,invert_country,service_mode,operation_mode,min_hold_period,min_hold_day,option_redempt_period,cash_manager,asset_ac_method,prod_mana_mode,ac_mana_name,price_method,invest_type,cooperate_mode,cooperator,invest_type_ratio,prod_benchmark,risk_level,prod_sales_region,fund_cur,principal_cur,income_cur,invest_threshold,plan_fund_amt,start_date_earliest,start_date_latest,principal_due_date,income_due_date,sales_commission_rate,manage_fee_rate,dc_cd_name,dc_cd_ident_code,seas_cd_nation,seas_cd_name,cd_fee_rate,risk_rate,early_tn_option,invest_rdm_option,prod_crt_enhance,crt_ins_type,prod_crt_method,details,register_serno,imp_date,register_date,register_status,main_doc,feasy_ass_report,inter_audit_doc,due_diligencr_doc,legal_doc_sifned,prod_sale_doc,prod_specifi,prod_mark_doc,other_doc,new_prod,found_dt,prod_esp_prpt,PROD_APRV_NM,PROD_DSN_NM,INV_MNG_NM,create_date) VALUES($S{prodName},$S{identCode},$S{prodBrand},$S{prodTermNo},$S{bankCode},$S{approverIdCode},$S{designerIdCode},$S{managerIdCode},$S{contactName},$S{contactTelphone},$S{contactMobile},$S{contactEmail},$S{typeCollect},$S{prodRetrunType},$S{prodTerm},$S{fiancialExclusive},$S{invertRegion},$S{invertCountry},$S{serviceMode},$S{operationMode},$S{minHoldPeriod},$S{minHoldDay},$S{optionRedemptPeriod},$S{cashManager},$S{assetAcMethod},$S{prodManaMode},$S{acManaName},$S{priceMethod},$S{investType},$S{cooperateMode},$S{cooperator},$S{investTypeRatio},$D{prodBenchmark},$S{riskLevel},$S{prodSalesRegion},$S{fundCur},$S{principalCur},$S{incomeCur},$D{investThreshold},$D{planFundAmt},$S{startDateEarliest},$S{startDateLatest},$S{principalDueDate},$S{incomeDueDate},$D{salesCommissionRate},$D{manageFeeRate},$S{dcCdName},$S{dcCdIdentCode},$S{seasCdNation},$S{seasCdName},$D{cdFeeRate},$S{riskRate},$S{earlyTnOption},$S{investRdmOption},$S{prodCrtEnhance},$S{crtInsType},$S{prodCrtMethod},$S{details},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',$S{mainDoc},$S{feasyAssReport},$S{interAuditDoc},$S{dueDiligencrDoc},$S{legalDocSifned},$S{prodSaleDoc},$S{prodSpecifi},$S{prodMarkDoc},$S{otherDoc},$S{newProd},$S{foundDt},$S{prodEspPrpt},$S{prodAprvNm},$S{prodDsnNm},$S{invMngNm},date_format(CURDATE(),'%Y%m%d'))",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult updateProdRegistFilingInfo(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		String sql = "UPDATE app_prod_regist_filing_info SET prod_name=$S{prodName} ,prod_brand=$S{prodBrand} ,prod_term_no=$S{prodTermNo} ," +
				"bank_code=$S{bankCode}, contact_name=$S{contactName} ,contact_telphone=$S{contactTelphone} ,contact_mobile=$S{contactMobile} ," +
				"contact_email=$S{contactEmail} ,type_collect=$S{typeCollect} ,prod_retrun_type=$S{prodRetrunType} ,prod_term=$S{prodTerm} ," +
				"fiancial_exclusive=$S{fiancialExclusive} ,invert_region=$S{invertRegion} ,invert_country=$S{invertCountry} ,service_mode=$S{serviceMode} ," +
				"operation_mode=$S{operationMode} ,min_hold_period=$S{minHoldPeriod} ,min_hold_day=$S{minHoldDay} ," +
				"option_redempt_period=$S{optionRedemptPeriod} ,cash_manager=$S{cashManager} ,asset_ac_method=$S{assetAcMethod} ,prod_mana_mode=$S{prodManaMode} ," +
				"ac_mana_name=$S{acManaName} ,price_method=$S{priceMethod} ,invest_type=$S{investType} ,cooperate_mode=$S{cooperateMode} ," +
				"cooperator=$S{cooperator} ,invest_type_ratio=$S{investTypeRatio} ,prod_benchmark=$D{prodBenchmark} ,risk_level=$S{riskLevel} ," +
				"prod_sales_region=$S{prodSalesRegion} ,fund_cur=$S{fundCur} ,principal_cur=$S{principalCur} ,income_cur=$S{incomeCur} ," +
				"invest_threshold=$D{investThreshold} ,plan_fund_amt=$D{planFundAmt} ,start_date_earliest=$S{startDateEarliest} ," +
				"start_date_latest=$S{startDateLatest} ,principal_due_date=$S{principalDueDate} ,income_due_date=$S{incomeDueDate} ," +
				"sales_commission_rate=$D{salesCommissionRate} ,manage_fee_rate=$D{manageFeeRate} ,dc_cd_name=$S{dcCdName} ," +
				"dc_cd_ident_code=$S{dcCdIdentCode} ,seas_cd_nation=$S{seasCdNation} ,seas_cd_name=$S{seasCdName} ,cd_fee_rate=$D{cdFeeRate} ," +
				"risk_rate=$S{riskRate} ,early_tn_option=$S{earlyTnOption} ,invest_rdm_option=$S{investRdmOption} ,prod_crt_enhance=$S{prodCrtEnhance} ," +
				"crt_ins_type=$S{crtInsType} ,prod_crt_method=$S{prodCrtMethod} ,details=$S{details} ,imp_date=$S{impDate} ," +
				"main_doc=$S{mainDoc} ,feasy_ass_report=$S{feasyAssReport} ,inter_audit_doc=$S{interAuditDoc} ,due_diligencr_doc=$S{dueDiligencrDoc} ," +
				"legal_doc_sifned=$S{legalDocSifned} ,prod_sale_doc=$S{prodSaleDoc} ,prod_specifi=$S{prodSpecifi} ,prod_mark_doc=$S{prodMarkDoc} ," +
				"other_doc=$S{otherDoc} ,new_prod=$S{newProd},found_dt= $S{foundDt},prod_esp_prpt= $S{prodEspPrpt}, prod_aprv_nm =$S{prodAprvNm}," +
				"prod_dsn_nm=$S{prodDsnNm},inv_mng_nm=$S{invMngNm}," +
				"approver_id_code = $S{approverIdCode},designer_id_code=$S{designerIdCode},manager_id_code=$S{managerIdCode} ";

		sql += " WHERE register_serno=$S{registerSerno} ";

		return super.update(sql, DataSourceProperty.PUB,params.getModel());
	}
	public String getProdCode (SqlParam<ProdRegistFilingInfo> params) throws Exception {
		StringBuffer ss = new StringBuffer();
		String sql = "select distinct t.IDENT_CODE as prod_code  from app_prod_regist_filing_info t where create_date>='" +params.getModel().getBeginDate()+
				"' and create_date<='" +params.getModel().getQueryDate() +"'";

		if (StringUtils.isNotBlank(params.getModel().getProdName())) {
			sql = sql + " and  t.prod_name like '%" + params.getModel().getProdName() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getIdentCode())) {
			sql = sql + " and  t.ident_code like '%" + params.getModel().getIdentCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  t.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		List<SqlRow> list =  super.findRows(sql,
				DataSourceProperty.PUB,params.getModel());
		if (list != null && list.size() >0 ){
			for(int i = 0;i<list.size();i++){
				if(i == list.size()-1){
					ss.append("'"+list.get(i).get("prod_code")+"'");
				}else{
					ss.append("'"+list.get(i).get("prod_code")+"',");
				}
			}
		}
		return ss.toString();
	}

	public UpdateResult updateProdRegistFilingInfoStatus(SqlParam<ProdRegistFilingInfo> params,String prod_code) throws Exception {
		String sql = "UPDATE app_prod_regist_filing_info t SET register_status='3' WHERE sys_data_status='1' ";
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql = sql + " and  t.create_date >= '" + params.getModel().getBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql = sql + " and  t.create_date <= '" + params.getModel().getQueryDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdName())) {
			sql = sql + " and  t.prod_name like '%" + params.getModel().getProdName() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getIdentCode())) {
			sql = sql + " and  t.ident_code like '%" + params.getModel().getIdentCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  t.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}

		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  t.register_serno like '%" + params.getModel().getRegisterSerno() + "%'";
		}
		if(StringUtils.isNotBlank(prod_code)){
			sql  = sql + " and t.IDENT_CODE in ("+prod_code+")";
		}
		return super.update(sql, DataSourceProperty.PUB);
	}

	public UpdateResult updateProdStat(SqlParam<ProdRegistFilingInfo> params,String prod_code) throws Exception {
		String sql = "UPDATE ods_prod_base_info SET PROD_STATUS='01'";
		sql += " WHERE mother_fund_code  in (select a.IDENT_CODE  from app_prod_regist_filing_info  a where create_date>='" +params.getModel().getBeginDate()+
				"' and create_date<='" +params.getModel().getQueryDate() +"')"
		+" and  PROD_STATUS = '00' ";

		if(StringUtils.isNotBlank(prod_code)){
			sql  = sql + " and prod_code in ("+prod_code+")";
		}

		return super.update(sql, DataSourceProperty.PUB,params);
	}



	public void updateProdDataFlag(SqlParam<ProdRegistFilingInfo> params,String prod_code) throws Exception {
		String create_date = "";
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			create_date = params.getModel().getBeginDate();
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			create_date = params.getModel().getQueryDate();
		}

//		String sql1 = "UPDATE ods_prod_base_info set data_flag_old=data_flag " +
//				" where upd_dt = date_format(now(),'%Y%m%d') and data_flag_old is null ";
		String sql2 = "UPDATE ods_prod_base_info set data_flag_old=data_flag,data_flag=trim(both ',' from replace(concat(data_flag,','),'0,','')) " +
				" where upd_dt = date_format(now(),'%Y%m%d') " +
				" and exists (select 1 from app_prod_regist_filing_info t1 where t1.ident_code = ods_prod_base_info.prod_code " +
				" and t1.create_date = upd_dt and register_status != '3' and report_date  = date_format(now(),'%Y%m%d') )";
		if(StringUtils.isNotBlank(prod_code)){
			sql2  = sql2 + " and prod_code in ("+prod_code+")";
		}
//		super.update(sql1, DataSourceProperty.PUB,params);
		super.update(sql2, DataSourceProperty.PUB, params);
	}

	
	public UpdateResult deleteProdRegistFilingInfo(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		return super.update("DELETE FROM app_prod_regist_filing_info WHERE  register_serno=$S{registerSerno} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public SqlResult<ProdRegistFilingInfo> findProdRegistFilingInfosById(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		String sql = "select \n" +
				" T1.PROD_TMS prod_term_no\n" +
				",T1.PROD_APRV_NM PROD_APRV_NM\n" +
				",T1.PROD_APRV_ID approver_id_code\n" +
				",T1.PROD_DSN_NM PROD_DSN_NM\n" +
				",T1.PROD_DSN_ID designer_id_code\n" +
				",T1.INV_MNG_NM INV_MNG_NM\n" +
				",T1.INV_MNG_ID manager_id_code\n" +
				",T1.BUS_CNTC_NM contact_name\n" +
				",T1.BUS_CNTC_FIX_NBR contact_telphone\n" +
				",T1.BUS_CNTC_TELNOMBL_NBR contact_mobile\n" +
				",T1.BUS_CNTC_EML contact_email\n" +
				",T1.NEW_OLD_PROD_F new_prod\n" +
				",T1.FND_DIR_ZON invert_region\n" +
				",T1.INV_PROD_BLG_CNR_NM invert_country\n" +
				",T1.BUS_SRV_MOD service_mode\n" +
				",T1.SHO_HLD_TRM_F min_hold_period\n" +
				",T1.SHO_HLD_TRM min_hold_day\n" +
				",T1.SHO_HLD_TRM_AFT_RDM_F option_redempt_period\n" +
				",T1.PROD_AST_CFGU_MTH asset_ac_method\n" +
				",T1.PROD_MNG_MOD prod_mana_mode\n" +
				",T1.ACTL_MNG_NM ac_mana_name\n" +
				",T1.PROD_PRC_MTH price_method\n" +
				",T1.CPER_MOD cooperate_mode\n" +
				",T1.CPER_ORG_NM cooperator\n" +
				",T1.INV_AST_TYP_AND_RTO invest_type_ratio\n" +
				",T1.PROD_SAL_ZON prod_sales_region\n" +
				",T1.CALL_PRCP_CCY principal_cur\n" +
				",T1.CALL_ERN_CCY income_cur\n" +
				",T1.CLC_BGN_DT start_date_earliest\n" +
				",T1.CLC_END_DT start_date_latest\n" +
				",T1.INV_PRCP_TO_ACT_DT principal_due_date\n" +
				",T1.INV_ERN_TO_ACT_DT income_due_date\n" +
				",T1.DMS_TRST_ORG_NM dc_cd_name\n" +
				",T1.DMS_TRST_ORG_CD dc_cd_ident_code\n" +
				",T1.OVS_TRST_ORG_CNR seas_cd_nation\n" +
				",T1.OVS_TRST_ORG_NM seas_cd_name\n" +
				",T1.ADV_TMN_F early_tn_option\n" +
				",T1.INV_RDM_F invest_rdm_option\n" +
				",T1.PROD_INC_CRD_F prod_crt_enhance\n" +
				",T1.PROD_INC_CRD_ORG_TYP_CD crt_ins_type\n" +
				",T1.PROD_INC_CRD_FORM prod_crt_method\n" +
				",T1.PROD_ESP_PRPT prod_esp_prpt\n" +
				",T2.PROD_CD IDENT_CODE\n" +
				",T2.PROD_NM prod_name\n" +
				",T2.PROD_CLC_MTH type_collect\n" +
				",T2.PROD_MOD operation_mode\n" +
				",T2.PROD_CCY fund_cur\n" +
				",T2.RSK_GRD  risk_rate\n" +
				",T2.INV_RSK_PFR risk_level\n" +
				",T2.BLG_FIN_SAM_BUS_F fiancial_exclusive\n" +
				",T2.CSH_MNG_F cash_manager\n" +
				",T2.ERN_TYP prod_retrun_type\n" +
				",T2.EXPE_SIZ plan_fund_amt\n" +
				",T2.PROD_TRM_CNBD prod_term\n" +
				",T2.PROD_BRND prod_brand\n" +
				",T2.ISU_ORG_CD bank_code\n" +
				",T2.PROD_INV_TYP invest_type\n" +
				",T2.PFM_COMP_BCHM prod_benchmark\n" +
				",(select IDV_SNG_SSCR_MIN_AMT  from DWD_PRD_PRD_LMT_INF where PROD_CD = $S{identCode}) invest_threshold\n" +
				",(select FEE_RAT from DWD_PRD_PRD_FEE_INF where PROD_CD = $S{identCode} and FEE_TYP = '3')  sales_commission_rate\n" +
				",(select FEE_RAT from DWD_PRD_PRD_FEE_INF where PROD_CD = $S{identCode} and FEE_TYP = '2')  manage_fee_rate\n" +
				",(select FEE_RAT from DWD_PRD_PRD_FEE_INF where PROD_CD = $S{identCode} and FEE_TYP = '2')  cd_fee_rate\n" +
				"FROM DWD_PRD_PRD_SPVS_INF T1\n" +
				"join DWD_PRD_PRD_BAS_INF T2\n" +
				"ON T1.PROD_CD = T2.PROD_CD\n" +
				"where T1.PROD_CD  = $S{identCode}";
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}

	public SqlResult<ProdRegistFilingInfo> findProdInfos(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		String sql = "select d.prod_cd  ident_code from DWD_PRD_PRD_SPVS_INF d";
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}

	public UpdateResult addImportProdRegistFilingInfo(Object map) throws Exception {
        return super.update("insert into app_prod_regist_filing_info (PROD_NAME,IDENT_CODE,PROD_BRAND,PROD_TERM_NO,BANK_CODE,PROD_APRV_NM,APPROVER_ID_CODE,PROD_DSN_NM,DESIGNER_ID_CODE," +
						"INV_MNG_NM,MANAGER_ID_CODE,CONTACT_NAME,CONTACT_TELPHONE,CONTACT_MOBILE,CONTACT_EMAIL,TYPE_COLLECT,PROD_RETRUN_TYPE,PROD_TERM,FIANCIAL_EXCLUSIVE,INVERT_REGION," +
						"INVERT_COUNTRY,SERVICE_MODE,OPERATION_MODE,MIN_HOLD_PERIOD,MIN_HOLD_DAY,OPTION_REDEMPT_PERIOD,CASH_MANAGER,ASSET_AC_METHOD,PROD_MANA_MODE,AC_MANA_NAME,PRICE_METHOD," +
						"INVEST_TYPE,COOPERATE_MODE,COOPERATOR,INVEST_TYPE_RATIO,PROD_BENCHMARK,RISK_LEVEL,PROD_SALES_REGION,FUND_CUR,PRINCIPAL_CUR,INCOME_CUR,INVEST_THRESHOLD,PLAN_FUND_AMT," +
						"START_DATE_EARLIEST,START_DATE_LATEST,PRINCIPAL_DUE_DATE,INCOME_DUE_DATE,SALES_COMMISSION_RATE,MANAGE_FEE_RATE,DC_CD_NAME,DC_CD_IDENT_CODE,SEAS_CD_NATION,SEAS_CD_NAME," +
						"CD_FEE_RATE,RISK_RATE,EARLY_TN_OPTION,INVEST_RDM_OPTION,PROD_CRT_ENHANCE,CRT_INS_TYPE,PROD_CRT_METHOD,DETAILS,register_serno,NEW_PROD,REGISTER_STATUS,create_date," +
						"report_date,theory_report_start_date,theory_report_end_date,PROD_ESP_PRPT,sys_data_version,sys_data_status) " +
						"VALUES ($S{prodName},$S{identCode},$S{prodBrand},$S{prodTermNo},$S{bankCode},$S{prodAprvNm},$S{approverIdCode},$S{prodDsnNm},$S{designerIdCode},$S{invMngNm},$S{managerIdCode}," +
						"$S{contactName},$S{contactTelphone},$S{contactMobile},$S{contactEmail},$S{typeCollect},$S{prodRetrunType},$S{prodTerm},$S{fiancialExclusive},$S{invertRegion},$S{invertCountry}," +
						"$S{serviceMode},$S{operationMode},$S{minHoldPeriod},$S{minHoldDay},$S{optionRedemptPeriod},$S{cashManager},$S{assetAcMethod},$S{prodManaMode},$S{acManaName},$S{priceMethod}," +
						"$S{investType},$S{cooperateMode},$S{cooperator},$S{investTypeRatio},$D{prodBenchmark},$S{riskLevel},$S{prodSalesRegion},$S{fundCur},$S{principalCur},$S{incomeCur},$D{investThreshold}," +
						"$D{planFundAmt},$S{startDateEarliest},$S{startDateLatest},$S{principalDueDate},$S{incomeDueDate},$D{salesCommissionRate},$D{manageFeeRate},$S{dcCdName},$S{dcCdIdentCode},$S{seasCdNation}," +
						"$S{seasCdName},$D{cdFeeRate},$S{riskRate},$S{earlyTnOption},$S{investRdmOption},$S{prodCrtEnhance},$S{crtInsType},$S{prodCrtMethod},$S{details}," +
						"(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{newProd},'0',date_format(CURDATE(),'%Y%m%d'),date_format(CURDATE(),'%Y%m%d')," +
						"$S{theoryReportStartDate},$S{theoryReportEndDate}, $S{prodEspPrpt},'1.0','1')",
                DataSourceProperty.PUB,map);
	}

	public UpdateResult addImportProdBaseInfo(Object map) throws Exception {
		return super.update("insert into ods_prod_base_info (PROD_NAME,PROD_NM_FU,PROD_CODE,mother_fund_code,PROD_BRAND,INCOME_TYPE,BLG_FIN_SAM_BUS_F,PROD_MOD,IS_MIN_HOLD_TERM,MIN_HOLD_TERM,REDEEM_AFTER_HOLD," +
						"CASH_TYPE_Z,T8_INVEST_PROP_TYPE,PERFM_BENCHM_RATE,INVESTOR_TREND,ISSU_CCY,RETURN_CCY,INCOME_CCY,PLAN_FUND_AMOUNT,SUBSCR_SD_EARLIEST,SUBSCR_ED_LATEST,INVEST_MNG_FEE_RATE,RISK_LEV,PROD_STATUS,CRT_DT,UPD_DT,MSG_TYPE,CUSTODY_ORG_MNG_DUTY,INCOME_TRANS_PROD_MARK,CROSS_BORDER_WEALTH,BASE_INFO_OPEN_MARK,PROD_RENEWAL_MARK,LIQUIDATE_MARK) " +
						"select $S{prodName},$S{prodName},$S{identCode},$S{identCode},$S{prodBrand},$S{prodRetrunType},(case when $S{fiancialExclusive} ='01' then '1' else '0' end) ,(case when $S{operationMode} ='01' then '7' else '6' end),$S{minHoldPeriod},$D{minHoldDay},$S{optionRedemptPeriod}," +
						"$S{cashManager},$S{investType},$D{prodBenchmark},$S{riskLevel},$S{fundCur},$S{principalCur},$S{incomeCur},$D{planFundAmt},$S{startDateEarliest},$S{startDateLatest},$D{manageFeeRate}," +
						"$S{riskRate} ,'00',date_format(CURDATE(),'%Y%m%d'),date_format(CURDATE(),'%Y%m%d'),'01','01','02','02','01','02','02' " +
				        "where not exists(select 1 from ods_prod_base_info t where t.PROD_CODE=$S{identCode}) ",
				DataSourceProperty.PUB,map);
	}
	public UpdateResult addImportProdSpvsInfo(Object map) throws Exception {
		return super.update("insert into ods_prod_spvs_info (PROD_CODE,BANK_PRODSID,PROD_BRAND,PROD_TIMES,AUTHOR_NAME,AUTHOR_IDENTIF,DESIGN_NAME,DESIGN_IDENTIF,MANAGE_NAME,MANAGE_IDENTIF,SALEMAN_NAME," +
						"SALEMAN_PHONENO,SALEMAN_TELNO,SALEMAN_EMAIL,COLL_MOD,PROD_CYCLE,SALE_PLACE,SPECI_COUNTRY_REGION,SRV_MODE,ASSET_MAPING,MANAGE_MODE,ADMIN_NAME,PRICING_TYPE,COOPERATION_MODE,COOPERATION_ORG_NAME," +
						"PROD_PRECENT,PROD_SAL_ZON,INVEST_THRESH,RETURN_COST,RETURN_INCOME,SALE_COMMIS_RATE,BORD_TRUSTI_NAME,BORD_TRUSTI_CODE,OVERS_TRUSTI_NATION\n,OVERS_TRUSTI_NAME\n,CUSTODY_FEE_RATE\n,TERM_FLAG," +
						"REDEEM_FLAG,PROD_CREDIT_FLAG,PROD_CREDIT_ORG,PROD_CREDIT_MOD,NEW_OLD_PROD_F,PROD_ESP_PRPT,REMARK,CRT_DT,UPD_DT) " +
						"select $S{identCode},$S{identCode},$S{prodBrand},$D{prodTermNo},$S{prodAprvNm},$S{approverIdCode},$S{prodDsnNm},$S{designerIdCode},$S{invMngNm},$S{managerIdCode},$S{contactName},$S{contactTelphone}," +
						"$S{contactMobile},$S{contactEmail},$S{typeCollect},$S{prodTerm},$S{invertRegion},$S{invertCountry},$S{serviceMode},$S{assetAcMethod},$S{prodManaMode},$S{acManaName},$S{priceMethod}," +
						"$S{cooperateMode},$S{cooperator},$S{investTypeRatio},$S{prodSalesRegion},$D{investThreshold},$S{principalDueDate},$S{incomeDueDate},$D{salesCommissionRate},$S{dcCdName},$S{dcCdIdentCode}," +
						"$S{seasCdNation},$S{seasCdName},$D{cdFeeRate},$S{earlyTnOption},$S{investRdmOption},$S{prodCrtEnhance},$S{crtInsType},$S{prodCrtMethod},$S{newProd},$S{prodEspPrpt},$S{details},date_format(CURDATE(),'%Y%m%d'),date_format(CURDATE(),'%Y%m%d')" +
						"where not exists(select 1 from ods_prod_spvs_info t where t.PROD_CODE=$S{identCode}) ",
				DataSourceProperty.PUB,map);
	}


	public UpdateResult deleteImportProdRegistFilingInfo(Object map) throws Exception {
		return super.update("DELETE FROM app_prod_regist_filing_info WHERE  register_serno=$S{registerSerno}",
				DataSourceProperty.PUB,map);
	}

    public UpdateResult deleteProdRegistFilingInfoDatas(Map<String, Object> params) throws Exception {
		return super.update("DELETE FROM app_prod_regist_filing_info where  register_serno=$S{registerSerno} ", params);
    }


	public SqlResult<ProdRegistFilingInfo> findProdRegistFilingInfoSingleById(SqlParam<ProdRegistFilingInfo> param) throws Exception {
		String sql = "SELECT t.id,t.create_date,t.prod_name,t.ident_code,t.prod_brand,t.prod_term_no,t.bank_code,t.PROD_APRV_NM,t.approver_id_code,t.PROD_DSN_NM,t.designer_id_code,t.INV_MNG_NM,t.manager_id_code," +
				"t.contact_name,t.contact_telphone,t.contact_mobile,t.contact_email,t.type_collect,t.prod_retrun_type,t.prod_term,t.fiancial_exclusive,t.invert_region,t.invert_country,t.service_mode,t.operation_mode," +
				"t.min_hold_period,t.min_hold_day,t.option_redempt_period,t.cash_manager,t.asset_ac_method,t.prod_mana_mode,t.ac_mana_name,t.price_method,t.invest_type,t.cooperate_mode,t.cooperator,t.invest_type_ratio," +
				"t.prod_benchmark,t.risk_level,t.prod_sales_region,t.fund_cur,t.principal_cur,t.income_cur,t.invest_threshold,t.plan_fund_amt,t.start_date_earliest,t.start_date_latest,t.principal_due_date,t.income_due_date," +
				"t.sales_commission_rate,t.manage_fee_rate,t.dc_cd_name,t.dc_cd_ident_code,t.seas_cd_nation,t.seas_cd_name,t.cd_fee_rate,t.risk_rate,t.early_tn_option,t.invest_rdm_option,t.prod_crt_enhance,t.crt_ins_type," +
				"t.prod_crt_method,t.details,t.register_serno,t.imp_date,t.register_date,t.register_status,t.main_doc,t.feasy_ass_report,t.inter_audit_doc,t.due_diligencr_doc,t.legal_doc_sifned,t.prod_sale_doc," +
				"t.prod_specifi,t.prod_mark_doc,t.other_doc,t.new_prod,ifnull(ARS.audit_status,0) audit_status,prod_esp_prpt,t.sys_data_version " +
				"FROM app_prod_regist_filing_info t\n" +
				"LEFT JOIN base_report_data_audit_results ARS ON t.report_date=ARS.report_date and ARS.table_id = 'app_prod_regist_filing_info' where t.sys_data_status ='1' \n";
		if (StringUtils.isNotBlank(param.getModel().getId())) {
			sql = sql + " and  t.id = $S{id} ";
		}
		return super.findRows(sql,DataSourceProperty.PUB, param);
	}

	//申报产品-行内标识码-是否已发行登记(已发行成功的，不允许删除申报记录)
	public int findProdRegistFilingInfoEffective(ProdRegistFilingInfo params) throws Exception {
		String sql = "SELECT count(1) FROM app_prod_issuance_regist_info WHERE sys_data_status ='1' and  register_status = '3' ";
//		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
//			sql = sql + " and  SUBSCRIPTION_START_DATE >= '" + params.getModel().getBeginDate() + "'";
//		}
		if (StringUtils.isNotBlank(params.getIdentCode())) {
			sql = sql + " and prod_ident_code ='" + params.getIdentCode() + "'";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

}
