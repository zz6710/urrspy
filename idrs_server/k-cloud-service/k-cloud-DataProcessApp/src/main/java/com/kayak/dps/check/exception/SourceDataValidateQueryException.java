package com.kayak.dps.check.exception;

public class SourceDataValidateQueryException extends Exception{

    /**
     * 源数据校验查询语句异常
     * @param message 异常信息
     */
    public SourceDataValidateQueryException(String message){
        super(message);
    }
}
