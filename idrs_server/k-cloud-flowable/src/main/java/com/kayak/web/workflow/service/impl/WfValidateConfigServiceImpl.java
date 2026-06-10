package com.kayak.web.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.utils.StringUtils;
import com.kayak.web.workflow.domain.WfValidateConfig;
import com.kayak.web.workflow.mapper.WfValidateConfigMapper;
import com.kayak.web.workflow.service.IWfValidateConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 回调返回参数校验配置Service业务层处理
 *
 * @author yuanjinqiao
 * @date 2022-09-15
 */
@RequiredArgsConstructor
@Service
public class WfValidateConfigServiceImpl implements IWfValidateConfigService {

    private final WfValidateConfigMapper baseMapper;

    /**
     * 查询回调返回参数校验配置列表
     */
    @Override
    public TableDataInfo<WfValidateConfig> queryPageList(WfValidateConfig bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WfValidateConfig> lqw = buildQueryWrapper(bo);
        Page<WfValidateConfig> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<WfValidateConfig> buildQueryWrapper(WfValidateConfig bo) {
        LambdaQueryWrapper<WfValidateConfig> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), WfValidateConfig::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getRule()), WfValidateConfig::getRule, bo.getRule());
        return lqw;
    }

}
