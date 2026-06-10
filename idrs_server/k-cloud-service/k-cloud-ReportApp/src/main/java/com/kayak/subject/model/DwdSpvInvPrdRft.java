package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwdSpvInvPrdRftService",table = "dwd_spv_inv_prd_rft")
@Data
public class DwdSpvInvPrdRft {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "特定目的载体代码", sql = "spv_code like '%$U{spvCode}%'" ,field = "spv_code")
   private String spvCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资管产品统计编码", sql = "amps_code like '%$U{ampsCode}%'" ,field = "amps_code")
   private String ampsCode;
   @GraphQLField(kkhtml = "KFieldText", label = "特定目的载体类型", sql = "spv_type like '%$U{spvType}%'" ,field = "spv_type")
   private String spvType;
   @GraphQLField(kkhtml = "KFieldText", label = "数据来源", sql = "data_from = $S{dataFrom}" ,field = "data_from")
   private String dataFrom;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "修改日期", sql = "update_date = $S{updateDate}" ,field = "update_date")
   private String updateDate;
   @GraphQLField(kkhtml = "KFieldText", label = "修改时间", sql = "update_time = $S{updateTime}" ,field = "update_time")
   private String updateTime;

}