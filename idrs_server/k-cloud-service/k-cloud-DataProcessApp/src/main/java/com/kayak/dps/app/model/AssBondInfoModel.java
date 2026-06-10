package com.kayak.dps.app.model;


import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "assBondInfoService", table = "ods_supply_bond_bas_inf")
public class AssBondInfoModel {
    //证券编号
    @ExcelProperty(value = "债券代码")
    @GraphQLField(label = "债券代码", sql = "SCR_ID = $S{scrId}", field = "SCR_ID")
    private String scrId;
    //中债一级分类
    @GraphQLField(label = "中债一级分类", sql = "CBND_FRS_CTG = $S{cbndFrsCtg}", field = "CBND_FRS_CTG")
    private String cbndFrsCtg;
    //中债二级分类
    @GraphQLField(label = "中债二级分类", sql = "CBND_SCD_CTG = $S{cbndScdCtg}", field = "CBND_SCD_CTG")
    private String cbndScdCtg;
    //人行一级分类
    @GraphQLField(label = "人行一级分类", sql = "PBNK_FRS_CTG = $S{pbnkFrsCtg}", field = "PBNK_FRS_CTG")
    private String pbnkFrsCtg;
    //人行二级分类
    @GraphQLField(label = "人行二级分类", sql = "PBNK_SCD_CTG = $S{pbnkScdCtg}", field = "PBNK_SCD_CTG")
    private String pbnkScdCtg;
    //人行三级分类
    @GraphQLField(label = "人行三级分类", sql = "PBNK_TRD_CTG = $S{pbnkTrdCtg}", field = "PBNK_TRD_CTG")
    private String pbnkTrdCtg;
    @ExcelProperty(value = "人行与G06分类")
    @GraphQLField(label = "人行与G06分类", sql = "G06_TYPE = $S{g06Type}", field = "G06_TYPE")
    private String g06Type;
    //发行机构类型（按技术领域划分）
    @ExcelProperty(value = "发行机构类型（按规模划分）")
    @GraphQLField(label = "发行机构类型（按技术领域划分）", sql = "ISU_ORG_TYP_TCHNO = $S{isuOrgTypTchno}", field = "ISU_ORG_TYP_TCHNO")
    private String isuOrgTypTchno;
    //发行机构类型（按经济类型划分）
    @ExcelProperty(value = "发行机构类型（按经济类型划分）")
    @GraphQLField(label = "发行机构类型（按经济类型划分）", sql = "ISU_ORG_TYP_ECN = $S{isuOrgTypEcn}", field = "ISU_ORG_TYP_ECN")
    private String isuOrgTypEcn;
    @ExcelProperty(value = "企业规模")
    @GraphQLField(label = "企业规模", sql = "ISU_ORG_TYP_SIZ = $S{isuOrgTypSiz}", field = "ISU_ORG_TYP_SIZ")
    private String isuOrgTypSiz;
    @ExcelProperty(value = "发行机构类型（按规模划分）")
    @GraphQLField(label = "发行机构类型（按规模划分）", sql = "ISU_ORG_TYP_SCALE_SIZ = $S{isuOrgTypScaleSiz}", field = "ISU_ORG_TYP_SCALE_SIZ")
    private String isuOrgTypScaleSiz;
    //国民经济一级分类
    @ExcelProperty(value = "发行机构所属行业（一级分类）")
    @GraphQLField(label = "发行机构所属行业（一级分类）", sql = "ECO_FRS_TYP = $S{ecoFrsTyp}", field = "ECO_FRS_TYP")
    private String ecoFrsTyp;
    @ExcelProperty(value = "发行机构所属行业（二级分类）")
    @GraphQLField(label = "发行机构所属行业（二级分类）", sql = "ISU_ORG_BLG_IDT = $S{isuOrgBlgIdt}", field = "ISU_ORG_BLG_IDT")
    private String isuOrgBlgIdt;
    @ExcelProperty(value = "登记托管机构")
    @GraphQLField(label = "登记托管机构", sql = "REG_TRST_ORG = $S{regTrstOrg}", field = "REG_TRST_ORG")
    private String regTrstOrg;
    @GraphQLField(label = "是否净价结算", sql = "IS_NETPRC = $S{isNetprc}", field = "IS_NETPRC")
    private String isNetprc;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
    private String updDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
    private String crtDt;
    @ExcelProperty(value = "交易流通场所")
    @GraphQLField(kkhtml = "KFieldText", label = "交易流通场所", sql = "trx_pla = $S{trxPla}" ,field = "trx_pla")
    private String trxPla;
    @ExcelProperty(value = "具体类别")
    @GraphQLField(kkhtml = "KFieldText", label = "具体类别", sql = "spc_type = $S{spcType}" ,field = "spc_type")
    private String spcType;
    @ExcelProperty(value = "发行人")
    @GraphQLField(kkhtml = "KFieldText", label = "发行人", sql = "ISU = $S{isu}" ,field = "ISU")
    private String isu;
    @ExcelProperty(value = "主体评级")
    @GraphQLField(kkhtml = "KFieldText", label = "主体评级", sql = "MAIN_RAT = $S{mainRat}" ,field = "MAIN_RAT")
    private String mainRat;
    @ExcelProperty(value = "债项评级")
    @GraphQLField(kkhtml = "KFieldText", label = "债项评级", sql = "BOND_RAT = $S{bondRat}" ,field = "BOND_RAT")
    private String bondRat;
    @ExcelProperty(value = "发行省")
    @GraphQLField(label = "发行省", sql = "ISSU_PROVINCE = $S{issuProvince}", field = "ISSU_PROVINCE")
    private String issuProvince;
    @ExcelProperty(value = "发行市")
    @GraphQLField(label = "发行市", sql = "ISSU_CITY = $S{issuCity}", field = "ISSU_CITY")
    private String issuCity;

