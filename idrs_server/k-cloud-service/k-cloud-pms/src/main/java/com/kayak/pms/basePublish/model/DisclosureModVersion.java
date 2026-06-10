package com.kayak.pms.basePublish.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureModVersionService",table = "idb_disclosure_mod_version")
public class DisclosureModVersion {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "模板id", sql = "disclosure_mod_id = $S{disclosureModId}" ,field = "disclosure_mod_id")
   private String disclosureModId;
   @GraphQLField(kkhtml = "KFieldText", label = "模板版本id", sql = "disclosure_mod_version_id = $S{disclosureModVersionId}" ,field = "disclosure_mod_version_id")
   private String disclosureModVersionId;
   @GraphQLField(kkhtml = "KFieldText", label = "文档版本编号", sql = "version = $S{version}" ,field = "version")
   private String version;
   @GraphQLField(kkhtml = "KFieldText", label = "文件上传路径", sql = "upload_path = $S{uploadPath}" ,field = "upload_path")
   private String uploadPath;
   @GraphQLField(kkhtml = "KFieldText", label = "文件名称", sql = "file_name = $S{fileName}" ,field = "file_name")
   private String fileName;
   @GraphQLField(kkhtml = "KFieldText", label = "下一版本号", sql = "next_version = $S{nextVersion}" ,field = "next_version")
   private String nextVersion;
   @GraphQLField(kkhtml = "KFieldText", label = "文档名称", sql = "doc_name = $S{docName}" ,field = "doc_name")
   private String docName;
   @GraphQLField(kkhtml = "KFieldText", label = "版本文档内容", sql = "doc_html = $S{docHtml}" ,field = "doc_html")
   private String docHtml;
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
    @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user_id = $S{updUserId}", field = "upd_user_id")
    private String updUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人名称", sql = "upd_user_name = $S{updUserName}", field = "upd_user_name")
    private String updUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "文档状态", sql = "status = $S{status}", field = "status")
    private String status;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}", field = "remark")
    private String remark;
    @GraphQLField(kkhtml = "KFieldText", label = "模板名称", sql = "modName = $S{mod_name}", field = "mod_name")
    private String modName;
    @GraphQLField(label = "信披类型")
    private String disclosureType;
    @GraphQLField(label = "信披子类型")
    private String disclosureSonType;

    @GraphQLField
    private String datas;//前台传入的json字符串
}