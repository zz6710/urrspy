package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
public class ValidationIndexTempModel {

    @GraphQLField(kkhtml = "KFieldText", label = "id" ,field = "id")
    private String id;

    @GraphQLField(kkhtml = "KFieldText", label = "校验指标代码", field = "index_code")
    private String indexCode;

    @GraphQLField(kkhtml = "KFieldText", label = "关联报表名称", field = "report_table")
    private String reportTable;

    @GraphQLField(kkhtml = "KFieldText", label = "项目列(一维)", field = "project_column")
    private String projectColumn;

    @GraphQLField(kkhtml = "KFieldText", label = "校验指标名称(一维)", field = "index_name")
    private String indexName;

    @GraphQLField(kkhtml = "KFieldText", label = "列编号(二维)", field = "column_code")
    private String columnCode;

    @GraphQLField(kkhtml = "KFieldText", label = "列项目名称(二维)", field = "column_name")
    private String columnName;

    @GraphQLField(kkhtml = "KFieldText", label = "行编号(二维)", field = "row_code")
    private String rowCode;

    @GraphQLField(kkhtml = "KFieldText", label = "行项目名称(二维)", field = "row_name")
    private String rowName;

    @GraphQLField(kkhtml = "KFieldText", label = "校验指标类型:rpt_validate_type", field = "index_type")
    private String indexType;

    @GraphQLField(kkhtml = "KFieldText", label = "校验指标详述", field = "index_detail")
    private String indexDetail;

    @GraphQLField(kkhtml = "KFieldText", label = "校验正常日志模板", field = "correct_prompt")
    private String correctPrompt;

    @GraphQLField(kkhtml = "KFieldText", label = "校验异常日志模板", field = "error_prompt")
    private String errorPrompt;

    @GraphQLField(kkhtml = "KFieldText", label = "备注", field = "remark")
    private String remark;

    @GraphQLField(kkhtml = "KFieldText", label = "校验项目类型:1-一维/2-二维", field = "project_type")
    private String projectType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getProjectColumn() {
        return projectColumn;
    }

    public void setProjectColumn(String projectColumn) {
        this.projectColumn = projectColumn;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getRowCode() {
        return rowCode;
    }

    public void setRowCode(String rowCode) {
        this.rowCode = rowCode;
    }

    public String getRowName() {
        return rowName;
    }

    public void setRowName(String rowName) {
        this.rowName = rowName;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
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

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
}