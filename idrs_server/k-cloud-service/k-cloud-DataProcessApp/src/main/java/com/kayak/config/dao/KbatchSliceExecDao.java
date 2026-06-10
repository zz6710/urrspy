package com.kayak.config.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.config.model.KbatchSliceExec;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import org.springframework.stereotype.Repository;

@Repository
public class KbatchSliceExecDao extends ComnDao {

    public SqlResult queryByTargeCode(SqlParam<KbatchSliceExec> param) throws  Exception{
        param.setMakeSql(false);
        String sql =" SELECT slice_execid,task_execid,task_id,step_no,moduleid,slice_status,slice_type" +
                ",target_code,prod_code,datasource,slice_no,slice_start,slice_end,slice_length,busi_params,exec_order" +
                ",in_queue_time,exec_start_date,exec_start_time,exec_end_date,exec_end_time,app_name,thread_id" +
                ",thread_uuid,server_node,server_name,server_ip,rtn_desc FROM kbatch_slice_exec where task_execid = $S{taskExecid} ";
        if (!Tools.strIsEmpty(param.getModel().getStepNo())) {
            sql = sql + "AND step_no = $S{stepNo} " ;
        }
        if (!Tools.strIsEmpty(param.getModel().getSliceStatus())) {
            sql = sql + "AND slice_status = $S{sliceStatus} " ;
        }
        if (!Tools.strIsEmpty(param.getModel().getDatasource())) {
            sql = sql + "AND datasource = $S{datasource} " ;
        }
        if (!Tools.strIsEmpty(param.getModel().getServerIp())) {
            sql = sql + "AND server_ip = $S{serverIp} " ;
        }
        sql = sql + " ORDER BY step_no " ;
        return super.findRows(sql ,param);
    }

    public SqlResult queryStepNo(SqlParam<KbatchSliceExec> param) throws  Exception{
        return super.findRows(" SELECT DISTINCT step_no from kbatch_slice_exec where task_execid = $S{taskExecid} ",param);
    }

    public SqlResult querySliceStatus(SqlParam<KbatchSliceExec> param) throws  Exception{
        return super.findRows(" SELECT DISTINCT slice_status FROM kbatch_slice_exec WHERE task_execid = $S{taskExecid} ",param);
    }

    public SqlResult queryDatasource(SqlParam<KbatchSliceExec> param) throws  Exception{
        return super.findRows(" SELECT DISTINCT datasource FROM kbatch_slice_exec WHERE task_execid = $S{taskExecid} ",param);
    }

    public SqlResult queryServerIp(SqlParam<KbatchSliceExec> param) throws  Exception{
        return super.findRows(" SELECT DISTINCT server_ip FROM kbatch_slice_exec WHERE task_execid = $S{taskExecid} ",param);
    }

}
