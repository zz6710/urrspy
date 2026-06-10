package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.ods.model.BaseEtlLog;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class BaseEtlLogDao extends ComnDao {

    public SqlResult<BaseEtlLog> findT8OdsTaskLogs(SqlParam<BaseEtlLog> params) throws Exception {
        return super.findRows("SELECT id,table_name,select_condition,exec_start_time,exec_end_time,cost_time,task_status,update_date,update_time,remark FROM t8_ods_task_log order by exec_start_time desc", params);
    }

    public UpdateResult addT8OdsTaskLog(SqlParam<BaseEtlLog> params) throws Exception {
        return super.update("INSERT INTO t8_ods_task_log(table_name,select_condition,exec_start_time,exec_end_time,cost_time,task_status,update_date,update_time,remark) VALUES($S{tableName},$S{selectCondition},$S{execStartTime},$S{execEndTime},$S{costTime},$S{taskStatus},$S{updateDate},$S{updateTime},$S{remark})",
                params.getModel());
    }

    public String saveTaskLog(Map<String, Object> logParam) throws Exception {
        String id = "";
        try {
            String insLog = "insert into t8_ods_task_log (table_name,select_condition,exec_start_time,exec_end_time,cost_time,task_status,update_date,update_time,remark) " +
                    "values($S{table_name},$S{select_condition},$S{exec_start_time},$S{exec_end_time},$S{cost_time},$S{task_status},$S{update_date},$S{update_time},$S{remark})";

            id = super.update(insLog, 0, logParam).getAutoId();//插入日志
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    public UpdateResult updateT8OdsTaskLog(SqlParam<BaseEtlLog> params) throws Exception {
        return super.update("UPDATE t8_ods_task_log SET table_name=$S{tableName} ,select_condition=$S{selectCondition} ,exec_start_time=$S{execStartTime} ,exec_end_time=$S{execEndTime} ,cost_time=$S{costTime} ,task_status=$S{taskStatus} ,update_date=$S{updateDate} ,update_time=$S{updateTime} ,remark=$S{remark}  WHERE  id=$S{id} ",
                params.getModel());
    }

    public UpdateResult deleteT8OdsTaskLog(SqlParam<BaseEtlLog> params) throws Exception {
        return super.update("DELETE FROM t8_ods_task_log WHERE  id=$S{id} ",
                params.getModel());
    }

    public void updateTaskLog(Map<String, Object> logParam) throws Exception {
        try {
            String insLog = "update t8_ods_task_log set exec_end_time=$S{exec_end_time},cost_time=$S{cost_time},task_status=$S{task_status},update_date=$S{update_date},update_time=$S{update_time},remark=$S{remark}  where id=$S{id}";

            super.update(insLog, 0, logParam);//插入日志
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
