package com.kayak.web.workflow.service;

import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.bo.WfBusConfigBo;
import com.kayak.web.workflow.domain.vo.WfBusConfigVo;

import java.util.Collection;

/**
 * 业务流程Service接口
 *
 * @author yuanjinqiao
 * @date 2022-09-14
 */
public interface IWfBusConfigService {

    /**
     * 查询业务流程
     */
    WfBusConfigVo queryById(String server);

    /**
     * 查询业务流程列表
     */
    TableDataInfo<WfBusConfigVo> queryPageList(WfBusConfigBo bo, PageQuery pageQuery);

    /**
     * 修改业务流程
     */
    Boolean insertByBo(WfBusConfigBo bo);

    /**
     * 修改业务流程
     */
    Boolean updateByBo(WfBusConfigBo bo);

    /**
     * 校验并批量删除业务流程信息
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
