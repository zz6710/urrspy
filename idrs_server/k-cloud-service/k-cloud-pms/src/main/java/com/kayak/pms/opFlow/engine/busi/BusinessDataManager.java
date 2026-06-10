package com.kayak.pms.opFlow.engine.busi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kayak.pms.opFlow.engine.dao.ProcessInstanceDao;
import com.kayak.pms.opFlow.engine.dao.TaskBusinessFormLogDao;
import com.kayak.pms.opFlow.engine.entity.ProcessInstance;
import com.kayak.pms.opFlow.engine.entity.TaskPassBusinessFormLog;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.annotation.ManagedBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BusinessDataManager {

    private final TaskBusinessFormLogDao taskBusinessFormLogDao;
    private final ProcessInstanceDao processInstanceDao;


    public Map<String, Object> getLatestData(String processInstanceId) throws Exception {
        // 获取初始参数
        Map<String, Object> initialSubmissionData = getInitialBusinessData(processInstanceId);
        // 获取历史修改日志
        List<TaskPassBusinessFormLog> logs = taskBusinessFormLogDao.list(TaskPassBusinessFormLog.builder()
                .processInstanceId(processInstanceId).build());
        // 还原为最新业务数据
        return restoreLatestData(initialSubmissionData, logs);
    }

    private Map<String, Object> getInitialBusinessData(String processInstanceId) throws Exception {
        ProcessInstance processInstance = processInstanceDao.getProcessInstanceById(processInstanceId);
        return (Map<String, Object>) JSON.parse(processInstance.getOriginalData());
    }

    private Map<String, Object> restoreLatestData(Map<String, Object> initialSubmissionData, List<TaskPassBusinessFormLog> logs) {
        if (CollectionUtils.isEmpty(logs)) {
            return initialSubmissionData;
        }

        Map<String, Object> latestData = new HashMap<>(initialSubmissionData);
        for (TaskPassBusinessFormLog log : logs) {
            JSONArray updateData = JSON.parseArray(log.getData());
            for (Object updateDatum : updateData) {
                JSONObject o = (JSONObject) updateDatum;
                latestData.put(o.getString("name"), o.getString("after"));
            }
        }
        return latestData;
    }
}
