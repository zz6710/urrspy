package com.kayak.rpt.rhzg.biz;

public enum RHZGTableStrEnum {
    ZG01("ods_rhzg_zg01","ZG01"),
    ZG02("ods_rhzg_zg02","ZG02"),
    ZG03("ods_rhzg_zg03","ZG03"),
    ZG04("ods_rhzg_zg04","ZG04"),
    ZG05("ods_rhzg_zg05","ZG05"),
    ZG06("ods_rhzg_zg06","ZG06"),
    ZG07("ods_rhzg_zg07","ZG07"),
    ZG08("ods_rhzg_zg08","ZG08"),
    ZG09("ods_rhzg_zg09","ZG09"),
    ZG10("ods_rhzg_zg10","ZG10"),
    ZG11("ods_rhzg_zg11","ZG11"),
    ZG12("ods_rhzg_zg12","ZG12"),
    ZG13("ods_rhzg_zg13","ZG13"),
    InterbankDepositInfo("app_interbank_deposit_info","CLTYCK"),
    InterbankDepositAmountInfo("app_interbank_deposit_amount_info","TYCKFS"),
    BondInvestInfo("app_bond_invest_info","CLZQTZ"),
    BondInvestAmountInfo("app_bond_invest_amount_info","ZQTZFS"),
//    SpvInvestAmountInfo("app_spv_invest_amount_info","SPVIAI"),
    SpvInvestAmountInfo("app_spv_invest_amount_info","SPVFSX"),
    SpvInvestInfo("app_spv_invest_info","SPVTZX"),
    ;


    //接口代码
    private String protCode;
    //表名字符串  对应报送文件
    private String tableStr;

    RHZGTableStrEnum(String protCode, String tableStr) {
        this.protCode = protCode;
        this.tableStr = tableStr;
    }

    public String getProtCode() {
        return protCode;
    }

    public void setProtCode(String protCode) {
        this.protCode = protCode;
    }

    public String getTableStr() {
        return tableStr;
    }

    public void setTableStr(String tableStr) {
        this.tableStr = tableStr;
    }

    public static String getProtCodeByTableStr(String tableStr){

        RHZGTableStrEnum[] values = RHZGTableStrEnum.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].getTableStr().equals(tableStr)){
                return values[i].getProtCode();
            }
        }
        return "";
    }
}
