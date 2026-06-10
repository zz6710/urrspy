package com.kayak.dps.sqlflow.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.config.model.KbatchTaskInfo;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.dps.sqlflow.model.RmsTableInfo;
import com.kayak.dps.sqlflow.model.TableLineage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RmsTableInfoDao extends ComnDao {

	public List<RmsTableInfo> findAllRmsTableInfos() throws Exception {
		Map<String ,String> params = new HashMap<String, String>();
		String sql = "SELECT id,table_info_id,database_name,table_name,comment,owner FROM rms_table_info where 1=1";
		return super.findRows(RmsTableInfo.class, sql, DataSourceProperty.PUB, params);
	}

	public SqlResult<RmsTableInfo> findRmsTableInfos(SqlParam<RmsTableInfo> params) throws Exception {
		String sql = "SELECT id,table_info_id,database_name,table_name,comment,owner FROM rms_table_info where 1=1";
		if (Tools.isNotEmpty(params.getModel().getTableName())) {
			sql += " and table_name like concat('%', $S{tableName}, '%')";
		}
		if (Tools.isNotEmpty(params.getModel().getOwner())) {
			sql += " and owner = $S{owner}";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addRmsTableInfo(SqlParam<RmsTableInfo> params) throws Exception {
		return super.update("INSERT INTO rms_table_info(id,table_info_id,database_name,table_name,comment,owner) VALUES($AUTOIDI{id},$S{tableInfoId},$S{databaseName},$S{tableName},$S{comment},$S{owner})",
				params.getModel());
	}
	
	public UpdateResult updateRmsTableInfo(SqlParam<RmsTableInfo> params) throws Exception {
		return super.update("UPDATE rms_table_info SET table_info_id=$S{tableInfoId} ,database_name=$S{databaseName} ,table_name=$S{tableName} ,comment=$S{comment} ,owner=$S{owner}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteRmsTableInfo(SqlParam<RmsTableInfo> params) throws Exception {
		return super.update("DELETE FROM rms_table_info WHERE  id=$I{id} ",
				params.getModel());
	}

	public SqlResult<RmsTableInfo> findRmsTableInfoDict(SqlParam<RmsTableInfo> params) throws Exception {
		return super.findRows("SELECT table_name,comment FROM rms_table_info", params);
	}

	public List<SqlRow> getDatabaseName(String database) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("database", database);
		return super.findRows("select TABLE_SCHEMA from information_schema.TABLES where TABLE_SCHEMA = $S{database}", params);
	}

	/**
	 * 清空表
	 * @throws Exception
	 */
	public void truncateRmsTableInfo() throws Exception {
		super.update("TRUNCATE TABLE rms_table_info");
	}

	/**
	 * 插入全量数据
	 * @param database
	 * @return
	 * @throws Exception
	 */
	public UpdateResult insertRmsTableInfoFromSchema(String database) throws Exception {
		if (Tools.isEmpty(database)) {
			return new UpdateResult();
		}
		database = database.replace(",", "','");
		return super.update("insert into rms_table_info(table_info_id, database_name, table_name, comment, owner)\n" +
			"select lower(CONCAT(table_schema, '.', TABLE_NAME)), LOWER(table_schema), LOWER(TABLE_NAME), TABLE_COMMENT,\n" +
			"case lower(SUBSTR(TABLE_NAME, 1, 3))\n" +
			"when 'stg' then '1'\n" +
			"when 'ods' then '2'\n" +
			"when 'dwd' then '3'\n" +
			"when 'dws' then '4'\n" +
			"when 'app' then '5'\n" +
			"else '0'\n" +
			"end as owner\n" +
			"from information_schema.TABLES\n" +
			"where TABLE_TYPE =1 and table_schema in ('" + database + "')");
	}

	/**
	 * 插入增量数据
	 * @param database
	 * @return
	 * @throws Exception
	 */
	public UpdateResult insertRmsTableInfoFromSchemaInc(String database) throws Exception {
		if (Tools.isEmpty(database)) {
			return new UpdateResult();
		}
		database = database.replace(",", "','");
		return super.update("insert into rms_table_info(table_info_id, database_name, table_name, comment, owner)\n" +
				"select lower(CONCAT(table_schema, '.', TABLE_NAME)), LOWER(table_schema), LOWER(TABLE_NAME), TABLE_COMMENT,\n" +
				"case lower(SUBSTR(TABLE_NAME, 1, 3))\n" +
				"when 'stg' then '1'\n" +
				"when 'ods' then '2'\n" +
				"when 'dwd' then '3'\n" +
				"when 'dws' then '4'\n" +
				"when 'app' then '5'\n" +
				"else '0'\n" +
				"end as owner\n" +
				"from information_schema.TABLES\n" +
				"where TABLE_TYPE =1 and table_schema in ('" + database + "') and instr(\n" +
				"(select group_concat(concat(rtl.to_table_info_id,',',rtl.from_table_info_id))  from rms_table_lineage rtl \n" +
				"where not exists(select 1 from rms_table_info rti where rti.table_info_id = rtl.to_table_info_id or rti.table_info_id = rtl.from_table_info_id)), concat(TABLE_SCHEMA,'.',TABLE_NAME)\n" +
				")");
	}

	/**
	 * 查询清算血缘关系列表
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<RmsTableInfo> findRelationshipTask(String database ,SqlParam<RmsTableInfo> params) throws Exception {
		String sql = "SELECT a.id,a.table_info_id,a.database_name,a.table_name,a.comment,a.owner,\n" +
				" '' as exeid,b.task_id,d.task_model,d.TASK_NAME,a.up_tasks,a.down_tasks\n" +
				" FROM rms_table_info a\n" +
				" inner join (\n" +
				" select DISTINCT r.to_table_info_id,c.task_id from rms_table_lineage r join base_port_sql_info c on r.exeid=c.exeid\n" +
				" union all \n" +
				" select concat('" + database + "','.',lower(bpm.port_table)) as to_table_info_id, bpm.pid as task_id from base_port_manage bpm where bpm.pid is not null and bpm.pid<>''\n" +
				" ) b on a.table_info_id = b.to_table_info_id\n" +
				" inner join kbatch_task_info d on b.task_id = d.task_id\n" +
				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getTableName())) {
			sql += " and a.table_name like concat('%', $S{tableName}, '%')";
		}
		if (Tools.isNotEmpty(params.getModel().getTaskId())) {
			sql += " and b.task_id = $S{taskId}";
		}
		if (Tools.isNotEmpty(params.getModel().getOwner())) {
			sql += " and a.owner = $S{owner}";
		}
		sql += " order by cast(d.task_model as signed),d.TASK_NAME";
		return super.findRows(sql, params);
	}

	/**
	 * 查询上游任务列表
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<RmsTableInfo> findUpStreamTask(String database ,SqlParam<RmsTableInfo> params) throws Exception {
		String sql = "select c.TASK_ID, c.TASK_NAME, c.task_model from (" +
				"SELECT DISTINCT c.TASK_ID, c.TASK_NAME, c.task_model ,sdi.itemorder\n" +
				" from (\n" +
				" \tselect DISTINCT r.to_table_info_id,c.task_id from rms_table_lineage r join base_port_sql_info c on r.exeid=c.exeid\n" +
				" \tunion all \n" +
				" \tselect concat('" + database + "','.',lower(bpm.port_table)) as to_table_info_id, bpm.pid as task_id from base_port_manage bpm where bpm.pid is not null and bpm.pid<>''\n" +
				" ) b \n" +
				" inner join kbatch_task_info c on b.task_id = c.task_id\n" +
				" inner join sys_dict_item sdi on sdi.dict ='task_model' and sdi.itemkey = c.task_model" +
				" where instr(\n" +
				" concat((\n" +
				" SELECT up_tasks\n" +
				" from rms_table_info\n" +
				" where table_info_id = $S{tableInfoId}\n" +
				" ),','),concat(c.TASK_ID,',')) > 0) c order by c.itemorder,c.TASK_ID,c.TASK_NAME";
		return super.findRows(sql, params);
	}

	/**
	 * 查询下游任务列表
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<RmsTableInfo> findDownStreamTask(String database ,SqlParam<RmsTableInfo> params) throws Exception {
		String sql = "select c.TASK_ID, c.TASK_NAME, c.task_model from (" +
				"SELECT DISTINCT c.TASK_ID, c.TASK_NAME, c.task_model ,sdi.itemorder\n" +
				" from (\n" +
				" \tselect DISTINCT r.from_table_info_id,c.task_id from rms_table_lineage r join base_port_sql_info c on r.exeid=c.exeid\n" +
				" \tunion all \n" +
				" \tselect concat('" + database + "','.',lower(bpm.port_table)) as from_table_info_id, bpm.pid as task_id from base_port_manage bpm where bpm.pid is not null and bpm.pid<>''\n" +
				" ) b \n" +
				" inner join kbatch_task_info c on b.task_id = c.task_id\n" +
				" inner join sys_dict_item sdi on sdi.dict ='task_model' and sdi.itemkey = c.task_model" +
				" where instr(\n" +
				" concat((\n" +
				" SELECT down_tasks\n" +
				" from rms_table_info\n" +
				" where table_info_id = $S{tableInfoId}\n" +
				" ),','),concat(c.TASK_ID,',')) > 0) c order by c.itemorder,c.TASK_ID,c.TASK_NAME";
		return super.findRows(sql, params);
	}

	/**
	 * 查询上游任务集
	 * @return
	 * @throws Exception
	 */
	public List<RmsTableInfo> findUpStreamTasks(String database ,String tableInfoId) throws Exception {
		Map<String ,String> params = new HashMap<String, String>();
		params.put("database", database);
		params.put("tableInfoId", tableInfoId);
		String sql = "SELECT ifnull(group_concat(distinct c.TASK_ID),'') as up_tasks\n" +
				" from (\n" +
				" \tselect DISTINCT r.to_table_info_id,c.task_id from rms_table_lineage r join base_port_sql_info c on r.exeid=c.exeid\n" +
				" \tunion all \n" +
				" \tselect concat('" + database + "','.',lower(bpm.port_table)) as to_table_info_id, bpm.pid as task_id from base_port_manage bpm where bpm.pid is not null and bpm.pid<>''\n" +
				" ) b \n" +
				" inner join kbatch_task_info c on b.task_id = c.task_id\n" +
				" inner join sys_dict_item sdi on sdi.dict ='task_model' and sdi.itemkey = c.task_model" +
				" where b.to_table_info_id = $S{tableInfoId} or b.to_table_info_id in\n" +
				" (\n" +
				" SELECT from_table_info_id\n" +
				" from rms_table_lineage_dependency\n" +
				" where INSTR(all_superior, concat('|', $S{tableInfoId}, '.')) > 0\n" +
				" ) order by sdi.itemorder,c.TASK_ID,c.TASK_NAME";
		return super.findRows(RmsTableInfo.class, sql, DataSourceProperty.PUB, params);
	}

	/**
	 * 查询下游任务集
	 * @return
	 * @throws Exception
	 */
	public List<RmsTableInfo> findDownStreamTasks(String database ,String tableInfoId) throws Exception {
		Map<String ,String> params = new HashMap<String, String>();
		params.put("database", database);
		params.put("tableInfoId", tableInfoId);
		String sql = "SELECT ifnull(group_concat(distinct c.TASK_ID),'') as down_tasks\n" +
				" from (\n" +
				" \tselect DISTINCT r.from_table_info_id,c.task_id from rms_table_lineage r join base_port_sql_info c on r.exeid=c.exeid\n" +
				" \tunion all \n" +
				" \tselect concat('" + database + "','.',lower(bpm.port_table)) as from_table_info_id, bpm.pid as task_id from base_port_manage bpm where bpm.pid is not null and bpm.pid<>''\n" +
				" ) b \n" +
				" inner join kbatch_task_info c on b.task_id = c.task_id\n" +
				" inner join sys_dict_item sdi on sdi.dict ='task_model' and sdi.itemkey = c.task_model" +
				" where b.from_table_info_id in (\n" +
				" SELECT DISTINCT to_table_info_id\n" +
				" from rms_table_lineage_dependency\n" +
				" where INSTR(all_dependency, concat('|', $S{tableInfoId}, '.')) > 0\n" +
				") order by sdi.itemorder,c.TASK_ID,c.TASK_NAME";
		return super.findRows(RmsTableInfo.class, sql, DataSourceProperty.PUB, params);
	}

	public void updateTask(List<String> taskIdList, String beginDate, String endDate) throws Exception {
		if (taskIdList == null || taskIdList.isEmpty()) {
			return;
		}
		String taskIds = String.join("','", taskIdList);
		Map<String, String> params = new HashMap<>(4);
		params.put("taskIds", taskIds);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		super.doTrans(() -> {
			String sql = "update kbatch_task_exec set EXEC_STATUS = '0' where TASK_DATE between $S{beginDate} and $S{endDate} and TASK_ID in ('" + taskIds + "')";
			String stepSql = "update kbatch_task_step_exec set EXEC_STATUS = '0' where TASK_DATE between $S{beginDate} and $S{endDate} and TASK_ID in ('" + taskIds + "')";
			super.update(sql, params);
			super.update(stepSql, params);
		});

	}

	/**
	 * 查询清算任务信息字典
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<KbatchTaskInfo> findTaskInfoDict(SqlParam<KbatchTaskInfo> params) throws Exception {
		String sql = "SELECT task_id,task_name FROM kbatch_task_info ORDER BY task_id";
		return super.findRows(sql, params);
	}

	public List<RmsTableInfo> findRmsTableInfoByTableName(String tablesString) throws Exception {
		List<RmsTableInfo> list = new ArrayList<>();
		if (Tools.isNotEmpty(tablesString)) {
			String string = tablesString.replace(",", "','");
			String sql = "SELECT table_name,comment FROM rms_table_info where table_name in('" + string + "')";
			list = super.findRows(RmsTableInfo.class, sql, DataSourceProperty.PUB, new HashMap<>(0));
		}
		return list;
	}

	public List<RmsTableInfo> findRmsTableInfoIdByExeSql(String database, String exeSql) throws Exception {
		Map<String ,String> params = new HashMap<String, String>();
		params.put("database", database);
		params.put("exeSql", exeSql);
		String sql = "select rti.table_info_id from rms_table_info rti where " +
				"(instr($S{exeSql},concat(' ',rti.table_name,' '))>0 and rti.database_name=$S{database}) " +
				"or instr($S{exeSql},concat(' ',rti.table_info_id,' '))>0 ";
		return super.findRows(RmsTableInfo.class, sql, DataSourceProperty.PUB, params);
	}

	public void updateTableInfoTasks(String tableInfoId, String upTasks, String downTasks) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("tableInfoId", tableInfoId);
		params.put("upTasks", upTasks);
		params.put("downTasks", downTasks);
		super.doTrans(() -> {
			String sql = "update rms_table_info set up_tasks = $S{upTasks},down_tasks = $S{downTasks} where table_info_id = $S{tableInfoId}";
			super.update(sql, params);
		});

	}

}
