package com.kayak.dps.check.exception;

public class ReportDataValidateExecuteException extends Exception{

    /**
     * 源数据校验执行语句异常
     * @param message 异常信息
     */
    public ReportDataValidateExecuteException(String message){
        super(message);
    }
}
