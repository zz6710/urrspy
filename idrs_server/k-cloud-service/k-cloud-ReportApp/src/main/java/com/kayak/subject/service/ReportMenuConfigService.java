package com.kayak.subject.service;

import com.kayak.core.sql.SqlRow;
import com.kayak.subject.dao.ReportMenuConfigDao;
import com.kayak.subject.model.ReportMenuConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "报表页面清算任务配置服务", model = ReportMenuConfig.class)
public class ReportMenuConfigService {

	@Autowired
	private ReportMenuConfigDao reportMenuConfigDao;

	@API(desc = "查询报表页面清算任务配置信息", auth = APIAuth.NO)
	public SqlResult<ReportMenuConfig> findReportMenuConfigs(SqlParam<ReportMenuConfig> params) throws Exception {
		params.setMakeSql(true);
		return reportMenuConfigDao.findReportMenuConfigs(params);
	}

	@API(desc = "查询报表页面清算任务配置信息", auth = APIAuth.NO)
	public SqlResult<ReportMenuConfig> findReportMenuConfigs1(SqlParam<ReportMenuConfig> params) throws Exception {
		params.setMakeSql(true);
		SqlResult<ReportMenuConfig> sqlResult = reportMenuConfigDao.findReportMenuConfigs1(params);

		ReportMenuConfig reportMenuConfig = new ReportMenuConfig();
		reportMenuConfig.setId("0");
		reportMenuConfig.setName("全部");
		reportMenuConfig.setChildren(sqlResult.getRows());

		List<ReportMenuConfig> reportMenuConfigList = new ArrayList<>();
		reportMenuConfigList.add(reportMenuConfig);
		sqlResult.setRows(reportMenuConfigList);

		return sqlResult;
	}

	public List<SqlRow> findReportMenuConfigs(Map<String, Object> params) throws Exception {
		return reportMenuConfigDao.findReportMenuConfigs(params);
	}

	@API(desc = "添加报表页面清算任务配置", params = "id,menu_id,menu_name,button_name,task_id,task_name,task_type,report_type,report_code,report_value,is_use,is_show,sort,create_date", auth = APIAuth.NO)
	public int addReportMenuConfig(SqlParam<ReportMenuConfig> params) throws Exception {
		return reportMenuConfigDao.addReportMenuConfig(params).getEffect();
	}
	
	@API(desc = "修改报表页面清算任务配置", params = "id,menu_id,menu_name,button_name,task_id,task_name,task_type,report_type,report_code,report_value,is_use,is_show,sort,create_date", auth = APIAuth.NO)
	public int updateReportMenuConfig(SqlParam<ReportMenuConfig> params) throws Exception {
		return reportMenuConfigDao.updateReportMenuConfig(params).getEffect();
	}
	
	@API(desc = "删除报表页面清算任务配置", params = "id,menu_id,menu_name,button_name,task_id,task_name,task_type,report_type,report_code,report_value,is_use,is_show,sort,create_date", auth = APIAuth.NO)
	public int deleteReportMenuConfig(SqlParam<ReportMenuConfig> params) throws Exception {
		return reportMenuConfigDao.deleteReportMenuConfig(params).getEffect();
	}

}
