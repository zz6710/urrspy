package com.kayak.web.workflow.service;

import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.bo.WfSurrogateBo;
import com.kayak.web.workflow.domain.vo.WfSurrogateVo;

import java.util.Collection;
import java.util.List;

/**
 * 任务代理Service接口
 *
 * @author yuanjinqiao
 * @date 2022-10-03
 */
public interface IWfSurrogateService {

    /**
     * 查询任务代理
     */
    WfSurrogateVo queryById(Long ID);

    /**
     * 查询任务代理列表
     */
    TableDataInfo<WfSurrogateVo> queryPageList(WfSurrogateBo bo, PageQuery pageQuery);

    /**
     * 修改任务代理
     */
    Boolean insertByBo(WfSurrogateBo bo);

    /**
     * 修改任务代理
     */
    Boolean updateByBo(WfSurrogateBo bo);

    /**
     * 校验并批量删除任务代理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
