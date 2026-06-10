package com.kayak.config.enums;

/**
 * 报送报表枚举
 */
public enum ReportTableEnums {

    RH1_1("app_pbc_report_1_1", "人行1-1 资管产品募集余额统计"),
    RH1_2("app_pbc_report_1_2", "人行1-2 资管产品募集及兑付发生额统计"),
    RH1_3("app_pbc_report_1_3", "人行1-3 资管产品只数情况统计"),
    RH1_4("app_pbc_report_1_4", "人行1-4 资管产品境内募集余额分地区统计"),
    ZZ1_PRD_ISU_REG("app_prd_isu_reg", "中债-公私募产品发行登记");

    private final String table_name;//表英文名

    private final String table_comment;//表中文注释

    ReportTableEnums(String table_name, String table_comment) {
        this.table_name = table_name;
        this.table_comment = table_comment;
    }

    public String getTable_name() {
        return table_name;
    }

    public String getTable_comment() {
        return table_comment;
    }

}
