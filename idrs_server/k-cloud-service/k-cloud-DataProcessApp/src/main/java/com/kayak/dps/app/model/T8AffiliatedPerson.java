package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@GraphQLModel(fetcher = "t8AffiliatedPersonService",table = "dwd_affiliated_person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class T8AffiliatedPerson {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "关联法人信息id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "关联法人名称", sql = "name_operson like '%$U{nameOperson}%'" ,field = "name_operson")
   private String nameOperson;
   @GraphQLField(kkhtml = "KFieldText", label = "生效日期", sql = "effective_date = $S{effectiveDate}" ,field = "effective_date")
   private String effectiveDate;
   @GraphQLField(kkhtml = "KFieldText", label = "失效日期", sql = "expiry_date = $S{expiryDate}" ,field = "expiry_date")
   private String expiryDate;
   @GraphQLField(kkhtml = "KFieldText", label = "关联方类型", sql = "affiliated_type = $S{affiliatedType}" ,field = "affiliated_type")
   private String affiliatedType;
   @GraphQLField(kkhtml = "KFieldText", label = "所属托管行", sql = "trutee_name = $S{truteeName}" ,field = "trutee_name")
   private String truteeName;

   //以下参数用于接收页面参数，类似DTO用法
   @GraphQLField(label = "生效日期起始日")
   private String effectiveDateStart ;
   @GraphQLField(label = "生效日期结束日")
   private String effectiveDateEnd ;
   @GraphQLField(label = "失效日期起始日")
   private String expiryDateStart;
   @GraphQLField(label = "失效日期结束日")
   private String expiryDateEnd;
}