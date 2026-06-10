package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "midTrmDpsInfService",table = "ods_trm_dps_inf")
public class MidTrmDpsInf {

   @GraphQLField(kkhtml = "KFieldText", label = "证券编码", sql = "SCR_ID= $S{scrId}" ,field = "SCR_ID")
   private String scrId;
   @GraphQLField(kkhtml = "KFieldText", label = "资产分类", sql = "ASSET_DEBT_TYPE= $S{assetDebtType}" ,field = "ASSET_DEBT_TYPE")
   private String assetDebtType;
   @GraphQLField(kkhtml = "KFieldText", label = "存款代码", sql = "SCR_CD= $S{scrCd}" ,field = "SCR_CD")
   private String scrCd;
   @GraphQLField(kkhtml = "KFieldText", label = "存款名称", sql = "SCR_NM= $S{scrNm}" ,field = "SCR_NM")
   private String scrNm;
   @GraphQLField(kkhtml = "KFieldText", label = "存款银行", sql = "DPS_BNK= $S{dpsBnk}" ,field = "DPS_BNK")
   private String dpsBnk;
   @GraphQLField(kkhtml = "KFieldText", label = "存款账号", sql = "DPS_ACT_NBR= $S{dpsActNbr}" ,field = "DPS_ACT_NBR")
   private String dpsActNbr;
   @GraphQLField(kkhtml = "KFieldText", label = "存款金额", sql = "DPS_AMT= $S{dpsAmt}" ,field = "DPS_AMT")
   private String dpsAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "起息日期", sql = "VAL_DT= $S{valDt}" ,field = "VAL_DT")
   private String valDt;
   @GraphQLField(kkhtml = "KFieldText", label = "到期日期", sql = "MTU_DT= $S{mtuDt}" ,field = "MTU_DT")
   private String mtuDt;
   @GraphQLField(kkhtml = "KFieldText", label = "存款年利率", sql = "ANL_YLD= $S{anlYld}" ,field = "ANL_YLD")
   private String anlYld;
   @GraphQLField(kkhtml = "KFieldText", label = "计息基础", sql = "INTR_BAS= $S{intrBas}" ,field = "INTR_BAS")
   private String intrBas;
   @GraphQLField(kkhtml = "KFieldText", label = "存款类型", sql = "DPS_TYP= $S{dpsTyp}" ,field = "DPS_TYP")
   private String dpsTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "市场或交易流通场所", sql = "TRX_MKT= $S{trxMkt}" ,field = "TRX_MKT")
   private String trxMkt;
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "CCY= $S{ccy}" ,field = "CCY")
   private String ccy;
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

   @GraphQLField(kkhtml = "KFieldText", label = "挂钩标的类别", sql = "LNK_SBJ_MAT_TYP= $S{lnkSbjMatTyp}" ,field = "LNK_SBJ_MAT_TYP")
   private String lnkSbjMatTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "挂钩标的", sql = "LNK_SBJ_MAT= $S{lnkSbjMat}" ,field = "LNK_SBJ_MAT")
   private String lnkSbjMat;
   @GraphQLField(kkhtml = "KFieldText", label = "付息频率", sql = "PAYINTEREST_FREQ= $S{payinterestFreq}" ,field = "PAYINTEREST_FREQ")
   private String payinterestFreq;
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
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "DEAL_DATE= $S{dealDate}" ,field = "DEAL_DATE")
   private String dealDate;
   @GraphQLField(kkhtml = "KFieldText", label = "起息日检索起始日",field = "VAL_DT_START")
   private String valDtStart;
   @GraphQLField(kkhtml = "KFieldText", label = "起息日检索结束日",field = "VAL_DT_END")
   private String valDtEnd;
   @GraphQLField(kkhtml = "KFieldText", label = "到息日检索起始日",field = "MTU_DT_START")
   private String mtuDtStart;
   @GraphQLField(kkhtml = "KFieldText", label = "到息日检索结束日",field = "MTU_DT_END")
   private String mtuDtEnd;
}