package com.kayak.pms.basePublish.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@GraphQLModel(fetcher = "disclosureSourceService",table = "idb_disclosure_source")
public class DisclosureSource {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "字段描述", sql = "column_label like '%$U{columnLabel}%' " ,field = "column_label")
    private String columnLabel;
    @GraphQLField(kkhtml = "KFieldText", label = "字段key", sql = "column_key like '%$U{columnKey}%'" ,field = "column_key")
    private String columnKey;
    @GraphQLField(kkhtml = "KFieldText", label = "字段默认值", sql = "column_value = $S{columnValue}" ,field = "column_value")
    private String columnValue;
    @GraphQLField(kkhtml = "KFieldText", label = "转换的数据字典", sql = "dict = $S{dict}" ,field = "dict")
    private String dict;
    @GraphQLField(kkhtml = "KFieldText", label = "输入类型", sql = "functype = $S{functype}" ,field = "functype")
    private String functype;
    @GraphQLField(kkhtml = "KFieldText", label = "前端组件属性配置", sql = "confoption = $S{confoption}" ,field = "confoption")
    private String confoption;
    @GraphQLField(kkhtml = "KFieldText", label = "文档状态", sql = "status = $S{status}" ,field = "status")
    private String status;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user_id = $S{crtUserId}" ,field = "crt_user_id")
    private String crtUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人名称", sql = "crt_user_name = $S{crtUserName}" ,field = "crt_user_name")
    private String crtUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}" ,field = "upd_date")
    private String updDate;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
    private String updTime;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user_id = $S{updUserId}" ,field = "upd_user_id")
    private String updUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人名称", sql = "upd_user_name = $S{updUserName}" ,field = "upd_user_name")
    private String updUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
    private String remark;
    @GraphQLField(kkhtml = "KFieldText", label = "金额格式", sql = "money_format = $S{moneyFormat}" ,field = "moneyFormat")
    private String moneyFormat;
    @GraphQLField(kkhtml = "computed_expression", label = "计算表达式", sql = "computed_expression = $S{computedExpression}" ,field = "computed_expression")
    private String computedExpression;
    @GraphQLField(kkhtml = "KFieldText", label = "数据长度", sql = "data_length = $D{dataLength}" ,field = "data_length")
    private String dataLength;
    @GraphQLField(kkhtml = "KFieldText", label = "sql参数名", sql = "sql_parameter = $S{sqlParameter}" ,field = "sql_parameter")
    private String sqlParameter;
    @GraphQLField(kkhtml = "KFieldText", label = "取值sql", sql = "value_sql = $S{valueSql}" ,field = "value_sql")
    private String valueSql;
    @GraphQLField(kkhtml = "KFieldText", label = "数据库数据源", sql = "data_source = $S{dataSource}" ,field = "data_source")
    private String dataSource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }
    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }
    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }
    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }
    public String getFunctype() {
        return functype;
    }

    public void setFunctype(String functype) {
        this.functype = functype;
    }
    public String getConfoption() {
        return confoption;
    }

    public void setConfoption(String confoption) {
        this.confoption = confoption;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    public String getCrtUserId() {
        return crtUserId;
    }

    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }
    public String getCrtUserName() {
        return crtUserName;
    }

    public void setCrtUserName(String crtUserName) {
        this.crtUserName = crtUserName;
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
    public String getUpdUserId() {
        return updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }
    public String getUpdUserName() {
        return updUserName;
    }

    public void setUpdUserName(String updUserName) {
        this.updUserName = updUserName;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}