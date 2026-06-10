package com.kayak.function;

import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.variable.VariableContainer;
import org.flowable.common.engine.impl.el.VariableContainerWrapper;
import org.flowable.common.engine.impl.el.function.AbstractFlowableVariableExpressionFunction;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuanjinqiao
 * @description 自定义解析流程参数表达式
 * @create 2022-10-12 09:22
 **/
@Slf4j
@Component
public class GetFormDataExpressionFunction extends AbstractFlowableVariableExpressionFunction {

    final static String FUNCTION_NAME = "json";

    public GetFormDataExpressionFunction() {
        super(FUNCTION_NAME);
    }

    /**
     * 获取表单数据，可以使用user.username获取深层次的数据
     *
     * @param key
     * @return
     */
    public static Object json(VariableContainer variableContainer, String key) throws Exception {
        Map<String, Object> variables = new HashMap<>();
        if (variableContainer instanceof VariableContainerWrapper) {
            //流程预测
            VariableContainerWrapper variableContainerWrapper = (VariableContainerWrapper) variableContainer;
            Class<? extends VariableContainerWrapper> aClass1 = variableContainerWrapper.getClass();
            Field field = aClass1.getDeclaredField("variables");
            field.setAccessible(true);
            variables = (Map<String, Object>) field.get(variableContainerWrapper);
        } else if (variableContainer instanceof ExecutionEntityImpl) {
            //流程中
            ExecutionEntityImpl executionEntityImpl = (ExecutionEntityImpl) variableContainer;
            variables = executionEntityImpl.getVariables();
        }
        Object value = StringUtils.getJsonValue(variables, key);
        if (value == null) {
            throw new WorkflowException("[" + key + "】对应的值不存在");
        }
        return value;
    }

}


