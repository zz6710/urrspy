package com.kayak.pms.opFlow.engine.handlers;

import com.kayak.pms.opFlow.engine.model.Execution;

/**
 * 决策处理器接口
 * Created by daniel on 19/03/2017.
 */
public interface DecisionHandler {

    /**
     * 定义决策方法，实现类需要根据执行对象做处理，并返回后置流转的name
     * @param execution
     * @return 后置流转的name
     */
     String decide(Execution execution);
}
