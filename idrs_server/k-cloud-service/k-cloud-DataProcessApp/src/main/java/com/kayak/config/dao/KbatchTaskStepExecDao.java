package com.kayak.config.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.config.model.KbatchTaskStepExec;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.springframework.stereotype.Repository;

@Repository
public class KbatchTaskStepExecDao extends ComnDao {

    public SqlResult queryByTargeCode(SqlParam<KbatchTaskStepExec> param) throws  Exception{
        param.setMakeSql(false);
        SqlResult<KbatchTaskStepExec> rows = super.findRows("SELECT a.task_execid,a.task_date,a.task_group,a.task_id," +
                " a.step_no,b.step_name,a.moduleid,a.target_code,a.prod_code,a.exec_status,a.should_exec_date,a.exec_date," +
                " a.start_time,a.end_time,a.is_neglect,a.is_skip,a.is_replay,a.is_stop,a.is_slice,a.thread_uuid,a.server_node," +
                " a.server_name,a.server_ip,a.rtn_code,a.rtn_desc,a.crt_time,a.upd_time,a.distributor_batch FROM  " +
                " kbatch_task_step_exec a LEFT JOIN kbatch_task_step b ON  a.step_no=b.step_no WHERE " +
                " a.task_execid =$S{taskExecid} AND b.task_id=$S{taskId}  ORDER BY  a.step_no", param);
        return rows;
    }

    /**
     * 查询回滚任务 by taskExecid
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<KbatchTaskStepExec> queryRevocation(SqlParam<KbatchTaskStepExec> params) throws Exception {
        String sql = "SELECT task_execid,task_date, step_no,exec_status,target_code, exec_date, server_ip, start_time, " +
                "rtn_desc FROM kbatch_task_step_exec t WHERE target_code = $S{taskExecid}";
        return super.findRows(sql, params);
    }

}
