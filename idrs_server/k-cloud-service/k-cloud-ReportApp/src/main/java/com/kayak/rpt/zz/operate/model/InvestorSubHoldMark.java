package com.kayak.rpt.zz.operate.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "investorSubHoldMarkService",table = "app_cust_vol_register_sub_remark")
@Data
public class InvestorSubHoldMark {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "操作用户", sql = "op_user = $S{opUser}" ,field = "op_user")
   private String opUser;
   @GraphQLField(kkhtml = "KFieldText", label = "操作日期", sql = "op_date = $S{opDate}" ,field = "op_date")
   private String opDate;
   @GraphQLField(kkhtml = "KFieldText", label = "操作时间", sql = "op_time = $S{opTime}" ,field = "op_time")
   private String opTime;
   @GraphQLField(kkhtml = "KFieldText", label = "数据操作类型（D", sql = "op_type = $S{opType}" ,field = "op_type")
   private String opType;
   @GraphQLField(kkhtml = "KFieldText", label = "登记机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "母产品代码", sql = "prod_code_m = $S{prodCodeM}" ,field = "prod_code_m")
   private String prodCodeM;
   @GraphQLField(kkhtml = "KFieldText", label = "子产品代码", sql = "prod_code_s = $S{prodCodeS}" ,field = "prod_code_s")
   private String prodCodeS;
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
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币金额（元）", sql = "convert_rmb = $S{convertRmb}" ,field = "convert_rmb")
   private String convertRmb;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "业务登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
   private String theoryReportStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
   private String theoryReportEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "报表日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "合并类型:0-合并前 1-合并后", sql = "mrg_typ = $S{mrgTyp}" ,field = "mrg_typ")
   private String mrgTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "TA_ID", sql = "ta_id = $S{taId}" ,field = "ta_id")
   private String taId;
   @GraphQLField(label = "投资者类别" ,field = "cust_type")
   private String custType;
   @GraphQLField(label = "渠道号" ,field = "channel_code")
   private String channelCode;
   @GraphQLField(label = "个人证件类别" ,field = "personal_id_type")
   private String personalIdType;
   @GraphQLField(label = "机构证件类别" ,field = "organization_id_type")
   private String organizationIdType;
   @GraphQLField(label = "其他证件名称" ,field = "other_id_name")
   private String otherIdName;
   @GraphQLField(label = "证件号码" ,field = "id_code")
   private String idCode;
   @GraphQLField(label = "开始时间")
   private String startDate;
   @GraphQLField(label = "结束时间")
   private String endDate;
   @GraphQLField(label = "开始时间")
   private String holdStartDate;
   @GraphQLField(label = "结束时间")
   private String holdEndDate;

}