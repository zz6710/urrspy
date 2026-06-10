package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.AppraiseRegistInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;
import java.util.Map;

@Repository
public class AppraiseRegistInfoDao extends ComnDao {

	public SqlResult<AppraiseRegistInfo> findAppraiseRegistInfos(SqlParam<AppraiseRegistInfo> params) throws Exception {
		String sql = "SELECT bank_code,asset_code,valuation_date,unit_debt_net,unit_debt_full,details,register_serno,imp_date,register_date,register_status,create_date,theory_report_start_date,theory_report_end_date,ARS.audit_status FROM app_appraise_regist_info T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_appraise_regist_info' where t1.sys_data_status ='1'";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(valuation_date) >= DATE($S{startDate}) and DATE(valuation_date) <= DATE($S{endDate})";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  register_serno = '" + params.getModel().getRegisterSerno() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryStartDate())) {
			sql = sql + " and  theory_report_start_date >= '" + params.getModel().getQueryStartDate() + "' and theory_report_start_date <= '"+params.getModel().getQueryEndDate() + "'";
		}
		return super.findRows(sql, DataSourceProperty.PUB,  params);
	}

	public UpdateResult addAppraiseRegistInfo(SqlParam<AppraiseRegistInfo> params) throws Exception {
		return super.update("INSERT INTO app_appraise_regist_info(bank_code,asset_code,valuation_date,unit_debt_net,unit_debt_full,details,register_serno,imp_date,register_date,register_status,create_date,theory_report_start_date) VALUES($S{bankCode},$S{assetCode},$S{valuationDate},$D{unitDebtNet},$D{unitDebtFull},$S{details},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',date_format(CURDATE(),'%Y%m%d'),DATE_FORMAT($S{valuationDate},'%Y%m%d'))",
				DataSourceProperty.PUB, params.getModel());
	}
	
	public UpdateResult updateAppraiseRegistInfo(SqlParam<AppraiseRegistInfo> params) throws Exception {
		return super.update("UPDATE app_appraise_regist_info SET bank_code=$S{bankCode} ,asset_code=$S{assetCode} ,valuation_date=$S{valuationDate} ,unit_debt_net=$D{unitDebtNet} ,unit_debt_full=$D{unitDebtFull} ,details=$S{details} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus}  WHERE  register_serno=$S{registerSerno} ",
				DataSourceProperty.PUB, params.getModel());
	}
	
	public UpdateResult deleteAppraiseRegistInfo(SqlParam<AppraiseRegistInfo> params) throws Exception {
		return super.update("DELETE FROM app_appraise_regist_info WHERE register_serno=$S{registerSerno}",
				DataSourceProperty.PUB, params.getModel());
	}


	public UpdateResult addImportAppraiseRegistInfo(Object params) throws Exception {
		return super.update("INSERT INTO app_appraise_regist_info(bank_code,asset_code,valuation_date,unit_debt_net,unit_debt_full,details,register_serno,imp_date,register_date,register_status,create_date,theory_report_start_date) VALUES($S{bankCode},$S{assetCode},$S{valuationDate},$D{unitDebtNet},$D{unitDebtFull},$S{details},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),$S{registerDate},'0',date_format(CURDATE(),'%Y%m%d'),$S{valuationDate})",
				DataSourceProperty.PUB, params);
	}

    public UpdateResult deleteImportAppraiseRegistInfo(Object params) throws Exception {
		return super.update("DELETE FROM app_appraise_regist_info where theory_report_start_date between $S{beginDate} and $S{queryDate} ", params);
    }
}
