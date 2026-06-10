package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @author XueJ
 * @version 1.0.0
 * @ClassName ImportTemplateManage.java
 * @Description TODO
 * @createTime 2023年08月11日 19:30:00
 */
@Data
@GraphQLModel(fetcher = "importTemplateManageField02Service",table = "import_template_manage_field_02")
public class ImportTemplateManageField02 {

    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "系统表名", sql = "system_table_name = $S{systemTableName}" ,field = "system_table_name")
    private String systemTableName;
    @GraphQLField(kkhtml = "KFieldText", label = "取值行", sql = "value_row = $S{valueRow}" ,field = "value_row")
    private String valueRow;
    @GraphQLField(kkhtml = "KFieldText", label = "取值列", sql = "value_column = $S{valueColumn}" ,field = "value_column")
    private String valueColumn;
    @GraphQLField(kkhtml = "KFieldText", label = "字段类型", sql = "column_type = $S{columnType}" ,field = "column_type")
    private String columnType;
    @GraphQLField(kkhtml = "KFieldText", label = "数据单位", sql = "column_unit = $S{columnUnit}" ,field = "column_unit")
    private String columnUnit;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人员", sql = "upt_usr = $S{uptUsr}" ,field = "upt_usr")
    private String uptUsr;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upt_date = $S{uptDate}" ,field = "upt_date")
    private String uptDate;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upt_time = $S{uptTime}" ,field = "upt_time")
    private String uptTime;

    /*表名*/
    private  String tableName;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSystemTableName() {
        return systemTableName;
    }

    public void setSystemTableName(String systemTableName) {
        this.systemTableName = systemTableName;
    }

    public String getValueRow() {
        return valueRow;
    }

    public void setValueRow(String valueRow) {
        this.valueRow = valueRow;
    }

    public String getValueColumn() {
        return valueColumn;
    }

    public void setValueColumn(String valueColumn) {
        this.valueColumn = valueColumn;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnUnit() { return columnUnit; }

    public void setColumnUnit(String columnUnit) {
        this.columnUnit = columnUnit;
    }

    public String getUptUsr() {
        return uptUsr;
    }

    public void setUptUsr(String uptUsr) {
        this.uptUsr = uptUsr;
    }

    public String getUptDate() {
        return uptDate;
    }

    public void setUptDate(String uptDate) {
        this.uptDate = uptDate;
    }

    public String getUptTime() {
        return uptTime;
    }

    public void setUptTime(String uptTime) {
        this.uptTime = uptTime;
    }
}
