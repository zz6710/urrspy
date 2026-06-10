package com.kayak.web.workflow.service;

import com.kayak.web.workflow.domain.bo.WfTaskBo;
import com.kayak.web.workflow.domain.vo.CalculatTaskVo;
import com.kayak.web.workflow.domain.vo.WfViewerVo;
import org.flowable.bpmn.model.FlowElement;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author yuanjinqiao
 * @description
 * @create 2023-03-07 16:36
 **/
public interface ICalculateProcessService {

    List getCalculateTask(WfTaskBo procInsId);

    List<FlowElement> sortCalculateList(List<FlowElement> taskList);

    String getFlowView(WfTaskBo bo);

    WfViewerVo getFlowViewRun(WfTaskBo bo);

    CalculatTaskVo getCalculatTaskVo(Map<String, Object> variables, String procInsId, FlowElement flowElement);

    List<FlowElement> calApprovePath(String procDefId, String procInsId, Map<String, Object> variableMap);

    void getPassElementList(List<FlowElement> passElements, Collection<FlowElement> flowElements, FlowElement curFlowElement, Map<String, Object> variableMap, String procInsId);

    CalculatTaskVo getCalculateCopy(WfTaskBo bo);
}
