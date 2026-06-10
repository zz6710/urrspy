package com.kayak.web.workflow.service;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.delegate.DelegateExecution;

import java.util.Map;

/**
 * 流程参数Service接口
 *
 * @author yuanjinqiao
 * @date 2022-10-11
 */
public interface IWfParamService {

    Object parseWfParam(String envItemId, Map<String, Object> variables,
                        FlowElement currentFlowElement, String processInstanceId,
                        Map<String, String> fieldExtensionMap);

    Object parseWfParam(String envItemId, DelegateExecution execution);

    Object parseWfParam(String envItemId, Map<String, Object> map, String processInstanceId);
}
