package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;

@Data
@GraphQLModel(fetcher = "midDirectFusionService",table = "ods_direct_fusion")
public class MidDirectFusion {


    @GraphQLField(kkhtml = "KFieldText", label = "证券编码", sql = "SCR_ID= $S{scrId}" ,field = "SCR_ID")
    private String scrId;
    @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "SCR_CD= $S{scrCd}" ,field = "SCR_CD")
    private String scrCd;
    @GraphQLField(kkhtml = "KFieldText", label = "资产名称", sql = "SCR_NM= $S{scrNm}" ,field = "SCR_NM")
    private String scrNm;
    @GraphQLField(kkhtml = "KFieldText", label = "资产全称", sql = "SCR_FULL_NM= $S{scrFullNm}" ,field = "SCR_FULL_NM")
    private String scrFullNm;
    @GraphQLField(kkhtml = "KFieldText", label = "交易流通场所", sql = "TRX_PLA= $S{trxPla}" ,field = "TRX_PLA")
    private String trxPla;
    @GraphQLField(kkhtml = "KFieldText", label = "市场", sql = "TRX_MKT= $S{trxMkt}" ,field = "TRX_MKT")
    private String trxMkt;
    @GraphQLField(kkhtml = "KFieldText", label = "具体类别", sql = "SPC_TYPE= $S{spcType}" ,field = "SPC_TYPE")
    private String spcType;
    @GraphQLField(kkhtml = "KFieldText", label = "发行方式", sql = "ISS_MOD= $S{issMod}" ,field = "ISS_MOD")
    private String issMod;
    @GraphQLField(kkhtml = "KFieldText", label = "主体外部评级", sql = "SUB_LEVEL= $S{subLevel}" ,field = "SUB_LEVEL")
    private String subLevel;
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构", sql = "ISSUER= $S{issuer}" ,field = "ISSUER")
    private String issuer;
    @GraphQLField(kkhtml = "KFieldText", label = "发行人所属行业中债", sql = "PUBLISHER_TRADE= $S{publisherTrade}" ,field = "PUBLISHER_TRADE")
    private String publisherTrade;
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构类型按技术领域划分", sql = "ISU_ORG_TYP_TCHNO= $S{isuOrgTypTchno}" ,field = "ISU_ORG_TYP_TCHNO")
    private String isuOrgTypTchno;
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构类型按经济类型划分", sql = "ISU_ORG_TYP_ECN= $S{isuOrgTypEcn}" ,field = "ISU_ORG_TYP_ECN")
    private String isuOrgTypEcn;
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构类型按规模划分", sql = "ISU_ORG_TYP_SCALE_SIZ= $S{isuOrgTypScaleSiz}" ,field = "ISU_ORG_TYP_SCALE_SIZ")
    private String isuOrgTypScaleSiz;
    @GraphQLField(kkhtml = "KFieldText", label = "登记托管机构", sql = "REG_TRST_ORG= $S{regTrstOrg}" ,field = "REG_TRST_ORG")
    private String regTrstOrg;
    @GraphQLField(kkhtml = "KFieldText", label = "登记托管机构说明", sql = "REG_TRST_ORG_CMT= $S{regTrstOrgCmt}" ,field = "REG_TRST_ORG_CMT")
    private String regTrstOrgCmt;
    @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "DEAL_DATE= $S{dealDate}" ,field = "DEAL_DATE")
    private String dealDate;
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

    @GraphQLField(kkhtml = "KFieldText", label = "发行价格", sql = "ISSUE_PRICE= $S{issuePrice}" ,field = "ISSUE_PRICE")
    private String issuePrice;
    @GraphQLField(kkhtml = "KFieldText", label = "发行量", sql = "ISSUE_VOLUME= $S{issueVolume}" ,field = "ISSUE_VOLUME")
    private String issueVolume;
    @GraphQLField(kkhtml = "KFieldText", label = "担保方式", sql = "GUAR_TYPE= $S{guarType}" ,field = "GUAR_TYPE")
    private String guarType;
    @GraphQLField(kkhtml = "KFieldText", label = "是否含权", sql = "IS_EXERCISE= $S{isExercise}" ,field = "IS_EXERCISE")
    private String isExercise;
    @GraphQLField(kkhtml = "KFieldText", label = "是否提前还本", sql = "IS_REPAID= $S{isRepaid}" ,field = "IS_REPAID")
    private String isRepaid;
    @GraphQLField(kkhtml = "KFieldText", label = "起息日", sql = "BEGIN_DATE= $S{beginDate}" ,field = "BEGIN_DATE")
    private String beginDate;
    @GraphQLField(kkhtml = "KFieldText", label = "到期日", sql = "END_DATE= $S{endDate}" ,field = "END_DATE")
    private String endDate;
    @GraphQLField(kkhtml = "KFieldText", label = "付息频率", sql = "PAY_FREQ= $S{payFreq}" ,field = "PAY_FREQ")
    private String payFreq;
    @GraphQLField(kkhtml = "KFieldText", label = "计息基础", sql = "INTEREST_BASE= $S{interestBase}" ,field = "INTEREST_BASE")
    private String interestBase;
    @GraphQLField(kkhtml = "KFieldText", label = "计息方式", sql = "INTEREST_MODE= $S{interestMode}" ,field = "INTEREST_MODE")
    private String interestMode;
    @GraphQLField(kkhtml = "KFieldText", label = "息票品种", sql = "INTEREST_TYPE= $S{interestType}" ,field = "INTEREST_TYPE")
    private String interestType;
    @GraphQLField(kkhtml = "KFieldText", label = "票面利率", sql = "COUPON_RATE= $S{couponRate}" ,field = "COUPON_RATE")
    private String couponRate;
    @GraphQLField(kkhtml = "KFieldText", label = "利差", sql = "BOND_SPREAD= $S{bondSpread}" ,field = "BOND_SPREAD")
    private String bondSpread;
    @GraphQLField(kkhtml = "KFieldText", label = "担保人", sql = "GUARANTEER= $S{guaranteer}" ,field = "GUARANTEER")
    private String guaranteer;
    @GraphQLField(kkhtml = "KFieldText", label = "发行时债券评级", sql = "ISU_BND_RAT= $S{isuBndRat}" ,field = "ISU_BND_RAT")
    private String isuBndRat;
    @GraphQLField(kkhtml = "KFieldText", label = "债券评级", sql = "BOND_CREDIT= $S{bondCredit}" ,field = "BOND_CREDIT")
    private String bondCredit;
    @GraphQLField(kkhtml = "KFieldText", label = "担保人评级", sql = "GRNT_RAT= $S{grntRat}" ,field = "GRNT_RAT")
    private String grntRat;
    @GraphQLField(kkhtml = "KFieldText", label = "中债一级分类", sql = "CBND_FRS_CTG= $S{cbndFrsCtg}" ,field = "CBND_FRS_CTG")
    private String cbndFrsCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "中债二级分类", sql = "CBND_SCD_CTG= $S{cbndScdCtg}" ,field = "CBND_SCD_CTG")
    private String cbndScdCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "G06二级分类", sql = "GG_CBC_SUB_TYPE= $S{ggCbcSubType}" ,field = "GG_CBC_SUB_TYPE")
    private String ggCbcSubType;
    @GraphQLField(kkhtml = "KFieldText", label = "G06一级分类", sql = "GG_CBC_TYPE= $S{ggCbcType}" ,field = "GG_CBC_TYPE")
    private String ggCbcType;
    @GraphQLField(kkhtml = "KFieldText", label = "发行人人行一级分类", sql = "PBNK_FRS_CTG= $S{pbnkFrsCtg}" ,field = "PBNK_FRS_CTG")
    private String pbnkFrsCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "发行人人行二级分类", sql = "PBNK_SCD_CTG= $S{pbnkScdCtg}" ,field = "PBNK_SCD_CTG")
    private String pbnkScdCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "发行人人行三级分类", sql = "PBNK_TRD_CTG= $S{pbnkTrdCtg}" ,field = "PBNK_TRD_CTG")
    private String pbnkTrdCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "人行发行机构所属行业", sql = "PUBLISHER_TRADE_PB= $S{publisherTradePb}" ,field = "PUBLISHER_TRADE_PB")
    private String publisherTradePb;
    @GraphQLField(kkhtml = "KFieldText", label = "人行发行机构企业规模", sql = "PUBLISHER_SCALE_PB= $S{publisherScalePb}" ,field = "PUBLISHER_SCALE_PB")
    private String publisherScalePb;
    @GraphQLField(kkhtml = "KFieldText", label = "版本号", sql = "VERSION= $S{version}" ,field = "VERSION")
    private String version;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "CMT= $S{cmt}" ,field = "CMT")
    private String cmt;
    @GraphQLField(kkhtml = "KFieldText", label = "起息日检索起始日",field = "VAL_DT_START")
    private String valDtStart;
    @GraphQLField(kkhtml = "KFieldText", label = "起息日检索结束日",field = "VAL_DT_END")
    private String valDtEnd;
    @GraphQLField(kkhtml = "KFieldText", label = "到息日检索起始日",field = "MTU_DT_START")
    private String mtuDtStart;
    @GraphQLField(kkhtml = "KFieldText", label = "到息日检索结束日",field = "MTU_DT_END")
    private String mtuDtEnd;


    /*浮息利率*/
    @GraphQLField(label= "起息日",sql = "FL_BEGIN_DATE = $S{flBeginDate}" ,field = "FL_BEGIN_DATE")
    private String flBeginDate;
    @GraphQLField(label= "基础利率",sql = "BASE_RATE = $S{baseRate}" ,field = "BASE_RATE")
    private String baseRate;
    @GraphQLField(label= "结束日",sql = "FL_END_DATE = $S{flEndDate}" ,field = "FL_END_DATE")
    private String flEndDate;

    /*还本信息*/
    @GraphQLField(label = "提前还本日期",sql = "REPAY_DATE = $S{repayDate}", field = "REPAY_DATE")
    private String repayDate;
    @GraphQLField(label = "单位还本本金",sql = "UNIT_PRINCIPAL = $S{unitPrincipal}", field = "UNIT_PRINCIPAL")
    private String unitPrincipal;

    /*行权信息*/
    @GraphQLField(label = "行权日期",sql = "EXERCISE_DATE =$S{exerciseDate}",field = "EXERCISE_DATE")
    private String exerciseDate;
    @GraphQLField(label = "票面补偿",sql = "EX_COUPON_RATE =$S{exCouponRate}",field = "EX_COUPON_RATE")
    private String exCouponRate;



    @GraphQLField(label = "行权信息")
    private List<MidDirectFusion> embOptFGridData;
    @GraphQLField(label = "还本信息")
    private List<MidDirectFusion> isRepaidGridData;
    @GraphQLField(label = "浮息信息")
    private List<MidDirectFusion> couponTypeGridData;

}