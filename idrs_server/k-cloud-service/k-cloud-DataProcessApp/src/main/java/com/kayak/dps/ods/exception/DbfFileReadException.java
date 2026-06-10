package com.kayak.dps.ods.exception;

/**
 * DBF文件读取异常
 * @author xiamh
 * @date 2021/10/9
 */
public class DbfFileReadException extends RuntimeException{

    public DbfFileReadException(String message) {
        super(message);
    }
}
