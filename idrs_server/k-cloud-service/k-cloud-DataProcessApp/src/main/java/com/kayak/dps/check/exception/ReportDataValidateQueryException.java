package com.kayak.dps.check.exception;

public class ReportDataValidateQueryException extends Exception{

    /**
     * 源数据校验查询语句异常
     * @param message 异常信息
     */
    public ReportDataValidateQueryException(String message){
        super(message);
    }
}
