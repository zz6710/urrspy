package com.kayak.core.util;


import java.math.BigDecimal;

public class BigDecimalUtil {
    private static final int DEF_DIV_SCALE = 2; //这个类不能实例化

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    /*public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }*/
    public static <T> double add(T v1, T v2) {
        double r1;
        double r2;
        r1 = Double.parseDouble(String.valueOf(v1 + "").replaceAll("[^0-9|.]",""));
        r2 = Double.parseDouble(String.valueOf(v2 + "").replaceAll("[^0-9|.]",""));
        BigDecimal b1 = new BigDecimal(Double.toString(r1));
        BigDecimal b2 = new BigDecimal(Double.toString(r2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static <T> double sub(T v1, T v2, int scale) {
        double r1;
        double r2;
        if (isEmpty(v1) && isEmpty(v2)) {
            return 0;
        }
        if (isEmpty(v1)) {
            return sub(0, v2);
        }
        if (isEmpty(v2)) {
            return sub(v1, 0);
        }
        r1 = Double.parseDouble((v1 + ""));
        r2 = Double.parseDouble((v2 + ""));

        BigDecimal b1 = new BigDecimal(Double.toString(r1));
        BigDecimal b2 = new BigDecimal(Double.toString(r2));
        return round(b1.subtract(b2).doubleValue(), scale);
    }

    public static <T> double sub(T v1, T v2) {
        return sub(v1, v2, 2);
    }


    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static <T> double mul(T v1, T v2, int scale) {
        double r1;
        double r2;
        if (isEmpty(v1) || isEmpty(v2)) {
            return mul(0, 0, scale);
        }
        r1 = Double.parseDouble((v1 + ""));
        r2 = Double.parseDouble((v2 + ""));
        BigDecimal b1 = new BigDecimal(Double.toString(r1));
        BigDecimal b2 = new BigDecimal(Double.toString(r2));
        return round(b1.multiply(b2).doubleValue(), scale);
    }

    private static <T> boolean isEmpty(T v1) {
        if ("".equals(v1) || v1 == null) {
            return true;
        }
        return false;
    }

    public static <T> double mul(T v1, T v2) {
        return mul(v1, v2, 2);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    public static <T> double div(T v1, T v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    public static <T> double div(T v1, T v2, int scale) {
        double r1;
        double r2;
        if ("".equals(v1) || v1 == null) {
            return div(0, 1, scale);// 返回 0
        }
        r1 = Double.parseDouble((v1 + ""));
        r2 = Double.parseDouble((v2 + ""));
        return div(r1, r2, scale);
    }


    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static BigDecimal getBigDecimal(Object object) {
        if (object == null || "".equals(object)) {
            return new BigDecimal(0);
        }
        return new BigDecimal((String) object);
    }

    public static double round2(double v) {
        return round(v, 2);
    }

/*
    public static void main(String[] args) {
        System.out.println( div("1", "2"));
        System.out.println( div(1, "2"));
        System.out.println( div(2, 1));
        System.out.println( div(2, "1"));

        System.out.println( div("1.1", "2"));
        System.out.println( div(1.1, "2"));
        System.out.println( div(2.1, 1));
        System.out.println( div(2, "1.1"));

        System.out.println( sub("1", "2"));
        System.out.println( sub(1, "2"));
        System.out.println( sub(2, 1.1));
        System.out.println( sub(2, "1"));

    }
*/

}

