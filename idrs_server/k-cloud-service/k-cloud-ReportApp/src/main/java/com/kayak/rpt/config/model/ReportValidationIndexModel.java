package com.kayak.rpt.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "reportValidationIndexService",table = "base_reportdata_index_config")
public class ReportValidationIndexModel {
    @GraphQLField(key = true , label = "校验指标代码" ,field = "index_code")
    private String indexCode;

    @GraphQLField(key = true , label = "校验指标名称" ,field = "index_name")
    private String indexName;

    @GraphQLField(key = true , label = "是否生效" ,field = "is_effect")
    private String isEffect;

    @GraphQLField(label = "关联报表名称" ,field = "report_table")
    private String reportTable;

    @GraphQLField(label = "校验字段行" ,field = "row_num")
    private String rowNum;

    @GraphQLField(label = "校验字段列" ,field = "column_num")
    private String columnNum;

    @GraphQLField(label = "校验字段行名称" ,field = "row_name")
    private String rowName;

    @GraphQLField(label = "校验字段列名称" ,field = "list_name")
    private String listName;

    //校验类别:01-非空校验/02-值域校验/03-字段格式及长度校验/04-文件格式及大小校验/05-字段联动校验/06-数字校验/07-重复性校验/08-身份证校验/09-计算校验
    @GraphQLField(label = "校验指标类型" ,field = "index_type")
    private String indexType;

    @GraphQLField(label = "校验表达式" ,field = "express")
    private String express;

    @GraphQLField(label = "数据来源sql" ,field = "relation_tables")
    private String relationTables;

    @GraphQLField(label = "允许差值" ,field = "allow_deviation")
    private String allowDeviation;

    @GraphQLField(label = "校验指标详述" ,field = "index_detail")
    private String indexDetail;

    @GraphQLField(label = "校验正常日志模板" ,field = "correct_prompt")
    private String correctPrompt;

    @GraphQLField(label = "校验异常日志模板" ,field = "error_prompt")
    private String errorPrompt;

    @GraphQLField(label = "备注" ,field = "remark")
    private String remark;

    @GraphQLField(label = "报表名称" ,field = "table_name")
    private String tableName;

    @GraphQLField(label = "规则标识" ,field = "index_rule")
    private String indexRule;

    @GraphQLField(label = "报表类型" ,field = "report_type")
    private String reportType;

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIsEffect() {
        return isEffect;
    }

    public void setIsEffect(String isEffect) {
        this.isEffect = isEffect;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexRule() {
        return indexRule;
    }

    public void setIndexRule(String indexRule) {
        this.indexRule = indexRule;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
}
