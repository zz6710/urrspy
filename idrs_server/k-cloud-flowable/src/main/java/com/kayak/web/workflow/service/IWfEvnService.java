package com.kayak.web.workflow.service;

import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.bo.WfEnvBo;
import com.kayak.web.workflow.domain.vo.WfEnvVo;

import java.util.Collection;
import java.util.List;

/**
 * 流程参数配置Service接口
 *
 * @author yuanjinqiao
 * @date 2022-09-08
 */
public interface IWfEvnService {

    /**
     * 查询流程参数配置
     */
    WfEnvVo queryById(Long envId);

    /**
     * 查询流程参数配置列表
     */
    TableDataInfo<WfEnvVo> queryPageList(WfEnvBo bo, PageQuery pageQuery);

    /**
     * 查询流程参数配置列表
     */
    List<WfEnvVo> queryList(WfEnvBo bo);

    /**
     * 修改流程参数配置
     */
    Boolean insertByBo(WfEnvBo bo);

    /**
     * 修改流程参数配置
     */
    Boolean updateByBo(WfEnvBo bo);

    /**
     * 校验并批量删除流程参数配置信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
