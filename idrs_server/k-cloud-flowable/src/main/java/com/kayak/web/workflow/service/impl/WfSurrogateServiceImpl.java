package com.kayak.web.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.StringUtils;
import com.kayak.utils.SysUtil;
import com.kayak.web.system.domain.SysUser;
import com.kayak.web.workflow.domain.WfSurrogate;
import com.kayak.web.workflow.domain.bo.WfSurrogateBo;
import com.kayak.web.workflow.domain.vo.WfSurrogateVo;
import com.kayak.web.workflow.mapper.WfSurrogateMapper;
import com.kayak.web.workflow.service.IWfSurrogateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * 任务代理Service业务层处理
 *
 * @author yuanjinqiao
 * @date 2022-10-03
 */
@RequiredArgsConstructor
@Service
public class WfSurrogateServiceImpl implements IWfSurrogateService {

    private final WfSurrogateMapper baseMapper;

    /**
     * 查询任务代理
     */
    @Override
    public WfSurrogateVo queryById(Long ID) {
        return baseMapper.selectVoById(ID);
    }

    /**
     * 查询任务代理列表
     */
    @Override
    public TableDataInfo<WfSurrogateVo> queryPageList(WfSurrogateBo bo, PageQuery pageQuery) {
        //只能查询自己的代理数据
        bo.setCreator(SysUtil.getCurrentUserId());
        LambdaQueryWrapper<WfSurrogate> lqw = buildQueryWrapper(bo);
        Page<WfSurrogateVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<WfSurrogate> buildQueryWrapper(WfSurrogateBo bo) {
        LambdaQueryWrapper<WfSurrogate> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getProcessKey()), WfSurrogate::getProcessKey, bo.getProcessKey());
        lqw.like(StringUtils.isNotBlank(bo.getProcessName()), WfSurrogate::getProcessName, bo.getProcessName());
        lqw.eq(bo.getStartDate() != null, WfSurrogate::getStartDate, bo.getStartDate());
        lqw.eq(bo.getStartDate() != null, WfSurrogate::getEndDate, bo.getEndDate());
        lqw.eq(StringUtils.isNotBlank(bo.getCreator()), WfSurrogate::getCreator, bo.getCreator());
        lqw.eq(StringUtils.isNotBlank(bo.getSurrogate()), WfSurrogate::getSurrogate, bo.getSurrogate());
        lqw.like(StringUtils.isNotBlank(bo.getSurrogateName()), WfSurrogate::getSurrogateName, bo.getSurrogateName());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), WfSurrogate::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增任务代理
     */
    @Override
    public Boolean insertByBo(WfSurrogateBo bo) {
        SysUser currentUserInfo = SysUtil.getCurrentUserInfo();
        bo.setCreator(currentUserInfo.getUserid());
        bo.setCreateName(currentUserInfo.getUsername());
        WfSurrogate add = BeanUtil.toBean(bo, WfSurrogate.class);
        validEntityBeforeSave(bo);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改任务代理
     */
    @Override
    public Boolean updateByBo(WfSurrogateBo bo) {
        SysUser currentUserInfo = SysUtil.getCurrentUserInfo();
        bo.setCreator(currentUserInfo.getUserid());
        bo.setCreateName(currentUserInfo.getUsername());
        WfSurrogate update = BeanUtil.toBean(bo, WfSurrogate.class);
        validEntityBeforeSave(bo);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(WfSurrogateBo bo) {
        LambdaQueryWrapper<WfSurrogate> lambdaQuery = Wrappers.lambdaQuery(WfSurrogate.class);
        lambdaQuery.eq(WfSurrogate::getProcessKey, bo.getProcessKey());
        lambdaQuery.eq(WfSurrogate::getCreator, bo.getCreator());
        lambdaQuery.ne(bo.getId() != null, WfSurrogate::getId, bo.getId());
        Long count = baseMapper.selectCount(lambdaQuery);
        if (count > 0) {
            throw new WorkflowException("当前流程已设置代理人");
        }
    }

    /**
     * 批量删除任务代理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
