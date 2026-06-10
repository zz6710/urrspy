package com.kayak.dps.sqlflow.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.clear.utils.Tools;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.sqlflow.dao.FieldLineageDao;
import com.kayak.dps.sqlflow.dao.RmsFieldInfoDao;
import com.kayak.dps.sqlflow.dao.TableLineageDao;
import com.kayak.dps.sqlflow.dao.RmsTableInfoDao;
import com.kayak.dps.sqlflow.model.FieldLineage;
import com.kayak.dps.sqlflow.model.RmsFieldInfo;
import com.kayak.dps.sqlflow.model.TableLineage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@APIDefine(desc = "表血缘关系表服务", model = TableLineage.class)
public class TableLineageService {

	@Autowired
	private TableLineageDao tableLineageDao;
	@Autowired
	private FieldLineageDao fieldLineageDao;
	@Autowired
	private RmsFieldInfoDao rmsFieldInfoDao;
	@Autowired
	private RmsTableInfoDao rmsTableInfoDao;
	@Autowired
	RmsFieldInfoService rmsFieldInfoService;
	@Autowired
	FieldLineageService fieldLineageService;

	@Value("${database.schemas}")
	private String databases;

	@API(desc = "查询表血缘关系表信息", auth = APIAuth.YES)
	public SqlResult<TableLineage> findTableLineages(SqlParam<TableLineage> params) throws Exception {
		return tableLineageDao.findTableLineages(params);
	}

	@API(desc = "添加表血缘关系表", params = "id,exeid,to_table_info_id,from_table_info_id", auth = APIAuth.YES)
	public int addTableLineage(SqlParam<TableLineage> params) throws Exception {
		tableLineageDao.addTableLineage(params).getEffect();
		return rmsTableInfoDao.insertRmsTableInfoFromSchemaInc(databases).getEffect();
	}
	
	@API(desc = "修改表血缘关系表", params = "id,exeid,to_table_info_id,from_table_info_id", auth = APIAuth.YES)
	public int updateTableLineage(SqlParam<TableLineage> params) throws Exception {
		tableLineageDao.updateTableLineage(params).getEffect();
		return rmsTableInfoDao.insertRmsTableInfoFromSchemaInc(databases).getEffect();
	}
	
	@API(desc = "删除表血缘关系表", params = "id,exeid,to_table_info_id,from_table_info_id", auth = APIAuth.YES)
	public int deleteTableLineage(SqlParam<TableLineage> params) throws Exception {
		tableLineageDao.deleteTableLineage(params).getEffect();
		return rmsTableInfoDao.insertRmsTableInfoFromSchemaInc(databases).getEffect();
	}

	@API(desc = "表上游血缘关系", auth = APIAuth.NO)
	public String findUpStreamTableLineage(SqlParam<TableLineage> params) throws Exception {
		String tableInfoId = (String) params.getParamsDirect().get("tableInfoId");
		if (Tools.strIsEmpty(tableInfoId)) {
			throw new PromptException("表名不能为空");
		}
		// 查询该表的字段
		List<RmsFieldInfo> fieldList = rmsFieldInfoService.findFieldNameByTableInfoId(tableInfoId);
		// 查询表上游血缘关系
		List<FieldLineage> fieldLineageList = new ArrayList<>();
		for (RmsFieldInfo rmsFieldInfo : fieldList) {
			fieldLineageList.addAll(fieldLineageDao.findUpStreamFieldLineage(rmsFieldInfo.getTableFieldId()));
		}
		String highLightTableName = tableInfoId.split("\\.")[1];
		Map<String, Object> data = fieldLineageService.getEdgesAndNodes(fieldLineageList, highLightTableName, fieldList);
		return RequestSupport.updateReturnJson(true, "操作成功", data).toString();
	}

	@API(desc = "表下游血缘关系", auth = APIAuth.NO)
	public String findDownStreamTableLineage(SqlParam<TableLineage> params) throws Exception {
		String tableInfoId = (String) params.getParamsDirect().get("tableInfoId");
		if (Tools.strIsEmpty(tableInfoId)) {
			throw new PromptException("表名不能为空");
		}
		// 查询该表的所有字段
		List<RmsFieldInfo> fieldList = rmsFieldInfoService.findFieldNameByTableInfoId(tableInfoId);
		// 查询表下游血缘关系
		List<FieldLineage> fieldLineageList = new ArrayList<>();
		for (RmsFieldInfo rmsFieldInfo : fieldList) {
			fieldLineageList.addAll(fieldLineageDao.findDownStreamFieldLineage(rmsFieldInfo.getTableFieldId()));
		}
		String highLightTableName = tableInfoId.split("\\.")[1];
		Map<String, Object> data = fieldLineageService.getEdgesAndNodes(fieldLineageList, highLightTableName, fieldList);
		return RequestSupport.updateReturnJson(true, "操作成功", data).toString();
	}

	@API(desc = "表全血缘关系", auth = APIAuth.NO)
	public String findTableLineage(SqlParam<TableLineage> params) throws Exception {
		String tableInfoId = (String) params.getParamsDirect().get("tableInfoId");
		if (Tools.strIsEmpty(tableInfoId)) {
			throw new PromptException("表名不能为空");
		}
		// 查询该表的所有字段
		List<RmsFieldInfo> fieldList = rmsFieldInfoService.findFieldNameByTableInfoId(tableInfoId);
		// 查询表全血缘关系
		Set<FieldLineage> fieldLineageSet = new HashSet<>();
		for (RmsFieldInfo rmsFieldInfo : fieldList) {
			fieldLineageSet.addAll(fieldLineageDao.findUpStreamFieldLineage(rmsFieldInfo.getTableFieldId()));
			fieldLineageSet.addAll(fieldLineageDao.findDownStreamFieldLineage(rmsFieldInfo.getTableFieldId()));
		}
		List<FieldLineage> fieldLineageList = new ArrayList<>(fieldLineageSet);
		String highLightTableName = tableInfoId.split("\\.")[1];
		Map<String, Object> data = fieldLineageService.getEdgesAndNodes(fieldLineageList, highLightTableName, fieldList);
		return RequestSupport.updateReturnJson(true, "操作成功", data).toString();
	}

	@API(desc = "解析sql血缘关系", auth = APIAuth.NO)
	public synchronized String getSqlLineage(SqlParam<FieldLineage> params) throws Exception {
		String runStatus = SysUtil.getSystemParamsByParaid("90000051111"); //执行状态
		if ("1".equals(runStatus)) {
			return RequestSupport.updateReturnJson(false, "正在执行血缘解析，稍后重试！", null).toString();
		}
		fieldLineageService.parseSql(params);
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	@API(desc = "查询血缘解析执行状态", auth = APIAuth.NO)
	public synchronized String getRunStatus(SqlParam<FieldLineage> params) throws Exception {
		String runStatus = SysUtil.getSystemParamsByParaid("90000051111"); //执行状态
		if ("1".equals(runStatus)) {
			return RequestSupport.updateReturnJson(true, "1", null).toString();
		}
		return RequestSupport.updateReturnJson(true, "0", null).toString();
	}

}
