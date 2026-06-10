package com.kayak.web.workflow.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.constant.ProcessConstants;
import com.kayak.common.constant.TaskConstants;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.exception.WorkflowException;
import com.kayak.factory.FlowServiceFactory;
import com.kayak.utils.*;
import com.kayak.utils.flow.FlowableUtils;
import com.kayak.utils.flow.ModelUtils;
import com.kayak.web.workflow.domain.WfEnv;
import com.kayak.web.workflow.domain.WfEnvItem;
import com.kayak.web.workflow.domain.WfFormField;
import com.kayak.web.workflow.domain.bo.ProcessConfigBo;
import com.kayak.web.workflow.domain.bo.WfModelBo;
import com.kayak.web.workflow.domain.bo.WfRejectTaskBo;
import com.kayak.web.workflow.domain.dto.WfMetaInfoDto;
import com.kayak.web.workflow.domain.vo.WfModelVo;
import com.kayak.web.workflow.domain.vo.WfTaskVo;
import com.kayak.web.workflow.mapper.WfEnvItemMapper;
import com.kayak.web.workflow.mapper.WfEnvMapper;
import com.kayak.web.workflow.mapper.WfFormFieldMapper;
import com.kayak.web.workflow.service.IWfModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author yuanjinqiao
 * @createTime 2022/6/21 9:11
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class WfModelServiceImpl extends FlowServiceFactory implements IWfModelService {
    @Autowired
    private WfEnvMapper wfEnvMapper;

    @Autowired
    private WfEnvItemMapper wfEnvItemMapper;

    @Autowired
    private WfFormFieldMapper wfFormFieldMapper;

    @Override
    public TableDataInfo<WfModelVo> list(WfModelBo modelBo, PageQuery pageQuery) {
        ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByCreateTime().desc();
        // 构建查询条件
        if (StringUtils.isNotBlank(modelBo.getModelKey())) {
            modelQuery.modelKey(modelBo.getModelKey());
        }
        if (StringUtils.isNotBlank(modelBo.getModelName())) {
            modelQuery.modelNameLike("%" + modelBo.getModelName() + "%");
        }
        // 执行查询
        long pageTotal = modelQuery.count();
        if (pageTotal <= 0) {
            return TableDataInfo.build();
        }
        List<Model> modelList = modelQuery.listPage(pageQuery.getStart(), pageQuery.getLimit());
        List<WfModelVo> modelVoList = new ArrayList<>(modelList.size());
        modelList.forEach(model -> {
            WfModelVo modelVo = new WfModelVo();
            modelVo.setModelId(model.getId());
            modelVo.setModelName(model.getName());
            modelVo.setModelKey(model.getKey());
            modelVo.setCategory(model.getCategory());
            modelVo.setCreateTime(model.getCreateTime());
            modelVo.setVersion(model.getVersion());
            WfMetaInfoDto metaInfo = JsonUtils.parseObject(model.getMetaInfo(), WfMetaInfoDto.class);
            if (metaInfo != null) {
                modelVo.setDescription(metaInfo.getDescription());
                modelVo.setFormType(metaInfo.getFormType());
                modelVo.setFormId(metaInfo.getFormId());
                modelVo.setCreateUserName(metaInfo.getCreateUser());
                modelVo.setUpdateTime(metaInfo.getUpdateTime());
                modelVo.setUpdateUserName(metaInfo.getUpdateUserName());
            }
            modelVoList.add(modelVo);
        });
        Page<WfModelVo> page = new Page<>();
        page.setRecords(modelVoList);
        page.setTotal(pageTotal);
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<WfModelVo> historyList(WfModelBo modelBo, PageQuery pageQuery) {
        ModelQuery modelQuery = repositoryService.createModelQuery()
                .modelKey(modelBo.getModelKey())
                .orderByModelVersion()
                .desc();
        // 执行查询（不显示最新版，-1）
        long pageTotal = modelQuery.count() - 1;
        if (pageTotal <= 0) {
            return TableDataInfo.build();
        }
        List<Model> modelList = modelQuery.listPage(pageQuery.getStart(), pageQuery.getLimit());
        List<WfModelVo> modelVoList = new ArrayList<>(modelList.size());
        modelList.forEach(model -> {
            WfModelVo modelVo = new WfModelVo();
            modelVo.setModelId(model.getId());
            modelVo.setModelName(model.getName());
            modelVo.setModelKey(model.getKey());
            modelVo.setCategory(model.getCategory());
            modelVo.setCreateTime(model.getCreateTime());
            modelVo.setVersion(model.getVersion());
            WfMetaInfoDto metaInfo = JsonUtils.parseObject(model.getMetaInfo(), WfMetaInfoDto.class);
            if (metaInfo != null) {
                modelVo.setDescription(metaInfo.getDescription());
                modelVo.setFormType(metaInfo.getFormType());
                modelVo.setFormId(metaInfo.getFormId());
                modelVo.setUpdateUserName(metaInfo.getUpdateUserName());
                modelVo.setUpdateTime(metaInfo.getUpdateTime());
            }
            modelVoList.add(modelVo);
        });
        Page<WfModelVo> page = new Page<>();
        page.setRecords(modelVoList);
        page.setTotal(pageTotal);
        return TableDataInfo.build(page);
    }

    @Override
    public WfModelVo getModel(String modelId) {
        // 获取流程模型
        Model model = repositoryService.getModel(modelId);
        if (ObjectUtil.isNull(model)) {
            throw new RuntimeException("流程模型不存在！");
        }
        // 获取流程图
        String bpmnXml = queryBpmnXmlById(modelId);
        WfModelVo modelVo = new WfModelVo();
        modelVo.setModelId(model.getId());
        modelVo.setModelName(model.getName());
        modelVo.setModelKey(model.getKey());
        modelVo.setCategory(model.getCategory());
        modelVo.setCreateTime(model.getCreateTime());
        modelVo.setVersion(model.getVersion());
        modelVo.setBpmnXml(bpmnXml);
        WfMetaInfoDto metaInfo = JsonUtils.parseObject(model.getMetaInfo(), WfMetaInfoDto.class);
        if (metaInfo != null) {
            modelVo.setDescription(metaInfo.getDescription());
            modelVo.setFormType(metaInfo.getFormType());
            modelVo.setFormId(metaInfo.getFormId());
        }
        return modelVo;
    }

    @Override
    public String queryBpmnXmlById(String modelId) {
        byte[] bpmnBytes = repositoryService.getModelEditorSource(modelId);
        return StrUtil.utf8Str(bpmnBytes);
    }

    @Override
    public WfModelBo insertModel(WfModelBo modelBo) {
        long count = repositoryService.createModelQuery().modelKey(modelBo.getModelKey()).count();
        if (count > 0) {
            throw new WorkflowException("模型标识已存在");
        }
        Model model = repositoryService.newModel();
        model.setName(modelBo.getModelName());
        model.setKey(modelBo.getModelKey());
        String metaInfo = buildMetaInfo(null, modelBo);
        model.setMetaInfo(metaInfo);
        // 保存流程模型
        repositoryService.saveModel(model);
        modelBo.setModelId(model.getId());
        return modelBo;
    }

    @Override
    public void updateModel(WfModelBo modelBo) {
        // 根据模型Key查询模型信息
        Model model = repositoryService.getModel(modelBo.getModelId());
        if (ObjectUtil.isNull(model)) {
            throw new RuntimeException("流程模型不存在！");
        }
        WfMetaInfoDto metaInfoDto = JsonUtils.parseObject(model.getMetaInfo(), WfMetaInfoDto.class);
        String metaInfo = buildMetaInfo(metaInfoDto, modelBo);
        model.setMetaInfo(metaInfo);
        model.setName(modelBo.getModelName());
        // 保存流程模型
        repositoryService.saveModel(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveModel(WfModelBo modelBo) {
        // 查询模型信息
        Model model = repositoryService.getModel(modelBo.getModelId());
        if (ObjectUtil.isNull(model)) {
            throw new RuntimeException("流程模型不存在！");
        }
        BpmnModel bpmnModel = ModelUtils.getBpmnModel(modelBo.getBpmnXml());
        String id = bpmnModel.getMainProcess().getId();
        String key = model.getKey();
        if (!StringUtils.equals(id, key)) {
            throw new RuntimeException("流程ID与模型标识不一致！");
        }
        if (ObjectUtil.isEmpty(bpmnModel)) {
            throw new RuntimeException("获取模型设计失败！");
        }
        String processName = bpmnModel.getMainProcess().getName();
        // 获取开始节点
        StartEvent startEvent = ModelUtils.getStartEvent(bpmnModel);
        if (ObjectUtil.isNull(startEvent)) {
            throw new RuntimeException("开始节点不存在，请检查流程设计是否有误！");
        }

        //判断回调报文校验是否存在
        Process mainProcess = bpmnModel.getMainProcess();
        String extensionElementText = ModelUtils.getExtensionElementText(mainProcess, ProcessConstants.PROCESS_CALLBACK_VALIDATE);
        if (StringUtils.isEmpty(extensionElementText)) {
            throw new WorkflowException("回调报文校验不存在！");
        }

        Model newModel;
        WfMetaInfoDto metaInfoDto = JsonUtils.parseObject(model.getMetaInfo(), WfMetaInfoDto.class);
        String metaInfo = buildMetaInfo(metaInfoDto, modelBo);
        if (Boolean.TRUE.equals(modelBo.getNewVersion())) {
            newModel = repositoryService.newModel();
            newModel.setName(processName);
            newModel.setKey(model.getKey());
            newModel.setCategory(model.getCategory());
            newModel.setMetaInfo(metaInfo);
            newModel.setVersion(model.getVersion() + 1);
        } else {
            newModel = model;
            // 设置流程名称
            newModel.setMetaInfo(metaInfo);
            newModel.setName(processName);
        }
        // 保存流程模型
        repositoryService.saveModel(newModel);
        // 保存 BPMN XML
        repositoryService.addModelEditorSource(newModel.getId(), StrUtil.utf8Bytes(modelBo.getBpmnXml()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void latestModel(String modelId) {
        // 获取流程模型
        Model model = repositoryService.getModel(modelId);
        if (ObjectUtil.isNull(model)) {
            throw new RuntimeException("流程模型不存在！");
        }
        String bpmnXml = queryBpmnXmlById(modelId);
        Integer latestVersion = repositoryService.createModelQuery()
                .modelKey(model.getKey())
                .latestVersion()
                .singleResult()
                .getVersion();
        if (model.getVersion().equals(latestVersion)) {
            throw new RuntimeException("当前版本已是最新版！");
        }
        Model newModel = repositoryService.newModel();
        newModel.setName(model.getName());
        newModel.setKey(model.getKey());
        newModel.setCategory(model.getCategory());
        newModel.setMetaInfo(model.getMetaInfo());
        newModel.setVersion(latestVersion + 1);
        // 保存流程模型
        repositoryService.saveModel(newModel);
        // 保存 BPMN XML
        repositoryService.addModelEditorSource(newModel.getId(), StrUtil.utf8Bytes(bpmnXml));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Collection<String> ids) {
        ids.forEach(id -> {
            Model model = repositoryService.getModel(id);
            if (ObjectUtil.isNull(model)) {
                throw new RuntimeException("流程模型不存在！");
            }
            repositoryService.deleteModel(id);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deployModel(String modelId) {
        // 获取流程图
        String bpmnXml = queryBpmnXmlById(modelId);
        if (StringUtils.isEmpty(bpmnXml)) {
            throw new WorkflowException("流程模型为空，请绘制流程模型！");
        }
        BpmnModel bpmnModel = ModelUtils.getBpmnModel(bpmnXml);
        // 获取流程模型
        Model model = repositoryService.getModel(modelId);
        if (ObjectUtil.isNull(model)) {
            throw new WorkflowException("流程模型不存在！");
        }
        // 获取开始节点
        StartEvent startEvent = ModelUtils.getStartEvent(bpmnModel);
        if (ObjectUtil.isNull(startEvent)) {
            throw new WorkflowException("开始节点不存在，请检查流程设计是否有误！");
        }
        String processName = model.getName() + ProcessConstants.SUFFIX;

        //创建申请人用户任务节点
        //createApplyUserTask(bpmnModel);

        // 部署流程
        Deployment deployment = repositoryService.createDeployment()
                .name(model.getName())
                .key(model.getKey())
                .addString(processName, bpmnXml)
                //.addBpmnModel(processName, bpmnModel)
                .category(model.getCategory())
                .deploy();

        return true;
    }

    @Override
    public List<WfTaskVo> getRejectTaskList(WfRejectTaskBo bo) {
        BpmnModel bpmnModel = ModelUtils.getBpmnModel(bo.getBpmnXml());
        UserTask source = (UserTask) FlowableUtils.findFlowElementByActivityId(bpmnModel, bo.getTaskDefKey());
        List<WfTaskVo> rejectTaskListByTask = getRejectTaskListByTask(source);
        return rejectTaskListByTask;
    }

    @Override
    public List<WfTaskVo> getRejectTaskListByTask(UserTask source) {
        // 获取节点的所有路线
        List<List<UserTask>> roads = FlowableUtils.findRoad(source, null, null, null);
        // 可回退的节点列表
        List<UserTask> userTaskList = new ArrayList<>();
        for (List<UserTask> road : roads) {
            if (userTaskList.size() == 0) {
                // 还没有可回退节点直接添加
                userTaskList = road;
            } else {
                // 如果已有回退节点，则比对取交集部分
                userTaskList.retainAll(road);
            }
        }
        ArrayList<WfTaskVo> wfTaskVos = new ArrayList<>();
        for (UserTask userTask : userTaskList) {
            Boolean isSequential = FlowableUtils.iteratorCheckSequentialReferTarget(source, userTask.getId(), null, null);
            if (isSequential) {
                WfTaskVo wfTaskVo = new WfTaskVo();
                wfTaskVo.setTaskName(userTask.getName());
                wfTaskVo.setTaskDefKey(userTask.getId());
                wfTaskVos.add(wfTaskVo);
            }
        }
        return wfTaskVos;
    }

    @Override
    public void importConfig(ProcessConfigBo bo) {
        WfEnv env = bo.getEnv();
        List<WfEnvItem> envItems = bo.getEnvItems();
        List<WfFormField> formFields = bo.getFormFields();
        wfEnvMapper.insertOrUpdate(env);
        wfEnvItemMapper.insertOrUpdateBatch(envItems);
        wfFormFieldMapper.insertOrUpdateBatch(formFields);
    }

    private void createApplyUserTask(BpmnModel bpmnModel) {
        Process process = bpmnModel.getMainProcess();

        //添加提交人节点 方便驳回
        UserTask applytask = new UserTask();
        applytask.setId(TaskConstants.APPLY_TASK_ID);
        applytask.setAssignee("${" + TaskConstants.PROCESS_INITIATOR + "}");
        applytask.setName(TaskConstants.APPLY_TASK_NAME);
        //添加审批节点
        process.addFlowElement(applytask);

        //修改开始节点 信息
        StartEvent start = process.findFlowElementsOfType(StartEvent.class).get(0);

        //修改连线信息 将 提交人节点 连到 下个节点
        process.findFlowElementsOfType(SequenceFlow.class).forEach(sequenceFlow -> {
            if (sequenceFlow.getSourceRef().equals(start.getId())) {
                sequenceFlow.setSourceRef(applytask.getId());
            }
        });
        //修改连线信息 将 开始节点 连到 提交人节点
        SequenceFlow startToApplyFlow = new SequenceFlow(start.getId(), applytask.getId());
        startToApplyFlow.setId(start.getId() + "To" + applytask.getId());
        process.addFlowElement(startToApplyFlow);

        //设置提交人节点 样式
        GraphicInfo applyGraphic = null;
        Map<String, GraphicInfo> locationMap = bpmnModel.getLocationMap();
        //平移X轴
        int panX = 155;
        for (String key : locationMap.keySet()) {
            if (key.equals(start.getId())) {
                GraphicInfo info = locationMap.get(key);
                applyGraphic = new GraphicInfo(info.getX() + 80, info.getY() - 12, 55.0, 105.0);
            } else {
                locationMap.get(key).setX(locationMap.get(key).getX() + panX);
            }

        }
        if (applyGraphic != null) {
            //手动添加 提交人节点 样式
            locationMap.put(applytask.getId(), applyGraphic);
            //手动 重置连线信息样式
            bpmnModel.getFlowLocationMap().forEach((key, flowLocations) -> flowLocations.forEach(graphicInfo -> graphicInfo.setX(graphicInfo.getX() + panX)));
            //添加开始节点到提交人 连线信息 样式
            List<GraphicInfo> graphicInfos = new ArrayList<>();
            GraphicInfo sourceInfo = locationMap.get(startToApplyFlow.getSourceRef());
            graphicInfos.add(new GraphicInfo(sourceInfo.getX() + sourceInfo.getWidth(), sourceInfo.getY() + (sourceInfo.getHeight() / 2)));
            GraphicInfo targetInfo = locationMap.get(startToApplyFlow.getTargetRef());
            graphicInfos.add(new GraphicInfo(targetInfo.getX(), targetInfo.getY() + (targetInfo.getHeight() / 2)));
            bpmnModel.getFlowLocationMap().put(startToApplyFlow.getId(), graphicInfos);
        }
    }

    /**
     * 构建模型扩展信息
     *
     * @return
     */
    private String buildMetaInfo(WfMetaInfoDto metaInfo, WfModelBo modelBo) {
        WfMetaInfoDto meta;
        if (metaInfo == null) {
            //创建
            meta = new WfMetaInfoDto();
            meta.setCreateUser(SysUtil.getCurrentUserUsername());
        } else {
            //修改
            meta = metaInfo;
        }
        meta.setDescription(modelBo.getDescription());
        meta.setUpdateTime(new Date());
        meta.setUpdateUserName(SysUtil.getCurrentUserUsername());
        return JsonUtils.toJsonString(meta);
    }
}
