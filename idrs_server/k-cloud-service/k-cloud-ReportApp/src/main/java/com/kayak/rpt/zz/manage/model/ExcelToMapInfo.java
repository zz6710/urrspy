package com.kayak.rpt.zz.manage.model;

import com.kayak.rpt.zz.manage.enums.ExcelEnum;

/**
 * @author XueJ
 * @version 1.0.0
 * @ClassName excelToMapInfo.java
 * @Description TODO
 * @createTime 2022年05月27日 14:55:00
 */
public class ExcelToMapInfo {


    /*字段名称*/
    private   String fieldName;

    /*字段*/
    private   String field;

    /*字段位置*/
    private   int fieldIndex;

    /*字段类型  */
    private ExcelEnum fieldType;

    /*是否非空*/
    private boolean  isNotNULL;

    /*长度校验 0 代表不校验 */
    private int  length;

    /*字典值，仅当字段类型为枚举时有值 */
    private String  dict;


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public ExcelEnum getFieldType() {
        return fieldType;
    }

    public void setFieldType(ExcelEnum fieldType) {
        this.fieldType = fieldType;
    }

    public boolean isNotNULL() {
        return isNotNULL;
    }

    public void setNotNULL(boolean notNULL) {
        isNotNULL = notNULL;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
