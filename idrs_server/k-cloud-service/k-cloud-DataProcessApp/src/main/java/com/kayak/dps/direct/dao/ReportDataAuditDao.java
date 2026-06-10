package com.kayak.dps.direct.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.dps.check.model.T8SqlParamInfo;
import com.kayak.dps.direct.model.ReportDataAudit;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDataAuditDao extends ComnDao {

	public SqlResult<ReportDataAudit> findReportDataAudits(SqlParam<ReportDataAudit> params) throws Exception {
		StringBuilder sql = new StringBuilder("select id,table_id,table_name,begin_date,end_date,date_type,audit_date,audit_status from base_report_data_audit where 1=1");
		if (Tools.isNotEmpty(params.getModel().getTableId())) {
			sql.append(" and table_id = '").append(params.getModel().getTableId()).append("'");
		}
		if (Tools.isNotEmpty(params.getModel().getAuditDate())) {
			sql.append(" and audit_date = '").append(params.getModel().getAuditDate()).append("'");
		}
		sql.append(" order by audit_date desc");
		return super.findRows(sql.toString(), params);
	}

}
