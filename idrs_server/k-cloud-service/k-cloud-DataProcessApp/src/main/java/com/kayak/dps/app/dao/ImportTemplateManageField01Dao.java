package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.ImportTemplateManage;
import com.kayak.dps.app.model.ImportTemplateManageField01;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ImportTemplateManageField01Dao extends ComnDao {
	@Value("${database.schemas}")
	private String schemas;
	/**
	*@param @param params
	*@return SqlResult<AssetManagePlanInfo>
	*@date 2023/8/11  19:49
	*@description 模板字段列表查询
	*
	*/
	public SqlResult<ImportTemplateManageField01> findTemplateFieldList(SqlParam<ImportTemplateManageField01> params) throws Exception {
		String sql = "SELECT id, system_table_name, (select   CONCAT(system_table_name_cn,'-',system_table_name) as name  from  app_table_info  where  id  = a.system_table_name  ) as  table_name , database_column_code, database_column_name, column_type, template_column_serial, upt_usr, upt_date, upt_time\n" +
				"FROM import_template_manage_field_01 a  where  system_table_name =  $S{systemTableName} order by 0+template_column_serial asc";
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	/**
	 *@param @param params
	 *@return SqlResult<AssetManagePlanInfo>
	 *@date 2023/8/11  19:49
	 *@description 模板字段列表查询
	 *
	 */
	public List<ImportTemplateManageField01> findTemplateFieldListByModel(ImportTemplateManageField01 params) throws Exception {
		String sql = "select a.id, a.system_table_name, a.database_column_code, a.database_column_name, a.column_type, a.column_unit, a.template_column_serial, a.upt_usr, a.upt_date, a.upt_time, (select system_table_name as itemval from app_table_info   where id  = a.system_table_name  limit 1) as table_name from import_template_manage_field_01 a where a.system_table_name = $S{systemTableName} order by 0+template_column_serial asc";
		return super.findRows(ImportTemplateManageField01.class,sql,DataSourceProperty.PUB, params);
	}


	/**
	*@param @param params
	*@return UpdateResult
	*@date 2023/8/11  20:09
	*@description  新增模板
	*
	*/
	public UpdateResult addTemplateFieldInfo(SqlParam<ImportTemplateManageField01> params) throws Exception {
		return super.update("INSERT INTO import_template_manage_field_01 " +
						"(system_table_name, database_column_code, database_column_name, column_type, template_column_serial, upt_usr, upt_date, upt_time) " +
						"VALUES($S{systemTableName}, $S{databaseColumnCode}, $S{databaseColumnName}, $S{columnType}, $S{templateColumnSerial}, $S{uptUsr}, $S{uptDate}, $S{uptTime})",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult uptTemplateFieldInfo(ImportTemplateManageField01 params) throws Exception {
		return super.update("UPDATE import_template_manage_field_01 " +
						"SET database_column_code=$S{databaseColumnCode}, database_column_name=$S{databaseColumnName}, column_type=$S{columnType}, template_column_serial=$S{templateColumnSerial}, upt_usr=$S{uptUsr}, upt_date=$S{uptDate}, upt_time=$S{uptTime} " +
						"WHERE id=  $S{id}",
				DataSourceProperty.PUB,params);
	}

	public UpdateResult delTemplateFieldInfo(ImportTemplateManageField01 params) throws Exception {
		return super.update("DELETE FROM import_template_manage_field_01 " +
						"WHERE id=  $S{id}",
				DataSourceProperty.PUB,params);
	}



	/**
	 *@param @param params
	 *@return UpdateResult
	 *@date 2023/8/11  20:09
	 *@description  批量导入
	 *
	 */
	public UpdateResult batchImplData(String sql,Map<String,Object> params) throws Exception {
		return super.update(sql,DataSourceProperty.PUB,params);
	}


	public SqlResult<ImportTemplateManageField01> findColumnName(SqlParam<ImportTemplateManageField01> params) throws Exception {
		String []schemaGroup =schemas.split(",");
		String newschemas= StringUtils.join(schemaGroup,"','");
		String sql = "select COLUMN_NAME,COLUMN_COMMENT from information_schema.`COLUMNS` c " +
				" left join import_template_manage_field_01 a on c.COLUMN_NAME = a.database_column_code and a.system_table_name = $S{systemTableName} " +
				" where TABLE_SCHEMA in ('"+newschemas+"')" +
				" and TABLE_NAME = (select system_table_name from app_table_info where id = $S{systemTableName})" +
				" and a.database_column_code is null";
		if (StringUtils.isNotBlank(params.getModel().getDatabaseColumnCode())) {
			sql = sql + " and COLUMN_NAME = '"+params.getModel().getDatabaseColumnCode()+"' ";
		}
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}



	public SqlResult<ImportTemplateManageField01> findTableName(SqlParam<ImportTemplateManageField01> params) throws Exception {

		String sql = "select id, CONCAT(system_table_name_cn,'-',system_table_name) as system_table_name from app_table_info ";

		return super.findRows(sql, DataSourceProperty.PUB, params);
	}

	/**
	 * 字段配置导入
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult updateTableField(Map<String, Object> params) throws Exception {
		String sql = "INSERT INTO import_template_manage_field_01 (system_table_name, database_column_code, database_column_name, column_type,\n" +
				"                                             template_column_serial, upt_usr, upt_date, upt_time)\n" +
				"VALUES ($S{systemTableName}, $S{columnName}, $S{columnComment}, $S{dataType}, $S{index}, '', '', '')";
		return super.update(sql, DataSourceProperty.PUB, params);
	}
	/**
	 * 删除字段配置
	 * @param importTemplateInfo
	 * @return
	 * @throws Exception
	 */
	public UpdateResult deleteTableField(ImportTemplateManage importTemplateInfo) throws Exception {
		String sql = "delete from import_template_manage_field_01 where system_table_name = $S{systemTableName}";
		return super.update(sql, DataSourceProperty.PUB, importTemplateInfo);
	}
}
