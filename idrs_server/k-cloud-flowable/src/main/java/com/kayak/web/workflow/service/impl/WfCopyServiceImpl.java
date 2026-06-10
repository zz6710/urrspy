package com.kayak.web.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.constant.ProcessConstants;
import com.kayak.common.constant.TaskConstants;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.exception.WorkflowException;
import com.kayak.factory.SystemServiceFactory;
import com.kayak.utils.AuthObjectUtil;
import com.kayak.utils.FieldUtil;
import com.kayak.utils.StringUtils;
import com.kayak.utils.SysUtil;
import com.kayak.utils.flow.FlowableUtils;
import com.kayak.utils.flow.ModelUtils;
import com.kayak.web.system.domain.SysUser;
import com.kayak.web.workflow.domain.WfCopy;
import com.kayak.web.workflow.domain.bo.*;
import com.kayak.web.workflow.domain.vo.WfCopyVo;
import com.kayak.web.workflow.domain.vo.WfFormConfVO;
import com.kayak.web.workflow.mapper.WfCopyMapper;
import com.kayak.web.workflow.service.IWfCopyService;
import com.kayak.web.workflow.service.IWfParamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程抄送Service业务层处理
 *
 * @author yuanjinqiao
 * @date 2022-05-19
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class WfCopyServiceImpl implements IWfCopyService {
    private final WfCopyMapper baseMapper;
    private final HistoryService historyService;
    private final IWfParamService wfParamService;
    private final SystemServiceFactory systemServiceFactory;
    private final RepositoryService repositoryService;

    @Override
    public TableDataInfo<WfCopyVo> queryPageList(WfCopyTaskQueryBo taskQueryBo, PageQuery pageQuery) {
        LambdaQueryWrapper<WfCopy> lqw = Wrappers.lambdaQuery();
        lqw.eq(WfCopy::getUserId, SysUtil.getCurrentUserId());
        lqw.eq(StringUtils.isNotEmpty(taskQueryBo.getProcKey()), WfCopy::getProcKey, taskQueryBo.getProcKey());
        lqw.eq(StringUtils.isNotEmpty(taskQueryBo.getRead()), WfCopy::getRead, taskQueryBo.getRead());
        lqw.eq(StringUtils.isNotEmpty(taskQueryBo.getLaunchCopyUserId()), WfCopy::getLaunchCopyUserId, taskQueryBo.getLaunchCopyUserId());
        //日期过滤
        SimpleDateFormat beginDataSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat endDataSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        if (taskQueryBo.getCreateStartDate() != null) {
            lqw.ge(WfCopy::getCreateTime, beginDataSdf.format(taskQueryBo.getCreateStartDate()));
        }
        if (taskQueryBo.getCreateEndDate() != null) {
            lqw.le(WfCopy::getCreateTime, endDataSdf.format(taskQueryBo.getCreateEndDate()));
        }
        lqw.orderByDesc(WfCopy::getCreateTime);
        Page<WfCopyVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        AuthObjectUtil.complementUserInfo(result.getRecords(), MapUtil.builder(new HashMap<String, String>()).put(FieldUtil.noPrefix(WfCopyVo::getLaunchCopyUserId), FieldUtil.noPrefix(WfCopyVo::getLaunchCopyUserName)).build(), FieldUtil.noPrefix(SysUser::getUsername));
        return TableDataInfo.build(result);
    }

    @Override
    public void makeCopy(String procInsId, UserTask userTask) {
        Set<String> copyUsers = new HashSet<>();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInsId).includeProcessVariables().singleResult();
        Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();
        Map<String, Object> o = (HashMap<String, Object>) processVariables.getOrDefault(TaskConstants.COPY_USER, new HashMap<String, Object>());
        CopyUserBo copyUser = BeanUtil.mapToBean(o, CopyUserBo.class, true, CopyOptions.create());
        if (userTask.getId().equals(copyUser.getId()) && CollectionUtil.isNotEmpty(copyUser.getCopyUsers())) {
            //指定的抄送人
            copyUsers.addAll(copyUser.getCopyUsers());
        } else {
            //配置的抄送人
            copyUsers = getCopyUsers(historicProcessInstance, userTask);
        }
        if (CollectionUtil.isEmpty(copyUsers)) {
            log.info("当前任务节点未配置抄送用户");
            return;
        }
        LambdaQueryWrapper<WfCopy> lqw = Wrappers.lambdaQuery();
        lqw.eq(WfCopy::getTaskDefKey, userTask.getId());
        lqw.eq(WfCopy::getProcInsId, procInsId);
        Long count = baseMapper.selectCount(lqw);
        if (count > 0) {
            //会签任务和驳回任务可能会出现多条相同的抄送
            log.info("当前任务节点已抄送用户");
            return;
        }
        List<WfCopy> copyList = new ArrayList<>();
        String currentUserId = SysUtil.getCurrentUserId();
        for (String id : copyUsers) {
            WfCopy copy = new WfCopy();
            copy.setCopyId(IdWorker.getId());
            copy.setTaskName(userTask.getName());
            copy.setProcDefName(historicProcessInstance.getProcessDefinitionName());
            copy.setProcDefVersion(historicProcessInstance.getProcessDefinitionVersion());
            copy.setProcKey(historicProcessInstance.getProcessDefinitionKey());
            copy.setProcInsId(historicProcessInstance.getId());
            copy.setProcDefId(historicProcessInstance.getProcessDefinitionId());
            //copy.setTaskId(task.getId());
            copy.setTaskDefKey(userTask.getId());
            copy.setUserId(id);
            copy.setLaunchCopyUserId(currentUserId);
            copy.setRead("0");
            copyList.add(copy);
        }
        baseMapper.insertBatch(copyList);
    }

    @Override
    public void read(WfCopyBo bo) {
        LambdaUpdateWrapper<WfCopy> wrapper = new LambdaUpdateWrapper<WfCopy>().eq(WfCopy::getCopyId, bo.getCopyId()).set(WfCopy::getRead, bo.getRead()).set(WfCopy::getUpdateTime, new Date()).set(WfCopy::getUpdateBy, SysUtil.getCurrentUserId());
        baseMapper.update(null, wrapper);
    }

    @Override
    public WfFormConfVO getFormConf(String procDefId, String taskDefKey) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
        //任务节点的表单
        UserTask userTask = (UserTask) FlowableUtils.findFlowElementByActivityId(bpmnModel, taskDefKey);
        String copyFormType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.COPY_FORM_TYPE);
        String copyFormKey = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.COPY_FORM_KEY);
        WfFormConfVO build = WfFormConfVO.builder().taskDefKey(userTask.getId()).formKey(copyFormKey).formType(copyFormType).build();
        return build;
    }

    private Set<String> getCopyUsers(HistoricProcessInstance historicProcessInstance, UserTask userTask) {
        Set<String> allUser = new HashSet<>();
        String copyUsers = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.COPY_USERS);
        String copyRoles = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.COPY_ROLES);
        String copyUserParam = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.COPY_USER_PARAM);
        if (StringUtils.isNotEmpty(copyUsers)) {
            allUser.addAll(Arrays.asList(copyUsers.split(",")));
        }
        if (StringUtils.isNotEmpty(copyRoles)) {
            List<SysUser> users = systemServiceFactory.createService().getUserByRoleIds(Arrays.asList(copyRoles.split(",")));
            List<String> collect = users.stream().map(t -> t.getUserid()).collect(Collectors.toList());
            allUser.addAll(collect);
        }
        if (StringUtils.isNotEmpty(copyUserParam)) {
            Object userIds = wfParamService.parseWfParam(copyUserParam, historicProcessInstance.getProcessVariables(), historicProcessInstance.getId());
            if (userIds instanceof List) {
                allUser.addAll((List<String>) userIds);
            } else if (userIds instanceof String) {
                allUser.add((String) userIds);
            } else {
                throw new WorkflowException("流程参数[" + copyUserParam + "]用于动态审批人时,返回值应该为List<String>或者String");
            }
        }
        return allUser;
    }
}
