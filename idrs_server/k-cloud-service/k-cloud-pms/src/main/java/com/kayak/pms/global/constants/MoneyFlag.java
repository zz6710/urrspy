package com.kayak.pms.global.constants;

//费用款项名称
public enum  MoneyFlag {

    DISTRIBUTOR_SUB("001","销售商认购款"),
    DISTRIBUTOR_APPLY("002","销售商申购款"),
    DISTRIBUTOR_REDEEM("003","销售商赎回款"),
    DISTRIBUTOR_CASH_DIVIDENDS("004","销售商现金分红"),
    DISTRIBUTOR_TERMIATED_REFUND("006","销售商终止退款"),
    DISTRIBUTOR_SUB_REFUND("007","销售商认购退款"),
    ISSUE_FAILED_REFUND("010","发行失败退款"),
    TRUTEE_APPLY("102","托管行申购款"),
    TRUTEE_REDEEM("103","托管行赎回款"),
    TRUTEE_TRANSFORM_OUT("104","托管行基金转换出"),
    TRUTEE_PROD_IN("105","托管行产品转入"),
    TRUTEE_DIVIDENDS("106","托管行分红"),
    TRUTEE_END_REFUND("107","托管行终止退款"),
    PROD_ESTABLISH("110","产品成立划款");

    private String desc;
    private String code;


    MoneyFlag(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }

    public static MoneyFlag codeOf(String code){
        for (MoneyFlag moneyFlag : values()) {
            if(moneyFlag.getCode().equals(code)){
                return moneyFlag;
            }
        }
        throw new RuntimeException("款项名称不存在");
    }
}
