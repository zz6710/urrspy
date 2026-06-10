package com.kayak.dps.ods.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.AssetCollection;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;

@Repository
public class AssetCollectionDao extends ComnDao {

	public SqlResult<AssetCollection> findAssetCollections(SqlParam<AssetCollection> params) throws Exception {
		String sql = "SELECT * FROM dwd_asset_collection where 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getPage())) {
			sql += " and page = $S{page}";
		}
		if (StringUtils.isNotBlank(params.getModel().getRoleid())) {
			sql += " and roleid = $S{roleid}";
		}
		if (StringUtils.isNotBlank(params.getModel().getFieldType())) {
			sql += " and field_type = $S{fieldType}";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addAssetCollection(SqlParam<AssetCollection> params) throws Exception {
		return super.update("INSERT INTO dwd_asset_collection(page,roles,remark,page_field,field_type) VALUES($S{page},$S{roles},$S{remark},$S{pageField},$S{fieldType})",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult updateAssetCollection(SqlParam<AssetCollection> params) throws Exception {
		return super.update("UPDATE dwd_asset_collection SET page=$S{page} ,roles=$S{roles} ,remark=$S{remark} ,page_field=$S{pageField},field_type=$S{fieldType}  WHERE id = $S{id}",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult updateAssetCollectionforChange(SqlParam<AssetCollection> params) throws Exception {
		return super.update("UPDATE dwd_asset_collection SET page_field=$S{pageField} WHERE page=$S{page} and roles=$S{roles} and field_type=$S{fieldType}",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult deleteAssetCollection(SqlParam<AssetCollection> params) throws Exception {
		return super.update("DELETE FROM dwd_asset_collection WHERE id = $S{id}",
				DataSourceProperty.PUB,params.getModel());
	}
	// 获取角色
	public List<SqlRow> findSysRoles(Object params) throws Exception {
		return super.findRows("select roleid,rolename from sys_role ",DataSourceProperty.PUB, params);
	}

	/**
	 * 根据表名来获取表字段,并按中文字段首字母排序
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<AssetCollection> findTableColumns(SqlParam<AssetCollection> params) throws Exception {
		return super.findRows("select distinct COLUMN_NAME value,COLUMN_COMMENT label from information_schema.COLUMNS where table_name = $S{tableName} order by convert(label using gbk) asc ",DataSourceProperty.PUB,params);
	}

	/**
	 * 查询已配置字段
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findColumnsLabel(SqlParam<AssetCollection> params,String pageField) throws Exception {
		return super.findRows("select group_concat(t.label) label from (select distinct " +
						" COLUMN_COMMENT label from " +
						" information_schema.COLUMNS  " +
						" where table_name = $S{tableName} and find_in_set(COLUMN_NAME,'"+pageField+"') " +
						" order by convert(label using gbk) asc) t "
				,DataSourceProperty.PUB,params.getModel());
	}

	/**
	 * 根据表名来获取补录表需要更改的字段,并按中文字段首字母排序
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<AssetCollection> findTableSupplyUpdateColumns(SqlParam<AssetCollection> params) throws Exception {
		return super.findRows("select distinct page_field,roles,page,field_type from dwd_asset_collection " +
				" where roles = $S{roles} and page = $S{page} and field_type =$S{fieldType} ",
				DataSourceProperty.PUB,params);
	}

	// 获取表名
	public List<SqlRow> getTableName(Object param) throws Exception {
		return super.findRows("select itemval tableName from  sys_dict_item  where dict = 'tableName' and itemkey= $S{page}",DataSourceProperty.PUB,param);
	}

	// 根据用户id获取角色id
	public List<SqlRow> getRoleIdFromUserId(String userid) throws Exception {
		return super.findRows("select group_concat(roleid) roleid from sys_user_role where userid = $S{userid} group by userid",DataSourceProperty.PUB,userid);
	}

	// 根据角色id获取可以补录的字段
	public List<SqlRow> findColumns(SqlParam<AssetCollection> params) throws Exception {
		return super.findRows("select concat_ws(',',page_field) page_field from  dwd_asset_collection where roles in ($U{roleid}) and page = $S{page} and field_type = $S{fieldType} group by page,field_type,page_field",DataSourceProperty.PUB,params.getModel());
	}

	public List<SqlRow> findRolesName(Object map) throws Exception {
		return super.findRows("select rolename from sys_role where roleid = $S{roleid}",DataSourceProperty.PUB, map);
	}

	public List<SqlRow> isOnlyOne(Object map) throws Exception {
		return super.findRows("SELECT SCR_CD FROM $U{checkTableName} WHERE SCR_ID = $S{scrId}",DataSourceProperty.PUB, map);
	}

	public List<SqlRow> isOnlyForOne(Object map) throws Exception {
		return super.findRows("SELECT $U{checkKey} FROM $U{checkTableName} WHERE $U{checkKey} = $S{checkValue}",DataSourceProperty.PUB, map);
	}
	public int findCount(SqlParam<AssetCollection> params) throws Exception {
		String sql="SELECT count(1) cnt " +
				"   FROM dwd_asset_collection t1 "  +
				"	WHERE t1.page ='"+params.getParams().get("page")+"'"
				+" and t1.roles='"+params.getParams().get("roles")+"'"
				+" and t1.field_Type='"+params.getParams().get("fieldType")+"'";
		return super.findRow(sql, null).getInteger("cnt");
	}
}
