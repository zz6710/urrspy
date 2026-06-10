/*
 * Copyright 2006-2009 Odysseus Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.common.engine.impl.de.odysseus.el.tree.impl.ast;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kayak.common.constant.FieldConstants;
import com.kayak.common.constant.ProcessConstants;
import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.StringUtils;
import com.kayak.utils.flow.ModelUtils;
import com.kayak.utils.spring.ApplicationContextUtils;
import com.kayak.web.workflow.domain.WfBusiInfo;
import com.kayak.web.workflow.domain.WfEnvItem;
import com.kayak.web.workflow.mapper.WfBusiInfoMapper;
import com.kayak.web.workflow.mapper.WfEnvItemMapper;
import com.kayak.web.workflow.service.impl.WfParamServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.api.variable.VariableContainer;
import org.flowable.common.engine.impl.de.odysseus.el.misc.LocalMessages;
import org.flowable.common.engine.impl.de.odysseus.el.tree.Bindings;
import org.flowable.common.engine.impl.el.VariableContainerWrapper;
import org.flowable.common.engine.impl.javax.el.*;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.repository.ProcessDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 重写该类，覆盖flowable的代码
 * 主要新增parseFormAndParam()方法用于解析表单参数和流程参数
 */
@Slf4j
public abstract class AstProperty extends AstNode {
    protected final AstNode prefix;
    protected final boolean lvalue;
    protected final boolean strict; // allow null as property value?
    protected final boolean ignoreReturnType;

    public AstProperty(AstNode prefix, boolean lvalue, boolean strict) {
        this(prefix, lvalue, strict, false);
    }

    private static final String FORM = "form";

    private static final String PARAM = "param";

    public AstProperty(AstNode prefix, boolean lvalue, boolean strict, boolean ignoreReturnType) {
        this.prefix = prefix;
        this.lvalue = lvalue;
        this.strict = strict;
        this.ignoreReturnType = ignoreReturnType;
    }

    protected abstract Object getProperty(Bindings bindings, ELContext context) throws ELException;

    protected AstNode getPrefix() {
        return prefix;
    }

