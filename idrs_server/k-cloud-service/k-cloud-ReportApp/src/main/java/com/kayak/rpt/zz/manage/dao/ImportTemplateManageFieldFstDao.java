package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import com.kayak.rpt.zz.manage.model.ImportTemplateManageFieldFst;
import com.kayak.rpt.zz.manage.model.ImportTemplateManageVo;
import com.kayak.rpt.zz.manage.model.InitialSubRegistInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ImportTemplateManageFieldFstDao extends ComnDao {
	/**
	*@param @param params
	*@return SqlResult<AssetManagePlanInfo>
	*@date 2023/8/11  19:49
	*@description 模板字段列表查询
	*
	*/
	public SqlResult<ImportTemplateManageFieldFst> findTemplateFieldList(SqlParam<ImportTemplateManageFieldFst> params) throws Exception {
		String sql = "SELECT a.id, b.system_table_name, b.system_table_name_cn as table_name ,  " +
				"	a.database_column_code, a.database_column_name, a.column_type, a.template_column_serial, a.upt_usr, a.upt_date, a.upt_time  " +
				" FROM import_template_manage_field_01 a  left join app_table_info b on a.system_table_name = b.id " +
				" where b.system_table_name = $S{systemTableName}  order by 0+template_column_serial asc ";
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}


/*	*//**
	 * 查询数据表对应模板起始行  判断数据行位置
	 * @param params  对应的业务表名
	 * @return
	 * @throws Exception
	 */
	public SqlResult<ImportTemplateManageVo> findTemplateHeadStartRow(SqlParam<ImportTemplateManageVo> params) throws Exception {
		String sql = "SELECT a.row_start " +
				" FROM import_template_manage a  left join app_table_info b on a.system_table_name = b.id " +
				" where b.system_table_name = $S{systemTableName}  and template_status ='1' ";
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}
}
