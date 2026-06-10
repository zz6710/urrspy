package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "appOverseasInvInfo1Service",table = "app_overseas_inv_info_1")
@Data
public class AppOverseasInvInfo1 {

   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date like '%$U{reportDate}%'" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "公司名称", sql = "comp_nm = $S{compNm}" ,field = "comp_nm")
   private String compNm;
   @GraphQLField(kkhtml = "KFieldText", label = "资金类型", sql = "fund_type = $S{fundType}" ,field = "fund_type")
   private String fundType;
   @GraphQLField(kkhtml = "KFieldText", label = "投资资产余额", sql = "inv_asst_mkt = $S{invAsstMkt}" ,field = "inv_asst_mkt")
   private String invAsstMkt;
   @GraphQLField(kkhtml = "KFieldText", label = "加权累计净值增长率", sql = "gro_rate = $S{groRate}" ,field = "gro_rate")
   private String groRate;

}