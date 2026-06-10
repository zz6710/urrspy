package com.kayak.listener.flow;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.common.constant.TaskConstants;
import com.kayak.utils.flow.FlowableUtils;
import com.kayak.web.workflow.service.IWfCopyService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.FlowableMultiInstanceActivityCompletedEvent;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableMultiInstanceActivityCompletedEventImpl;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author yuanjinqiao
 * @description 全局多实例监听器
 * @create 2022-10-14 14:44
 **/
@Component
@Slf4j
public class GlobalMultiInstanceActivityCompletedWithConditionListener extends AbstractFlowableEngineEventListener {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IWfCopyService copyService;

    @Override
    protected void multiInstanceActivityCompletedWithCondition(FlowableMultiInstanceActivityCompletedEvent event) {
        log.info("进入全局多实例监听器完成:{}", "multiInstanceActivityCompletedWithCondition");
        BpmnModel bpmnModel = repositoryService.getBpmnModel(event.getProcessDefinitionId());
        FlowElement flowElement = FlowableUtils.findFlowElementByActivityId(bpmnModel, event.getActivityId());
        if (ObjectUtil.isNotEmpty(flowElement) && flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            //执行抄送,会签的抄送要在会签执行完成后。
            copyService.makeCopy(event.getProcessInstanceId(), userTask);
        }
        //完成会签后，重置会签数据
        runtimeService.removeVariables(event.getExecutionId(), Arrays.asList(TaskConstants.MULTI_PASS_COUNT, TaskConstants.MULTI_REFUSE_COUNT));
    }

}
