package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import lombok.Data;

@Data
public class CheckIndexModel {

    @GraphQLField(kkhtml = "KFieldText", label = "校验指标代码", field = "index_code")
    private String indexCode;

    @GraphQLField(kkhtml = "KFieldText", label = "校验指标名称", field = "index_name")
    private String indexName;

    @GraphQLField(kkhtml = "KFieldText", label = "校验指标标识", field = "index_rule")
    private String indexRule;

    @GraphQLField(kkhtml = "KFieldText", label = "关联报表名称", field = "report_table")
    private String reportTable;

    @GraphQLField(kkhtml = "KFieldText", label = "校验字段行", field = "row_num")
    private String rowNum;

    @GraphQLField(kkhtml = "KFieldText", label = "校验字段列", field = "column_num")
    private String columnNum;

    @GraphQLField(kkhtml = "KFieldText", label = "校验字段行名称", field = "row_name")
    private String rowName;

    @GraphQLField(kkhtml = "KFieldText", label = "校验字段列名称", field = "list_name")
    private String listName;

    @GraphQLField(kkhtml = "KFieldText", label = "校验指标类型:rpt_validate_type", field = "index_type")
    private String indexType;

    @GraphQLField(kkhtml = "KFieldText", label = "校验表达式", field = "express")
    private String express;

    @GraphQLField(kkhtml = "KFieldText", label = "关联表信息", field = "relation_tables")
    private String relationTables;

    @GraphQLField(kkhtml = "KFieldText", label = "关联关系", field = "tables_relationships")
    private String tablesRelationships;

    @GraphQLField(kkhtml = "KFieldText", label = "表达式准许差值", field = "allow_deviation")
    private String allowDeviation;

    @GraphQLField(kkhtml = "KFieldText", label = "校验指标详述", field = "index_detail")
    private String indexDetail;

    @GraphQLField(kkhtml = "KFieldText", label = "校验正常日志模板", field = "correct_prompt")
    private String correctPrompt;

    @GraphQLField(kkhtml = "KFieldText", label = "校验异常日志模板", field = "error_prompt")
    private String errorPrompt;

    @GraphQLField(kkhtml = "KFieldText", label = "备注", field = "remark")
    private String remark;

    private String splitFlag;
    private String reportFreq;
    private String isRevalue;
    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public String getReportTable() {
        return reportTable;
    }

    public void setReportTable(String reportTable) {
        this.reportTable = reportTable;
    }

    public String getRowNum() {
        return rowNum;
    }

    public void setRowNum(String rowNum) {
        this.rowNum = rowNum;
    }

    public String getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(String columnNum) {
        this.columnNum = columnNum;
    }

    public String getRowName() {
        return rowName;
    }

    public void setRowName(String rowName) {
        this.rowName = rowName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getAllowDeviation() {
        return allowDeviation;
    }

    public void setAllowDeviation(String allowDeviation) {
        this.allowDeviation = allowDeviation;
    }

    public String getIndexDetail() {
        return indexDetail;
    }

    public void setIndexDetail(String indexDetail) {
        this.indexDetail = indexDetail;
    }

    public String getCorrectPrompt() {
        return correctPrompt;
    }

    public void setCorrectPrompt(String correctPrompt) {
        this.correctPrompt = correctPrompt;
    }

    public String getErrorPrompt() {
        return errorPrompt;
    }

    public void setErrorPrompt(String errorPrompt) {
        this.errorPrompt = errorPrompt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getRelationTables() {
        return relationTables;
    }

    public void setRelationTables(String relationTables) {
        this.relationTables = relationTables;
    }

    public String getTablesRelationships() {
        return tablesRelationships;
    }

    public void setTablesRelationships(String tablesRelationships) {
        this.tablesRelationships = tablesRelationships;
    }

    public String getIndexRule() {
        return indexRule;
    }

    public void setIndexRule(String indexRule) {
        this.indexRule = indexRule;
    }
}