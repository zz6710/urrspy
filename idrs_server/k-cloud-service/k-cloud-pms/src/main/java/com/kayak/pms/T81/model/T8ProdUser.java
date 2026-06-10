package com.kayak.pms.T81.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8ProdUserService",table = "t8_prod_user")
public class T8ProdUser {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "t8_prod_info_id", sql = "t8_prod_info_id = $S{t8ProdInfoId}" ,field = "t8_prod_info_id")
   private String t8ProdInfoId;
   @GraphQLField(kkhtml = "KFieldText", label = "角色Id", sql = "role_id = $S{roleId}" ,field = "role_id")
   private String roleId;
   @GraphQLField(kkhtml = "KFieldText", label = "B角用户id", sql = "userid_b = $S{useridB}" ,field = "userid_b")
   private String useridB;
   @GraphQLField(kkhtml = "KFieldText", label = "所属A角", sql = "upperid = $S{upperid}" ,field = "upperid")
   private String upperid;
   @GraphQLField(kkhtml = "KFieldText", label = "A角状态", sql = "statu_a = $S{statuA}" ,field = "statu_a")
   private String statuA;
   @GraphQLField(kkhtml = "KFieldText", label = "提交人员", sql = "inputuser = $S{inputuser}" ,field = "inputuser")
   private String inputuser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}" ,field = "upd_date")
   private String updDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
   private String updTime;
   @GraphQLField(kkhtml = "KFieldText", label = "A角用户id", sql = "userid_a = $S{useridA}" ,field = "userid_a")
   private String useridA;
   @GraphQLField
   private String userid;
   

}