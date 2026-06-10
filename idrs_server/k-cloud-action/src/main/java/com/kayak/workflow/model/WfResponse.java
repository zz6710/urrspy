package com.kayak.workflow.model;

public class WfResponse {

    public static final String SUCCESS = "200";
    public static final String ERROR = "500";
    //200 成功  500 失败
    private String status;

    public WfResponse() {
    }

    public WfResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
