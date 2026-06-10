package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.app.model.BaseReportResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class BaseReportResultDesktopDao extends ComnDao {

	public SqlResult<BaseReportResult> findReportResultInfo(SqlParam<BaseReportResult> params) throws Exception {
		String sql = "SELECT res.report_type,res.report_table,res.report_table_name,res.theory_report_end_date,res.theory_report_start_date,total,res.report_success_number,res.status,res.register_status,(total-report_success_number) need_total FROM base_report_result res WHERE  STATUS='2'";
		if (StringUtils.isNotBlank(params.getModel().getTheoryReportStartDate())) {
			sql = sql + " and res.theory_report_start_date ='" + params.getModel().getTheoryReportStartDate() + "'";
		}
		return super.findRows(sql, params);
	}

	public SqlResult<BaseReportResult> findNextReportResultInfo(SqlParam<BaseReportResult> params) throws Exception {
		String sql = "SELECT res.report_type,res.report_table,res.report_table_name,res.theory_report_end_date,res.theory_report_start_date,total,res.report_success_number,res.status,res.register_status,(total-report_success_number) need_total FROM base_report_result res WHERE  STATUS='2'";
		if (StringUtils.isNotBlank(params.getModel().getTheoryReportStartDate())) {
			sql = sql + " and res.theory_report_end_date <'" + params.getModel().getTheoryReportStartDate() + "'";
		}
		return super.findRows(sql, params);
	}

	public SqlResult<BaseReportResult> findTodayReportResultInfo(SqlParam<BaseReportResult> params) throws Exception {
		String sql = "SELECT res.report_type,res.report_table,res.report_table_name,res.theory_report_end_date,res.theory_report_start_date,total,res.report_success_number,res.status,res.register_status,(total-report_success_number) need_total FROM base_report_result res WHERE  STATUS='1' ";
		if (StringUtils.isNotBlank(params.getModel().getTheoryReportStartDate())) {
			sql = sql + " and res.theory_report_start_date ='" + params.getModel().getTheoryReportStartDate() + "'";
		}
		return super.findRows(sql, params);
	}

}
