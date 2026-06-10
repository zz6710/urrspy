package com.kayak.dps.check.constants;

/**
 * 数据校验规则常量
 */
public class DataValidateTypeConstants {

    public static final String NULLABLE_CHECK = "01";//01-非空校验

    public static final String RANGING_CHECK = "02";//02-值域校验

    public static final String COLUMN_FORMAT_CHECK = "03";//03-字段格式及长度校验

    public static final String FILE_FORMAT_CHECK = "04";//04-文件格式及大小校验

    public static final String LINKED_CHECK = "05";//05-字段联动校验

    public static final String NUMERICAL_CHECK = "06";//06-数字校验

    public static final String REPEATED_CHECK = "07";//07-重复性校验

    public static final String IDENTITY_CHECK = "08";//08-身份证校验

    public static final String CALCULATE_CHECK = "09";//09-计算校验

    public static final String TABLE_CHECK = "10";//10-表间校验

}
