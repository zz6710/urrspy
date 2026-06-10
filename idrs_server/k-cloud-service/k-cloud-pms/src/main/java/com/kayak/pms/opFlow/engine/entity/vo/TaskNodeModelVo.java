package com.kayak.pms.opFlow.engine.entity.vo;

import com.kayak.pms.opFlow.engine.entity.Task;
import com.kayak.pms.opFlow.engine.model.Execution;
import com.kayak.pms.opFlow.engine.model.OperationModel;
import lombok.Data;

import java.util.List;

@Data
public class TaskNodeModelVo {
    private List<Task> taskList;
    private OperationModel operationModel;
    private Execution execution;

    public TaskNodeModelVo() {}

    public TaskNodeModelVo(List<Task> taskList, OperationModel operationModel, Execution execution) {
        this.taskList = taskList;
        this.operationModel = operationModel;
        this.execution = execution;
    }
}
