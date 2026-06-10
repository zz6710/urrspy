package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsG06BIIDerivateInfoService",table = "dws_g06b_der_info")
@Data
public class DwsG06BIIDerivateInfo {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;

   @GraphQLField(kkhtml = "KFieldText", label = "理财投资资产代码", sql = "i_code = $S{icode}" ,field = "i_code")
   private String icode;

   @GraphQLField(kkhtml = "KFieldText", label = "资管产品名称(第一层)", sql = "i_name_c1 = $S{inamec1}" ,field = "i_name_c1")
   private String inamec1;

   @GraphQLField(kkhtml = "KFieldText", label = "底层代码", sql = "asset_code = $S{assetCode}" ,field = "asset_code")
   private String assetCode;

   @GraphQLField(kkhtml = "KFieldText", label = "g06穿透底层分类", sql = "g06_type = $S{g06Type}" ,field = "g06_type")
   private String g06Type;

   @GraphQLField(kkhtml = "KFieldText", label = "投资经理", sql = "inv_manager = $S{invManager}" ,field = "inv_manager")
   private String invManager;

   @GraphQLField(kkhtml = "KFieldText", label = "衍生品业务类型", sql = "der_bus_typ = $S{derBusTyp}" ,field = "der_bus_typ")
   private String derBusTyp;

   @GraphQLField(kkhtml = "KFieldText", label = "是否标准化衍生品", sql = "if_stand_der = $S{ifStandDer}" ,field = "if_stand_der")
   private String ifStandDer;

   @GraphQLField(kkhtml = "KFieldText", label = "填报数据说明", sql = "data_info = $S{dataInfo}" ,field = "data_info")
   private String dataInfo;

   @GraphQLField(kkhtml = "KFieldText", label = "填报数据", sql = "net_value = $S{netValue}" ,field = "net_value")
   private String netValue;

   @GraphQLField(kkhtml = "KFieldText", label = "系数", sql = "coef = $S{coef}" ,field = "coef")
   private String coef;

   @GraphQLField(kkhtml = "KFieldText", label = "投资规模", sql = "inv_value = $S{invValue}" ,field = "inv_value")
   private String invValue;

   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;

}