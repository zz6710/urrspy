package com.kayak.dps.app.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.model.ImportTemplateDataLog;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ImportTemplateDataLogDao extends ComnDao {

	public SqlResult<ImportTemplateDataLog> findImportTemplateDataLogs(SqlParam<ImportTemplateDataLog> params) throws Exception {
		String sql = "SELECT a.id,a.import_template_manage_id,a.report_date,a.sys_data_version,a.imp_date," +
				" b.template_name," +
				" concat(c.system_table_name_cn, c.system_table_name) as table_name" +
				" FROM import_template_data_log a" +
				" left join import_template_manage b on a.import_template_manage_id = b.id" +
				" left join app_table_info c on b.system_table_name = c.id" +
				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getTableName())) {
			sql += " and c.id = " + params.getModel().getTableName();
		}
		if (Tools.isNotEmpty(params.getModel().getTemplateName())) {
			sql += " and b.template_name like '%" + params.getModel().getTemplateName() + "%'";
		}
		if (Tools.isNotEmpty(params.getModel().getReportDate())) {
			sql += " and a.report_date = '" + params.getModel().getReportDate() + "'";
		}
		sql += " order by a.imp_date desc, a.sys_data_version desc";
		return super.findRows(sql, params);
	}

	public UpdateResult addImportTemplateDataLog(SqlParam<ImportTemplateDataLog> params) throws Exception {
		return super.update("INSERT INTO import_template_data_log(id,import_template_manage_id,report_date,sys_data_version,imp_date) VALUES($AUTOIDI{id},$S{importTemplateManageId},$S{reportDate},$S{sysDataVersion},$S{impDate})",
				params.getModel());
	}

	public UpdateResult updateImportTemplateDataLog(SqlParam<ImportTemplateDataLog> params) throws Exception {
		return super.update("UPDATE import_template_data_log SET import_template_manage_id=$S{importTemplateManageId} ,report_date=$S{reportDate} ,sys_data_version=$S{sysDataVersion} ,imp_date=$S{impDate}  WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteImportTemplateDataLog(SqlParam<ImportTemplateDataLog> params) throws Exception {
		return super.update("DELETE FROM import_template_data_log WHERE  id=$I{id} ",
				params.getModel());
	}

}
