package com.kayak.pms.T85.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.Sql;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.T85.model.T8ProdTaskSet;
import com.kayak.pms.T85.model.T8ProdTaskSetList;
import com.kayak.pms.global.constants.GlobalConstants;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件名: TaProdTaskSetOperDao.java
 * 描述:   产品清算任务配置操作DAO
 * 创建人: zengzt
 * 创建时间:2020年5月6日下午4:51:04
 */
@Repository
public class T8ProdTaskSetDao extends ComnDao {

	/**
	 * 
	 * 方法描述:查询产品清算任务配置
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8ProdTaskSet> selectTaProdTaskSets(SqlParam<T8ProdTaskSet> params) throws Exception {
		
		String sql = "SELECT se.prod_mode,se.task_id,se.moduleid,se.exec_time,se.alarm_time,"
				+ "	se.pre_task_id,se.task_params,se.display_order,se.simple_flow,se.exec_order,info.task_name,info.lifecycle_type "
				+ "		FROM t8_prod_task_set se LEFT JOIN t8_clear_task_info info ON se.task_id=info.task_id "
				+ "	WHERE se.prod_mode = $S{prodMode} AND se.moduleid='"+GlobalConstants.MODULEID+"' ORDER BY se.prod_mode,se.display_order";
		
		return super.findRows(sql, params);
	}
	
	/**
	 * 
	 * 方法描述:插入产品清算任务配置
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertTaProdTaskSet(T8ProdTaskSet params) throws Exception {

		String sqlAll = "INSERT INTO t8_prod_task_set(prod_mode,task_id,moduleid,exec_time,alarm_time,pre_task_id,task_params,display_order,simple_flow,exec_order,upd_time)"
				+ "VALUES($S{prodMode},$S{taskId},$S{moduleid},$S{execTime},$S{alarmTime},$S{preTaskId},$S{taskParams},$S{displayOrder},$S{simpleFlow},$S{execOrder},current_timestamp)";
		String sqlDb2 = "INSERT INTO t8_prod_task_set(prod_mode,task_id,moduleid,exec_time,alarm_time,pre_task_id,task_params,display_order,simple_flow,exec_order,upd_time)"
				+ "VALUES($S{prodMode},$S{taskId},$S{moduleid},$S{execTime},$S{alarmTime},$S{preTaskId},$S{taskParams},$S{displayOrder},$S{simpleFlow},$S{execOrder},current_timestamp)";
		//Sql sql = Sql.build().mysqlSql(sqlAll).mysqlSql(sqlDb2);
		Sql sql = Sql.build().mysqlSql(sqlAll).mysqlSql(sqlDb2);

		return super.update(sql, params).getEffect();
		
	}
	
	/**
	 * 
	 * 方法描述:删除产品清算任务配置
	 * @param
	 * @return
	 * @throws Exception
	 */
	public void deleteTaProdTaskSet(String prodMode) throws Exception {

		String sql = "DELETE FROM t8_prod_task_set WHERE prod_mode = $S{prodMode} ";

		//删除清算配置
		super.update(sql, prodMode);
		
	}
	
	public void addTaProdTaskSets(SqlParam<T8ProdTaskSetList> params) throws Exception{
		
		//先删除原任务设置，再插入新任务设置
		doTrans(() -> {
			
			deleteTaProdTaskSet(params.getModel().getProdMode());
			List<T8ProdTaskSet> taskSetList = params.getModel().getProdTaskList();
			for (T8ProdTaskSet taskSet : taskSetList) {
				insertTaProdTaskSet(taskSet);
			}
			
		});
		
	}
	
	
}
