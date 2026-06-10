package com.kayak.pms.opFlow.engine.service;

import com.kayak.pms.opFlow.engine.dao.TaskBusinessFormLogDao;
import com.kayak.pms.opFlow.engine.entity.TaskPassBusinessFormLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModifyDataLogService {
    @Autowired
    private TaskBusinessFormLogDao taskBusinessFormLogDao;

    public List<TaskPassBusinessFormLog> list(TaskPassBusinessFormLog log) {
        return taskBusinessFormLogDao.list(log);
    }
}
