package com.kayak.web.workflow.service;

import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.bo.ProcessConfigBo;
import com.kayak.web.workflow.domain.bo.WfModelBo;
import com.kayak.web.workflow.domain.bo.WfRejectTaskBo;
import com.kayak.web.workflow.domain.vo.WfModelVo;
import com.kayak.web.workflow.domain.vo.WfTaskVo;
import org.flowable.bpmn.model.UserTask;

import java.util.Collection;
import java.util.List;

/**
 * @author yuanjinqiao
 * @createTime 2022/6/21 9:11
 */
public interface IWfModelService {

    /**
     * 查询流程模型列表
     */
    TableDataInfo<WfModelVo> list(WfModelBo modelBo, PageQuery pageQuery);

    /**
     * 查询流程模型列表
     */
    TableDataInfo<WfModelVo> historyList(WfModelBo modelBo, PageQuery pageQuery);

    /**
     * 查询流程模型详情信息
     */
    WfModelVo getModel(String modelId);

    /**
     * 查询流程表单详细信息
     */
    String queryBpmnXmlById(String modelId);

    /**
     * 新增模型信息
     *
     * @return
     */
    WfModelBo insertModel(WfModelBo modelBo);

    /**
     * 修改模型信息
     */
    void updateModel(WfModelBo modelBo);

    /**
     * 保存流程模型信息
     */
    void saveModel(WfModelBo modelBo);

    /**
     * 设为最新流程模型
     */
    void latestModel(String modelId);

    /**
     * 删除流程模型
     */
    void deleteByIds(Collection<String> ids);

    /**
     * 部署流程模型
     */
    boolean deployModel(String modelId);

    /**
     * 可驳回节点
     * @param bo
     * @return
     */
    List<WfTaskVo> getRejectTaskList(WfRejectTaskBo bo);


    List<WfTaskVo> getRejectTaskListByTask(UserTask source);

    void importConfig(ProcessConfigBo bo);
}
