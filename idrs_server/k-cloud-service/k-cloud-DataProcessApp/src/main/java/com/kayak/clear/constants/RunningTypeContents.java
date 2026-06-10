package com.kayak.clear.constants;

/**
 * 文件名: RunningType.java
 * 描述:  跑批类型
 * 创建人: zengzt
 * 创建时间:2020年3月31日下午6:18:11
 */
public class RunningTypeContents {

    /**
     * 0 - T 日
     */
    public static final String T = "0";

    /**
     * 1 - T-1日
     */
    public static final String TPre1 = "1";


    public static boolean isT(String val){
        return T.equals(val);
    }

    public static boolean isTPre1(String val){
        return TPre1.equals(val);
    }


}
