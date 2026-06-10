package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@GraphQLModel(fetcher = "dataSupplementService",table = "dwd_data_supplement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSupplementModel {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "发行人表内资产余额",field = "balance_assets")
   private String balanceAssets;
   @GraphQLField(kkhtml = "KFieldText", label = "发行人表内金融资产余额",field = "financial_assets")
   private String financialAssets;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", field = "upd_date")
   private String updDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", field = "upd_time")
   private String updTime;
}