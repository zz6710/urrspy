package com.kayak.dps.check.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.dps.check.model.T8SqlParamInfo;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class T8SqlParamInfoDao extends ComnDao {

	public SqlResult<T8SqlParamInfo> findT8SqlParamInfos(SqlParam<T8SqlParamInfo> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,code,sqlstr,data_type,remark,status FROM base_port_sql_param_info where 1=1");
		if (Tools.isNotEmpty(params.getModel().getCode())) {
			sql.append(" and code like '%").append(params.getModel().getCode()).append("%'");
		}
		if (Tools.isNotEmpty(params.getModel().getDataType())) {
			sql.append(" and data_type = '").append(params.getModel().getDataType()).append("'");
		}
		if (Tools.isNotEmpty(params.getModel().getStatus())) {
			sql.append(" and status = '").append(params.getModel().getStatus()).append("'");
		}
		return super.findRows(sql.toString(), params);
	}

	public UpdateResult addT8SqlParamInfo(SqlParam<T8SqlParamInfo> params) throws Exception {
		return super.update("INSERT INTO base_port_sql_param_info(id,code,sqlstr,data_type,remark,status) VALUES($AUTOIDI{id},$S{code},$S{sqlstr},$S{dataType},$S{remark},$S{status})",
				params.getModel());
	}
	
	public UpdateResult updateT8SqlParamInfo(SqlParam<T8SqlParamInfo> params) throws Exception {
		return super.update("UPDATE base_port_sql_param_info SET code=$S{code} ,sqlstr=$S{sqlstr} ,data_type=$S{dataType} ,remark=$S{remark} ,status=$S{status}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteT8SqlParamInfo(SqlParam<T8SqlParamInfo> params) throws Exception {
		return super.update("DELETE FROM base_port_sql_param_info WHERE  id=$I{id} ",
				params.getModel());
	}

}