    @Override
    public ValueReference getValueReference(Bindings bindings, ELContext context) {
        Object base = prefix.eval(bindings, context);
        if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", prefix));
        }
        Object property = getProperty(bindings, context);
        if (property == null && strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", "null", base));
        }
        return new ValueReference(base, property);
    }

    @Override
    public Object eval(Bindings bindings, ELContext context) {
        Object o = null;
        try {
            o = parseFormAndParam(bindings, context);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        if (o != null) {
            //解析成功直接返回结果
            return o;
        }
        Object base = prefix.eval(bindings, context);
        if (base == null) {
            return null;
        }
        Object property = getProperty(bindings, context);
        if (property == null && strict) {
            return null;
        }
        context.setPropertyResolved(false);
        Object result = context.getELResolver().getValue(context, base, property);
        if (!context.isPropertyResolved()) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", property, base));
        }
        return result;
    }

    /**
     * 自定义解析表单参数和流程参数
     *
     * @param bindings
     * @param context
     * @return
     */
    private Object parseFormAndParam(Bindings bindings, ELContext context) throws Exception {
        Object property = getProperty(bindings, context);
        if (property == null && strict) {
            return null;
        }
        if (prefix instanceof AstIdentifier) {
            Object variableContainer = context.getContext(VariableContainer.class);
            AstIdentifier identifierPrefix = (AstIdentifier) prefix;
            String prefixName = identifierPrefix.getName();
            if (variableContainer instanceof ExecutionEntityImpl) {
                ExecutionEntityImpl execution = (ExecutionEntityImpl) variableContainer;
                if (Objects.equals(prefixName, FORM)) {
                    WfBusiInfoMapper wfBusiInfoMapper = ApplicationContextUtils.getApplicationContext().getBean("wfBusiInfoMapper", WfBusiInfoMapper.class);
                    LambdaQueryWrapper<WfBusiInfo> query = Wrappers.lambdaQuery();
                    query.eq(WfBusiInfo::getProcessInstanceId, execution.getProcessInstanceId());
                    WfBusiInfo busiInfo = wfBusiInfoMapper.selectOne(query);
                    String submitData = busiInfo.getSubmitData();
                    Map map = JSONUtil.toBean(submitData, Map.class);
                    return StringUtils.getJsonValue(map, property.toString());
                } else if (Objects.equals(prefixName, PARAM)) {
                    WfEnvItemMapper wfEnvItemMapper = ApplicationContextUtils.getApplicationContext().getBean("wfEnvItemMapper", WfEnvItemMapper.class);
                    RepositoryService repositoryService = ApplicationContextUtils.getApplicationContext().getBean(RepositoryService.class);
                    WfParamServiceImpl wfParamServiceImpl = ApplicationContextUtils.getApplicationContext().getBean("wfParamServiceImpl", WfParamServiceImpl.class);
                    BpmnModel bpmnModel = repositoryService.getBpmnModel(execution.getProcessDefinitionId());
                    String envId = ModelUtils.getExtensionElementText(bpmnModel.getMainProcess(), ProcessConstants.ENV);
                    LambdaQueryWrapper<WfEnvItem> lqw = Wrappers.lambdaQuery();
                    lqw.eq(WfEnvItem::getEnvId, envId);
                    lqw.eq(WfEnvItem::getItemKey, property.toString());
                    List<WfEnvItem> wfEnvItems = wfEnvItemMapper.selectList(lqw);
                    if (CollectionUtil.isEmpty(wfEnvItems)) {
                        throw new WorkflowException("找不到流程参数【" + property + "】");
                    }
                    return wfParamServiceImpl.parseWfParam(wfEnvItems.get(0).getEnvItemId().toString(), execution);
                }
            } else if (variableContainer instanceof VariableContainerWrapper) {
                if (Objects.equals(prefixName, FORM)) {
                    //流程预测
                    VariableContainerWrapper variableContainerWrapper = (VariableContainerWrapper) variableContainer;
                    Class<? extends VariableContainerWrapper> aClass1 = variableContainerWrapper.getClass();
                    Field field = aClass1.getDeclaredField("variables");
                    field.setAccessible(true);
                    Map<String, Object> variables = (Map<String, Object>) field.get(variableContainerWrapper);
                    return StringUtils.getJsonValue(variables, property.toString());
                } else if (Objects.equals(prefixName, PARAM)) {
                    //流程预测
                    VariableContainerWrapper variableContainerWrapper = (VariableContainerWrapper) variableContainer;
                    Class<? extends VariableContainerWrapper> aClass1 = variableContainerWrapper.getClass();
                    Field field = aClass1.getDeclaredField("variables");
                    field.setAccessible(true);
                    Map<String, Object> variables = (Map<String, Object>) field.get(variableContainerWrapper);
                    String processKey = variables.get(FieldConstants.PROCESS_KEY).toString();
                    WfParamServiceImpl wfParamServiceImpl = ApplicationContextUtils.getApplicationContext().getBean("wfParamServiceImpl", WfParamServiceImpl.class);
                    WfEnvItemMapper wfEnvItemMapper = ApplicationContextUtils.getApplicationContext().getBean("wfEnvItemMapper", WfEnvItemMapper.class);
                    RepositoryService repositoryService = ApplicationContextUtils.getApplicationContext().getBean(RepositoryService.class);
                    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey).latestVersion().singleResult();
                    BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
                    String envId = ModelUtils.getExtensionElementText(bpmnModel.getMainProcess(), ProcessConstants.ENV);
                    LambdaQueryWrapper<WfEnvItem> lqw = Wrappers.lambdaQuery();
                    lqw.eq(WfEnvItem::getEnvId, envId);
                    lqw.eq(WfEnvItem::getItemKey, property.toString());
                    List<WfEnvItem> wfEnvItems = wfEnvItemMapper.selectList(lqw);
                    if (CollectionUtil.isEmpty(wfEnvItems)) {
                        throw new WorkflowException("找不到流程参数【" + property + "】");
                    }
                    return wfParamServiceImpl.parseWfParam(wfEnvItems.get(0).getEnvItemId().toString(), variables, null);
                }
            }
        }
        return null;
    }

    @Override
    public final boolean isLiteralText() {
        return false;
    }

    @Override
    public final boolean isLeftValue() {
        return lvalue;
    }

    @Override
    public boolean isMethodInvocation() {
        return false;
    }

    @Override
    public Class<?> getType(Bindings bindings, ELContext context) {
        if (!lvalue) {
            return null;
        }
        Object base = prefix.eval(bindings, context);
        if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", prefix));
        }
        Object property = getProperty(bindings, context);
        if (property == null && strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", "null", base));
        }
        context.setPropertyResolved(false);
        Class<?> result = context.getELResolver().getType(context, base, property);
        if (!context.isPropertyResolved()) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", property, base));
        }
        return result;
    }

    @Override
    public boolean isReadOnly(Bindings bindings, ELContext context) throws ELException {
        if (!lvalue) {
            return true;
        }
        Object base = prefix.eval(bindings, context);
        if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", prefix));
        }
        Object property = getProperty(bindings, context);
        if (property == null && strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", "null", base));
        }
        context.setPropertyResolved(false);
        boolean result = context.getELResolver().isReadOnly(context, base, property);
        if (!context.isPropertyResolved()) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", property, base));
        }
        return result;
    }

    @Override
    public void setValue(Bindings bindings, ELContext context, Object value) throws ELException {
        if (!lvalue) {
            throw new ELException(LocalMessages.get("error.value.set.rvalue", getStructuralId(bindings)));
        }
        Object base = prefix.eval(bindings, context);
        if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", prefix));
        }
        Object property = getProperty(bindings, context);
        if (property == null && strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", "null", base));
        }
        context.setPropertyResolved(false);
        Class<?> type = context.getELResolver().getType(context, base, property);
        if (context.isPropertyResolved()) {
            if (type != null && (value != null || type.isPrimitive())) {
                value = bindings.convert(value, type);
            }
            context.setPropertyResolved(false);
        }
        context.getELResolver().setValue(context, base, property, value);
        if (!context.isPropertyResolved()) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", property, base));
        }
    }

    protected Method findMethod(String name, Class<?> clazz, Class<?> returnType, Class<?>[] paramTypes) {
        Method method = null;
        try {
            method = clazz.getMethod(name, paramTypes);
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundException(LocalMessages.get("error.property.method.notfound", name, clazz), e);
        }
        method = findAccessibleMethod(method);
        if (method == null) {
            throw new MethodNotFoundException(LocalMessages.get("error.property.method.notfound", name, clazz));
        }
        if (!ignoreReturnType && returnType != null && !returnType.isAssignableFrom(method.getReturnType())) {
            throw new MethodNotFoundException(LocalMessages.get("error.property.method.returntype", method.getReturnType(), name, clazz, returnType));
        }
        return method;
    }

    @Override
    public MethodInfo getMethodInfo(Bindings bindings, ELContext context, Class<?> returnType, Class<?>[] paramTypes) {
        Object base = prefix.eval(bindings, context);
        if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", prefix));
        }
        Object property = getProperty(bindings, context);
        if (property == null && strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.method.notfound", "null", base));
        }
        String name = bindings.convert(property, String.class);
        Method method = findMethod(name, base.getClass(), returnType, paramTypes);
        return new MethodInfo(method.getName(), method.getReturnType(), paramTypes);
    }

    @Override
    public Object invoke(Bindings bindings, ELContext context, Class<?> returnType, Class<?>[] paramTypes, Object[] paramValues) {
        Object base = prefix.eval(bindings, context);
        if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", prefix));
        }
        Object property = getProperty(bindings, context);
        if (property == null && strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.method.notfound", "null", base));
        }
        String name = bindings.convert(property, String.class);
        Method method = findMethod(name, base.getClass(), returnType, paramTypes);
        try {
            return method.invoke(base, paramValues);
        } catch (IllegalAccessException e) {
            throw new ELException(LocalMessages.get("error.property.method.access", name, base.getClass()), e);
        } catch (IllegalArgumentException e) {
            throw new ELException(LocalMessages.get("error.property.method.invocation", name, base.getClass()), e);
        } catch (InvocationTargetException e) {
            throw new ELException(LocalMessages.get("error.property.method.invocation", name, base.getClass()), e.getCause());
        }
    }

    @Override
    public AstNode getChild(int i) {
        return i == 0 ? prefix : null;
    }
}
