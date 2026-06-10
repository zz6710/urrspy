package com.kayak.web.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.StringUtils;
import com.kayak.web.workflow.domain.WfEnv;
import com.kayak.web.workflow.domain.WfEnvItem;
import com.kayak.web.workflow.domain.bo.WfEnvBo;
import com.kayak.web.workflow.domain.vo.WfEnvVo;
import com.kayak.web.workflow.mapper.WfEnvItemMapper;
import com.kayak.web.workflow.mapper.WfEnvMapper;
import com.kayak.web.workflow.service.IWfEvnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 流程参数配置Service业务层处理
 *
 * @author yuanjinqiao
 * @date 2022-09-08
 */
@RequiredArgsConstructor
@Service
public class WfEvnServiceImpl implements IWfEvnService {

    private final WfEnvMapper baseMapper;

    private final WfEnvItemMapper wfEnvItemMapper;

    /**
     * 查询流程参数配置
     */
    @Override
    public WfEnvVo queryById(Long envId) {
        return baseMapper.selectVoById(envId);
    }

    /**
     * 查询流程参数配置列表
     */
    @Override
    public TableDataInfo<WfEnvVo> queryPageList(WfEnvBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WfEnv> lqw = buildQueryWrapper(bo);
        Page<WfEnvVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询流程参数配置列表
     */
    @Override
    public List<WfEnvVo> queryList(WfEnvBo bo) {
        LambdaQueryWrapper<WfEnv> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WfEnv> buildQueryWrapper(WfEnvBo bo) {
        LambdaQueryWrapper<WfEnv> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), WfEnv::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getDisplayName()), WfEnv::getDisplayName, bo.getDisplayName());
        return lqw;
    }

    /**
     * 新增流程参数配置
     */
    @Override
    public Boolean insertByBo(WfEnvBo bo) {
        checkEnvName(bo);

        WfEnv add = BeanUtil.toBean(bo, WfEnv.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setEnvId(add.getEnvId());
        }
        return flag;
    }

    /**
     * 修改流程参数配置
     */
    @Override
    public Boolean updateByBo(WfEnvBo bo) {
        checkEnvName(bo);
        WfEnv update = BeanUtil.toBean(bo, WfEnv.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    public void checkEnvName(WfEnvBo bo) {
        LambdaQueryWrapper<WfEnv> lqw = Wrappers.lambdaQuery();
        if (bo.getEnvId() == null) {
            lqw.eq(WfEnv::getName, bo.getName()).or().eq(WfEnv::getDisplayName, bo.getDisplayName());
        } else {
            lqw.ne(WfEnv::getEnvId, bo.getEnvId()).and(i -> i.eq(WfEnv::getName, bo.getName()).or().eq(WfEnv::getDisplayName, bo.getDisplayName()));
        }
        Long aLong = baseMapper.selectCount(lqw);
        if (aLong > 0) {
            throw new WorkflowException("流程参数英文名和中文名不能重复");
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(WfEnv entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除流程参数配置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        LambdaQueryWrapper<WfEnvItem> lqw = Wrappers.lambdaQuery();
        lqw.in(WfEnvItem::getEnvId, ids);
        wfEnvItemMapper.delete(lqw);
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
