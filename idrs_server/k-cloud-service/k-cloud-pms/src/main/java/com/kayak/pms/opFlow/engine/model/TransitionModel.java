package com.kayak.pms.opFlow.engine.model;

import com.kayak.pms.opFlow.engine.handlers.impl.CreateOperationTaskHandler;
import com.kayak.pms.opFlow.engine.handlers.impl.CreateTaskHandler;
import com.kayak.pms.opFlow.engine.intercepter.TaskInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by daniel on 19/03/2017.
 */
public class TransitionModel extends BaseModel implements Action {
    private static Logger logger = LoggerFactory.getLogger(TransitionModel.class);

    /**
     * 变迁的源节点引用
     */
    private NodeModel source;
    /**
     * 变迁的目标节点引用
     */
    private NodeModel target;
    /**
     * 变迁的目标节点name名称
     */
    private String to;
    /**
     * 变迁的条件表达式，用于decision
     */
    private String expr;

    /**
     * 表达式的类型, 用于值转化，数字或者字符串
     */
    private String exprType;

    /**
     * 表但是的key, 用于获取表单的数据并进行值转化
     */
    private String exprKey;

    /**
     * 表但是的key, 用于比较值
     */
    private String exprValue;

    /**
     * 表达式条件
     */
    private String exprCondition;

    /**
     * 上下文表达式
     */
    private String envExp;

    /**
     * 当前变迁路径是否可用
     */
    private boolean enabled = false;

    @Override
    public void execute(Execution execution) throws Exception {
        if (!enabled) return;

        if (target instanceof OperationModel) {
            // 审批节点，创建操作流任务
            new CreateOperationTaskHandler((OperationModel) target).handle(execution);
        } else if (target instanceof TaskModel) {
            // 审批节点，创建审批任务
            new CreateTaskHandler((TaskModel) target).handle(execution);
        } else if (target instanceof SubProcessModel) {
            // 子任务
        } else {
            //如果目标节点模型为其它控制类型，则继续由目标节点执行
            target.execute(execution);
        }
    }

    public NodeModel getSource() {
        return source;
    }

    public void setSource(NodeModel source) {
        this.source = source;
    }

    public NodeModel getTarget() {
        return target;
    }

    public void setTarget(NodeModel target) {
        this.target = target;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getExprType() {
        return exprType;
    }

    public void setExprType(String exprType) {
        this.exprType = exprType;
    }

    public String getExprKey() {
        return exprKey;
    }

    public void setExprKey(String exprKey) {
        this.exprKey = exprKey;
    }

    public String getExprValue() {
        return exprValue;
    }

    public void setExprValue(String exprValue) {
        this.exprValue = exprValue;
    }

    public String getExprCondition() {
        return exprCondition;
    }

    public void setExprCondition(String exprCondition) {
        this.exprCondition = exprCondition;
    }

    private void intercept(List<TaskInterceptor> interceptors, Execution execution) {
        try {
            for (TaskInterceptor interceptor : interceptors) {
                interceptor.intercept(execution);
            }
        } catch (Exception e) {
            logger.error("拦截器执行失败: {}", e);
        }
    }

    public String getEnvExp() {
        return envExp;
    }

    public void setEnvExp(String envExp) {
        this.envExp = envExp;
    }
}
