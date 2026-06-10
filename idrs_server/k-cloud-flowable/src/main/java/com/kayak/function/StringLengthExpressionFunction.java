package com.kayak.function;

import com.kayak.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.el.function.AbstractFlowableVariableExpressionFunction;
import org.springframework.stereotype.Component;

/**
 * @author yuanjinqiao
 * @description 自定义解析流程参数表达式
 * @create 2022-10-12 09:22
 **/
@Slf4j
@Component
public class StringLengthExpressionFunction extends AbstractFlowableVariableExpressionFunction {

    final static String FUNCTION_NAME = "length";

    public StringLengthExpressionFunction() {
        super(FUNCTION_NAME);
    }

    /**
     * 获取字符串长度
     *
     * @param value
     * @return
     */
    public static Integer length(String value) {
        return StringUtils.length(value);
    }

}


