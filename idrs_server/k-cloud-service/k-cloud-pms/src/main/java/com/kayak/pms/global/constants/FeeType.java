package com.kayak.pms.global.constants;

/**
 * 描述：费用类型
 * 费用类型（0-认购费，1-申购费，2-赎回费，3-转换费，4-转换补差费，5-定投费，6-违约赎回费）
 * @author grt
 * @date 2020-05-09
 */
public enum FeeType {
    SUB("0", "认购费"),
    APPLY("1", "申购费"),
    REDEEM("2", "赎回费"),
    TRANSFORM("3", "转换费"),
    TRANSFORM_MARGIN("4", "转换补差费"),
    AIP("5", "定投费"),
    VIOLATE_REDEEM("6", "违约赎回费"),
    SALES_SERVICE("7", "销售服务费"),
    MANAGE_SERVICE("8", "管理费"),
    ;
    private String desc;
    private String code;

    FeeType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }

    public static FeeType codeOf(String code){
        for (FeeType feeType : values()) {
            if(feeType.getCode().equals(code)){
                return feeType;
            }
        }
        throw new RuntimeException("费率类型不存在");
    }
}
