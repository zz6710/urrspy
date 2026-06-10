package com.kayak.pms.opFlow.engine.model;

/**
 * Created by daniel on 20/03/2017.
 */
public class ForkModel extends NodeModel {

    private static final long serialVersionUID = 8386558511091166331L;

    private String forkHandler;

    @Override
    protected void exec(Execution execution) throws Exception {
        runOutTransition(execution);
    }

    public String getForkHandler() {
        return forkHandler;
    }

    public void setForkHandler(String forkHandler) {
        this.forkHandler = forkHandler;
    }
}
