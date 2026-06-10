package com.kayak.dps.valtabimp.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "valReadassetsMergeService",table = "ods_fa_readassets_merge")
public class ValReadassetsMerge {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "表层资产代码", sql = "t1.asset_code = $S{assetCode}" ,field = "asset_code")
   private String assetCode;
   @GraphQLField(kkhtml = "KFieldText", label = "变动日期", sql = "change_date = $S{changeDate}" ,field = "change_date")
   private String changeDate;
   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "ftool_code = $S{ftoolCode}" ,field = "ftool_code")
   private String ftoolCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资产名称", sql = "ftool_name like '%$U{ftoolName}%'" ,field = "ftool_name")
   private String ftoolName;
   @GraphQLField(kkhtml = "KFieldText", label = "品种ID", sql = "t8_sys_adtype_id = $S{t8SysAdtypeId}" ,field = "t8_sys_adtype_id")
   private String t8SysAdtypeId;
   @GraphQLField(kkhtml = "KFieldText", label = "市场", sql = "market = $S{market}" ,field = "market")
   private String market;
   @GraphQLField(kkhtml = "KFieldText", label = "资产会计分类", sql = "account_type = $S{accountType}" ,field = "account_type")
   private String accountType;
   @GraphQLField(kkhtml = "KFieldText", label = "面额余额", sql = "positionbln = $S{positionbln}" ,field = "positionbln")
   private String positionbln;
   @GraphQLField(kkhtml = "KFieldText", label = "本金余额", sql = "principalbln = $S{principalbln}" ,field = "principalbln")
   private String principalbln;
   @GraphQLField(kkhtml = "KFieldText", label = "摊余成本余额，利息调整余额", sql = "interestbln = $S{interestbln}" ,field = "interestbln")
   private String interestbln;
   @GraphQLField(kkhtml = "KFieldText", label = "应收利息余额", sql = "accruedincomebln = $S{accruedincomebln}" ,field = "accruedincomebln")
   private String accruedincomebln;
   @GraphQLField(kkhtml = "KFieldText", label = "录入柜员", sql = "npamountbln = $S{npamountbln}" ,field = "npamountbln")
   private String npamountbln;
   @GraphQLField(kkhtml = "KFieldText", label = "录入柜员", sql = "feepaybln = $S{feepaybln}" ,field = "feepaybln")
   private String feepaybln;
   @GraphQLField(kkhtml = "KFieldText", label = "公允价余额", sql = "fairvaluebln = $S{fairvaluebln}" ,field = "fairvaluebln")
   private String fairvaluebln;
   @GraphQLField(kkhtml = "KFieldText", label = "应付税费余额", sql = "taxfeebln = $S{taxfeebln}" ,field = "taxfeebln")
   private String taxfeebln;
   @GraphQLField(kkhtml = "KFieldText", label = "待付税费余额", sql = "pay_taxbln = $S{payTaxbln}" ,field = "pay_taxbln")
   private String payTaxbln;
   @GraphQLField(kkhtml = "KFieldText", label = "应付利息余额", sql = "accruedpaybln = $S{accruedpaybln}" ,field = "accruedpaybln")
   private String accruedpaybln;
   @GraphQLField(kkhtml = "KFieldText", label = "证券清算款余额", sql = "securitiesliquidationbln = $S{securitiesliquidationbln}" ,field = "securitiesliquidationbln")
   private String securitiesliquidationbln;
   @GraphQLField(kkhtml = "KFieldText", label = "金融计算值", sql = "jrjs_value = $S{jrjsValue}" ,field = "jrjs_value")
   private String jrjsValue;
   @GraphQLField(kkhtml = "KFieldText", label = "录入柜员", sql = "inputuser = $S{inputuser}" ,field = "inputuser")
   private String inputuser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "是否产品或者资产1", sql = "t1.isprodorasset = $S{isprodorasset}" ,field = "isprodorasset")
   private String isprodorasset;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name = $S{prodName}" ,field = "prod_name")
   private String prodName;
   @GraphQLField(kkhtml = "KFieldText", label = "当日行情", sql = "balance = $S{balance}" ,field = "balance")
   private String balance;
   @GraphQLField(kkhtml = "KFieldText", label = "估值表ID", sql = "t4.id = $S{t8ValReporttabId}" ,field = "t8ValReporttabId")
   private String t8ValReporttabId;
   private String adName;
   private String  reporttabName;
   private String  isprodorassetname;
}