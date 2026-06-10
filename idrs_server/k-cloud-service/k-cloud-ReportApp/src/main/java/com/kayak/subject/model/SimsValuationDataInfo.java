package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "simsValuationDataInfoService",table = "dwd_s03_ttrd_sims_valuation_data")
public class SimsValuationDataInfo {
   @GraphQLField(label = "id")
   private String id;
   @GraphQLField(label = "组合代码")
   private String comcode;
   @GraphQLField(label = "底层资产代码")
   private String bottomCode;
   @GraphQLField(label = "类型 01活期，02组合代码本身，03 债券，04 基金，05 股票，06 资管计划，07 负债，08 其他负债，09 逆回购，10 期货，11 期权，12 远期，13 互换")
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
   @GraphQLField(kkhtml = "KFieldText",  label = "'导入日期'", sql = "importDate = $S{importDate}" ,field = "import_date")
   private String importDate;
   @GraphQLField(kkhtml = "KFieldText", label = "查询范围起始", sql = "importDate >= $S{beginDate}" ,field = "begin_date")
   private String beginDate;
   @GraphQLField(kkhtml = "KFieldText", label = "查询范围终止", sql = "importDate <= $S{queryDate}" ,field = "query_date")
   private String queryDate;
   @GraphQLField(label = "I_CODE")
   private String icode;
   @GraphQLField(label = "A_TYPE")
   private String atype;
   @GraphQLField(label = "M_TYPE")
   private String mtype;
   @GraphQLField(label = "起息代码")
   private String assetCode;
   @GraphQLField(label = "层级")
   private String orgLevel;
   @GraphQLField(label = "中债资产报送类别")
   private String zzReportType;
   @GraphQLField(label = "交易场所")
   private String tradePlace;
   @GraphQLField(label = "是否公募基金 01-是、02否")
   private String isPublic;
   @GraphQLField(label = "净价金额")
   private String netValue;
   @GraphQLField(label = "数据插入日期")
   private String dataInsrDt;
   @GraphQLField(label = "处理日期")
   private String dealDate;
   @GraphQLField(label = "菜单id")
   private String menuId;
   @GraphQLField(label = "按钮名称")
   private String buttonName;
}