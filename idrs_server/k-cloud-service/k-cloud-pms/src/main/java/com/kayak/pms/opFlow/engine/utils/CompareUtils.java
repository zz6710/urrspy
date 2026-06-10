package com.kayak.pms.opFlow.engine.utils;

import com.kayak.core.util.BigDecimalUtil;

/**
 * Created by daniel on 22/09/2017.
 */
public class CompareUtils {
    public static final String GREATER = "1";
    public static final String GREATER_EQUAL = "2";
    public static final String LESS = "3";
    public static final String LESS_EQUAL = "4";
    public static final String EQUAL = "5";
    public static final String NOT_EQUAL = "6";
    public static final String STRING = "string";
    public static final String NUMBER = "number";


    public static <T> boolean greater(T t1, T t2) {
//        if (t1 instanceof Integer) {
//            return ((Integer) t1).compareTo((Integer) t2) > 0;
//        }
        if (t1 instanceof Number) {
            return ((Double) t1).compareTo(Double.parseDouble(String.valueOf(t2))) > 0;
        }
        return t1 instanceof String && ((String) t1).compareTo((String) t2) > 0;
    }

    public static <T> boolean equal(T t1, T t2,boolean flag) {
        return BigDecimalUtil.sub(t1, t2) == 0;
    }

    public static <T> boolean equal(T t1, T t2) {

//        if (t1 instanceof Integer) {
//            return ((Integer) t1).compareTo((Integer) t2) == 0;
//        }
        if (t1 instanceof Double) {
            return ((Double) t1).compareTo(Double.parseDouble( String.valueOf(t2))) == 0;
        }
        return t1 instanceof String && ((String) t1).compareTo((String) t2) == 0;
    }

    public static <T> boolean notEqual(T t1, T t2) {
        return !equal(t1, t2);
    }

    public static <T> boolean greaterEqual(T t1, T t2) {
        return greater(t1, t2) || equal(t1, t2);
    }

    public static <T> boolean less(T t1, T t2) {
//        if (t1 instanceof Integer) {
//            return ((Integer) t1).compareTo((Integer) t2) < 0;
//        }
        if (t1 instanceof Double) {
            return ((Double) t1).compareTo(Double.parseDouble( String.valueOf(t2))) < 0;
        }
        return t1 instanceof String && ((String) t1).compareTo((String) t2) < 0;
    }

    public static <T> boolean lessEqual(T t1, T t2) {
        return less(t1, t2) || equal(t1, t2);
    }

    public static <T> boolean compare(T t1, T t2, String exprCondition) {
        if (GREATER.equalsIgnoreCase(exprCondition)) {
            return greater(t1, t2);
        }
        if (GREATER_EQUAL.equalsIgnoreCase(exprCondition)) {
            return greaterEqual(t1, t2);
        }
        if (LESS.equalsIgnoreCase(exprCondition)) {
            return less(t1, t2);
        }
        if (LESS_EQUAL.equalsIgnoreCase(exprCondition)) {
            return lessEqual(t1, t2);
        }
        if (EQUAL.equalsIgnoreCase(exprCondition)) {
            return equal(t1, t2);
        }
        if (NOT_EQUAL.equalsIgnoreCase(exprCondition)) {
            return notEqual(t1, t2);
        }
        throw new RuntimeException("不能计算的表达式:" + exprCondition);
    }

}
