package com.kayak.pms.T81.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8ProdDocInfoService",table = "t8_prod_doc_info")
public class T8ProdDocInfo {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "t8ProdInfoId", sql = "t8_prod_info_id = $S{t8ProdInfoId}" ,field = "t8_prod_info_id")
   private String t8ProdInfoId;
   @GraphQLField(kkhtml = "KFieldText", label = "文档类型", sql = "doc_type = $S{docType}" ,field = "doc_type")
   private String docType;
   @GraphQLField(kkhtml = "KFieldText", label = "销售商代码", sql = "distributor_code = $S{distributorCode}" ,field = "distributor_code")
   private String distributorCode;
   @GraphQLField(kkhtml = "KFieldText", label = "托管行id", sql = "t8_trutee_info_id = $S{t8TruteeInfoId}" ,field = "t8_trutee_info_id")
   private String t8TruteeInfoId;
   @GraphQLField(kkhtml = "KFieldText", label = "会议i", sql = "t8_meet_create_id = $S{t8MeetCreateId}" ,field = "t8_meet_create_id")
   private String t8MeetCreateId;
   @GraphQLField(kkhtml = "KFieldText", label = "模板子版本id", sql = "t8_print_temp_version_id = $S{t8PrintTempVersionId}" ,field = "t8_print_temp_version_id")
   private String t8PrintTempVersionId;
   @GraphQLField(kkhtml = "KFieldText", label = "文档版本", sql = "doc_version = $S{docVersion}" ,field = "doc_version")
   private String docVersion;
   @GraphQLField(kkhtml = "KFieldText", label = "文档描述", sql = "doc_desc = $S{docDesc}" ,field = "doc_desc")
   private String docDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
   private String crtUser;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}" ,field = "upd_date")
   private String updDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
   private String updTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user = $S{updUser}" ,field = "upd_user")
   private String updUser;
   @GraphQLField
   private String prodMode;
   @GraphQLField
   private String tempName;
   @GraphQLField
   private String meetName;
   @GraphQLField
   private String raiseType;
   @GraphQLField
   private String itemval;
   @GraphQLField
   private String prodCode;
   @GraphQLField(label = "产品状态", sql = "prod_status =$S{prodStatus}", field = "prod_status")
   private String prodStatus;
   @GraphQLField(label = "产品名称", sql = "prod_name like '%$U{prodName}%'", field = "prod_name")
   private String prodName;
   @GraphQLField(label = "调整记录表id", sql = "t8_prod_adjust_id = $S{t8ProdAdjustId}", field = "t8_prod_adjust_id")
   private String t8ProdAdjustId;
   @GraphQLField(field = "has_template")
   private String hasTemplate;
   @GraphQLField()
   private String correlationTime;
   
   @GraphQLField
   private String isCurrencyTemplate;
}