package com.kayak.pms.opFlow.engine.handlers;

import com.kayak.pms.opFlow.engine.entity.ProcessInstance;
import com.kayak.pms.opFlow.engine.entity.Task;

/**
 * 任务、实例完成时触发动作的接口
 * Created by daniel on 30/03/2017.
 */
public interface Completion {
    /**
     * 任务完成触发执行
     * @param task 任务对象
     */
    public void complete(Task task);

    /**
     * 实例完成触发执行
     * @param processInstance 实例对象
     */
    public void complete(ProcessInstance processInstance);
}
