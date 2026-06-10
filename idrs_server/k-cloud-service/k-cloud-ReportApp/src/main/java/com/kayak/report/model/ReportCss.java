package com.kayak.report.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

import java.util.List;

@GraphQLModel(fetcher = "reportCssService", table = "sys_report_css")
public class ReportCss {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "id", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "input字段id", sql = "css_id = $S{cssId}", field = "css_id")
    private String cssId;
    @GraphQLField(kkhtml = "KFieldText", label = "input字段名", sql = "css_name = $S{cssName}", field = "css_name")
    private String cssName;
    @GraphQLField(kkhtml = "KFieldText", label = "类型", sql = "css_type = $S{cssType}", field = "css_type")
    private String cssType;
    @GraphQLField(kkhtml = "KFieldText", label = "样式", sql = "css_class = $S{cssClass}", field = "css_class")
    private String cssClass;
    @GraphQLField(kkhtml = "KFieldText", label = "dataList", sql = "datalist = $S{datalist}", field = "datalist")
    private String datalist;
    @GraphQLField(kkhtml = "KFieldText", label = "data_on_change", sql = "data_on_change = $S{dataOnChange}", field = "data_on_change")
    private String dataOnChange;
    @GraphQLField(kkhtml = "KFieldText", label = "label描述", sql = "label_discrible = $S{labelDiscrible}", field = "label_discrible")
    private String labelDiscrible;
    @GraphQLField(kkhtml = "KFieldText", label = "最小长度", sql = "data_min_length = $S{dataMinLength}", field = "data_min_length")
    private String dataMinLength;
    @GraphQLField(kkhtml = "KFieldText", label = "最大长度", sql = "data_max_length = $S{dataMaxLength}", field = "data_max_length")
    private String dataMaxLength;
    @GraphQLField(kkhtml = "KFieldText", label = "直接指定下拉数据，格式：{‘value1’", sql = "data_data = $S{dataData}", field = "data_data")
    private String dataData;
    @GraphQLField(kkhtml = "KFieldText", label = "指定exeid查询为下拉数据来源", sql = "data_exeid = $S{dataExeid}", field = "data_exeid")
    private String dataExeid;
    @GraphQLField(kkhtml = "KFieldText", label = "指定数据字典为下拉数据来源", sql = "data_dict = $S{dataDict}", field = "data_dict")
    private String dataDict;
    @GraphQLField(kkhtml = "KFieldText", label = "指定查询数据中作为值的字段名称", sql = "data_value_field = $S{dataValueField}", field = "data_value_field")
    private String dataValueField;
    @GraphQLField(kkhtml = "KFieldText", label = "指定查询数据中作为文本显示的字段名称", sql = "data_display_field = $S{dataDisplayField}", field = "data_display_field")
    private String dataDisplayField;
    @GraphQLField(kkhtml = "KFieldText", label = "true则表示只能选择系统工作日日期", sql = "data_workday = $S{dataWorkday}", field = "data_workday")
    private String dataWorkday;
    @GraphQLField(kkhtml = "KFieldText", label = "指定date控件显示的日期格式，默认为yyyy-dd-mm", sql = "data_date_format = $S{dataDateFormat}", field = "data_date_format")
    private String dataDateFormat;
    @GraphQLField(kkhtml = "KFieldText", label = "设置默认值", sql = "data_value = $S{dataValue}", field = "data_value")
    private String dataValue;
    @GraphQLField(kkhtml = "KFieldText", label = "true/false，是否允许为空", sql = "data_allowblank = $S{dataAllowblank}", field = "data_allowblank")
    private String dataAllowblank;
    @GraphQLField(kkhtml = "KFieldText", label = "指定字段最小值", sql = "data_min_value = $S{dataMinValue}", field = "data_min_value")
    private String dataMinValue;
    @GraphQLField(kkhtml = "KFieldText", label = "指定字段最大值", sql = "data_max_value = $S{dataMaxValue}", field = "data_max_value")
    private String dataMaxValue;
    @GraphQLField(kkhtml = "KFieldText", label = "用来识别是否是同一个li下面的input", sql = "ligroupid = $S{ligroupid}", field = "ligroupid")
    private String ligroupid;
    @GraphQLField(kkhtml = "KFieldText", label = "报表中查询的输入框中输入数据类型的控制", sql = "validatetype = $S{validatetype}", field = "validatetype")
    private String validatetype;
    @GraphQLField(kkhtml = "KFieldText", label = "SQL语句", sql = "data_sqlinfo = $S{dataSqlinfo}", field = "data_sqlinfo")
    private String dataSqlinfo;
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
    @GraphQLField(kkhtml = "KFieldText", label = "模式", sql = "data_define = $S{dataDefine}", field = "data_define")
    private String dataDefine;
    @GraphQLField(kkhtml = "KFieldText", sql = "remark = $S{remark}", field = "remark")
    private String remark;
    @GraphQLField(kkhtml = "KFieldText", sql = "data_select_branch = $S{dataSelectBranch}", field = "data_select_branch")
    private String dataSelectBranch;

    @GraphQLField(sql = "a.for_table = $S{forTable}", field = "for_table")
    private String forTable;

    @GraphQLField
    private List<ReportCss> reportCssList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCssId() {
        return cssId;
    }

    public void setCssId(String cssId) {
        this.cssId = cssId;
    }

    public String getCssName() {
        return cssName;
    }

    public void setCssName(String cssName) {
        this.cssName = cssName;
    }

    public String getCssType() {
        return cssType;
    }

    public void setCssType(String cssType) {
        this.cssType = cssType;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getDatalist() {
        return datalist;
    }

    public void setDatalist(String datalist) {
        this.datalist = datalist;
    }

    public String getDataOnChange() {
        return dataOnChange;
    }

    public void setDataOnChange(String dataOnChange) {
        this.dataOnChange = dataOnChange;
    }

    public String getLabelDiscrible() {
        return labelDiscrible;
    }

    public void setLabelDiscrible(String labelDiscrible) {
        this.labelDiscrible = labelDiscrible;
    }

    public String getDataMinLength() {
        return dataMinLength;
    }

    public void setDataMinLength(String dataMinLength) {
        this.dataMinLength = dataMinLength;
    }

    public String getDataMaxLength() {
        return dataMaxLength;
    }

    public void setDataMaxLength(String dataMaxLength) {
        this.dataMaxLength = dataMaxLength;
    }

    public String getDataData() {
        return dataData;
    }

    public void setDataData(String dataData) {
        this.dataData = dataData;
    }

    public String getDataExeid() {
        return dataExeid;
    }

    public void setDataExeid(String dataExeid) {
        this.dataExeid = dataExeid;
    }

    public String getDataDict() {
        return dataDict;
    }

    public void setDataDict(String dataDict) {
        this.dataDict = dataDict;
    }

    public String getDataValueField() {
        return dataValueField;
    }

    public void setDataValueField(String dataValueField) {
        this.dataValueField = dataValueField;
    }

    public String getDataDisplayField() {
        return dataDisplayField;
    }

    public void setDataDisplayField(String dataDisplayField) {
        this.dataDisplayField = dataDisplayField;
    }

    public String getDataWorkday() {
        return dataWorkday;
    }

    public void setDataWorkday(String dataWorkday) {
        this.dataWorkday = dataWorkday;
    }

    public String getDataDateFormat() {
        return dataDateFormat;
    }

    public void setDataDateFormat(String dataDateFormat) {
        this.dataDateFormat = dataDateFormat;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getDataAllowblank() {
        return dataAllowblank;
    }

    public void setDataAllowblank(String dataAllowblank) {
        this.dataAllowblank = dataAllowblank;
    }

    public String getDataMinValue() {
        return dataMinValue;
    }

    public void setDataMinValue(String dataMinValue) {
        this.dataMinValue = dataMinValue;
    }

    public String getDataMaxValue() {
        return dataMaxValue;
    }

    public void setDataMaxValue(String dataMaxValue) {
        this.dataMaxValue = dataMaxValue;
    }

    public String getLigroupid() {
        return ligroupid;
    }

    public void setLigroupid(String ligroupid) {
        this.ligroupid = ligroupid;
    }

    public String getValidatetype() {
        return validatetype;
    }

    public void setValidatetype(String validatetype) {
        this.validatetype = validatetype;
    }

    public String getDataSqlinfo() {
        return dataSqlinfo;
    }

    public void setDataSqlinfo(String dataSqlinfo) {
        this.dataSqlinfo = dataSqlinfo;
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

    public String getDataDefine() {
        return dataDefine;
    }

    public void setDataDefine(String dataDefine) {
        this.dataDefine = dataDefine;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDataSelectBranch() {
        return dataSelectBranch;
    }

    public void setDataSelectBranch(String dataSelectBranch) {
        this.dataSelectBranch = dataSelectBranch;
    }

    public String getForTable() {
        return forTable;
    }

    public void setForTable(String forTable) {
        this.forTable = forTable;
    }

    public List<ReportCss> getReportCssList() {
        return reportCssList;
    }

    public void setReportCssList(List<ReportCss> reportCssList) {
        this.reportCssList = reportCssList;
    }
}