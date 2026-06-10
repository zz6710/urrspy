package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "fundInfoService",table = "ods_ast_fnd_bas_inf")
public class FundInfoModel {


    @GraphQLField(kkhtml="KFieldText", label = "中债一级分类", sql="CBND_FRS_CTG=$S{cbndFrsCtg}",field = "CBND_FRS_CTG")
    private String cbndFrsCtg;
    @GraphQLField(kkhtml="KFieldText", label = "中债二级分类", sql="CBND_SCD_CTG=$S{cbndScdCtg}",field = "CBND_SCD_CTG")
    private String cbndScdCtg;
    @GraphQLField(kkhtml="KFieldText", label = "创建日期", sql="CRT_DATE=$S{crtDate}",field = "CRT_DATE")
    private String crtDate;
    @GraphQLField(kkhtml="KFieldText", label = "创建时间", sql="CRT_TIME=$S{crtTime}",field = "CRT_TIME")
    private String crtTime;
    @GraphQLField(kkhtml="KFieldText", label = "创建人", sql="CRT_USER=$S{crtUser}",field = "CRT_USER")
    private String crtUser;
    @GraphQLField(kkhtml="KFieldText", label = "处理日期", sql="DEAL_DATE=$S{dealDate}",field = "DEAL_DATE")
    private String dealDate;
    @GraphQLField(kkhtml="KFieldText", label = "固定收益类标识", sql="FIX_ERN_F=$S{fixErnF}",field = "FIX_ERN_F")
    private String fixErnF;
    @GraphQLField(kkhtml="KFieldText", label = "基金发行公司", sql="FND_CMP_NM=$S{fndCmpNm}",field = "FND_CMP_NM")
    private String fndCmpNm;
    @GraphQLField(kkhtml="KFieldText", label = "基金管理机构名称", sql="FND_MNG_ORG_NM=$S{fndMngOrgNm}",field = "FND_MNG_ORG_NM")
    private String fndMngOrgNm;
    @GraphQLField(kkhtml="KFieldText", label = "基金托管机构名称", sql="FND_TRST_ORG_NM=$S{fndTrstOrgNm}",field = "FND_TRST_ORG_NM")
    private String fndTrstOrgNm;
    @GraphQLField(kkhtml="KFieldText", label = "发行机构所属行业二级分类", sql="IDT=$S{idt}",field = "IDT")
    private String idt;
    @GraphQLField(kkhtml="KFieldText", label = "投资企业类型按经济类型划分", sql="INV_ENTP_TYP_ECN=$S{invEntpTypEcn}",field = "INV_ENTP_TYP_ECN")
    private String invEntpTypEcn;
    @GraphQLField(kkhtml="KFieldText", label = "投资企业类型按规模划分", sql="INV_ENTP_TYP_SIZ=$S{invEntpTypSiz}",field = "INV_ENTP_TYP_SIZ")
    private String invEntpTypSiz;
    @GraphQLField(kkhtml="KFieldText", label = "投资企业类型按技术领域划分", sql="INV_ENTP_TYP_TCHNO=$S{invEntpTypTchno}",field = "INV_ENTP_TYP_TCHNO")
    private String invEntpTypTchno;
    @GraphQLField(kkhtml="KFieldText", label = "登记备案机构", sql="REG_RCD_ORG=$S{regRcdOrg}",field = "REG_RCD_ORG")
    private String regRcdOrg;
    @GraphQLField(kkhtml="KFieldText", label = "基金代码", sql="SCR_CD=$S{scrCd}",field = "SCR_CD")
    private String scrCd;
    @GraphQLField(kkhtml="KFieldText", label = "证券编号", sql="SCR_ID=$S{scrId}",field = "SCR_ID")
    private String scrId;
    @GraphQLField(kkhtml="KFieldText", label = "基金名称", sql="SCR_NM=$S{scrNm}",field = "SCR_NM")
    private String scrNm;
    @GraphQLField(kkhtml="KFieldText", label = "交易市场", sql="TRX_MKT=$S{trxMkt}",field = "TRX_MKT")
    private String trxMkt;
    @GraphQLField(kkhtml="KFieldText", label = "交易流通场所", sql="TRX_PLA=$S{trxPla}",field = "TRX_PLA")
    private String trxPla;
    @GraphQLField(kkhtml="KFieldText", label = "修改日期", sql="UPD_DATE=$S{updDate}",field = "UPD_DATE")
    private String updDate;
    @GraphQLField(kkhtml="KFieldText", label = "修改时间", sql="UPD_TIME=$S{updTime}",field = "UPD_TIME")
    private String updTime;
    @GraphQLField(kkhtml="KFieldText", label = "修改人", sql="UPD_USER=$S{updUser}",field = "UPD_USER")
    private String updUser;
    @GraphQLField(kkhtml= "KFiledText", label = "资讯分类", sql = "ASS_INF_CLASS=$S{assInfClass}",field = "ASS_INF_CLASS")
    private String assInfClass;
    @GraphQLField(kkhtml= "KFiledText", label = "归属政府投资基金标识", sql = "BLG_GOV_INV_FND_F=$S{blgGovInvFndF}",field = "BLG_GOV_INV_FND_F")
    private String blgGovInvFndF;
    @GraphQLField(kkhtml= "KFiledText", label = "备注", sql = "CMT=$S{cmt}",field = "CMT")
    private String cmt;
    @GraphQLField(kkhtml= "KFiledText", label = "金融资产投资公司发行标识", sql = "FIN_AST_INV_CMP_ISU_F=$S{finAstInvCmpIsuF}",field = "FIN_AST_INV_CMP_ISU_F")
    private String finAstInvCmpIsuF;
    @GraphQLField(kkhtml= "KFiledText", label = "基金投资资产", sql = "FND_INV_AST=$S{fndInvAst}",field = "FND_INV_AST")
    private String fndInvAst;
    @GraphQLField(kkhtml= "KFiledText", label = "发行机构编码", sql = "FND_ORG_ENC=$S{fndOrgEnc}",field = "FND_ORG_ENC")
    private String fndOrgEnc;
    @GraphQLField(kkhtml= "KFiledText", label = "基金登记编码", sql = "FND_PROD_REG_ENC=$S{fndProdRegEnc}",field = "FND_PROD_REG_ENC")
    private String fndProdRegEnc;
    @GraphQLField(kkhtml= "KFiledText", label = "G06二级分类", sql = "GG_CBC_SUB_TYPE=$S{ggCbcSubType}",field = "GG_CBC_SUB_TYPE")
    private String ggCbcSubType;
    @GraphQLField(kkhtml= "KFiledText", label = "G06一级分类", sql = "GG_CBC_TYPE=$S{ggCbcType}",field = "GG_CBC_TYPE")
    private String ggCbcType;
    @GraphQLField(kkhtml= "KFiledText", label = "政府投资基金投向", sql = "GOV_INV_FND_DIR=$S{govInvFndDir}",field = "GOV_INV_FND_DIR")
    private String govInvFndDir;
    @GraphQLField(kkhtml= "KFiledText", label = "投资阶段", sql = "INV_STG=$S{invStg}",field = "INV_STG")
    private String invStg;
    @GraphQLField(kkhtml= "KFiledText", label = "发行份额", sql = "ISU_LOT=$S{isuLot}",field = "ISU_LOT")
    private String isuLot;
    @GraphQLField(kkhtml= "KFiledText", label = "人行一级分类", sql = "PBNK_FRS_CTG=$S{pbnkFrsCtg}",field = "PBNK_FRS_CTG")
    private String pbnkFrsCtg;
    @GraphQLField(kkhtml= "KFiledText", label = "人行四级分类", sql = "PBNK_FUR_CTG=$S{pbnkFurCtg}",field = "PBNK_FUR_CTG")
    private String pbnkFurCtg;
    @GraphQLField(kkhtml= "KFiledText", label = "人行二级分类", sql = "PBNK_SCD_CTG=$S{pbnkScdCtg}",field = "PBNK_SCD_CTG")
    private String pbnkScdCtg;
    @GraphQLField(kkhtml= "KFiledText", label = "人行三级分类", sql = "PBNK_TRD_CTG=$S{pbnkTrdCtg}",field = "PBNK_TRD_CTG")
    private String pbnkTrdCtg;
    @GraphQLField(kkhtml= "KFiledText", label = "运作方式", sql = "RUN_MTH=$S{runMth}",field = "RUN_MTH")
    private String runMth;
    @GraphQLField(kkhtml= "KFiledText", label = "发行成立日", sql = "SET_UP_DT=$S{setUpDt}",field = "SET_UP_DT")
    private String setUpDt;
    @GraphQLField(kkhtml= "KFiledText", label = "版本号", sql = "VERSION=$S{version}",field = "VERSION")
    private String version;
}