package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.ProdConsignmentSales;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ProdConsignmentSalesDao extends ComnDao {

	public SqlResult<ProdConsignmentSales> findProdConsignmentSales(SqlParam<ProdConsignmentSales> params) throws Exception {
		String sql = "SELECT id,establish_date,bank_code,prod_reg_enc,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date,register_date FROM app_prod_consignment_sales where sys_data_status = '1'";
		if (StringUtils.isNotBlank(params.getModel().getStartDate())) {
			sql = sql + " and report_date >= '" + params.getModel().getStartDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getEndDate())) {
			sql = sql + " and report_date <= '" + params.getModel().getEndDate() + "'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addProdConsignmentSales(SqlParam<ProdConsignmentSales> params) throws Exception {
		return super.update("INSERT INTO app_prod_consignment_sales(id,establish_date,bank_code,prod_reg_enc,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date,register_date) VALUES($AUTOIDI{id},$S{establishDate},$S{bankCode},$S{prodRegEnc},$S{createDate},$S{theoryReportStartDate},$S{theoryReportEndDate},$S{impDate},$S{registerStatus},$S{sysDataStatus},$S{sysDataVersion},$S{sysDataSource},$S{registerSerno},$S{reportDate},$S{registerDate})",
				params.getModel());
	}
	
	public UpdateResult updateProdConsignmentSales(SqlParam<ProdConsignmentSales> params) throws Exception {
		return super.update("UPDATE app_prod_consignment_sales SET establish_date=$S{establishDate} ,bank_code=$S{bankCode} ,prod_reg_enc=$S{prodRegEnc} ,create_date=$S{createDate} ,theory_report_start_date=$S{theoryReportStartDate} ,theory_report_end_date=$S{theoryReportEndDate} ,imp_date=$S{impDate} ,register_status=$S{registerStatus} ,sys_data_status=$S{sysDataStatus} ,sys_data_version=$S{sysDataVersion} ,sys_data_source=$S{sysDataSource} ,register_serno=$S{registerSerno} ,report_date=$S{reportDate} ,register_date=$S{registerDate}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteProdConsignmentSales(SqlParam<ProdConsignmentSales> params) throws Exception {
		return super.update("DELETE FROM app_prod_consignment_sales WHERE  id=$I{id} ",
				params.getModel());
	}

}
