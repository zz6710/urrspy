package com.kayak.workflow.exception;

import com.kayak.core.exception.PromptException;

/**
 * 工作流异常
 * @author libo
 */
public class WorkflowException extends PromptException {

    private static final long serialVersionUID = 8360041344449844472L;

    public WorkflowException(String message) {
        super(message);
    }
}
