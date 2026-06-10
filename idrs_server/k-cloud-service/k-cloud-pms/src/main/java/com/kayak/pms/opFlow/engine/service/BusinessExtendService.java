package com.kayak.pms.opFlow.engine.service;

import com.alibaba.fastjson.JSONArray;
import com.kayak.core.spring.SpringContextHolder;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.busi.ExtraData;
import com.kayak.pms.opFlow.engine.busi.SaveTaskUpdateBusinessLogService;
import com.kayak.pms.opFlow.engine.dao.BusinessExtendDao;
import com.kayak.pms.opFlow.engine.dao.CommonDao;
import com.kayak.pms.opFlow.engine.dao.EnvItemDao;
import com.kayak.pms.opFlow.engine.entity.Approval;
import com.kayak.pms.opFlow.engine.entity.ModifiedData;
import com.kayak.pms.opFlow.engine.entity.Process;
import com.kayak.pms.opFlow.engine.model.ProcessModel;
import com.kayak.pms.opFlow.engine.model.TaskModel;
import com.kayak.pms.opFlow.engine.model.WfBusiExtend;
import com.kayak.pms.opFlow.engine.utils.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author libo
 */
@Service
public class BusinessExtendService {
    @Autowired
    BusinessExtendDao businessExtendDao;

    public void updateStatus(String processInstanceId, String status) {
        Date now = new Date();
        businessExtendDao.updateByProcessInstanceId(
                WfBusiExtend.builder()
                        .processInstanceId(processInstanceId)
                        .processStatus(status)
                        .updateDate(Tools.dt2Date1(now))
                        .updateTime(Tools.dt2Time1(now)).build());
    }

    public void deleteByProcessInstanceId(String processInstanceId) {
        businessExtendDao.deleteByProcessInstanceId(processInstanceId);
    }

    public void updateBusinessData(Approval approval) throws Exception {
        final ProcessService processService = SpringContextHolder.getBean("processService");
        Process process = processService.getProcessById(approval.getProcessId());
        ProcessModel processModel = process.getProcessModel();
        TaskModel taskModel = (TaskModel) processModel.getNode(approval.getTaskName());
        String evnItemKeyStr = taskModel.getUpdateEnvTask();

        if (StringHelper.isNotEmpty(evnItemKeyStr)) {
            CommonDao commonDao = SpringContextHolder.getBean("commonDao");
            EnvItemDao envItemDao = SpringContextHolder.getBean("envItemDao");
            // 当前用户和当前表主键
            Map<String, Object> params = new HashMap<>(SysUtil.getUserInfo());
            if (StringHelper.isNotBlank(approval.getExtraData())) {
                List<ExtraData> extraData = JSONArray.parseArray(approval.getExtraData(), ExtraData.class);

                params.putAll(extraData.stream().collect(Collectors.toMap(ExtraData::getName, ExtraData::getValue)));

                params.put("process_instance_id", approval.getProcessInstanceId());
                params.put("current_date", Tools.getCurrentDate());
                String[] envItemKeys = evnItemKeyStr.split(",");
                for (String envItemKey : envItemKeys) {
                    String sql = envItemDao.getSqlByItemKey(approval.getProcessId(), envItemKey);
                    sql = RegexUtil.getSql(sql, params);
                    commonDao.updateBySql(sql);
                }
                SaveTaskUpdateBusinessLogService saveTaskUpdateBusinessLogService = new SaveTaskUpdateBusinessLogService();
                saveTaskUpdateBusinessLogService.saveLog(approval,
                        () -> extraData.stream()
                                .map(o -> new SaveTaskUpdateBusinessLogService.UpdateDataElement(
                                        o.getName(), o.getValue(), o.getDisplayName()
                                ))
                                .collect(Collectors.toList()));
            }

        }

    }

    public void saveFormDataUpdateLog(Approval approval, List<ModifiedData> modifiedDatas) throws Exception {
        List<ExtraData> extraData = modifiedDatas.stream()
                .map(o -> ExtraData.builder().build())
                .collect(Collectors.toList());
        SaveTaskUpdateBusinessLogService saveTaskUpdateBusinessLogService = new SaveTaskUpdateBusinessLogService();
        saveTaskUpdateBusinessLogService.saveLog(approval,
                () -> extraData.stream()
                        .map(o -> new SaveTaskUpdateBusinessLogService.UpdateDataElement(
                                o.getName(), o.getValue(), o.getDisplayName()
                        ))
                        .collect(Collectors.toList()));
    }
}
