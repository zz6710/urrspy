package com.kayak.dps.check.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.dps.check.model.T8SQLConfigModel;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class T8SQLConfigModelDao extends ComnDao {

	public SqlResult<T8SQLConfigModel> findT8SQLConfigModels(SqlParam<T8SQLConfigModel> params) throws Exception {
		T8SQLConfigModel t8SQLConfigModel = params.getModel();
		String sql = "SELECT t1.exeid,t1.sqlid,t1.desc,t1.sqlstr,t1.task_id,t1.exe_order,t2.task_name,t3.username,t1.version,t1.operation_date,t1.operation_time  FROM base_port_sql_info t1 " +
				"\t\t\t\t left join kbatch_task_info t2 on t1.task_id = t2.task_id left join sys_user t3 ON t1.userid = t3.userid where 1 = 1 ";
		if(!Tools.strIsEmpty(t8SQLConfigModel.getExeid())){
			sql += " and t1.exeid like '%$U{exeid}%' " ;
		}
		if(!Tools.strIsEmpty(t8SQLConfigModel.getSqlid())){
			sql += "  and t1.sqlid like '%$U{sqlid}%' " ;
		}
		if(!Tools.strIsEmpty(t8SQLConfigModel.getTaskId())){
			sql += "  and t1.task_id = $S{taskId} " ;
		}
		sql += " order by t1.exe_order+0,t1.exeid";
		return super.findRows(sql, params);
	}

	public UpdateResult addT8SQLConfigModel(SqlParam<T8SQLConfigModel> params) throws Exception {
		return super.update("INSERT INTO base_port_sql_info(exeid,sqlid,`desc`,sqlstr , task_id,exe_order ) " +
						"VALUES($S{exeid},$S{sqlid},$S{desc},$S{sqlstr},$S{taskId},$S{exeOrder} )",
				params.getModel());
	}

	public UpdateResult updateT8SQLConfigModel(SqlParam<T8SQLConfigModel> params) throws Exception {
		return super.update("UPDATE base_port_sql_info SET  exeid=$S{exeid}, sqlid=$S{sqlid} ," +
						"`desc`=$S{desc} ,sqlstr=$S{sqlstr}, " +
						"`task_id`=$S{taskId} ,exe_order=$S{exeOrder},userid = $S{userid},version = $S{version},operation_date = $S{operationDate},operation_time = $S{operationTime}" +
						" WHERE  exeid=$S{exeid} ",
				params.getModel());
	}

	public UpdateResult deleteT8SQLConfigModel(SqlParam<T8SQLConfigModel> params) throws Exception {
		return super.update("DELETE FROM base_port_sql_info WHERE  exeid=$S{exeid} ",
				params.getModel());
	}

	public T8SQLConfigModel getMaxVersion(String taskId,String exeid) throws Exception {
		String sql = "SELECT IF(MAX(VERSION)<>'' or MAX(VERSION) is not null,MAX(VERSION),'V1.0') version FROM base_port_sql_info WHERE task_id =$S{taskId} and exeid='"+exeid+"'";
		return super.findRow(T8SQLConfigModel.class, sql,
				DataSourceProperty.PUB, taskId);
	}
}
