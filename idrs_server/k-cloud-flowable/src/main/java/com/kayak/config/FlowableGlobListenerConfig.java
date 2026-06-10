package com.kayak.config;

import com.kayak.listener.flow.GlobalMultiInstanceActivityCompletedWithConditionListener;
import com.kayak.listener.flow.GlobalProcessCompletedListener;
import com.kayak.listener.flow.GlobalTaskCompletedListener;
import com.kayak.listener.flow.GlobalTaskCreatedListener;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventDispatcher;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author : yuanjinqiao
 * @title: : FlowableGlobListenerConfig
 * @projectName : flowable
 * @description: 全局监听配置 ContextRefreshedEvent在类被初始化之后触发
 * @date : 2021/05/11
 */
@Configuration
public class FlowableGlobListenerConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private SpringProcessEngineConfiguration configuration;
    @Autowired
    private GlobalTaskCreatedListener globalTaskCreatedListener;
    @Autowired
    private GlobalTaskCompletedListener globalTaskCompletedListener;
    @Autowired
    private GlobalProcessCompletedListener globalProcessCompletedListener;

    @Autowired
    private GlobalMultiInstanceActivityCompletedWithConditionListener globalMultiInstanceActivityCompletedWithConditionListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        FlowableEventDispatcher dispatcher = configuration.getEventDispatcher();
        dispatcher.addEventListener(globalTaskCreatedListener, FlowableEngineEventType.TASK_CREATED);
        dispatcher.addEventListener(globalTaskCompletedListener, FlowableEngineEventType.TASK_COMPLETED);
        dispatcher.addEventListener(globalProcessCompletedListener, FlowableEngineEventType.PROCESS_COMPLETED);
        dispatcher.addEventListener(globalMultiInstanceActivityCompletedWithConditionListener, FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED_WITH_CONDITION);
    }
}
