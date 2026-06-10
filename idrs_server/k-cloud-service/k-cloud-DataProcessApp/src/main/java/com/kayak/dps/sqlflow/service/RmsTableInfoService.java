package com.kayak.dps.sqlflow.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.clear.utils.Tools;
import com.kayak.config.model.KbatchTaskInfo;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.sqlflow.dao.RmsTableInfoDao;
import com.kayak.dps.sqlflow.model.RmsTableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@APIDefine(desc = "表基础信息服务", model = RmsTableInfo.class)
public class RmsTableInfoService {

	@Autowired
	private RmsTableInfoDao rmsTableInfoDao;

	@Value("${database.schemas}")
	private String databases;

	@API(desc = "查询表基础信息信息", auth = APIAuth.YES)
	public SqlResult<RmsTableInfo> findRmsTableInfos(SqlParam<RmsTableInfo> params) throws Exception {
		return rmsTableInfoDao.findRmsTableInfos(params);
	}

	@API(desc = "添加表基础信息", params = "id,table_info_id,database_name,table_name,comment,owner", auth = APIAuth.NO)
	public int addRmsTableInfo(SqlParam<RmsTableInfo> params) throws Exception {
		return rmsTableInfoDao.addRmsTableInfo(params).getEffect();
	}
	
	@API(desc = "修改表基础信息", params = "id,table_info_id,database_name,table_name,comment,owner", auth = APIAuth.NO)
	public int updateRmsTableInfo(SqlParam<RmsTableInfo> params) throws Exception {
		return rmsTableInfoDao.updateRmsTableInfo(params).getEffect();
	}

	@API(desc = "查询表字典", auth = APIAuth.YES)
	public SqlResult<RmsTableInfo> findRmsTableInfoDict(SqlParam<RmsTableInfo> params) throws Exception {
		return rmsTableInfoDao.findRmsTableInfoDict(params);
	}

	/**
	 * 更新表信息
	 * @param database 数据库名 eg: t1,t2
	 * @throws Exception
	 */
	public void updateRmsTableInfo(String database) throws Exception {
		rmsTableInfoDao.truncateRmsTableInfo();
		rmsTableInfoDao.insertRmsTableInfoFromSchema(database);
	}

	@API(desc = "查询清算血缘关系", auth = APIAuth.YES)
	public SqlResult<RmsTableInfo> findRelationshipTask(SqlParam<RmsTableInfo> params) throws Exception {
		//默认第一个数据库为主数据库
		String database = databases.split(",")[0];
		return rmsTableInfoDao.findRelationshipTask(database, params);
	}

	@API(desc = "查询上游任务列表", auth = APIAuth.YES)
	public SqlResult<RmsTableInfo> findUpStreamTask(SqlParam<RmsTableInfo> params) throws Exception {
		//默认第一个数据库为主数据库
		String database = databases.split(",")[0];
		return rmsTableInfoDao.findUpStreamTask(database, params);
	}

	@API(desc = "查询下游任务列表", auth = APIAuth.YES)
	public SqlResult<RmsTableInfo> findDownStreamTask(SqlParam<RmsTableInfo> params) throws Exception {
		//默认第一个数据库为主数据库
		String database = databases.split(",")[0];
		return rmsTableInfoDao.findDownStreamTask(database, params);
	}

	@API(desc = "重跑任务", auth = APIAuth.YES)
	public String updateTask(SqlParam<RmsTableInfo> params) throws Exception {
		// 重跑任务：将任务的状态改为0，定时任务自动重跑
		if (Tools.strIsEmpty(params.getModel().getDate())) {
			return RequestSupport.updateReturnJson(false, "请选择重跑日期", null).toString();
		}
		//默认第一个数据库为主数据库
		String database = databases.split(",")[0];
		String taskType = params.getModel().getTaskType();
		List<String> taskIdList = new ArrayList<>();
		String date = params.getModel().getDate();
		String tableInfoId = params.getModel().getTableInfoId();
		String taskId = params.getModel().getTaskId();
		if ("upAll".equals(taskType) && Tools.strIsNotEmpty(tableInfoId)) {
			// 上游任务
			SqlResult<RmsTableInfo> sqlResult = rmsTableInfoDao.findUpStreamTask(database, params);
			taskIdList = sqlResult.getRows().stream().map(RmsTableInfo::getTaskId).collect(Collectors.toList());
		} else if ("downAll".equals(taskType) && Tools.strIsNotEmpty(tableInfoId)) {
			// 下游任务
			SqlResult<RmsTableInfo> sqlResult = rmsTableInfoDao.findDownStreamTask(database, params);
			taskIdList = sqlResult.getRows().stream().map(RmsTableInfo::getTaskId).collect(Collectors.toList());
		} else if (("up".equals(taskType) || "down".equals(taskType)) && Tools.strIsNotEmpty(taskId)) {
			// 单个任务
			taskIdList.add(taskId);
		}
		rmsTableInfoDao.updateTask(taskIdList, date, date);
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	@API(desc = "查询清算任务信息字典", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<KbatchTaskInfo> findTaskInfoDict(SqlParam<KbatchTaskInfo> params) throws Exception {
		return rmsTableInfoDao.findTaskInfoDict(params);
	}

}
