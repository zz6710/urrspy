package com.kayak.core.system;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