    public String getScrId() {
        return scrId;
    }

    public void setScrId(String scrId) {
        this.scrId = scrId;
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

    public String getG06Type() {
        return g06Type;
    }

    public void setG06Type(String g06Type) {
        this.g06Type = g06Type;
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

    public String getIsuOrgTypSiz() {
        return isuOrgTypSiz;
    }

    public void setIsuOrgTypSiz(String isuOrgTypSiz) {
        this.isuOrgTypSiz = isuOrgTypSiz;
    }

    public String getEcoFrsTyp() {
        return ecoFrsTyp;
    }

    public void setEcoFrsTyp(String ecoFrsTyp) {
        this.ecoFrsTyp = ecoFrsTyp;
    }

    public String getIsuOrgBlgIdt() {
        return isuOrgBlgIdt;
    }

    public void setIsuOrgBlgIdt(String isuOrgBlgIdt) {
        this.isuOrgBlgIdt = isuOrgBlgIdt;
    }

    public String getRegTrstOrg() {
        return regTrstOrg;
    }

    public void setRegTrstOrg(String regTrstOrg) {
        this.regTrstOrg = regTrstOrg;
    }

    public String getIsNetprc() {
        return isNetprc;
    }

    public void setIsNetprc(String isNetprc) {
        this.isNetprc = isNetprc;
    }

    public String getUpdDt() {
        return updDt;
    }

    public void setUpdDt(String updDt) {
        this.updDt = updDt;
    }

    public String getCrtDt() {
        return crtDt;
    }

    public void setCrtDt(String crtDt) {
        this.crtDt = crtDt;
    }

    public String getTrxPla() {
        return trxPla;
    }

    public void setTrxPla(String trxPla) {
        this.trxPla = trxPla;
    }

    public String getSpcType() {
        return spcType;
    }

    public void setSpcType(String spcType) {
        this.spcType = spcType;
    }

    public String getIssuProvince() {
        return issuProvince;
    }

    public void setIssuProvince(String issuProvince) {
        this.issuProvince = issuProvince;
    }

    public String getIssuCity() {
        return issuCity;
    }

    public void setIssuCity(String issuCity) {
        this.issuCity = issuCity;
    }

    public String getIsuOrgTypScaleSiz() {
        return isuOrgTypScaleSiz;
    }

    public void setIsuOrgTypScaleSiz(String isuOrgTypScaleSiz) {
        this.isuOrgTypScaleSiz = isuOrgTypScaleSiz;
    }

    public String getMainRat() {
        return mainRat;
    }

    public void setMainRat(String mainRat) {
        this.mainRat = mainRat;
    }

    public String getBondRat() {
        return bondRat;
    }

    public void setBondRat(String bondRat) {
        this.bondRat = bondRat;
    }

    public String getIsu() {
        return isu;
    }

    public void setIsu(String isu) {
        this.isu = isu;
    }
}
