package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@GraphQLModel(fetcher = "assetRightService",table = "ODS_ASS_RIGHT_BAS_INF")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetRightModel {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "序号", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "股权代码", field = "ASS_NBR_EXT")
   private String assNbrExt;
   @GraphQLField(kkhtml = "KFieldText", label = "融资企业代码", field = "ORG_NBR_EXT")
   private String orgNbrExt;
   @GraphQLField(kkhtml = "KFieldText", label = "融资企业名称", field = "ORG_FULL_NAME")
   private String orgFullName;
   @GraphQLField(kkhtml = "KFieldText", label = "交易流通场所", field = "TRADE_PLACES")
   private String tradePlaces;
   @GraphQLField(kkhtml = "KFieldText", label = "融资企业行业", field = "INDUSTRY_ISSUER")
   private String industryIssuer;
   @GraphQLField(kkhtml = "KFieldText", label = "投资阶段", field = "INVESTMENT_TYPE")
   private String investmentType;
   @GraphQLField(kkhtml = "KFieldText", label = "币种", field = "TRANS_CCY")
   private String transCcy;
   @GraphQLField(kkhtml = "KFieldText", label = "股权退出安排", field = "SHAREHOLD")
   private String sharehold;
   @GraphQLField(kkhtml = "KFieldText", label = "是否通道投资", field = "IS_CHANNEL")
   private String isChannel;
   @GraphQLField(kkhtml = "KFieldText", label = "通道", field = "CHANNEL_CODE")
   private String channelCode;
   @GraphQLField(kkhtml = "KFieldText", label = "企业类型（按规模划分）", field = "GG_ENTER_TYPE_SCALE")
   private String ggEnterTypeScale;
   @GraphQLField(kkhtml = "KFieldText", label = "企业类型（按技术领域划分）", field = "GG_ENTER_TYPE_TECH")
   private String ggEnterTypeTech;
   @GraphQLField(kkhtml = "KFieldText", label = "企业类型（按经济类型分）", field = "GG_ENTER_TYPE_ECONOMIC")
   private String ggEnterTypeEconomic;
   @GraphQLField(kkhtml = "KFieldText", label = "是否为质押融资", field = "GG_PLEDGED_FINACE")
   private String ggPledgedFinace;
   @GraphQLField(kkhtml = "KFieldText", label = "是否为债转股", field = "GG_DEBT_EQUITY_SWAP")
   private String ggDebtEquitySwap;
   @GraphQLField(kkhtml = "KFieldText", label = "中债一级分类", field = "CBC_TYPE")
   private String cbcType;
   @GraphQLField(kkhtml = "KFieldText", label = "中债二级分类", field = "CBC_SUB_TYPE")
   private String cbcSubType;
   @GraphQLField(kkhtml = "KFieldText", label = "G06一级分类", field = "GG_CBC_TYPE")
   private String ggCbcType;
   @GraphQLField(kkhtml = "KFieldText", label = "G06二级分类", field = "GG_CBC_SUB_TYPE")
   private String ggCbcSubType;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", field = "CRT_USER")
   private String crtUser;
   @GraphQLField(kkhtml = "KFieldText", label = "更改人", field = "UPD_USER")
   private String updUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", field = "CRT_DATE")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", field = "UPD_DATE")
   private String updDate;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", field = "DEAL_DATE")
   private String dealDate;


}