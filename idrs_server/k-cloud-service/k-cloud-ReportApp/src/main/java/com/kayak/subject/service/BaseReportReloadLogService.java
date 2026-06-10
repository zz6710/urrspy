package com.kayak.subject.service;

import com.kayak.subject.dao.BaseReportReloadLogDao;
import com.kayak.subject.model.BaseReportReloadLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Service
@APIDefine(desc = "报表重新生成结果服务", model = BaseReportReloadLog.class)
public class BaseReportReloadLogService {

	@Autowired
	private BaseReportReloadLogDao baseReportReloadLogDao;

	@API(desc = "查询报表重新生成结果信息", auth = APIAuth.NO)
	public SqlResult<BaseReportReloadLog> findBaseReportReloadLogs(SqlParam<BaseReportReloadLog> params) throws Exception {
		return baseReportReloadLogDao.findBaseReportReloadLogs(params);
	}

	@API(desc = "添加报表重新生成结果", params = "id,menu_id,report_date,start_date,start_time,end_date,end_time,result_status,result_info,user_name", auth = APIAuth.NO)
	public int addBaseReportReloadLog(SqlParam<BaseReportReloadLog> params) throws Exception {
		return baseReportReloadLogDao.addBaseReportReloadLog(params).getEffect();
	}

	@API(desc = "修改报表重新生成结果", params = "id,menu_id,report_date,start_date,start_time,end_date,end_time,result_status,result_info,user_name", auth = APIAuth.NO)
	public int updateBaseReportReloadLog(SqlParam<BaseReportReloadLog> params) throws Exception {
		return baseReportReloadLogDao.updateBaseReportReloadLog(params).getEffect();
	}

	@API(desc = "删除报表重新生成结果", params = "id,menu_id,report_date,start_date,start_time,end_date,end_time,result_status,result_info,user_name", auth = APIAuth.NO)
	public int deleteBaseReportReloadLog(SqlParam<BaseReportReloadLog> params) throws Exception {
		return baseReportReloadLogDao.deleteBaseReportReloadLog(params).getEffect();
	}

}
