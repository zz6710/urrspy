package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.entity.SubmitParams;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by daniel on 25/08/2017.
 */
@Repository("submitParamsDao")
public class SubmitParamsDao extends ComnDao {
    public void save(SubmitParams submitParams) throws Exception {
        String sql = "INSERT INTO opf_submit_params (id, process_instance_id, process_id, task_id, submit_params, create_date, create_time)" +
                " VALUES ($S{id}, $S{processInstanceId}, $S{processId}, $S{taskId}, $S{submitParams}, $S{createDate}, $S{createTime})";
        super.update(sql, submitParams);
    }

    public void delete(SubmitParams submitParams) throws Exception {
        String sql = "DELETE FROM opf_submit_params WHERE process_id=$S{processId} AND process_instance_id=$S{processInstanceId} AND task_id=$S{taskId}";
        super.update(sql, submitParams);
    }

    public SubmitParams getSubmitParamsByTask(SqlParam<SubmitParams> params) throws Exception {
        String sql = "SELECT id, process_instance_id, process_id, task_id, submit_params FROM opf_submit_params WHERE task_id=$S{taskId}";
        return super.findRow(SubmitParams.class, sql, 0, params.getModel());
    }

    public void saveByFind(String taskId, String oldTaskId) throws Exception {
        String sql = "INSERT INTO opf_submit_params (id, process_instance_id, process_id, task_id, submit_params, create_date, create_time)" +
                " SELECT '" + StringHelper.getPrimaryKey() + "', process_instance_id, process_id, '" + taskId + "', submit_params, create_date, create_time FROM opf_submit_params WHERE task_id='" + oldTaskId + "'";
        super.update(sql);
    }

    public List<SubmitParams> getSubmitParamsByProcessInstanceId(String processInstanceId) {
        return null;
    }

    public String getSubmitParamsByApprovalId(String approvalId) {
        return null;
    }
}
