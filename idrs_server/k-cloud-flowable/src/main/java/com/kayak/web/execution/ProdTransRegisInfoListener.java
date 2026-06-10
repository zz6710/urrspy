package com.kayak.web.execution;

import com.kayak.core.system.SysBeans;
import com.kayak.web.business.mapper.ImportMenuFileManageMapper;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.flowable.variable.api.persistence.entity.VariableInstance;

import java.util.Map;

public class ProdTransRegisInfoListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        Map<String, VariableInstance> variableInstances = delegateTask.getVariableInstances();
        VariableInstance variableInstance = variableInstances.get("id");
        if (((TaskEntityImpl) delegateTask).isCanceled() && variableInstance != null) {
            ImportMenuFileManageMapper mapper = SysBeans.getBean("importMenuFileManageMapper");
            mapper.updateStatusById("2", variableInstance.getTextValue());
        }
    }
}
