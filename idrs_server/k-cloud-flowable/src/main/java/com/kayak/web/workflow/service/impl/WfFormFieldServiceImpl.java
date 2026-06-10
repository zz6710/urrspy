package com.kayak.web.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.constant.ProcessConstants;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.enums.FormTypeEnum;
import com.kayak.utils.StringUtils;
import com.kayak.utils.flow.ModelUtils;
import com.kayak.web.workflow.domain.WfFormField;
import com.kayak.web.workflow.domain.bo.WfFormFieldBo;
import com.kayak.web.workflow.domain.bo.WfRejectTaskBo;
import com.kayak.web.workflow.domain.vo.WfFormFieldVo;
import com.kayak.web.workflow.mapper.WfFormFieldMapper;
import com.kayak.web.workflow.service.IWfFormFieldService;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 单字段配置Service业务层处理
 *
 * @author yuanjinqiao
 * @date 2022-09-02
 */
@RequiredArgsConstructor
@Service
public class WfFormFieldServiceImpl implements IWfFormFieldService {

    private final WfFormFieldMapper baseMapper;

    /**
     * 查询单字段配置
     */
    @Override
    public WfFormFieldVo queryById(Long formFieldId) {
        return baseMapper.selectVoById(formFieldId);
    }

    /**
     * 查询单字段配置列表
     */
    @Override
    public TableDataInfo<WfFormFieldVo> queryPageList(WfFormFieldBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WfFormField> lqw = buildQueryWrapper(bo);
        Page<WfFormFieldVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<WfFormField> buildQueryWrapper(WfFormFieldBo bo) {
        LambdaQueryWrapper<WfFormField> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), WfFormField::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getDisplayName()), WfFormField::getDisplayName, bo.getDisplayName());
        lqw.eq(StringUtils.isNotBlank(bo.getJson()), WfFormField::getJson, bo.getJson());
        return lqw;
    }

    /**
     * 新增单字段配置
     */
    @Override
    public Boolean insertByBo(WfFormFieldBo bo) {
        WfFormField add = BeanUtil.toBean(bo, WfFormField.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setFormFieldId(add.getFormFieldId());
        }
        return flag;
    }

    /**
     * 修改单字段配置
     */
    @Override
    public Boolean updateByBo(WfFormFieldBo bo) {
        WfFormField update = BeanUtil.toBean(bo, WfFormField.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(WfFormField entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除单字段配置
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<WfFormFieldVo> listByIds(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }
        LambdaQueryWrapper<WfFormField> lqw = Wrappers.lambdaQuery();
        lqw.in(WfFormField::getFormFieldId, ids);
        List<WfFormFieldVo> wfFormFieldVos = baseMapper.selectVoList(lqw);
        return wfFormFieldVos;
    }

    @Override
    public List<WfFormFieldVo> listByXml(WfRejectTaskBo bo) {
        List<String> ids = new ArrayList<>();
        //获取流程所有的开始节点与用户任务节点
        BpmnModel bpmnModel = ModelUtils.getBpmnModel(bo.getBpmnXml());
        Collection<UserTask> allUserTaskEvent = ModelUtils.getAllUserTaskEvent(bpmnModel);
        StartEvent startEvent = ModelUtils.getStartEvent(bpmnModel);
        //获取节点中的表单配置
        if (startEvent != null) {
            String formKey = startEvent.getFormKey();
            String formType = startEvent.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_FORM_TYPE);
            if (FormTypeEnum.FORM_FIELD_ID.getType().equals(formType) && StringUtils.isNotEmpty(formKey)) {
                ids.add(formKey);
            }
        }
        if (CollectionUtil.isNotEmpty(allUserTaskEvent)) {
            for (UserTask userTask : allUserTaskEvent) {
                String formKey = userTask.getFormKey();
                String formType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_FORM_TYPE);
                if (FormTypeEnum.FORM_FIELD_ID.getType().equals(formType) && StringUtils.isNotEmpty(formKey)) {
                    ids.add(formKey);
                }
            }
        }
        List<WfFormFieldVo> wfFormFieldVos = this.listByIds(ids);
        return wfFormFieldVos;
    }

}
