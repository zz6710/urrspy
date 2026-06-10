package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "netValSPVInfoModelService",table = "ast_ast_mng_plan_inf")
public class NetValSPVInfoModel {


    @GraphQLField(kkhtml = "KFieldText", label = "证券编码", sql = "SCR_ID= $S{scrId}" ,field = "SCR_ID")
    private String scrId;
    @GraphQLField(kkhtml = "KFieldText", label = "证券代码", sql = "SCR_CD= $S{scrCd}" ,field = "SCR_CD")
    private String scrCd;
    @GraphQLField(kkhtml = "KFieldText", label = "证券名称", sql = "SCR_NM= $S{scrNm}" ,field = "SCR_NM")
    private String scrNm;
    @GraphQLField(kkhtml = "KFieldText", label = "交易市场", sql = "TRX_MKT= $S{trxMkt}" ,field = "TRX_MKT")
    private String trxMkt;
    @GraphQLField(kkhtml = "KFieldText", label = "交易流通场所", sql = "TRX_PLA= $S{trxPla}" ,field = "TRX_PLA")
    private String trxPla;
    @GraphQLField(kkhtml = "KFieldText", label = "投资方式", sql = "INVEST_WAY= $S{investWay}" ,field = "INVEST_WAY")
    private String investWay;
    @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "CCY= $S{ccy}" ,field = "CCY")
    private String ccy;
    @GraphQLField(kkhtml = "KFieldText", label = "SPV机构编码", sql = "SPV_ORG_ENC= $S{spvOrgEnc}" ,field = "SPV_ORG_ENC")
    private String spvOrgEnc;
    @GraphQLField(kkhtml = "KFieldText", label = "SPV产品登记编码", sql = "SPV_PROD_REG_ENC= $S{spvProdRegEnc}" ,field = "SPV_PROD_REG_ENC")
    private String spvProdRegEnc;
    @GraphQLField(kkhtml = "KFieldText", label = "管理人", sql = "MNG= $S{mng}" ,field = "MNG")
    private String mng;
    @GraphQLField(kkhtml = "KFieldText", label = "托管人", sql = "CSTD= $S{cstd}" ,field = "CSTD")
    private String cstd;
    @GraphQLField(kkhtml = "KFieldText", label = "资金实际投向", sql = "FND_ACTL_DIR= $S{fndActlDir}" ,field = "FND_ACTL_DIR")
    private String fndActlDir;
    @GraphQLField(kkhtml = "KFieldText", label = "资金运用行业", sql = "FND_CRRY_IDT= $S{fndCrryIdt}" ,field = "FND_CRRY_IDT")
    private String fndCrryIdt;
    @GraphQLField(kkhtml = "KFieldText", label = "成立日期", sql = "SET_UP_DT= $S{setUpDt}" ,field = "SET_UP_DT")
    private String setUpDt;
    @GraphQLField(kkhtml = "KFieldText", label = "到期日期", sql = "MTU_DT= $S{mtuDt}" ,field = "MTU_DT")
    private String mtuDt;
    @GraphQLField(kkhtml = "KFieldText", label = "预期收益率标识", sql = "EXPE_RAT_F= $S{expeRatF}" ,field = "EXPE_RAT_F")
    private String expeRatF;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "CRT_DATE= $S{crtDate}" ,field = "CRT_DATE")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "CRT_TIME= $S{crtTime}" ,field = "CRT_TIME")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "CRT_USER= $S{crtUser}" ,field = "CRT_USER")
    private String crtUser;
    @GraphQLField(kkhtml = "KFieldText", label = "修改日期", sql = "UPD_DATE= $S{updDate}" ,field = "UPD_DATE")
    private String updDate;
    @GraphQLField(kkhtml = "KFieldText", label = "修改时间", sql = "UPD_TIME= $S{updTime}" ,field = "UPD_TIME")
    private String updTime;
    @GraphQLField(kkhtml = "KFieldText", label = "修改人", sql = "UPD_USER= $S{updUser}" ,field = "UPD_USER")
    private String updUser;
    @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "DEAL_DATE= $S{dealDate}" ,field = "DEAL_DATE")
    private String dealDate;

    @GraphQLField(kkhtml = "KFieldText", label = "投向私募资产产品", sql = "IS_FIN_ISU_F= $S{isFinIsuF}" ,field = "IS_FIN_ISU_F")
    private String isFinIsuF;
    @GraphQLField(kkhtml = "KFieldText", label = "银行理财产品标识", sql = "BNK_INV_PROD_F= $S{bnkInvProdF}" ,field = "BNK_INV_PROD_F")
    private String bnkInvProdF;
    @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "PROD_REG_ENC= $S{prodRegEnc}" ,field = "PROD_REG_ENC")
    private String prodRegEnc;
    @GraphQLField(kkhtml = "KFieldText", label = "金融资产投资公司发行标识", sql = "FIN_AST_INV_CMP_ISU_F= $S{finAstInvCmpIsuF}" ,field = "FIN_AST_INV_CMP_ISU_F")
    private String finAstInvCmpIsuF;
    @GraphQLField(kkhtml = "KFieldText", label = "金额", sql = "AMT= $S{amt}" ,field = "AMT")
    private String amt;
    @GraphQLField(kkhtml = "KFieldText", label = "资金运用方式", sql = "FND_CRRY_MTH= $S{fndCrryMth}" ,field = "FND_CRRY_MTH")
    private String fndCrryMth;
    @GraphQLField(kkhtml = "KFieldText", label = "资管计划属性", sql = "AST_MNG_PLAN_PRPT= $S{astMngPlanPrpt}" ,field = "AST_MNG_PLAN_PRPT")
    private String astMngPlanPrpt;
    @GraphQLField(kkhtml = "KFieldText", label = "预期最高收益率", sql = "EXPE_MAX_RAT= $S{expeMaxRat}" ,field = "EXPE_MAX_RAT")
    private String expeMaxRat;
    @GraphQLField(kkhtml = "KFieldText", label = "预期最低收益率", sql = "EXPE_MIN_RAT= $S{expeMinRat}" ,field = "EXPE_MIN_RAT")
    private String expeMinRat;
    @GraphQLField(kkhtml = "KFieldText", label = "购买结构", sql = "BUY_STRC= $S{buyStrc}" ,field = "BUY_STRC")
    private String buyStrc;
    @GraphQLField(kkhtml = "KFieldText", label = "管理方式", sql = "MNG_MTH= $S{mngMth}" ,field = "MNG_MTH")
    private String mngMth;
    @GraphQLField(kkhtml = "KFieldText", label = "管理费率", sql = "MNG_FEE_TAT= $S{mngFeeTat}" ,field = "MNG_FEE_TAT")
    private String mngFeeTat;
    @GraphQLField(kkhtml = "KFieldText", label = "托管费率", sql = "TRST_FEE_TAT= $S{trstFeeTat}" ,field = "TRST_FEE_TAT")
    private String trstFeeTat;
    @GraphQLField(kkhtml = "KFieldText", label = "交易相关合计费率", sql = "TRX_REL_SMR_FEE_RAT= $S{trxRelSmrFeeRat}" ,field = "TRX_REL_SMR_FEE_RAT")
    private String trxRelSmrFeeRat;
    @GraphQLField(kkhtml = "KFieldText", label = "中介服务机构合计费率", sql = "MED_AGN_SRV_ORG_SMR_FEE_RAT= $S{medAgnSrvOrgSmrFeeRat}" ,field = "MED_AGN_SRV_ORG_SMR_FEE_RAT")
    private String medAgnSrvOrgSmrFeeRat;
    @GraphQLField(kkhtml = "KFieldText", label = "其他合计费率", sql = "OTH_SMR_FEE_RAT= $S{othSmrFeeRat}" ,field = "OTH_SMR_FEE_RAT")
    private String othSmrFeeRat;
    @GraphQLField(kkhtml = "KFieldText", label = "发起人机构编码", sql = "ISU_ORG_ENC= $S{isuOrgEnc}" ,field = "ISU_ORG_ENC")
    private String isuOrgEnc;
    @GraphQLField(kkhtml = "KFieldText", label = "中债一级分类", sql = "CBND_FRS_CTG= $S{cbndFrsCtg}" ,field = "CBND_FRS_CTG")
    private String cbndFrsCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "中债二级分类", sql = "CBND_SCD_CTG= $S{cbndScdCtg}" ,field = "CBND_SCD_CTG")
    private String cbndScdCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "G06二级分类", sql = "GG_CBC_SUB_TYPE= $S{ggCbcSubType}" ,field = "GG_CBC_SUB_TYPE")
    private String ggCbcSubType;
    @GraphQLField(kkhtml = "KFieldText", label = "G06一级分类", sql = "GG_CBC_TYPE= $S{ggCbcType}" ,field = "GG_CBC_TYPE")
    private String ggCbcType;
    @GraphQLField(kkhtml = "KFieldText", label = "人行一级分类", sql = "PBNK_FRS_CTG= $S{pbnkFrsCtg}" ,field = "PBNK_FRS_CTG")
    private String pbnkFrsCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "人行二级分类", sql = "PBNK_SCD_CTG= $S{pbnkScdCtg}" ,field = "PBNK_SCD_CTG")
    private String pbnkScdCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "人行三级分类", sql = "PBNK_TRD_CTG= $S{pbnkTrdCtg}" ,field = "PBNK_TRD_CTG")
    private String pbnkTrdCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "人行四级分类", sql = "PBNK_FUR_CTG= $S{pbnkFurCtg}" ,field = "PBNK_FUR_CTG")
    private String pbnkFurCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "CMT= $S{cmt}" ,field = "CMT")
    private String cmt;
    @GraphQLField(kkhtml = "KFieldText", label = "版本号", sql = "VERSION= $S{version}" ,field = "VERSION")
    private String version;
    @GraphQLField(kkhtml = "KFieldText", label = "起息日检索起始日",field = "VAL_DT_START")
    private String valDtStart;
    @GraphQLField(kkhtml = "KFieldText", label = "起息日检索结束日",field = "VAL_DT_END")
    private String valDtEnd;
    @GraphQLField(kkhtml = "KFieldText", label = "到息日检索起始日",field = "MTU_DT_START")
    private String mtuDtStart;
    @GraphQLField(kkhtml = "KFieldText", label = "到息日检索结束日",field = "MTU_DT_END")
    private String mtuDtEnd;
    @GraphQLField(kkhtml = "KFieldText", label = "同业机构类型",sql = "SAM_BUS_ORG_TYP= $S{samBusOrgTyp}",field = "SAM_BUS_ORG_TYP")
    private String samBusOrgTyp;

}