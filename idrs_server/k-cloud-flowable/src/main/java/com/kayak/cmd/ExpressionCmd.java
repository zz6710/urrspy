package com.kayak.cmd;

import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.common.engine.impl.el.VariableContainerWrapper;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.persistence.entity.HistoricProcessInstanceEntity;

import java.io.Serializable;
import java.util.Map;

public class ExpressionCmd implements Command<Boolean>, Serializable {
    protected RuntimeService runtimeService;

    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    protected String processInstanceId;

    protected String exp;

    protected Map<String, Object> variableMap;

    public ExpressionCmd(ProcessEngineConfigurationImpl processEngineConfiguration, String processInstanceId, String exp, Map<String, Object> variableMap) {
        this.processEngineConfiguration = processEngineConfiguration;
        this.processInstanceId = processInstanceId;
        this.exp = exp;
        this.variableMap = variableMap;
    }

    @Override
    public Boolean execute(CommandContext commandContext) {
        if (StringUtils.isEmpty(this.exp)) {
            //在flowable中，表达式为空，则认为时true
            return true;
        }
        Expression expression = processEngineConfiguration.getExpressionManager().createExpression(this.exp);
        VariableContainerWrapper variableContainerWrapper = new VariableContainerWrapper(variableMap);
        Object value = expression.getValue(variableContainerWrapper);
        return value != null && "true".equals(value.toString());
    }
}