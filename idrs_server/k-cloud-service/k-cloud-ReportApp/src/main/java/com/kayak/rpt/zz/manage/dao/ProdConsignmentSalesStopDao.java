package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.ProdConsignmentSalesStop;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ProdConsignmentSalesStopDao extends ComnDao {

	public SqlResult<ProdConsignmentSalesStop> findProdConsignmentSalesStops(SqlParam<ProdConsignmentSalesStop> params) throws Exception {
		String sql = "SELECT id,end_date,bank_code,prod_reg_enc,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date,register_date FROM app_prod_consignment_sales_stop where sys_data_status = '1' ";
		if (StringUtils.isNotBlank(params.getModel().getStartDate())) {
			sql = sql + " and report_date >= '" + params.getModel().getStartDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getEndDate())) {
			sql = sql + " and report_date <= '" + params.getModel().getEndDate() + "'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addProdConsignmentSalesStop(SqlParam<ProdConsignmentSalesStop> params) throws Exception {
		return super.update("INSERT INTO app_prod_consignment_sales_stop(id,end_date,bank_code,prod_reg_enc,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date,register_date) VALUES($AUTOIDI{id},$S{endDate},$S{bankCode},$S{prodRegEnc},$S{createDate},$S{theoryReportStartDate},$S{theoryReportEndDate},$S{impDate},$S{registerStatus},$S{sysDataStatus},$S{sysDataVersion},$S{sysDataSource},$S{registerSerno},$S{reportDate},$S{registerDate})",
				params.getModel());
	}
	
	public UpdateResult updateProdConsignmentSalesStop(SqlParam<ProdConsignmentSalesStop> params) throws Exception {
		return super.update("UPDATE app_prod_consignment_sales_stop SET end_date=$S{endDate} ,bank_code=$S{bankCode} ,prod_reg_enc=$S{prodRegEnc} ,create_date=$S{createDate} ,theory_report_start_date=$S{theoryReportStartDate} ,theory_report_end_date=$S{theoryReportEndDate} ,imp_date=$S{impDate} ,register_status=$S{registerStatus} ,sys_data_status=$S{sysDataStatus} ,sys_data_version=$S{sysDataVersion} ,sys_data_source=$S{sysDataSource} ,register_serno=$S{registerSerno} ,report_date=$S{reportDate} ,register_date=$S{registerDate}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteProdConsignmentSalesStop(SqlParam<ProdConsignmentSalesStop> params) throws Exception {
		return super.update("DELETE FROM app_prod_consignment_sales_stop WHERE  id=$I{id} ",
				params.getModel());
	}

}
