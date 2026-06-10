package com.kayak.web.workflow.service;

import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.bo.WfProcessBo;
import com.kayak.web.workflow.domain.bo.WfProcessStateBo;
import com.kayak.web.workflow.domain.vo.WfDeployVo;

import java.util.List;

/**
 * @author yuanjinqiao
 * @createTime 2022/6/30 9:03
 */
public interface IWfDeployService {

    TableDataInfo<WfDeployVo> queryPageList(WfProcessBo processBo, PageQuery pageQuery);

    TableDataInfo<WfDeployVo> queryPublishList(String processKey, PageQuery pageQuery);

    void updateState(WfProcessStateBo wfProcessStateBo);

    String queryBpmnXmlById(String definitionId);

    void deleteByIds(List<String> deployIds);

    void override(String fromProcessDefId, String toProcessDefId);

}
