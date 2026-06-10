package com.kayak.web.workflow.service;

import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.entity.result.R;
import com.kayak.web.workflow.domain.bo.WfTaskQueryBo;
import com.kayak.web.workflow.domain.vo.WfFormConfVO;
import com.kayak.web.workflow.domain.vo.WfTaskVo;

import java.util.List;
import java.util.Map;

/**
 * @author yuanjinqiao
 * @createTime 2022/3/24 18:57
 */
public interface IWfProcessService {

    /**
     * 启动流程实例
     *
     * @param variables 扩展参数
     * @return
     */
    R<String> startProcess(Map<String, Object> variables);

    /**
     * 启动第一个任务
     *
     * @param processInstanceId 流程实例id
     * @param variables         流程参数
     */
    void startFirstTask(String processInstanceId, Map<String, Object> variables);

    /**
     * 删除流程实例
     *
     * @param procInsId    流程实例ID
     * @param deleteReason 删除原因
     */
    void deleteProcessInstance(String procInsId, String deleteReason);

    /**
     * 查询我的流程列表
     *
     * @param bo
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> queryPageOwnProcessList(WfTaskQueryBo bo, PageQuery pageQuery);

    /**
     * 查询代办任务列表
     *
     * @param bo
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> queryPageTodoProcessList(WfTaskQueryBo bo, PageQuery pageQuery);

    /**
     * 查询已办任务列表
     *
     * @param bo
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> queryPageFinishedProcessList(WfTaskQueryBo bo, PageQuery pageQuery);

    /**
     * 获取历史任务详情
     *
     * @param procInsId
     * @return
     */
    List<WfTaskVo> historyTaskList(String procInsId);

    /**
     * 表单配置
     *
     * @param procDefId
     * @param taskDefKey
     * @return
     */
    WfFormConfVO getFormConf(String procDefId, String taskDefKey);

    /**
     * 审批表单数据
     *
     * @param procInsId
     * @return
     */
    Map<String, Object> getFormData(String procInsId);

    /**
     * 表单字段显示json
     *
     * @param procInsId
     * @return
     */
    Map<String, Object> getFormLabelInfo(String procInsId);

    Map<String, Object> getProcessVariables(String processInstanceId);
}
