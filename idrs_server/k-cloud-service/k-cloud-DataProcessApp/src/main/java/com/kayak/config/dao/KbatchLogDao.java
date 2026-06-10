package com.kayak.config.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.config.constants.GlobalConstants;
import com.kayak.config.model.KbatchLog;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.app.model.FundInfoModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

/**
 * 文件名: Ta5004Dao.java
 * 描述:  清算日志查询
 * 创建人: zengzt
 * 创建时间:2020年4月27日上午10:20:47
 */
@Repository
public class KbatchLogDao extends ComnDao{

	public SqlResult<KbatchLog> queryBatchLog(SqlParam<KbatchLog> params) throws Exception {
		String sql = "select  t.log_serno,t.task_execid,t.task_group,t.task_id,t.step_no,t.task_date,t.moduleid,t.exec_status,t.exec_date,t.should_exec_date,t.prod_code,t.target_code,t.start_time,t.end_time,t.thread_uuid,t.server_name,t.server_ip,t.rtn_code,t.rtn_desc,t.upd_date,t.upd_time,t2.task_model,t2.task_name " +
				"  from kbatch_log t left join kbatch_task_info t2  \n" +
				"  on t.TASK_ID = t2.TASK_ID 		"
					+"WHERE t.moduleid = '"+ GlobalConstants.MODULEID+"'";
		if (StringUtils.isNotEmpty(params.getModel().getTaskDateEnd())) {
			sql += " and TASK_DATE <= $S{taskDateEnd} ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getLogSerno())) {
			sql += " and LOG_SERNO like '%$U{logSerno}%'";
		}
		if (StringUtils.isNotEmpty(params.getModel().getTaskExecid())) {
			sql += " and TASK_EXECID like '%$U{taskExecid}%'";
		}
		if (StringUtils.isNotEmpty(params.getModel().getTaskGroup())) {
			sql += " and TASK_GROUP like '%$U{taskGroup}%'";
		}
		if (StringUtils.isNotEmpty(params.getModel().getTaskId())) {
			sql += " and t.TASK_ID like '%$U{taskId}%'";
		}
		if (StringUtils.isNotEmpty(params.getModel().getExecStatus())) {
			sql += " and EXEC_STATUS = $S{execStatus}";
		}
		if (StringUtils.isNotEmpty(params.getModel().getProdCode())) {
			sql += " and PROD_CODE = $S{prodCode}";
		}
		if (StringUtils.isNotEmpty(params.getModel().getProdCode())) {
			sql += " and PROD_CODE = $S{prodCode}";
		}
		if (StringUtils.isNotEmpty(params.getModel().getTaskModel())) {
			sql += " and task_model = $S{taskModel} ";
		}
		if (StringUtils.equals(params.getModel().getTaskDate(),"sysDate")) {
			sql += " and task_Date = (select paravalue from sys_param sp where sp.paraid = '10004' limit 1) ";
		}else if (StringUtils.isNotBlank(params.getModel().getTaskDate()) && !StringUtils.equals(params.getModel().getTaskDate(),"sysDate")) {
			sql += " and DATE(task_Date) = DATE($S{taskDate}) ";
		}
		sql +="ORDER BY t.upd_date desc, t.log_serno ";
		return super.findRows(sql, params);
	}

	
}
