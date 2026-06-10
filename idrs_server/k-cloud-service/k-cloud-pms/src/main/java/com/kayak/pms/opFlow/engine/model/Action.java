package com.kayak.pms.opFlow.engine.model;

/**
 * 所有的模型对象需要实现的接口，需要实现execute方法，每个节点的执行方式不一样
 * Created by daniel on 19/03/2017.
 */
public interface Action {
    void execute(Execution execution) throws Exception;
}
