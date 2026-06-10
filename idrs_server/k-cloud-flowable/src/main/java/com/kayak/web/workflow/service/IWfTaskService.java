package com.kayak.web.workflow.service;

import com.kayak.web.workflow.domain.bo.WfTaskBo;
import com.kayak.web.workflow.domain.vo.WfTaskVo;
import com.kayak.web.workflow.domain.vo.WfViewerVo;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author yuanjinqiao
 * @createTime 2022/3/10 00:12
 */
public interface IWfTaskService {

    /**
     * 审批任务
     *
     * @param task 请求实体参数
     */
    void complete(WfTaskBo task) throws Exception;

    /**
     * 驳回任务
     *
     * @param bo
     */
    void taskReturn(WfTaskBo bo);

    /**
     * 退回任务
     *
     * @param bo 请求实体参数
     */
    void taskReject(WfTaskBo bo);

    /**
     * 获取所有可驳回的节点
     *
     * @param bo
     * @return
     */
    List<WfTaskVo> findRejectTaskList(WfTaskBo bo);

    /**
     * 认领/签收任务
     *
     * @param bo 请求实体参数
     */
    void claim(WfTaskBo bo);

    /**
     * 取消认领/签收任务
     *
     * @param bo 请求实体参数
     */
    void unClaim(WfTaskBo bo);

    /**
     * 委派任务
     *
     * @param bo 请求实体参数
     */
    void taskDelegate(WfTaskBo bo);

    /**
     * 转办任务
     *
     * @param bo 请求实体参数
     */
    void taskTransfer(WfTaskBo bo);

    /**
     * 撤回申请
     *
     * @param bo
     * @return
     */
    void revokeProcess(WfTaskBo bo);

    /**
     * 获取流程过程图
     *
     * @param processId
     * @return
     */
    InputStream diagram(String processId);

    /**
     * 获取流程执行过程
     *
     * @param procInsId
     * @return
     */
    WfViewerVo getFlowViewer(String procInsId);

    /**
     * 拒绝任务
     *
     * @param bo
     */
    void taskRefuse(WfTaskBo bo);

    /**
     * 拒绝删除結束流程
     *
     * @param comment           刪除流程的原因
     * @param processInstanceId 流程实例Ida
     */
    void refuseProcessInstance(String comment, String processInstanceId);

    /**
     * 获取任务定义中配置的按钮
     *
     * @param procDefId
     * @param taskDefKey
     * @return
     */
    List<String> getBtns(String procDefId, String taskDefKey);

    /**
     * 触发接收任务
     *
     * @param procInsId
     * @param receiveTaskId
     */
    void triggerReceiveTask(String procInsId, String receiveTaskDefKey);
}
