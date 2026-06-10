package com.kayak.pms.basePublish.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

import lombok.Data;
@Data
@GraphQLModel(fetcher = "disclosureModService",table = "idb_disclosure_mod")
public class DisclosureMod {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "信披类型", sql = "disclosure_type = $S{disclosureType}" ,field = "disclosure_type")
   private String disclosureType;
   @GraphQLField(kkhtml = "KFieldText", label = "信披子类型", sql = "disclosure_son_type = $S{disclosureSonType}" ,field = "disclosure_son_type")
   private String disclosureSonType;
   @GraphQLField(kkhtml = "KFieldText", label = "模板名称描述", sql = "mod_name like '%$U{modName}%' " ,field = "mod_name")
   private String modName;
   @GraphQLField(kkhtml = "KFieldText", label = "模板文档名称", sql = "doc_name = $S{docName}" ,field = "doc_name")
   private String docName;
   @GraphQLField(kkhtml = "KFieldText", label = "上传日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "上传时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
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
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}", field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "版本号", field = "version")
   private String version;
   @GraphQLField
   private String status;
   @GraphQLField
   private String url;
   @GraphQLField(field = "operation_type")
   private String operationType;

}