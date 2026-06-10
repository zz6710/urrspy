package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 14/06/2017.
 */
@Repository
public class AuthorityDao extends ComnDao {
    public List<String> listTaskIdsByCurrentUser(Map<String, String> queryCriteria) throws Exception {
        String sql = "SELECT DISTINCT task_id FROM opf_task_actor WHERE" +
                " (actor_type=$S{ordinaryType} AND actor_id=$S{ordinaryId})" +
                " OR (actor_type=$S{specialType} AND actor_id=$S{specialId})" +
                " OR (actor_type=$S{roleType} AND FIND_IN_SET(ACTOR_ID,$S{roleIds}))" +
                " OR (actor_type=$S{specialRoleType} AND FIND_IN_SET(ACTOR_ID,$S{specialRoles}))";
        List<String> rows = super.findRows(String.class, sql, 0, queryCriteria);
        // 查询未配置任何权限的任务
        sql = "SELECT id FROM opf_task WHERE id not in (SELECT DISTINCT task_id FROM opf_task_actor)";
        List<String> rows1 = super.findRows(String.class, sql, 0, null);
        rows.addAll(rows1);
        return rows;
    }

    public List<String> listTaskIdsBySurrogateUser(Map<String, Object> queryCriteria) {
        return null;
    }

    public List<String> listProcessInstanceIdsByCurrentUser(Map<String, Object> queryCriteria) {
        return null;
    }

    public List<String> getRoleIdsByUser(String userid) throws Exception {
        return super.findRows(String.class,"SELECT roleid FROM sys_user_role WHERE userid = '" + userid + "'",0, userid);
    }
}
