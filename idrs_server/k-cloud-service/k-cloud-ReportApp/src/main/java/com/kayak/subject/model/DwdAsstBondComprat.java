package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwdAsstBondCompratService",table = "dwd_asst_bond_comprat")
@Data
public class DwdAsstBondComprat {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "公司代码", sql = "comy_cd = $S{comyCd}" ,field = "comy_cd")
   private String comyCd;
   @GraphQLField(kkhtml = "KFieldText", label = "公司名称", sql = "comy_name like '%$U{comyName}%'" ,field = "comy_name")
   private String comyName;
   @GraphQLField(kkhtml = "KFieldText", label = "评级结果", sql = "rating = $S{rating}" ,field = "rating")
   private String rating;
   @GraphQLField(kkhtml = "KFieldText", label = "评级公司", sql = "rating_comp like '%$U{ratingComp}%'" ,field = "rating_comp")
   private String ratingComp;
   @GraphQLField(kkhtml = "KFieldText", label = "评级日期", sql = "annt_dt like '%$U{anntDt}%'" ,field = "annt_dt")
   private String anntDt;
   @GraphQLField(kkhtml = "KFieldText", label = "变更标识", sql = "flag = $S{flag}" ,field = "flag")
   private String flag;
   @GraphQLField(kkhtml = "KFieldText", label = "操作人员", sql = "summit_user = $S{summitUser}" ,field = "summit_user")
   private String summitUser;
   @GraphQLField(kkhtml = "KFieldText", label = "变更日期", sql = "update_date = $S{updateDate}" ,field = "update_date")
   private String updateDate;
   @GraphQLField(kkhtml = "KFieldText", label = "变更时间", sql = "update_time = $S{updateTime}" ,field = "update_time")
   private String updateTime;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;
   @GraphQLField(kkhtml = "KFieldText")
   private String multRating;

}