package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;


@Data
@GraphQLModel(fetcher = "asharedescriptionService",table = "ods_supply_asharedescription")
public class AsharedescriptionModel {
   @GraphQLField(kkhtml = "KFieldText", label = "证券编号", sql = "scr_id = $S{scrId}" ,field = "scr_id")
   private String scrId;
   @GraphQLField(kkhtml = "KFieldText", label = "证券代码", sql = "scr_cd like '%$U{scrCd}%'" ,field = "scr_cd")
   private String scrCd;
   @GraphQLField(kkhtml = "KFieldText", label = "证券名称", sql = "scr_nm like '%$U{scrNm}%'" ,field = "scr_nm")
   private String scrNm;
   @GraphQLField(kkhtml = "KFieldText", label = "交易市场", sql = "trx_mkt = $S{trxMkt}" ,field = "trx_mkt")
   private String trxMkt;
   @GraphQLField(kkhtml = "KFieldText", label = "公司名称", sql = "company_namelike '%$U{companyName}%'" ,field = "company_name")
   private String companyName;
   @GraphQLField(kkhtml = "KFieldText", label = "板块类型", sql = "plate_type = $S{plateType}" ,field = "plate_type")
   private String plateType;
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "ccy = $S{ccy}" ,field = "ccy")
   private String ccy;
   @GraphQLField(kkhtml = "KFieldText", label = "中债一级分类", sql = "cbnd_frs_ctg = $S{cbndFrsCtg}" ,field = "cbnd_frs_ctg")
   private String cbndFrsCtg;
   @GraphQLField(kkhtml = "KFieldText", label = "中债二级分类", sql = "cbnd_scd_ctg = $S{cbndScdCtg}" ,field = "cbnd_scd_ctg")
   private String cbndScdCtg;
   @GraphQLField(kkhtml = "KFieldText", label = "人行一级分类", sql = "pbnk_frs_ctg = $S{pbnkFrsCtg}" ,field = "pbnk_frs_ctg")
   private String pbnkFrsCtg;
   @GraphQLField(kkhtml = "KFieldText", label = "人行二级分类", sql = "pbnk_scd_ctg = $S{pbnkScdCtg}" ,field = "pbnk_scd_ctg")
   private String pbnkScdCtg;
   @GraphQLField(kkhtml = "KFieldText", label = "人行三级分类", sql = "pbnk_trd_ctg = $S{pbnkTrdCtg}" ,field = "pbnk_trd_ctg")
   private String pbnkTrdCtg;
   @GraphQLField(kkhtml = "KFieldText", label = "股票类型", sql = "stock_type = $S{stockType}" ,field = "stock_type")
   private String stockType;
   @GraphQLField(kkhtml = "KFieldText", label = "投资阶段", sql = "investment_type = $S{investmentType}" ,field = "investment_type")
   private String investmentType;
   @GraphQLField(kkhtml = "KFieldText", label = "股权退出安排", sql = "sharehold = $S{sharehold}" ,field = "sharehold")
   private String sharehold;
   @GraphQLField(kkhtml = "KFieldText", label = "机构类型按规模划分", sql = "isu_org_typ_siz = $S{isuOrgTypSiz}" ,field = "isu_org_typ_siz")
   private String isuOrgTypSiz;
   @GraphQLField(kkhtml = "KFieldText", label = "机构类型按技术领域划分", sql = "isu_org_typ_tchno = $S{isuOrgTypTchno}" ,field = "isu_org_typ_tchno")
   private String isuOrgTypTchno;
   @GraphQLField(kkhtml = "KFieldText", label = "机构类型按经济类型划分", sql = "isu_org_typ_ecn = $S{isuOrgTypEcn}" ,field = "isu_org_typ_ecn")
   private String isuOrgTypEcn;
   @GraphQLField(kkhtml = "KFieldText", label = "机构所属行业", sql = "industry_issuer = $S{industryIssuer}" ,field = "industry_issuer")
   private String industryIssuer;
   @GraphQLField(kkhtml = "KFieldText", label = "是否为质押融资", sql = "pledged_finace = $S{pledgedFinace}" ,field = "pledged_finace")
   private String pledgedFinace;
   @GraphQLField(kkhtml = "KFieldText", label = "是否为债转股", sql = "debt_equity_swap = $S{debtEquitySwap}" ,field = "debt_equity_swap")
   private String debtEquitySwap;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;
   @GraphQLField(kkhtml = "KFieldText", label = "版本号", sql = "version = $S{version}" ,field = "version")
   private Integer version;
   @GraphQLField(kkhtml = "KFieldText", label = "外部资讯分类", sql = "ASS_INF_CLASS = $S{assInfClass}" ,field = "ASS_INF_CLASS")
   private Integer assInfClass;

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

    public String getScrNm() {
        return scrNm;
    }

    public void setScrNm(String scrNm) {
        this.scrNm = scrNm;
    }

    public String getTrxMkt() {
        return trxMkt;
    }

    public void setTrxMkt(String trxMkt) {
        this.trxMkt = trxMkt;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getCbndFrsCtg() {
        return cbndFrsCtg;
    }

    public void setCbndFrsCtg(String cbndFrsCtg) {
        this.cbndFrsCtg = cbndFrsCtg;
    }

    public String getCbndScdCtg() {
        return cbndScdCtg;
    }

    public void setCbndScdCtg(String cbndScdCtg) {
        this.cbndScdCtg = cbndScdCtg;
    }

    public String getPbnkFrsCtg() {
        return pbnkFrsCtg;
    }

    public void setPbnkFrsCtg(String pbnkFrsCtg) {
        this.pbnkFrsCtg = pbnkFrsCtg;
    }

    public String getPbnkScdCtg() {
        return pbnkScdCtg;
    }

    public void setPbnkScdCtg(String pbnkScdCtg) {
        this.pbnkScdCtg = pbnkScdCtg;
    }

    public String getPbnkTrdCtg() {
        return pbnkTrdCtg;
    }

    public void setPbnkTrdCtg(String pbnkTrdCtg) {
        this.pbnkTrdCtg = pbnkTrdCtg;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }

    public String getSharehold() {
        return sharehold;
    }

    public void setSharehold(String sharehold) {
        this.sharehold = sharehold;
    }

    public String getIsuOrgTypSiz() {
        return isuOrgTypSiz;
    }

    public void setIsuOrgTypSiz(String isuOrgTypSiz) {
        this.isuOrgTypSiz = isuOrgTypSiz;
    }

    public String getIsuOrgTypTchno() {
        return isuOrgTypTchno;
    }

    public void setIsuOrgTypTchno(String isuOrgTypTchno) {
        this.isuOrgTypTchno = isuOrgTypTchno;
    }

    public String getIsuOrgTypEcn() {
        return isuOrgTypEcn;
    }

    public void setIsuOrgTypEcn(String isuOrgTypEcn) {
        this.isuOrgTypEcn = isuOrgTypEcn;
    }

    public String getIndustryIssuer() {
        return industryIssuer;
    }

    public void setIndustryIssuer(String industryIssuer) {
        this.industryIssuer = industryIssuer;
    }

    public String getPledgedFinace() {
        return pledgedFinace;
    }

    public void setPledgedFinace(String pledgedFinace) {
        this.pledgedFinace = pledgedFinace;
    }

    public String getDebtEquitySwap() {
        return debtEquitySwap;
    }

    public void setDebtEquitySwap(String debtEquitySwap) {
        this.debtEquitySwap = debtEquitySwap;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}