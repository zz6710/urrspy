package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "trCustTransInfoService",table = "app_cust_trans_info")
public class TrCustTransInfo {
    @GraphQLField(kkhtml = "KFieldText", label = "ID", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "登记机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
    private String bankCode;
    @GraphQLField(kkhtml = "KFieldText", label = "核心交易流水号", sql = "trans_serno = $S{transSerno}" ,field = "trans_serno")
    private String transSerno;
    @GraphQLField(kkhtml = "KFieldText", label = "销售合同号", sql = "contract_no = $S{contractNo}" ,field = "contract_no")
    private String contractNo;
    @GraphQLField(kkhtml = "KFieldText", label = "理财账号", sql = "fnc_trans_acct_no = $S{fncTransAcctNo}" ,field = "fnc_trans_acct_no")
    private String fncTransAcctNo;
    @GraphQLField(kkhtml = "KFieldText", label = "客户统一编号", sql = "host_cust_no = $S{hostCustNo}" ,field = "host_cust_no")
    private String hostCustNo;
    @GraphQLField(kkhtml = "KFieldText", label = "识别标识", sql = "cust_no = $S{custNo}" ,field = "cust_no")
    private String custNo;
    @GraphQLField(kkhtml = "KFieldText", label = "客户姓名", sql = "cust_name = $S{custName}" ,field = "cust_name")
    private String custName;
    @GraphQLField(kkhtml = "KFieldText", label = "关联活期存款账号", sql = "acct_no = $S{acctNo}" ,field = "acct_no")
    private String acctNo;
    @GraphQLField(kkhtml = "KFieldText", label = "关联账号开户所在地", sql = "acct_loc_code = $S{acctLocCode}" ,field = "acct_loc_code")
    private String acctLocCode;
    @GraphQLField(kkhtml = "KFieldText", label = "是否代销", sql = "is_agent = $S{isAgent}" ,field = "is_agent")
    private String isAgent;
    @GraphQLField(kkhtml = "KFieldText", label = "销售机构代码", sql = "agent_bank_code = $S{agentBankCode}" ,field = "agent_bank_code")
    private String agentBankCode;
    @GraphQLField(kkhtml = "KFieldText", label = "销售机构名称", sql = "agent_bank_name = $S{agentBankName}" ,field = "agent_bank_name")
    private String agentBankName;
    @GraphQLField(kkhtml = "KFieldText", label = "销售机构所属监管机构", sql = "agent_regu_code = $S{agentReguCode}" ,field = "agent_regu_code")
    private String agentReguCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "业务种类", sql = "busi_code = $S{busiCode}" ,field = "busi_code")
    private String busiCode;
    @GraphQLField(kkhtml = "KFieldText", label = "业务发生地所属监管", sql = "busi_regu_code = $S{busiReguCode}" ,field = "busi_regu_code")
    private String busiReguCode;
    @GraphQLField(kkhtml = "KFieldText", label = "业务确认日期", sql = "ack_date = $S{ackDate}" ,field = "ack_date")
    private String ackDate;
    @GraphQLField(kkhtml = "KFieldText", label = "业务确认时间", sql = "ack_time = $S{ackTime}" ,field = "ack_time")
    private String ackTime;
    @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "cur = $S{cur}" ,field = "cur")
    private String cur;
    @GraphQLField(kkhtml = "KFieldText", label = "金额", sql = "ack_amt = $S{ackAmt}" ,field = "ack_amt")
    private String ackAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "折算人民币金额", sql = "convert_rmb = $S{convertRmb}" ,field = "convert_rmb")
    private String convertRmb;
    @GraphQLField(kkhtml = "KFieldText", label = "确认净值", sql = "nav = $S{nav}" ,field = "nav")
    private String nav;
    @GraphQLField(kkhtml = "KFieldText", label = "份额", sql = "ack_vol = $S{ackVol}" ,field = "ack_vol")
    private String ackVol;
    @GraphQLField(kkhtml = "KFieldText", label = "费用", sql = "fee_amt = $S{feeAmt}" ,field = "fee_amt")
    private String feeAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "渠道", sql = "channel_flag = $S{channelFlag}" ,field = "channel_flag")
    private String channelFlag;
    @GraphQLField(kkhtml = "KFieldText", label = "交易柜员号", sql = "inputuser = $S{inputuser}" ,field = "inputuser")
    private String inputuser;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
    private String remark;
    @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
    private String registerSerno;
    @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
    private String impDate;
    @GraphQLField(kkhtml = "KFieldText", label = "登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
    private String registerDate;
    @GraphQLField(kkhtml = "KFieldText", label = "登记状态（0", sql = "register_status = $S{registerStatus}" ,field = "register_status")
    private String registerStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "是否审核通过", sql = "is_error = $S{isError}" ,field = "is_error")
    private String isError;
    @GraphQLField(kkhtml = "KFieldText", label = "交易序列号", sql = "deal_No = $S{dealNo}" )
    private String dealNo;
    @GraphQLField(kkhtml = "KFieldText", label = "关联活期存款账号开户行代码", sql = "acct_bank_no = $S{acctBankNo}" )
    private String acctBankNo;
    @GraphQLField(kkhtml = "KFieldText", label = "关联活期存款账号开户行名称", sql = "acct_bank_name = $S{acctBankName}" )
    private String acctBankName;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "create_date")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
    private String theoryReportEndDate;
    @GraphQLField(label = "开始时间")
    private String startDate;

    @GraphQLField(label = "结束时间")
    private String endDate;

    @GraphQLField(kkhtml = "KFieldText", sql = "theory_report_start_date >= $S{queryStartDate}" ,field = "queryStartDate")
    private String queryStartDate;

    @GraphQLField(kkhtml = "KFieldText", sql = "theory_report_start_date <= $S{queryEndDate}" ,field = "queryEndDate")
    private String queryEndDate;

    @GraphQLField(kkhtml = "KFieldText", sql = "audit_status = $S{auditStatus}" ,field = "audit_status")
    private String auditStatus;

    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;

    @GraphQLField(kkhtml = "KFieldText", label = "是否脱敏" ,field = "is_desensitization")
    private String isDesensitization;

    @GraphQLField(kkhtml = "KFieldText", label = "子份额代码", sql = "son_share_code = $S{sonShareCode}" ,field = "son_share_code")
    private String sonShareCode;

    @GraphQLField(kkhtml = "KFieldText", label = "特殊渠道", sql = "spe_channel_flag = $S{speChannelFlag}" ,field = "spe_channel_flag")
    private String speChannelFlag;

    @GraphQLField(kkhtml = "KFieldText",  label = "校验表名")
    private String validateTable;
    @GraphQLField(kkhtml = "KFieldText",  label = "数据日期")
    private String dealDate;
    @GraphQLField(kkhtml = "KFieldText",  label = "数据ID")
    private String dataId;
    @GraphQLField(kkhtml = "KFieldText",  label = "指标代码")
    private String indexCode;
    @GraphQLField(kkhtml = "KFieldText",  label = "校验错误日志")
    private String reason;

    //脱敏数据字段置于末端
    @GraphQLField(kkhtml = "KFieldText", field = "cust_name_display")
    private String custNameDisplay;
    @GraphQLField(kkhtml = "KFieldText", field = "acct_no_display")
    private String acctNoDisplay;

    @GraphQLField(kkhtml = "KFieldText",  label = "最小ID")
    private String minId;
    @GraphQLField(kkhtml = "KFieldText",  label = "最大ID")
    private String maxId;
}
