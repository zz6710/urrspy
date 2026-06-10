package com.kayak.rpt.zz.historyInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "custVolRegisterInfohService",table = "app_cust_vol_register_info_h")
public class CustVolRegisterInfoh {
   @GraphQLField(kkhtml = "KFieldText", label = "登记机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "识别标识", sql = "cust_no = $S{custNo}" ,field = "cust_no")
   private String custNo;
   @GraphQLField(kkhtml = "KFieldText", label = "持有日期", sql = "hold_date = $S{holdDate}" ,field = "hold_date")
   private String holdDate;
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "cur = $S{cur}" ,field = "cur")
   private String cur;
   @GraphQLField(kkhtml = "KFieldText", label = "持有份额", sql = "hold_vol = $S{holdVol}" ,field = "hold_vol")
   private String holdVol;
   @GraphQLField(kkhtml = "KFieldText", label = "持有金额", sql = "hold_amt = $S{holdAmt}" ,field = "hold_amt")
   private String holdAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币金额", sql = "convert_rmb = $S{convertRmb}" ,field = "convert_rmb")
   private String convertRmb;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "记录ID", sql = "id = $S{id}" ,field = "id")
   private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;

    @GraphQLField(label = "导入日期开始时间")
    private String startDate;

    @GraphQLField(label = "导入日期截止时间")
    private String endDate;

    @GraphQLField(kkhtml = "KFieldText", sql = "hold_date >= $S{holdStartDate}" ,field = "hold_start_date")
    private String holdStartDate;

    @GraphQLField(kkhtml = "KFieldText", sql = "hold_date <= $S{holdEndDate}" ,field = "hold_end_date")
    private String holdEndDate;

    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", field = "report_date")
    private String reportDate;

    @GraphQLField(label = "合并状态" ,field = "mrg_typ")
    private String mrgTyp;

    @GraphQLField(label = "合并指令id" ,field = "order_id")
    private String orderId;

}