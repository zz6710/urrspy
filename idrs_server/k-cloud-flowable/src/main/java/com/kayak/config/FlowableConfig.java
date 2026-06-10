package com.kayak.config;

import com.kayak.converter.StartEventXMLConverter;
import com.kayak.function.GetFormDataExpressionFunction;
import com.kayak.function.StringLengthExpressionFunction;
import com.kayak.function.WfParamExpressionFunction;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.common.engine.api.delegate.FlowableFunctionDelegate;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanjinqiao
 * @date 2021/4/5 01:32
 */
@Configuration
@Slf4j
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
        engineConfiguration.setActivityFontName("宋体");
        engineConfiguration.setLabelFontName("宋体");
        engineConfiguration.setAnnotationFontName("宋体");
        BpmnXMLConverter.addConverter(new StartEventXMLConverter());

        List<FlowableFunctionDelegate> customFlowableFunctionDelegates = engineConfiguration.getCustomFlowableFunctionDelegates();
        // 增加至流程配置中
        if (null == customFlowableFunctionDelegates) {
            customFlowableFunctionDelegates = new ArrayList<>();
        }
        customFlowableFunctionDelegates.add(new WfParamExpressionFunction());
        customFlowableFunctionDelegates.add(new StringLengthExpressionFunction());
        customFlowableFunctionDelegates.add(new GetFormDataExpressionFunction());
        engineConfiguration.setCustomFlowableFunctionDelegates(customFlowableFunctionDelegates);
    }
}

