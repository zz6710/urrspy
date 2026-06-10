package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "fundPrivateInfoService",table = "ods_amng_fund_ntpinfo")
public class FundPrivateInfoModel {
    @GraphQLField(kkhtml="KFieldText", label = "证券编号", sql="SCR_ID=$S{scrId}",field = "SCR_ID")
    private String scrId;
    @GraphQLField(kkhtml="KFieldText", label = "基金代码", sql="SCR_CD=$S{scrCd}",field = "SCR_CD")
    private String scrCd;
    @GraphQLField(kkhtml="KFieldText", label = "基金名称", sql="SCR_NM=$S{scrNm}",field = "SCR_NM")
    private String scrNm;
    @GraphQLField(kkhtml="KFieldText", label = "金融咨询系统资产代码", sql="FIS_CD=$S{fisCd}",field = "FIS_CD")
    private String fisCd;
    @GraphQLField(kkhtml="KFieldText", label = "是否由金融资产投资公司发行", sql="IS_ISSUE_FAIC=$S{isIssueFaic}",field = "IS_ISSUE_FAIC")
    private String isIssueFaic;
    @GraphQLField(kkhtml="KFieldText", label = "投资行业", sql="INVESTMENT_INDUSTRY=$S{investmentIndustry}",field = "INVESTMENT_INDUSTRY")
    private String investmentIndustry;
    @GraphQLField(kkhtml="KFieldText", label = "登记备案机构", sql="FND_RFA=$S{fndRfa}",field = "FND_RFA")
    private String fndRfa;
    @GraphQLField(kkhtml="KFieldText", label = "是否为固定收益类", sql="IS_FIC=$S{isFic}",field = "IS_FIC")
    private String isFic;
    @GraphQLField(kkhtml= "KFiledText", label = "是否属于政府投资基金", sql = "IS_GIFND=$S{isGifnd}",field = "IS_GIFND")
    private String isGifnd;
    @GraphQLField(kkhtml= "KFiledText", label = "政府投资基金投向", sql = "FND_IDG=$S{fndIdg}",field = "FND_IDG")
    private String fndIdg;
    @GraphQLField(kkhtml= "KFiledText", label = "基金公司名称", sql = "FND_MI=$S{fndMi}",field = "FND_MI")
    private String fndMi;
    @GraphQLField(kkhtml= "KFiledText", label = "基金管理机构名称", sql = "FND_MI_NAME=$S{fndMiName}",field = "FND_MI_NAME")
    private String fndMiName;
    @GraphQLField(kkhtml= "KFiledText", label = "基金托管机构名称", sql = "FND_CI_NAME=$S{fndCiName}",field = "FND_CI_NAME")
    private String fndCiName;
    @GraphQLField(kkhtml= "KFiledText", label = "投资企业类型（按规模划分）", sql = "LVRG_TYP_SIZ=$S{lvrgTypSiz}",field = "LVRG_TYP_SIZ")
    private String lvrgTypSiz;
    @GraphQLField(kkhtml= "KFiledText", label = "投资企业类型（按技术领域划分）", sql = "LVRG_TYP_TCHNO=$S{lvrgTypTchno}",field = "LVRG_TYP_TCHNO")
    private String lvrgTypTchno;
    @GraphQLField(kkhtml= "KFiledText", label = "投资企业类型（按经济类型划分）", sql = "LVRG_TYP_ECN=$S{lvrgTypEcn}",field = "LVRG_TYP_ECN")
    private String lvrgTypEcn;
    @GraphQLField(kkhtml= "KFiledText", label = "投资阶段", sql = "FND_IS=$S{fndIs}",field = "fndIs")
    private String fndIs;
    @GraphQLField(kkhtml= "KFiledText", label = "是否投向金融资产投资公司或其附属机构发行的私募股权投资基金", sql = "IS_INV_PEIF=$S{isInvPeif}",field = "IS_INV_PEIF")
    private String isInvPeif;
    @GraphQLField(kkhtml= "KFiledText", label = "资产分类", sql = "FND_TYPE=$S{fndType}",field = "FND_TYPE")
    private String fndType;
    @GraphQLField(kkhtml="KFieldText", label = "中债一级分类", sql="CBND_FRS_CTG=$S{cbndFrsCtg}",field = "CBND_FRS_CTG")
    private String cbndFrsCtg;
    @GraphQLField(kkhtml="KFieldText", label = "中债二级分类", sql="CBND_SCD_CTG=$S{cbndScdCtg}",field = "CBND_SCD_CTG")
    private String cbndScdCtg;
    @GraphQLField(kkhtml= "KFiledText", label = "人行一级分类", sql = "PBNK_FRS_CTG=$S{pbnkFrsCtg}",field = "PBNK_FRS_CTG")
    private String pbnkFrsCtg;
    @GraphQLField(kkhtml= "KFiledText", label = "人行二级分类", sql = "PBNK_SCD_CTG=$S{pbnkScdCtg}",field = "PBNK_SCD_CTG")
    private String pbnkScdCtg;
    @GraphQLField(kkhtml= "KFiledText", label = "人行三级分类", sql = "PBNK_TRD_CTG=$S{pbnkTrdCtg}",field = "PBNK_TRD_CTG")
    private String pbnkTrdCtg;
    @GraphQLField(kkhtml= "KFiledText", label = "人行四级分类", sql = "PBNK_FOU_CTG=$S{pbnkFouCtg}",field = "PBNK_FOU_CTG")
    private String pbnkFouCtg;
    @GraphQLField(kkhtml= "KFiledText", label = "G06一级分类", sql = "G06_FRS_CTG=$S{g06FrsCtg}",field = "G06_FRS_CTG")
    private String g06FrsCtg;
    @GraphQLField(kkhtml= "KFiledText", label = "G06二级分类", sql = "G06_SCD_CTG=$S{g06ScdCtg}",field = "G06_SCD_CTG")
    private String g06ScdCtg;
    @GraphQLField(kkhtml= "KFiledText", label = "G06三级分类", sql = "G06_TRD_CTG=$S{g06TrdCtg}",field = "G06_TRD_CTG")
    private String g06TrdCtg;
    @GraphQLField(kkhtml= "KFiledText", label = "基金投资资产", sql = "INV_ASSET=$S{invAsset}",field = "INV_ASSET")
    private String invAsset;
    @GraphQLField(kkhtml= "KFiledText", label = "备注", sql = "CMT=$S{cmt}",field = "CMT")
    private String cmt;
    @GraphQLField(kkhtml="KFieldText", label = "创建日期", sql="CRT_DATE=$S{crtDate}",field = "CRT_DATE")
    private String crtDate;
    @GraphQLField(kkhtml="KFieldText", label = "创建时间", sql="CRT_TIME=$S{crtTime}",field = "CRT_TIME")
    private String crtTime;
    @GraphQLField(kkhtml="KFieldText", label = "创建人", sql="CRT_USER=$S{crtUser}",field = "CRT_USER")
    private String crtUser;
    @GraphQLField(kkhtml="KFieldText", label = "处理日期", sql="DEAL_DATE=$S{dealDate}",field = "DEAL_DATE")
    private String dealDate;
    @GraphQLField(kkhtml="KFieldText", label = "修改日期", sql="UPD_DATE=$S{updDate}",field = "UPD_DATE")
    private String updDate;
    @GraphQLField(kkhtml="KFieldText", label = "修改时间", sql="UPD_TIME=$S{updTime}",field = "UPD_TIME")
    private String updTime;
    @GraphQLField(kkhtml="KFieldText", label = "修改人", sql="UPD_USER=$S{updUser}",field = "UPD_USER")
    private String updUser;
    @GraphQLField(label = "版本号",sql = "VERSION = $S{version}",field = "VERSION")
    private String version;
    private String orgNbrExt;
    private String orgFullName2;
    private String orgFullName3;
    private String orgFullName4;
}