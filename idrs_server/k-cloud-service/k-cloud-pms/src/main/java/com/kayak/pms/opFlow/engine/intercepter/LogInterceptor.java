package com.kayak.pms.opFlow.engine.intercepter;

import com.kayak.pms.opFlow.engine.entity.Task;
import com.kayak.pms.opFlow.engine.model.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 任务的日志拦截
 * Created by daniel on 30/03/2017.
 */
@Component
public class LogInterceptor implements TaskInterceptor{

    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public void intercept(Execution execution) {
        if(logger.isDebugEnabled()){
            for (Task task: execution.getTasks() ) {
                StringBuffer buffer = new StringBuffer(100);
                buffer.append("创建任务[标识=").append(task.getId());
                buffer.append(",名称=").append(task.getDisplayName());
                buffer.append(",创建时间=").append(task.getCreateTime());
                logger.info(buffer.toString());
            }
        }
    }
}
