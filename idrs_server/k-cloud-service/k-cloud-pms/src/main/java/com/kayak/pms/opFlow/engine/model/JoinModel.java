package com.kayak.pms.opFlow.engine.model;

import com.kayak.pms.opFlow.engine.handlers.impl.MergeBranchHandler;

/**
 * Created by daniel on 20/03/2017.
 */
public class JoinModel extends NodeModel {
    @Override
    protected void exec(Execution execution) throws Exception {
        fire(new MergeBranchHandler(this), execution);
        if(execution.isMerged()){
            runOutTransition(execution);
        }
    }
}
