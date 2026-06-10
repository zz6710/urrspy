package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.dao.BaseReportExportLogDao;
import com.kayak.rpt.zz.manage.model.BaseReportExportLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "报表导出审批历史表服务", model = BaseReportExportLog.class)
@Slf4j
public class BaseReportExportLogService {

	@Autowired
	private BaseReportExportLogDao baseReportExportLogDao;

	@API(desc = "投资者报表文件下载", operation = APIOperation.UPDATE, auth = APIAuth.YES)
	public SqlResult<BaseReportExportLog> findBaseReportExportLogs(SqlParam<BaseReportExportLog> params) throws Exception {
		params.setMakeSql(false);
		String userId = (String) SysUtil.getSysUserParamValue("sys_user_userid");
		if (Tools.strIsEmpty(userId)) {
			return new SqlResult<>();
		}
		params.getModel().setUserid(userId);
		return baseReportExportLogDao.findBaseReportExportLogs(params);
	}

	@API(desc = "投资者报表文件下载状态查询", auth = APIAuth.NO)
	public String fileStatusQuery(SqlParam<BaseReportExportLog> params) throws Exception {
		Map<String, Object> returndata = new HashMap<>();
		try {
			params.setMakeSql(false);
			params.getModel().setFileStatus("1");
			params.getModel().setDataStatus("3");
			params.getModel().setApplyTime("30");
			SqlResult<BaseReportExportLog> sqlRowList = baseReportExportLogDao.findBaseReportExportLogsLc(params);
			List<BaseReportExportLog> list = sqlRowList.getRows();
			if (CollectionUtil.isNotEmpty(list)) {
				returndata.put("flag", "2");
				return RequestSupport.updateReturnJson(true, "正在导出"+params.getModel().getReportName()+"报表，请稍后重试！", returndata).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			returndata.put("flag", "1");
			return RequestSupport.updateReturnJson(false, e.getMessage(), returndata).toString();
		}
		returndata.put("flag", "0");
		returndata.put("reportDate", getReportDateBetween(params.getModel().getReportStartDate(), params.getModel().getReportEndDate()));
		return RequestSupport.updateReturnJson(true, "", returndata).toString();
	}

	@API(desc = "添加报表导出审批历史表", params = "id,userid,report_id,apply_time,data_time,data_status,process_instance_id,create_by,create_time,update_by,update_time,report_name,file_path,file_status,remote_path", auth = APIAuth.NO)
	public int addBaseReportExportLog(SqlParam<BaseReportExportLog> params) throws Exception {
		return baseReportExportLogDao.addBaseReportExportLog(params).getEffect();
	}
	
	@API(desc = "修改报表导出审批历史表", params = "id,userid,report_id,apply_time,data_time,data_status,process_instance_id,create_by,create_time,update_by,update_time,report_name,file_path,file_status,remote_path", auth = APIAuth.NO)
	public int updateBaseReportExportLog(SqlParam<BaseReportExportLog> params) throws Exception {
		return baseReportExportLogDao.updateBaseReportExportLog(params).getEffect();
	}
	
	@API(desc = "删除报表导出审批历史表", params = "id,userid,report_id,apply_time,data_time,data_status,process_instance_id,create_by,create_time,update_by,update_time,report_name,file_path,file_status,remote_path", auth = APIAuth.NO)
	public int deleteBaseReportExportLog(SqlParam<BaseReportExportLog> params) throws Exception {
		return baseReportExportLogDao.deleteBaseReportExportLog(params).getEffect();
	}

	public int updateFilePath(BaseReportExportLog exportLog) throws Exception {
		return baseReportExportLogDao.updateFilePath(exportLog).getEffect();
	}

	public int updateFileNamePath(BaseReportExportLog exportLog) throws Exception {
		return baseReportExportLogDao.updateFileNamePath(exportLog).getEffect();
	}

	public int updateDataTimePath(BaseReportExportLog exportLog) throws Exception {
		return baseReportExportLogDao.updateDataTimePath(exportLog).getEffect();
	}

	public String getReportDateBetween(String startStr, String endStr) throws Exception {
		StringBuilder dates = new StringBuilder();
		if (StringUtils.isNotEmpty(startStr) && StringUtils.isNotEmpty(endStr)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate start = LocalDate.parse(startStr, formatter);
			LocalDate end = LocalDate.parse(endStr, formatter);
			LocalDate current = start;
			while (!current.isAfter(end)) {
				// 直接转换为字符串并添加到列表
				if (dates.length() == 0) {
					dates.append(current.format(formatter));
				} else {
					dates.append("," + current.format(formatter));
				}
				current = current.plusDays(1);
			}
		}
		return dates.toString();
	}

}
