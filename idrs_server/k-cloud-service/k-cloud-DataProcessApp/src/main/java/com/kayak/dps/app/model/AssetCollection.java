package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "assetCollectionService",table = "dwd_asset_collection")
public class AssetCollection {
    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "补录页面", sql = "page = $S{page}" ,field = "page")
   private String page;
   @GraphQLField(kkhtml = "KFieldText", label = "角色", sql = "roles = $S{roles}" ,field = "roles")
   private String roles;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "补录页面字段", sql = "page_field = $S{pageField}" ,field = "page_field")
   private String pageField;
    @GraphQLField(kkhtml = "KFieldText", label = "补录字段类型" , sql = "field_type = $S{fieldType}" ,field = "field_type")
    private String fieldType;

    @GraphQLField(kkhtml = "KFieldText", label = "角色id")
    private String roleid;
    @GraphQLField(kkhtml = "KFieldText", label = "角色名称")
    private String rolename;
    @GraphQLField(kkhtml = "KFieldText", label = "字段名")
    private String label;
    @GraphQLField(kkhtml = "KFieldText", label = "字段英文名")
    private String value;
    @GraphQLField(kkhtml = "KFieldText", label = "表名")
    private String tableName;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
  	public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
  	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
  	public String getPageField() {
        return pageField;
    }

    public void setPageField(String pageField) {
        this.pageField = pageField;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

}