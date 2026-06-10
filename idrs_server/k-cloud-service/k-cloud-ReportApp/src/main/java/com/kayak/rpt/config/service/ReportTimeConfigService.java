package com.kayak.rpt.config.service;

import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.config.model.ReportTimeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.config.dao.ReportTimeConfigDao;

import java.util.List;


@Service
@APIDefine(desc = "报送时点配置服务", model = ReportTimeConfig.class)
public class ReportTimeConfigService {

	@Autowired
	private ReportTimeConfigDao reportTimeConfigDao;

	@API(desc = "查询报送时点配置信息", auth = APIAuth.YES)
	public SqlResult<ReportTimeConfig> findReportTimeConfigs(SqlParam<ReportTimeConfig> params) throws Exception {
		params.setMakeSql(true);
		SqlResult<ReportTimeConfig> result = reportTimeConfigDao.findReportTimeConfigs(params);
		// 获取中文名
		List<ReportTimeConfig> resultRows = result.getRows();
		if (resultRows.size() <= 0) {
			return result;
		}
		resultRows.forEach(a -> {
			try {
				 a.setTableName(reportTimeConfigDao.findChineseReportName(a).getString("table_name"));
				 if("1".equals(a.getTimeType())) { //当时点类型为1 非规则配置类型时，进行查询操作
					 a.setEndDateString(reportTimeConfigDao.getEndDateList(a.getReportTable())); //获取截止日期列表
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		result.setRows(resultRows);
		return result;
	}

	@API(desc = "添加报送时点配置", params = "id,report_type,report_table,base_type,data_type,inner_submission_time_require,supervise_submission_time_require,create_date,update_date,time_type,time_type_value", auth = APIAuth.YES)
	public String addReportTimeConfig(SqlParam<ReportTimeConfig> params) throws Exception {
		String innerSubmissionTime = params.getModel().getInnerSubmissionTime();
		String superviseSubmissionTime = params.getModel().getSuperviseSubmissionTime();
		String dataGenerTime = params.getModel().getDataGenerTime();
		// 判断正负号标识
		if ("1".equals(innerSubmissionTime)) {
			innerSubmissionTime = "-";
		} else {
			innerSubmissionTime = "+";
		}
		if ("1".equals(superviseSubmissionTime)) {
			superviseSubmissionTime = "-";
		} else {
			superviseSubmissionTime = "+";
		}
		if ("1".equals(dataGenerTime)) {
			dataGenerTime = "-";
		} else {
			dataGenerTime = "+";
		}
		String innerSubmissionTimeRequire = params.getModel().getInnerSubmissionTimeRequire();
		String superviseSubmissionTimeRequire = params.getModel().getSuperviseSubmissionTimeRequire();
		String dataGenerTimeRequire = params.getModel().getDataGenerTimeRequire();
		params.getModel().setInnerSubmissionTimeRequire(innerSubmissionTime+innerSubmissionTimeRequire);
		params.getModel().setSuperviseSubmissionTimeRequire(superviseSubmissionTime+superviseSubmissionTimeRequire);
		params.getModel().setDataGenerTimeRequire(dataGenerTime+dataGenerTimeRequire);
		SqlResult<ReportTimeConfig>timeList = reportTimeConfigDao.getReportTimeConfig(params);
		if(timeList.getRows().size()>0){
			return RequestSupport.updateReturnJson(false, "报表报送时点已配置!", null).toString();
		}else{
			reportTimeConfigDao.addReportTimeConfig(params);
			return RequestSupport.updateReturnJson(true, "报送时点配置成功!", null).toString();
		}

	}
	
	@API(desc = "修改报送时点配置", params = "id,report_type,report_table,base_type,data_type,inner_submission_time_require,supervise_submission_time_require,create_date,update_date,time_type,time_type_value", auth = APIAuth.YES)
	public int updateReportTimeConfig(SqlParam<ReportTimeConfig> params) throws Exception {
		String innerSubmissionTime = params.getModel().getInnerSubmissionTime();
		String superviseSubmissionTime = params.getModel().getSuperviseSubmissionTime();
		String dataGenerTime = params.getModel().getDataGenerTime();
		// 判断正负号标识
		if ("1".equals(innerSubmissionTime)) {
			innerSubmissionTime = "-";
		} else {
			innerSubmissionTime = "+";
		}
		if ("1".equals(superviseSubmissionTime)) {
			superviseSubmissionTime = "-";
		} else {
			superviseSubmissionTime = "+";
		}
		if ("1".equals(dataGenerTime)) {
			dataGenerTime = "-";
		} else {
			dataGenerTime = "+";
		}
		String innerSubmissionTimeRequire = params.getModel().getInnerSubmissionTimeRequire();
		String superviseSubmissionTimeRequire = params.getModel().getSuperviseSubmissionTimeRequire();
		String dataGenerTimeRequire = params.getModel().getDataGenerTimeRequire();
		params.getModel().setInnerSubmissionTimeRequire(innerSubmissionTime+innerSubmissionTimeRequire);
		params.getModel().setSuperviseSubmissionTimeRequire(superviseSubmissionTime+superviseSubmissionTimeRequire);
		params.getModel().setDataGenerTimeRequire(dataGenerTime+dataGenerTimeRequire);
		return reportTimeConfigDao.updateReportTimeConfig(params).getEffect();
	}
	
	@API(desc = "删除报送时点配置", params = "id,report_type,report_table,base_type,data_type,inner_submission_time_require,supervise_submission_time_require,create_date,update_date", auth = APIAuth.YES)
	public int deleteReportTimeConfig(SqlParam<ReportTimeConfig> params) throws Exception {
		return reportTimeConfigDao.deleteReportTimeConfig(params).getEffect();
	}

	@API(desc = "根据报表大类过滤报表名称",  auth = APIAuth.NO)
	public SqlResult<ReportTimeConfig> getReportTable(SqlParam<ReportTimeConfig> params) throws Exception {
		return reportTimeConfigDao.getReportTable(params);
	}

	@API(desc = "根据报表大类过滤配置了校验规则的报表名称",  auth = APIAuth.NO)
	public SqlResult<ReportTimeConfig> getValidReportTable(SqlParam<ReportTimeConfig> params) throws Exception {
		return reportTimeConfigDao.getValidReportTable(params);
	}
}
