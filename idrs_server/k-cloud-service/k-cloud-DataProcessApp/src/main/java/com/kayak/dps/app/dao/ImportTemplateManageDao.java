package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.clear.utils.Tools;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.AssetManagePlanInfo;
import com.kayak.dps.app.model.ImportTemplateManage;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ImportTemplateManageDao extends ComnDao {

	/**
	 *@param @param params
	 *@return UpdateResult
	 *@date 2023/12/11  20:09
	 *@description  修改起始行起始列
	 *
	 */
	public UpdateResult updTemplateInfoRowStart(ImportTemplateManage params) throws Exception {
		if (Tools.strIsEmpty(params.getColumnStart())) {
			params.setColumnStart("");
		}
		return super.update("UPDATE import_template_manage SET row_start=$S{rowStart}, column_start=$S{columnStart}, skip_column = $S{skipColumn} WHERE id=$S{id} ",
				DataSourceProperty.PUB,params);
	}

	/**
	*@param @param params
	*@return SqlResult<AssetManagePlanInfo>
	*@date 2023/8/11  19:49
	*@description 模板列表查询-首页
	*
	*/
	public SqlResult<ImportTemplateManage> findTemplateList(SqlParam<ImportTemplateManage> params) throws Exception {
		String sql = "SELECT id, row_start, column_start, skip_column, system_table_name, (select   CONCAT(system_table_name_cn,'-',system_table_name) as name  from  app_table_info  where  id   = xx.system_table_name)  as table_name, " +
				"template_name, template_file_name, template_file_path, import_type, version, template_status, imp_usr, oss_file_path," +
				" date_format(str_to_date(concat(imp_date, ' ', imp_time), '%Y%m%d %H%i%s'),'%Y-%m-%d %H:%i:%s') as imp_date, imp_time\n" +
				"FROM import_template_manage xx  where  id in (select  max(a.id)  from import_template_manage  a group  by  a.system_table_name)";
		if (Strings.isNotBlank(params.getModel().getSystemTableName())) {
			sql += " and system_table_name = $S{systemTableName}";
		}
		if (Strings.isNotBlank(params.getModel().getTemplateName())) {
			sql += " and template_name like '%$U{templateName}%'";
		}
		if (Strings.isNotBlank(params.getModel().getImportType())) {
			sql += " and import_type = $S{importType}";
		}
		if (Strings.isNotBlank(params.getModel().getTemplateStatus())) {
			sql += " and template_status = $S{templateStatus}";
		}
		sql += " order by imp_date desc, imp_time desc";
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}


	/**
	 *@param @param params
	 *@return SqlResult<AssetManagePlanInfo>
	 *@date 2023/8/11  19:49
	 *@description 模板列表查询-首页
	 *
	 */
	public SqlResult<ImportTemplateManage> findTemplateListHis(SqlParam<ImportTemplateManage> params) throws Exception {
		String sql = "SELECT id, system_table_name,(select   CONCAT(system_table_name_cn,'-',system_table_name) as name  from  app_table_info  where  id  = a.system_table_name  ) as  table_name , template_name, template_file_name, template_file_path, import_type, version, template_status, imp_usr, imp_date, imp_time\n" +
				"FROM import_template_manage a where  1 =1 ";
		if (Strings.isNotBlank(params.getModel().getSystemTableName())) {
			sql += " and system_table_name = $S{systemTableName}";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}



	/**
	 *@param @param params
	 *@return SqlResult<AssetManagePlanInfo>
	 *@date 2023/8/11  19:49
	 *@description 模板数据列表查询
	 *
	 */
	public SqlResult<ImportTemplateManage> findTemplateDataList(String sql ,SqlParam<ImportTemplateManage> params) throws Exception {

		if (Strings.isNotBlank(params.getModel().getSystemTableName())) {
			sql += " and x.itemkey = $S{systemTableName}";
		}
		if (Strings.isNotBlank(params.getModel().getTemplateName())) {
			sql += " and x.template_name like '%$U{templateName}%'";
		}
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and imp_date = $S{impDate}";
		}

		return super.findRows(sql,DataSourceProperty.PUB, params);
	}



	/**
	 *@param @param params
	 *@return SqlResult<AssetManagePlanInfo>
	 *@date 2023/8/11  19:49
	 *@description 模板数据列表查询 -枚举信息
	 *
	 */
	// TODO  OK
	public List<ImportTemplateManage> findTableNameList(ImportTemplateManage params) throws Exception {
//		String sql = "select  itemval as table_name  from  sys_dict_item sdi  where dict  = 'systemTableNameDict' " ;
		String sql = "select  system_table_name as table_name  from  app_table_info sdi  " ;
		return super.findRows(ImportTemplateManage.class,sql,DataSourceProperty.PUB, params);
	}

//	/**
//	 *@param @param params
//	 *@return SqlResult<AssetManagePlanInfo>
//	 *@date 2023/8/11  19:49
//	 *@description  查询真实表名
//	 *
//	 */
//	public List<ImportTemplateManage> findTableName(ImportTemplateManage params) throws Exception {
//		String sql = "select  itemval as  itemkey  from  sys_dict_item sdi  where dict  = 'systemTableNameDict'  and  itemkey  = $S{systemTableName} " ;
//		return super.findRows(ImportTemplateManage.class,sql,DataSourceProperty.PUB, params);
//	}




	/**
	 *@param @param params
	 *@return SqlResult<AssetManagePlanInfo>
	 *@date 2023/8/11  19:49
	 *@description 模板列表查询 -
	 *
	 */
	public SqlResult<ImportTemplateManage> findTemplateInfo(SqlParam<ImportTemplateManage> params) throws Exception {
		String sql = "SELECT id, system_table_name, template_name, template_file_name, template_file_path, import_type, version, template_status, imp_usr, imp_date, imp_time, oss_file_path\n" +
				"FROM import_template_manage where 1=1";
		if (Strings.isNotBlank(params.getModel().getSystemTableName())) {
			sql += " and system_table_name = $S{systemTableName}";
		}
		if (Strings.isNotBlank(params.getModel().getTemplateStatus())) {
			sql += " and template_status = $S{templateStatus}";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	/**
	 *@param @param params
	 *@return SqlResult<AssetManagePlanInfo>
	 *@date 2023/8/11  19:49
	 *@description 模板列表查询 - id 降序
	 *
	 */
	public SqlResult<ImportTemplateManage> findTemplateInfoOrderById(SqlParam<ImportTemplateManage> params) throws Exception {
		String sql = "SELECT id, system_table_name, template_name, template_file_name, template_file_path, import_type, version, template_status, imp_usr, imp_date, imp_time, oss_file_path\n" +
				"FROM import_template_manage where  system_table_name = $S{systemTableName}  order  by   id  desc";
//		if (Strings.isNotBlank(params.getModel().getSystemTableName())) {
//			sql += " and system_table_name = $S{systemTableName}";
//		}
//		if (Strings.isNotBlank(params.getModel().getTemplateStatus())) {
//			sql += " and template_status = $S{templateStatus}";
//		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}




	/**
	 *@param @param params
	 *@return SqlResult<AssetManagePlanInfo>
	 *@date 2023/8/11  19:49
	 *@description 模板列表查询- 查询当前最新模板 - 无分页
	 *
	 */
	// TODO
	public List<ImportTemplateManage> findTemplateInfoByModel(ImportTemplateManage params) throws Exception {
		String sql = "select b.row_start, b.column_start,b.skip_column, b.id, b.system_table_name, b.template_name, b.template_file_name, b.template_file_path, b.import_type, b.version, b.oss_file_path," +
				" b.template_status, b.imp_usr, b.imp_date, b.imp_time, (select  system_table_name as  itemval  from  app_table_info  " +
				" where id = b.system_table_name  limit 1) as table_Name" +
				" from import_template_manage b " +
				"where b.id in ( select max(a.id) from import_template_manage a group by a.system_table_name)";
		if (Strings.isNotBlank(params.getSystemTableName())) {
			sql += " and system_table_name = $S{systemTableName}";
		}
		return super.findRows(ImportTemplateManage.class,sql,DataSourceProperty.PUB, params);
	}

	/**
	 * 根据表名称获取模板信息
	 * @param tableName 表名称
	 * @return
	 * @throws Exception
	 */
	public List<ImportTemplateManage> findTemplateInfoByTableName(String tableName) throws Exception {
		String sql = "select b.row_start, b.column_start,b.skip_column, b.id, b.system_table_name, b.template_name, b.template_file_name, b.template_file_path, b.import_type, b.version, b.oss_file_path," +
				" b.template_status, b.imp_usr, b.imp_date, b.imp_time, (select  system_table_name as  itemval  from  app_table_info  " +
				" where id = b.system_table_name  limit 1) as table_Name" +
				" from import_template_manage b " +
				"where b.id in ( select max(a.id) from import_template_manage a group by a.system_table_name)";
		if (Strings.isNotBlank(tableName)) {
			sql += " and system_table_name = (select id from app_table_info t where t.system_table_name ='"+tableName+"')";
		}
		return super.findRows(ImportTemplateManage.class,sql,DataSourceProperty.PUB,null);
	}

	/**
	 *@param @param params
	 *@return SqlResult<AssetManagePlanInfo>
	 *@date 2023/8/11  19:49
	 *@description 模板列表查询- 查询当前最新模板 - 无分页
	 *
	 */
	public List<SqlRow> findTemplateData(ImportTemplateManage params) throws Exception {
		String sql = "select   *  from " + params.getTableName() + " where imp_date = $S{sysDataDate}  and sys_data_version  =  $S{sysDataVersion} " ;
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}



	/**
	 *@param @param params
	 *@return SqlResult<AssetManagePlanInfo>
	 *@date 2023/8/11  19:49
	 *@description 模板列表查询- 无分页 - 分组
	 *
	 */
	public List<ImportTemplateManage> findTemplateInfoByModelAndTable(ImportTemplateManage params) throws Exception {
		String sql = "SELECT id, system_table_name, template_name, template_file_name, template_file_path, import_type, version, template_status, imp_usr, imp_date, imp_time, oss_file_path\n" +
				"FROM import_template_manage  where  id  in (select  max(a.id)  from import_template_manage  a group  by  a.system_table_name)";
		if (Strings.isNotBlank(params.getSystemTableName())) {
			sql += " and system_table_name = $S{systemTableName}";
		}
		return super.findRows(ImportTemplateManage.class,sql,DataSourceProperty.PUB, params);
	}


	/**
	 *@param @param params
	 *@return SqlResult<AssetManagePlanInfo>
	 *@date 2023/8/11  19:49
	 *@description 模板列表查询 - 下载模板  - 根据id 查询
	 *
	 */
	public List<ImportTemplateManage> findTemplateInfoById(ImportTemplateManage params) throws Exception {
		String sql = "SELECT id, system_table_name, template_name, template_file_name, template_file_path, import_type, version, template_status, imp_usr, imp_date, imp_time, oss_file_path\n" +
				"FROM import_template_manage  where  1=1";
		if (Strings.isNotBlank(params.getId())) {
			sql += " and id = $S{id}";
		}
		return super.findRows(ImportTemplateManage.class,sql,DataSourceProperty.PUB, params);
	}

	/**
	*@param @param params
	*@return UpdateResult
	*@date 2023/8/11  20:09
	*@description  新增模板
	*
	*/
	public UpdateResult addTemplateInfo(SqlParam<ImportTemplateManage> params) throws Exception {
		return super.update("INSERT INTO import_template_manage\n" +
						"(system_table_name, template_name, template_file_name, template_file_path, import_type, version, template_status, imp_usr, imp_date, imp_time, oss_file_path)\n" +
						"VALUES($S{systemTableName}, $S{templateName}, $S{templateFileName}, $S{templateFilePath}, $S{importType}, $S{version}, $S{templateStatus}, $S{impUsr}, $S{impDate}, $S{impTime}, $S{ossFilePath})",
				DataSourceProperty.PUB,params.getModel());
	}


	/**
	 *@param @param params
	 *@return UpdateResult
	 *@date 2023/8/11  20:09
	 *@description  新增模板
	 *
	 */
	public UpdateResult addTemplateInfoByModel(ImportTemplateManage params) throws Exception {
		return super.update("INSERT INTO import_template_manage\n" +
						"(system_table_name, template_name, template_file_name, template_file_path, import_type, version, template_status, imp_usr, imp_date, imp_time, row_start, column_start, oss_file_path)\n" +
						"VALUES($S{systemTableName}, $S{templateName}, $S{templateFileName}, $S{templateFilePath}, $S{importType}, $S{version}, $S{templateStatus}, $S{impUsr}, $S{impDate}, $S{impTime}, $S{rowStart}, $S{columnStart}, $S{ossFilePath})",
				DataSourceProperty.PUB,params);
	}

	/**
	 *@param @param params
	 *@return UpdateResult
	 *@date 2023/8/11  20:09
	 *@description  模板启用停用
	 *
	 */
	public UpdateResult updTemplateInfo(ImportTemplateManage params) throws Exception {
		return super.update("UPDATE import_template_manage SET template_status=$S{templateStatus}   WHERE id=$S{id} ",
				DataSourceProperty.PUB,params);
	}




	/**
	 *@param @param params
	 *@return SqlResult<AssetManagePlanInfo>
	 *@date 2023/8/11  19:49
	 *@description 模板报送情况
	 *
	 */
	// TODO
	public List<ImportTemplateManage> findRepeatSubmission(ImportTemplateManage params) throws Exception {
		String sql = "select  id  from base_report_result where register_date  = $S{sysDataDate} and report_table = (select  system_table_name as  itemkey  from  app_table_info where   id  = $S{systemTableName}  limit 1) ";
		return super.findRows(ImportTemplateManage.class,sql,DataSourceProperty.PUB, params);
	}

	/**
	 *@param @param params
	 *@return SqlResult<AssetManagePlanInfo>
	 *@date 2023/8/11  19:49
	 *@description 查询数据的版本号
	 *
	 */
	public List<ImportTemplateManage> getDateVersionByDate(String sql,ImportTemplateManage params) throws Exception {
		return super.findRows(ImportTemplateManage.class,sql,DataSourceProperty.PUB, params);
	}

	/**
	 *@param @param params
	 *@return UpdateResult
	 *@date 2023/8/11  20:09
	 *@description  旧数据置为失效
	 *
	 */
	public UpdateResult updDateStatusByDate(String sql,ImportTemplateManage params) throws Exception {
		return super.update(sql, DataSourceProperty.PUB,params);
	}

	/**
	 * 查询真实表名
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String findTableName(ImportTemplateManage params) throws Exception {
		String tableName = "";
//		String sql = "select itemval as table_name from sys_dict_item where dict = 'systemTableNameDict' and itemkey = $S{systemTableName}";
		String sql = "select system_table_name as table_name from app_table_info where id = $S{systemTableName}";
		if (!super.findRows(sql, DataSourceProperty.PUB,params).isEmpty()){
			tableName = super.findRows(sql, DataSourceProperty.PUB,params).get(0).getString("table_name");
		}
		return tableName;
	}

	/**
	 * 根据表名和字段注释查询字段详细信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findTableFieldInfo(Map<String,Object> params) throws Exception {
//		String tableName = "";
		String sql = "select c.COLUMN_NAME,c.COLUMN_COMMENT,c.ORDINAL_POSITION,c.DATA_TYPE\n" +
				"from information_schema.COLUMNS c\n" +
				"where c.TABLE_SCHEMA = $S{database}\n" +
				"and c.TABLE_NAME = $S{tableName}";
//		if (!super.findRows(sql, DataSourceProperty.PUB,params).isEmpty()){
//			tableName = super.findRows(sql, DataSourceProperty.PUB,params).get(0).getString("table_name");
//		}
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}

	/**
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findTableFieldDictInfo(Map<String,Object> params) throws Exception {
		String sql = "select field_name from auto_generate_dict where field_name = $S{columnComment}";
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}
}
