package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsProdTTRDBefOriService",table = "dws_prod_ttrd_bef_g06a2_ori")
@Data
public class DwsProdTTRDBefOri {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "product_code = $S{productCode}" ,field = "product_code")
   private String productCode;
   //现金管理类标识:01-是/02-否
   @GraphQLField(kkhtml = "KFieldText", label = "是否现管产品", sql = "csh_mng_f = $S{cshMngF}" ,field = "csh_mng_f")
   private String cshMngF;
   //养老理财产品标识:01-是/02-否
   @GraphQLField(kkhtml = "KFieldText", label = "是否养老理财产品", sql = "pen_inv_f = $S{penInvF}" ,field = "pen_inv_f")
   private String penInvF;
   //个人养老金产品标识:01-是/02-否
   @GraphQLField(kkhtml = "KFieldText", label = "是否个人养老金产品", sql = "per_pen_inv_f = $S{perPenInvF}" ,field = "per_pen_inv_f")
   private String perPenInvF;
   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "i_code = $S{icode}" ,field = "i_code")
   private String icode;
   @GraphQLField(kkhtml = "KFieldText", label = "资产三类", sql = "asset_third_type = $S{assetThirdType}" ,field = "asset_third_type")
   private String assetThirdType;
   @GraphQLField(kkhtml = "KFieldText", label = "资产名称", sql = "i_name = $S{iname}" ,field = "i_name")
   private String iname;
   @GraphQLField(kkhtml = "KFieldText", label = "金额", sql = "amount = $S{amount}" ,field = "amount")
   private String amount;
   @GraphQLField(kkhtml = "KFieldText", label = "汇率", sql = "changerate = $S{changerate}" ,field = "changerate")
   private String changerate;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币金额", sql = "investedamountcny = $S{investedamountcny}" ,field = "investedamountcny")
   private String investedamountcny;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透前分类", sql = "org_classific = $S{orgClassific}" ,field = "org_classific")
   private String orgClassific;
   @GraphQLField(kkhtml = "KFieldText", label = "委托/自主管理", sql = "orderfreemanage = $S{orderfreemanage}" ,field = "orderfreemanage")
   private String orderfreemanage;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透后分类", sql = "new_classific = $S{newClassific}" ,field = "new_classific")
   private String newClassific;
   @GraphQLField(kkhtml = "KFieldText", label = "是否逾期", sql = "isoverdue = $S{isoverdue}" ,field = "isoverdue")
   private String isoverdue;
   @GraphQLField(kkhtml = "KFieldText", label = "是否投向房地产业", sql = "moneyofproperty = $S{moneyofproperty}" ,field = "moneyofproperty")
   private String moneyofproperty;
   @GraphQLField(kkhtml = "KFieldText", label = "资金投向具体领域", sql = "cashtodomain = $S{cashtodomain}" ,field = "cashtodomain")
   private String cashtodomain;
   @GraphQLField(kkhtml = "KFieldText", label = "创业投资基金", sql = "vcintfund = $S{vcintfund}" ,field = "vcintfund")
   private String vcintfund;
   @GraphQLField(kkhtml = "KFieldText", label = "政府出资产业投资基金", sql = "govintfund = $S{govintfund}" ,field = "govintfund")
   private String govintfund;
   @GraphQLField(kkhtml = "KFieldText", label = "非标行业", sql = "isnostandard = $S{isnostandard}" ,field = "isnostandard")
   private String isnostandard;
   @GraphQLField(kkhtml = "KFieldText", label = "如填列1.4.6-11债券，请分信用登记", sql = "bondrating = $S{bondrating}" ,field = "bondrating")
   private String bondrating;
   @GraphQLField(kkhtml = "KFieldText", label = "如填列1.4.2，请补充是否“1.4.2.a 专项债券”", sql = "specialbond = $S{specialbond}" ,field = "specialbond")
   private String specialbond;
   @GraphQLField(kkhtml = "KFieldText", label = "如填列1.5.16 非标，对股质类业务区分“1.5.16.a 场内”和“1.5.16.b 场外”", sql = "inmarketornot = $S{inmarketornot}" ,field = "inmarketornot")
   private String inmarketornot;
   @GraphQLField(kkhtml = "KFieldText", label = "如填列1.5 非标，需补充是否“1.5.a 投向地方政府融资平台的部分”", sql = "cashtogovernment = $S{cashtogovernment}" ,field = "cashtogovernment")
   private String cashtogovernment;
   @GraphQLField(kkhtml = "KFieldText", label = "1.15.a 投向“公共私营合作项目”（PPP）的部分（金额）", sql = "cashtopublic = $S{cashtopublic}" ,field = "cashtopublic")
   private String cashtopublic;
   @GraphQLField(kkhtml = "KFieldText", label = "1.15.b 投向市场化债转股相关产品的部分（金额）", sql = "cashtorelateproduct = $S{cashtorelateproduct}" ,field = "cashtorelateproduct")
   private String cashtorelateproduct;
   @GraphQLField(kkhtml = "KFieldText", label = "1.15.c 逾期资产（金额）", sql = "moneyofoverdueasset = $S{moneyofoverdueasset}" ,field = "moneyofoverdueasset")
   private String moneyofoverdueasset;
   @GraphQLField(kkhtml = "KFieldText", label = "3.2.3 二级资本债", sql = "secondlevelcaptialbond = $S{secondlevelcaptialbond}" ,field = "secondlevelcaptialbond")
   private String secondlevelcaptialbond;
   @GraphQLField(kkhtml = "KFieldText", label = "3.2.2 永续债", sql = "continuebondforever = $S{continuebondforever}" ,field = "continuebondforever")
   private String continuebondforever;
   @GraphQLField(kkhtml = "KFieldText", label = "3.2.1 优先股", sql = "seniorbond = $S{seniorbond}" ,field = "seniorbond")
   private String seniorbond;
   @GraphQLField(kkhtml = "KFieldText", label = "3.2.4 可转债", sql = "convertbond = $S{convertbond}" ,field = "convertbond")
   private String convertbond;
   @GraphQLField(kkhtml = "KFieldText", label = "3.2.5 其他银行资本补充工具", sql = "otherbanksupplementtools = $S{otherbanksupplementtools}" ,field = "otherbanksupplementtools")
   private String otherbanksupplementtools;
   @GraphQLField(kkhtml = "KFieldText", label = "持仓类型", sql = "assettype = $S{assettype}" ,field = "assettype")
   private String assettype;
   @ExcelIgnore
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;
   @GraphQLField(kkhtml = "KFieldText", label = "应收未收金额", sql = "recvbl_prnc = $S{recvblPrnc}" ,field = "recvbl_prnc")
   private String recvblPrnc;
   @GraphQLField(kkhtml = "KFieldText", label = "持仓数量", sql = "hldn_qntt = $S{hldnQntt}" ,field = "hldn_qntt")
   private String hldnQntt;
   @GraphQLField(label = "菜单id")
   private String menuId;
   @GraphQLField(label = "按钮名称")
   private String buttonName;

   @GraphQLField(kkhtml = "KFieldText", label = "是否通过港股通投资", sql = "hk_inv = $S{hkInv}" ,field = "hk_inv")
   private String hkInv;
   @GraphQLField(kkhtml = "KFieldText", label = "是否通过QDII投资", sql = "qdii_inv = $S{qdiiInv}" ,field = "qdii_inv")
   private String qdiiInv;

}