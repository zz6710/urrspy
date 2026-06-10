package com.kayak.subject.exception;

public class ReportDataProcessExecuteException extends Exception{

    /**
     * 源数据校验执行语句异常
     * @param message 异常信息
     */
    public ReportDataProcessExecuteException(String message){
        super(message);
    }
}
