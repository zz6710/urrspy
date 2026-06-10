package com.kayak.dps.ods.exception;

/**
 * DBF文件生成异常
 * @author xiamh
 * @date 2021/10/9
 */
public class DbfFileWriteException extends RuntimeException{

    public DbfFileWriteException(String message) {
        super(message);
    }
}
