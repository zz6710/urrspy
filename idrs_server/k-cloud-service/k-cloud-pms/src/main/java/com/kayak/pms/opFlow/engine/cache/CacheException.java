package com.kayak.pms.opFlow.engine.cache;

import com.kayak.pms.opFlow.engine.exception.WorkflowException;

/**
 * Created by daniel on 17/03/2017.
 */
public class CacheException extends WorkflowException {
    public CacheException() {
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }
}
