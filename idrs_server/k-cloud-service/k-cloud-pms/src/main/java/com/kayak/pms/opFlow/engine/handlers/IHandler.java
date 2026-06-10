package com.kayak.pms.opFlow.engine.handlers;

import com.kayak.pms.opFlow.engine.model.Execution;

/**
 * 流程各模型操作处理接口
 * Created by daniel on 19/03/2017.
 */
public interface IHandler {

    void handle(Execution execution) throws Exception;

}
