package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;


@Data
@GraphQLModel(fetcher = "nonStandInfoModelService",table = "ods_ast_nstd_ast_inf")
public class NonStandInfoModel {

    @GraphQLField(kkhtml = "KFieldText",label = "证券编号",sql = "SCR_ID= $S{scrId}",field = "SCR_ID")
    private String scrId;
    @GraphQLField(kkhtml = "KFieldText",label = "资产代码",sql = "SCR_CD= $S{scrCd}",field = "SCR_CD")
    private String scrCd;
    @GraphQLField(kkhtml = "KFieldText",label = "资产名称",sql = "SCR_NM= $S{scrNm}",field = "SCR_NM")
    private String scrNm;
    @GraphQLField(kkhtml = "KFieldText",label = "资产类型",sql = "ASSET_TYPE= $S{assetType}",field = "ASSET_TYPE")
    private String assetType;
    @GraphQLField(kkhtml = "KFieldText",label = "交易市场",sql = "TRX_MKT= $S{trxMkt}",field = "TRX_MKT")
    private String trxMkt;
    @GraphQLField(kkhtml = "KFieldText",label = "交易流通场所",sql = "TRX_PLA= $S{trxPla}",field = "TRX_PLA")
    private String trxPla;
    @GraphQLField(kkhtml = "KFieldText",label = "金额",sql = "AMT= $S{amt}",field = "AMT")
    private String amt;
    @GraphQLField(kkhtml = "KFieldText",label = "起息日期",sql = "VAL_DT= $S{valDt}",field = "VAL_DT")
    private String valDt;
    @GraphQLField(kkhtml = "KFieldText",label = "到期日期",sql = "MTU_DT= $S{mtuDt}",field = "MTU_DT")
    private String mtuDt;
    @GraphQLField(kkhtml = "KFieldText",label = "预期收益率标识",sql = "EXPE_RAT_F= $S{expeRatF}",field = "EXPE_RAT_F")
    private String expeRatF;
    @GraphQLField(kkhtml = "KFieldText",label = "项目收益率",sql = "YLD= $S{yld}",field = "YLD")
    private String yld;
    @GraphQLField(kkhtml = "KFieldText",label = "计息类型",sql = "INTR_TYP= $S{intrTyp}",field = "INTR_TYP")
    private String intrTyp;
    @GraphQLField(kkhtml = "KFieldText",label = "规则付息标识",sql = "RUL_PAY_INTR_F= $S{rulPayIntrF}",field = "RUL_PAY_INTR_F")
    private String rulPayIntrF;
    @GraphQLField(kkhtml = "KFieldText",label = "付息频率",sql = "PAY_INTR_FRQ= $S{payIntrFrq}",field = "PAY_INTR_FRQ")
    private String payIntrFrq;
    @GraphQLField(kkhtml = "KFieldText",label = "付息频率",sql = "PAYINTEREST_FREQ= $S{payinterestFreq}",field = "PAYINTEREST_FREQ")
    private String payinterestFreq;
    @GraphQLField(kkhtml = "KFieldText",label = "还本付息情况说明",sql = "PAY_PRCP_INTR_STS_CMT= $S{payPrcpIntrStsCmt}",field = "PAY_PRCP_INTR_STS_CMT")
    private String payPrcpIntrStsCmt;
    @GraphQLField(kkhtml = "KFieldText",label = "计息基础",sql = "INTR_BAS= $S{intrBas}",field = "INTR_BAS")
    private String intrBas;
    @GraphQLField(kkhtml = "KFieldText",label = "基准利率种类",sql = "BCHM_RAT_TYP= $S{bchmRatTyp}",field = "BCHM_RAT_TYP")
    private String bchmRatTyp;
    @GraphQLField(kkhtml = "KFieldText",label = "利差",sql = "SPRD= $S{sprd}",field = "SPRD")
    private String sprd;
    @GraphQLField(kkhtml = "KFieldText",label = "分期还本标识",sql = "INS_PAY_PRCP_F= $S{insPayPrcpF}",field = "INS_PAY_PRCP_F")
    private String insPayPrcpF;
    @GraphQLField(kkhtml = "KFieldText",label = "融资人",sql = "LVRG= $S{lvrg}",field = "LVRG")
    private String lvrg;
    @GraphQLField(kkhtml = "KFieldText",label = "融资人内部信用评级",sql = "LVRG_IN_CRD_RAT= $S{lvrgInCrdRat}",field = "LVRG_IN_CRD_RAT")
    private String lvrgInCrdRat;
    @GraphQLField(kkhtml = "KFieldText",label = "外部评级机构及融资人评级",sql = "OUT_RAT_ORG_AND_LVRG_RAT= $S{outRatOrgAndLvrgRat}",field = "OUT_RAT_ORG_AND_LVRG_RAT")
    private String outRatOrgAndLvrgRat;
    @GraphQLField(kkhtml = "KFieldText",label = "融资人类型按规模划分",sql = "LVRG_TYP_SIZ= $S{lvrgTypSiz}",field = "LVRG_TYP_SIZ")
    private String lvrgTypSiz;
    @GraphQLField(kkhtml = "KFieldText",label = "融资人类型按技术领域划分",sql = "LVRG_TYP_TCHNO= $S{lvrgTypTchno}",field = "LVRG_TYP_TCHNO")
    private String lvrgTypTchno;
    @GraphQLField(kkhtml = "KFieldText",label = "融资人类型按经济类型划分",sql = "LVRG_TYP_ECN= $S{lvrgTypEcn}",field = "LVRG_TYP_ECN")
    private String lvrgTypEcn;
    @GraphQLField(kkhtml = "KFieldText",label = "融资人归属行业",sql = "LVRG_BLG_IDT= $S{lvrgBlgIdt}",field = "LVRG_BLG_IDT")
    private String lvrgBlgIdt;
    @GraphQLField(kkhtml = "KFieldText",label = "担保方式",sql = "GRNT_MTH= $S{grntMth}",field = "GRNT_MTH")
    private String grntMth;
    @GraphQLField(kkhtml = "KFieldText",label = "抵质押物类型",sql = "PLG_TYP= $S{plgTyp}",field = "PLG_TYP")
    private String plgTyp;
    @GraphQLField(kkhtml = "KFieldText",label = "抵质押物价值",sql = "PLG_VAL= $S{plgVal}",field = "PLG_VAL")
    private String plgVal;
    @GraphQLField(kkhtml = "KFieldText",label = "担保性质",sql = "GRNT_CHR= $S{grntChr}",field = "GRNT_CHR")
    private String grntChr;
    @GraphQLField(kkhtml = "KFieldText",label = "担保人与融资人关系",sql = "GRNT_LVRG_REL= $S{grntLvrgRel}",field = "GRNT_LVRG_REL")
    private String grntLvrgRel;
    @GraphQLField(kkhtml = "KFieldText",label = "融资人主体评级",sql = "GRNT_MAIN_RAT= $S{grntMainRat}",field = "GRNT_MAIN_RAT")
    private String grntMainRat;
    @GraphQLField(kkhtml = "KFieldText",label = "含权类型",sql = "EMB_OPT_TYP= $S{embOptTyp}",field = "EMB_OPT_TYP")
    private String embOptTyp;
    @GraphQLField(kkhtml = "KFieldText",label = "行权方式",sql = "XCS_RIT_MTH= $S{xcsRitMth}",field = "XCS_RIT_MTH")
    private String xcsRitMth;
    @GraphQLField(kkhtml = "KFieldText",label = "固定行权日",sql = "FIX_XCS_RIT_DT= $S{fixXcsRitDt}",field = "FIX_XCS_RIT_DT")
    private String fixXcsRitDt;
    @GraphQLField(kkhtml = "KFieldText",label = "首次付息日",sql = "FRS_PAY_INTR_DT= $S{frsPayIntrDt}",field = "FRS_PAY_INTR_DT")
    private String frsPayIntrDt;
    @GraphQLField(kkhtml = "KFieldText",label = "行权价格",sql = "XCS_RIT_PRC= $S{xcsRitPrc}",field = "XCS_RIT_PRC")
    private String xcsRitPrc;
    @GraphQLField(kkhtml = "KFieldText",label = "融资人归属地区",sql = "LVRG_BLG_ZON= $S{lvrgBlgZon}",field = "LVRG_BLG_ZON")
    private String lvrgBlgZon;
    @GraphQLField(kkhtml = "KFieldText",label = "融资人组织机构代码",sql = "LVRG_ORG_ORG_CD= $S{lvrgOrgOrgCd}",field = "LVRG_ORG_ORG_CD")
    private String lvrgOrgOrgCd;
    @GraphQLField(kkhtml = "KFieldText",label = "增信机构代码",sql = "INC_CRD_ORG_CD= $S{incCrdOrgCd}",field = "INC_CRD_ORG_CD")
    private String incCrdOrgCd;
    @GraphQLField(kkhtml = "KFieldText",label = "增信机构名称",sql = "INC_CRD_ORG_NM= $S{incCrdOrgNm}",field = "INC_CRD_ORG_NM")
    private String incCrdOrgNm;
    @GraphQLField(kkhtml = "KFieldText",label = "币种",sql = "CCY= $S{ccy}",field = "CCY")
    private String ccy;
    @GraphQLField(kkhtml = "KFieldText",label = "创建日期",sql = "CRT_DATE= $S{crtDate}",field = "CRT_DATE")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText",label = "创建时间",sql = "CRT_TIME= $S{crtTime}",field = "CRT_TIME")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText",label = "创建人",sql = "CRT_USER= $S{crtUser}",field = "CRT_USER")
    private String crtUser;
    @GraphQLField(kkhtml = "KFieldText",label = "修改日期",sql = "UPD_DATE= $S{updDate}",field = "UPD_DATE")
    private String updDate;
    @GraphQLField(kkhtml = "KFieldText",label = "修改时间",sql = "UPD_TIME= $S{updTime}",field = "UPD_TIME")
    private String updTime;
    @GraphQLField(kkhtml = "KFieldText",label = "修改人",sql = "UPD_USER= $S{updUser}",field = "UPD_USER")
    private String updUser;
    @GraphQLField(kkhtml = "KFieldText",label = "处理日期",sql = "DEAL_DATE= $S{dealDate}",field = "DEAL_DATE")
    private String dealDate;
    @GraphQLField(kkhtml = "KFieldText", label = "收受益权类型", sql = "INC_BEN_RIT_TYP=$S{incBenRitTyp}" ,field = "INC_BEN_RIT_TYP")
    private String incBenRitTyp;
    @GraphQLField(kkhtml = "KFieldText", label = "买入返售标识", sql = "BUY_BACK_F=$S{buyBackF}" ,field = "BUY_BACK_F")
    private String buyBackF;
    @GraphQLField(kkhtml = "KFieldText", label = "份额面值", sql = "LOT_PAR_VAL=$S{lotParVal}" ,field = "LOT_PAR_VAL")
    private String lotParVal;
    @GraphQLField(kkhtml = "KFieldText", label = "法定到期日", sql = "STA_MTU_DT=$S{staMtuDt}" ,field = "STA_MTU_DT")
    private String staMtuDt;
    @GraphQLField(kkhtml = "KFieldText", label = "利息分布方式", sql = "INTR_ALC_MTH=$S{intrAlcMth}" ,field = "INTR_ALC_MTH")
    private String intrAlcMth;
    @GraphQLField(kkhtml = "KFieldText", label = "浮动因子标识", sql = "FLT_FCT_F=$S{fltFctF}" ,field = "FLT_FCT_F")
    private String fltFctF;
    @GraphQLField(kkhtml = "KFieldText", label = "浮动因子", sql = "FLT_FCT=$S{fltFct}" ,field = "FLT_FCT")
    private String fltFct;
    @GraphQLField(kkhtml = "KFieldText", label = "结构档次", sql = "STRC_GRD=$S{strcGrd}" ,field = "STRC_GRD")
    private String strcGrd;
    @GraphQLField(kkhtml = "KFieldText", label = "还本方式", sql = "PAY_PRCP_MTH=$S{payPrcpMth}" ,field = "PAY_PRCP_MTH")
    private String payPrcpMth;
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产类型", sql = "BAS_AST_TYP=$S{basAstTyp}" ,field = "BAS_AST_TYP")
    private String basAstTyp;
    @GraphQLField(kkhtml = "KFieldText", label = "超额收益分配比例", sql = "EXS_ERN_ALC_RTO=$S{exsErnAlcRto}" ,field = "EXS_ERN_ALC_RTO")
    private String exsErnAlcRto;
    @GraphQLField(kkhtml = "KFieldText", label = "融资项目", sql = "LVRG_PRJ=$S{lvrgPrj}" ,field = "LVRG_PRJ")
    private String lvrgPrj;
    @GraphQLField(kkhtml = "KFieldText", label = "项目归属重点监控行业和领域标识", sql = "PRJ_BLG_KEY_MNT_IDT=$S{prjBlgKeyMntIdt}" ,field = "PRJ_BLG_KEY_MNT_IDT")
    private String prjBlgKeyMntIdt;
    @GraphQLField(kkhtml = "KFieldText", label = "重点监控行业和领域类别", sql = "KEY_MNT_IDT_TYP=$S{keyMntIdtTyp}" ,field = "KEY_MNT_IDT_TYP")
    private String keyMntIdtTyp;
    @GraphQLField(kkhtml = "KFieldText", label = "重点监控行业和领域类别说明", sql = "KEY_MNT_IDT_TYP_CMT=$S{keyMntIdtTypCmt}" ,field = "KEY_MNT_IDT_TYP_CMT")
    private String keyMntIdtTypCmt;
    @GraphQLField(kkhtml = "KFieldText", label = "担保情况说明", sql = "GRNT_STS_CMT=$S{grntStsCmt}" ,field = "GRNT_STS_CMT")
    private String grntStsCmt;
    @GraphQLField(kkhtml = "KFieldText", label = "资产内部评级", sql = "AST_IN_RAT=$S{astInRat}" ,field = "AST_IN_RAT")
    private String astInRat;
    @GraphQLField(kkhtml = "KFieldText", label = "资产外部评级", sql = "AST_OUT_RAT=$S{astOutRat}" ,field = "AST_OUT_RAT")
    private String astOutRat;
    @GraphQLField(kkhtml = "KFieldText", label = "行权周期", sql = "XCS_RIT_PRD=$S{xcsRitPrd}" ,field = "XCS_RIT_PRD")
    private String xcsRitPrd;
    @GraphQLField(kkhtml = "KFieldText", label = "永续条款类型", sql = "PERP_TYP=$S{perpTyp}" ,field = "PERP_TYP")
    private String perpTyp;
    @GraphQLField(kkhtml = "KFieldText", label = "利息递延条款类型", sql = "INTR_PPN_TYP=$S{intrPpnTyp}" ,field = "INTR_PPN_TYP")
    private String intrPpnTyp;
    @GraphQLField(kkhtml = "KFieldText", label = "递延利息计息标识", sql = "PPN_INTR_INTR_F=$S{ppnIntrIntrF}" ,field = "PPN_INTR_INTR_F")
    private String ppnIntrIntrF;
    @GraphQLField(kkhtml = "KFieldText", label = "首次重定价日期", sql = "FRS_RPRC_DT=$S{frsRprcDt}" ,field = "FRS_RPRC_DT")
    private String frsRprcDt;
    @GraphQLField(kkhtml = "KFieldText", label = "重定价周期", sql = "RPRC_PRD=$S{rprcPrd}" ,field = "RPRC_PRD")
    private String rprcPrd;
    @GraphQLField(kkhtml = "KFieldText", label = "部分赎回标识", sql = "PART_RDM_F=$S{partRdmF}" ,field = "PART_RDM_F")
    private String partRdmF;
    @GraphQLField(kkhtml = "KFieldText", label = "部分赎回比例", sql = "PART_RDM_RTO=$S{partRdmRto}" ,field = "PART_RDM_RTO")
    private String partRdmRto;
    @GraphQLField(kkhtml = "KFieldText", label = "选择权", sql = "CHC_RIT=$S{chcRit}" ,field = "CHC_RIT")
    private String chcRit;
    @GraphQLField(kkhtml = "KFieldText", label = "行权条件说明", sql = "XCS_RIT_COND_CMT=$S{xcsRitCondCmt}" ,field = "XCS_RIT_COND_CMT")
    private String xcsRitCondCmt;
    @GraphQLField(kkhtml = "KFieldText", label = "融资总费率", sql = "LVRG_TOT_FEE=$S{lvrgTotFee}" ,field = "LVRG_TOT_FEE")
    private String lvrgTotFee;
    @GraphQLField(kkhtml = "KFieldText", label = "融资项目归属行业", sql = "LVRG_PRJ_BLG_IDT=$S{lvrgPrjBlgIdt}" ,field = "LVRG_PRJ_BLG_IDT")
    private String lvrgPrjBlgIdt;
    @GraphQLField(kkhtml = "KFieldText", label = "通道代码", sql = "CHANNEL_NO=$S{channelNo}" ,field = "CHANNEL_NO")
    private String channelNo;
    @GraphQLField(kkhtml = "KFieldText", label = "通道名称", sql = "CHANNEL_NAME=$S{channelName}" ,field = "CHANNEL_NAME")
    private String channelName;
    @GraphQLField(kkhtml = "KFieldText", label = "是否通道投资", sql = "IS_CHANNEL=$S{isChannel}" ,field = "IS_CHANNEL")
    private String isChannel;
    @GraphQLField(kkhtml = "KFieldText", label = "付息计划", sql = "PAY_PLAN=$S{payPlan}" ,field = "PAY_PLAN")
    private String payPlan;
    @GraphQLField(kkhtml = "KFieldText", label = "还本计划", sql = "REPAY_PLAN=$S{repayPlan}" ,field = "REPAY_PLAN")
    private String repayPlan;
    @GraphQLField(kkhtml = "KFieldText", label = "投向", sql = "MM_ACTUAL_DIRECT=$S{mmActualDirect}" ,field = "MM_ACTUAL_DIRECT")
    private String mmActualDirect;
    @GraphQLField(kkhtml = "KFieldText", label = "首次行权日期", sql = "FRS_FIX_XCS_DT=$S{frsFixXcsDt}" ,field = "FRS_FIX_XCS_DT")
    private String frsFixXcsDt;
    @GraphQLField(kkhtml = "KFieldText", label = "中债一级分类", sql = "CBND_FRS_CTG=$S{cbndFrsCtg}" ,field = "CBND_FRS_CTG")
    private String cbndFrsCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "中债二级分类", sql = "CBND_SCD_CTG=$S{cbndScdCtg}" ,field = "CBND_SCD_CTG")
    private String cbndScdCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "中债发行机构所属行业", sql = "CC_INDUSTRY_ISSUER=$S{ccIndustryIssuer}" ,field = "CC_INDUSTRY_ISSUER")
    private String ccIndustryIssuer;
    @GraphQLField(kkhtml = "KFieldText", label = "中债发行机构类型按规模划分", sql = "ISU_ORG_TYP_SCALE_SIZ=$S{isuOrgTypScaleSiz}" ,field = "ISU_ORG_TYP_SCALE_SIZ")
    private String isuOrgTypScaleSiz;
    @GraphQLField(kkhtml = "KFieldText", label = "G06三级分类", sql = "GG_CBC_TRD_TYPE=$S{ggCbcTrdType}" ,field = "GG_CBC_TRD_TYPE")
    private String ggCbcTrdType;
    @GraphQLField(kkhtml = "KFieldText", label = "G06二级分类", sql = "GG_CBC_SUB_TYPE=$S{ggCbcSubType}" ,field = "GG_CBC_SUB_TYPE")
    private String ggCbcSubType;
    @GraphQLField(kkhtml = "KFieldText", label = "G06一级分类", sql = "GG_CBC_TYPE=$S{ggCbcType}" ,field = "GG_CBC_TYPE")
    private String ggCbcType;
    @GraphQLField(kkhtml = "KFieldText", label = "人行一级分类", sql = "PBNK_FRS_CTG=$S{pbnkFrsCtg}" ,field = "PBNK_FRS_CTG")
    private String pbnkFrsCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "人行二级分类", sql = "PBNK_SCD_CTG=$S{pbnkScdCtg}" ,field = "PBNK_SCD_CTG")
    private String pbnkScdCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "人行三级分类", sql = "PBNK_TRD_CTG=$S{pbnkTrdCtg}" ,field = "PBNK_TRD_CTG")
    private String pbnkTrdCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "人行四级分类", sql = "PBNK_FUR_CTG=$S{pbnkFurCtg}" ,field = "PBNK_FUR_CTG")
    private String pbnkFurCtg;
    @GraphQLField(kkhtml = "KFieldText", label = "人行发行机构所属行业", sql = "PBNK_INDUSTRY_ISSUER=$S{pbnkIndustryIssuer}" ,field = "PBNK_INDUSTRY_ISSUER")
    private String pbnkIndustryIssuer;
    @GraphQLField(kkhtml = "KFieldText", label = "人行发行机构企业规模", sql = "ISU_ORG_TYP_SIZ=$S{isuOrgTypSiz}" ,field = "ISU_ORG_TYP_SIZ")
    private String isuOrgTypSiz;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "CMT=$S{cmt}" ,field = "CMT")
    private String cmt;
    @GraphQLField(kkhtml = "KFieldText", label = "版本号", sql = "VERSION=$S{version}" ,field = "VERSION")
    private String version;

    @GraphQLField(kkhtml = "KFieldText", label = "起息检索起始日",field = "val_dt_start")
    private String valDtStart;
    @GraphQLField(kkhtml = "KFieldText", label = "起息检索结束日",field = "val_dt_end")
    private String valDtEnd;
    @GraphQLField(kkhtml = "KFieldText", label = "到息检索起始日",field = "mtu_dt_start")
    private String mtuDtStart;
    @GraphQLField(kkhtml = "KFieldText", label = "到息检索结束日",field = "mtu_dt_end")
    private String mtuDtEnd;


}