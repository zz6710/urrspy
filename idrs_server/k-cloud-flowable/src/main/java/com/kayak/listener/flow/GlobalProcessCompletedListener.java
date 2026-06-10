package com.kayak.listener.flow;

import com.kayak.common.enums.ProcessInstanceStatusEnum;
import com.kayak.scheduled.WorkFlowCallbackService;
import com.kayak.web.workflow.mapper.WfBusiInfoMapper;
import com.kayak.web.workflow.service.IWfBusiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yuanjinqiao
 * @description 全局的流程监听器
 * @create 2022-08-26 09:40
 **/
@Component
@Slf4j
public class GlobalProcessCompletedListener extends AbstractFlowableEngineEventListener {
    @Autowired
    private WfBusiInfoMapper wfBusiInfoMapper;
    @Autowired
    private WorkFlowCallbackService workFlowCallbackService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IWfBusiInfoService busiInfoService;

    @Override
    protected void processCompleted(FlowableEngineEntityEvent event) {
        log.info("进入全局流程完成监听器:{}", "processCompleted");
        String processInstanceId = event.getProcessInstanceId();
        //修改流程状态
        busiInfoService.updateProcStatus(processInstanceId, ProcessInstanceStatusEnum.FINISH.getType());
        runtimeService.updateBusinessStatus(processInstanceId, ProcessInstanceStatusEnum.FINISH.getType());
        //执行异步业务回调
        workFlowCallbackService.execute(processInstanceId);
    }
}
