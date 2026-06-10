package com.kayak.pms.global.constants;

/**
 * 账户状态 TA账号/交易账号 枚举类
 *
 * @author kkws-admin
 */
public enum AcctStatus {
    /**
     * 正常
     */
    NORMAL("0"),
    /**
     * 冻结
     */
    FROZEN("1"),
    /**
     * 注销/挂失
     */
    CANCELED("2");

    /**
     * 状态值
     */
    private final String status;

    AcctStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return status;
    }

    /**
     * 检查账号是否正常
     *
     * @param status
     * @return
     */
    public static boolean isNormal(String status) {
        return AcctStatus.NORMAL.getType().equals(status);
    }


    /**
     * 证件类型判断
     *
     * @param status 状态值
     * @return
     */
    public static boolean isEnums(String status) {
        AcctStatus[] enums= AcctStatus.values();
        for (AcctStatus enumObj:enums) {
            if(enumObj.getType().equals(status)){
                return true;
            }
        }
        return false;
    }

 /**
     * 检查账号是否冻结
     *
     * @param status
     * @return
     */
    public static boolean isFrozen(String status) {
        return AcctStatus.FROZEN.getType().equals(status);
    }

    /**
     * 检查账号是否注销/挂失
     *
     * @param status
     * @return
     */
    public static boolean isCanceled(String status) {
        return AcctStatus.CANCELED.getType().equals(status);
    }
}
