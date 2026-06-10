package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "assetHoldRegistCheckService",table = "app_asset_hold_regist_check")
@Data
public class AssetHoldRegistCheck {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_reg_enc = $S{prodRegEnc}" ,field = "prod_reg_enc")
   private String prodRegEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品总资产金额(元)", sql = "tot_assets = $S{totAssets}" ,field = "tot_assets")
   private String totAssets;
   @GraphQLField(kkhtml = "KFieldText", label = "净资产", sql = "assets_jn = $S{assetsJn}" ,field = "assets_jn")
   private String assetsJn;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透前资产", sql = "org_assets = $S{orgAssets}" ,field = "org_assets")
   private String orgAssets;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透前负债", sql = "org_debt = $S{orgDebt}" ,field = "org_debt")
   private String orgDebt;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透前资产净值", sql = "org_assets_jn = $S{orgAssetsJn}" ,field = "org_assets_jn")
   private String orgAssetsJn;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透前资产差额", sql = "org_assets_ce = $S{orgAssetsCe}" ,field = "org_assets_ce")
   private String orgAssetsCe;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透前资产差异比例（%）", sql = "org_assets_cerate = $S{orgAssetsCerate}" ,field = "org_assets_cerate")
   private String orgAssetsCerate;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透前净资产差额", sql = "org_assets_jnce = $S{orgAssetsJnce}" ,field = "org_assets_jnce")
   private String orgAssetsJnce;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透前净资产差异比例（%）", sql = "org_assets_jncerate = $S{orgAssetsJncerate}" ,field = "org_assets_jncerate")
   private String orgAssetsJncerate;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透后总资产", sql = "otc_all_assets = $S{otcAllAssets}" ,field = "otc_all_assets")
   private String otcAllAssets;
   @GraphQLField(kkhtml = "KFieldText", label = "底层负债", sql = "buttom_debt = $S{buttomDebt}" ,field = "buttom_debt")
   private String buttomDebt;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透后总负债", sql = "otc_debt = $S{otcDebt}" ,field = "otc_debt")
   private String otcDebt;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透后资产", sql = "otc_assets = $S{otcAssets}" ,field = "otc_assets")
   private String otcAssets;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透后资产净值", sql = "otc_assets_jn = $S{otcAssetsJn}" ,field = "otc_assets_jn")
   private String otcAssetsJn;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透后资产差额", sql = "otc_assets_ce = $S{otcAssetsCe}" ,field = "otc_assets_ce")
   private String otcAssetsCe;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透后资产差额比例（%）", sql = "otc_assets_cerate = $S{otcAssetsCerate}" ,field = "otc_assets_cerate")
   private String otcAssetsCerate;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透后资产净值差额", sql = "otc_assets_jnce = $S{otcAssetsJnce}" ,field = "otc_assets_jnce")
   private String otcAssetsJnce;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透后资产净值差额比例（%）", sql = "otc_assets_jncerate = $S{otcAssetsJncerate}" ,field = "otc_assets_jncerate")
   private String otcAssetsJncerate;
   @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
   private String theoryReportStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
   private String theoryReportEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态（0：未处理，1：已登记，2：登记失败）", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "状态 (0-失效，1-生效）", sql = "sys_data_status = $S{sysDataStatus}" ,field = "sys_data_status")
   private String sysDataStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "版本(从1.0开始自增)", sql = "sys_data_version = $S{sysDataVersion}" ,field = "sys_data_version")
   private String sysDataVersion;
   @GraphQLField(kkhtml = "KFieldText", label = "数据源(1-系统生成，2-人工导入)", sql = "sys_data_source = $S{sysDataSource}" ,field = "sys_data_source")
   private String sysDataSource;
   @GraphQLField(kkhtml = "KFieldText", label = "报表日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;

}