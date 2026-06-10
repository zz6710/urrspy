package com.kayak.pms.opFlow.engine.model;

import java.util.Collections;
import java.util.List;

/**
 * Created by daniel on 19/03/2017.
 */
public class StartModel extends NodeModel {

    private static final long serialVersionUID = 3963029247142643715L;

    /**
     * 开始节点无输入变迁
     */
    public List<TransitionModel> getInputs(){
        return Collections.emptyList();
    }

    protected void exec(Execution execution) throws Exception {
        runOutTransition(execution);
    }

}
