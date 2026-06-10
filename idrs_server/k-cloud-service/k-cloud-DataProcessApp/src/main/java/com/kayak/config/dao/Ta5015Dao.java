package com.kayak.config.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.config.enums.DriverEnums;
import com.kayak.config.model.Ta5015;
import com.kayak.core.sql.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author: lfzh
 * @date: 2021-01-04 9:22
 */
@Repository
public class Ta5015Dao extends ComnDao {
    /**
     * 查询清算任务组
     *
     * @param params
     * @return
     */
    public SqlResult<Ta5015> queryClearGroups(SqlParam<Ta5015> params) throws Exception {
        String sql = "SELECT task_group,exec_task_type, task_group_name,pre_task_group,running_type,should_exec_time,last_task_group,task_model FROM kbatch_group_info where exec_task_type=$S{execTaskType} ORDER BY should_exec_time";
        return super.findRows(sql, params);
    }

    public List<SqlRow> getSequence(SqlParam<Ta5015> param) throws Exception {
        String sql = "";
        List<SqlRow> rows = null;
        if (DriverEnums.ORACLE.getType().equals(daoService.getDbType(0)) || DriverEnums.DB2.getType().equals(daoService.getDbType(0))) {
            sql = "SELECT LPAD(seq_task_group.nextVal,12,'0') sequence FROM kbatch_group_info";
            rows = super.findRows(sql);
        } else if (DriverEnums.MYSQL.getType().equals(daoService.getDbType(0))) {
            String random = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
            rows = new ArrayList<>();
            SqlRow sqlRow = new SqlRow();
            sqlRow.put("sequence", random);
            rows.add(sqlRow);
        }
        return rows;
    }

    public List<SqlRow> getAllTaskGloupName(SqlParam<Ta5015> param) throws Exception {
        String sql = "SELECT task_group_name name FROM kbatch_group_info where exec_task_type=$S{execTaskType}";
        List<SqlRow> rows = super.findRows(sql);
        return rows;
    }

    public UpdateResult DeleteAllData(Map<String,Object> tparam) throws Exception {
        String sql = "delete from kbatch_group_info where exec_task_type=$S{execTaskType}";
        UpdateResult result = super.update(sql,tparam);
        return result;
    }

    /**
     * 方法描述:插入清算组信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public int insertTaClearGroupInfo(SqlParam<Ta5015> params) throws Exception {
        String sqlAll = "INSERT INTO kbatch_group_info(task_group,task_group_name,exec_task_type,pre_task_group,crt_date,upd_date,last_task_group,running_type,should_exec_time,task_model)"
                + "VALUES($S{taskGroup},$S{taskGroupName},$S{execTaskType},$S{preTaskGroup},current_timestamp,current_timestamp,$S{lastTaskGroup},$S{runningType},$S{shouldExecTime},$S{taskModel})";
        String sqlDb2 = "INSERT INTO kbatch_group_info(task_group,task_group_name,exec_task_type,pre_task_group,crt_date,upd_date,last_task_group,running_type,should_exec_time,task_model)"
                + "VALUES($S{taskGroup},$S{taskGroupName},$S{execTaskType},$S{preTaskGroup},current timestamp,current timestamp,$S{lastTaskGroup},$S{runningType},$S{shouldExecTime},$S{taskModel})";
        Sql sql = Sql.build().oracleSql(sqlAll).db2Sql(sqlDb2).mysqlSql(sqlAll);
        return super.update(sql, params.getModel()).getEffect();
    }

}
