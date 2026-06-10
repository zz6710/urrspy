package com.kayak.pms.T85.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.T85.model.T8ClearTaskInfo;
import com.kayak.pms.global.constants.BatchTaskType;
import com.kayak.pms.global.constants.GlobalConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件名: TaClearTaskInfoOperDao.java
 * 描述:  清算信息操作DAO
 * 创建人: zengzt
 * 创建时间:2020年5月9日下午5:05:51
 */
@Repository
public class T8ClearTaskInfoDao extends ComnDao{
	
	/**
	 * 
	 * 方法描述:查询清算信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8ClearTaskInfo> queryTaClearTaskInfos(SqlParam<T8ClearTaskInfo> params) throws Exception {
		
		String sql = "SELECT task_id,task_name,moduleid,task_type,can_again,service_class,req_class,in_class,simple_flow,lifecycle_type FROM t8_clear_task_info ORDER BY simple_flow";
		
		return super.findRows(sql, params);
	}
	
	/**
	 * 
	 * 方法描述:查询清算信息，标志是否已经设置到任务配置中
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8ClearTaskInfo> queryTaClearTaskInfoWithFlag(SqlParam<T8ClearTaskInfo> params) throws Exception {
		
		String sql = "SELECT t.task_id,t.task_name,t.moduleid,t.task_type,t.can_again,t.service_class,t.req_class,t.in_class,"
				+ "	t.simple_flow, t.lifecycle_type,(CASE WHEN se.task_id is null THEN '0' ELSE '1' END) AS in_task_set"
				+ "	FROM t8_clear_task_info t LEFT JOIN t8_clear_task_set se ON t.task_id=se.task_id AND se.task_group=$S{taskGroup}"
				+ " WHERE t.MODULEID='"+GlobalConstants.MODULEID+"'";

		T8ClearTaskInfo t8ClearTaskInfo = params.getModel();
		if(StringUtils.isNotBlank(t8ClearTaskInfo.getTaskType())){
			sql += " and t.task_type = $S{taskType}  ";
		}
		if(StringUtils.isNotBlank(t8ClearTaskInfo.getTaskModel())){
			sql += " and t.task_model = $S{taskModel}  ";
		}

		return super.findRows(sql, params);
	}
	
	/**
	 * 
	 * 方法描述:查询产品清算信息，标志是否已经设置到产品任务配置中
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8ClearTaskInfo> queryProdClearTaskInfoWithFlag(SqlParam<T8ClearTaskInfo> params) throws Exception {
		
		String sql = "SELECT t.task_id,t.task_name,t.moduleid,t.task_type,t.can_again,t.service_class,t.req_class,t.in_class,"
				+ " t.simple_flow, t.lifecycle_type, (CASE WHEN se.task_id is null THEN '0' ELSE '1' END) AS in_task_set"
				+ "	FROM t8_clear_task_info t LEFT JOIN t8_prod_task_set se ON t.task_id=se.task_id AND se.prod_mode=$S{prodMode}"
				+ " WHERE t.task_type = '"+BatchTaskType.PRODUCT+"' AND t.MODULEID='"+GlobalConstants.MODULEID+"'";
		
		return super.findRows(sql, params);
	}
	
	/**
	 * 
	 * 方法描述:查询产品清算信息，标志是否已经设置到产品任务配置中
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> queryTaClearTaskInfoById(String params) throws Exception {
		
		String sql = "SELECT 1 FROM t8_clear_task_info WHERE task_id=$S{taskId}";
		
		return super.findRows(sql, params);
	}
	
	
	
	/**
	 * 
	 * 方法描述:插入清算任务信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertTaClearTaskInfo(SqlParam<T8ClearTaskInfo> params) throws Exception {

		String sql = "INSERT INTO t8_clear_task_info(task_id,task_name,moduleid,task_type,can_again,service_class,req_class,in_class,simple_flow,lifecycle_type)"
				+ "VALUES($S{taskId},$S{taskName},'"+GlobalConstants.MODULEID+"',$S{taskType},$S{canAgain},$S{serviceClass},$S{reqClass},$S{inClass},$S{simpleFlow},$S{lifecycleType})";
		
		return super.update(sql, params.getModel()).getEffect();
		
	}
	
	/**
	 * 
	 * 方法描述:修改清算任务信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateTaClearTaskInfo(SqlParam<T8ClearTaskInfo> params) throws Exception {

		String sql = "UPDATE t8_clear_task_info"
				+ " SET task_name = $S{taskName},"
				+ "		can_again = $S{canAgain}, "
				+ "		service_class = $S{serviceClass},"
				+ "		req_class = $S{reqClass},"
				+ "		in_class = $S{inClass},"
				+ "		simple_flow = $S{simpleFlow},"
				+ "		lifecycle_type = $S{lifecycleType}"
				+ " WHERE task_id = $S{taskId} ";
		
		return super.update(sql, params.getModel()).getEffect();
		
	}
	
	/**
	 * 
	 * 方法描述:删除清算任务信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void deleteTaClearTaskInfo(SqlParam<T8ClearTaskInfo> params) throws Exception {

		doTrans(() -> {

			String sql = "DELETE FROM t8_clear_task_info WHERE task_id = $S{taskId} ";
			String sql2 = "DELETE FROM t8_clear_task_step WHERE task_id = $S{taskId} ";
			
			super.update(sql, params.getModel()).getEffect();
			super.update(sql2, params.getModel()).getEffect();
			
		});
		
	}
	

	/**
	 * 
	 * 方法描述:查询清算信息
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> checkTaskIdIsUsed(String taskId) throws Exception {
		
		String sql = "SELECT count(1) setNum "
					+"  FROM t8_clear_task_info info "
					+" WHERE info.task_id = $S{taskId} "
					+"   AND (EXISTS "
					+" (select 1 from t8_clear_task_set t1 where t1.task_id = info.task_id) "
					+"    OR EXISTS "
					+" (select 1 from t8_prod_task_set t2 where t2.task_id = info.task_id)) ";
		
		return super.findRows(sql, taskId);
	}
	
}
