package com.kayak.web.workflow.service;

import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.WfValidateConfig;

import java.util.Collection;
import java.util.List;

/**
 * 回调返回参数校验配置Service接口
 *
 * @author yuanjinqiao
 * @date 2022-09-15
 */
public interface IWfValidateConfigService {


    /**
     * 查询回调返回参数校验配置列表
     */
    TableDataInfo<WfValidateConfig> queryPageList(WfValidateConfig bo, PageQuery pageQuery);

}
