package com.kayak.dps.sqlflow.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.dps.sqlflow.model.RmsFieldInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RmsFieldInfoDao extends ComnDao {

	public SqlResult<RmsFieldInfo> findRmsFieldInfos(SqlParam<RmsFieldInfo> params) throws Exception {
		String sql = "SELECT a.id,a.table_field_id,a.table_info_id,a.field_name,a.field_data_type,a.field_comment,a.field_index," +
				" b.database_name, b.table_name, b.comment, b.owner" +
				" FROM rms_table_field a" +
				" left join rms_table_info b on a.table_info_id = b.table_info_id" +
				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getTableName())) {
			sql += " and b.table_name like concat('%', $S{tableName}, '%')";
		}
		if (Tools.isNotEmpty(params.getModel().getFieldName())) {
			sql += " and a.field_name like concat('%', $S{fieldName}, '%')";
		}
		if (Tools.isNotEmpty(params.getModel().getOwner())) {
			sql += " and b.owner = $S{owner}";
		}
		return super.findRows(sql, params);
	}

	public List<RmsFieldInfo> findFieldNameByTableInfoId(String tableInfoId, String excludeColumn) throws Exception {
		String column = "";
		if (Tools.isNotEmpty(excludeColumn)) {
			column = excludeColumn.replace(",", "','");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("tableInfoId", tableInfoId);
		params.put("column", column);
		String sql = "SELECT a.table_field_id, a.field_name, a.field_comment" +
				" FROM rms_table_field a" +
				" where a.table_info_id = $S{tableInfoId}" +
				" and a.field_name not in ('" + column + "')" +
				" order by a.field_index";
		return super.findRows(RmsFieldInfo.class, sql, DataSourceProperty.PUB, params);
	}

	public UpdateResult addRmsFieldInfo(SqlParam<RmsFieldInfo> params) throws Exception {
		return super.update("INSERT INTO rms_table_field(id,table_field_id,table_info_id,field_name,field_data_type,field_comment,field_index) VALUES($AUTOIDI{id},$S{tableFieldId},$S{tableInfoId},$S{fieldName},$S{fieldDataType},$S{fieldComment},$I{fieldIndex})",
				params.getModel());
	}

	public UpdateResult updateRmsFieldInfo(SqlParam<RmsFieldInfo> params) throws Exception {
		return super.update("UPDATE rms_table_field SET table_field_id=$S{tableFieldId} ,table_info_id=$S{tableInfoId} ,field_name=$S{fieldName} ,field_data_type=$S{fieldDataType} ,field_comment=$S{fieldComment} ,field_index=$I{fieldIndex}  WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteRmsFieldInfo(SqlParam<RmsFieldInfo> params) throws Exception {
		return super.update("DELETE FROM rms_table_field WHERE  id=$I{id} ",
				params.getModel());
	}

	public void truncateRmsFieldInfo() throws Exception {
		super.update("TRUNCATE TABLE rms_table_field");
	}

	public void insertRmsFieldInfoFromSchema(String database) throws Exception {
		if (Tools.isEmpty(database)) {
			return ;
		}
		database = database.replace(",", "','");
		super.update("INSERT INTO rms_table_field(table_field_id, table_info_id, field_name, field_data_type, field_comment, field_index)\n" +
			" select LOWER(CONCAT(c.table_schema, '.', c.TABLE_NAME, '.', c.COLUMN_NAME)), LOWER(CONCAT(c.table_schema, '.', c.TABLE_NAME)),\n" +
			" LOWER(c.COLUMN_NAME), c.COLUMN_TYPE, c.COLUMN_COMMENT, c.ORDINAL_POSITION\n" +
			" from information_schema.columns c join information_schema.TABLES t on c.TABLE_NAME = t.TABLE_NAME  and c.TABLE_SCHEMA = t.TABLE_SCHEMA " +
			" where length(c.COLUMN_TYPE) <= 20" +
			" and t.TABLE_TYPE =1 and t.table_schema in ('" + database + "')");
	}

	public void deleteTwoDimensionalFiledInfo(String database) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("database", database);
		super.update("delete from rms_table_field" +
				" where table_info_id in (" +
				"    SELECT DISTINCT LOWER(concat($S{database}, '.', report_table)) " +
				"    FROM base_report_info" +
				"    where coordinate_type = '2'" +
				" )", params);
	}

	public void insertTwoDimensionalFiledInfo(String database) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("database", database);
		/*super.update("INSERT INTO rms_table_field(table_field_id, table_info_id, field_name, field_data_type, field_comment, field_index)" +
				" select LOWER(CONCAT($S{database}, '.', a.report_table, IFNULL(a.reflect_column, '')))," +
				" LOWER(CONCAT($S{database}, '.', a.report_table))," +
				" LOWER(IFNULL(a.reflect_column, ''))," +
				" ''," +
				" a.field_name," +
				" a.field_index" +
				" from base_report_column_info a" +
				" inner join base_report_info b on a.report_table = b.report_table and b.coordinate_type = '2'", params);*/
		super.update("INSERT INTO rms_table_field(table_field_id, table_info_id, field_name, field_data_type, field_comment, field_index)" +
				" select LOWER(CONCAT($S{database}, '.', a.report_table, '.'," +
				" CONCAT(case when a.field_type='C' then '列' else '行' end,a.field_code)))," +
				" LOWER(CONCAT($S{database}, '.', a.report_table))," +
				" LOWER(CONCAT(case when a.field_type='C' then '列' else '行' end,a.field_code))," +
				" ''," +
				" a.field_name," +
				" a.field_index" +
				" from base_report_column_info a" +
				" inner join base_report_info b on a.report_table = b.report_table and b.coordinate_type = '2'", params);

	}

	public void addFiledInfo(String database, String tableName, String fieldName, String fieldComment) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("database", database);
		params.put("tableName", tableName);
		params.put("fieldName", fieldName);
		// 校验字段是否存在
		String fieldCheckSql = "select table_field_id from rms_table_field where table_field_id = LOWER(CONCAT($S{database}, '.', $S{tableName}, '.', $S{fieldName} ))";
		SqlRow resultRow = super.findRow(fieldCheckSql, DataSourceProperty.PUB, params);
		if(resultRow == null || resultRow.isEmpty()){
			String fieldIndexSql = "select max(field_index)+1 from rms_table_field where table_info_id = concat($S{database}, '.', $S{tableName})";
			String fieldIndex = super.findRow(String.class, fieldIndexSql, DataSourceProperty.PUB, params);
			params.put("fieldIndex", fieldIndex);
			params.put("fieldComment", fieldComment);
			super.update("REPLACE INTO rms_table_field(table_field_id, table_info_id, field_name, field_data_type, field_comment, field_index)" +
					" values (" +
					" LOWER(CONCAT($S{database}, '.', $S{tableName}, '.', $S{fieldName} ))," +
					" LOWER(CONCAT($S{database}, '.', $S{tableName}))," +
					" $S{fieldName}," +
					" ''," +
					" $S{fieldComment}," +
					" $S{fieldIndex}" +
					" )", params);
		}

	}

}
