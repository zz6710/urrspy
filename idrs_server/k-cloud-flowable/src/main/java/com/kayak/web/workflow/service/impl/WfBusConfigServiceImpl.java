package com.kayak.web.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.ConfigUitl;
import com.kayak.utils.StringUtils;
import com.kayak.web.workflow.domain.WfBusConfig;
import com.kayak.web.workflow.domain.bo.WfBusConfigBo;
import com.kayak.web.workflow.domain.vo.WfBusConfigVo;
import com.kayak.web.workflow.mapper.WfBusConfigMapper;
import com.kayak.web.workflow.service.IWfBusConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 业务流程Service业务层处理
 *
 * @author yuanjinqiao
 * @date 2022-09-14
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class WfBusConfigServiceImpl implements IWfBusConfigService {

    private final WfBusConfigMapper baseMapper;

    /**
     * 查询业务流程
     */
    @Override
    public WfBusConfigVo queryById(String server) {
        return baseMapper.selectVoById(server);
    }

    /**
     * 查询业务流程列表
     */
    @Override
    public TableDataInfo<WfBusConfigVo> queryPageList(WfBusConfigBo bo, PageQuery pageQuery) {
        QueryWrapper<WfBusConfig> wrapper = Wrappers.query();
        wrapper.eq(StringUtils.isNotEmpty(bo.getProcessKey()), "t.process_key", bo.getProcessKey());
        wrapper.eq(StringUtils.isNotEmpty(bo.getServer()), "t.server", bo.getServer());
        Page<WfBusConfigVo> list = baseMapper.queryPageList(wrapper, pageQuery.build());
        return TableDataInfo.build(list);
    }

    /**
     * 新增业务流程
     */
    @Override
    public Boolean insertByBo(WfBusConfigBo bo) {
        LambdaQueryWrapper<WfBusConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(WfBusConfig::getServer, bo.getServer());
        Long count = baseMapper.selectCount(lqw);
        if (count > 0) {
            throw new WorkflowException("该业务已配置审批流，不能重复配置");
        }
        WfBusConfig add = BeanUtil.toBean(bo, WfBusConfig.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setServer(add.getServer());
        }
        try {
            ConfigUitl.publicNacosConfig("kcloud_wf_busi_config", System.currentTimeMillis() + "add");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * 修改业务流程
     */
    @Override
    public Boolean updateByBo(WfBusConfigBo bo) {
        WfBusConfig update = BeanUtil.toBean(bo, WfBusConfig.class);
        int i = baseMapper.updateById(update);
        try {
            ConfigUitl.publicNacosConfig("kcloud_wf_busi_config", System.currentTimeMillis() + "edit");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return i > 0;
    }

    /**
     * 批量删除业务流程
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        int i = baseMapper.deleteBatchIds(ids);
        try {
            ConfigUitl.publicNacosConfig("kcloud_wf_busi_config", System.currentTimeMillis() + "delete");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return i > 0;
    }
}
