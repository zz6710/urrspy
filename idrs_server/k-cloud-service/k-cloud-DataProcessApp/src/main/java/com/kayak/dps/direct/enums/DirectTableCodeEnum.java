package com.kayak.dps.direct.enums;

/**
 * 直联表与代码，表名称映射关系
 */
public enum DirectTableCodeEnum {
    PRODUCT_FILING("app_prod_regist_filing_info", "113", "产品申报登记信息管理"),

    PRODUCT_ISSUANCE("app_prod_issuance_regist_info", "114", "产品发行登记信息管理"),

    PROD_STATE("app_prod_state_regist_info", "115", "产品状态登记"),

    INITIAL_SUBSCRIPTION("app_initial_sub_regist_info", "104", "募集总量登记管理"),

    SUBSEQUENT_SUBSCRIPTION("app_subseq_subscr_regist_info", "105", "产品存续期登记管理"),

    ASSET_DEBT_REGISTER("app_asset_debt_register_info", "106", "资产负债要素登记管理"),

    PRODUCT_TRANSFER("app_prod_trans_regist_info", "108", "交易信息登记管理"),

    ASSET_REGISTER("app_asset_regist_info", "117", "资产持仓登记管理"),

    // APPRAISAL_REGISTER("app_appraise_regist_info", "109", "估值信息登记管理"),

    UNDERLYING_ASSET("app_under_asset_regist_info", "110", "底层资产持仓管理"),

    TERMINATION_REGISTER("app_termination_regist_info", "111", "产品终止登记管理"),

    CUSTOMER_REGISTER("app_cust_register_info", "201", "投资者身份信息登记管理"),

    CUSTOMER_VOLUME("app_cust_vol_register_info", "202", "投资者持有信息登记管理"),

    CUSTOMER_TRANSACTION("app_cust_trans_info", "203", "投资者明细信息登记管理"),

    PRDNAV_INFORMATION("app_nav_info_reg", "118", "净值信息登记");



    private String tableName;
    private String mapCode;
    private String desc;

    public static DirectTableCodeEnum fromMapCode(String mapCode) {
        if (mapCode == null) {
            return null;
        }
        for (DirectTableCodeEnum mapping : values()) {
            if (mapCode.equals(mapping.getMapCode())) {
                return mapping;
            }
        }
        return null;
    }

    /**
     * 根据mapCode直接获取tableName
     */
    public static String getTableNameByMapCode(String mapCode) {
        DirectTableCodeEnum mapping = fromMapCode(mapCode);
        return mapping != null ? mapping.getTableName() : null;
    }

    DirectTableCodeEnum(String tableName, String mapCode, String desc) {
        this.tableName = tableName;
        this.mapCode = mapCode;
        this.desc = desc;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getMapCode() {
        return mapCode;
    }

    public void setMapCode(String mapCode) {
        this.mapCode = mapCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
