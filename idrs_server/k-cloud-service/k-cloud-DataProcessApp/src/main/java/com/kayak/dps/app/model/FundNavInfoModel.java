package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "fundNavInfoModelService",table = "dwd_ast_fnd_nav_inf")
public class FundNavInfoModel {
   @GraphQLField(kkhtml = "KFieldText", label = "证券编号", sql = "scr_id = $S{scrId}" ,field = "scr_id")
   private String scrId;
   @GraphQLField(kkhtml = "KFieldText", label = "证券代码", sql = "scr_cd like '%$U{scrCd}%'" ,field = "scr_cd")
   private String scrCd;
   @GraphQLField(kkhtml = "KFieldText", label = "交易市场", sql = "trx_mkt = $S{trxMkt}" ,field = "trx_mkt")
   private String trxMkt;
   @GraphQLField(kkhtml = "KFieldText", label = "公告日期", sql = "ntc_dt = $S{ntcDt}" ,field = "ntc_dt")
   private String ntcDt;
   @GraphQLField(kkhtml = "KFieldText", label = "截止日期")
   private String stopDt;
   @GraphQLField(kkhtml = "KFieldText", label = "单位净值", sql = "unt_nav = $S{untNav}" ,field = "unt_nav")
   private String untNav;
   @GraphQLField(kkhtml = "KFieldText", label = "累计净值", sql = "acm_nav = $S{acmNav}" ,field = "acm_nav")
   private String acmNav;
   @GraphQLField(kkhtml = "KFieldText", label = "累计分红", sql = "acm_dvd = $S{acmDvd}" ,field = "acm_dvd")
   private String acmDvd;
   @GraphQLField(kkhtml = "KFieldText", label = "复权因子", sql = "adj_fct = $S{adjFct}" ,field = "adj_fct")
   private String adjFct;
   @GraphQLField(kkhtml = "KFieldText", label = "货币代码", sql = "ccy_cd = $S{ccyCd}" ,field = "ccy_cd")
   private String ccyCd;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
   private String updDt;
    //证券名称
    @GraphQLField(label = "基金名称")
    private String scrNm;
    //证券简称
    @GraphQLField(label = "基金简称")
    private String scrShtNm;
    @GraphQLField(label = "收盘价")
    private String clsPrc;
    @GraphQLField(label = "七日年化收益率（%）")
    private String rct7dAnlYld;
    @GraphQLField(label = "万份收益（元）")
    private String tenThsdShrErn;
    @GraphQLField(label = "基金类型")
    private String fundType;
    @GraphQLField(kkhtml = "KFieldText", label = "开始日期")
    private String startDate;
    @GraphQLField(kkhtml = "KFieldText", label = "结束日期")
    private String endDate;

    private String dealDate;
    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }
  	public String getScrId() {
        return scrId;
    }

    public void setScrId(String scrId) {
        this.scrId = scrId;
    }
  	public String getScrCd() {
        return scrCd;
    }

    public void setScrCd(String scrCd) {
        this.scrCd = scrCd;
    }
  	public String getTrxMkt() {
        return trxMkt;
    }

    public void setTrxMkt(String trxMkt) {
        this.trxMkt = trxMkt;
    }
  	public String getNtcDt() {
        return ntcDt;
    }

    public void setNtcDt(String ntcDt) {
        this.ntcDt = ntcDt;
    }
  	public String getStopDt() {
        return stopDt;
    }

    public void setStopDt(String stopDt) {
        this.stopDt = stopDt;
    }
  	public String getUntNav() {
        return untNav;
    }

    public void setUntNav(String untNav) {
        this.untNav = untNav;
    }
  	public String getAcmNav() {
        return acmNav;
    }

    public void setAcmNav(String acmNav) {
        this.acmNav = acmNav;
    }
  	public String getAcmDvd() {
        return acmDvd;
    }

    public void setAcmDvd(String acmDvd) {
        this.acmDvd = acmDvd;
    }
  	public String getAdjFct() {
        return adjFct;
    }

    public void setAdjFct(String adjFct) {
        this.adjFct = adjFct;
    }
  	public String getCcyCd() {
        return ccyCd;
    }

    public void setCcyCd(String ccyCd) {
        this.ccyCd = ccyCd;
    }
  	public String getCrtDt() {
        return crtDt;
    }

    public void setCrtDt(String crtDt) {
        this.crtDt = crtDt;
    }
  	public String getUpdDt() {
        return updDt;
    }

    public void setUpdDt(String updDt) {
        this.updDt = updDt;
    }

}