package com.kayak.pms.opFlow.engine.model;

import com.kayak.pms.opFlow.engine.handlers.impl.EndProcessHandler;
import com.kayak.pms.opFlow.engine.helper.InterceptHelper;

import java.util.List;

/**
 * Created by daniel on 19/03/2017.
 */
public class EndModel extends NodeModel {
    private static final long serialVersionUID = -7941964923978222955L;
    private String busiUrl;

    @Override
    protected void exec(Execution execution) throws Exception {
        EndProcessHandler endProcessHandler = new EndProcessHandler();
        endProcessHandler.setBusiUrl(busiUrl);
        fire(endProcessHandler, execution);
        List<EndModel> model = execution.getProcess().getProcessModel().getModels(EndModel.class);
        if (model.size() == 1) {
            InterceptHelper.intercept(model.get(0).getPostInterceptorList(), execution);
        }
    }

    public String getBusiUrl() {
        return busiUrl;
    }

    public void setBusiUrl(String busiUrl) {
        this.busiUrl = busiUrl;
    }
}
