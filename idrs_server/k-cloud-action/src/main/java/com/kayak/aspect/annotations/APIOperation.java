package com.kayak.aspect.annotations;

/**
 * @version 1.0
 * @author: beacon
 * @Date: 2019-03-20   21:02
 * @Description
 */
public enum APIOperation {

    UPDATE("更新"),

    INSTER("新增"),

    DELETE("删除"),

    SELECT("查询"),

    INSTERORUPDATE("有则更新，无则新增");

    private String operation;

    APIOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
