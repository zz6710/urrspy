package com.kayak.pms.global.constants;

/**
 * 产品状态
 *
 * @author FangruiZhu
 * @date 2019-02-23
 **/
public enum ProdStatus {
    /**
     * 可申购赎回
     */
    CAN_APPLY_REDEEM("0"),

    /**
     * 发行
     */
    COLLECT("1"),
    
    /**
     * 发行成功
     */
    ESTABLISH_SUCCESS("2"),
    
    /**
     * 发行失败
     */
    ESTABLISH_FAILURE("3"),
    
    /**
     * 停止申购赎回
     */
    STOP_APPLY_REDEEM("4"),

    /**
     * 停止申购5
     */
    STOP_APPLY("5"),

    /**
     * 停止赎回6
     */
    STOP_REDEEM("6"),

    /**
     * 基金终止8
     */
    PROD_TERMINAT("8"),

    /**
     * 基金封闭9
     */
    PROD_CLOSE("9"),
    
    /**
     * 发行前B
     */
    COLLECT_BEFORE("b");

    /**
     * 状态值
     */
    private final String status;

    ProdStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
