package com.kayak.dps.valtabimp.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "valReadassetsService",table = "ods_fa_readassets")
public class ValReadassets {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品id", sql = "t8_prod_base_id = $S{t8ProdBaseId}" ,field = "t8_prod_base_id")
   private String t8ProdBaseId;
   @GraphQLField(kkhtml = "KFieldText", label = "估值表日期", sql = "change_date = $S{changeDate}" ,field = "change_date")
   private String changeDate;
   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "ftool_code = $S{ftoolCode}" ,field = "ftool_code")
   private String ftoolCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资产名称", sql = "ftool_name = $S{ftoolName}" ,field = "ftool_name")
   private String ftoolName;
   @GraphQLField(kkhtml = "KFieldText", label = "品种ID", sql = "t8_sys_adtype_id = $S{t8SysAdtypeId}" ,field = "t8_sys_adtype_id")
   private String t8SysAdtypeId;
   @GraphQLField(kkhtml = "KFieldText", label = "市场", sql = "market = $S{market}" ,field = "market")
   private String market;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "account_type = $S{accountType}" ,field = "account_type")
   private String accountType;
   @GraphQLField(kkhtml = "KFieldText", label = "面额余额", sql = "positionbln = $S{positionbln}" ,field = "positionbln")
   private String positionbln;
   @GraphQLField(kkhtml = "KFieldText", label = "本金余额", sql = "principalbln = $S{principalbln}" ,field = "principalbln")
   private String principalbln;
   @GraphQLField(kkhtml = "KFieldText", label = "摊余成本余额，利息调整余额", sql = "interestbln = $S{interestbln}" ,field = "interestbln")
   private String interestbln;
   @GraphQLField(kkhtml = "KFieldText", label = "应收利息余额", sql = "accruedincomebln = $S{accruedincomebln}" ,field = "accruedincomebln")
   private String accruedincomebln;
   @GraphQLField(kkhtml = "KFieldText", label = "净价成本余额/成本余额", sql = "npamountbln = $S{npamountbln}" ,field = "npamountbln")
   private String npamountbln;
   @GraphQLField(kkhtml = "KFieldText", label = "费用余额", sql = "feepaybln = $S{feepaybln}" ,field = "feepaybln")
   private String feepaybln;
   @GraphQLField(kkhtml = "KFieldText", label = "公允价值变动余额", sql = "fairvaluebln = $S{fairvaluebln}" ,field = "fairvaluebln")
   private String fairvaluebln;
   @GraphQLField(kkhtml = "KFieldText", label = "应付税费余额", sql = "taxfeebln = $S{taxfeebln}" ,field = "taxfeebln")
   private String taxfeebln;
   @GraphQLField(kkhtml = "KFieldText", label = "待付税费余额", sql = "pay_taxbln = $S{payTaxbln}" ,field = "pay_taxbln")
   private String payTaxbln;
   @GraphQLField(kkhtml = "KFieldText", label = "应付利息余额", sql = "accruedpaybln = $S{accruedpaybln}" ,field = "accruedpaybln")
   private String accruedpaybln;
   @GraphQLField(kkhtml = "KFieldText", label = "证券清算款余额", sql = "securitiesliquidationbln = $S{securitiesliquidationbln}" ,field = "securitiesliquidationbln")
   private String securitiesliquidationbln;
   @GraphQLField(kkhtml = "KFieldText", label = "金融计算值", sql = "jrjs_value = $S{jrjsValue}" ,field = "jrjs_value")
   private String jrjsValue;
   @GraphQLField(kkhtml = "KFieldText", label = "录入柜员", sql = "inputuser = $S{inputuser}" ,field = "inputuser")
   private String inputuser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "是否产品或者资产1", sql = "isprodorasset = $S{isprodorasset}" ,field = "isprodorasset")
   private String isprodorasset;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name = $S{prodName}" ,field = "prod_name")
   private String prodName;
   @GraphQLField(kkhtml = "KFieldText", label = "当日行情", sql = "balance = $S{balance}" ,field = "balance")
   private String balance;
   
  	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
  	public String getT8ProdBaseId() {
        return t8ProdBaseId;
    }

    public void setT8ProdBaseId(String t8ProdBaseId) {
        this.t8ProdBaseId = t8ProdBaseId;
    }
  	public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }
  	public String getFtoolCode() {
        return ftoolCode;
    }

    public void setFtoolCode(String ftoolCode) {
        this.ftoolCode = ftoolCode;
    }
  	public String getFtoolName() {
        return ftoolName;
    }

    public void setFtoolName(String ftoolName) {
        this.ftoolName = ftoolName;
    }
  	public String getT8SysAdtypeId() {
        return t8SysAdtypeId;
    }

    public void setT8SysAdtypeId(String t8SysAdtypeId) {
        this.t8SysAdtypeId = t8SysAdtypeId;
    }
  	public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }
  	public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
  	public String getPositionbln() {
        return positionbln;
    }

    public void setPositionbln(String positionbln) {
        this.positionbln = positionbln;
    }
  	public String getPrincipalbln() {
        return principalbln;
    }

    public void setPrincipalbln(String principalbln) {
        this.principalbln = principalbln;
    }
  	public String getInterestbln() {
        return interestbln;
    }

    public void setInterestbln(String interestbln) {
        this.interestbln = interestbln;
    }
  	public String getAccruedincomebln() {
        return accruedincomebln;
    }

    public void setAccruedincomebln(String accruedincomebln) {
        this.accruedincomebln = accruedincomebln;
    }
  	public String getNpamountbln() {
        return npamountbln;
    }

    public void setNpamountbln(String npamountbln) {
        this.npamountbln = npamountbln;
    }
  	public String getFeepaybln() {
        return feepaybln;
    }

    public void setFeepaybln(String feepaybln) {
        this.feepaybln = feepaybln;
    }
  	public String getFairvaluebln() {
        return fairvaluebln;
    }

    public void setFairvaluebln(String fairvaluebln) {
        this.fairvaluebln = fairvaluebln;
    }
  	public String getTaxfeebln() {
        return taxfeebln;
    }

    public void setTaxfeebln(String taxfeebln) {
        this.taxfeebln = taxfeebln;
    }
  	public String getPayTaxbln() {
        return payTaxbln;
    }

    public void setPayTaxbln(String payTaxbln) {
        this.payTaxbln = payTaxbln;
    }
  	public String getAccruedpaybln() {
        return accruedpaybln;
    }

    public void setAccruedpaybln(String accruedpaybln) {
        this.accruedpaybln = accruedpaybln;
    }
  	public String getSecuritiesliquidationbln() {
        return securitiesliquidationbln;
    }

    public void setSecuritiesliquidationbln(String securitiesliquidationbln) {
        this.securitiesliquidationbln = securitiesliquidationbln;
    }
  	public String getJrjsValue() {
        return jrjsValue;
    }

    public void setJrjsValue(String jrjsValue) {
        this.jrjsValue = jrjsValue;
    }
  	public String getInputuser() {
        return inputuser;
    }

    public void setInputuser(String inputuser) {
        this.inputuser = inputuser;
    }
  	public String getCrtDate() {
        return crtDate;
    }

    public void setCrtDate(String crtDate) {
        this.crtDate = crtDate;
    }
  	public String getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(String crtTime) {
        this.crtTime = crtTime;
    }
  	public String getIsprodorasset() {
        return isprodorasset;
    }

    public void setIsprodorasset(String isprodorasset) {
        this.isprodorasset = isprodorasset;
    }
  	public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
  	public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

}