package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@GraphQLModel(fetcher = "trusteeService",table = "ODS_TRUSTEE_BAS_INF")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrusteeModel {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "序号", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "托管行机构代码", field = "TRUSTEE_CODE")
   private String trusteeCode;
   @GraphQLField(kkhtml = "KFieldText", label = "托管行机构名称", field = "TRUSTEE_NAME")
   private String trusteeName;
   @GraphQLField(kkhtml = "KFieldText", label = "开户名", field = "ACCT_NAME")
   private String acctName;
   @GraphQLField(kkhtml = "KFieldText", label = "开户账号", field = "ACCT_NO")
   private String acctNo;
   @GraphQLField(kkhtml = "KFieldText", label = "所在地区", field = "TRUSTEE_COUNTRY")
   private String trusteeCountry;
   @GraphQLField(kkhtml = "KFieldText", label = "开户国家名称", field = "TRUSTEE_COUNTRY_NAME")
   private String trusteeCountryName;
   @GraphQLField(kkhtml = "KFieldText", label = "托管行开户行", field = "TRUSTEE_ACCT_NAME")
   private String trusteeAcctName;
   @GraphQLField(kkhtml = "KFieldText", label = "组织机构代码", field = "TRUSTEE_ACCT_CODE")
   private String trusteeAcctCode;
   @GraphQLField(kkhtml = "KFieldText", label = "托管行性质", field = "TRUSTEE_PROPERTY")
   private String trusteeProperty;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", field = "CRT_USER")
   private String crtUser;
   @GraphQLField(kkhtml = "KFieldText", label = "更改人", field = "UPD_USER")
   private String updUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", field = "CRT_DT")
   private String crtDt;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", field = "UPD_DT")
   private String updDt;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", field = "DEAL_DATE")
   private String dealDate;


}