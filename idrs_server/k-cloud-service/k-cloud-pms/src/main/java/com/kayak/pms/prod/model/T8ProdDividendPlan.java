package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8ProdDividendPlanService",table = "t8_prod_dividend_plan")
public class T8ProdDividendPlan {
  @GraphQLField(label = "ID", sql = "id = $S{id}" ,field = "id")
  private String id;
  @GraphQLField(label = "产品代码",kkhtmlDefault = true, kkhtml = "KFieldText", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
  private String prodCode;
  @GraphQLField(label = "产品名称", kkhtmlDefault = true, kkhtml = "KFieldText",sql = "prod_name = $S{prodName}" ,field = "prod_name")
  private String prodName;
  @GraphQLField(label = "分红方式", kkhtmlDefault = true, kkhtml = "KFieldSelect", sql = "dividend_type = $S{dividendType}" ,field = "dividend_type",kkhtmlExt = "{\"data-dict\":\"t8_bonus_type\"}")
  private String dividendType;
  @GraphQLField(label = "当前份额", kkhtmlDefault = true, kkhtml = "KFieldText", sql = "share = $S{share}" ,field = "share")
  private String share;
  @GraphQLField(label = "当前份额", kkhtmlDefault = true, kkhtml = "KFieldText", sql = "bonus_model = $S{bonusModel}" ,field = "bonus_model")
  private String bonusModel;
  @GraphQLField(label = "分红模式", kkhtmlDefault = true, kkhtml = "KFieldSelect", sql = "dividend_mode = $S{dividendMode}" ,field = "dividend_mode",kkhtmlExt = "{\"data-dict\":\"t8_dividend_mode\"}")
  private String dividendMode;
  @GraphQLField(label = "分红金额", kkhtmlDefault = true, kkhtml = "KFieldText", sql = "amount = $S{amount}" ,field = "amount")
  private String amount;
  @GraphQLField(label = "分红基准日",  kkhtmlDefault = true, kkhtml = "KFieldDate",sql = "dividend_base_date = $S{dividendBaseDate}" ,field = "dividend_base_date")
  private String dividendBaseDate;
  @GraphQLField(label = "分红除权日",kkhtmlDefault = true, kkhtml = "KFieldDate", sql = "dividend_ex_date = $S{dividendExDate}" ,field = "dividend_ex_date")
  private String dividendExDate;
  @GraphQLField(label = "分红权益登记日",kkhtmlDefault = true, kkhtml = "KFieldDate", sql = "dividend_register_date = $S{dividendRegisterDate}" ,field = "dividend_register_date")
  private String dividendRegisterDate;
  @GraphQLField(label = "红利发放/再投日",kkhtmlDefault = true, kkhtml = "KFieldDate", sql = "hand_out_date = $S{handOutDate}" ,field = "hand_out_date")
  private String handOutDate;
  @GraphQLField(label = "清算天数",kkhtmlDefault = true, kkhtml = "KFieldText", sql = "liquidation_days = $S{liquidationDays}" ,field = "liquidation_days")
  private String liquidationDays;
  @GraphQLField(label = "备注",kkhtmlDefault = true, kkhtml = "KFieldText", sql = "remarks = $S{remarks}" ,field = "remarks")
  private String remarks;
  @GraphQLField(label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
  private String crtDate;
  @GraphQLField(label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
  private String crtTime;
  @GraphQLField(label = "创建人", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
  private String crtUser;
  @GraphQLField( label = "修改日期", sql = "upd_date = $S{updDate}" ,field = "upd_date")
  private String updDate;
  @GraphQLField(label = "修改时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
  private String updTime;
  @GraphQLField(label = "更新者", sql = "upd_user = $S{updUser}" ,field = "upd_user")
  private String updUser;
  @GraphQLField(sql = "process_instance_id = $S{processInstanceId}", field = "process_instance_id")
  private String processInstanceId;
  @GraphQLField(sql = "process_status = $S{processStatus}", field = "process_status")
  private String processStatus;

  
  @GraphQLField(sql = "dividend_status = $S{dividendStatus}", field = "dividend_status")
  private String dividendStatus;
  @GraphQLField(label = "可分配利润",sql = "profit = $S{profit}",kkhtmlDefault = true, kkhtml = "KFieldText", field = "profit")
  private String profit;

  @GraphQLField(label = "分红除权开始日",field = "dividend_ex_date")
  private String dividendExStartDate;
  @GraphQLField(label = "分红除权结束日",field = "dividend_ex_date")
  private String dividendExEndDate;

  @GraphQLField(label = "客户到账日",sql = "cust_arrival_date = $S{custArrivalDate}", kkhtml = "KFieldText", field = "cust_arrival_date")
  private String custArrivalDate;
  //用于附件删除
  @GraphQLField(field = "parentId")
  private String parentId;
  @GraphQLField(field = "prod_document_id")
  private String prodDocumentId;
  @GraphQLField(field = "path")
  private String path;
  @GraphQLField(field = "file_name")
  private String fileName;
  @GraphQLField(field = "attachment_type")
  private String attachmentType;
  @GraphQLField(field = "inputuser")
  private String inputuser;
  @GraphQLField(field = "updateuser")
  private String updateuser;
  @GraphQLField(field = "distributor_code")
  private String distributorCode;
  @GraphQLField(field = "t8_trutee_info_id")
  private String t8TruteeInfoId;
  @GraphQLField
  private String documentType;
}
