package com.kayak.common.entity.result;

import lombok.Data;

@Data
public class ResponseResult<T> {
    public static final String SUCCESS = "200";
    public static final String ERROR = "500";
    //200 成功  500 失败
    private String status;
    private T data; //正确结果
    private String message; //错误提示消息

    public ResponseResult() {
    }

    public ResponseResult(String status) {
        this.status = status;
    }

}
