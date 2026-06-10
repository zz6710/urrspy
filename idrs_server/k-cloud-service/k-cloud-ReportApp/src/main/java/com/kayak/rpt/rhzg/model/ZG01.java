package com.kayak.rpt.rhzg.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//资管产品基本信息
@Data
@GraphQLModel(fetcher = "ZG01Service",table = "app_pbc_report_zg01")
public class ZG01 {

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
    private String id;

    @ExcelProperty(value = "信息类型")
    @GraphQLField(kkhtml = "KFieldText", label = "信息类型", sql = "msg_typ = $S{msgTyp}" ,field = "msg_typ")
    private String msgTyp;

    @ExcelProperty(value = "数据日期")
    @GraphQLField(kkhtml = "KFieldText", label = "实际报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
    private String theoryReportStartDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "create_date")
    private String createDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
    private String theoryReportEndDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
    private String registerStatus;


    @ExcelProperty(value = "产品代码_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd like '%$U{prodCd}%'" ,field = "prod_cd")
    private String prodCd;

    @ExcelProperty(value = "产品名称")
    @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_nm = $S{prodNm}" ,field = "prod_nm")
    private String prodNm;

    @ExcelProperty(value = "发行机构代码")
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "isu_org_cd = $S{isuOrgCd}" ,field = "isu_org_cd")
    private String isuOrgCd;

    @ExcelProperty(value = "发行机构名称")
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构名称", sql = "isu_org_nm = $S{isuOrgNm}" ,field = "isu_org_nm")
    private String isuOrgNm;

    @ExcelProperty(value = "产品品种_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "产品品种", sql = "prod_cate = $S{prodCate}" ,field = "prod_cate")
    private String prodCate;

    @ExcelProperty(value = "产品类型")
    @GraphQLField(kkhtml = "KFieldText", label = "产品类型", sql = "prod_inv_typ = $S{prodInvTyp}" ,field = "prod_inv_typ")
    private String prodInvTyp;

    @ExcelProperty(value = "产品品牌")
    @GraphQLField(kkhtml = "KFieldText", label = "产品品牌", sql = "prod_brnd = $S{prodBrnd}" ,field = "prod_brnd")
    private String prodBrnd;

    @ExcelProperty(value = "产品期次")
    @GraphQLField(kkhtml = "KFieldText", label = "产品期次", sql = "prod_tms = $S{prodTms}" ,field = "prod_tms")
    private String prodTms;

    @ExcelProperty(value = "发行机构内部产品代码")
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构内部产品代码", sql = "isu_org_prod_cd = $S{isuOrgProdCd}" ,field = "isu_org_prod_cd")
    private String isuOrgProdCd;

    @ExcelProperty(value = "募集资金币种")
    @GraphQLField(kkhtml = "KFieldText", label = "募集资金币种", sql = "clc_ccy = $S{clcCcy}" ,field = "clc_ccy")
    private String clcCcy;

    @ExcelProperty(value = "兑付本金币种")
    @GraphQLField(kkhtml = "KFieldText", label = "兑付本金币种", sql = "call_prcp_ccy = $S{callPrcpCcy}" ,field = "call_prcp_ccy")
    private String callPrcpCcy;

    @ExcelProperty(value = "兑付收益币种")
    @GraphQLField(kkhtml = "KFieldText", label = "兑付收益币种", sql = "call_ern_ccy = $S{callErnCcy}" ,field = "call_ern_ccy")
    private String callErnCcy;

    @ExcelProperty(value = "募集方式")
    @GraphQLField(kkhtml = "KFieldText", label = "募集方式", sql = "prod_clc_mth = $S{prodClcMth}" ,field = "prod_clc_mth")
    private String prodClcMth;

    @ExcelProperty(value = "管理方式")
    @GraphQLField(kkhtml = "KFieldText", label = "管理方式", sql = "mng_mth = $S{mngMth}" ,field = "mng_mth")
    private String mngMth;

    @ExcelProperty(value = "运行方式_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "运行方式", sql = "prod_mod = $S{prodMod}" ,field = "prod_mod")
    private String prodMod;

    @ExcelProperty(value = "募集起始日期")
    @GraphQLField(kkhtml = "KFieldText", label = "募集起始日期", sql = "clc_bgn_dt = $S{clcBgnDt}" ,field = "clc_bgn_dt")
    private String clcBgnDt;

    @ExcelProperty(value = "募集结束日期")
    @GraphQLField(kkhtml = "KFieldText", label = "募集结束日期", sql = "clc_end_dt = $S{clcEndDt}" ,field = "clc_end_dt")
    private String clcEndDt;

    @ExcelProperty(value = "发行机构提前终止权标识")
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构提前终止权标识", sql = "isu_org_early_term_f = $S{isuOrgEarlyTermF}" ,field = "isu_org_early_term_f")
    private String isuOrgEarlyTermF;

    @ExcelProperty(value = "客户赎回权标识")
    @GraphQLField(kkhtml = "KFieldText", label = "客户赎回权标识", sql = "cust_redemption_f = $S{custRedemptionF}" ,field = "cust_redemption_f")
    private String custRedemptionF;

    @ExcelProperty(value = "产品增信标识")
    @GraphQLField(kkhtml = "KFieldText", label = "产品增信标识", sql = "prod_inc_crd_f = $S{prodIncCrdF}" ,field = "prod_inc_crd_f")
    private String prodIncCrdF;

    @ExcelProperty(value = "增信机构类型")
    @GraphQLField(kkhtml = "KFieldText", label = "增信机构类型", sql = "prod_inc_crd_org_typ = $S{prodIncCrdOrgTyp}" ,field = "prod_inc_crd_org_typ")
    private String prodIncCrdOrgTyp;

    @ExcelProperty(value = "增信形式")
    @GraphQLField(kkhtml = "KFieldText", label = "增信形式", sql = "prod_inc_crd_form = $S{prodIncCrdForm}" ,field = "prod_inc_crd_form")
    private String prodIncCrdForm;

    @ExcelProperty(value = "境内托管机构代码")
    @GraphQLField(kkhtml = "KFieldText", label = "境内托管机构代码", sql = "dms_trst_org_cd = $S{dmsTrstOrgCd}" ,field = "dms_trst_org_cd")
    private String dmsTrstOrgCd;

    @ExcelProperty(value = "境外托管机构国别代码")
    @GraphQLField(kkhtml = "KFieldText", label = "境外托管机构国别代码", sql = "ovs_trst_org_cnr = $S{ovsTrstOrgCnr}" ,field = "ovs_trst_org_cnr")
    private String ovsTrstOrgCnr;

    @ExcelProperty(value = "托管机构名称")
    @GraphQLField(kkhtml = "KFieldText", label = "境外托管机构名称", sql = "ovs_trst_org_nm = $S{ovsTrstOrgNm}" ,field = "ovs_trst_org_nm")
    private String ovsTrstOrgNm;

    @ExcelProperty(value = "产品起始日期")
    @GraphQLField(kkhtml = "KFieldText", label = "产品起始日期", sql = "found_dt = $S{foundDt}" ,field = "found_dt")
    private String foundDt;

    @ExcelProperty(value = "产品变更日期")
    @GraphQLField(kkhtml = "KFieldText", label = "产品变更日期", sql = "change_dt = $S{changeDt}" ,field = "change_dt")
    private String changeDt;

    @ExcelProperty(value = "产品预计终止日期")
    @GraphQLField(kkhtml = "KFieldText", label = "产品预计终止日期", sql = "prod_scheduled_end_dt = $S{prodScheduledEndDt}" ,field = "prod_scheduled_end_dt")
    private String prodScheduledEndDt;

    @ExcelProperty(value = "受托机构管理职责")
    @GraphQLField(kkhtml = "KFieldText", label = "受托机构管理职责", sql = "entrusted_duty = $S{entrustedDuty}" ,field = "entrusted_duty")
    private String entrustedDuty;

    @ExcelProperty(value = "分级产品标识")
    @GraphQLField(kkhtml = "KFieldText", label = "分级产品标识", sql = "clsf_prod_f = $S{clsfProdF}" ,field = "clsf_prod_f")
    private String clsfProdF;

    @ExcelProperty(value = "收益权转让产品标识")
    @GraphQLField(kkhtml = "KFieldText", label = "收益权转让产品标识", sql = "usufruct_change_prod_f = $S{usufructChangeProdF}" ,field = "usufruct_change_prod_f")
    private String usufructChangeProdF;

    @ExcelProperty(value = "货基或现金管理类产品标识")
    @GraphQLField(kkhtml = "KFieldText", label = "货基或现金管理类产品标识", sql = "cash_mng_prod_f = $S{cashMngProdF}" ,field = "cash_mng_prod_f")
    private String cashMngProdF;

    @ExcelProperty(value = "跨境理财通")
    @GraphQLField(kkhtml = "KFieldText", label = "跨境理财通", sql = "cb_w_mng_f = $S{cbWMngF}" ,field = "cb_w_mng_f")
    private String cbWMngF;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "以财产权信托名义开展的资金信托", sql = "ownership_trust_f = $S{ownershipTrustF}" ,field = "ownership_trust_f")
    private String ownershipTrustF;
    @ExcelProperty(value = "信托产品类型")
    @GraphQLField(kkhtml = "KFieldText", label = "信托产品类型", sql = "trust_prod_type = $S{trustProdType}" ,field = "trust_prod_type")
    private String trustProdType;

    @ExcelProperty(value = "基本信息公开标识")
    @GraphQLField(kkhtml = "KFieldText", label = "基本信息公开标识", sql = "base_open_info_f = $S{baseOpenInfoF}" ,field = "base_open_info_f")
    private String baseOpenInfoF;

    @ExcelProperty(value = "变更原因")
    @GraphQLField(kkhtml = "KFieldText", label = "变更原因", sql = "change_reason = $S{changeReason}" ,field = "change_reason")
    private String changeReason;

    @ExcelProperty(value = "产品展期标识")
    @GraphQLField(kkhtml = "KFieldText", label = "产品展期标识", sql = "back1 = $S{back1}" ,field = "back1")
    private String back1;

    @ExcelProperty(value = "产品登记注册编码")
    @GraphQLField(kkhtml = "KFieldText", label = "产品登记注册编码", sql = "back2 = $S{back2}" ,field = "back2")
    private String back2;

    @ExcelProperty(value = "是否处于清算中")
    @GraphQLField(kkhtml = "KFieldText", label = "是否处于清算中", sql = "back3 = $S{back3}" ,field = "back3")
    private String back3;

    @ExcelProperty(value = "最短开放周期")
    @GraphQLField(kkhtml = "KFieldText", label = "最短开放周期", sql = "back4 = $S{back4}" ,field = "back4")
    private String back4;

    @ExcelProperty(value = "备用字段5")
    @GraphQLField(kkhtml = "KFieldText", label = "备用字段5", sql = "back5 = $S{back5}" ,field = "back5")
    private String back5;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围起始", sql = "theory_report_start_date >= $S{beginDate}" ,field = "begin_date")
    private String beginDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围终止", sql = "theory_report_start_date <= $S{queryDate}" ,field = "query_date")
    private String queryDate;

    @ExcelProperty(value = "备用字段5")
    @GraphQLField(kkhtml = "KFieldText", label = "备用字段5", sql = "sys_data_version = $S{sysDataVersion}" ,field = "sys_data_version")
    private String sysDataVersion;

    @GraphQLField(kkhtml = "KFieldText", label = "报送日期起始", sql = "report_date >= $S{reportBeginDate}" ,field = "report_begin_date")
    private String reportBeginDate;
    @GraphQLField(kkhtml = "KFieldText", label = "报送日期终止", sql = "report_date <= $S{reportEndDate}" ,field = "report_end_date")
    private String reportEndDate;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgTyp() {
        return msgTyp;
    }

    public void setMsgTyp(String msgTyp) {
        this.msgTyp = msgTyp;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getTheoryReportStartDate() {
        return theoryReportStartDate;
    }

    public void setTheoryReportStartDate(String theoryReportStartDate) {
        this.theoryReportStartDate = theoryReportStartDate;
    }

    public String getTheoryReportEndDate() {
        return theoryReportEndDate;
    }

    public void setTheoryReportEndDate(String theoryReportEndDate) {
        this.theoryReportEndDate = theoryReportEndDate;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }

    public String getProdCd() {
        return prodCd;
    }

    public void setProdCd(String prodCd) {
        this.prodCd = prodCd;
    }

    public String getProdNm() {
        return prodNm;
    }

    public void setProdNm(String prodNm) {
        this.prodNm = prodNm;
    }

    public String getIsuOrgCd() {
        return isuOrgCd;
    }

    public void setIsuOrgCd(String isuOrgCd) {
        this.isuOrgCd = isuOrgCd;
    }

    public String getIsuOrgNm() {
        return isuOrgNm;
    }

    public void setIsuOrgNm(String isuOrgNm) {
        this.isuOrgNm = isuOrgNm;
    }

    public String getProdCate() {
        return prodCate;
    }

    public void setProdCate(String prodCate) {
        this.prodCate = prodCate;
    }

    public String getProdInvTyp() {
        return prodInvTyp;
    }

    public void setProdInvTyp(String prodInvTyp) {
        this.prodInvTyp = prodInvTyp;
    }

    public String getProdBrnd() {
        return prodBrnd;
    }

    public void setProdBrnd(String prodBrnd) {
        this.prodBrnd = prodBrnd;
    }

    public String getProdTms() {
        return prodTms;
    }

    public void setProdTms(String prodTms) {
        this.prodTms = prodTms;
    }

    public String getIsuOrgProdCd() {
        return isuOrgProdCd;
    }

    public void setIsuOrgProdCd(String isuOrgProdCd) {
        this.isuOrgProdCd = isuOrgProdCd;
    }

    public String getClcCcy() {
        return clcCcy;
    }

    public void setClcCcy(String clcCcy) {
        this.clcCcy = clcCcy;
    }

    public String getCallPrcpCcy() {
        return callPrcpCcy;
    }

    public void setCallPrcpCcy(String callPrcpCcy) {
        this.callPrcpCcy = callPrcpCcy;
    }

    public String getCallErnCcy() {
        return callErnCcy;
    }

    public void setCallErnCcy(String callErnCcy) {
        this.callErnCcy = callErnCcy;
    }

    public String getProdClcMth() {
        return prodClcMth;
    }

    public void setProdClcMth(String prodClcMth) {
        this.prodClcMth = prodClcMth;
    }

    public String getMngMth() {
        return mngMth;
    }

    public void setMngMth(String mngMth) {
        this.mngMth = mngMth;
    }

    public String getProdMod() {
        return prodMod;
    }

    public void setProdMod(String prodMod) {
        this.prodMod = prodMod;
    }

    public String getClcBgnDt() {
        return clcBgnDt;
    }

    public void setClcBgnDt(String clcBgnDt) {
        this.clcBgnDt = clcBgnDt;
    }

    public String getClcEndDt() {
        return clcEndDt;
    }

    public void setClcEndDt(String clcEndDt) {
        this.clcEndDt = clcEndDt;
    }

    public String getIsuOrgEarlyTermF() {
        return isuOrgEarlyTermF;
    }

    public void setIsuOrgEarlyTermF(String isuOrgEarlyTermF) {
        this.isuOrgEarlyTermF = isuOrgEarlyTermF;
    }

    public String getCustRedemptionF() {
        return custRedemptionF;
    }

    public void setCustRedemptionF(String custRedemptionF) {
        this.custRedemptionF = custRedemptionF;
    }

    public String getProdIncCrdF() {
        return prodIncCrdF;
    }

    public void setProdIncCrdF(String prodIncCrdF) {
        this.prodIncCrdF = prodIncCrdF;
    }

    public String getProdIncCrdOrgTyp() {
        return prodIncCrdOrgTyp;
    }

    public void setProdIncCrdOrgTyp(String prodIncCrdOrgTyp) {
        this.prodIncCrdOrgTyp = prodIncCrdOrgTyp;
    }

    public String getProdIncCrdForm() {
        return prodIncCrdForm;
    }

    public void setProdIncCrdForm(String prodIncCrdForm) {
        this.prodIncCrdForm = prodIncCrdForm;
    }

    public String getDmsTrstOrgCd() {
        return dmsTrstOrgCd;
    }

    public void setDmsTrstOrgCd(String dmsTrstOrgCd) {
        this.dmsTrstOrgCd = dmsTrstOrgCd;
    }

    public String getOvsTrstOrgCnr() {
        return ovsTrstOrgCnr;
    }

    public void setOvsTrstOrgCnr(String ovsTrstOrgCnr) {
        this.ovsTrstOrgCnr = ovsTrstOrgCnr;
    }

    public String getOvsTrstOrgNm() {
        return ovsTrstOrgNm;
    }

    public void setOvsTrstOrgNm(String ovsTrstOrgNm) {
        this.ovsTrstOrgNm = ovsTrstOrgNm;
    }

    public String getFoundDt() {
        return foundDt;
    }

    public void setFoundDt(String foundDt) {
        this.foundDt = foundDt;
    }

    public String getChangeDt() {
        return changeDt;
    }

    public void setChangeDt(String changeDt) {
        this.changeDt = changeDt;
    }

    public String getProdScheduledEndDt() {
        return prodScheduledEndDt;
    }

    public void setProdScheduledEndDt(String prodScheduledEndDt) {
        this.prodScheduledEndDt = prodScheduledEndDt;
    }

    public String getEntrustedDuty() {
        return entrustedDuty;
    }

    public void setEntrustedDuty(String entrustedDuty) {
        this.entrustedDuty = entrustedDuty;
    }

    public String getClsfProdF() {
        return clsfProdF;
    }

    public void setClsfProdF(String clsfProdF) {
        this.clsfProdF = clsfProdF;
    }

    public String getUsufructChangeProdF() {
        return usufructChangeProdF;
    }

    public void setUsufructChangeProdF(String usufructChangeProdF) {
        this.usufructChangeProdF = usufructChangeProdF;
    }

    public String getCashMngProdF() {
        return cashMngProdF;
    }

    public void setCashMngProdF(String cashMngProdF) {
        this.cashMngProdF = cashMngProdF;
    }

    public String getCbWMngF() {
        return cbWMngF;
    }

    public void setCbWMngF(String cbWMngF) {
        this.cbWMngF = cbWMngF;
    }

    public String getOwnershipTrustF() {
        return ownershipTrustF;
    }

    public void setOwnershipTrustF(String ownershipTrustF) {
        this.ownershipTrustF = ownershipTrustF;
    }

    public String getBaseOpenInfoF() {
        return baseOpenInfoF;
    }

    public void setBaseOpenInfoF(String baseOpenInfoF) {
        this.baseOpenInfoF = baseOpenInfoF;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getBack1() {
        return back1;
    }

    public void setBack1(String back1) {
        this.back1 = back1;
    }

    public String getBack2() {
        return back2;
    }

    public void setBack2(String back2) {
        this.back2 = back2;
    }

    public String getBack3() {
        return back3;
    }

    public void setBack3(String back3) {
        this.back3 = back3;
    }

    public String getBack4() {
        return back4;
    }

    public void setBack4(String back4) {
        this.back4 = back4;
    }

    public String getBack5() {
        return back5;
    }

    public void setBack5(String back5) {
        this.back5 = back5;
    }
}
