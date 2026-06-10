package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "investorSubHoldService",table = "app_cust_vol_register_sub_info")
public class InvestorSubHoldInfo {

    @GraphQLField(key = true , label = "ID" ,field = "id")
    private String id;

    @GraphQLField(label = "登记机构代码" ,field = "bank_code")
    private String bankCode;

    @GraphQLField(label = "产品登记编码" ,field = "prod_code")
    private String prodCode;

    @GraphQLField(label = "产品代码" ,field = "prod_code_m")
    private String prodCodeM;

    @GraphQLField(label = "子产品代码" ,field = "prod_code_s")
    private String prodCodeS;

    @GraphQLField(label = "识别标识" ,field = "cust_no")
    private String custNo;

    @GraphQLField(label = "持有日期" ,field = "hold_date")
    private String holdDate;

    @GraphQLField(label = "币种" ,field = "cur")
    private String cur;

    @GraphQLField(label = "持有份额" ,field = "hold_vol")
    private String holdVol;

    @GraphQLField(label = "持有金额" ,field = "hold_amt")
    private String holdAmt;

    @GraphQLField(label = "折算人民币金额(元)" ,field = "convert_rmb")
    private String convertRmb;

    @GraphQLField(label = "导入日期" ,field = "imp_date")
    private String impDate;

    @GraphQLField(label = "业务登记日期" ,field = "register_date")
    private String registerDate;

    @GraphQLField(label = "登记状态" ,field = "register_status")
    private String registerStatus;

    @GraphQLField(label = "登记流水号" ,field = "register_serno")
    private String registerSerno;

    @GraphQLField(label = "新增日期" ,field = "create_date")
    private String createDate;

    @GraphQLField(label = "理论报送起始日期" ,field = "theory_report_start_date")
    private String theoryReportStartDate;

    @GraphQLField(label = "理论报送截止日期" ,field = "theory_report_end_date")
    private String theoryReportEndDate;

    @GraphQLField(label = "报表日期" ,field = "report_date")
    private String reportDate;

    @GraphQLField(label = "TA_ID" ,field = "ta_id")
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

    @GraphQLField(label = "合并状态" ,field = "mrg_typ")
    private String mrgTyp;

    @GraphQLField(label = "合并指令id" ,field = "order_id")
    private String orderId;
}
