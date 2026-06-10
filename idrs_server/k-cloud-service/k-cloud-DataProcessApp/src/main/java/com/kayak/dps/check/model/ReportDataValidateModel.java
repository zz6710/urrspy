package com.kayak.dps.check.model;

public class ReportDataValidateModel {
    //校验指标编号
    private String indexCode = "";
    //关联报表名称
    private String reportTable = "";
    //校验字段行
    private String rowNum = "";
    //校验字段列
    private String columnNum = "";
    //报表行名称
    private String rowName = "";
    //报表列名称
    private String listName = "";
    //校验类别:01-非空校验/02-值域校验/03-字段格式及长度校验/04-文件格式及大小校验/05-字段联动校验/06-数字校验/07-重复性校验/08-身份证校验/09-计算校验
    private String indexType = "";
    //校验公式
    private String express = "";
    //允许差值
    private String allowDeviation = "";
    //校验指标详述
    private String indexDetail = "";
    //校验正常日志模板
    private String correctPrompt = "";
    //校验异常日志模板
    private String errorPrompt = "";
    //备注
    private String remark = "";

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

    public String getAllowDeviation() {
        return allowDeviation;
    }

    public void setAllowDeviation(String allowDeviation) {
        this.allowDeviation = allowDeviation;
    }

}
