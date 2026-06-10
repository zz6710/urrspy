package com.kayak.dps.expresssion.enums;

/**
 * 条件表达式函数枚举
 */
public enum ExpressionRegrexEnum {

    EMAIL("EMAIL", "邮箱"),
    IDENT("IDENT", "32或者46位数字或字母"),
    TEL("TEL", "电话"),
    FIX_TEL("FIX_TEL", "固定电话"),
    LCZS("LCZS", "理财登记培训证书编号, LC+12 位数字"),
    RATE("RATE", "末尾不带百分号"),
    CREDIT_CD("CREDIT_CD", "15或18位数字或字母"),
    SPV("SPV", "特殊编码校验"),
    SOC_CRED_CD("SOC_CRED_CD", "统一社会信用代码"),
    OCC_PRC_CD("OCC_PRC_CD", "全国组织机构代码"),
    RZR_ZJ_CD("RZR_ZJ_CD", "融资人组织机构（社会信用）代码"),
    ZJ_TG_ZH("ZJ_TG_ZH", "资金托管账号"),
    SFZ_CD("SFZ_CD", "身份证验证"),
    YHDM("YHDM", "银行代码"),
    HHMMSS("HHMMSS", "时分秒");


    ExpressionRegrexEnum(String val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    private String val;
    private String desc;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
