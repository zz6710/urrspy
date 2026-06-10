package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.SubseqSubscrRegistInfo;
import com.kayak.rpt.zz.manage.model.TrTerminationRegistInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class TrTerminationRegistInfoDao extends ComnDao {

	public SqlResult<TrTerminationRegistInfo> findTrTerminationRegistInfos(SqlParam<TrTerminationRegistInfo> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT report_date,\n" +
				"       prod_code,\n" +
				"       bank_code,\n" +
				"       actual_prod_ter_date,\n" +
				"       interest_payment,\n" +
				"       register_serno,\n" +
				"       imp_date,\n" +
				"       register_date,\n" +
				"       register_status,\n" +
				"       realized_bank_income,\n" +
				"       payment,\n" +
				"       delivered_vol,\n" +
				"       in_custodian_fee,\n" +
				"       in_manage_fee,\n" +
				"       in_sales_commision,\n" +
				"       in_other_prod_fee,\n" +
				"       other_custodian_fee,\n" +
				"       other_manage_fee,\n" +
				"       other_sales_comm,\n" +
				"       consult_fee,\n" +
				"       other_prod_fee,\n" +
				"       annual_return_client,\n" +
				"       annual_return_prod,\n" +
				"       is_error,\n" +
				"       AA.audit_status\n" +
				" FROM (SELECT T1.*, (case when t2.register_serno is null then '0' else '1' end) AS IS_ERROR,ifnull(ARS.audit_status,0) AS audit_status\n" +
				"         FROM app_termination_regist_info T1\n" +
				"         LEFT JOIN app_termination_regist_info_erdesc T2 ON T2.register_serno = T1.register_serno " +
				"         LEFT JOIN base_report_data_audit_results ARS ON T1.report_date = ARS.report_date and ARS.table_id = 'app_termination_regist_info') AA\n" +
				"where sys_data_status ='1'");
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql.append(" and  report_date >= '" + params.getModel().getBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql.append(" and  report_date <= '" + params.getModel().getQueryDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and register_status = '" + params.getModel().getRegisterStatus() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql.append(" and prod_code = '" + params.getModel().getProdCode() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getActualStartDate())) {
			sql.append("  and DATE(actual_prod_ter_date) >= DATE($S{actualStartDate}) and DATE(actual_prod_ter_date) <= DATE($S{actualEndDate})");
		}

		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql.append(" and  register_serno = '" + params.getModel().getRegisterSerno() + "'");
		}
		return super.findRows(sql.toString(), params);
	}

	public int findTrTerminationRegistInfosCount(SqlParam<TrTerminationRegistInfo> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(1)  FROM (SELECT T1.*, (case when t2.register_serno is null then '0' else '1' end) AS IS_ERROR,ifnull(ARS.audit_status,0) AS audit_status\n" +
				"         FROM app_termination_regist_info T1\n" +
				"         LEFT JOIN app_termination_regist_info_erdesc T2 ON T2.register_serno = T1.register_serno " +
				"         LEFT JOIN base_report_data_audit_results ARS ON T1.report_date = ARS.report_date and ARS.table_id = 'app_termination_regist_info') AA\n" +
				"where sys_data_status ='1'");
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql.append(" and  report_date >= '" + params.getModel().getBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql.append(" and  report_date <= '" + params.getModel().getQueryDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and register_status = '" + params.getModel().getRegisterStatus() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql.append(" and prod_code = '" + params.getModel().getProdCode() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getActualStartDate())) {
			sql.append("  and DATE(actual_prod_ter_date) >= '" + params.getModel().getActualStartDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getActualEndDate())) {
			sql.append("  and DATE(actual_prod_ter_date) <= '" + params.getModel().getActualEndDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql.append(" and  register_serno = '" + params.getModel().getRegisterSerno() + "'");
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql.toString(),DataSourceProperty.PUB, params)));
	}

	public int findTrTerminationRegistInfoFailStatus(SqlParam<TrTerminationRegistInfo> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(1) FROM (SELECT T1.*, (case when t2.register_serno is null then '0' else '1' end) AS IS_ERROR,ARS.audit_status AS audit_status\n" +
				"      FROM app_termination_regist_info T1\n" +
				"               LEFT JOIN app_termination_regist_info_erdesc T2 ON T2.register_serno = T1.register_serno LEFT JOIN base_report_data_audit_results ARS ON ARS.table_id = 'app_termination_regist_info') AA\n" +
				"where sys_data_status ='1' " +
				" and register_status in (0,1) ");
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql.append(" and  report_date >= '" + params.getModel().getBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql.append(" and  report_date <= '" + params.getModel().getQueryDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql.append(" and prod_code like '%" + params.getModel().getProdCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getActualStartDate())) {
			sql.append("  and DATE(actual_prod_ter_date) >= '" + params.getModel().getActualStartDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getActualEndDate())) {
			sql.append("  and DATE(actual_prod_ter_date) <= '" + params.getModel().getActualEndDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and register_status like '%" + params.getModel().getRegisterStatus() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql.append(" and  register_serno = '" + params.getModel().getRegisterSerno() + "'");
		}

		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql.toString(),DataSourceProperty.PUB, params)));
	}

	public UpdateResult addTrTerminationRegistInfo(SqlParam<TrTerminationRegistInfo> params) throws Exception {
		return super.update("INSERT INTO app_termination_regist_info(prod_code,bank_code,actual_prod_ter_date,interest_payment,register_serno,imp_date,register_date,register_status,realized_bank_income,payment,delivered_vol,in_custodian_fee,in_manage_fee,in_sales_commision,in_other_prod_fee,other_custodian_fee,other_manage_fee,other_sales_comm,consult_fee,other_prod_fee,annual_return_client,annual_return_prod,report_date) VALUES($S{prodCode},$S{bankCode},$S{actualProdTerDate},$D{interestPayment},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',$S{realizedBankIncome},$S{payment},$S{deliveredVol},$S{inCustodianFee},$D{inManageFee},$D{inSalesCommision},$S{inOtherProdFee},$D{otherCustodianFee},$S{otherManageFee},$S{otherSalesComm},$S{consultFee},$S{otherProdFee},$S{annualReturnClient},$D{annualReturnProd},date_format(CURDATE(),'%Y%m%d'))",
				DataSourceProperty.PUB,params.getModel());
	}

	public void addTrTerminationRegistInfoForBatch(List<Map<String, Object>> mapList) throws Exception {
		Date date = new Date();
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(date);

		String  sql  = "INSERT INTO app_termination_regist_info(prod_code,bank_code,actual_prod_ter_date,interest_payment,register_serno,imp_date,register_date,register_status,realized_bank_income,payment,delivered_vol,in_custodian_fee,in_manage_fee,in_sales_commision,in_other_prod_fee,other_custodian_fee,other_manage_fee,other_sales_comm,consult_fee,other_prod_fee,annual_return_client,annual_return_prod,create_date) VALUES($S{prodCode},$S{bankCode},$S{actualProdTerDate},$D{interestPayment},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',$S{realizedBankIncome},$S{payment},$S{deliveredVol},$S{inCustodianFee},$D{inManageFee},$D{inSalesCommision},$S{inOtherProdFee},$D{otherCustodianFee},$S{otherManageFee},$S{otherSalesComm},$S{consultFee},$S{otherProdFee},$S{annualReturnClient},$D{annualReturnProd},date_format(CURDATE(),'%Y%m%d'))";
		for (Map<String, Object> map : mapList) {
			map.put("impDate",dateStr);
			super.update(sql,DataSourceProperty.PUB,map);
		}
	}

	public UpdateResult updateTrTerminationRegistInfo(SqlParam<TrTerminationRegistInfo> params) throws Exception {
		return super.update("UPDATE app_termination_regist_info SET prod_code=$S{prodCode} ,bank_code=$S{bankCode} ,actual_prod_ter_date=$S{actualProdTerDate} ,interest_payment=$D{interestPayment}  ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,realized_bank_income=$S{realizedBankIncome} ,payment=$S{payment} ,delivered_vol=$S{deliveredVol} ,in_custodian_fee=$S{inCustodianFee} ,in_manage_fee=$D{inManageFee} ,in_sales_commision=$D{inSalesCommision} ,in_other_prod_fee=$S{inOtherProdFee} ,other_custodian_fee=$D{otherCustodianFee} ,other_manage_fee=$S{otherManageFee} ,other_sales_comm=$S{otherSalesComm} ,consult_fee=$S{consultFee} ,other_prod_fee=$S{otherProdFee} ,annual_return_client=$S{annualReturnClient} ,annual_return_prod=$D{annualReturnProd}  WHERE register_serno=$S{registerSerno} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public String getProdCode(SqlParam<TrTerminationRegistInfo> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT prod_code from app_termination_regist_info WHERE sys_data_status ='1' ");
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql.append(" and  report_date >= '" + params.getModel().getBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql.append(" and  report_date <= '" + params.getModel().getQueryDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and register_status like '%" + params.getModel().getRegisterStatus() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql.append(" and prod_code like '%" + params.getModel().getProdCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getActualStartDate())) {
			sql.append("  and DATE(actual_prod_ter_date) >= DATE($S{actualStartDate}) and DATE(actual_prod_ter_date) <= DATE($S{actualEndDate})");
		}

		StringBuffer ss = new StringBuffer();
		List<SqlRow> list = super.findRows(sql.toString(), DataSourceProperty.PUB, params.getModel());

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

	public UpdateResult updateTrTerminationRegistInfoStatus(SqlParam<TrTerminationRegistInfo> params, String prod_code) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE app_termination_regist_info SET register_status='3' WHERE sys_data_status ='1' ");
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql.append(" and  report_date >= '" + params.getModel().getBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql.append(" and  report_date <= '" + params.getModel().getQueryDate() + "'");
		}
		if (StringUtils.isNotBlank(prod_code)) {
			sql.append(" and  prod_code in (" + prod_code + ")");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and register_status like '%" + params.getModel().getRegisterStatus() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql.append(" and prod_code like '%" + params.getModel().getProdCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getActualStartDate())) {
			sql.append("  and DATE(actual_prod_ter_date) >= DATE($S{actualStartDate}) and DATE(actual_prod_ter_date) <= DATE($S{actualEndDate})");
		}
		return super.update(sql.toString(),
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult updateProdStat(SqlParam<TrTerminationRegistInfo> params, String prod_code) throws Exception {
		String sql = "UPDATE ods_prod_base_info SET PROD_STATUS='07'";
			sql += " WHERE CHECK_INON  in (select a.prod_code  from app_termination_regist_info  a where report_date>='" +params.getModel().getBeginDate()+ "' " +
				"  and report_date<='" +params.getModel().getQueryDate() +"')";

		if(StringUtils.isNotBlank(prod_code)){
			sql = sql + " and CHECK_INON in ("+prod_code+") ";
		}
		return super.update(sql, DataSourceProperty.PUB);
	}

	public void updateBaseReportResultInfo(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		String sql="update base_report_result set register_date =theory_report_start_date,report_success_number=total,status= '1',register_status= '1',update_date=date_format(now(),'%Y%m%d'),update_time=date_format(now(),'%H%i%s') where report_table = 'app_termination_regist_info' and theory_report_start_date in (select theory_report_start_date from app_termination_regist_info where report_date between $S{beginDate} and $S{queryDate}) ";
		super.update(sql, DataSourceProperty.PUB, params.getModel());
	}

	public UpdateResult deleteTrTerminationRegistInfo(SqlParam<TrTerminationRegistInfo> params) throws Exception {
		return super.update("DELETE FROM app_termination_regist_info WHERE  register_serno=$S{registerSerno}",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportTrTerminationRegistInfo(Object param) throws Exception {
		return super.update("INSERT INTO app_termination_regist_info(prod_code,bank_code,actual_prod_ter_date,interest_payment,register_serno,imp_date,register_date,register_status,realized_bank_income,payment,delivered_vol,in_custodian_fee,in_manage_fee,in_sales_commision,in_other_prod_fee,other_custodian_fee,other_manage_fee,other_sales_comm,consult_fee,other_prod_fee,annual_return_client,annual_return_prod,report_date,theory_report_start_date,sys_data_status) VALUES($S{prodCode},$S{bankCode},$S{actualProdTerDate},$D{interestPayment},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),$S{registerDate},'0',$D{realizedBankIncome},$D{payment},$D{deliveredVol},$D{inCustodianFee},$D{inManageFee},$D{inSalesCommision},$D{inOtherProdFee},$D{otherCustodianFee},$D{otherManageFee},$D{otherSalesComm},$D{consultFee},$D{otherProdFee},$D{annualReturnClient},$D{annualReturnProd},(select workday  from sys_workday_set  where workday <$S{reportDate} order by workday desc limit 1),$S{reportDate},'1')",
				DataSourceProperty.PUB,param);
	}

	public UpdateResult deleteImportTrTerminationRegistInfo(Map<String, Object> params) throws Exception {
		return super.update("DELETE FROM app_termination_regist_info where register_serno=$S{registerSerno} ", params);
	}
}
