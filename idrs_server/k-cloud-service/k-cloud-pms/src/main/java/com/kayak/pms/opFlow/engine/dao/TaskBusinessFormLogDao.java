package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.TaskPassBusinessFormLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("taskBusinessFormLogDao")
public class TaskBusinessFormLogDao extends ComnDao {


    /**
     * 添加任务审核数据修改日志
     * @param log
     */
    public void add(TaskPassBusinessFormLog log) {}


    /**
     * 列表查询
     * @param model
     * @return
     */
    public List<TaskPassBusinessFormLog> list(TaskPassBusinessFormLog model) {
        return null;
    }


}
