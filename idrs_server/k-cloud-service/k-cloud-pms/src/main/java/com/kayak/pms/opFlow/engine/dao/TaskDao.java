package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.entity.Task;
import com.kayak.pms.opFlow.engine.entity.vo.ReApplyProcess;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 30/03/2017.
 */
@Repository("taskDao")
public class TaskDao extends ComnDao {

    public void createTask(Task task) throws Exception {
        String sql = "INSERT INTO opf_task(id, process_instance_id, name, display_name, perform_type, create_date, create_time, parent_task_id, submit_user, process_id, task_type, busi_id)" +
                " VALUES ($S{id}, $S{processInstanceId}, $S{name}, $S{displayName}, $S{performType}, $S{createDate}, $S{createTime}, $S{parentTaskId}, $S{submitUser}, $S{processId}, $S{taskType}, $S{busiId})";
        super.update(sql, task);
    }

    /**
     * 根据查询条件，查询该用户可以审核的所有任务
     *
     * @param queryCriteria
     * @return
     */
    public SqlResult<Task> listActiveTasks(SqlParam<Task> param, Map<String, Object> queryCriteria) throws Exception {
        String sql = "SELECT wt.busi_id, wp.process_id, wp.version AS process_version, wp.display_name AS process_display_name, wp.name AS process_name, wt.id AS task_id, wt.name AS task_name, wt.task_type," +
                "wt.display_name AS task_display_name, wt.parent_task_id AS his_task_id, wt.create_date AS task_create_date, wt.create_time AS task_create_time," +
                "su.username AS apply_user, wpi.create_date AS process_instance_create_date, wpi.create_time AS process_instance_create_time, " +
                "wpi.process_instance_id,tpf.prod_code,tpi.prod_name" +
                " FROM opf_task wt" +
                " LEFT JOIN opf_process_instance wpi ON wpi.process_instance_id = wt.process_instance_id" +
                " LEFT JOIN opf_process wp ON wp.process_id = wt.process_id AND wp.version = wpi.process_version" +
                " LEFT JOIN sys_user su ON su.userid = wpi.creator" +
                " LEFT JOIN t8_prod_flow tpf ON tpf.op_process_id = wt.process_instance_id" +
                " LEFT JOIN t8_prod_info tpi ON tpf.prod_code=tpi.prod_code" +
                " WHERE   wt.id IN ('"+queryCriteria.get("taskIds")+"') ";//过滤发起任务节点 AND wt.id IN ('"+queryCriteria.get("taskIds")+"')
        //wt.PARENT_TASK_ID != 'start' AND
        if (StringHelper.isNotEmpty((String) queryCriteria.get("processName"))) {
            sql += " AND wp.name = '"+queryCriteria.get("processName")+"'";
        }
        if (StringHelper.isNotEmpty((String) queryCriteria.get("createStartDate"))) {
            sql += " AND wpi.create_date >= '"+queryCriteria.get("createStartDate")+"'";
        }
        if (StringHelper.isNotEmpty((String) queryCriteria.get("createEndDate"))) {
            sql += " AND wpi.create_date <= '"+queryCriteria.get("createEndDate")+"'";
        }
        if (StringHelper.isNotEmpty((String) queryCriteria.get("processNameLike"))) {
            sql += " AND wp.display_name like '%"+queryCriteria.get("processNameLike")+"%'";
        }
        if (StringHelper.isNotEmpty(param.getModel().getTaskType())){
            sql += " AND wt.task_type = '"+param.getModel().getTaskType()+"'";
        }
        sql += " ORDER BY wpi.create_date DESC, wpi.create_time DESC";
        return super.findRows(sql, param);
    }

    public Task getTaskById(String taskId) throws Exception {
        String sql = "SELECT id, process_id, process_instance_id, name, display_name, perform_type, create_date, create_time, finish_date, finish_time, parent_task_id, submit_user, busi_id, task_type" +
                " FROM opf_task WHERE id='" + taskId + "'";
        return super.findRow(Task.class, sql, 0, null);
    }

    public List<Task> getTasksByProcessInstanceId(String processInstanceId) throws Exception {
        String sql = "SELECT id, process_id, process_instance_id, name, display_name, perform_type, create_date, create_time, finish_date, finish_time, parent_task_id, submit_user, busi_id" +
                " FROM opf_task WHERE process_instance_id='" + processInstanceId + "'";
        return super.findRows(Task.class, sql, 0, null);
    }

    public void deleteTaskById(String taskId) throws Exception {
        String sql = "DELETE FROM opf_task WHERE id='" + taskId + "'";
        super.update(sql);
    }

    public Task getApproveTask(String processInstanceId, String currentNode) throws Exception {
        String sql = "SELECT id,process_id,process_instance_id,name,display_name,perform_type,create_date,create_time,finish_date,finish_time,parent_task_id,submit_user,task_type,busi_id FROM opf_task WHERE process_instance_id='" + processInstanceId + "' AND name='"+currentNode+"' AND task_type='1'";
        return super.findRow(Task.class, sql, 0, null);
    }

    public List<Task> listActiveTasksByProcessInstanceId(String processInstanceId) throws Exception {
        return null;
    }

    public List<Task> listActiveTasksByParentProcessInstanceId(String parentProcessInstanceId) throws Exception {
        String sql = "SELECT id,process_id,process_instance_id,name,display_name,perform_type,create_date,create_time,finish_date,finish_time,parent_task_id,submit_user,task_type,busi_id FROM opf_task WHERE process_instance_id in (" +
                " SELECT process_instance_id FROM opf_process_instance WHERE parent_process_id = '" + parentProcessInstanceId + "')";
        return super.findRows(Task.class, sql, 0, null);
    }

    public List<Task>  listActiveTasksByParentId(String taskId) {
        return null;
    }

    public List<Task> getLatestTask(String processInstanceId) {
        return null;
    }

    public void deleteTaskByProcessInstanceId(String processInstanceId) {}

    public List<ReApplyProcess> listReApply(SqlParam<ReApplyProcess> param) {
        return null;
    }

    public List<String> getApprovalUserNameByTaskId(@Param("taskId") String taskId) {
        return null;
    }

    public List<String> getApprovalRoleNameByTaskId(@Param("taskId") String taskId) {
        return null;
    }
}
