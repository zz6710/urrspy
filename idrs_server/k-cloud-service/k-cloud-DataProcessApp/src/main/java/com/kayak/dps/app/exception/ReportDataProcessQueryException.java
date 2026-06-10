package com.kayak.dps.app.exception;

public class ReportDataProcessQueryException extends Exception{

    /**
     * 源数据校验查询语句异常
     * @param message 异常信息
     */
    public ReportDataProcessQueryException(String message){
        super(message);
    }
}
