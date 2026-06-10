package com.kayak.web.workflow.service;

import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.bo.WfEnvItemBo;
import com.kayak.web.workflow.domain.vo.WfEnvItemVo;

import java.util.List;

/**
 * 流程参数项配置Service接口
 *
 * @author yuanjinqiao
 * @date 2022-09-08
 */
public interface IWfEvnItemService {
    /**
     * 查询流程参数项配置列表
     */
    TableDataInfo<WfEnvItemVo> queryPageList(WfEnvItemBo bo, PageQuery pageQuery);


    boolean saveAll(List<WfEnvItemBo> boList, boolean b);
}
