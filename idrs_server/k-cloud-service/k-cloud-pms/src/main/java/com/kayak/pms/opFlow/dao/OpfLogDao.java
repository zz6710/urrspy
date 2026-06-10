package com.kayak.pms.opFlow.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.opFlow.model.OpfLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OpfLogDao extends ComnDao {
    public void addLog(OpfLog opfLog) throws Exception {
        String sql = "INSERT INTO opf_log (id, operator, operation_type, create_date, process_instance_id, children_process_instance_id, task_id, busi_id, remark, display_name, form_data_id, next_node)" +
                " VALUES($S{id}, $S{operator}, $S{operationType}, $S{createDate}, $S{processInstanceId}, $S{childrenProcessInstanceId}, $S{taskId}, $S{busiId}, $S{remark}, $S{displayName}, $S{formDataId}, $S{nextNode})";
        super.update(sql, opfLog);
    }

    public List<OpfLog> findLogs(OpfLog opfLog) throws Exception {
        String sql = "SELECT t1.id, t2.username operator, t3.deptname, t1.operation_type, t1.create_date, t1.process_instance_id, t1.children_process_instance_id, t1.task_id, t1.busi_id, t1.remark, t1.display_name, t1.form_data_id, t1.next_node" +
                " FROM opf_log t1" +
                " LEFT JOIN sys_user t2 ON t1.operator=t2.userid" +
                " LEFT JOIN sys_dept t3 ON t2.deptno=t3.deptno" +
                " WHERE t1.process_instance_id=$S{processInstanceId}" +
                " ORDER BY t1.create_date";
        return super.findRows(OpfLog.class, sql, 0, opfLog);
    }

    //根据用户id获取用户名
    public SqlRow findUsernameByUserid(String userid) throws Exception {
        return super.findRow("SELECT username FROM sys_user WHERE userid = '" + userid + "'", userid);
    }
}
