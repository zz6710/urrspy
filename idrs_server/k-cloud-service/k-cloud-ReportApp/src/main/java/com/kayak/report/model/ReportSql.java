package com.kayak.report.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "reportSqlService", table = "sys_report_sql")
public class ReportSql {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "id", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "功能对应的SQL编号", sql = "exeid = $S{exeid}", field = "exeid")
    private String exeid;
    @GraphQLField(kkhtml = "KFieldText", label = "sqlid", sql = "sqlid = $S{sqlid}", field = "sqlid")
    private String sqlid;
    @GraphQLField(kkhtml = "KFieldText", label = "报表sql", sql = "report_sql = $S{reportSql}", field = "report_sql")
    private String reportSql;
    @GraphQLField(kkhtml = "KFieldText", label = "录入柜员", sql = "inputuser = $S{inputuser}", field = "inputuser")
    private String inputuser;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期    ", sql = "crt_date = $S{crtDate}", field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}", field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}", field = "upd_date")
    private String updDate;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}", field = "upd_time")
    private String updTime;

    @GraphQLField(sql = "b.for_table = $S{forTable}", field = "for_table")
    private String forTable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExeid() {
        return exeid;
    }

    public void setExeid(String exeid) {
        this.exeid = exeid;
    }

    public String getSqlid() {
        return sqlid;
    }

    public void setSqlid(String sqlid) {
        this.sqlid = sqlid;
    }

    public String getReportSql() {
        return reportSql;
    }

    public void setReportSql(String reportSql) {
        this.reportSql = reportSql;
    }

    public String getInputuser() {
        return inputuser;
    }

    public void setInputuser(String inputuser) {
        this.inputuser = inputuser;
    }

    public String getCrtDate() {
        return crtDate;
    }

    public void setCrtDate(String crtDate) {
        this.crtDate = crtDate;
    }

    public String getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(String crtTime) {
        this.crtTime = crtTime;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }

    public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
        this.updTime = updTime;
    }

    public String getForTable() {
        return forTable;
    }

    public void setForTable(String forTable) {
        this.forTable = forTable;
    }
}