package com.kayak.web.workflow.service;

import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.bo.WfTaskQueryBo;
import com.kayak.web.workflow.domain.vo.WfTaskVo;

/**
 * @author Ty
 * @since 2023-05-15 15:44:49
 */
public interface IWfDesktopService {

    /**
     * 查询代办任务列表
     *
     * @param bo
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> queryPageTodoProcessList(WfTaskQueryBo bo, PageQuery pageQuery);

    /**
     * 查询我的流程列表
     *
     * @param bo
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> queryPageOwnProcessList(WfTaskQueryBo bo, PageQuery pageQuery);

}
