package com.kayak.web.execution;

import com.kayak.core.system.SysBeans;
import com.kayak.web.business.mapper.BaseAccountMergeOrderMapper;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.flowable.variable.api.persistence.entity.VariableInstance;

import java.util.Map;

public class CustomerDataMergeListener implements ExecutionListener, TaskListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        Map<String, VariableInstance> variableInstances = delegateExecution.getParent().getVariableInstances();
        VariableInstance variableInstance = variableInstances.get("id");
        if (variableInstance != null) {
            BaseAccountMergeOrderMapper mapper = SysBeans.getBean("baseAccountMergeOrderMapper");
            mapper.updateMrgStsById("02", variableInstance.getTextValue());
        }
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        Map<String, VariableInstance> variableInstances = delegateTask.getVariableInstances();
        VariableInstance variableInstance = variableInstances.get("id");
        if (((TaskEntityImpl) delegateTask).isCanceled() && variableInstance != null) {
            BaseAccountMergeOrderMapper mapper = SysBeans.getBean("baseAccountMergeOrderMapper");
            mapper.updateMrgStsById("01", variableInstance.getTextValue());
        }
    }
}
