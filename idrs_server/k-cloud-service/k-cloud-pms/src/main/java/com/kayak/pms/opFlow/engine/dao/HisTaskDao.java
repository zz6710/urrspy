package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.HisCompleteTask;
import com.kayak.pms.opFlow.engine.entity.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 30/03/2017.
 */
@Repository
public class HisTaskDao extends ComnDao {

    public void saveHisTask(Task hisTask) throws Exception {
        String sql = "INSERT INTO opf_his_task(id, process_instance_id, name,display_name,perform_type, create_date, create_time, finish_date, finish_time, parent_task_id, submit_user, process_id, busi_id,task_type)" +
                " VALUES ($S{id}, $S{processInstanceId}, $S{name}, $S{displayName}, $S{performType}, $S{createDate} , $S{createTime}, $S{finishDate}, $S{finishTime}, $S{parentTaskId}, $S{submitUser}, $S{processId}, $S{busiId},$S{taskType})";
        super.update(sql, hisTask);
    }

    public Task getHisTaskById(String hisTaskId) throws Exception {
        String sql = "SELECT id, process_id, process_instance_id, name, display_name, perform_type, create_date, create_time, finish_date, finish_time, parent_task_id, submit_user, task_type, busi_id FROM opf_his_task WHERE id='" + hisTaskId + "'";
        return super.findRow(Task.class, sql, 0, null);
    }

    public Task getLastHisTaskByInstance(String processInstanceId) throws Exception {
        String sql = "SELECT id, id old_id, process_id, process_instance_id, name, display_name, perform_type, create_date, create_time, finish_date, finish_time, parent_task_id, submit_user, task_type, busi_id FROM opf_his_task WHERE process_instance_id='" + processInstanceId + "'";
        return super.findRow(Task.class, sql, 0, null);
    }

    public List<HisCompleteTask> listHisCompleteTasks(Map<String, Object> queryCriteria) {
        return null;
    }
}
