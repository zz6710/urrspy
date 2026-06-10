package com.kayak.pms.opFlow.engine.handlers.impl;

import com.kayak.core.system.SysBeans;
import com.kayak.pms.opFlow.engine.entity.Task;
import com.kayak.pms.opFlow.engine.handlers.IHandler;
import com.kayak.pms.opFlow.engine.model.Execution;
import com.kayak.pms.opFlow.engine.model.OperationModel;
import com.kayak.pms.opFlow.engine.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by daniel on 19/03/2017.
 */
public class CreateOperationTaskHandler implements IHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 任务模型
     */
    private OperationModel model;

    /**
     * 调用者需要提供的任务模型
     *
     * @param model
     */
    public CreateOperationTaskHandler(OperationModel model) {
        this.model = model;
    }

    /**
     * 根据任务模型、执行对象，创建下一个任务，并添加到execution对象的tasks集合中
     */
    @Override
    public void handle(Execution execution) {
        TaskService taskService = SysBeans.getBean("taskService");
        //创建任务并添加到历史任务列表
        List<Task> task;
        try {
            task = taskService.createOperationTask(model, execution);
            execution.addTasks(task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
