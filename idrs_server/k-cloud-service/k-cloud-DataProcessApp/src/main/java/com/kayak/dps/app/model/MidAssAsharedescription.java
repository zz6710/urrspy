package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "midAssAsharedescriptionService",table = "mid_ass_asharedescription")
public class MidAssAsharedescription {


   @GraphQLField(kkhtml = "KFieldText", label = "证券编码", sql = "SCR_ID= $S{scrId}" ,field = "SCR_ID")
   private String scrId;
   @GraphQLField(kkhtml = "KFieldText", label = "股票代码", sql = "SCR_CD= $S{scrCd}" ,field = "SCR_CD")
   private String scrCd;
   @GraphQLField(kkhtml = "KFieldText", label = "股票名称", sql = "SCR_NM= $S{scrNm}" ,field = "SCR_NM")
   private String scrNm;
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "CCY= $S{ccy}" ,field = "CCY")
   private String ccy;
   @GraphQLField(kkhtml = "KFieldText", label = "市场", sql = "TRX_MKT= $S{trxMkt}" ,field = "TRX_MKT")
   private String trxMkt;
   @GraphQLField(kkhtml = "KFieldText", label = "交易流通场所", sql = "TRX_PLA= $S{trxPla}" ,field = "TRX_PLA")
   private String trxPla;
   @GraphQLField(kkhtml = "KFieldText", label = "公司名称", sql = "COMPANY_NAME= $S{companyName}" ,field = "COMPANY_NAME")
   private String companyName;
   @GraphQLField(kkhtml = "KFieldText", label = "板块类型", sql = "PLATE_TYPE= $S{plateType}" ,field = "PLATE_TYPE")
   private String plateType;
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

   @GraphQLField(kkhtml = "KFieldText", label = "投资阶段", sql = "INVESTMENT_TYPE= $S{investmentType}" ,field = "INVESTMENT_TYPE")
   private String investmentType;
   @GraphQLField(kkhtml = "KFieldText", label = "股权退出安排", sql = "SHAREHOLD= $S{sharehold}" ,field = "SHAREHOLD")
   private String sharehold;
   @GraphQLField(kkhtml = "KFieldText", label = "是否为质押融资", sql = "PLEDGED_FINACE= $S{pledgedFinace}" ,field = "PLEDGED_FINACE")
   private String pledgedFinace;
   @GraphQLField(kkhtml = "KFieldText", label = "是否为债转股", sql = "DEBT_EQUITY_SWAP= $S{debtEquitySwap}" ,field = "DEBT_EQUITY_SWAP")
   private String debtEquitySwap;
   @GraphQLField(kkhtml = "KFieldText", label = "股票类型", sql = "STOCK_TYPE= $S{stockType}" ,field = "STOCK_TYPE")
   private String stockType;
   @GraphQLField(kkhtml = "KFieldText", label = "机构所属行业中债", sql = "INDUSTRY_ISSUER= $S{industryIssuer}" ,field = "INDUSTRY_ISSUER")
   private String industryIssuer;
   @GraphQLField(kkhtml = "KFieldText", label = "机构类型按规模划分", sql = "ISU_ORG_TYP_SIZ= $S{isuOrgTypSiz}" ,field = "ISU_ORG_TYP_SIZ")
   private String isuOrgTypSiz;
   @GraphQLField(kkhtml = "KFieldText", label = "机构类型按技术领域划分", sql = "ISU_ORG_TYP_TCHNO= $S{isuOrgTypTchno}" ,field = "ISU_ORG_TYP_TCHNO")
   private String isuOrgTypTchno;
   @GraphQLField(kkhtml = "KFieldText", label = "机构类型按经济类型划分", sql = "ISU_ORG_TYP_ECN= $S{isuOrgTypEcn}" ,field = "ISU_ORG_TYP_ECN")
   private String isuOrgTypEcn;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "CMT= $S{cmt}" ,field = "CMT")
   private String cmt;
   @GraphQLField(kkhtml = "KFieldText", label = "外部资讯分类", sql = "ASS_INF_CLASS= $S{assInfClass}" ,field = "ASS_INF_CLASS")
   private String assInfClass;
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
   @GraphQLField(kkhtml = "KFieldText", label = "版本号", sql = "VERSION= $S{version}" ,field = "VERSION")
   private String version;

}