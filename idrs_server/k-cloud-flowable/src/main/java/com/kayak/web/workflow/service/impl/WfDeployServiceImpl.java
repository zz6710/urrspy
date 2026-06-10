package com.kayak.web.workflow.service.impl;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.constant.SurrogateConstants;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.StringUtils;
import com.kayak.web.workflow.domain.bo.WfProcessBo;
import com.kayak.web.workflow.domain.bo.WfProcessStateBo;
import com.kayak.web.workflow.domain.vo.WfDeployVo;
import com.kayak.web.workflow.service.IWfDeployService;
import lombok.RequiredArgsConstructor;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessMigrationService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuanjinqiao
 * @createTime 2022/6/30 9:04
 */
@RequiredArgsConstructor
@Service
public class WfDeployServiceImpl implements IWfDeployService {

    private final RepositoryService repositoryService;

    private final ProcessMigrationService processMigrationService;

    private final HistoryService historyService;

    @Override
    public TableDataInfo<WfDeployVo> queryPageList(WfProcessBo processBo, PageQuery pageQuery) {
        // 流程定义列表数据查询
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .orderByProcessDefinitionKey()
                .asc();
        if (StringUtils.isNotBlank(processBo.getProcessKey())) {
            processDefinitionQuery.processDefinitionKeyLike("%" + processBo.getProcessKey() + "%");
        }
        if (StringUtils.isNotBlank(processBo.getProcessName())) {
            processDefinitionQuery.processDefinitionNameLike("%" + processBo.getProcessName() + "%");
        }
        if (StringUtils.isNotBlank(processBo.getState())) {
            if (SuspensionState.ACTIVE.toString().equals(processBo.getState())) {
                processDefinitionQuery.active();
            } else if (SuspensionState.SUSPENDED.toString().equals(processBo.getState())) {
                processDefinitionQuery.suspended();
            }
        }
        long pageTotal = processDefinitionQuery.count();
        if (pageTotal <= 0) {
            return TableDataInfo.build();
        }
        List<ProcessDefinition> definitionList = processDefinitionQuery.listPage(pageQuery.getStart(), pageQuery.getLimit());

        List<WfDeployVo> deployVoList = new ArrayList<>(definitionList.size());
        for (ProcessDefinition processDefinition : definitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            long count = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinition.getId()).count();
            WfDeployVo vo = new WfDeployVo();
            vo.setDeleteFlag(count>0? SurrogateConstants.DISENABLE:SurrogateConstants.ENABLE);
            vo.setDefinitionId(processDefinition.getId());
            vo.setProcessKey(processDefinition.getKey());
            vo.setProcessName(processDefinition.getName());
            vo.setVersion(processDefinition.getVersion());
            vo.setCategory(processDefinition.getCategory());
            vo.setDeploymentId(processDefinition.getDeploymentId());
            vo.setSuspended(processDefinition.isSuspended());
            // 流程部署信息
            vo.setCategory(deployment.getCategory());
            vo.setDeploymentTime(deployment.getDeploymentTime());
            deployVoList.add(vo);
        }
        Page<WfDeployVo> page = new Page<>();
        page.setRecords(deployVoList);
        page.setTotal(pageTotal);
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<WfDeployVo> queryPublishList(String processKey, PageQuery pageQuery) {
        // 创建查询条件
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .orderByProcessDefinitionVersion()
                .desc();
        long pageTotal = processDefinitionQuery.count();
        if (pageTotal <= 0) {
            return TableDataInfo.build();
        }
        // 根据查询条件，查询所有版本
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery
                .listPage(pageQuery.getStart(), pageQuery.getLimit());
        List<WfDeployVo> deployVoList = processDefinitionList.stream().map(item -> {
            long count = historyService.createHistoricProcessInstanceQuery().processDefinitionId(item.getId()).count();
            WfDeployVo vo = new WfDeployVo();
            vo.setDeleteFlag(count>0? SurrogateConstants.DISENABLE:SurrogateConstants.ENABLE);
            vo.setDefinitionId(item.getId());
            vo.setProcessKey(item.getKey());
            vo.setProcessName(item.getName());
            vo.setVersion(item.getVersion());
            vo.setCategory(item.getCategory());
            vo.setDeploymentId(item.getDeploymentId());
            vo.setSuspended(item.isSuspended());
            return vo;
        }).collect(Collectors.toList());
        Page<WfDeployVo> page = new Page<>();
        page.setRecords(deployVoList);
        page.setTotal(pageTotal);
        return TableDataInfo.build(page);
    }

    /**
     * 激活或挂起流程
     */
    @Override
    public void updateState(WfProcessStateBo wfProcessStateBo) {
        String state = wfProcessStateBo.getState();
        String definitionId = wfProcessStateBo.getDefinitionId();
        if (SuspensionState.ACTIVE.toString().equals(state)) {
            // 激活
            repositoryService.activateProcessDefinitionById(definitionId, true, null);
        } else if (SuspensionState.SUSPENDED.toString().equals(state)) {
            // 挂起
            repositoryService.suspendProcessDefinitionById(definitionId, true, null);
        }
    }

    @Override
    public String queryBpmnXmlById(String definitionId) {
        InputStream inputStream = repositoryService.getProcessModel(definitionId);
        try {
            return IoUtil.readUtf8(inputStream);
        } catch (IORuntimeException exception) {
            throw new RuntimeException("加载xml文件异常");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> deployIds) {
        for (String deployId : deployIds) {
            repositoryService.deleteDeployment(deployId, true);
        }
    }

    @Override
    public void override(String fromProcessDefId, String toProcessDefId) {
        if(fromProcessDefId.equals(toProcessDefId)){
            throw  new WorkflowException("流程定义相同，不能覆盖");
        }
        // 迁移验证
        boolean migrationValid = processMigrationService.createProcessInstanceMigrationBuilder()
                .migrateToProcessDefinition(toProcessDefId)
                .validateMigrationOfProcessInstances(fromProcessDefId)
                .isMigrationValid();
        if (!migrationValid) {
            // 验证不通过
            throw new WorkflowException("流程定义差异过大不满足在途流程的迁移，请修改流程图");
        }
        // 验证通过执行迁移
        processMigrationService.createProcessInstanceMigrationBuilder()
                .migrateToProcessDefinition(toProcessDefId)
                .migrateProcessInstances(fromProcessDefId);
    }
}
