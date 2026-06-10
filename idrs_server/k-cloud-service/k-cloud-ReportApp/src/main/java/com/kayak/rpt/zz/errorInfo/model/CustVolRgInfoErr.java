package com.kayak.rpt.zz.errorInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "custVolRgInfoErrService",table = "app_cust_vol_register_info_erdesc")
public class CustVolRgInfoErr {
   @GraphQLField(kkhtml = "KFieldText", label = "银行代码错误描述", sql = "bank_code_desc = $S{bankCodeDesc}" ,field = "bank_code_desc")
   private String bankCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码错误描述", sql = "prod_code_desc = $S{prodCodeDesc}" ,field = "prod_code_desc")
   private String prodCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "识别标识错误描述", sql = "cust_no_desc = $S{custNoDesc}" ,field = "cust_no_desc")
   private String custNoDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "持有日期错误描述", sql = "hold_date_desc = $S{holdDateDesc}" ,field = "hold_date_desc")
   private String holdDateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "币种错误描述", sql = "cur_desc = $S{curDesc}" ,field = "cur_desc")
   private String curDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "持有份额错误描述", sql = "hold_vol_desc = $S{holdVolDesc}" ,field = "hold_vol_desc")
   private String holdVolDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "持有金额错误描述", sql = "hold_amt_desc = $S{holdAmtDesc}" ,field = "hold_amt_desc")
   private String holdAmtDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币金额错误描述", sql = "convert_rmb_desc = $S{convertRmbDesc}" ,field = "convert_rmb_desc")
   private String convertRmbDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "记录ID", sql = "id = $S{id}" ,field = "id")
   private String id;
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
    @GraphQLField(kkhtml = "KFieldText", label = "报告日期", sql = "report_date = $S{reportDate}" ,field = "reportDate")
    private String reportDate;

  	public String getBankCodeDesc() {
        return bankCodeDesc;
    }

    public void setBankCodeDesc(String bankCodeDesc) {
        this.bankCodeDesc = bankCodeDesc;
    }
  	public String getProdCodeDesc() {
        return prodCodeDesc;
    }

    public void setProdCodeDesc(String prodCodeDesc) {
        this.prodCodeDesc = prodCodeDesc;
    }
  	public String getCustNoDesc() {
        return custNoDesc;
    }

    public void setCustNoDesc(String custNoDesc) {
        this.custNoDesc = custNoDesc;
    }
  	public String getHoldDateDesc() {
        return holdDateDesc;
    }

    public void setHoldDateDesc(String holdDateDesc) {
        this.holdDateDesc = holdDateDesc;
    }
  	public String getCurDesc() {
        return curDesc;
    }

    public void setCurDesc(String curDesc) {
        this.curDesc = curDesc;
    }
  	public String getHoldVolDesc() {
        return holdVolDesc;
    }

    public void setHoldVolDesc(String holdVolDesc) {
        this.holdVolDesc = holdVolDesc;
    }
  	public String getHoldAmtDesc() {
        return holdAmtDesc;
    }

    public void setHoldAmtDesc(String holdAmtDesc) {
        this.holdAmtDesc = holdAmtDesc;
    }
  	public String getConvertRmbDesc() {
        return convertRmbDesc;
    }

    public void setConvertRmbDesc(String convertRmbDesc) {
        this.convertRmbDesc = convertRmbDesc;
    }
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
  	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}