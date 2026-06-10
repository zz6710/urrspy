package com.kayak.dps.check.exception;

public class SourceDataValidateExecuteException extends Exception{

    /**
     * 源数据校验执行语句异常
     * @param message 异常信息
     */
    public SourceDataValidateExecuteException(String message){
        super(message);
    }
}
