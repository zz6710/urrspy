package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.ProdIssuanceRegistInfo;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import io.micrometer.core.instrument.util.StringUtils;

import java.util.List;
import java.util.Map;

@Repository
public class ProdIssuanceRegistInfoDao extends ComnDao {

	public SqlResult<ProdIssuanceRegistInfo> findProdIssuanceRegistInfos(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
//		String sql = "SELECT prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method," +
//				"            structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate," +
//				"            regular_open_period,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period," +
//				"            custody_acct_no,custody_acct_name,create_date,theory_report_start_date,theory_report_end_date,audit_status " +
//				"       FROM app_prod_issuance_regist_info where 1=1 ";
		String sql = "SELECT\n" +
				"\tid,\n" +
				"\tprod_code,\n" +
				"\tbank_code,\n" +
				"\tprod_ident_code,\n" +
				"\tsubscription_start_date,\n" +
				"\tsubscription_end_date,\n" +
				"\tprod_value_date,\n" +
				"\tprod_maturity_date,\n" +
				"\tmanagement_method,\n" +
				"\tstructured_prod,\n" +
				"\tdetails_per_rate,\n" +
				"\topening_mode,\n" +
				"\tregister_serno,\n" +
				"\timp_date,\n" +
				"\tregister_date,\n" +
				"\tregister_status,\n" +
				"\tup_limit_per_rate,\n" +
				"\tlow_limit_per_rate,\n" +
				"\tregular_open_period,\n" +
				"\tregular_open_period_day,\n" +
				"\tother_open_period,\n" +
				"\tdisorder_open_period,\n" +
				"\tfirst_open_day,\n" +
				"\tholiday_open_type,\n" +
				"\taverage_open_no,\n" +
				"\tbusi_open_period,\n" +
				"\tdetails_busi_op_period,\n" +
				"\tcustody_acct_no,\n" +
				"\tcustody_acct_name,\n" +
				"\tifnull(ARS.audit_status,0) audit_status,create_date,REG.report_date,sys_data_version,clsf_sto\n" +
				"FROM\n" +
				"\tapp_prod_issuance_regist_info REG\n" +
				"\tLEFT JOIN base_report_data_audit_results ARS\n" +
				"\tON REG.report_date=ARS.report_date and ARS.table_id = 'app_prod_issuance_regist_info'\n" +
				"WHERE sys_data_status ='1'\n";
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql = sql + " and  REG.SUBSCRIPTION_START_DATE >= '" + params.getModel().getBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql = sql + " and  REG.SUBSCRIPTION_START_DATE <= '" + params.getModel().getQueryDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getBeginCrtDate())) {
			sql = sql + " and  REG.create_date >= '" + params.getModel().getBeginCrtDate()+ "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getEndCrtDate())) {
			sql = sql + " and  REG.create_date <= '" + params.getModel().getEndCrtDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql = sql + " and  REG.report_date >= '" + params.getModel().getReportBeginDate()+ "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql = sql + " and  REG.report_date <= '" + params.getModel().getReportEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdIdentCode())) {
			sql = sql + " and REG.prod_ident_code like '%" + params.getModel().getProdIdentCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and  REG.prod_code like '%" + params.getModel().getProdCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  REG.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}

		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  REG.register_serno like '%" + params.getModel().getRegisterSerno() + "%'";
		}

		return super.findRows(sql, DataSourceProperty.PUB, params);
	}

	public int findProdIssuanceRegistInfosCount(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
//		String sql = "SELECT prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method," +
//				"            structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate," +
//				"            regular_open_period,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period," +
//				"            custody_acct_no,custody_acct_name,create_date,theory_report_start_date,theory_report_end_date,audit_status " +
//				"       FROM app_prod_issuance_regist_info where 1=1 ";
		String sql = "SELECT count(1) FROM\n" +
				"\tapp_prod_issuance_regist_info REG\n" +
				"\tLEFT JOIN base_report_data_audit_results ARS\n" +
				"\tON REG.report_date=ARS.report_date and ARS.table_id = 'app_prod_issuance_regist_info'\n" +
				"WHERE sys_data_status ='1'\n";
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql = sql + " and  REG.SUBSCRIPTION_START_DATE >= '" + params.getModel().getBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql = sql + " and  REG.SUBSCRIPTION_START_DATE <= '" + params.getModel().getQueryDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getBeginCrtDate())) {
			sql = sql + " and  REG.create_date >= '" + params.getModel().getBeginCrtDate()+ "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getEndCrtDate())) {
			sql = sql + " and  REG.create_date <= '" + params.getModel().getEndCrtDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql = sql + " and  REG.report_date >= '" + params.getModel().getReportBeginDate()+ "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql = sql + " and  REG.report_date <= '" + params.getModel().getReportEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdIdentCode())) {
			sql = sql + " and REG.prod_ident_code like '%" + params.getModel().getProdIdentCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and  REG.prod_code like '%" + params.getModel().getProdCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  REG.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}

		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  REG.register_serno like '%" + params.getModel().getRegisterSerno() + "%'";
		}

		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public SqlResult<ProdIssuanceRegistInfo> findProdIssuanceRegistInfosByprod(String prods,SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		params.setStart(0);
		params.setLimit(2000);

		String sql = " SELECT  prod_code,  bank_code,  prod_ident_code,  subscription_start_date,  subscription_end_date,  prod_value_date,  prod_maturity_date, " +
				" management_method,  structured_prod,  details_per_rate,  opening_mode,  register_serno,  imp_date,  register_date,  register_status,  " +
				"up_limit_per_rate,  low_limit_per_rate,  regular_open_period,  regular_open_period_day,  other_open_period,  disorder_open_period,  " +
				"first_open_day,  holiday_open_type,  average_open_no,  busi_open_period,  details_busi_op_period,  custody_acct_no,  custody_acct_name, " +
				" ARS.audit_status,create_date,REG.report_date,sys_data_version,clsf_sto  FROM  app_prod_issuance_regist_info REG  " +
				" LEFT JOIN base_report_data_audit_results ARS  ON ARS.table_id = 'app_prod_issuance_regist_info'  WHERE sys_data_status ='1' ";
		if(StringUtils.isNotBlank(prods)){
			sql = sql + " and  REG.prod_code in (" + prods + ")";
		}
		SqlResult<ProdIssuanceRegistInfo> r1 = super.findRows(sql, DataSourceProperty.PUB, params);
		return r1;
	}
	public SqlResult<ProdIssuanceRegistInfo> findOldProdIssuanceRegistInfoById(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		String sql = "SELECT\n" +
				"\tprod_code,\n" +
				"\tbank_code,\n" +
				"\tprod_ident_code,\n" +
				"\tsubscription_start_date,\n" +
				"\tsubscription_end_date,\n" +
				"\tprod_value_date,\n" +
				"\tprod_maturity_date,\n" +
				"\tmanagement_method,\n" +
				"\tstructured_prod,\n" +
				"\tdetails_per_rate,\n" +
				"\topening_mode,\n" +
				"\tregister_serno,\n" +
				"\timp_date,\n" +
				"\tregister_date,\n" +
				"\tregister_status,\n" +
				"\tup_limit_per_rate,\n" +
				"\tlow_limit_per_rate,\n" +
				"\tregular_open_period,\n" +
				"\tregular_open_period_day,\n" +
				"\tother_open_period,\n" +
				"\tdisorder_open_period,\n" +
				"\tfirst_open_day,\n" +
				"\tholiday_open_type,\n" +
				"\taverage_open_no,\n" +
				"\tbusi_open_period,\n" +
				"\tdetails_busi_op_period,\n" +
				"\tcustody_acct_no,\n" +
				"\tcustody_acct_name,\n" +
				"\tifnull(ARS.audit_status,0) audit_status,create_date,REG.report_date,sys_data_version\n" +
				"FROM\n" +
				"\tapp_prod_issuance_regist_info REG\n" +
				"\tLEFT JOIN base_report_data_audit_results ARS\n" +
				"\tON REG.report_date=ARS.report_date and ARS.table_id = 'app_prod_issuance_regist_info'\n" +
				"WHERE sys_data_status ='1'\n";
		if (StringUtils.isNotBlank(params.getModel().getId())) {
			sql = sql + " and REG.id = $S{id}";
		}

		return super.findRows(sql, DataSourceProperty.PUB, params);
	}


	public int findProdIssuanceRegistInfosStatus(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		String sql = "SELECT count(1) FROM\n" +
				"\tapp_prod_issuance_regist_info REG\n" +
				"\tLEFT JOIN base_report_data_audit_results ARS\n" +
				"\tON ARS.table_id = 'app_prod_issuance_regist_info'\n" +
				"WHERE  sys_data_status ='1' " +
				" and REG.register_status in (0,1) ";
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql = sql + " and  REG.SUBSCRIPTION_START_DATE >= '" + params.getModel().getBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql = sql + " and  REG.SUBSCRIPTION_START_DATE <= '" + params.getModel().getQueryDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getBeginCrtDate())) {
			sql = sql + " and  REG.create_date >= '" + params.getModel().getBeginCrtDate()+ "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getEndCrtDate())) {
			sql = sql + " and  REG.create_date <= '" + params.getModel().getEndCrtDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql = sql + " and  REG.report_date >= '" + params.getModel().getReportBeginDate()+ "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql = sql + " and  REG.report_date <= '" + params.getModel().getReportEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdIdentCode())) {
			sql = sql + " and REG.prod_ident_code like '%" + params.getModel().getProdIdentCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and  REG.prod_code like '%" + params.getModel().getProdCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  REG.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public UpdateResult addProdIssuanceRegistInfo(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		return super.update("INSERT INTO app_prod_issuance_regist_info(prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method,structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate,regular_open_period,regular_open_period_day,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period,custody_acct_no,custody_acct_name) VALUES($S{prodCode},$S{bankCode},$S{prodIdentCode},$S{subscriptionStartDate},$S{subscriptionEndDate},$S{prodValueDate},$S{prodMaturityDate},$S{managementMethod},$S{structuredProd},$S{detailsPerRate},$S{openingMode},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',$D{upLimitPerRate},$D{lowLimitPerRate},$S{regularOpenPeriod},$D{regularOpenPeriodDay},$D{otherOpenPeriod},$S{disorderOpenPeriod},$S{firstOpenDay},$S{holidayOpenType},$D{averageOpenNo},$S{busiOpenPeriod},$S{detailsBusiOpPeriod},$S{custodyAcctNo},$S{custodyAcctName},date_format(CURDATE(),'%Y%m%d'))",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult updateProdIssuanceRegistInfo(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		return super.update("UPDATE app_prod_issuance_regist_info SET bank_code=$S{bankCode} ,prod_ident_code=$S{prodIdentCode} ,subscription_start_date=$S{subscriptionStartDate} ,subscription_end_date=$S{subscriptionEndDate} ,prod_value_date=$S{prodValueDate} ,prod_maturity_date=$S{prodMaturityDate} ,management_method=$S{managementMethod} ,structured_prod=$S{structuredProd} ,details_per_rate=$S{detailsPerRate} ,opening_mode=$S{openingMode} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,up_limit_per_rate=$D{upLimitPerRate} ,low_limit_per_rate=$D{lowLimitPerRate} ,regular_open_period=$S{regularOpenPeriod},regular_open_period_day=$D{regularOpenPeriodDay} ,other_open_period=$D{otherOpenPeriod} ,disorder_open_period=$S{disorderOpenPeriod} ,first_open_day=$S{firstOpenDay} ,holiday_open_type=$S{holidayOpenType} ,average_open_no=$D{averageOpenNo} ,busi_open_period=$S{busiOpenPeriod} ,details_busi_op_period=$S{detailsBusiOpPeriod} ,custody_acct_no=$S{custodyAcctNo} ,custody_acct_name=$S{custodyAcctName}  WHERE  register_serno=$S{registerSerno} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public String getProdCode (SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		StringBuffer ss = new StringBuffer();
		String sql = "select distinct prod_code from app_prod_issuance_regist_info REG where 1=1 ";

		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql = sql + " and  REG.SUBSCRIPTION_START_DATE >= '" + params.getModel().getBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql = sql + " and  REG.SUBSCRIPTION_START_DATE <= '" + params.getModel().getQueryDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getBeginCrtDate())) {
			sql = sql+"  and  REG.create_date >= '" + params.getModel().getBeginCrtDate()+ "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getEndCrtDate())) {
			sql = sql+"  and  REG.create_date <= '" + params.getModel().getEndCrtDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql = sql + " and  REG.report_date >= '" + params.getModel().getReportBeginDate()+ "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql = sql + " and  REG.report_date <= '" + params.getModel().getReportEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql = sql + " and  REG.report_date <= '" + params.getModel().getReportEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdIdentCode())) {
			sql = sql + " and REG.prod_ident_code like '%" + params.getModel().getProdIdentCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and  REG.prod_code like '%" + params.getModel().getProdCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  REG.register_status = '" + params.getModel().getRegisterStatus() + "'";
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

	public UpdateResult updateProdIssuanceRegistInfoStatus(SqlParam<ProdIssuanceRegistInfo> params,String prod_code) throws Exception {
		String sql = "UPDATE app_prod_issuance_regist_info REG SET register_status='3'  WHERE sys_data_status='1'";
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql = sql + " and  REG.SUBSCRIPTION_START_DATE >= '" + params.getModel().getBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql = sql + " and  REG.SUBSCRIPTION_START_DATE <= '" + params.getModel().getQueryDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getBeginCrtDate())) {
			sql = sql+"  and  REG.create_date >= '" + params.getModel().getBeginCrtDate()+ "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getEndCrtDate())) {
			sql = sql+"  and  REG.create_date <= '" + params.getModel().getEndCrtDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql = sql + " and  REG.report_date >= '" + params.getModel().getReportBeginDate()+ "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql = sql + " and  REG.report_date <= '" + params.getModel().getReportEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdIdentCode())) {
			sql = sql + " and REG.prod_ident_code like '%" + params.getModel().getProdIdentCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and  REG.prod_code like '%" + params.getModel().getProdCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  REG.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}

		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  REG.register_serno like '%" + params.getModel().getRegisterSerno() + "%'";
		}
		if(StringUtils.isNotBlank(prod_code)){
			sql  = sql + " and prod_code in ("+prod_code+")";
		}
		return super.update(sql,
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult updateProdStat(SqlParam<ProdIssuanceRegistInfo> params,String prod_code) throws Exception {
		String sql = "UPDATE ods_prod_base_info t left join  app_prod_issuance_regist_info a on t.mother_fund_code = a.PROD_IDENT_CODE SET t.PROD_STATUS='04' WHERE t.PROD_STATUS = '02' ";
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())&&StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql += " and  a.SUBSCRIPTION_START_DATE>='" +params.getModel().getBeginDate()+ "' and a.SUBSCRIPTION_START_DATE<='" +params.getModel().getQueryDate()+"' ";
		}

		if (StringUtils.isNotBlank(params.getModel().getBeginCrtDate())&&StringUtils.isNotBlank(params.getModel().getEndCrtDate())) {
			sql += " and a.create_date>='" +params.getModel().getBeginCrtDate()+"' and a.create_date<='" +params.getModel().getEndCrtDate()+"' ";
		}

		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql = sql + " and  a.report_date >= '" + params.getModel().getReportBeginDate()+ "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql = sql + " and  a.report_date <= '" + params.getModel().getReportEndDate() + "'";
		}
		if(StringUtils.isNotBlank(prod_code)){
			sql = sql + " and t.prod_code in (select prod_ident_code from app_prod_issuance_regist_info where prod_code in ("+prod_code+") )";
		}
		return super.update(sql, DataSourceProperty.PUB);
	}

	public void updateBaseReportResultInfo(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		String sql="update base_report_result set register_date = theory_report_start_date,report_success_number=total,status= '1',register_status= '1',update_date=date_format(now(),'%Y%m%d'),update_time=date_format(now(),'%H%i%s') where report_table = 'app_prod_issuance_regist_info' and theory_report_start_date in (select theory_report_start_date from app_prod_issuance_regist_info where report_date between $S{reportBeginDate} and $S{reportEndDate}) ";
		super.update(sql, DataSourceProperty.PUB, params.getModel());
	}

	public void updateProdDataFlag(SqlParam<ProdIssuanceRegistInfo> params,String prod_code) throws Exception {
//		String sql1 = "UPDATE ods_prod_base_info set data_flag_old=data_flag where upd_dt = date_format(now(),'%Y%m%d') " +
//				" and exists (select 1 from app_prod_issuance_regist_info t1 where t1.prod_code = ods_prod_base_info.prod_code " +
//				" and register_status != '3' and report_date  = date_format(now(),'%Y%m%d') )";

		String sql2 = "UPDATE ods_prod_base_info set data_flag_old=data_flag , data_flag=trim(both ',' from replace(concat(data_flag,','),'1,','')) " +
				" where upd_dt = date_format(now(),'%Y%m%d') " +
				" and exists (select 1 from app_prod_issuance_regist_info t1 where t1.prod_ident_code = ods_prod_base_info.prod_code " +
				" and register_status != '3' and report_date  = date_format(now(),'%Y%m%d') )";
		if(StringUtils.isNotBlank(prod_code)){
			sql2  = sql2 + " and prod_code in (select prod_ident_code from app_prod_issuance_regist_info where prod_code in ("+prod_code+") )";
		}
//		super.update(sql1, DataSourceProperty.PUB,params);
		super.update(sql2, DataSourceProperty.PUB, params);
	}

	public UpdateResult deleteProdIssuanceRegistInfo(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		return super.update("DELETE FROM app_prod_issuance_regist_info WHERE  register_serno=$S{registerSerno} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportProdIssuanceRegistInfo(Object parma) throws Exception {
		return super.update("INSERT INTO app_prod_issuance_regist_info(prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method,structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate,regular_open_period,regular_open_period_day,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period,custody_acct_no,custody_acct_name,report_date,theory_report_start_date,sys_data_status,sys_data_version,clsf_sto) VALUES($S{prodCode},$S{bankCode},$S{prodIdentCode},$S{subscriptionStartDate},$S{subscriptionEndDate},$S{prodValueDate},$S{prodMaturityDate},$S{managementMethod},$S{structuredProd},$S{detailsPerRate},$S{openingMode},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),'','0',$D{upLimitPerRate},$D{lowLimitPerRate},$S{regularOpenPeriod},$D{regularOpenPeriodDay},$D{otherOpenPeriod},$S{disorderOpenPeriod},$S{firstOpenDay},$S{holidayOpenType},$D{averageOpenNo},$S{busiOpenPeriod},$S{detailsBusiOpPeriod},$S{custodyAcctNo},$S{custodyAcctName},$S{reportDate},(select workday  from sys_workday_set  where workday <$S{reportDate} order by workday desc limit 1),'1','1.0',$D{clsfSto})",
				DataSourceProperty.PUB,parma);
	}

	public UpdateResult deleteImportProdIssuanceRegistInfo(Map<String, Object> params) throws Exception {
		return super.update("DELETE FROM app_prod_issuance_regist_info where register_serno=$S{registerSerno} ", params);
	}
}
