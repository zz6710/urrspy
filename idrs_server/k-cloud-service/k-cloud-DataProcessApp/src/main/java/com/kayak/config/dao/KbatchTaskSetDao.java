package com.kayak.config.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.config.constants.GlobalConstants;
import com.kayak.config.model.KbatchTaskSet;
import com.kayak.config.model.KbatchTaskSetList;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 文件名: TaClearTaskSetOperDao.java
 * 描述:   清算组任务配置操作DAO
 * 创建人: zengzt
 * 创建时间:2020年5月6日下午4:18:17
 */
@Repository
public class KbatchTaskSetDao extends ComnDao {

    /**
     * 方法描述:查询清算组任务配置
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<KbatchTaskSet> queryKbatchTaskSets(SqlParam<KbatchTaskSet> params) throws Exception {
        String sql = "SELECT se.task_group,se.task_id,se.moduleid,se.distributor_batch,se.exec_time,se.alarm_time,se.proc_start_time,"
                + " se.proc_end_time,se.pre_task_id,se.task_params,se.display_order,se.simple_flow,se.exec_order,info.task_name "
                + "	FROM kbatch_task_set se LEFT JOIN kbatch_task_info info ON se.task_id=info.task_id "
                + " WHERE se.task_group = $S{taskGroup} AND se.moduleid='" + GlobalConstants.MODULEID + "' ORDER BY se.display_order";
        return super.findRows(sql, params);
    }

    /**
     * 方法描述:插入清算组任务配置
     * @param params
     * @return
     * @throws Exception
     */
    public int insertKbatchTaskSet(KbatchTaskSet params) throws Exception {
        String sql = "INSERT INTO kbatch_task_set(task_group,task_id,moduleid,distributor_batch,exec_time,alarm_time,proc_start_time,proc_end_time,pre_task_id,task_params,display_order,simple_flow,exec_order)"
                + "VALUES($S{taskGroup},$S{taskId},$S{moduleid},$S{distributorBatch},$S{execTime},$S{alarmTime},$S{procStartTime},$S{procEndTime},$S{preTaskId},$S{taskParams},$D{displayOrder},$S{simpleFlow},$S{execOrder})";
        return super.update(sql, params).getEffect();
    }

    /**
     * 方法描述:删除所有清算组任务配置
     * @return
     * @throws Exception
     */
    public int deleteAllKbatchTaskSet(Map<String,Object> tparam) throws Exception {
        String sql = "delete from kbatch_task_set where task_group in(select task_group from kbatch_group_info where exec_task_type=$S{execTaskType})";
        return super.update(sql,tparam).getEffect();
    }

    /**
     * 方法描述：组清算任务
     * @param params
     * @throws Exception
     */
    public void addKbatchTaskSets(KbatchTaskSetList params) throws Exception {
        List<KbatchTaskSet> taskSetList = params.getTaskSetList();
        for (KbatchTaskSet taskSet : taskSetList) {
            insertKbatchTaskSet(taskSet);
        }
    }

}
