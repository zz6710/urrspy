package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.ImportTemplateManageField01;
import com.kayak.dps.app.model.ImportTemplateManageField02;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImportTemplateManageField02Dao extends ComnDao {

	/**
	*@param @param params
	*@return SqlResult<AssetManagePlanInfo>
	*@date 2023/8/11  19:49
	*@description 模板字段列表查询
	*
	*/
	public SqlResult<ImportTemplateManageField02> findTemplateFieldList(SqlParam<ImportTemplateManageField02> params) throws Exception {
		String sql = "SELECT id,system_table_name,  (select   CONCAT(system_table_name_cn,'-',system_table_name) as name  from  app_table_info  where  id  = a.system_table_name  ) as  table_name , value_row, value_column, column_type, upt_usr, upt_date, upt_time\n" +
				"FROM import_template_manage_field_02 a  where  system_table_name =  $S{systemTableName} ";
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}



	/**
	*@param @param params
	*@return UpdateResult
	*@date 2023/8/11  20:09
	*@description  新增模板
	*
	*/
	public UpdateResult addTemplateFieldInfo(SqlParam<ImportTemplateManageField02> params) throws Exception {
		return super.update("INSERT INTO import_template_manage_field_02 " +
						"(system_table_name, value_row, value_column, column_type, upt_usr, upt_date, upt_time) " +
						"VALUES($S{systemTableName}, $S{valueRow}, $S{valueColumn}, $S{columnType}, $S{uptUsr}, $S{uptDate}, $S{uptTime})",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult uptTemplateFieldInfo(ImportTemplateManageField02 params) throws Exception {
		return super.update("UPDATE import_template_manage_field_02 " +
						"SET value_row=$S{valueRow}, value_column=$S{valueColumn}, column_type=$S{columnType}, upt_usr=$S{uptUsr}, upt_date=$S{uptDate}, upt_time=$S{uptTime} " +
						"WHERE id=  $S{id}",
				DataSourceProperty.PUB,params);
	}

	public UpdateResult delTemplateFieldInfo(ImportTemplateManageField02 params) throws Exception {
		return super.update("DELETE FROM import_template_manage_field_02 " +
						"WHERE id=  $S{id}",
				DataSourceProperty.PUB,params);
	}



	/**
	 *@param @param params
	 *@return SqlResult<AssetManagePlanInfo>
	 *@date 2023/8/11  19:49
	 *@description 模板字段列表查询
	 *
	 */
	public List<ImportTemplateManageField02> findTemplateFieldListByModel(ImportTemplateManageField02 params) throws Exception {
		String sql = "SELECT a.id, a.system_table_name, a.value_row, a.value_column, a.column_type, a.column_unit, a.upt_usr, a.upt_date, a.upt_time, (select  system_table_name as  itemkey  from  app_table_info where   id  = a.system_table_name ) as table_Name from import_template_manage_field_02 a where a.system_table_name = $S{systemTableName} ";
		return super.findRows(ImportTemplateManageField02.class,sql,DataSourceProperty.PUB, params);
	}
}
