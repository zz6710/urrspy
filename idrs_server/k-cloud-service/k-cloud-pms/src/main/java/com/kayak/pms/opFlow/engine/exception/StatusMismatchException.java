package com.kayak.pms.opFlow.engine.exception;

public class StatusMismatchException extends WorkflowException {

    public StatusMismatchException() {
    }

    public StatusMismatchException(String message) {
        super(message);
    }

    public StatusMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusMismatchException(Throwable cause) {
        super(cause);
    }

}
