package com.kayak.report.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.report.dao.ReportDataLockConfigDao;
import com.kayak.report.model.ReportDataLockConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "报送数据锁表管理配置", model = ReportDataLockConfig.class)
public class ReportDataLockConfigService {

	@Autowired
	private ReportDataLockConfigDao reportDataLockConfigDao;

	@API(desc = "查询报送数据锁表管理配置信息", auth = APIAuth.YES)
	public SqlResult<ReportDataLockConfig> findReportDataLockConfigInfo(SqlParam<ReportDataLockConfig> params) throws Exception {
		return reportDataLockConfigDao.findReportDataLockConfigInfo(params);
	}

	@API(desc = "根据报表大类过滤报表名称",  auth = APIAuth.NO)
	public SqlResult<ReportDataLockConfig> getReportTable(SqlParam<ReportDataLockConfig> params) throws Exception {
		return reportDataLockConfigDao.getReportTable(params);
	}


}
