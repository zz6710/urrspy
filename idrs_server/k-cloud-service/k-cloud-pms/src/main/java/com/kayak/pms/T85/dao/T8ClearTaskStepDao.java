package com.kayak.pms.T85.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.T85.model.T8ClearTaskStep;
import org.springframework.stereotype.Repository;

/**
 * 文件名: TaClearTaskStepDao.java
 * 描述:  清算任务子步骤表
 * 创建人: zengzt
 * 创建时间:2020年6月6日下午4:21:49
 */
@Repository
public class T8ClearTaskStepDao extends ComnDao{


	/**
	 * 
	 * 方法描述:查询清算子任务
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8ClearTaskStep> findTaClearTaskSteps(SqlParam<T8ClearTaskStep> params) throws Exception {
		
		String sql = " SELECT task_id,step_no,step_name,can_replay,can_skip,is_slice,slice_service_class,slice_req_class FROM t8_clear_task_step ORDER BY step_no ";
		
		return super.findRows(sql, params);
	}
	
	/**
	 * 
	 * 方法描述:新增清算子任务
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertTaClearTaskStep(SqlParam<T8ClearTaskStep> params) throws Exception {
		

		String sql = " INSERT INTO t8_clear_task_step(task_id,step_no,step_name,can_replay,can_skip,is_slice,slice_service_class,slice_req_class)"
				+ " VALUES ($S{taskId},$S{stepNo},$S{stepName},$S{canReplay},$S{canSkip},$S{isSlice},$S{sliceServiceClass},$S{sliceReqClass})";
		
		return super.update(sql, params.getModel()).getEffect();
		
	}
	
	/**
	 * 
	 * 方法描述:更新清算子任务
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateTaClearTaskStep(SqlParam<T8ClearTaskStep> params) throws Exception {
		

		String sql = " UPDATE t8_clear_task_step "
				+ "	SET step_name=$S{stepName},"
				+ " can_replay=$S{canReplay},can_skip=$S{canSkip},is_slice=$S{isSlice},"
				+ "	slice_service_class=$S{sliceServiceClass},slice_req_class=$S{sliceReqClass}"
				+ " WHERE task_id = $S{taskId} AND step_no=$S{stepNo}";
		
		return super.update(sql, params.getModel()).getEffect();
		
	}
	
	/**
	 * 
	 * 方法描述:删除清算子任务
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteTaClearTaskStep(SqlParam<T8ClearTaskStep> params) throws Exception {
		

		String sql = " DELETE FROM t8_clear_task_step WHERE task_id = $S{taskId} AND step_no=$S{stepNo}";
		
		return super.update(sql, params.getModel()).getEffect();
		
	}
	
	
	
}
