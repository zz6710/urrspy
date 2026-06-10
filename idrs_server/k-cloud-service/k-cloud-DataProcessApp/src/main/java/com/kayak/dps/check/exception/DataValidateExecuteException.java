package com.kayak.dps.check.exception;

public class DataValidateExecuteException extends Exception{

    /**
     * 源数据校验执行语句异常
     * @param message 异常信息
     */
    public DataValidateExecuteException(String message){
        super(message);
    }
}
