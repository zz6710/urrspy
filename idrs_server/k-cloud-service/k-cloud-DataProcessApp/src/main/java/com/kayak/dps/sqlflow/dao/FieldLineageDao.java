package com.kayak.dps.sqlflow.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.dps.sqlflow.model.FieldLineage;
import org.springframework.stereotype.Repository;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FieldLineageDao extends ComnDao {

	public SqlResult<FieldLineage> findFieldLineages(SqlParam<FieldLineage> params) throws Exception {
		return super.findRows("SELECT to_table_info_id,to_table_field_id,from_table_info_id,from_table_field_id,all_dependency,all_superior,manual_flag,sequence FROM rms_table_lineage_dependency", params);
	}

	public UpdateResult addFieldLineage(SqlParam<FieldLineage> params) throws Exception {
		return super.update("INSERT INTO rms_table_lineage_dependency(to_table_info_id,to_table_field_id,from_table_info_id,from_table_field_id,all_dependency,all_superior,manual_flag,sequence) VALUES($S{toTableInfoId},$S{toTableFieldId},$S{fromTableInfoId},$S{fromTableFieldId},$S{allDependency},$S{allSuperior},$S{manualFlag},$I{sequence})",
				params.getModel());
	}

	public UpdateResult updateFieldLineage(SqlParam<FieldLineage> params) throws Exception {
		return super.update("UPDATE rms_table_lineage_dependency SET to_table_info_id=$S{toTableInfoId} ,from_table_info_id=$S{fromTableInfoId} ,all_dependency=$S{allDependency}, all_superior=$S{allSuperior} ,manual_flag=$S{manualFlag} ,sequence=$I{sequence}  WHERE  to_table_field_id=$S{toTableFieldId} AND from_table_field_id=$S{fromTableFieldId} ",
				params.getModel());
	}

	public UpdateResult deleteFieldLineage(SqlParam<FieldLineage> params) throws Exception {
		return super.update("DELETE FROM rms_table_lineage_dependency WHERE  to_table_field_id=$S{toTableFieldId} AND from_table_field_id=$S{fromTableFieldId} ",
				params.getModel());
	}

	public void addFieldLineage(FieldLineage params) throws Exception {
		super.update("INSERT INTO rms_table_lineage_dependency(to_table_info_id,to_table_field_id,from_table_info_id,from_table_field_id,all_dependency,all_superior,manual_flag,sequence) VALUES($S{toTableInfoId},$S{toTableFieldId},$S{fromTableInfoId},$S{fromTableFieldId},$S{allDependency},$S{allSuperior},$S{manualFlag},$S{sequence})",
				params);
	}

	// 更新all_dependency
	public void updateAllDependency(FieldLineage params) throws Exception {
		super.update("UPDATE rms_table_lineage_dependency" +
						" SET all_dependency=$S{allDependency} " +
						" WHERE to_table_field_id=$S{toTableFieldId} AND from_table_field_id=$S{fromTableFieldId}",
				params);
	}

	public void updateAllSuperior(FieldLineage params) throws Exception {
		super.update("UPDATE rms_table_lineage_dependency" +
						" SET all_superior=$S{allSuperior} " +
						" WHERE to_table_field_id=$S{toTableFieldId} AND from_table_field_id=$S{fromTableFieldId}",
				params);
	}

	// 查询需要解析的sql
	public List<SqlRow> findAllSql() throws Exception {
		/*String sql = "select b.exeid, b.sqlid, b.task_id, LOWER(b.sqlStr) as sqlstr, c.coordinate_type, c.report_table" +
				" from base_port_sql_info b" +
				" left join base_report_info c on b.task_id = c.task_id" +
				" where b.task_id is not null " +
				" and b.task_id<>'' " +
				" and upper(sqlstr) not like 'SELECT%' " +
				" and upper(sqlstr) not like '%DELETE%' " +
				" and upper(sqlstr) not like 'TRUNCATE%'"; */
		String sql = "select b.exeid, b.sqlid, b.task_id, LOWER(b.sqlStr) as sqlstr, c.coordinate_type, c.report_table" +
				" from base_port_sql_info b" +
				" left join base_report_info c on b.task_id = c.task_id" +
				" where b.task_id is not null " +
				" and b.task_id<>'' " +
				" and (upper(sqlstr) like '%INSERT%INTO%' " +
				" or upper(sqlstr) like '%REPLACE%INTO%' " +
				" or upper(sqlstr) like '%UPDATE%SET%'" +
				" or (upper(sqlstr) not like '%INSERT%INTO%' and upper(sqlstr) not like '%REPLACE%INTO%' and upper(sqlstr) not like '%UPDATE%SET%' and upper(sqlstr) like '%SELECT%FROM%' and c.coordinate_type = '2'))" +
				" and upper(sqlstr) not like '%DELETE%FROM%'"; //注意匹配mysql的truncate()函数
		return super.findRows(sql, 0, new HashMap<>(0));
	}

	// 删除非手工维护的血缘关系
	public UpdateResult deleteFieldLineage() throws Exception {
		return super.update("DELETE FROM rms_table_lineage_dependency where manual_flag = '0'");
	}

	/**
	 * 查询需要更新的字段血缘关系
	 * 查询不以|toTableFieldId|开头的数据，即包含||toTableFieldId|
	 * @param toTableFieldId
	 * @return
	 * @throws Exception
	 */
	public List<FieldLineage> findUpdateAllDependencyList(String toTableFieldId) throws Exception {
		if (Tools.isEmpty(toTableFieldId)) {
			return new ArrayList<>();
		}
		String sql = "select to_table_field_id, from_table_field_id, all_dependency" +
				" from rms_table_lineage_dependency" +
				" where instr(all_dependency, '||" + toTableFieldId + "|') > 0";
		return super.findRows(FieldLineage.class, sql, DataSourceProperty.PUB, new HashMap<>(0));
	}

	public List<FieldLineage> findUpdateAllSuperiorList(String fromTableFieldId) throws Exception {
		if (Tools.isEmpty(fromTableFieldId)) {
			return new ArrayList<>();
		}
		String sql = "select to_table_field_id, from_table_field_id, all_superior" +
				" from rms_table_lineage_dependency" +
				" where instr(all_superior, '||" + fromTableFieldId + "|') > 0";
		return super.findRows(FieldLineage.class, sql, DataSourceProperty.PUB, new HashMap<>(0));
	}

	/**
	 * 源字段对应的all_dependency
	 * @param fromTableFieldId
	 * @return
	 * @throws Exception
	 */
	public String findChildAllDependency(String fromTableFieldId) throws Exception {
		if (Tools.isEmpty(fromTableFieldId)) {
			return null;
		}
		String sql = "select all_dependency from rms_table_lineage_dependency" +
				" where manual_flag = '0'" +
				" and to_table_field_id = '" + fromTableFieldId + "'";
		List<FieldLineage> list = super.findRows(FieldLineage.class, sql, DataSourceProperty.PUB, fromTableFieldId);
		if (list.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (FieldLineage item : list) {
			sb.append(item.getAllDependency());
		}
		return sb.toString();
	}

	/**
	 * 查询子下游
	 * @param toTableFieldId
	 * @return
	 * @throws Exception
	 */
	public String findChildAllSuperior(String toTableFieldId) throws Exception {
		if (Tools.isEmpty(toTableFieldId)) {
			return null;
		}
		String sql = "select all_superior from rms_table_lineage_dependency" +
				" where manual_flag = '0'" +
				" and from_table_field_id = '" + toTableFieldId + "'";
		List<FieldLineage> list = super.findRows(FieldLineage.class, sql, DataSourceProperty.PUB, toTableFieldId);
		if (list.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (FieldLineage item : list) {
			sb.append(item.getAllSuperior());
		}
		return sb.toString();
	}

	/**
	 * 查询手工维护的字段血缘关系
	 * 自动0 手动1
	 * @return
	 * @throws Exception
	 */
	public List<String> findManualFieldLineages() throws Exception {
		String sql = "select concat(to_table_field_id, '-', from_table_field_id) as value" +
				" from rms_table_lineage_dependency" +
				" where manual_flag = '1'";
		return super.findRows(String.class, sql, DataSourceProperty.PUB, new HashMap<>(0));
	}

	/**
	 * 查询下游字段血缘关系
	 * @return
	 * @throws Exception
	 */
	public List<FieldLineage> findDownStreamFieldLineage(String tableFieldId) throws Exception {
		String sql = "select a.to_table_info_id,a.to_table_field_id,a.from_table_info_id,a.from_table_field_id,a.all_dependency,a.all_superior,a.manual_flag,a.sequence," +
				" b.field_comment as to_field_comment, c.field_comment as from_field_comment" +
				" from rms_table_lineage_dependency a" +
				" left join rms_table_field b on a.to_table_field_id = b.table_field_id" +
				" left join rms_table_field c on a.from_table_field_id = c.table_field_id" +
				" where instr(all_dependency, '||" + tableFieldId + "|') > 0 ";
		return super.findRows(FieldLineage.class, sql, DataSourceProperty.PUB, new HashMap<>(0));
	}

	/**
	 * 查询上游字段血缘关系
	 * @param tableFieldId
	 * @return
	 * @throws Exception
	 */
	public List<FieldLineage> findUpStreamFieldLineage(String tableFieldId) throws Exception {
		String sql = "select to_table_info_id,to_table_field_id,from_table_info_id,from_table_field_id,all_dependency,all_superior,manual_flag,sequence," +
				" b.field_comment as to_field_comment, c.field_comment as from_field_comment" +
				" from rms_table_lineage_dependency a" +
				" left join rms_table_field b on a.to_table_field_id = b.table_field_id" +
				" left join rms_table_field c on a.from_table_field_id = c.table_field_id" +
				" where instr(all_superior, '||" + tableFieldId + "|') > 0 ";
		return super.findRows(FieldLineage.class, sql, DataSourceProperty.PUB, new HashMap<>(0));
	}

	public UpdateResult setSystemParamsByParaid(String paraid, String paravalue) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("paravalue", paravalue);
		params.put("paraid", paraid);
		return super.update("UPDATE sys_param SET paravalue = $S{paravalue} WHERE paraid = $S{paraid}", params);
	}

	public List<SqlRow> queryTwoDimensionalTableField(String tableName, List<String> columnList) throws Exception {
		String columnString = String.join("','", columnList);
		Map<String, Object> params = new HashMap<>();
		params.put("fields", columnString);
		params.put("tableName", tableName);
		/*String sql = "select a.reflect_column, LOWER(CONCAT(a.field_type, a.field_index)) as field_name" +
				" from base_report_column_info a" +
				" where LOWER(CONCAT(a.field_type, a.field_index)) in ('" + columnString + "')" +
				" and report_table = $S{tableName}";*/
		String sql = "select CONCAT(case when a.field_type='C' then '列' else '行' end,a.field_code) as reflect_column," +
				" LOWER(CONCAT(a.field_type, a.field_index)) as field_name" +
				" from base_report_column_info a" +
				" where LOWER(CONCAT(a.field_type, a.field_index)) in ('" + columnString + "')" +
				" and report_table = $S{tableName}";
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}

	public ResultSetMetaData getMetaData(String sql, Object... params) throws Exception {
		return super.getMetaData(sql, DataSourceProperty.PUB, params);
	}

}
