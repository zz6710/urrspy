package com.kayak.pms.opFlow.engine.handlers;


import com.kayak.pms.opFlow.engine.entity.Task;
import com.kayak.pms.opFlow.engine.entity.User;

/**
 * 任务访问策略类
 * 用于判断给定的操作人员是否允许执行某个任务
 * 如果涉及用户角色
 */
public interface TaskAccessStrategy {
    /**
     *
     * @param operator 当前用户
     * @param task 当前任务
     * @return 用户是否可以查看该任务
     */
    boolean isAllowed(String operator, User user, Task task);

}
