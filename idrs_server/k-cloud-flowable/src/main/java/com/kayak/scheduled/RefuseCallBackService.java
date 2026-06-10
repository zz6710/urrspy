package com.kayak.scheduled;

import com.kayak.common.constant.FieldConstants;
import com.kayak.common.enums.ProcessInstanceStatusEnum;
import com.kayak.utils.StringUtils;
import com.kayak.web.workflow.service.IWfParamService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RefuseCallBackService {
    @Autowired
    private IWfParamService wfParamService;

    /**
     * 拒绝回调
     *
     * @param processInstance
     * @param callbackParam
     */
    public void invoke(HistoricProcessInstance processInstance, String callbackParam) {
        if (StringUtils.isEmpty(callbackParam)) {
            return;
        }
        //构建返回结果
        Map<String, Object> retMap = new HashMap<>();
        retMap.putAll(processInstance.getProcessVariables());
        retMap.put(FieldConstants.PROCESS_KEY, processInstance.getProcessDefinitionKey());
        retMap.put(FieldConstants.PROCESS_INSTANCE_ID, processInstance.getId());
        retMap.put(FieldConstants.PROCESS_DEFINITION_ID, processInstance.getProcessDefinitionId());
        retMap.put(FieldConstants.PROCESS_STATUS, ProcessInstanceStatusEnum.REFUSE.getType());
        wfParamService.parseWfParam(callbackParam, retMap, processInstance.getId());

    }

}
