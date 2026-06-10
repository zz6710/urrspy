package com.kayak.subject.dao;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.subject.model.BaseReportReloadLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;

@Repository
public class BaseReportReloadLogDao extends ComnDao {

	public SqlResult<BaseReportReloadLog> findBaseReportReloadLogs(SqlParam<BaseReportReloadLog> params) throws Exception {
		return super.findRows("SELECT id,menu_id,report_date,date_format(concat(start_date, start_time), '%Y/%m/%d %H:%i:%s') start_time,date_format(concat(end_date, end_time), '%Y/%m/%d %H:%i:%s') end_time,result_status,result_info,user_name FROM base_report_reload_log where menu_id='"+params.getModel().getMenuId()+"' order by id desc limit 1", params);
	}

	public List<SqlRow> findIdBaseReportReloadLogs(BaseReportReloadLog params) throws Exception {
		return super.findRows("SELECT max(id) id FROM base_report_reload_log where menu_id='"+params.getMenuId()+"'");
	}

	public UpdateResult addBaseReportReloadLog(SqlParam<BaseReportReloadLog> params) throws Exception {
		return super.update("INSERT INTO base_report_reload_log(id,menu_id,report_date,start_date,start_time,end_date,end_time,result_status,result_info,user_name) VALUES($AUTOIDI{id},$S{menuId},$S{reportDate},$S{startDate},$S{startTime},$S{endDate},$S{endTime},$S{resultStatus},$S{resultInfo},$S{userName})",
				params.getModel());
	}

	public UpdateResult addBaseReportReloadLog(BaseReportReloadLog params) throws Exception {
		return super.update("INSERT INTO base_report_reload_log(id,menu_id,report_date,start_date,start_time,end_date,end_time,result_status,result_info,user_name) VALUES($AUTOIDI{id},$S{menuId},$S{reportDate},$S{startDate},$S{startTime},$S{endDate},$S{endTime},$S{resultStatus},$S{resultInfo},$S{userName})",
				params);
	}

	public UpdateResult updateBaseReportReloadLog(SqlParam<BaseReportReloadLog> params) throws Exception {
		return super.update("UPDATE base_report_reload_log SET menu_id=$S{menuId} ,report_date=$S{reportDate} ,start_date=$S{startDate} ,start_time=$S{startTime} ,end_date=$S{endDate} ,end_time=$S{endTime} ,result_status=$S{resultStatus} ,result_info=$S{resultInfo} ,user_name=$S{userName}  WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult updateBaseReportReloadLog(BaseReportReloadLog params) throws Exception {
		return super.update("UPDATE base_report_reload_log SET menu_id=$S{menuId} ,report_date=$S{reportDate} ,start_date=$S{startDate} ,start_time=$S{startTime} ,end_date=$S{endDate} ,end_time=$S{endTime} ,result_status=$S{resultStatus} ,result_info=$S{resultInfo} ,user_name=$S{userName}  WHERE  id=$I{id} ",
				params);
	}

	public UpdateResult deleteBaseReportReloadLog(SqlParam<BaseReportReloadLog> params) throws Exception {
		return super.update("DELETE FROM base_report_reload_log WHERE  id=$I{id} ",
				params.getModel());
	}

}
