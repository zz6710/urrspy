package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8ProdCustodianEmailService",table = "t8_prod_custodian_email")
public class T8ProdCustodianEmail {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name = $S{prodName}" ,field = "prod_name")
   private String prodName;
   @GraphQLField(kkhtml = "KFieldText", label = "核算经办", sql = "accounting_manager like '%$U{accountingManager}%'" ,field = "accounting_manager")
   private String accountingManager;
   @GraphQLField(kkhtml = "KFieldText", label = "运营机构", sql = "operating_agency like '%$U{operatingAgency}%'" ,field = "operating_agency")
   private String operatingAgency;
   @GraphQLField(kkhtml = "KFieldText", label = "经办电话", sql = "handling_phone = $S{handlingPhone}" ,field = "handling_phone")
   private String handlingPhone;
   @GraphQLField(kkhtml = "KFieldText", label = "经办邮箱", sql = "handling_mailbox like '%$U{handlingMailbox}%'" ,field = "handling_mailbox")
   private String handlingMailbox;
   @GraphQLField(kkhtml = "KFieldText", label = "导入时间", sql = "import_date = $S{importDate}" ,field = "import_date")
   private String importDate;
   @GraphQLField(kkhtml = "KFieldText", label = "复核状态", sql = "review_status = $S{reviewStatus}" ,field = "review_status")
   private String reviewStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user_id = $S{crtUserId}" ,field = "crt_user_id")
   private String crtUserId;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人名称", sql = "crt_user_name = $S{crtUserName}" ,field = "crt_user_name")
   private String crtUserName;
   @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user_id = $S{updUserId}" ,field = "upd_user_id")
   private String updUserId;
   @GraphQLField(kkhtml = "KFieldText", label = "更新人名字", sql = "upd_user_name = $S{updUserName}" ,field = "upd_user_name")
   private String updUserName;
   @GraphQLField(kkhtml = "KFieldText", label = "意见", sql = "opinion = $S{opinion}" ,field = "opinion")
   private String opinion;
}