package com.kayak.web.workflow.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.*;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.constant.*;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.entity.result.R;
import com.kayak.common.enums.ProcessInstanceStatusEnum;
import com.kayak.common.enums.RoleTypeEnum;
import com.kayak.common.enums.TaskCommentEnum;
import com.kayak.common.enums.TaskStatusEnum;
import com.kayak.common.exception.WorkflowException;
import com.kayak.factory.FlowServiceFactory;
import com.kayak.factory.SystemServiceFactory;
import com.kayak.utils.*;
import com.kayak.utils.flow.FlowableUtils;
import com.kayak.utils.flow.ModelUtils;
import com.kayak.web.system.domain.SysOrg;
import com.kayak.web.system.domain.SysRole;
import com.kayak.web.system.domain.SysUser;
import com.kayak.web.system.domain.SystemParam;
import com.kayak.web.workflow.domain.WfBusiInfo;
import com.kayak.web.workflow.domain.WfSurrogate;
import com.kayak.web.workflow.domain.bo.WfTaskQueryBo;
import com.kayak.web.workflow.domain.vo.*;
import com.kayak.web.workflow.mapper.WfBusiInfoMapper;
import com.kayak.web.workflow.mapper.WfSurrogateMapper;
import com.kayak.web.workflow.mapper.WfTaskInstMapper;
import com.kayak.web.workflow.service.ICalculateProcessService;
import com.kayak.web.workflow.service.IWfProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLinkInfo;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuanjinqiao
 * @createTime 2022/3/24 18:57
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class WfProcessServiceImpl extends FlowServiceFactory implements IWfProcessService {
    private final WfBusiInfoMapper wfBusiInfoMapper;
    private final WfSurrogateMapper wfSurrogateMapper;
    private final SystemServiceFactory systemServiceFactory;
    private final WfTaskInstMapper wfTaskInstMapper;
    private final ICalculateProcessService iCalculateProcessService;

    /**
     * 根据流程定义key启动流程实例
     *
     * @param variables 流程变量
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> startProcess(Map<String, Object> variables) {
        //参数校验
        String server = (String) variables.remove(FieldConstants.SERVER);
        if (StringUtils.isBlank(server)) {
            throw new WorkflowException("回调SERVER不能为空");
        }
        String url = (String) variables.remove(FieldConstants.URL);
        if (StringUtils.isBlank(url)) {
            throw new WorkflowException("回调URL不能为空");
        }
        String processKey = (String) variables.remove(FieldConstants.PROCESS_KEY);
        if (StringUtils.isBlank(processKey)) {
            throw new WorkflowException("流程key不能为空");
        }
        //表单字段显示json
        String labelInfo = (String) variables.remove(FieldConstants.LABEL_INFO);
        //请求参数类型
        String contentType = (String) variables.remove(FieldConstants.CONTENT_TYPE);
        //流程实例id
        String processInstanceId = (String) variables.remove(FieldConstants.PROCESS_INSTANCE_ID);
        // 业务主键名
        String unKey = (String) variables.remove(FieldConstants.BUSI_UN_KEY);
        // 业务主键值
        String values = getKeyValues(unKey, variables);
        R<String> checkFlag = null;
        if (StringUtils.isNotEmpty(values)) {
            //根据业务主键值判断流程状态
            checkFlag = checkProcessStatusByUnKey(variables, unKey, values, processKey);
        } else if (StringUtils.isNotEmpty(processInstanceId)) {
            //根据流程实例id判断流程状态
            checkFlag = checkProcessStatusByProcessInstanceId(variables, processInstanceId);
        }
        if (checkFlag != null) {
            return checkFlag;
        }
        //发起新的流程
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey).latestVersion().singleResult();
        if (Objects.isNull(processDefinition)) {
            throw new WorkflowException("[" + processKey + "]流程不存在或未部署");
        }
        if (Objects.nonNull(processDefinition) && processDefinition.isSuspended()) {
            throw new WorkflowException("流程已被挂起，请先激活流程");
        }

        //判断回调报文校验是否存在
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        Process mainProcess = bpmnModel.getMainProcess();
        String validateId = ModelUtils.getExtensionElementText(mainProcess, ProcessConstants.PROCESS_CALLBACK_VALIDATE);
        if (StringUtils.isEmpty(validateId)) {
            throw new WorkflowException("回调报文校验不存在！");
        }
        //预测流程就只能把表单数据放到流程变量中来进行条件判断
        HashMap<String, Object> varMap = new HashMap<>();
        varMap.putAll(variables);
        // 设置流程发起人Id到流程中
        this.buildProcessVariables(varMap);
        //开启流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), varMap);
        // 记录业务审批表
        WfBusiInfo busiInfo = WfBusiInfo.builder().server(server).url(url).keys(unKey).values(values).contentType(contentType).processKey(processKey).processDefinitionId(processDefinition.getId()).processInstanceId(processInstance.getId()).processStatus(ProcessInstanceStatusEnum.RUNNING.getType()).busStatus(BusinessStatus.READY).validateId(validateId).submitData(JsonUtils.toJsonString(variables)).labelInfo(labelInfo).build();
        wfBusiInfoMapper.insert(busiInfo);
        //将流程业务状态修改为运行中
        runtimeService.updateBusinessStatus(processInstance.getProcessInstanceId(), ProcessInstanceStatusEnum.RUNNING.getType());
        // 给第一步申请人节点设置任务执行人和意见
        startFirstTask(processInstance.getProcessInstanceId(), variables);
        //构建返回结果
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put(FieldConstants.PROCESS_KEY, processInstance.getProcessDefinitionKey());
        retMap.put(FieldConstants.PROCESS_INSTANCE_ID, processInstance.getProcessInstanceId());
        retMap.put(FieldConstants.PROCESS_DEFINITION_ID, processInstance.getProcessDefinitionId());
        retMap.put(FieldConstants.PROCESS_STATUS, ProcessInstanceStatusEnum.RUNNING.getType());
        return R.ok("流程启动成功", retMap);
    }

    /**
     * 根据业务主键判断流程状态
     *
     * @param variables
     * @param unKey
     * @param values
     * @param processKey
     * @return
     */
    private R<String> checkProcessStatusByUnKey(Map<String, Object> variables, String unKey, String values, String processKey) {
        // 查询未审批完的数据
        List<WfBusiInfo> notFinishDataCount = wfBusiInfoMapper.findNotFinishDataByUnKey(processKey, values, unKey, ProcessInstanceStatusEnum.FINISH.getType(), ProcessInstanceStatusEnum.REFUSE.getType(), BusinessStatus.FINISH, BusinessStatus.ERROR_CONFIRMED);
        if (CollectionUtil.isNotEmpty(notFinishDataCount)) {
            //同一条数据不能重复审批
            WfBusiInfo wfBusiInfo = notFinishDataCount.get(0);
            //只能重新提交自己的流程
            if (wfBusiInfo.getCreateBy().equals(SysUtil.getCurrentUserId()) && wfBusiInfo.getProcessStatus().equals(ProcessInstanceStatusEnum.BACK_TO_APPLY.getType())) {
                //如果当前请求为退回到申请节点后重新发起的请求
                reApplyHandle(variables, wfBusiInfo.getProcessInstanceId());
                return R.ok("重新提交请求成功");
            } else {
                return R.fail("存在审批中的数据[" + unKey + " - " + values + "]");
            }
        }
        return null;
    }

    /**
     * 根据流程实例id判断流程状态
     *
     * @param variables
     * @param processInstanceId
     * @return
     */
    private R<String> checkProcessStatusByProcessInstanceId(Map<String, Object> variables, String processInstanceId) {
        WfBusiInfo wfBusiInfo = wfBusiInfoMapper.findNotFinishDataByProcessInstanceId(processInstanceId, ProcessInstanceStatusEnum.FINISH.getType(), ProcessInstanceStatusEnum.REFUSE.getType(), BusinessStatus.FINISH, BusinessStatus.ERROR_CONFIRMED);
        if (wfBusiInfo != null) {
            //同一条数据不能重复审批
            //只能重新提交自己的流程
            if (wfBusiInfo.getCreateBy().equals(SysUtil.getCurrentUserId()) && wfBusiInfo.getProcessStatus().equals(ProcessInstanceStatusEnum.BACK_TO_APPLY.getType())) {
                //如果当前请求为退回到申请节点后重新发起的请求
                reApplyHandle(variables, wfBusiInfo.getProcessInstanceId());
                return R.ok("重新提交请求成功");
            } else {
                return R.fail("存在审批中的数据");
            }
        }
        return null;
    }

    /**
     * 驳回到申请时的处理
     *
     * @param variables
     * @param processInstanceId
     */
    private void reApplyHandle(Map<String, Object> variables, String processInstanceId) {
        //自动完成第一个申请任务
        startFirstTask(processInstanceId, variables);
        //更新表单数据和流程状态
        LambdaUpdateWrapper<WfBusiInfo> wrapper = new LambdaUpdateWrapper<WfBusiInfo>().eq(WfBusiInfo::getProcessInstanceId, processInstanceId).set(WfBusiInfo::getProcessStatus, ProcessInstanceStatusEnum.RE_APPLY.getType()).set(WfBusiInfo::getSubmitData, JsonUtils.toJsonString(variables)).set(WfBusiInfo::getUpdateTime, new Date()).set(WfBusiInfo::getUpdateBy, SysUtil.getCurrentUserId());
        wfBusiInfoMapper.update(null, wrapper);
        //更新流程状态
        runtimeService.updateBusinessStatus(processInstanceId, ProcessInstanceStatusEnum.RE_APPLY.getType());
    }

    /**
     * 启动第一个任务
     *
     * @param processInstanceId 流程实例id
     * @param variables         流程参数
     */
    @Override
    public void startFirstTask(String processInstanceId, Map<String, Object> variables) {
        // 给第一步申请人节点设置任务执行人和意见
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        if (Objects.nonNull(task)) {
            String userIdStr = (String) variables.get(TaskConstants.PROCESS_INITIATOR);
            if (!StrUtil.equalsAny(task.getAssignee(), userIdStr, SysUtil.getCurrentUserId())) {
                throw new WorkflowException("数据验证失败，该工作流第一个用户任务的指派人并非当前用户，不能执行该操作！");
            }
            identityService.setAuthenticatedUserId(SysUtil.getCurrentUserId());
            //标识为申请任务
            task.setDescription(TaskConstants.APPLY_TASK_ID);
            taskService.saveTask(task);
            taskService.setAssignee(task.getId(), SysUtil.getCurrentUserId());
            taskService.addComment(task.getId(), processInstanceId, TaskCommentEnum.APPLY.getType(), SysUtil.getCurrentUserUsername() + "发起申请");
            taskService.complete(task.getId(),variables);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProcessInstance(String procInsId, String deleteReason) {
        // 查询历史数据
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInsId).singleResult();
        if (Objects.isNull(historicProcessInstance)) {
            throw new FlowableObjectNotFoundException("流程实例不存在: " + procInsId);
        }
        if (historicProcessInstance.getEndTime() != null) {
            historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
            return;
        }
        // 删除流程实例
        runtimeService.deleteProcessInstance(procInsId, deleteReason);
        // 删除历史流程实例
        historyService.deleteHistoricProcessInstance(procInsId);

    }

    public String getKeyValues(String busKeys, Map<String, Object> params) {
        if (Tools.isBlank(busKeys)) {
            return "";
        }
        String[] keys = busKeys.split(",");
        List<String> values = new ArrayList<>(keys.length);
        for (String key : keys) {
            if (Tools.isBlank(key)) {
                continue;
            }
            String value = (String) params.get(key);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(value)) {
                values.add(value);
            }
        }
        return values.stream().collect(Collectors.joining(","));
    }

    @Override
    public TableDataInfo<WfTaskVo> queryPageOwnProcessList(WfTaskQueryBo bo, PageQuery pageQuery) {
        String userId = SysUtil.getCurrentUserId();
        //查询流程实例
        QueryWrapper wrapper = Wrappers.query();
        if(!StringUtils.equals(userId, "admin")){
            wrapper.eq("RES.START_USER_ID_", userId);
        }
        //过滤条件
        wrapper.eq(StringUtils.isNotEmpty(bo.getStatus()), "RES.BUSINESS_STATUS_", bo.getStatus());
        wrapper.eq(StringUtils.isNotEmpty(bo.getProcessKey()), "DEF.KEY_", bo.getProcessKey());
        wrapper.eq(StringUtils.isNotEmpty(bo.getValues()), "bu.`values`", bo.getValues());
        //日期过滤
        SimpleDateFormat beginDataSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat endDataSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        if (bo.getCreateStartDate() != null) {
            wrapper.ge("RES.START_TIME_", beginDataSdf.format(bo.getCreateStartDate()));
        }
        if (bo.getCreateEndDate() != null) {
            wrapper.le("RES.START_TIME_", endDataSdf.format(bo.getCreateEndDate()));
        }
        if (bo.getFinishStartDate() != null) {
            wrapper.isNotNull("RES.END_TIME_");
            wrapper.ge("RES.END_TIME_", beginDataSdf.format(bo.getFinishStartDate()));
        }
        if (bo.getFinishEndDate() != null) {
            wrapper.isNotNull("RES.END_TIME_");
            wrapper.le("RES.END_TIME_", endDataSdf.format(bo.getFinishEndDate()));
        }
        //排序
        wrapper.orderByDesc("RES.START_TIME_");
        Page<WfTaskVo> result = wfTaskInstMapper.queryPageOwnProcessList(wrapper, pageQuery.build());
        Map<String, BpmnModel> bpmnModelMap = getBpmnModelMap(result.getRecords().stream().map(t -> t.getProcDefId()).collect(Collectors.toSet()));
        for (WfTaskVo record : result.getRecords()) {
            // 计算耗时
            if (Objects.nonNull(record.getProcEndTime())) {
                record.setDuration(DateUtils.getDatePoor(record.getProcEndTime(), record.getProcStartTime()));
            } else {
                record.setDuration(DateUtils.getDatePoor(DateUtils.getNowDate(), record.getProcStartTime()));
            }
            record.setCreateTime(record.getProcStartTime());
            record.setFinishTime(record.getProcEndTime());
            //查询出申请节点，确保只能查看申请节点的表单
            UserTask applyTask = ModelUtils.getApplyTask(bpmnModelMap.get(record.getProcDefId()));
            record.setTaskDefKey(applyTask.getId());
            //流程业务主键与名称
            List<WfBusiInfo> valuesNameList = wfBusiInfoMapper.findNameByValues(record.getProcInsId());
            record.setValues(valuesNameList.size()>0?valuesNameList.get(0).getValues():"");
            record.setValuesName(valuesNameList.size()>0?valuesNameList.get(0).getValuesName():"");
        }
        return TableDataInfo.build(result);
    }

    @Override
    public TableDataInfo<WfTaskVo> queryPageTodoProcessList(WfTaskQueryBo bo, PageQuery pageQuery) {
        List<WfTaskVo> flowList;
        if (SurrogateConstants.ENABLE.equals(bo.getSurrogateFlag())) {
            flowList = querySurrogateTaskList();
        } else {
            flowList = queryMyTodoTaskList();
        }

        //条件过滤
        Iterator<WfTaskVo> iterator = flowList.iterator();
        while (iterator.hasNext()) {
            WfTaskVo next = iterator.next();

            if (StringUtils.isNotEmpty(bo.getProcessKey())) {
                if (!StringUtils.equals(next.getProcDefKey(), bo.getProcessKey())) {
                    iterator.remove();
                    continue;
                }
            }
            if (StringUtils.isNotEmpty(bo.getApplyUser())) {
                if (!StringUtils.equals(next.getStartUserId(), bo.getApplyUser())) {
                    iterator.remove();
                    continue;
                }
            }
            if (bo.getCreateStartDate() != null) {
                int compare = DateUtil.compare(next.getProcStartTime(), bo.getCreateStartDate(), DatePattern.NORM_DATE_PATTERN);
                if (compare < 0) {
                    iterator.remove();
                    continue;
                }
            }

            if (bo.getCreateEndDate() != null) {
                int compare = DateUtil.compare(next.getProcStartTime(), bo.getCreateEndDate(), DatePattern.NORM_DATE_PATTERN);
                if (compare > 0) {
                    iterator.remove();
                    continue;
                }
            }

            if (StringUtils.isNotEmpty(bo.getValues())) {
                if (!StringUtils.equals(next.getValues(), bo.getValues())) {
                    iterator.remove();
                    continue;
                }
            }

            if(!StringUtils.equals(SysUtil.getCurrentUserId(), "admin")){
                // 获取系统参数：是否排除发起人为当前用户的待审核任务，默认为否
                List<SystemParam> sysparam = systemServiceFactory.createService().getSysParam("90000051112");
                if(sysparam != null && sysparam.size() > 0) {
                    SystemParam isExCurrUser = sysparam.get(0);

                    if (StringUtils.equals(isExCurrUser.getParavalue(), "1")) {
                        if (StringUtils.isNotEmpty(SysUtil.getCurrentUserId())) {
                            if (StringUtils.equals(next.getStartUserId(), SysUtil.getCurrentUserId())) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }

        }

        //手动分页
        Page<Object> build = pageQuery.build();
        Long current = build.getCurrent() - 1;
        Long size = build.getSize();
        List<WfTaskVo> pageList = ListUtil.page(current.intValue() < 0 ? 0 : current.intValue(), size.intValue(), flowList);
        Page<WfTaskVo> page = new Page<>();
        page.setTotal(flowList.size());
        page.setRecords(pageList);
        return TableDataInfo.build(page);
    }

    /**
     * 自己需要审批的任务
     *
     * @return
     */
    public List<WfTaskVo> queryMyTodoTaskList() {
        SysUser currentUserInfo = SysUtil.getCurrentUserInfo();
        String userId = currentUserInfo.getUserid();
        //获取角色
        List<String> roleList = systemServiceFactory.createService().findUserRoleByUserId(userId);
        //获取当前用户的下级机构
        if(Tools.isEmpty(currentUserInfo.getOrgno())){
            currentUserInfo.setOrgno(systemServiceFactory.createService().getOrg(userId).get(0).getOrgno());
        }
        List<SysOrg> lowerOrgs = systemServiceFactory.createService().getLowerOrgs(currentUserInfo.getOrgno());
        List<String> lowerOrgNo = lowerOrgs.stream().map(t -> t.getOrgno()).collect(Collectors.toList());
        //获取待审核任务
        List<Task> taskList = new ArrayList<>();
        if(StringUtils.equals(SysUtil.getCurrentUserId(), "admin")){
            //当为admin用户时，查询所有 程晓鹏 2025.01.08 modify
            taskList = taskService.createTaskQuery().active().includeIdentityLinks().orderByTaskCreateTime().desc().list();
        }else {
            taskList = taskService.createTaskQuery().active().taskCandidateOrAssigned(userId).taskCandidateGroupIn(roleList).includeIdentityLinks().orderByTaskCreateTime().desc().list();
        }

        List<WfTaskVo> flowList = new ArrayList<>();

        //避免在循环中重复查询
        //获取任务中的流程定义
        Set<String> procDefIds = taskList.stream().map(t -> t.getProcessDefinitionId()).collect(Collectors.toSet());
        //获取任务中的流程模型
        Map<String, BpmnModel> bpmnModelMap = getBpmnModelMap(procDefIds);
        //获取任务中的流程实例
        Map<String, HistoricProcessInstance> procInsMap = getProcInsMap(taskList);
        //获取流程实例的发起人
        List<SysUser> startUserIdList = systemServiceFactory.createService().findUserByIds(procInsMap.values().stream().map(t -> t.getStartUserId()).collect(Collectors.toSet()));
        for (Task task : taskList) {
            // 当前流程信息
            HistoricProcessInstance historicProcessInstance = procInsMap.get(task.getProcessInstanceId());
            //流程发起人
            SysUser startUser = startUserIdList.stream().filter(t -> t.getUserid().equals(historicProcessInstance.getStartUserId())).findAny().get();
            //流程业务主键与名称
            List<WfBusiInfo> valuesNameList = wfBusiInfoMapper.findNameByValues(task.getProcessInstanceId());
            //构建task视图
            WfTaskVo flowTask = buildTaskVo(task, historicProcessInstance, startUser,valuesNameList);
            //过滤任务
            filterTask(currentUserInfo, roleList, lowerOrgNo, flowList, bpmnModelMap, task, flowTask);
        }
        return flowList;
    }

    private void filterTask(SysUser currentUserInfo, List<String> roleList, List<String> lowerOrgNo, List<WfTaskVo> flowList, Map<String, BpmnModel> bpmnModelMap, Task task, WfTaskVo flowTask) {
        String userId = currentUserInfo.getUserid();
        //当前用户不能为流程上一节点用户
        /*if(checkUserByTask(task)){
            return;
        }*/
        //申请节点需要从业务系统发起请求来完成，所以过滤掉
        BpmnModel bpmnModel = bpmnModelMap.get(task.getProcessDefinitionId());
        UserTask applyTask = ModelUtils.getApplyTask(bpmnModel);
        if (applyTask.getId().equals(task.getTaskDefinitionKey())) {
            return;
        }
        UserTask userTask = (UserTask) FlowableUtils.findFlowElementByActivityId(bpmnModel, task.getTaskDefinitionKey());
        String roleType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.ROLE_TYPE);
        List<String> candidateUsers = userTask.getCandidateUsers();
        String assignee = task.getAssignee();
        //当前用户是审批人或者候选用户，无需处理
        if (StringUtils.equals(SysUtil.getCurrentUserId(), "admin") ||
                (userId.equals(assignee) || CollectionUtil.contains(candidateUsers, userId))) {
            flowList.add(flowTask);
            return;
        }

        //获取任务相关人员和角色
        List<? extends IdentityLinkInfo> identityLinks = task.getIdentityLinks();
        List<String> groupIds = identityLinks.stream().map(t -> t.getGroupId()).collect(Collectors.toList()).stream().filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        List<String> userIds = identityLinks.stream().map(t -> t.getUserId()).collect(Collectors.toList()).stream().filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        //如果当前用户是候选用户，不过滤
        if (CollectionUtil.contains(userIds, userId)) {
            flowList.add(flowTask);
            return;
        }
        // 当前用户是候选角色角色时，需要考虑上级机构、同级机构
        List<String> origin = new ArrayList<>();
        origin.addAll(groupIds);
        origin.retainAll(roleList);
        if (origin.size() > 0) {
            if (RoleTypeEnum.UPPER_ORG.getType().equals(roleType)) {
                //判断申请人的的机构是否为当前用户的下级机构
                if (lowerOrgNo.contains(flowTask.getStartUserOrgNo())) {
                    flowList.add(flowTask);
                }
                return;
            } else if (RoleTypeEnum.PEER_ORG.getType().equals(roleType)) {
                //判断申请人的的机构是否为当前用户的下级机构
                if (currentUserInfo.getOrgno().equals(flowTask.getStartUserOrgNo())) {
                    flowList.add(flowTask);
                }
                return;
            }
        }
        flowList.add(flowTask);
    }

    /**
     * 获取任务中的流程实例
     *
     * @param taskList
     */
    private Map<String, HistoricProcessInstance> getProcInsMap(List<Task> taskList) {
        if (CollectionUtil.isEmpty(taskList)) {
            return Collections.EMPTY_MAP;
        }
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().processInstanceIds(taskList.stream().map(Task::getProcessInstanceId).collect(Collectors.toSet())).list();
        return historicProcessInstanceList.stream().collect(Collectors.toMap(t -> t.getId(), t -> t));
    }

    /**
     * 获取任务中的流程模型
     *
     * @param procDefIds
     * @return
     */
    private Map<String, BpmnModel> getBpmnModelMap(Set<String> procDefIds) {
        Map<String, BpmnModel> bpmnModelMap = new HashMap<>();
        for (String procDefId : procDefIds) {
            BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
            bpmnModelMap.put(procDefId, bpmnModel);
        }
        return bpmnModelMap;
    }

    /**
     * 查询当前用户的转审批任务
     *
     * @return
     */
    private List<WfTaskVo> querySurrogateTaskList() {
        String userId = SysUtil.getCurrentUserId();
        LambdaQueryWrapper<WfSurrogate> lqw = Wrappers.lambdaQuery();
        lqw.eq(WfSurrogate::getSurrogate, userId);
        lqw.eq(WfSurrogate::getStatus, SurrogateConstants.ENABLE);
        lqw.le(WfSurrogate::getStartDate, DateUtils.getDate());
        lqw.ge(WfSurrogate::getEndDate, DateUtils.getDate());
        List<WfSurrogateVo> wfSurrogates = wfSurrogateMapper.selectVoList(lqw);

        List<WfTaskVo> retList = new ArrayList<>();
        for (WfSurrogateVo wfSurrogate : wfSurrogates) {
            //代理的流程
            String processKey = wfSurrogate.getProcessKey();
            //授权人
            String creator = wfSurrogate.getCreator();
            SysUser creatorInfo = systemServiceFactory.createService().getUserInfo(creator);
            //授权人角色
            List<String> roleIdList = systemServiceFactory.createService().findUserRoleByUserId(creator);
            //获取授权人的下级机构
            List<SysOrg> lowerOrgs = systemServiceFactory.createService().getLowerOrgs(creatorInfo.getOrgno());
            List<String> lowerOrgNo = lowerOrgs.stream().map(t -> t.getOrgno()).collect(Collectors.toList());
            //查询授权人的任务
            List<Task> taskList = taskService.createTaskQuery().active().processDefinitionKey(processKey).taskCandidateOrAssigned(creator).includeIdentityLinks().taskCandidateGroupIn(roleIdList).orderByTaskCreateTime().desc().list();
            //获取任务中的流程定义
            Set<String> procDefIds = taskList.stream().map(t -> t.getProcessDefinitionId()).collect(Collectors.toSet());
            //获取任务中的流程模型
            Map<String, BpmnModel> bpmnModelMap = getBpmnModelMap(procDefIds);
            //获取任务中的流程实例
            Map<String, HistoricProcessInstance> procInsMap = getProcInsMap(taskList);
            //获取流程实例的发起人
            List<SysUser> startUserIdList = systemServiceFactory.createService().findUserByIds(procInsMap.values().stream().map(t -> t.getStartUserId()).collect(Collectors.toSet()));
            for (Task task : taskList) {
                // 当前流程信息
                HistoricProcessInstance historicProcessInstance = procInsMap.get(task.getProcessInstanceId());
                SysUser startUser = startUserIdList.stream().filter(t -> t.getUserid().equals(historicProcessInstance.getStartUserId())).findAny().get();
                //流程业务主键与名称
                List<WfBusiInfo> valuesNameList = wfBusiInfoMapper.findNameByValues(task.getProcessInstanceId());
                WfTaskVo flowTask = buildTaskVo(task, historicProcessInstance, startUser,valuesNameList);
                //设置授权人
                flowTask.setAuthorizeName(wfSurrogate.getCreateName());
                //过滤任务
                filterTask(creatorInfo, roleIdList, lowerOrgNo, retList, bpmnModelMap, task, flowTask);
            }
        }
        return retList;
    }

    private WfTaskVo buildTaskVo(Task task, HistoricProcessInstance historicProcessInstance, SysUser startUser ,List<WfBusiInfo> valuesNameList) {
        WfTaskVo flowTask = new WfTaskVo();
        flowTask.setTaskId(task.getId());
        flowTask.setTaskDefKey(task.getTaskDefinitionKey());
        flowTask.setCreateTime(task.getCreateTime());
        flowTask.setProcDefId(task.getProcessDefinitionId());
        flowTask.setTaskName(task.getName());
        flowTask.setStartUserId(startUser.getUserid());
        flowTask.setStartUserName(startUser.getUsername());
        flowTask.setStartUserOrgNo(startUser.getOrgno());
        flowTask.setProcStartTime(historicProcessInstance.getStartTime());
        flowTask.setProcDefKey(historicProcessInstance.getProcessDefinitionKey());
        flowTask.setProcDefId(historicProcessInstance.getProcessDefinitionId());
        flowTask.setDeployId(historicProcessInstance.getDeploymentId());
        flowTask.setProcDefName(historicProcessInstance.getProcessDefinitionName());
        flowTask.setProcDefVersion(historicProcessInstance.getProcessDefinitionVersion());
        flowTask.setProcInsId(historicProcessInstance.getId());
        flowTask.setProcStatus(historicProcessInstance.getBusinessStatus());
        flowTask.setValues(valuesNameList.size()>0?valuesNameList.get(0).getValues():"");
        flowTask.setValuesName(valuesNameList.size()>0?valuesNameList.get(0).getValuesName():"");
        return flowTask;
    }

    @Override
    public TableDataInfo<WfTaskVo> queryPageFinishedProcessList(WfTaskQueryBo bo, PageQuery pageQuery) {
        String userId = SysUtil.getCurrentUserId();
        //当前用户已完成的任务
        QueryWrapper<?> wrapper = new QueryWrapper<>();
        if(!StringUtils.equals(userId, "admin")) {  //放开admin权限 查询全部  程晓鹏 2025.01.08 modify
            wrapper.eq("TASK.ASSIGNEE_", userId);
        }
        wrapper.isNotNull("TASK.END_TIME_");
        //过滤条件
        wrapper.eq(StringUtils.isNotEmpty(bo.getStatus()), "RES.BUSINESS_STATUS_", bo.getStatus());
        wrapper.eq(StringUtils.isNotEmpty(bo.getApplyUser()), "RES.START_USER_ID_", bo.getApplyUser());
        wrapper.eq(StringUtils.isNotEmpty(bo.getValues()), "bu.`values`", bo.getValues());
        //日期过滤
        SimpleDateFormat beginDataSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat endDataSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        if (bo.getCreateStartDate() != null) {
            wrapper.ge("RES.START_TIME_", beginDataSdf.format(bo.getCreateStartDate()));
        }
        if (bo.getCreateEndDate() != null) {
            wrapper.le("RES.START_TIME_", endDataSdf.format(bo.getCreateEndDate()));
        }
        if (bo.getFinishStartDate() != null) {
            wrapper.ge("RES.END_TIME_", beginDataSdf.format(bo.getFinishStartDate()));
        }
        if (bo.getFinishEndDate() != null) {
            wrapper.le("RES.END_TIME_", endDataSdf.format(bo.getFinishEndDate()));
        }

        wrapper.eq(StringUtils.isNotEmpty(bo.getProcessKey()), "DEF.KEY_", bo.getProcessKey());
        //过滤申请任务
        wrapper.and(QueryWrapper -> QueryWrapper.ne("TASK.DESCRIPTION_", TaskConstants.APPLY_TASK_ID).or().isNull("TASK.DESCRIPTION_"));
        //排序
        wrapper.orderByDesc("RES.START_TIME_", "TASK.END_TIME_");
        Page<WfTaskVo> result = wfTaskInstMapper.queryPageFinishedProcessList(wrapper, pageQuery.build());

        for (WfTaskVo record : result.getRecords()) {
            // 计算耗时
            if (Objects.nonNull(record.getProcEndTime())) {
                record.setDuration(DateUtils.getDatePoor(record.getProcEndTime(), record.getProcStartTime()));
            } else {
                record.setDuration(DateUtils.getDatePoor(DateUtils.getNowDate(), record.getProcStartTime()));
            }
            //流程业务主键与名称
            List<WfBusiInfo> valuesNameList = wfBusiInfoMapper.findNameByValues(record.getProcInsId());
            record.setValues(valuesNameList.size()>0?valuesNameList.get(0).getValues():"");
            record.setValuesName(valuesNameList.size()>0?valuesNameList.get(0).getValuesName():"");
        }
        //获取流程发起人
        if(!StringUtils.equals(userId, "admin")) { //放开admin权限 查询全部  程晓鹏 2025.01.08 modify
            AuthObjectUtil.complementUserInfo(result.getRecords(), MapUtil.builder(new HashMap<String, String>()).put(FieldUtil.noPrefix(WfTaskVo::getStartUserId), FieldUtil.noPrefix(WfTaskVo::getStartUserName)).build(), FieldUtil.noPrefix(SysUser::getUsername));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 扩展参数构建
     *
     * @param variables 扩展参数
     */
    private void buildProcessVariables(Map<String, Object> variables) {
        String userIdStr = SysUtil.getCurrentUserId();
        identityService.setAuthenticatedUserId(userIdStr);
        variables.put(TaskConstants.PROCESS_INITIATOR, userIdStr);
    }

    /**
     * 获取历史任务信息列表
     */
    @Override
    public List<WfTaskVo> historyTaskList(String procInsId) {
        //默认一个大日期，确保排序时在最后
        DateTime defaultData = DateUtil.offset(new Date(), DateField.YEAR, 100);
        //这里查出来的是已审批和下一个审批中的任务
        List<WfTaskVo> historyTaskList = wfTaskInstMapper.queryHistoryTaskList(procInsId, defaultData, CollUtil.newHashSet(BpmnXMLConstants.ELEMENT_TASK_USER, BpmnXMLConstants.ELEMENT_TASK_RECEIVE, BpmnXMLConstants.ELEMENT_TASK_MANUAL, BpmnXMLConstants.ELEMENT_TASK_SERVICE, BpmnXMLConstants.ELEMENT_TASK_SCRIPT));
        //流程实例
        HistoricProcessInstance hiProcIns = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInsId).includeProcessVariables().singleResult();
        //获取待审批任务的key
        Set<String> pendingApprovalTaskDefKeyList = new HashSet<>();
        for (WfTaskVo wfTaskVo : historyTaskList) {
            //完成时间为空，为待审批任务
            if (Objects.isNull(wfTaskVo.getFinishTime())) {
                pendingApprovalTaskDefKeyList.add(wfTaskVo.getTaskDefKey());
            }
        }
        //从待审核任务开始预测，得出之后的所有节点
        BpmnModel bpmnModel = repositoryService.getBpmnModel(hiProcIns.getProcessDefinitionId());
        List<FlowElement> allFlowElements = new ArrayList<>(bpmnModel.getMainProcess().getFlowElements());
        List<FlowElement> passElements = new ArrayList<>();
        for (String taskDefKey : pendingApprovalTaskDefKeyList) {
            FlowElement taskElement = FlowableUtils.findFlowElementByActivityId(bpmnModel, taskDefKey);
            iCalculateProcessService.getPassElementList(passElements, allFlowElements, taskElement, hiProcIns.getProcessVariables(), procInsId);
        }
        //过滤出任务节点
        List<FlowElement> taskElementList = passElements.stream().filter(curFlowElement -> curFlowElement instanceof UserTask || curFlowElement instanceof ReceiveTask || curFlowElement instanceof ManualTask || curFlowElement instanceof ServiceTask || curFlowElement instanceof ScriptTask).collect(Collectors.toList());
        //整理排序任务节点，得出待审批任务，以及之后的任务节点
        List<FlowElement> pendingApprovalFlowElementTaskList = iCalculateProcessService.sortCalculateList(taskElementList);
        //为了避免待审批任务出现重复，需要去掉待审批任务
        Iterator<FlowElement> iterator = pendingApprovalFlowElementTaskList.iterator();
        while (iterator.hasNext()) {
            FlowElement next = iterator.next();
            if (pendingApprovalTaskDefKeyList.contains(next.getId())) {
                iterator.remove();
            }
        }
        //处理审批人信息
        HashSet<String> allUserId = new HashSet<>();
        HashSet<String> allRoleId = new HashSet<>();
        //查询出审核中任务的候选用户、候选角色id
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(procInsId).includeIdentityLinks().unfinished().list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            List<? extends IdentityLinkInfo> identityLinks = historicTaskInstance.getIdentityLinks();
            List<String> groupIds = identityLinks.stream().map(t -> t.getGroupId()).filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
            List<String> userIds = identityLinks.stream().map(t -> t.getUserId()).filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
            allUserId.addAll(userIds);
            allRoleId.addAll(groupIds);
        }
        //查询出待审核任务的候选用户、候选角色id
        List<CalculatTaskVo> pendingApprovalTaskList = new ArrayList<>();
        for (FlowElement flowElement : pendingApprovalFlowElementTaskList) {
            CalculatTaskVo calculatTaskVo = iCalculateProcessService.getCalculatTaskVo(hiProcIns.getProcessVariables(), hiProcIns.getId(), flowElement);
            pendingApprovalTaskList.add(calculatTaskVo);
            allUserId.addAll(calculatTaskVo.getUserIds());
            allRoleId.addAll(calculatTaskVo.getRoleIds());
        }
        //根据用户、角色id查询出所有用户角色
        Map<String, SysUser> userMap = systemServiceFactory.createService().findUserByIds(allUserId).stream().collect(Collectors.toMap(t -> t.getUserid(), t -> t));
        Map<String, SysRole> roleMap = systemServiceFactory.createService().findRoleByIds(allRoleId).stream().collect(Collectors.toMap(t -> t.getRoleid(), t -> t));
        //将候选用户id、候选角色id转换为中文
        Map<String, String> candidateNameMap = getHistoryTaskCandidateName(list, userMap, roleMap);

        for (int i = 0; i < historyTaskList.size(); i++) {
            WfTaskVo taskVo = historyTaskList.get(i);
            //有消息提交人时，审批人为消息提交人
            if (StringUtils.isNotEmpty(taskVo.getMessageUserId())) {
                taskVo.setAssigneeId(taskVo.getMessageUserId());
            }
            //设置候选人
            if (StringUtils.isNotEmpty(taskVo.getTaskId())) {
                taskVo.setCandidate(candidateNameMap.get(taskVo.getTaskId()));
            }
            //当任务完成时间不为空时,或审批意见类型不为空，任务已完成
            if (Objects.nonNull(taskVo.getFinishTime()) || StringUtils.isNotEmpty(taskVo.getMessageType())) {
                taskVo.setFinishTime(taskVo.getMessageTime());
                taskVo.setTaskStatus(TaskStatusEnum.COMPLETED.getType());
            } else {
                taskVo.setTaskStatus(TaskStatusEnum.UNDER_APPROVAL.getType());
            }
            // 计算耗时
            if (Objects.nonNull(taskVo.getFinishTime()) && Objects.nonNull(taskVo.getCreateTime())) {
                long duration = taskVo.getFinishTime().getTime() - taskVo.getCreateTime().getTime();
                taskVo.setDuration(DateUtil.formatBetween(duration, BetweenFormatter.Level.SECOND));
            }
            //同一个任务中，上一条数据的完成时间，就是当前数据的创建时间
            if (i == 0) {
                //第一条数据不处理
                continue;
            }
            WfTaskVo preTaskVo = historyTaskList.get(i - 1);
            if (!StringUtils.equals(taskVo.getTaskId(), preTaskVo.getTaskId())) {
                //不同任务不处理
                continue;
            }
            taskVo.setCreateTime(preTaskVo.getFinishTime());
            // 计算耗时
            if (Objects.nonNull(taskVo.getFinishTime()) && Objects.nonNull(taskVo.getCreateTime())) {
                long duration = taskVo.getFinishTime().getTime() - taskVo.getCreateTime().getTime();
                taskVo.setDuration(DateUtil.formatBetween(duration, BetweenFormatter.Level.SECOND));
            }

        }
        for (CalculatTaskVo calculatTaskVo : pendingApprovalTaskList) {
            List<String> userIds = calculatTaskVo.getUserIds();
            List<String> roleIds = calculatTaskVo.getRoleIds();
            StringBuilder userStr = new StringBuilder();
            for (String userId : userIds) {
                SysUser sysUser = userMap.get(userId);
                userStr.append(sysUser.getUsername()).append(",");
            }
            StringBuilder roleStr = new StringBuilder();
            for (String roleId : roleIds) {
                SysRole sysRole = roleMap.get(roleId);
                roleStr.append(sysRole.getRolename()).append(",");
            }
            StringBuilder candidateStr = new StringBuilder();
            candidateStr.append(userStr).append(roleStr);
            WfTaskVo wfTaskVo = new WfTaskVo();
            wfTaskVo.setTaskDefKey(calculatTaskVo.getId());
            wfTaskVo.setTaskName(calculatTaskVo.getTaskName());
            wfTaskVo.setTaskStatus(TaskStatusEnum.PENDING_APPROVAL.getType());
            if (StringUtils.isNotEmpty(candidateStr)) {
                wfTaskVo.setCandidate(candidateStr.substring(0, candidateStr.lastIndexOf(",")));
            }
            historyTaskList.add(wfTaskVo);
        }
        AuthObjectUtil.complementUserInfo(historyTaskList, MapUtil.builder(new HashMap<String, String>()).put(FieldUtil.noPrefix(WfTaskVo::getAssigneeId), FieldUtil.noPrefix(WfTaskVo::getAssigneeName)).build(), FieldUtil.noPrefix(SysUser::getUsername));
        return historyTaskList;
    }

    private Map<String, String> getHistoryTaskCandidateName(List<HistoricTaskInstance> list, Map<String, SysUser> userMap, Map<String, SysRole> roleMap) {
        //拼接用户名、角色名
        HashMap<String, String> map = new HashMap<>();
        for (HistoricTaskInstance historicTaskInstance : list) {
            List<? extends IdentityLinkInfo> identityLinks = historicTaskInstance.getIdentityLinks();
            Set<String> groupIds = identityLinks.stream().map(t -> t.getGroupId()).filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toSet());
            Set<String> userIds = identityLinks.stream().map(t -> t.getUserId()).filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toSet());
            StringBuilder userStr = new StringBuilder();
            for (String userId : userIds) {
                SysUser sysUser = userMap.get(userId);
                userStr.append(sysUser.getUsername()).append(",");
            }
            StringBuilder roleStr = new StringBuilder();
            for (String roleId : groupIds) {
                SysRole sysRole = roleMap.get(roleId);
                roleStr.append(sysRole.getRolename()).append(",");
            }
            StringBuilder candidateStr = new StringBuilder();
            candidateStr.append(userStr).append(roleStr);
            if (StringUtils.isNotEmpty(candidateStr.toString())) {
                map.put(historicTaskInstance.getId(), candidateStr.substring(0, candidateStr.lastIndexOf(",")));
            }
        }
        return map;
    }

    @Override
    public WfFormConfVO getFormConf(String procDefId, String taskDefKey) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
        //开始节点的表单
        StartEvent startEvent = ModelUtils.getStartEvent(bpmnModel);
        String startEventFormKey = startEvent.getFormKey();
        String startEventFormType = startEvent.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_FORM_TYPE);
        String startEventBusiFormKey = startEvent.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_BUSI_FORM_KEY);
        String startEventBusiFormType = startEvent.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_BUSI_FORM_TYPE);
        //任务节点的表单
        UserTask userTask = (UserTask) FlowableUtils.findFlowElementByActivityId(bpmnModel, taskDefKey);
        String formKey = userTask.getFormKey();
        String formType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_FORM_TYPE);
        String busiFormKey = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_BUSI_FORM_KEY);
        String busiFormType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_BUSI_FORM_TYPE);
        //如果任务节点的表单为空，则取开始节点的表单
        formKey = StringUtils.isEmpty(formKey) ? startEventFormKey : formKey;
        formType = StringUtils.isEmpty(formType) ? startEventFormType : formType;
        busiFormKey = StringUtils.isEmpty(busiFormKey) ? startEventBusiFormKey : busiFormKey;
        busiFormType = StringUtils.isEmpty(busiFormType) ? startEventBusiFormType : busiFormType;

        WfFormConfVO build = WfFormConfVO.builder().taskDefKey(userTask.getId()).formKey(formKey).formType(formType).busiFormType(busiFormType).busiFormKey(busiFormKey).build();
        return build;
    }

    @Override
    public Map<String, Object> getFormData(String procInsId) {
        return this.getProcessVariables(procInsId);
    }

    @Override
    public Map<String, Object> getFormLabelInfo(String procInsId) {
        LambdaQueryWrapper<WfBusiInfo> queryWrapper = Wrappers.lambdaQuery(WfBusiInfo.class);
        queryWrapper.eq(WfBusiInfo::getProcessInstanceId, procInsId);
        WfBusiInfoVo wfBusiInfoVo = wfBusiInfoMapper.selectVoOne(queryWrapper);
        String labelInfo = wfBusiInfoVo.getLabelInfo();
        return JsonUtils.parseMap(labelInfo);
    }

    /**
     * 获取流程变量
     *
     * @param processInstanceId 流程实例ID
     * @return 流程变量
     */
    @Override
    public Map<String, Object> getProcessVariables(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .includeProcessVariables()
                .singleResult();
        return historicProcessInstance.getProcessVariables();
    }

    /**
     * 根据流程实例判断当前用户节点审批人与上一节点是否一致
     *
     * @param task 请求实体参数
     */
    public Boolean checkUserByTask(Task task) {
        UserTask taskElement = (UserTask) FlowableUtils.findFlowElementByActivityId(task.getProcessDefinitionId(), task.getTaskDefinitionKey(), repositoryService);
        List<SequenceFlow> incomingFlows = taskElement.getIncomingFlows();
        for (SequenceFlow incomingFlow:incomingFlows) {
            //获取源节点，一个目标节点只可能有一个源节点
            FlowElement sourceFlowElement = incomingFlow.getSourceFlowElement();
            return isSameUser(sourceFlowElement);
        }
        return false;
    }

    private boolean isSameUser ( FlowElement sourceFlowElement){
        //源节点为用户节点，则获取该节点审批的用户ID与系统登入用户ID比对
        if (sourceFlowElement instanceof  UserTask){
            //节点为用户
            UserTask sourceTask = (UserTask) sourceFlowElement;
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskDefinitionKey(sourceTask.getId()).orderByHistoricTaskInstanceEndTime().finished().desc().list();
            HistoricTaskInstance historicTaskInstance = list.get(0);
            String assignee = historicTaskInstance.getAssignee();
            String userIdStr = SysUtil.getCurrentUserId();
            if (assignee.equals(userIdStr)) {
                return true;
            }
        }else if(sourceFlowElement instanceof Gateway){
            //节点为网关
            Gateway sourceFlowElementGateWay =(Gateway) sourceFlowElement;
            List<SequenceFlow> incomingFlowGateWays = sourceFlowElementGateWay.getIncomingFlows();
            for (SequenceFlow incomingFlowGateWay:incomingFlowGateWays) {
                //获取源节点
                FlowElement sourceFlowElement1 = incomingFlowGateWay.getSourceFlowElement();
                //递归调用
                isSameUser(sourceFlowElement1);
            }
        }
        return false;
    }
}
