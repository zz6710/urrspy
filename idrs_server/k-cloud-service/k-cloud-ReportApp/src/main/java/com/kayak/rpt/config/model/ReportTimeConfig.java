package com.kayak.rpt.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "reportTimeConfigService",table = "base_submission_time_config")
public class ReportTimeConfig {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "报表大类", sql = "report_type = $S{reportType}" ,field = "report_type")
   private String reportType;
   @GraphQLField(kkhtml = "KFieldText", label = "报送报表名称", sql = "report_table = $S{reportTable}" ,field = "report_table")
   private String reportTable;
    @GraphQLField(kkhtml = "KFieldText", label = "报送报表中文名称")
    private String tableName;
   @GraphQLField(kkhtml = "KFieldText", label = "基准日期", sql = "base_type = $S{baseType}" ,field = "base_type")
   private String baseType;
   @GraphQLField(kkhtml = "KFieldText", label = "日期类型", sql = "data_type = $S{dataType}" ,field = "data_type")
   private String dataType;
   @GraphQLField(kkhtml = "KFieldText", label = "行内报送时点要求", sql = "inner_submission_time_require = $S{innerSubmissionTimeRequire}" ,field = "inner_submission_time_require")
   private String innerSubmissionTimeRequire;
    @GraphQLField(kkhtml = "KFieldText", label = "行内报送时点要求标识（正负号）",field = "inner_submission_time")
    private String innerSubmissionTime;
    @GraphQLField(kkhtml = "KFieldText", label = "监管报送时点要求标识（正负号）",field = "supervise_submission_time")
    private String superviseSubmissionTime;
   @GraphQLField(kkhtml = "KFieldText", label = "监管报送时点要求", sql = "supervise_submission_time_require = $S{superviseSubmissionTimeRequire}" ,field = "supervise_submission_time_require")
   private String superviseSubmissionTimeRequire;
   @GraphQLField(kkhtml = "KFieldText", label = "报送数据生成日期(工作日)（正负号）",field = "data_gener_time")
   private String dataGenerTime;
   @GraphQLField(kkhtml = "KFieldText", label = "报送数据生成日期(工作日)", sql = "data_gener_time_require = $S{dataGenerTimeRequire}" ,field = "data_gener_time_require")
   private String dataGenerTimeRequire;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "update_date = $S{updateDate}" ,field = "update_date")
   private String updateDate;
   @GraphQLField(kkhtml = "KFieldText", label = "时点类型", sql = "time_type = $S{timeType}" ,field = "time_type")
   private String timeType;
    @GraphQLField(kkhtml = "KFieldText", label = "非规则配置的截止日期列表")
   private String endDateString;

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
  	public String getBaseType() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }
  	public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
  	public String getInnerSubmissionTimeRequire() {
        return innerSubmissionTimeRequire;
    }

    public void setInnerSubmissionTimeRequire(String innerSubmissionTimeRequire) {
        this.innerSubmissionTimeRequire = innerSubmissionTimeRequire;
    }
  	public String getSuperviseSubmissionTimeRequire() {
        return superviseSubmissionTimeRequire;
    }

    public void setSuperviseSubmissionTimeRequire(String superviseSubmissionTimeRequire) {
        this.superviseSubmissionTimeRequire = superviseSubmissionTimeRequire;
    }
  	public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
  	public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getInnerSubmissionTime() {
        return innerSubmissionTime;
    }

    public void setInnerSubmissionTime(String innerSubmissionTime) {
        this.innerSubmissionTime = innerSubmissionTime;
    }

    public String getSuperviseSubmissionTime() {
        return superviseSubmissionTime;
    }

    public void setSuperviseSubmissionTime(String superviseSubmissionTime) {
        this.superviseSubmissionTime = superviseSubmissionTime;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }
}