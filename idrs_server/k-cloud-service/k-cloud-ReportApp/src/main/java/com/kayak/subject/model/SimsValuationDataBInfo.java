package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "simsValuationDataBInfoService",table = "ods_sims_valuation_data")
public class SimsValuationDataBInfo {
   @GraphQLField(label = "id")
   private String id;
   @GraphQLField(label = "组合代码")
   private String comcode;
   @GraphQLField(label = "底层代码")
   private String bottomCode;
   @GraphQLField(label = "资产分类")
   private String assetType;
   @GraphQLField(label = "市值")
   private String amount;
   @GraphQLField(label = "成本")
   private String cost;
   @GraphQLField(label = "币种")
   private String currency;
   @GraphQLField(label = "估值日期")
   private String inputDate;
   @GraphQLField(label = "科目代码")
   private String itemId;
   @GraphQLField(label = "科目名称")
   private String itemName;
   @GraphQLField(kkhtml = "KFieldText",  label = "导入日期", sql = "importDate = $S{importDate}" ,field = "import_date")
   private String importDate;
   @GraphQLField(kkhtml = "KFieldText", label = "查询范围起始", sql = "importDate >= $S{beginDate}" ,field = "begin_date")
   private String beginDate;
   @GraphQLField(kkhtml = "KFieldText", label = "查询范围终止", sql = "importDate <= $S{queryDate}" ,field = "query_date")
   private String queryDate;
   @GraphQLField(label = "资产代码")
   private String icode;
   @GraphQLField(label = "资产类型")
   private String atype;
   @GraphQLField(label = "市场类型")
   private String mtype;
   @GraphQLField(label = "起息代码")
   private String assetCode;
   @GraphQLField(label = "层级")
   private String orgLevel;
   @GraphQLField(label = "中债报送类别")
   private String zzReportType;
   @GraphQLField(label = "交易场所")
   private String tradePlace;
   @GraphQLField(label = "是否公募基金")
   private String isPublic;
   @GraphQLField(label = "净价金额")
   private String netValue;
   @GraphQLField(label = "底层资产首次入库日期")
   private String dataInsrDt;
   @GraphQLField(label = "处理日期")
   private String dealDate;
}