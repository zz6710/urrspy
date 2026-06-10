package com.kayak.rpt.rhzj.util;

import java.math.BigDecimal;

public class rhcodeUtil {

    /**
     * 生成人行产品代码的最后一位校验码；
     * 生成代码的逻辑，由人行后台人员提供
     *
     * @param prod_rh__code
     * @return
     */
    public static String rh_check_no(String prod_rh__code) {
        char[] xc = prod_rh__code.toCharArray();
        int P_VAL = 0;//存储转换的数字

        BigDecimal b_sum = new BigDecimal(0);
        for (int i = xc.length - 1; i >= 0; i--) {
            String x1 = String.valueOf(xc[i]);
            if (x1.toUpperCase().compareTo("A") >= 0 && x1.toUpperCase().compareTo("Z") <= 0) {
                switch (x1) {
                    case "A":
                        P_VAL = 1;
                        break;
                    case "B":
                        P_VAL = 2;
                        break;
                    case "C":
                        P_VAL = 3;
                        break;
                    case "D":
                        P_VAL = 4;
                        break;
                    case "E":
                        P_VAL = 5;
                        break;
                    case "F":
                        P_VAL = 6;
                        break;
                    case "G":
                        P_VAL = 7;
                        break;
                    case "H":
                        P_VAL = 8;
                        break;
                    case "I":
                        P_VAL = 9;
                        break;
                    case "J":
                        P_VAL = 10;
                        break;
                    case "K":
                        P_VAL = 11;
                        break;
                    case "L":
                        P_VAL = 12;
                        break;
                    case "M":
                        P_VAL = 13;
                        break;
                    case "N":
                        P_VAL = 14;
                        break;
                    case "O":
                        P_VAL = 15;
                        break;
                    case "P":
                        P_VAL = 16;
                        break;
                    case "Q":
                        P_VAL = 17;
                        break;
                    case "R":
                        P_VAL = 18;
                        break;
                    case "S":
                        P_VAL = 19;
                        break;
                    case "T":
                        P_VAL = 20;
                        break;
                    case "U":
                        P_VAL = 21;
                        break;
                    case "V":
                        P_VAL = 22;
                        break;
                    case "W":
                        P_VAL = 23;
                        break;
                    case "X":
                        P_VAL = 24;
                        break;
                    case "Y":
                        P_VAL = 25;
                        break;
                    case "Z":
                        P_VAL = 26;
                        break;
                }
            }

            if (x1.compareTo("0") >= 0 && x1.compareTo("9") <= 0) {
                P_VAL = Integer.parseInt(x1);
            }
            //倒序循环遍历
            //如果位数是偶数，则用当前位数的值乘以2除以10取整+用当前值乘以2除以10取余。
            //如果位数是奇数，则用当前位数的值除以10取整+用当前值除以10取余。
            //将换算后的值累加
            if ((xc.length - i - 1) % 2 == 0) {
                b_sum = b_sum.add(BigDecimal.valueOf(P_VAL).multiply(BigDecimal.valueOf(2)).divide(BigDecimal.TEN).setScale(0, BigDecimal.ROUND_DOWN))
                        .add(BigDecimal.valueOf(P_VAL).multiply(BigDecimal.valueOf(2)).divideAndRemainder(BigDecimal.TEN)[1]);
            } else {
                b_sum = b_sum.add(BigDecimal.valueOf(P_VAL).divide(BigDecimal.TEN).setScale(0, BigDecimal.ROUND_DOWN))
                        .add(BigDecimal.valueOf(P_VAL).divideAndRemainder(BigDecimal.TEN)[1]);
            }
        }
        //遍历完成，将合计值除10向上取整后再乘以10,减去合计值本身得到最终唯一校验码
        BigDecimal no_code = b_sum.divide(BigDecimal.TEN, 0, BigDecimal.ROUND_UP).multiply(BigDecimal.TEN).subtract(b_sum);
        return no_code.toString();
    }

    /**
     * 实现顺序码补位函数
     *
     * @param s--原数值
     * @param n--位数
     * @param replace--增加的0
     * @return
     */
    public static String lpad(String s, int n, String replace) {
        while (s.length() < n) {
            s = replace + s;
        }
        return s;
    }
}
