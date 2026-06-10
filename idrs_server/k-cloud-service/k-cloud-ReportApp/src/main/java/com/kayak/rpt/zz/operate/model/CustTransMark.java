package com.kayak.rpt.zz.operate.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "custTransMarkService",table = "app_cust_trans_remark")
public class CustTransMark {
    @GraphQLField(label = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "操作用户", sql = "summit_user LIKE '%$U{summitUser}%'" ,field = "summit_user")
   private String summitUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "销售合同号", sql = "contract_no = $S{contractNo}" ,field = "contract_no")
   private String contractNo;
   @GraphQLField(kkhtml = "KFieldText", label = "核心交易流水号", sql = "trans_serno = $S{transSerno}" ,field = "trans_serno")
   private String transSerno;
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
   @GraphQLField(kkhtml = "KFieldText", label = "关联账号开户所在地 ", sql = "acct_loc_code = $S{acctLocCode}" ,field = "acct_loc_code")
   private String acctLocCode;
   @GraphQLField(kkhtml = "KFieldText", label = "是否代销", sql = "is_agent = $S{isAgent}" ,field = "is_agent")
   private String isAgent;
   @GraphQLField(kkhtml = "KFieldText", label = "销售机构代码", sql = "agent_bank_code = $S{agentBankCode}" ,field = "agent_bank_code")
   private String agentBankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "销售机构名称", sql = "agent_bank_name = $S{agentBankName}" ,field = "agent_bank_name")
   private String agentBankName;
   @GraphQLField(kkhtml = "KFieldText", label = "销售机构所属监管机构", sql = "agent_regu_code = $S{agentReguCode}" ,field = "agent_regu_code")
   private String agentReguCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code like '%$U{prodCode}%'" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "业务种类 ", sql = "busi_code = $S{busiCode}" ,field = "busi_code")
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
   @GraphQLField(kkhtml = "KFieldText", label = "数据操作类型（D", sql = "op_type = $S{opType}" ,field = "op_type")
   private String opType;
    @GraphQLField(label = "开始时间")
    private String startDate;

    @GraphQLField(label = "结束时间")
    private String endDate;
    @GraphQLField(label = "交易序列号")
    private String dealNo;
    @GraphQLField(kkhtml = "KFieldText", label = "关联活期存款账号开户行代码", sql = "acct_bank_no = $S{acctBankNo}" )
    private String acctBankNo;
    @GraphQLField(kkhtml = "KFieldText", label = "关联活期存款账号开户行名称", sql = "acct_bank_name = $S{acctBankName}" )
    private String acctBankName;

    @GraphQLField(kkhtml = "KFieldText",  label = "数据日期", sql = "report_date = $S{reportDate}" ,field = "reportDate")
    private String reportDate;

    @GraphQLField(kkhtml = "KFieldText", label = "子份额代码", sql = "son_share_code = $S{sonShareCode}" ,field = "sonShareCode")
    private String sonShareCode;

    @GraphQLField(kkhtml = "KFieldText", label = "特殊渠道", sql = "spe_channel_flag = $S{speChannelFlag}" ,field = "speChannelFlag")
    private String speChannelFlag;

  	public String getSummitUser() {
        return summitUser;
    }

    public void setSummitUser(String summitUser) {
        this.summitUser = summitUser;
    }
  	public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
  	public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
  	public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
  	public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
  	public String getTransSerno() {
        return transSerno;
    }

    public void setTransSerno(String transSerno) {
        this.transSerno = transSerno;
    }
  	public String getFncTransAcctNo() {
        return fncTransAcctNo;
    }

    public void setFncTransAcctNo(String fncTransAcctNo) {
        this.fncTransAcctNo = fncTransAcctNo;
    }
  	public String getHostCustNo() {
        return hostCustNo;
    }

    public void setHostCustNo(String hostCustNo) {
        this.hostCustNo = hostCustNo;
    }
  	public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }
  	public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }
  	public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }
  	public String getAcctLocCode() {
        return acctLocCode;
    }

    public void setAcctLocCode(String acctLocCode) {
        this.acctLocCode = acctLocCode;
    }
  	public String getIsAgent() {
        return isAgent;
    }

    public void setIsAgent(String isAgent) {
        this.isAgent = isAgent;
    }
  	public String getAgentBankCode() {
        return agentBankCode;
    }

    public void setAgentBankCode(String agentBankCode) {
        this.agentBankCode = agentBankCode;
    }
  	public String getAgentBankName() {
        return agentBankName;
    }

    public void setAgentBankName(String agentBankName) {
        this.agentBankName = agentBankName;
    }
  	public String getAgentReguCode() {
        return agentReguCode;
    }

    public void setAgentReguCode(String agentReguCode) {
        this.agentReguCode = agentReguCode;
    }
  	public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }
  	public String getBusiCode() {
        return busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }
  	public String getBusiReguCode() {
        return busiReguCode;
    }

    public void setBusiReguCode(String busiReguCode) {
        this.busiReguCode = busiReguCode;
    }
  	public String getAckDate() {
        return ackDate;
    }

    public void setAckDate(String ackDate) {
        this.ackDate = ackDate;
    }
  	public String getAckTime() {
        return ackTime;
    }

    public void setAckTime(String ackTime) {
        this.ackTime = ackTime;
    }
  	public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }
  	public String getAckAmt() {
        return ackAmt;
    }

    public void setAckAmt(String ackAmt) {
        this.ackAmt = ackAmt;
    }
  	public String getConvertRmb() {
        return convertRmb;
    }

    public void setConvertRmb(String convertRmb) {
        this.convertRmb = convertRmb;
    }
  	public String getNav() {
        return nav;
    }

    public void setNav(String nav) {
        this.nav = nav;
    }
  	public String getAckVol() {
        return ackVol;
    }

    public void setAckVol(String ackVol) {
        this.ackVol = ackVol;
    }
  	public String getFeeAmt() {
        return feeAmt;
    }

    public void setFeeAmt(String feeAmt) {
        this.feeAmt = feeAmt;
    }
  	public String getChannelFlag() {
        return channelFlag;
    }

    public void setChannelFlag(String channelFlag) {
        this.channelFlag = channelFlag;
    }
  	public String getInputuser() {
        return inputuser;
    }

    public void setInputuser(String inputuser) {
        this.inputuser = inputuser;
    }
  	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
  	public String getRegisterSerno() {
        return registerSerno;
    }

    public void setRegisterSerno(String registerSerno) {
        this.registerSerno = registerSerno;
    }
  	public String getImpDate() {
        return impDate;
    }

    public void setImpDate(String impDate) {
        this.impDate = impDate;
    }
  	public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
  	public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }
  	public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

}