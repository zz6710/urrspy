package com.kayak.dps.direct.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.check.dao.T8SqlParamInfoDao;
import com.kayak.dps.check.model.T8SqlParamInfo;
import com.kayak.dps.direct.dao.ReportDataAuditDao;
import com.kayak.dps.direct.model.ReportDataAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "SQL语句参数配置服务", model = ReportDataAudit.class)
public class ReportDataAuditService {

	@Autowired
	private ReportDataAuditDao reportDataAuditDao;

	@API(desc = "查询报表数据复核状态", auth = APIAuth.NO)
	public SqlResult<ReportDataAudit> findReportDataAudits(SqlParam<ReportDataAudit> params) throws Exception {
		return reportDataAuditDao.findReportDataAudits(params);
	}

}
