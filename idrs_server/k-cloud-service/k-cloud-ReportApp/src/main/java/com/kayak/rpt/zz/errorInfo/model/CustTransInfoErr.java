package com.kayak.rpt.zz.errorInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "custTransInfoErrService",table = "app_cust_trans_info_erdesc")
public class CustTransInfoErr {
    @GraphQLField(label = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "导入时间", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码错误描述", sql = "bank_code_desc = $S{bankCodeDesc}" ,field = "bank_code_desc")
   private String bankCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "核心交易流水号错误描述", sql = "trans_serno_desc = $S{transSernoDesc}" ,field = "trans_serno_desc")
   private String transSernoDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "销售合同号错误描述", sql = "contract_no_desc = $S{contractNoDesc}" ,field = "contract_no_desc")
   private String contractNoDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财账号错误描述", sql = "fnc_trans_acct_no_desc = $S{fncTransAcctNoDesc}" ,field = "fnc_trans_acct_no_desc")
   private String fncTransAcctNoDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "客户统一编号错误描述", sql = "host_cust_no_desc = $S{hostCustNoDesc}" ,field = "host_cust_no_desc")
   private String hostCustNoDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "识别标识错误描述", sql = "cust_no_desc = $S{custNoDesc}" ,field = "cust_no_desc")
   private String custNoDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "客户姓名错误描述", sql = "cust_name_desc = $S{custNameDesc}" ,field = "cust_name_desc")
   private String custNameDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "关联活期存款账号错误描述", sql = "acct_no_desc = $S{acctNoDesc}" ,field = "acct_no_desc")
   private String acctNoDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "关联账号开户所在地错误描述", sql = "acct_loc_code_desc = $S{acctLocCodeDesc}" ,field = "acct_loc_code_desc")
   private String acctLocCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "是否有其他机构代销错误描述", sql = "is_agent_desc = $S{isAgentDesc}" ,field = "is_agent_desc")
   private String isAgentDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "销售机构代码错误描述", sql = "agent_bank_code_desc = $S{agentBankCodeDesc}" ,field = "agent_bank_code_desc")
   private String agentBankCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "销售机构名称错误描述", sql = "agent_bank_name_desc = $S{agentBankNameDesc}" ,field = "agent_bank_name_desc")
   private String agentBankNameDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "销售机构所属监管机构错误描述", sql = "agent_regu_code_desc = $S{agentReguCodeDesc}" ,field = "agent_regu_code_desc")
   private String agentReguCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码错误描述", sql = "prod_code_desc = $S{prodCodeDesc}" ,field = "prod_code_desc")
   private String prodCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "业务种类错误描述", sql = "busi_code_desc = $S{busiCodeDesc}" ,field = "busi_code_desc")
   private String busiCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "业务发生地所属监管错误描述", sql = "busi_regu_code_desc = $S{busiReguCodeDesc}" ,field = "busi_regu_code_desc")
   private String busiReguCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "业务确认日期错误描述", sql = "ack_date_desc = $S{ackDateDesc}" ,field = "ack_date_desc")
   private String ackDateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "业务确认时间错误描述", sql = "ack_time_desc = $S{ackTimeDesc}" ,field = "ack_time_desc")
   private String ackTimeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "币种错误描述", sql = "cur_desc = $S{curDesc}" ,field = "cur_desc")
   private String curDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "金额错误描述", sql = "ack_amt_desc = $S{ackAmtDesc}" ,field = "ack_amt_desc")
   private String ackAmtDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币金额错误描述", sql = "convert_rmb_desc = $S{convertRmbDesc}" ,field = "convert_rmb_desc")
   private String convertRmbDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "确认净值错误描述", sql = "nav_desc = $S{navDesc}" ,field = "nav_desc")
   private String navDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "份额错误描述", sql = "ack_vol_desc = $S{ackVolDesc}" ,field = "ack_vol_desc")
   private String ackVolDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "费用错误描述", sql = "fee_amt_desc = $S{feeAmtDesc}" ,field = "fee_amt_desc")
   private String feeAmtDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "渠道错误描述", sql = "channel_flag_desc = $S{channelFlagDesc}" ,field = "channel_flag_desc")
   private String channelFlagDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "交易柜员号错误描述", sql = "inputuser_desc = $S{inputuserDesc}" ,field = "inputuser_desc")
   private String inputuserDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "备注错误描述", sql = "remark_desc = $S{remarkDesc}" ,field = "remark_desc")
   private String remarkDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号错误描述", sql = "register_serno_desc = $S{registerSernoDesc}" ,field = "register_serno_desc")
   private String registerSernoDesc;
    @GraphQLField(label = "导入日期开始时间")
    private String impStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;
    @GraphQLField(label = "导入日期截止时间")
    private String impEndDate;
    @GraphQLField(kkhtml = "KFieldText", label = "交易序列号错误描述", sql = "deal_no_desc = $S{dealNoDesc}" ,field = "deal_no_desc")
    private String dealNoDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "关联活期存款账号开户行代码错误描述", sql = "acct_bank_no_desc = $S{acctBankNo}" ,field = "acct_bank_no_desc")
    private String acctBankNoDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "关联活期存款账号开户行名称错误描述", sql = "acct_bank_name_desc = $S{acctBankNameDesc}" ,field = "acct_bank_name_desc")
    private String acctBankNameDesc;

    @GraphQLField(kkhtml = "KFieldText", label = "报告日期", sql = "report_date = $S{reportDate}" ,field = "reportDate")
    private String reportDate;

  	public String getImpDate() {
        return impDate;
    }

    public void setImpDate(String impDate) {
        this.impDate = impDate;
    }
  	public String getRegisterSerno() {
        return registerSerno;
    }

    public void setRegisterSerno(String registerSerno) {
        this.registerSerno = registerSerno;
    }
  	public String getBankCodeDesc() {
        return bankCodeDesc;
    }

    public void setBankCodeDesc(String bankCodeDesc) {
        this.bankCodeDesc = bankCodeDesc;
    }
  	public String getTransSernoDesc() {
        return transSernoDesc;
    }

    public void setTransSernoDesc(String transSernoDesc) {
        this.transSernoDesc = transSernoDesc;
    }
  	public String getContractNoDesc() {
        return contractNoDesc;
    }

    public void setContractNoDesc(String contractNoDesc) {
        this.contractNoDesc = contractNoDesc;
    }
  	public String getFncTransAcctNoDesc() {
        return fncTransAcctNoDesc;
    }

    public void setFncTransAcctNoDesc(String fncTransAcctNoDesc) {
        this.fncTransAcctNoDesc = fncTransAcctNoDesc;
    }
  	public String getHostCustNoDesc() {
        return hostCustNoDesc;
    }

    public void setHostCustNoDesc(String hostCustNoDesc) {
        this.hostCustNoDesc = hostCustNoDesc;
    }
  	public String getCustNoDesc() {
        return custNoDesc;
    }

    public void setCustNoDesc(String custNoDesc) {
        this.custNoDesc = custNoDesc;
    }
  	public String getCustNameDesc() {
        return custNameDesc;
    }

    public void setCustNameDesc(String custNameDesc) {
        this.custNameDesc = custNameDesc;
    }
  	public String getAcctNoDesc() {
        return acctNoDesc;
    }

    public void setAcctNoDesc(String acctNoDesc) {
        this.acctNoDesc = acctNoDesc;
    }
  	public String getAcctLocCodeDesc() {
        return acctLocCodeDesc;
    }

    public void setAcctLocCodeDesc(String acctLocCodeDesc) {
        this.acctLocCodeDesc = acctLocCodeDesc;
    }
  	public String getIsAgentDesc() {
        return isAgentDesc;
    }

    public void setIsAgentDesc(String isAgentDesc) {
        this.isAgentDesc = isAgentDesc;
    }
  	public String getAgentBankCodeDesc() {
        return agentBankCodeDesc;
    }

    public void setAgentBankCodeDesc(String agentBankCodeDesc) {
        this.agentBankCodeDesc = agentBankCodeDesc;
    }
  	public String getAgentBankNameDesc() {
        return agentBankNameDesc;
    }

    public void setAgentBankNameDesc(String agentBankNameDesc) {
        this.agentBankNameDesc = agentBankNameDesc;
    }
  	public String getAgentReguCodeDesc() {
        return agentReguCodeDesc;
    }

    public void setAgentReguCodeDesc(String agentReguCodeDesc) {
        this.agentReguCodeDesc = agentReguCodeDesc;
    }
  	public String getProdCodeDesc() {
        return prodCodeDesc;
    }

    public void setProdCodeDesc(String prodCodeDesc) {
        this.prodCodeDesc = prodCodeDesc;
    }
  	public String getBusiCodeDesc() {
        return busiCodeDesc;
    }

    public void setBusiCodeDesc(String busiCodeDesc) {
        this.busiCodeDesc = busiCodeDesc;
    }
  	public String getBusiReguCodeDesc() {
        return busiReguCodeDesc;
    }

    public void setBusiReguCodeDesc(String busiReguCodeDesc) {
        this.busiReguCodeDesc = busiReguCodeDesc;
    }
  	public String getAckDateDesc() {
        return ackDateDesc;
    }

    public void setAckDateDesc(String ackDateDesc) {
        this.ackDateDesc = ackDateDesc;
    }
  	public String getAckTimeDesc() {
        return ackTimeDesc;
    }

    public void setAckTimeDesc(String ackTimeDesc) {
        this.ackTimeDesc = ackTimeDesc;
    }
  	public String getCurDesc() {
        return curDesc;
    }

    public void setCurDesc(String curDesc) {
        this.curDesc = curDesc;
    }
  	public String getAckAmtDesc() {
        return ackAmtDesc;
    }

    public void setAckAmtDesc(String ackAmtDesc) {
        this.ackAmtDesc = ackAmtDesc;
    }
  	public String getConvertRmbDesc() {
        return convertRmbDesc;
    }

    public void setConvertRmbDesc(String convertRmbDesc) {
        this.convertRmbDesc = convertRmbDesc;
    }
  	public String getNavDesc() {
        return navDesc;
    }

    public void setNavDesc(String navDesc) {
        this.navDesc = navDesc;
    }
  	public String getAckVolDesc() {
        return ackVolDesc;
    }

    public void setAckVolDesc(String ackVolDesc) {
        this.ackVolDesc = ackVolDesc;
    }
  	public String getFeeAmtDesc() {
        return feeAmtDesc;
    }

    public void setFeeAmtDesc(String feeAmtDesc) {
        this.feeAmtDesc = feeAmtDesc;
    }
  	public String getChannelFlagDesc() {
        return channelFlagDesc;
    }

    public void setChannelFlagDesc(String channelFlagDesc) {
        this.channelFlagDesc = channelFlagDesc;
    }
  	public String getInputuserDesc() {
        return inputuserDesc;
    }

    public void setInputuserDesc(String inputuserDesc) {
        this.inputuserDesc = inputuserDesc;
    }
  	public String getRemarkDesc() {
        return remarkDesc;
    }

    public void setRemarkDesc(String remarkDesc) {
        this.remarkDesc = remarkDesc;
    }
  	public String getRegisterSernoDesc() {
        return registerSernoDesc;
    }

    public void setRegisterSernoDesc(String registerSernoDesc) {
        this.registerSernoDesc = registerSernoDesc;
    }


}