package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.TaskActor;
import org.springframework.stereotype.Repository;

/**
 * Created by daniel on 30/03/2017.
 */
@Repository("taskActorDao")
public class TaskActorDao extends ComnDao {

    public void saveTaskActor(TaskActor taskActor) throws Exception {
        String sql = "INSERT INTO opf_task_actor(id, actor_id,task_id,actor_type)" +
                " VALUES($S{id},$S{actorId},$S{taskId},$S{actorType})";
        super.update(sql, taskActor);
    }

    public void removeTaskActorByTaskId(String taskId) throws Exception {
        String sql = "DELETE FROM opf_task_actor WHERE task_id='" + taskId + "'";
        super.update(sql);
    }

}
