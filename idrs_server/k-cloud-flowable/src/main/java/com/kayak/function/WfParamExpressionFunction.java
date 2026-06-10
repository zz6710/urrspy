package com.kayak.function;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kayak.common.constant.FieldConstants;
import com.kayak.common.constant.ProcessConstants;
import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.StringUtils;
import com.kayak.utils.flow.ModelUtils;
import com.kayak.utils.spring.ApplicationContextUtils;
import com.kayak.web.workflow.domain.WfEnvItem;
import com.kayak.web.workflow.mapper.WfEnvItemMapper;
import com.kayak.web.workflow.service.IWfParamService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.constant.FieldConstant;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.api.variable.VariableContainer;
import org.flowable.common.engine.impl.el.VariableContainerWrapper;
import org.flowable.common.engine.impl.el.function.AbstractFlowableVariableExpressionFunction;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuanjinqiao
 * @description 自定义解析流程参数表达式
 * @create 2022-10-12 09:22
 **/
@Slf4j
@Component
public class WfParamExpressionFunction extends AbstractFlowableVariableExpressionFunction {

    @Autowired
    private IWfParamService wfParamService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private WfEnvItemMapper wfEnvItemMapper;
    private static WfParamExpressionFunction paramExpressionFunction;

    @PostConstruct
    public void init() {
        paramExpressionFunction = this;
    }

    final static String FUNCTION_NAME = "parse";

    public WfParamExpressionFunction() {
        super(FUNCTION_NAME);
    }

    /**
     * 解析流程参数
     *
     * @param paramName
     * @return
     */
    public static Object parse(VariableContainer variableContainer, String paramName) throws Exception {
        if (variableContainer instanceof VariableContainerWrapper) {
            //流程预测
            VariableContainerWrapper variableContainerWrapper = (VariableContainerWrapper) variableContainer;
            Class<? extends VariableContainerWrapper> aClass1 = variableContainerWrapper.getClass();
            Field field = aClass1.getDeclaredField("variables");
            field.setAccessible(true);
            Map<String, Object> variables = (Map<String, Object>) field.get(variableContainerWrapper);
            String processKey = variables.get(FieldConstants.PROCESS_KEY).toString();
            RepositoryService repositoryService = ApplicationContextUtils.getApplicationContext().getBean(RepositoryService.class);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey).latestVersion().singleResult();
            BpmnModel bpmnModel = paramExpressionFunction.repositoryService.getBpmnModel(processDefinition.getId());
            String envId = ModelUtils.getExtensionElementText(bpmnModel.getMainProcess(), ProcessConstants.ENV);
            LambdaQueryWrapper<WfEnvItem> lqw = Wrappers.lambdaQuery();
            lqw.eq(WfEnvItem::getEnvId, envId);
            lqw.eq(WfEnvItem::getItemKey, paramName);
            List<WfEnvItem> wfEnvItems = paramExpressionFunction.wfEnvItemMapper.selectList(lqw);
            if (CollectionUtil.isEmpty(wfEnvItems)) {
                throw new WorkflowException("找不到流程参数【" + paramName + "】");
            }
            return paramExpressionFunction.wfParamService.parseWfParam(wfEnvItems.get(0).getEnvItemId().toString(), variables, null);
        } else if (variableContainer instanceof ExecutionEntityImpl) {
            //流程中
            ExecutionEntityImpl execution = (ExecutionEntityImpl) variableContainer;
            BpmnModel bpmnModel = paramExpressionFunction.repositoryService.getBpmnModel(execution.getProcessDefinitionId());
            String envId = ModelUtils.getExtensionElementText(bpmnModel.getMainProcess(), ProcessConstants.ENV);
            LambdaQueryWrapper<WfEnvItem> lqw = Wrappers.lambdaQuery();
            lqw.eq(WfEnvItem::getEnvId, envId);
            lqw.eq(WfEnvItem::getItemKey, paramName);
            List<WfEnvItem> wfEnvItems = paramExpressionFunction.wfEnvItemMapper.selectList(lqw);
            if (CollectionUtil.isEmpty(wfEnvItems)) {
                throw new WorkflowException("找不到流程参数【" + paramName + "】");
            }
            return paramExpressionFunction.wfParamService.parseWfParam(wfEnvItems.get(0).getEnvItemId().toString(), execution);
        } else {
            throw new WorkflowException("流程参数无法解析【" + paramName + "】");
        }

    }

}


