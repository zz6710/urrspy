package com.kayak.pms.declare.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "materialTemplateService", table = "pids_material_template")
public class MaterialTemplate {


    @GraphQLField(field = "产品代码")
    private String prodCode;
    @GraphQLField(field = "产品名称")
    private String prodName;

    /** pids_declare_type **/
    @GraphQLField(field = "ID")
    private String id;
    @GraphQLField(field = "模块；1-申报登记")
    private String module;
    @GraphQLField(field = "是否有子文档")
    private String isSonTemplate;
    @GraphQLField(field = "文档类型描述")
    private String templateLabel;
    @GraphQLField(field = "文档子类型描述")
    private String templateSonLabel;
    @GraphQLField(field = "文档取值方式")
    private String valueMethod;
    @GraphQLField(field = "是否有占位符")
    private String isPlaceholder;

    /** pids_material_template **/
    @GraphQLField(field = "ID")
    private String templateId;
    @GraphQLField(field = "产品模式")
    private String prodMod;
    @GraphQLField(field = "托管行")
    private String truteeBank;
    @GraphQLField(field = "托管行名称")
    private String truteeBankName;
    @GraphQLField(field = "模板类型")
    private String templateType;
    @GraphQLField(field = "模板子类型")
    private String templateSonType;
    @GraphQLField(field = "模板名称")
    private String templateName;
    @GraphQLField(field = "模板状态")
    private String templateStart;

    /** pids_material_version **/
    @GraphQLField(field = "版本ID")
    private String versionId;
    @GraphQLField(field = "文件名")
    private String versionName;
    @GraphQLField(field = "路径")
    private String versionPath;
    @GraphQLField(field = "状态")
    private String versionStart;
    @GraphQLField(field = "版本号")
    private String versionNum;

    /** pids_material_version **/
    @GraphQLField(field = "sql_id")
    private String sqlId;
    @GraphQLField(field = "查询sql")
    private String sqlSelect;
    @GraphQLField(field = "执行顺序")
    private String sqlOrder;
    @GraphQLField(field = "状态")
    private String sqlStart;
    @GraphQLField(field = "")
    private String columnLabel;
    @GraphQLField(field = "")
    private String columnKey;

    @GraphQLField(field = "备注")
    private String remark;
    @GraphQLField(field = "创建日期")
    private String crtDate;
    @GraphQLField(field = "创建时间")
    private String crtTime;
    @GraphQLField(field = "创建人")
    private String crtUser;
    @GraphQLField(field = "更新日期")
    private String updDate;
    @GraphQLField(field = "更新时间")
    private String updTime;
    @GraphQLField(field = "更新人")
    private String updUser;

}
