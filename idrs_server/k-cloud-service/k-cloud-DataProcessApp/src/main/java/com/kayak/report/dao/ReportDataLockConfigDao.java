package com.kayak.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.report.model.ReportDataLockConfig;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDataLockConfigDao extends ComnDao {

	public SqlResult<ReportDataLockConfig> findReportDataLockConfigInfo(SqlParam<ReportDataLockConfig> params) throws Exception {
		String sql = "SELECT a.report_category,a.report_table,a.table_name,a.task_id,a.task_name,a.latest_date,a.is_lst_lock,group_concat(a.upper_grade) as upper_grade  " +
				"       FROM base_report_data_lock_config a " +
				"      WHERE 1=1 " +
				"        AND a.report_category <> '8' /*自营报表先排除*/";
		if (StringUtils.isNotBlank(params.getModel().getReportCategory())) {
			sql = sql + " and a.report_category = '" + params.getModel().getReportCategory() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportTable())) {
			sql = sql + " and a.report_table = '" + params.getModel().getReportTable() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getUpperGrade())) {
			sql = sql + " and a.upper_grade = '" + params.getModel().getUpperGrade() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getIsLstLock())) {
			sql = sql + " and a.is_lst_lock = '" + params.getModel().getIsLstLock() + "'";
		}
		sql = sql + " group by a.report_category,a.report_table,a.table_name,a.task_id,a.task_name,a.latest_date,a.is_lst_lock  order by a.report_category, a.report_table ";
		return super.findRows(sql, params);
	}

	/**
	 * 获取报表数据字典
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<ReportDataLockConfig> getReportTable(SqlParam<ReportDataLockConfig> params) throws Exception {
		String sql = " select report_table ,table_name  from base_report_info " +
				"       where 1=1 and report_freq in ('2','3') ";/*仅查询月度or季度报表*/
		if (Strings.isNotBlank(params.getModel().getReportCategory())) {
			sql += " and report_catgory = $S{reportCategory}";
		}
		if (Strings.isNotBlank(params.getModel().getReportTable())) {
			sql += " and report_table = $S{reportTable}";
		}
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}




}
