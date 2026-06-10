package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.BaseReportExportLog;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class BaseReportExportLogDao extends ComnDao {

	public SqlResult<BaseReportExportLog> findBaseReportExportLogs(SqlParam<BaseReportExportLog> params) throws Exception {
		String sql = "SELECT a.id,a.userid,a.report_id,a.apply_time,a.data_time,a.process_instance_id,a.create_by,a.create_time,a.update_by,a.update_time,a.report_name,a.file_path,a.file_status,a.remote_path, " +
				" b.process_status as data_status," +
				" c.username" +
				" FROM base_report_export_log a" +
				" left join flow_busi_info b on a.process_instance_id = b.process_instance_id" +
				" left join sys_user c on a.userid = c.userid" +
				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getId())) {
			sql += " and a.id = '" + params.getModel().getId() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getDataStatus())) {
			sql += " and b.process_status = '" + params.getModel().getDataStatus() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getUserid())) {
			sql += " and a.userid = '" + params.getModel().getUserid() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getReportName())) {
			sql += " and a.report_name = '" + params.getModel().getReportName() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getFileStatus())) {
			sql += " and a.file_status = '" + params.getModel().getFileStatus() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getApplyTime())) {
			sql += " and a.apply_time between date_sub(now(), interval 30 minute) and date_sub(now(), interval 0 minute) ";
		}
		sql += " order by a.apply_time desc, a.data_time desc";
		return super.findRows(sql, params);
	}

	public SqlResult<BaseReportExportLog> findBaseReportExportLogsLc(SqlParam<BaseReportExportLog> params) throws Exception {
		String sql = "SELECT a.id,a.userid,a.report_id,a.apply_time,a.data_time,a.process_instance_id,a.create_by,a.create_time,a.update_by,a.update_time,a.report_name,a.file_path,a.file_status,a.remote_path, " +
				" b.process_status as data_status," +
				" c.username" +
				" FROM base_report_export_log a" +
				" left join flow_busi_info b on a.process_instance_id = b.process_instance_id" +
				" left join sys_user c on a.userid = c.userid" +
				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getDataStatus())) {
			sql += " and b.process_status <> '" + params.getModel().getDataStatus() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getReportName())) {
			sql += " and a.report_name = '" + params.getModel().getReportName() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getFileStatus())) {
			sql += " and a.file_status = '" + params.getModel().getFileStatus() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getApplyTime())) {
			sql += " and a.apply_time between date_sub(now(), interval 30 minute) and date_sub(now(), interval 0 minute) ";
		}
		sql += " order by a.update_time desc, a.create_time desc";
		return super.findRows(sql, params);
	}

	public UpdateResult addBaseReportExportLog(SqlParam<BaseReportExportLog> params) throws Exception {
		return super.update("INSERT INTO base_report_export_log(id,userid,report_id,apply_time,data_time,process_instance_id,create_by,create_time,update_by,update_time,report_name,file_path,file_status,remote_path) VALUES($AUTOIDI{id},$S{userid},$S{reportId},$S{applyTime},$S{dataTime},$S{processInstanceId},$S{createBy},$S{createTime},$S{updateBy},$S{updateTime},$S{reportName},$S{filePath},$S{fileStatus},$S{,remotePath})",
				params.getModel());
	}
	
	public UpdateResult updateBaseReportExportLog(SqlParam<BaseReportExportLog> params) throws Exception {
		return super.update("UPDATE base_report_export_log SET remote_path=$S{remotePath} ,userid=$S{userid} ,report_id=$S{reportId} ,apply_time=$S{applyTime} ,data_time=$S{dataTime} ,process_instance_id=$S{processInstanceId} ,create_by=$S{createBy} ,create_time=$S{createTime} ,update_by=$S{updateBy} ,update_time=$S{updateTime} ,report_name=$S{reportName} ,file_path=$S{filePath} ,file_status=$S{fileStatus}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteBaseReportExportLog(SqlParam<BaseReportExportLog> params) throws Exception {
		return super.update("DELETE FROM base_report_export_log WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult updateFilePath(BaseReportExportLog params) throws Exception {
		return super.update("UPDATE base_report_export_log SET remote_path=$S{remotePath} ,file_path=$S{filePath} ,file_status=$S{fileStatus} WHERE process_instance_id=$S{processInstanceId}",
				params);
	}

	public UpdateResult updateFileNamePath(BaseReportExportLog params) throws Exception {
		return super.update("UPDATE base_report_export_log SET remote_path=$S{remotePath} ,file_path=$S{filePath} ,file_status=$S{fileStatus} ,report_name=$S{reportName} ,data_time=$S{dataTime}  WHERE process_instance_id=$S{processInstanceId}",
				params);
	}

	public UpdateResult updateDataTimePath(BaseReportExportLog params) throws Exception {
		return super.update("UPDATE base_report_export_log SET remote_path=$S{remotePath} ,file_path=$S{filePath} ,file_status=$S{fileStatus} ,update_time=now()  WHERE process_instance_id=$S{processInstanceId} AND data_time=$S{dataTime}",
				params);
	}

}
