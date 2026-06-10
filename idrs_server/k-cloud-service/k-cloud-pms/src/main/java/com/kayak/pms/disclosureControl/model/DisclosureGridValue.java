package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureGridValueService",table = "app_grid_fbasset_holding_analysis_data")
public class DisclosureGridValue {
   @GraphQLField(key = true ,field = "id")
   private String id;
   @GraphQLField( label = "信披类型",field = "disclosure_type")
   private String disclosureType;
   @GraphQLField( label = "信披子类型",field = "disclosure_son_type")
   private String disclosureSonType;
   @GraphQLField( label = "公告版本id",field = "notice_version_id")
   private String noticeVersionId;
   @GraphQLField( label = "任务id",field = "task_id")
   private String taskId;
   @GraphQLField( label = "产品代码",field = "prod_cd")
   private String prodCd;
   @GraphQLField( label = "所有产品代码")
   private String prodCds;
   @GraphQLField( label = "产品名称",field = "prod_nm")
   private String prodNm;
   @GraphQLField( label = "数据日期",field = "deal_date")
   private String dealDate;
   @GraphQLField( label = "创建日期",field = "crt_date")
   private String crtDate;
   @GraphQLField( label = "创建日期",field = "crt_dte")
   private String crtDte;
   @GraphQLField( label = "创建时间",field = "crt_time")
   private String crtTime;
   @GraphQLField( label = "发布日期",field = "pos_dt")
   private String posDt;
   @GraphQLField( label = "发布起始日",field = "pos_start_dt")
   private String posStartDt;
   @GraphQLField( label = "发布结束日",field = "pos_end_dt")
   private String posEndDt;

   /**净值型产品净值情况*/
   @GraphQLField( label = "资产净值",field = "asset_value")
   private String assetValue;
   @GraphQLField( label = "净值日期",field = "nav_dt")
   private String navDt;
   @GraphQLField( label = "份额净值",field = "net_price")
   private String netPrice;

   /**资产配置情况*/
   @GraphQLField( label = "投资方式",field = "invest_way")
   private String investWay;
   @GraphQLField( label = "资产种类",field = "invest_type")
   private String investType;
   @GraphQLField( label = "余额(万元)",field = "balance_amt")
   private String balanceAmt;
   @GraphQLField( label = "占比",field = "ratio")
   private String ratio;

   /**非标资产持仓情况*/
   @GraphQLField( label = "序号",field = "row_num")
   private String rowNum;
   @GraphQLField( label = "融资客户",field = "finance_customer")
   private String financeCustomer;
   @GraphQLField( label = "项目名称",field = "project_name")
   private String projectName;
   @GraphQLField( label = "剩余融资期限",field = "left_days")
   private String leftDays;
   @GraphQLField( label = "到期收益分配",field = "income_allocate")
   private String incomeAllocate;
   @GraphQLField( label = "交易结构",field = "deal_structure")
   private String dealStructure;
   @GraphQLField( label = "风险状况",field = "risk_conditions")
   private String riskConditions;

   /**投资关联方情况*/
   @GraphQLField( label = "资产类型",field = "asset_type")
   private String assetType;
   @GraphQLField( label = "关联方名称",field = "affiliate_name")
   private String affiliateName;
   @GraphQLField( label = "资产代码",field = "asset_code")
   private String assetCode;
   @GraphQLField( label = "资产名称",field = "asset_name")
   private String assetName;
   @GraphQLField( label = "交易类型",field = "deal_type")
   private String dealType;
   @GraphQLField( label = "数量(单位:张)",field = "quantity")
   private String quantity;
   @GraphQLField( label = "总金额(单位:元)",field = "total_amount")
   private String totalAmount;
}