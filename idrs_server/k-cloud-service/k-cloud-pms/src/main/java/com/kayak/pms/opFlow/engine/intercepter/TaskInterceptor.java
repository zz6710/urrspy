package com.kayak.pms.opFlow.engine.intercepter;

import com.kayak.pms.opFlow.engine.model.Execution;

/**
 * 任务拦截器，对产生的任务结果进行拦截
 * Created by daniel on 19/03/2017.
 */
public interface TaskInterceptor {
    void intercept(Execution execution);
}
