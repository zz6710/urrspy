package com.kayak.pms.basePublish.enums;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2022/1/19 15:48
 */
//私募资管产品明细字段枚举类
public enum RegularAssetEnum {
    FIXED_INCOME_INVEST("固定收益投资", "fixedIncomeInvest", "7"),
    EQUITY_INVEST("权益投资", "equityInvest", "8"),
    FINANCIAL_INVEST("金融衍生品投资", "financialInvest", "9"),
    OTHER_INVEST("商品及其他投资", "otherInvest", "10"),
    PUBLIC_PROD_INVEST("公募资管产品", "publicProdInvest", "11");
    private final String assetsType;//资产种类
    private final String fieldName;//属性名称
    private final String rowNumbers;//序号
    RegularAssetEnum(String assetsType, String fieldName, String rowNumbers) {
        this.assetsType = assetsType;
        this.fieldName = fieldName;
        this.rowNumbers = rowNumbers;
    }

    public String getAssetsType() {
        return assetsType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getRowNumbers() {
        return rowNumbers;
    }
}
