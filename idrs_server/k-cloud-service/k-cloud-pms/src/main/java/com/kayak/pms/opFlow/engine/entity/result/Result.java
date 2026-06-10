package com.kayak.pms.opFlow.engine.entity.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by daniel on 31/03/2017.
 * 返回结果对象
 * http://stackoverflow.com/questions/12806386/standard-json-api-response-format
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {
    private boolean success;
    private Object data;//存放数据，真正响应的数据,值存放成功的数据，异常后放到message中进行说明
    private String returnmsg; //可选,附加说明失败原因

    public Result() {
    }

    public Result(boolean success, String returnmsg) {
        this.success = success;
        this.returnmsg = returnmsg;
    }

    public Result(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public Result(boolean success, String returnmsg, Object data) {
        this.success = success;
        this.returnmsg = returnmsg;
        this.data = data;
    }

}
