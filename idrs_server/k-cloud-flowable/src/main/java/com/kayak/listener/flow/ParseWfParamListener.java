package com.kayak.listener.flow;

import cn.hutool.core.util.StrUtil;
import com.kayak.utils.StringUtils;
import com.kayak.utils.flow.FlowableUtils;
import com.kayak.web.workflow.service.IWfParamService;
import org.flowable.bpmn.model.*;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.DelegateHelper;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : yuanjinqiao
 * @description: 用于解析流程参数的监听器
 * @date : 2022/10/11
 */
@Component
public class ParseWfParamListener {
    @Autowired
    private IWfParamService wfParamService;
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 解析任务监听器流程参数
     *
     * @param paramName
     * @param delegateTask
     */
    public void parseTaskParam(String paramName, DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        Map<String, Object> variables = delegateTask.getVariables();
        FlowElement flowElement = FlowableUtils.findFlowElementByActivityId(delegateTask.getProcessDefinitionId(), delegateTask.getTaskDefinitionKey(), repositoryService);
        HashMap<String, String> fieldExtensionMap = new HashMap<>();
        //获取注入字段
        if (flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            List<FlowableListener> taskListeners = userTask.getTaskListeners();
            for (FlowableListener taskListener : taskListeners) {
                if (eventName.equals(taskListener.getEvent())
                        && taskListener.getImplementationType().equals(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION)
                        && taskListener.getImplementation().contains(StrUtil.lowerFirst(ParseWfParamListener.class.getSimpleName()))
                ) {
                    List<FieldExtension> fieldExtensions = taskListener.getFieldExtensions();
                    for (FieldExtension fieldExtension : fieldExtensions) {
                        String fieldName = fieldExtension.getFieldName();
                        String expression = fieldExtension.getExpression();
                        String stringValue = fieldExtension.getStringValue();
                        fieldExtensionMap.put(fieldName, StringUtils.isNotEmpty(expression) ? expression : stringValue);
                    }
                }
            }
            wfParamService.parseWfParam(paramName, variables, flowElement, delegateTask.getProcessInstanceId(), fieldExtensionMap);
        }
    }

    /**
     * 解析执行监听器流程参数
     *
     * @param paramName
     * @param execution
     */
    public void parseExecutionParam(String paramName, DelegateExecution execution) {
        HashMap<String, String> fieldExtensionMap = new HashMap<>();
        List<FieldExtension> fieldExtensions = DelegateHelper.getFields(execution);
        for (FieldExtension fieldExtension : fieldExtensions) {
            String fieldName = fieldExtension.getFieldName();
            String expression = fieldExtension.getExpression();
            String stringValue = fieldExtension.getStringValue();
            fieldExtensionMap.put(fieldName, StringUtils.isNotEmpty(expression) ? expression : stringValue);
        }
        wfParamService.parseWfParam(paramName, execution.getVariables(), execution.getCurrentFlowElement(), execution.getProcessInstanceId(), fieldExtensionMap);
    }
}
