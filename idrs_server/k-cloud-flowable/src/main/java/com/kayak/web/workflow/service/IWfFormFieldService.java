package com.kayak.web.workflow.service;

import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.bo.WfFormFieldBo;
import com.kayak.web.workflow.domain.bo.WfRejectTaskBo;
import com.kayak.web.workflow.domain.vo.WfFormFieldVo;

import java.util.Collection;
import java.util.List;

/**
 * 单字段配置Service接口
 *
 * @author yuanjinqiao
 * @date 2022-09-02
 */
public interface IWfFormFieldService {

    /**
     * 查询单字段配置
     */
    WfFormFieldVo queryById(Long formFieldId);

    /**
     * 查询单字段配置列表
     */
    TableDataInfo<WfFormFieldVo> queryPageList(WfFormFieldBo bo, PageQuery pageQuery);

    /**
     * 修改单字段配置
     */
    Boolean insertByBo(WfFormFieldBo bo);

    /**
     * 修改单字段配置
     */
    Boolean updateByBo(WfFormFieldBo bo);

    /**
     * 校验并批量删除单字段配置信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<WfFormFieldVo> listByIds(List<String> ids);

    List<WfFormFieldVo> listByXml(WfRejectTaskBo bo);
}
