package com.kayak.pms.declare.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "materialDocumentService", table = "pids_material_document")
public class MaterialDocument {

    @GraphQLField(field = "文档id")
    private String documentId;
    @GraphQLField(field = "产品代码")
    private String prodCode;
    @GraphQLField(field = "模板类型")
    private String templateType;
    @GraphQLField(field = "模板子类型")
    private String templateSonType;
    @GraphQLField(field = "模板类型")
    private String templateLabel;
    @GraphQLField(field = "模板子类型")
    private String templateSonLabel;
    @GraphQLField(field = "文档名称")
    private String documentName;
    @GraphQLField(field = "文档路径")
    private String documentPath;
    @GraphQLField(field = "文档历史路径")
    private String documentHisPath;
    @GraphQLField(field = "手工上传标识")
    private String handUpload;
    @GraphQLField(field = "创建日期")
    private String crtDate;
    @GraphQLField(field = "创建时间")
    private String crtTime;
    @GraphQLField(field = "创建人")
    private String crtUser;
    @GraphQLField(field = "页面展示序列")
    private String documentNum;


    @GraphQLField(field = "产品模型")
    private String prodMod;
    @GraphQLField(field = "产品名称")
    private String prodName;
    @GraphQLField(field = "成立日")
    private String foundDate;
    @GraphQLField(field = "到期日")
    private String mtuDate;
    @GraphQLField(field = "产品状态")
    private String prodSts;
    @GraphQLField(field = "托管行")
    private String truteeBank;
    @GraphQLField(field = "托管行Key")
    private String truteeBankKey;

    @GraphQLField(field = "")
    private String module;
    @GraphQLField(field = "")
    private String sysName;


}
