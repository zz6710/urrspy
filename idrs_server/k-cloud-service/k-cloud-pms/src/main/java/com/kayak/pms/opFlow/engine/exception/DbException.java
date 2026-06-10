package com.kayak.pms.opFlow.engine.exception;

/**
 * Created by daniel on 17/03/2017.
 */
public class DbException extends WorkflowException {
    public DbException() {
    }

    public DbException(String message) {
        super(message);
    }

    public DbException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbException(Throwable cause) {
        super(cause);
    }

}
