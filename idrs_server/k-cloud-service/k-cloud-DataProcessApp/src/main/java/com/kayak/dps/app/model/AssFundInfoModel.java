package com.kayak.dps.app.model;


import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "assFundInfoService", table = "ods_supply_fund_bas_inf")
public class AssFundInfoModel {
    //证券编号
    @ExcelProperty(value = "基金代码")
    @GraphQLField(label = "基金代码", sql = "SCR_ID = $S{scrId}", field = "SCR_ID")
    private String scrId;
    //中债一级分类
    @GraphQLField(label = "中债一级分类", sql = "CBND_FRS_CTG = $S{cbndFrsCtg}", field = "CBND_FRS_CTG")
    private String cbndFrsCtg;
    //中债二级分类
    @ExcelProperty(value = "中债二级分类")
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
    @GraphQLField(kkhtml = "KFieldText", label = "金融资产投资公司发行标识", sql = "fin_ast_inv_cmp_isu_f = $S{finAstInvCmpIsuF}" ,field = "fin_ast_inv_cmp_isu_f")
    private String finAstInvCmpIsuF;
    @ExcelProperty(value = "发行机构所属行业（二级分类）")
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构所属行业（二级分类）", sql = "idt = $S{idt}" ,field = "idt")
    private String idt;
    @GraphQLField(kkhtml = "KFieldText", label = "归属政府投资基金标识", sql = "blg_gov_inv_fnd_f = $S{blgGovInvFndF}" ,field = "blg_gov_inv_fnd_f")
    private String blgGovInvFndF;
    @GraphQLField(kkhtml = "KFieldText", label = "政府投资基金投向", sql = "gov_inv_fnd_dir = $S{govInvFndDir}" ,field = "gov_inv_fnd_dir")
    private String govInvFndDir;
    @GraphQLField(kkhtml = "KFieldText", label = "投资阶段", sql = "inv_stg = $S{invStg}" ,field = "inv_stg")
    private String invStg;
    @GraphQLField(kkhtml = "KFieldText", label = "投资企业类型", sql = "inv_entp_typ_siz = $S{invEntpTypSiz}" ,field = "inv_entp_typ_siz")
    private String invEntpTypSiz;
    @GraphQLField(kkhtml = "KFieldText", label = "投资企业类型", sql = "inv_entp_typ_tchno = $S{invEntpTypTchno}" ,field = "inv_entp_typ_tchno")
    private String invEntpTypTchno;
    @GraphQLField(kkhtml = "KFieldText", label = "投资企业类型", sql = "inv_entp_typ_ecn = $S{invEntpTypEcn}" ,field = "inv_entp_typ_ecn")
    private String invEntpTypEcn;
    @ExcelProperty(value = "基金投资资产")
    @GraphQLField(kkhtml = "KFieldText", label = "基金投资资产", sql = "fnd_inv_ast = $S{fndInvAst}" ,field = "fnd_inv_ast")
    private String fndInvAst;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
    private String updDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
    private String crtDt;
    @ExcelProperty(value = "登记备案机构")
    @GraphQLField(label = "登记备案机构")
    private String regRcdOrg;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "cmt = $S{cmt}" ,field = "cmt")
    private String cmt;
    @ExcelProperty(value = "交易流通场所")
    @GraphQLField(kkhtml = "KFieldText", label = "交易流通场所", sql = "trx_pla = $S{trxPla}" ,field = "trx_pla")
    private String trxPla;
    @ExcelProperty(value = "SPV机构编码")
    @GraphQLField(kkhtml = "KFieldText", label = "SPV机构编码", sql = "SPV_ORG_ENC = $S{spvOrgEnc}" ,field = "SPV_ORG_ENC")
    private String spvOrgEnc;
    @ExcelProperty(value = "SPV产品登记编码")
    @GraphQLField(kkhtml = "KFieldText", label = "SPV产品登记编码", sql = "SPV_PROD_REG_ENC = $S{spvProdRegEnc}" ,field = "SPV_PROD_REG_ENC")
    private String spvProdRegEnc;

}
