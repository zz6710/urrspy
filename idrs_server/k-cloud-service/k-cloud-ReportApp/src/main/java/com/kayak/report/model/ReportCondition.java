package com.kayak.report.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "reportConditionService", table = "sys_report_condition")
public class ReportCondition {

    @GraphQLField(key = true, kkhtml = "KFieldText", label = "id", sql = "id = $S{id}", field = "id")
    private String id;

    @GraphQLField(kkhtml = "KFieldText", label = "报表名", sql = "report_name = $S{reportName}", field = "report_name")
    private String reportName;

    @GraphQLField(kkhtml = "KFieldText", label = "菜单ID", sql = "for_table = $S{forTable}", field = "for_table")
    private String forTable;

    @GraphQLField(kkhtml = "KFieldText", label = "控件类型", sql = "obj_type = $S{objType}", field = "obj_type")
    private String objType;

    @GraphQLField(kkhtml = "KFieldText", label = "是否为报表", sql = "is_report = $S{isReport}", field = "is_report")
    private String isReport;

    @GraphQLField(kkhtml = "KFieldText", label = "按钮列表", sql = "button_ids = $S{buttonIds}", field = "button_ids")
    private String buttonIds;

    @GraphQLField(kkhtml = "KFieldText", label = "录入柜员", sql = "inputuser = $S{inputuser}", field = "inputuser")
    private String inputuser;

    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}", field = "crt_date")
    private String crtDate;

    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}", field = "crt_time")
    private String crtTime;

    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}", field = "upd_date")
    private String updDate;

    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}", field = "upd_time")
    private String updTime;

    @GraphQLField(kkhtml = "KFieldText", label = "导入报表时初始化的SQL", sql = "init_sql = $S{initSql}", field = "init_sql")
    private String initSql;

    @GraphQLField(sql = "a.menuid like '%$U{menuid}%'", field = "menuid")
    private String menuid;

    @GraphQLField(sql = "a.menuname like '%$U{menuname}%'", field = "menuname")
    private String menuname;

    @GraphQLField(sql = "upperid = $S{upperid}", field = "upperid")
    private String upperid;

    @GraphQLField(sql = "url = $S{url}", field = "url")
    private String url;

    @GraphQLField(sql = "icon = $S{icon}", field = "icon")
    private String icon;

    @GraphQLField(sql = "loadorder = $S{loadorder}", field = "loadorder")
    private String loadorder;

    @GraphQLField(sql = "status = $S{status}", field = "status")
    private String status;

    @GraphQLField(sql = "pageid = $S{pageid}", field = "pageid")
    private String pageid;

    @GraphQLField(sql = "reporturl = $S{reporturl}", field = "reporturl")
    private String reporturl;

    @GraphQLField(sql = "jimuReportId = $S{jimuReportId}", field = "jimu_report_id")
    private String jimuReportId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getForTable() {
        return forTable;
    }

    public void setForTable(String forTable) {
        this.forTable = forTable;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getIsReport() {
        return isReport;
    }

    public void setIsReport(String isReport) {
        this.isReport = isReport;
    }

    public String getButtonIds() {
        return buttonIds;
    }

    public void setButtonIds(String buttonIds) {
        this.buttonIds = buttonIds;
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

    public String getInitSql() {
        return initSql;
    }

    public void setInitSql(String initSql) {
        this.initSql = initSql;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getUpperid() {
        return upperid;
    }

    public void setUpperid(String upperid) {
        this.upperid = upperid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLoadorder() {
        return loadorder;
    }

    public void setLoadorder(String loadorder) {
        this.loadorder = loadorder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public String getReporturl() {
        return reporturl;
    }

    public void setReporturl(String reporturl) {
        this.reporturl = reporturl;
    }

    public String getJimuReportId() {
        return jimuReportId;
    }

    public void setJimuReportId(String jimuReportId) {
        this.jimuReportId = jimuReportId;
    }
}