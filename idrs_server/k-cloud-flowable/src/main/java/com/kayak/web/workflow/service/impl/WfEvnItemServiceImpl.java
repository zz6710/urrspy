package com.kayak.web.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.WfEnvItem;
import com.kayak.web.workflow.domain.bo.WfEnvItemBo;
import com.kayak.web.workflow.domain.vo.WfEnvItemVo;
import com.kayak.web.workflow.mapper.WfEnvItemMapper;
import com.kayak.web.workflow.service.IWfEvnItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 流程参数项配置Service业务层处理
 *
 * @author yuanjinqiao
 * @date 2022-09-08
 */
@RequiredArgsConstructor
@Service
public class WfEvnItemServiceImpl implements IWfEvnItemService {

    private final WfEnvItemMapper baseMapper;

    @Override
    public TableDataInfo<WfEnvItemVo> queryPageList(WfEnvItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WfEnvItem> lqw = buildQueryWrapper(bo);
        Page<WfEnvItemVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<WfEnvItem> buildQueryWrapper(WfEnvItemBo bo) {
        LambdaQueryWrapper<WfEnvItem> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getEnvId() != null, WfEnvItem::getEnvId, bo.getEnvId());
        return lqw;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAll(List<WfEnvItemBo> boList, boolean b) {
        List<WfEnvItem> wfEnvItems = BeanUtil.copyToList(boList, WfEnvItem.class);
        LambdaQueryWrapper<WfEnvItem> lqw = Wrappers.lambdaQuery();
        lqw.eq(WfEnvItem::getEnvId, wfEnvItems.get(0).getEnvId());
        baseMapper.delete(lqw);
        return baseMapper.insertBatch(wfEnvItems);
    }

}
