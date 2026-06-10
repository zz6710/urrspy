package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureNoticeVersionService",table = "idb_disclosure_notice_version")
public class DisclosureNoticeVersion {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "公告版本id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "信披类型", sql = "disclosure_type = $S{disclosureType}" ,field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(kkhtml = "KFieldText", label = "信披子类型", sql = "disclosure_son_type = $S{disclosureSonType}" ,field = "disclosure_son_type")
    private String disclosureSonType;
    @GraphQLField(kkhtml = "KFieldText", label = "模板名称", sql = "mod_name = $S{modName}" ,field = "mod_name")
    private String modName;
    @GraphQLField(kkhtml = "KFieldText", label = "模板版本号", sql = "mod_version = $S{modVersion}" ,field = "mod_version")
    private String modVersion;
    @GraphQLField(kkhtml = "KFieldText", label = "文件名", sql = "file_name = $S{fileName}" ,field = "file_name")
    private String fileName;
    @GraphQLField(kkhtml = "KFieldText", label = "文件存放路径", sql = "file_path = $S{filePath}" ,field = "file_path")
    private String filePath;
    @GraphQLField(kkhtml = "KFieldText", label = "版本号", sql = "notice_version = $S{noticeVersion}" ,field = "notice_version")
    private String noticeVersion;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name = $S{prodName}" ,field = "prod_name")
    private String prodName;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人名称", sql = "crt_user_name = $S{crtUserName}" ,field = "crt_user_name")
    private String crtUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "公告id", sql = "t8_disclosure_notice_id = $S{t8DisclosureNoticeId}" ,field = "t8_disclosure_notice_id")
    private String t8DisclosureNoticeId;
    @GraphQLField(kkhtml = "KFieldText", label = "模板版本id", sql = "disclosure_mod_version_id = $S{disclosureModVersionId}" ,field = "disclosure_mod_version_id")
    private String disclosureModVersionId;
    @GraphQLField(kkhtml = "KFieldText", label = "文档类型", sql = "doc_type = $S{docType}" ,field = "doc_type")
    private String docType;
    @GraphQLField(kkhtml = "KFieldText", label = "公告是否已发布", sql = "is_notice_pub = $S{isNoticePub}" ,field = "is_notice_pub")
    private String isNoticePub;
    @GraphQLField(kkhtml = "KFieldText", label = "生成文件路径", sql = "crt_path = $S{crtPath}" ,field = "crt_path")
    private String crtPath;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user_id = $S{crtUserId}" ,field = "crt_user_id")
    private String crtUserId;
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
    @GraphQLField(kkhtml = "KFieldText", label = "公告标题", sql = "notice_title = $S{noticeTitle}" ,field = "notice_title")
    private String noticeTitle;
    @GraphQLField(kkhtml = "KFieldText", label = "发布文件名", sql = "pub_file_name = $S{pubFileName}" ,field = "pub_file_name")
    private String pubFileName;
    @GraphQLField(kkhtml = "KFieldText", label = "模板版本文件格式", sql = "suffix = $S{suffix}", field = "suffix")
    private String suffix;
    @GraphQLField(kkhtml = "KFieldText", label = "托管行名称", sql = "trustee_name = $S{trusteeName}", field = "trustee_name")
    private String trusteeName;
    @GraphQLField(kkhtml = "KFieldText", label = "文档类型", sql = "file_type = $S{fileType}", field = "file_type")
    private String fileType;
}