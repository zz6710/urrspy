package com.kayak.workflow.exception;

/**
 * @author libo
 */
public enum WorkflowExceptionEnum {

    DUPLICATE_LAUNCH("流程已发起，请勿重复操作"),

    GET_BUSINESS_INFO_ERROR("获取业务数据失败"),

    CALL_BACK_REMOTE_ERROR("回调远端服务失败"),

    FAIL_LAUNCH("流程发起失败");

    private String message;

    WorkflowExceptionEnum(String message) {
        this.message = message;
    }

    public void throwException() throws WorkflowException {
        throw new WorkflowException(message);
    }
}
