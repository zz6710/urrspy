package com.kayak.listener.flow;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.factory.SystemServiceFactory;
import com.kayak.utils.flow.FlowableUtils;
import com.kayak.web.workflow.service.IWfCopyService;
import com.kayak.web.workflow.service.IWfParamService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : yuanjinqiao
 * @title: : GlobalTaskListener
 * @description: 任务创建全局监听
 * @date : 2022/10/11
 */
@Slf4j
@Component
public class GlobalTaskCompletedListener extends AbstractFlowableEngineEventListener {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IWfParamService wfParamService;
    @Autowired
    private SystemServiceFactory systemServiceFactory;
    @Autowired
    private IWfCopyService copyService;

    @Override
    protected void taskCompleted(FlowableEngineEntityEvent event) {
        log.info("进入全局任务创建监听器:{}", "taskCompleted");
        BpmnModel bpmnModel = repositoryService.getBpmnModel(event.getProcessDefinitionId());
        FlowableEntityEventImpl flowableEntityEvent = (FlowableEntityEventImpl) event;
        TaskEntityImpl taskEntity = (TaskEntityImpl) flowableEntityEvent.getEntity();
        String taskDefinitionKey = taskEntity.getTaskDefinitionKey();
        FlowElement flowElement = FlowableUtils.findFlowElementByActivityId(bpmnModel, taskDefinitionKey);
        if (ObjectUtil.isNotEmpty(flowElement) && flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            //执行抄送
            if (!userTask.hasMultiInstanceLoopCharacteristics()) {
                //会签时同一个任务节点会执行多次，所以不是会签时才执行抄送。会签的抄送在GlobalMultiInstanceActivityCompletedWithConditionListener中
                //会签时，拒绝时调用的也是complete方法。
                copyService.makeCopy(taskEntity.getProcessInstanceId(), userTask);
            }
        }
        log.info("完成全局任务创建监听器:{}", "taskCompleted");
    }

}
