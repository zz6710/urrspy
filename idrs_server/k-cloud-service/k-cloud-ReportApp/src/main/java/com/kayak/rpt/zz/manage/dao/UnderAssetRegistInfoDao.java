package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.zz.manage.model.AssetRegistInfo;
import com.kayak.rpt.zz.manage.model.UnderAssetRegistInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Repository
public class UnderAssetRegistInfoDao extends ComnDao {

	public SqlResult<UnderAssetRegistInfo> findUnderAssetRegistInfos(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		String reportDate = params.getModel().getStartDate();
		String assetManagerCode = params.getModel().getAssetManagerCode();
		String underAssetCode = params.getModel().getUnderAssetCode();
		String registerStatus = params.getModel().getRegisterStatus();
		String registerSerno = params.getModel().getRegisterSerno();
 		String sql = "SELECT T1.BANK_CODE,T1.asset_manager_code,T1.convert_sum_amt,T1.asset_sum_number,T1.non_invested_amt,T1.under_asset_code,T1.under_asset_sum,T1.under_convert_sum_amt,T1.report_date,T1.register_serno,T1.imp_date,T1.register_date,T1.register_status,T1.create_date,T1.theory_report_start_date,T1.theory_report_end_date,T1.sys_data_source,T1.sys_data_status,T1.sys_data_version,ifnull(ARS.audit_status,0) audit_status FROM";
		sql += " app_under_asset_regist_info";
		sql += " T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_under_asset_regist_info' where T1.sys_data_status ='1' ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and T1.report_date = '" + reportDate + "' ";
		}
		if (Strings.isNotBlank(assetManagerCode)) {
			sql = sql + " and  T1.asset_manager_code = '" + assetManagerCode + "'";
		}
		if (Strings.isNotBlank(underAssetCode)) {
			sql += " and T1.under_asset_code = '" + underAssetCode + "' ";
		}
		if (Strings.isNotBlank(registerStatus)) {
			sql += " and T1.register_status = '" + registerStatus + "' ";
		}
		if (Strings.isNotBlank(registerSerno)) {
			sql += " and T1.register_serno = '" + registerSerno + "' ";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public int findUnderAssetRegistInfosCount(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		String reportDate = params.getModel().getStartDate();
		String assetManagerCode = params.getModel().getAssetManagerCode();
		String underAssetCode = params.getModel().getUnderAssetCode();
		String registerStatus = params.getModel().getRegisterStatus();
		String registerSerno = params.getModel().getRegisterSerno();
		String sql = "SELECT count(1) FROM ";
		sql += " app_under_asset_regist_info";
		sql += " T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_under_asset_regist_info' where T1.sys_data_status ='1' ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and T1.report_date = '" + reportDate + "' ";
		}
		if (Strings.isNotBlank(assetManagerCode)) {
			sql = sql + " and  T1.asset_manager_code = '" + assetManagerCode + "'";
		}
		if (Strings.isNotBlank(underAssetCode)) {
			sql += " and T1.under_asset_code = '" + underAssetCode + "' ";
		}
		if (Strings.isNotBlank(registerStatus)) {
			sql += " and T1.register_status = '" + registerStatus + "' ";
		}
		if (Strings.isNotBlank(registerSerno)) {
			sql += " and T1.register_serno = '" + registerSerno + "' ";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public SqlResult<UnderAssetRegistInfo> findUnderAssetRegistInfos_day(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		String startDate = params.getModel().getStartDate();
		String lastCycleDay = DateUtil.getLastCycleDay(startDate,0, 1);
		String sql = "SELECT T1.BANK_CODE,T1.asset_manager_code,T1.convert_sum_amt,T1.asset_sum_number,T1.non_invested_amt,T1.under_asset_code,T1.under_asset_sum,T1.under_convert_sum_amt,T1.report_date,T1.register_serno,T1.imp_date,T1.register_date,T1.register_status,T1.create_date,T1.theory_report_start_date,T1.theory_report_end_date,T1.sys_data_source,T1.sys_data_status,T1.sys_data_version,ifnull(ARS.audit_status,0) audit_status FROM";
		String reportDate = startDate;
		sql += " app_under_asset_regist_info_day";
		sql += " T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_under_asset_regist_info' where t1.sys_data_status ='1' ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and T1.report_date like '" + reportDate + "%' ";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addUnderAssetRegistInfo(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		return super.update("INSERT INTO app_under_asset_regist_info(create_date,bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status,theory_report_start_date) VALUES(date_format(CURDATE(),'%Y%m%d'),$S{bankCode},$S{assetManagerCode},$D{convertSumAmt},$D{assetSumNumber},$D{nonInvestedAmt},$S{underAssetCode},$D{underAssetSum},$D{underConvertSumAmt},$S{reportDate},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',$S{reportDate})",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult updateUnderAssetRegistInfo(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		return super.update("UPDATE app_under_asset_regist_info SET bank_code=$S{bankCode} ,asset_manager_code=$S{assetManagerCode} ,convert_sum_amt=$D{convertSumAmt} ,asset_sum_number=$D{assetSumNumber} ,non_invested_amt=$D{nonInvestedAmt} ,under_asset_code=$S{underAssetCode} ,under_asset_sum=$D{underAssetSum} ,under_convert_sum_amt=$D{underConvertSumAmt} ,report_date=$S{reportDate} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate}  WHERE register_serno=$S{registerSerno}",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteUnderAssetRegistInfo(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		return super.update("DELETE FROM app_under_asset_regist_info WHERE register_serno=$S{registerSerno}  " ,
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportUnderAssetRegistInfo(Object param) throws Exception {
		return super.update("INSERT INTO app_under_asset_regist_info(create_date,bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status,theory_report_start_date,sys_data_status) VALUES(date_format(CURDATE(),'%Y%m%d'),$S{bankCode},$S{assetManagerCode},$D{convertSumAmt},$D{assetSumNumber},$D{nonInvestedAmt},$S{underAssetCode},$D{underAssetSum},$D{underConvertSumAmt},$S{reportDate},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),$S{registerDate},'0',$S{theoryReportStartDate},'1')",
				DataSourceProperty.PUB,param);
	}

    public UpdateResult deleteImportUnderAssetRegistInfo(Map<String, Object> params) throws Exception {
		return super.update("DELETE FROM app_under_asset_regist_info where theory_report_start_date between $S{beginDate} and $S{queryDate} ", params);
    }

	public int findUnderAssetRegistInfoFailStatus(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		String startDate = params.getModel().getStartDate();
		String assetManagerCode = params.getModel().getAssetManagerCode();
		String underAssetCode = params.getModel().getUnderAssetCode();
		String registerStatus = params.getModel().getRegisterStatus();
		String registerSerno = params.getModel().getRegisterSerno();
		String sql = "SELECT count(1) FROM ";
		String reportDate = startDate;
		sql += " app_under_asset_regist_info";
		sql += " T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_under_asset_regist_info' where t1.sys_data_status ='1' and t1.register_status in (0,1) ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and T1.report_date like '" + reportDate + "%' ";
		}
		if (Strings.isNotBlank(assetManagerCode)) {
			sql = sql + " and  T1.asset_manager_code = '" + assetManagerCode + "'";
		}
		if (Strings.isNotBlank(underAssetCode)) {
			sql += " and T1.under_asset_code = '" + underAssetCode + "' ";
		}
		if (Strings.isNotBlank(registerStatus)) {
			sql += " and T1.register_status = '" + registerStatus + "' ";
		}
		if (Strings.isNotBlank(registerSerno)) {
			sql += " and T1.register_serno = '" + registerSerno + "' ";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public UpdateResult updateUnderAssetRegistInfoStatus(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		String holdDate=params.getModel().getStartDate();
		String assetManagerCode = params.getModel().getAssetManagerCode();
		String underAssetCode = params.getModel().getUnderAssetCode();
		String registerStatus = params.getModel().getRegisterStatus();
		String registerSerno = params.getModel().getRegisterSerno();
		String sql = "update app_under_asset_regist_info SET register_status='3' WHERE sys_data_status ='1' and report_date='"+holdDate+"' ";
		if (Strings.isNotBlank(assetManagerCode)) {
			sql = sql + " and  asset_manager_code = '" + assetManagerCode + "'";
		}
		if (Strings.isNotBlank(underAssetCode)) {
			sql += " and under_asset_code = '" + underAssetCode + "' ";
		}
		if (Strings.isNotBlank(registerStatus)) {
			sql += " and register_status = '" + registerStatus + "' ";
		}
		if (Strings.isNotBlank(registerSerno)) {
			sql += " and register_serno = '" + registerSerno + "' ";
		}
		return super.update(sql, DataSourceProperty.PUB,params.getModel());
	}
	public void updateBaseReportResultInfo(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		String holdDate=params.getModel().getStartDate();
		String sql="update base_report_result set register_date = theory_report_start_date,report_success_number=total,status= '1',register_status= '1',update_date=date_format(now(),'%Y%m%d'),update_time=date_format(now(),'%H%i%s') where report_table = 'app_under_asset_regist_info' and theory_report_start_date in (select theory_report_start_date from app_under_asset_regist_info where report_date='"+holdDate+"' and sys_data_status='1') ";
		super.update(sql, DataSourceProperty.PUB, params.getModel());
	}
}
