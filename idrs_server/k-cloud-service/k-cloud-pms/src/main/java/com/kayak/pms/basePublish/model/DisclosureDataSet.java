package com.kayak.pms.basePublish.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "disclosureDataSetService", table = "idb_disclosure_data_set")
public class DisclosureDataSet {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "id", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "信披类型", sql = "disclosure_type = $S{disclosureType}", field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(kkhtml = "KFieldText", label = "信披子类型", sql = "disclosure_son_type = $S{disclosureSonType}", field = "disclosure_son_type")
    private String disclosureSonType;
    @GraphQLField(kkhtml = "KFieldText", label = "查询字段", sql = "select_columns = $S{selectColumns}", field = "select_columns")
    private String selectColumns;
    @GraphQLField(kkhtml = "KFieldText", label = "查询SQL语句", sql = "select_sqls = $S{selectSqls}", field = "select_sqls")
    private String selectSqls;
    @GraphQLField(kkhtml = "KFieldText", label = "目标表删除SQL语句", sql = "delete_sqls = $S{deleteSqls}", field = "delete_sqls")
    private String deleteSqls;
    @GraphQLField(kkhtml = "KFieldText", label = "目标表插入SQL语句", sql = "insert_sqls = $S{insertSqls}", field = "insert_sqls")
    private String insertSqls;
    @GraphQLField(kkhtml = "KFieldText", label = "目标表更新SQL语句", sql = "update_sqls = $S{updateSqls}", field = "update_sqls")
    private String updateSqls;
    @GraphQLField(kkhtml = "KFieldText", label = "状态", sql = "status = $S{status}", field = "status")
    private String status;
    @GraphQLField(kkhtml = "KFieldText", label = "执行条件", sql = "conditions = $S{conditions}", field = "conditions")
    private String conditions;
    @GraphQLField(kkhtml = "KFieldText", label = "执行顺序", sql = "exec_order = $S{execOrder}", field = "exec_order")
    private String execOrder;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}", field = "remark")
    private String remark;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人姓名", sql = "crt_user_name = $S{crtUserName}", field = "crt_user_name")
    private String crtUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}", field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}", field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "创建用户", sql = "crt_user_id = $S{crtUserId}", field = "crt_user_id")
    private String crtUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "修改人id", sql = "upd_user_id = $S{updUserId}", field = "upd_user_id")
    private String updUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "修改人姓名", sql = "upd_user_name = $S{updUserName}", field = "upd_user_name")
    private String updUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "修改日期", sql = "upd_date = $S{updDate}", field = "upd_date")
    private String updDate;
    @GraphQLField(kkhtml = "KFieldText", label = "修改时间", sql = "upd_time = $S{updTime}", field = "upd_time")
    private String updTime;
    @GraphQLField(kkhtml = "KFieldText", label = "执行类型", sql = "exec_type = $S{execType}", field = "exec_type")
    private String execType;

    public String getExecType() {
        return execType;
    }

    public void setExecType(String execType) {
        this.execType = execType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisclosureType() {
        return disclosureType;
    }

    public void setDisclosureType(String disclosureType) {
        this.disclosureType = disclosureType;
    }

    public String getDisclosureSonType() {
        return disclosureSonType;
    }

    public void setDisclosureSonType(String disclosureSonType) {
        this.disclosureSonType = disclosureSonType;
    }

    public String getSelectSqls() {
        return selectSqls;
    }

    public void setSelectSqls(String selectSqls) {
        this.selectSqls = selectSqls;
    }

    public String getDeleteSqls() {
        return deleteSqls;
    }

    public void setDeleteSqls(String deleteSqls) {
        this.deleteSqls = deleteSqls;
    }

    public String getInsertSqls() {
        return insertSqls;
    }

    public void setInsertSqls(String insertSqls) {
        this.insertSqls = insertSqls;
    }

    public String getUpdateSqls() {
        return updateSqls;
    }

    public void setUpdateSqls(String updateSqls) {
        this.updateSqls = updateSqls;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getExecOrder() {
        return execOrder;
    }

    public void setExecOrder(String execOrder) {
        this.execOrder = execOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCrtUserName() {
        return crtUserName;
    }

    public void setCrtUserName(String crtUserName) {
        this.crtUserName = crtUserName;
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

    public String getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(String selectColumns) {
        this.selectColumns = selectColumns;
    }
}