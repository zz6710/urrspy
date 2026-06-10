package com.kayak.rpt.rhlc.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "appAssetUnincorporatedEntityService",table = "app_asset_unincorporated_entity")
@Data
public class AppAssetUnincorporatedEntity {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "所属期", sql = "dt_dt = $S{dtDt}" ,field = "dt_dt")
   private String dtDt;
   @GraphQLField(kkhtml = "KFieldText", label = "产品全称", sql = "prdc_nm like '%$U{prdcNm}%'" ,field = "prdc_nm")
   private String prdcNm;
   @GraphQLField(kkhtml = "KFieldText", label = "产品21位码", sql = "prdc_cd = $S{prdcCd}" ,field = "prdc_cd")
   private String prdcCd;
   @GraphQLField(kkhtml = "KFieldText", label = "产品类型", sql = "prdc_type = $S{prdcType}" ,field = "prdc_type")
   private String prdcType;
   @GraphQLField(kkhtml = "KFieldText", label = "产品性质", sql = "prdc_class = $S{prdcClass}" ,field = "prdc_class")
   private String prdcClass;
   @GraphQLField(kkhtml = "KFieldText", label = "是否特殊类型基金", sql = "is_special_fund = $S{isSpecialFund}" ,field = "is_special_fund")
   private String isSpecialFund;
   @GraphQLField(kkhtml = "KFieldText", label = "净资产（亿元）", sql = "asset = $S{asset}" ,field = "asset")
   private String asset;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details = $S{details}" ,field = "details")
   private String details;
   @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
   private String theoryReportStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
   private String theoryReportEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "数据源(1-系统生成，2-人工导入)", sql = "sys_data_source = $S{sysDataSource}" ,field = "sys_data_source")
   private String sysDataSource;
   @GraphQLField(kkhtml = "KFieldText", label = "状态 (0-失效，1-生效）", sql = "sys_data_status = $S{sysDataStatus}" ,field = "sys_data_status")
   private String sysDataStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "版本(从1.0开始自增)", sql = "sys_data_version = $S{sysDataVersion}" ,field = "sys_data_version")
   private String sysDataVersion;

}