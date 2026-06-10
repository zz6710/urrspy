package com.kayak.pms.opFlow.engine.handlers;

import com.kayak.pms.opFlow.engine.model.Execution;

import java.util.List;

public interface ForkHandler {

    List<String> decide(Execution execution);

}
