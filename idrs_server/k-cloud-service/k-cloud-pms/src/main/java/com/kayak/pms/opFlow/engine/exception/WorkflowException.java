package com.kayak.pms.opFlow.engine.exception;

/**
 * Created by daniel on 17/03/2017.
 * 工作流所有异常类的基类
 */
public class WorkflowException extends RuntimeException {

    public WorkflowException() {
    }

    public WorkflowException(String message) {
        super(message);
    }

    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkflowException(Throwable cause) {
        super(cause);
    }

}
