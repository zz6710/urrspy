package com.kayak.config.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.config.model.KbatchTaskStep;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.springframework.stereotype.Repository;

/**
 * 文件名: TaClearTaskStepDao.java
 * 描述:  清算任务子步骤表
 * 创建人: zengzt
 * 创建时间:2020年6月6日下午4:21:49
 */
@Repository
public class KbatchTaskStepDao extends ComnDao{


	/**
	 * 方法描述:查询清算子任务
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<KbatchTaskStep> findKbatchTaskSteps(SqlParam<KbatchTaskStep> params) throws Exception {
		String sql = " SELECT task_id,step_no,step_name,can_replay,can_skip,is_slice,slice_service_class,slice_req_class FROM kbatch_task_step ORDER BY step_no ";
		return super.findRows(sql, params);
	}
	
	/**
	 * 方法描述:新增清算子任务
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertKbatchTaskStep(SqlParam<KbatchTaskStep> params) throws Exception {
		String sql = " INSERT INTO kbatch_task_step(task_id,step_no,step_name,can_replay,can_skip,is_slice,slice_service_class,slice_req_class)"
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
	public int updateKbatchTaskStep(SqlParam<KbatchTaskStep> params) throws Exception {
		String sql = " UPDATE kbatch_task_step "
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
	public int deleteKbatchTaskStep(SqlParam<KbatchTaskStep> params) throws Exception {
		String sql = " DELETE FROM kbatch_task_step WHERE task_id = $S{taskId} AND step_no=$S{stepNo}";
		return super.update(sql, params.getModel()).getEffect();
	}

}
