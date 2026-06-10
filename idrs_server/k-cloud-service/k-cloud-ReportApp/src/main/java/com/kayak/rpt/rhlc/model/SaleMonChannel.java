package com.kayak.rpt.rhlc.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "saleMonChannelService",table = "app_prod_sale_channel")
@Data
public class SaleMonChannel {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "*产品登记编码", sql = "prod_reg_enc = $S{prodRegEnc}" ,field = "prod_reg_enc")
   private String prodRegEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "*是否代销", sql = "sale_proxy = $S{saleProxy}" ,field = "sale_proxy")
   private String saleProxy;
   @GraphQLField(kkhtml = "KFieldText", label = "*销售机构代码", sql = "sale_org_code = $S{saleOrgCode}" ,field = "sale_org_code")
   private String saleOrgCode;
   @GraphQLField(kkhtml = "KFieldText", label = "*投资者数量", sql = "investor_num = $S{investorNum}" ,field = "investor_num")
   private String investorNum;
   @GraphQLField(kkhtml = "KFieldText", label = "*总销售金额", sql = "sale_total_money = $S{saleTotalMoney}" ,field = "sale_total_money")
   private String saleTotalMoney;
   @GraphQLField(kkhtml = "KFieldText", label = "*净销售金额", sql = "sale_net_money = $S{saleNetMoney}" ,field = "sale_net_money")
   private String saleNetMoney;
   @GraphQLField(kkhtml = "KFieldText", label = "*持有余额", sql = "hold_babance = $S{holdBabance}" ,field = "hold_babance")
   private String holdBabance;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "业务日期", sql = "trade_date = $S{tradeDate}" ,field = "trade_date")
   private String tradeDate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品特殊销售渠道", sql = "prod_esp_sale_channel = $S{prodEspSaleChannel}" ,field = "prod_esp_sale_channel")
   private String prodEspSaleChannel;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
   private String theoryReportStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
   private String theoryReportEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "报送状态", sql = "sys_data_status = $S{sysDataStatus}" ,field = "sys_data_status")
   private String sysDataStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "数据源(1-系统生成，2-人工导入)", sql = "sys_data_version = $S{sysDataVersion}" ,field = "sys_data_version")
   private String sysDataVersion;
   @GraphQLField(kkhtml = "KFieldText", label = "数据源(1-系统生成，2-人工导入)", sql = "sys_data_source = $S{sysDataSource}" ,field = "sys_data_source")
   private String sysDataSource;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;

}