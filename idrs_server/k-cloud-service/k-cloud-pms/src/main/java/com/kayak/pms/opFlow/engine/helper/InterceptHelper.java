package com.kayak.pms.opFlow.engine.helper;

import com.kayak.pms.opFlow.engine.entity.Task;
import com.kayak.pms.opFlow.engine.intercepter.TaskInterceptor;
import com.kayak.pms.opFlow.engine.model.Execution;
import com.kayak.pms.opFlow.engine.model.NodeModel;
import com.kayak.pms.opFlow.engine.model.TaskModel;
import com.kayak.pms.opFlow.engine.utils.RemoteInvokeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InterceptHelper {
    private static Logger logger = LoggerFactory.getLogger(InterceptHelper.class);

    public static void intercept(List<TaskInterceptor> interceptors, Execution execution) {
        try {
            for (TaskInterceptor interceptor : interceptors) {
                setCurrentTaskRoles(execution);
                interceptor.intercept(execution);
            }
        } catch (Exception e) {
            logger.error("拦截器执行失败: {}", e);
        }
    }

    public static void intercept(String url, Execution execution) {
        RemoteInvokeUtil.restInvoke(url, execution);
    }

    public static void setCurrentTaskRoles(Execution execution) {
        Task task = execution.getTasks().get(execution.getTasks().size() - 1);//获取最新的节点
        try {
            NodeModel node = execution.getProcess().getProcessModel().getNode(task.getName());
            if (node instanceof TaskModel) {
                TaskModel taskModel = (TaskModel) node;
                execution.setCurrentRoles(taskModel.getRoleIds());
            }
        } catch (Exception e) {
            logger.error("获取节点模型失败！", e);
        }

    }
}
