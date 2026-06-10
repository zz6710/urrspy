package com.kayak.pms.opFlow.engine.model;

import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.exception.WorkflowException;
import com.kayak.pms.opFlow.engine.handlers.DecisionHandler;
import com.kayak.pms.opFlow.engine.helper.ClassHelper;
import com.kayak.pms.opFlow.engine.utils.CompareUtils;
import com.kayak.pms.opFlow.engine.utils.RemoteInvokeUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniel on 20/03/2017.
 */
public class DecisionModel extends NodeModel {
    private static final long serialVersionUID = 7171602622116416999L;
    private static Logger logger = LoggerFactory.getLogger(DecisionModel.class);

    /**
     * 决策处理类，对于复杂的分支条件，可通过handleClass来处理
     */
    private String handleClass;

    /**
     * 决策处理类实例,根据handleClass生成
     */
    private DecisionHandler decide;

    private String env;

    @Override
    protected void exec(Execution execution) throws Exception {
        logger.info(execution.getProcessInstance().getProcessInstanceId() + "->decision execution.getParams():" + execution.getParams());
        //直接计算模型中连线上面的条件，如果表达式为真，则执行
        boolean isfound = false;
        // 通过选择表单来计算
        isfound = foundByExpr(execution);
        // 通过上下文表达式来执行
        if (!isfound) {
            isfound = DecisionModelUtil.foundByEnvExp(execution, getOutputs());
        }
        if (!isfound) {
            String next;
            if (decide != null) {//如果没有表达式就使用自定义决策类来实现功能
                if (StringHelper.isNotEmpty(handleClass)) {
                    next = (String) RemoteInvokeUtil.restInvoke(handleClass, execution);
                } else {
                    next = decide.decide(execution);
                }
                logger.info("流程实例:{} 用于自定义处理类{} 返回的执行的下一个任务节点是{}", execution.getProcessInstance().getProcessInstanceId(), decide.getClass(), next);
                for (TransitionModel tm : getOutputs()) {
                    if (tm.getTo().equals(next)) {
                        tm.setEnabled(true);
                        tm.execute(execution);
                        isfound = true;
                        break;//找到不在继续寻找
                    }
                }
            }
        }
        if (!isfound) {
            throw new WorkflowException("流程名:" + execution.getProcess().getDisplayName() + "中,decision节点【" + getName() + "】无法确定下一步执行路线");
        }
    }


    private boolean foundByExpr(Execution execution) throws Exception {
        boolean isfound = false;
        for (TransitionModel tm : getOutputs()) {
            String exprType = tm.getExprType();
            if (StringHelper.isNotEmpty(exprType)) {//如果有表达式类型，说明可以计算表达式的值, 这个必须执行计算，如果不能执行计算，则错误。
                Map<String, Object> cxtParams = new HashMap<String, Object>();//上下文参数
                cxtParams.putAll(execution.getParams());//主要是流程中动态设置的数据
                cxtParams.putAll(execution.getLatestSubmitParams());//表单参数

                Object value = cxtParams.get(tm.getExprKey());
                if (value == null) {
                    logger.error("表达式{}不能获取对应的值", tm.getExprKey());
                }

                if (CompareUtils.NUMBER.equalsIgnoreCase(exprType)) {
                    if (!NumberUtils.isCreatable((String) value)) {//如果不是指定类型的比较，则没有意义
                        logger.error("表达式:{}不能获取对应的{}不是数字类型", tm.getExprKey(), value);
                    }
                    value = Double.parseDouble(String.valueOf(value));
                }

                isfound = CompareUtils.compare(value, tm.getExprValue(), tm.getExprCondition());
                if (isfound) {
                    tm.setEnabled(true);
                    tm.execute(execution);
                    break;//找到不在继续寻找
                }
            }
        }
        return isfound;
    }

    public void setHandleClass(String handleClass) {
        this.handleClass = handleClass;
        if (StringHelper.isNotEmpty(handleClass)) {
            decide = (DecisionHandler) ClassHelper.newInstance(handleClass);
        }
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getHandleClass() {
        return handleClass;
    }

    public DecisionHandler getDecide() {
        return decide;
    }

    public void setDecide(DecisionHandler decide) {
        this.decide = decide;
    }
}
