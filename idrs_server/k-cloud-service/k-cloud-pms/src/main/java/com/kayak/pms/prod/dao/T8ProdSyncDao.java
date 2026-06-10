package com.kayak.pms.prod.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.prod.model.T8ProdSync;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;

@Repository
public class T8ProdSyncDao extends ComnDao {

	public SqlResult<T8ProdSync> findT8ProdSyncs(SqlParam<T8ProdSync> params) throws Exception {
		return super.findRows("SELECT id,table_name,field_name,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user FROM t8_prod_sync", params);
	}

	public UpdateResult addT8ProdSync(SqlParam<T8ProdSync> params) throws Exception {
		return super.update("INSERT INTO t8_prod_sync(id,table_name,field_name,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user) VALUES($AUTOIDS{id},$S{tableName},$S{fieldName},$S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser})",
				params.getModel());
	}
	
	public UpdateResult updateT8ProdSync(SqlParam<T8ProdSync> params) throws Exception {
		return super.update("UPDATE t8_prod_sync SET table_name=$S{tableName} ,field_name=$S{fieldName},crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,crt_user=$S{crtUser} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user=$S{updUser}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteT8ProdSync(SqlParam<T8ProdSync> params) throws Exception {
		return super.update("DELETE FROM t8_prod_sync WHERE  id=$S{id} ",
				params.getModel());
	}

	public List<T8ProdSync> findTableName() throws Exception {
		return super.findRows(T8ProdSync.class,"select table_name,field_name from t8_prod_sync",0,null);
	}

    public SqlResult<T8ProdSync> getTableField(SqlParam<T8ProdSync> params) throws Exception {
		String sql="select column_name as field_name from information_schema.columns where table_name='"+params.getModel().getTableName()+"' ";
		return super.findRows(sql,params);
    }

	public Boolean findTableIsExist(SqlParam<T8ProdSync> params) throws Exception {
		int count= super.findRow(Integer.class, "SELECT count(*) as count FROM information_schema.TABLES WHERE table_name ='"+params.getModel().getTableName()+"';", 0, params);
		if(count>0){
			return true;
		}
		return false;
	}

	public Boolean findTableByName(SqlParam<T8ProdSync> params) throws Exception {
		Integer count = super.findRow(Integer.class, "SELECT count(id) as id FROM t8_prod_sync WHERE table_name ='" + params.getModel().getTableName() + "';", 0, params);
		if(count>0){
			return true;
		}
		return false;
	}
}
