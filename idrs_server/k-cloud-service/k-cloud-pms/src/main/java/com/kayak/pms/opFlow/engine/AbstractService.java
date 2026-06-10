package com.kayak.pms.opFlow.engine;

import com.kayak.helper.JsonHelper;
import com.kayak.pms.opFlow.engine.entity.result.Result;

public abstract class AbstractService {
    protected String updateSuccess() {
        return updateSuccess(null);
    }

    protected String updateSuccess(Object data) {
        return JsonHelper.toJson(new Result(true, data));
    }

    protected String updateSuccess(String msg, Object data) {
        return JsonHelper.toJson(new Result(true, msg, data));
    }

    protected String updateError(String errMsg) {
        return JsonHelper.toJson(new Result(false, errMsg));
    }
}
