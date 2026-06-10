package com.kayak.pms.T85.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.T85.model.T8ClearTaskSet;
import com.kayak.pms.T85.model.T8ClearTaskSetList;
import com.kayak.pms.global.constants.GlobalConstants;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件名: TaClearTaskSetOperDao.java
 * 描述:   清算组任务配置操作DAO
 * 创建人: zengzt
 * 创建时间:2020年5月6日下午4:18:17
 */
@Repository
public class T8ClearTaskSetDao extends ComnDao {

	/**
	 * 
	 * 方法描述:查询清算组任务配置
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8ClearTaskSet> queryTaClearTaskSets(SqlParam<T8ClearTaskSet> params) throws Exception {
		
		String sql = "SELECT se.task_group,se.task_id,se.moduleid,se.distributor_batch,se.exec_time,se.alarm_time,se.proc_start_time,"
				+ " se.proc_end_time,se.pre_task_id,se.task_params,se.display_order,se.simple_flow,se.exec_order,info.task_name "
				+ "	FROM t8_clear_task_set se LEFT JOIN t8_clear_task_info info ON se.task_id=info.task_id "
				+ " WHERE se.task_group = $S{taskGroup} AND se.moduleid='"+GlobalConstants.MODULEID+"' ORDER BY se.DISPLAY_ORDER";
		
		return super.findRows(sql, params);
	}
	
	/**
	 * 
	 * 方法描述:插入清算组任务配置
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertTaClearTaskSet(T8ClearTaskSet params) throws Exception {

		String sql = "INSERT INTO t8_clear_task_set(task_group,task_id,moduleid,distributor_batch,exec_time,alarm_time,proc_start_time,proc_end_time,pre_task_id,task_params,display_order,simple_flow,exec_order)"
				+ "VALUES($S{taskGroup},$S{taskId},$S{moduleid},$S{distributorBatch},$S{execTime},$S{alarmTime},$S{procStartTime},$S{procEndTime},$S{preTaskId},$S{taskParams},$S{displayOrder},$S{simpleFlow},$S{execOrder})";
		
		return super.update(sql, params).getEffect();
		
	}
	
	/**
	 * 
	 * 方法描述:删除清算组任务配置
	 * @return
	 * @throws Exception
	 */
	public int deleteTaClearTaskSet(String taskGroup) throws Exception {

		String sql = "DELETE FROM t8_clear_task_set WHERE task_group = $S{taskGroup} AND moduleid='"+GlobalConstants.MODULEID+"'";
		
		return super.update(sql, taskGroup).getEffect();
		
	}
	
	/**
	 * 
	 * 方法描述：组清算任务
	 * @param params
	 * @throws Exception 
	 */
	public void addTaClearTaskSets(T8ClearTaskSetList params) throws Exception{
		
		//先删除原任务设置，再插入新任务设置
		doTrans(() -> {
			
			deleteTaClearTaskSet(params.getTaskGroup());
			
			List<T8ClearTaskSet> taskSetList = params.getTaskSetList();
			for (T8ClearTaskSet taskSet : taskSetList) {
				insertTaClearTaskSet(taskSet);
			}
			
		});
		
	}
	
	
}
