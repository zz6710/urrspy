package com.kayak.rpt.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "baseReportResultsService",table = "base_report_result")
public class BaseReportResultModel {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "报表大类", sql = "report_type = $S{reportType}" ,field = "report_type")
   private String reportType;
   @GraphQLField(kkhtml = "KFieldText", label = "系统关联表", sql = "report_table = $S{reportTable}" ,field = "report_table")
   private String reportTable;
   @GraphQLField(kkhtml = "KFieldText", label = "报表名称", sql = "report_table_name like '%$U{reportTableName}%'" ,field = "report_table_name")
   private String reportTableName;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_reg_enc=$S{prodRegEnc} " ,field = "prod_reg_enc")
   private String prodRegEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
   private String theoryReportStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
   private String theoryReportEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "实际报送日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "总数量", sql = "total = $S{total}" ,field = "total")
   private String total;
   @GraphQLField(kkhtml = "KFieldText", label = "报送成功数量", sql = "report_success_number = $S{reportSuccessNumber}" ,field = "report_success_number")
   private String reportSuccessNumber;
   @GraphQLField(kkhtml = "KFieldText", label = "整体状态", sql = "status = $S{status}" ,field = "status")
   private String status;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "update_date = $S{updateDate}" ,field = "update_date")
   private String updateDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "update_time = $S{updateTime}" ,field = "update_time")
   private String updateTime;
   @GraphQLField(kkhtml = "KFieldText", label = "开始时间", sql = "theory_report_start_date >= $S{startDate}" ,field = "start_date")
   private String startDate;
   @GraphQLField(kkhtml = "KFieldText", label = "结束时间", sql = "theory_report_start_date <= $S{endDate}" ,field = "end_date")
   private String endDate;
    //产品代码
    @GraphQLField(label = "产品代码", field = "prodCodes")
    private String prodCodes;
    //报送状态
    @GraphQLField(label = "报送状态", field = "registerStatus")
    private String registerStatus;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportTable() {
        return reportTable;
    }

    public void setReportTable(String reportTable) {
        this.reportTable = reportTable;
    }

    public String getProdRegEnc() {
        return prodRegEnc;
    }

    public void setProdRegEnc(String prodRegEnc) {
        this.prodRegEnc = prodRegEnc;
    }

    public String getReportTableName() {
        return reportTableName;
    }

    public void setReportTableName(String reportTableName) {
        this.reportTableName = reportTableName;
    }

    public String getTheoryReportStartDate() {
        return theoryReportStartDate;
    }

    public void setTheoryReportStartDate(String theoryReportStartDate) {
        this.theoryReportStartDate = theoryReportStartDate;
    }

    public String getTheoryReportEndDate() {
        return theoryReportEndDate;
    }

    public void setTheoryReportEndDate(String theoryReportEndDate) {
        this.theoryReportEndDate = theoryReportEndDate;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getReportSuccessNumber() {
        return reportSuccessNumber;
    }

    public void setReportSuccessNumber(String reportSuccessNumber) {
        this.reportSuccessNumber = reportSuccessNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}