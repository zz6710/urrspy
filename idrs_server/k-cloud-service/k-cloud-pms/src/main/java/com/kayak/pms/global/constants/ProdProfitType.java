package com.kayak.pms.global.constants;

/**
 * 产品计价类型
 *
 * @author kkws-admin
 */
public class ProdProfitType {
    /**
     * 净值
     */
    public static final String NAV = "0";
    /**
     * 收益
     */
    public static final String INCOME = "1";
    /**
     * 货币
     */
    public static final String CUR = "2";

    public static boolean isNav(String profitType) {
        return NAV.equals(profitType);
    }

    public static boolean isIncome(String profitType) {
        return INCOME.equals(profitType);
    }

    public static boolean isCur(String profitType) {
        return CUR.equals(profitType);
    }

}
