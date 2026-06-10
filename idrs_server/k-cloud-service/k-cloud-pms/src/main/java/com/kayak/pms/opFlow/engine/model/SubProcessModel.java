package com.kayak.pms.opFlow.engine.model;

/**
 * Created by daniel on 19/03/2017.
 */
public class SubProcessModel extends NodeModel {
    @Override
    protected void exec(Execution execution) throws Exception {
        runOutTransition(execution);
    }
}
