package com.kayak.dps.check.exception;

public class SourceDataValidateFormatException extends Exception{

    /**
     * 源数据校验执行语句异常
     * @param message 异常信息
     */
    public SourceDataValidateFormatException(String message){
        super(message);
    }
}
