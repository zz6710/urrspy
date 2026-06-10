package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;


/**
 * 债券实体
 */

@Data
@GraphQLModel(fetcher = "bondInfoService", table = "ods_ast_bnd_bas_inf")
public class BondInfoModel {


    @GraphQLField(label = "证券编号",sql = "SCR_ID = $S{scrId}",field = "SCR_ID")
    private String scrId;
    @GraphQLField(label = "证券代码",sql = "SCR_CD = $S{scrCd}",field = "SCR_CD")
    private String scrCd;
    @GraphQLField(label = "证券简称",sql = "SCR_SHT_NM = $S{scrShtNm}",field = "SCR_SHT_NM")
    private String scrShtNm;
    @GraphQLField(label = "证券全称",sql = "SCR_NM = $S{scrNm}",field = "SCR_NM")
    private String scrNm;
    @GraphQLField(label = "交易市场",sql = "TRX_MKT = $S{trxMkt}",field = "TRX_MKT")
    private String trxMkt;
    @GraphQLField(label = "交易流通场所",sql = "TRX_PLA = $S{trxPla}",field = "TRX_PLA")
    private String trxPla;
    @GraphQLField(label = "发行价格",sql = "ISU_PRC = $S{isuPrc}",field = "ISU_PRC")
    private String isuPrc;
    @GraphQLField(label = "实际发行总量(元)",sql = "ACTL_ISU_TOT_AMT = $S{actlIsuTotAmt}",field = "ACTL_ISU_TOT_AMT")
    private String actlIsuTotAmt;
    @GraphQLField(label = "发行方式",sql = "ISU_MTH = $S{isuMth}",field = "ISU_MTH")
    private String isuMth;
    @GraphQLField(label = "担保方式",sql = "GRNT_MTH = $S{grntMth}",field = "GRNT_MTH")
    private String grntMth;
    @GraphQLField(label = "是否信用债",sql = "IS_CREDIT = $S{isCredit}",field = "IS_CREDIT")
    private String isCredit;
    @GraphQLField(label = "含权标识",sql = "EMB_OPT_F = $S{embOptF}",field = "EMB_OPT_F")
    private String embOptF;
    @GraphQLField(label = "是否提前还本",sql = "IS_REPAID = $S{isRepaid}",field = "IS_REPAID")
    private String isRepaid;
    @GraphQLField(label = "起息日",sql = "VAL_DT = $S{valDt}",field = "VAL_DT")
    private String valDt;
    @GraphQLField(label = "到期日",sql = "MTU_DT = $S{mtuDt}",field = "MTU_DT")
    private String mtuDt;
    @GraphQLField(label = "计息方式",sql = "INTR_MTH = $S{intrMth}",field = "INTR_MTH")
    private String intrMth;
    @GraphQLField(label = "付息频率",sql = "PAY_INTR_FRQ = $S{payIntrFrq}",field = "PAY_INTR_FRQ")
    private String payIntrFrq;
    @GraphQLField(label = "计息基础",sql = "INTR_BAS = $S{intrBas}",field = "INTR_BAS")
    private String intrBas;
    @GraphQLField(label = "息票类型",sql = "COUPON_TYPE = $S{couponType}",field = "COUPON_TYPE")
    private String couponType;
    @GraphQLField(label = "票面利率",sql = "PAR_RAT = $S{parRat}",field = "PAR_RAT")
    private String parRat;
    @GraphQLField(label = "利差",sql = "SPRD = $S{sprd}",field = "SPRD")
    private String sprd;
    @GraphQLField(label = "发行人",sql = "ISU = $S{isu}",field = "ISU")
    private String isu;
    @GraphQLField(label = "原始权益人",sql = "ORIGN_INTEREST_OBJECT = $S{orignInterestObject}",field = "ORIGN_INTEREST_OBJECT")
    private String orignInterestObject;
    @GraphQLField(label = "中债发行机构所属行业",sql = "CC_INDUSTRY_ISSUER = $S{ccIndustryIssuer}",field = "CC_INDUSTRY_ISSUER")
    private String ccIndustryIssuer;
    @GraphQLField(label = "中债发行机构类型按技术领域划分",sql = "ISU_ORG_TYP_TCHNO = $S{isuOrgTypTchno}",field = "ISU_ORG_TYP_TCHNO")
    private String isuOrgTypTchno;
    @GraphQLField(label = "中债发行机构类型按经济类型划分",sql = "ISU_ORG_TYP_ECN = $S{isuOrgTypEcn}",field = "ISU_ORG_TYP_ECN")
    private String isuOrgTypEcn;
    @GraphQLField(label = "中债发行机构类型按规模划分",sql = "ISU_ORG_TYP_SCALE_SIZ = $S{isuOrgTypScaleSiz}",field = "ISU_ORG_TYP_SCALE_SIZ")
    private String isuOrgTypScaleSiz;
    @GraphQLField(label = "登记托管机构",sql = "REG_TRST_ORG = $S{regTrstOrg}",field = "REG_TRST_ORG")
    private String regTrstOrg;
    @GraphQLField(label = "担保人",sql = "GRNT = $S{grnt}",field = "GRNT")
    private String grnt;
    @GraphQLField(label = "发行时债券评级",sql = "ISU_BND_RAT = $S{isuBndRat}",field = "ISU_BND_RAT")
    private String isuBndRat;
    @GraphQLField(label = "债券当前评级外部",sql = "BOND_FRS_RAT = $S{bondFrsRat}",field = "BOND_FRS_RAT")
    private String bondFrsRat;
    @GraphQLField(label = "主体评级",sql = "MAIN_RAT = $S{mainRat}",field = "MAIN_RAT")
    private String mainRat;
    @GraphQLField(label = "担保人评级外部",sql = "GRNT_RAT = $S{grntRat}",field = "GRNT_RAT")
    private String grntRat;
    @GraphQLField(label = "资讯分类",sql = "ASS_INF_CLASS = $S{assInfClass}",field = "ASS_INF_CLASS")
    private String assInfClass;
    @GraphQLField(label = "债股类别",sql = "DEBT_EQUITY_CLASS = $S{debtEquityClass}",field = "DEBT_EQUITY_CLASS")
    private String debtEquityClass;
    @GraphQLField(label = "是否永续",sql = "IS_SUSTAIN = $S{isSustain}",field = "IS_SUSTAIN")
    private String isSustain;
    @GraphQLField(label = "中债一级分类",sql = "CBND_FRS_CTG = $S{cbndFrsCtg}",field = "CBND_FRS_CTG")
    private String cbndFrsCtg;
    @GraphQLField(label = "中债二级分类",sql = "CBND_SCD_CTG = $S{cbndScdCtg}",field = "CBND_SCD_CTG")
    private String cbndScdCtg;
    @GraphQLField(label = "具体类别",sql = "SPC_TYPE = $S{spcType}",field = "SPC_TYPE")
    private String spcType;
    @GraphQLField(label = "G06二级分类",sql = "GG_CBC_SUB_TYPE = $S{ggCbcSubType}",field = "GG_CBC_SUB_TYPE")
    private String ggCbcSubType;
    @GraphQLField(label = "G06一级分类",sql = "GG_CBC_TYPE = $S{ggCbcType}",field = "GG_CBC_TYPE")
    private String ggCbcType;
    @GraphQLField(label = "人行一级分类",sql = "PBNK_FRS_CTG = $S{pbnkFrsCtg}",field = "PBNK_FRS_CTG")
    private String pbnkFrsCtg;
    @GraphQLField(label = "人行二级分类",sql = "PBNK_SCD_CTG = $S{pbnkScdCtg}",field = "PBNK_SCD_CTG")
    private String pbnkScdCtg;
    @GraphQLField(label = "人行三级分类",sql = "PBNK_TRD_CTG = $S{pbnkTrdCtg}",field = "PBNK_TRD_CTG")
    private String pbnkTrdCtg;
    @GraphQLField(label = "人行四级分类",sql = "PBNK_FUR_CTG = $S{pbnkFurCtg}",field = "PBNK_FUR_CTG")
    private String pbnkFurCtg;
    @GraphQLField(label = "人行发行机构所属行业",sql = "PBNK_INDUSTRY_ISSUER = $S{pbnkIndustryIssuer}",field = "PBNK_INDUSTRY_ISSUER")
    private String pbnkIndustryIssuer;
    @GraphQLField(label = "企业规模",sql = "ISU_ORG_TYP_SIZ = $S{isuOrgTypSiz}",field = "ISU_ORG_TYP_SIZ")
    private String isuOrgTypSiz;
    @GraphQLField(label = "版本号",sql = "VERSION = $S{version}",field = "VERSION")
    private String version;
    @GraphQLField(label = "备注",sql = "CMT = $S{cmt}",field = "CMT")
    private String cmt;
    @GraphQLField(label = "创建日期",sql = "CRT_DATE = $S{crtDate}",field = "CRT_DATE")
    private String crtDate;
    @GraphQLField(label = "创建时间",sql = "CRT_TIME = $S{crtTime}",field = "CRT_TIME")
    private String crtTime;
    @GraphQLField(label = "创建人",sql = "CRT_USER = $S{crtUser}",field = "CRT_USER")
    private String crtUser;
    @GraphQLField(label = "修改日期",sql = "UPD_DATE = $S{updDate}",field = "UPD_DATE")
    private String updDate;
    @GraphQLField(label = "修改时间",sql = "UPD_TIME = $S{updTime}",field = "UPD_TIME")
    private String updTime;
    @GraphQLField(label = "修改人",sql = "UPD_USER = $S{updUser}",field = "UPD_USER")
    private String updUser;
    @GraphQLField(label = "处理日期",sql = "DEAL_DATE = $S{dealDate}",field = "DEAL_DATE")
    private String dealDate;

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
    private List<BondInfoModel> embOptFGridData;
    @GraphQLField(label = "还本信息")
    private List<BondInfoModel> isRepaidGridData;
    @GraphQLField(label = "浮息信息")
    private List<BondInfoModel> couponTypeGridData;

    @GraphQLField(label = "字典英文名称", sql = "DICT = $S{dict}", field = "DICT")
    private String dict;
    @GraphQLField(label = "字典itemval", sql = "ITEMVAL = $S{itemval}", field = "ITEMVAL")
    private String itemval;
    @GraphQLField(label = "字典itemkey", sql = "ITEMKEY = $S{itemkey}", field = "ITEMKEY")
    private String itemkey;

}
