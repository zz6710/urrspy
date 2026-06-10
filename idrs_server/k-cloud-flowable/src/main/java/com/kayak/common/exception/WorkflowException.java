package com.kayak.common.exception;

/**
 * 工作流所有异常类的基类
 *
 * @author yuanjinqiao
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
