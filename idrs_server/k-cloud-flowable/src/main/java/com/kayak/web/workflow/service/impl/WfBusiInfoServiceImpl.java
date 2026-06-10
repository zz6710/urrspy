package com.kayak.web.workflow.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.constant.BusinessStatus;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.utils.AuthObjectUtil;
import com.kayak.utils.FieldUtil;
import com.kayak.utils.StringUtils;
import com.kayak.utils.SysUtil;
import com.kayak.web.system.domain.SysUser;
import com.kayak.web.workflow.domain.WfBusiInfo;
import com.kayak.web.workflow.domain.bo.WfBusiInfoBo;
import com.kayak.web.workflow.domain.vo.WfBusiInfoVo;
import com.kayak.web.workflow.mapper.WfBusiInfoMapper;
import com.kayak.web.workflow.service.IWfBusiInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * 业务审批Service业务层处理
 *
 * @author yuanjinqiao
 * @date 2022-09-15
 */
@RequiredArgsConstructor
@Service
public class WfBusiInfoServiceImpl implements IWfBusiInfoService {

    private final WfBusiInfoMapper baseMapper;

    /**
     * 查询业务审批列表
     */
    @Override
    public TableDataInfo<WfBusiInfoVo> queryPageList(WfBusiInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WfBusiInfo> lqw = buildQueryWrapper(bo);
        Page<WfBusiInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        AuthObjectUtil.complementUserInfo(
                result.getRecords(),
                MapUtil.builder(new HashMap<String, String>()).put(FieldUtil.noPrefix(WfBusiInfoVo::getCreateBy), FieldUtil.noPrefix(WfBusiInfoVo::getCreatorName)).build(),
                FieldUtil.noPrefix(SysUser::getUsername)
        );
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<WfBusiInfo> buildQueryWrapper(WfBusiInfoBo bo) {
        LambdaQueryWrapper<WfBusiInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getServer()), WfBusiInfo::getServer, bo.getServer());
        lqw.eq(StringUtils.isNotBlank(bo.getUrl()), WfBusiInfo::getUrl, bo.getUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getKeys()), WfBusiInfo::getKeys, bo.getKeys());
        lqw.eq(StringUtils.isNotBlank(bo.getValues()), WfBusiInfo::getValues, bo.getValues());
        lqw.eq(StringUtils.isNotBlank(bo.getProcessKey()), WfBusiInfo::getProcessKey, bo.getProcessKey());
        lqw.eq(StringUtils.isNotBlank(bo.getProcessDefinitionId()), WfBusiInfo::getProcessDefinitionId, bo.getProcessDefinitionId());
        lqw.eq(StringUtils.isNotBlank(bo.getProcessStatus()), WfBusiInfo::getProcessStatus, bo.getProcessStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getBusStatus()), WfBusiInfo::getBusStatus, bo.getBusStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getBusReturnMsg()), WfBusiInfo::getBusReturnMsg, bo.getBusReturnMsg());
        lqw.eq(bo.getCallbackNum() != null, WfBusiInfo::getCallbackNum, bo.getCallbackNum());
        lqw.eq(StringUtils.isNotBlank(bo.getValidateId()), WfBusiInfo::getValidateId, bo.getValidateId());
        lqw.orderByDesc(WfBusiInfo::getCreateTime);
        return lqw;
    }

    @Override
    public boolean errorConfirm(WfBusiInfo update) {
        update.setBusStatus(BusinessStatus.ERROR_CONFIRMED);
        update.setBusReturnMsg("手工确认");
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 更新流程状态
     *
     * @param procInsId
     * @param status
     * @return
     */
    @Override
    public boolean updateProcStatus(String procInsId, String status) {
        LambdaUpdateWrapper<WfBusiInfo> wrapper = new LambdaUpdateWrapper<WfBusiInfo>().eq(WfBusiInfo::getProcessInstanceId, procInsId).set(WfBusiInfo::getProcessStatus, status).set(WfBusiInfo::getUpdateTime, new Date()).set(WfBusiInfo::getUpdateBy, SysUtil.getCurrentUserId());
        return baseMapper.update(null, wrapper) > 0;
    }

}
