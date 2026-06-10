package com.kayak.dps.ods.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.NetValSPVInfoModel;
import com.kayak.dps.app.model.NonStandInfoModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;

@Repository
public class NonStandInfoModelDao extends ComnDao {

	public SqlResult<NonStandInfoModel> findNonStandInfoModels(SqlParam<NonStandInfoModel> params) throws Exception {
		String sql = "select t1.SCR_ID, t1.SCR_CD,t1.PAYINTEREST_FREQ, t1.SCR_NM, t1.ASSET_TYPE, t1.CBND_SCD_CTG,t1.TRX_MKT, t1.TRX_PLA, t1.AMT, " +
				"t1.VAL_DT, t1.MTU_DT, t1.EXPE_RAT_F, t1.YLD, t1.INTR_TYP, t1.RUL_PAY_INTR_F, t1.PAY_INTR_FRQ, " +
				"t1.PAY_PRCP_INTR_STS_CMT, t1.INTR_BAS, t1.BCHM_RAT_TYP, t1.SPRD, t1.INS_PAY_PRCP_F, t1.LVRG, " +
				"t1.LVRG_IN_CRD_RAT, t1.OUT_RAT_ORG_AND_LVRG_RAT, t1.LVRG_TYP_SIZ, t1.LVRG_TYP_TCHNO, t1.LVRG_TYP_ECN, " +
				"t1.LVRG_BLG_IDT, t1.GRNT_MTH, t1.PLG_TYP, t1.PLG_VAL, t1.GRNT_CHR, t1.GRNT_LVRG_REL, t1.GRNT_MAIN_RAT, " +
				"t1.EMB_OPT_TYP, t1.XCS_RIT_MTH, t1.FIX_XCS_RIT_DT, t1.FRS_PAY_INTR_DT, t1.XCS_RIT_PRC, t1.LVRG_BLG_ZON," +
				" t1.LVRG_ORG_ORG_CD, t1.INC_CRD_ORG_CD, t1.INC_CRD_ORG_NM, t1.CCY, t1.CRT_DATE, t1.CRT_TIME, t1.CRT_USER," +
				" t1.UPD_DATE, t1.UPD_TIME, t1.UPD_USER, t1.DEAL_DATE, t1.INC_BEN_RIT_TYP, t1.BUY_BACK_F, t1.LOT_PAR_VAL," +
				" t1.STA_MTU_DT, t1.INTR_ALC_MTH, t1.FLT_FCT_F, t1.FLT_FCT, t1.STRC_GRD, t1.PAY_PRCP_MTH, t1.BAS_AST_TYP," +
				" t1.EXS_ERN_ALC_RTO, t1.LVRG_PRJ, t1.PRJ_BLG_KEY_MNT_IDT, t1.KEY_MNT_IDT_TYP, t1.KEY_MNT_IDT_TYP_CMT, " +
				"t1.GRNT_STS_CMT, t1.AST_IN_RAT, t1.AST_OUT_RAT, t1.XCS_RIT_PRD, t1.PERP_TYP, t1.INTR_PPN_TYP," +
				" t1.PPN_INTR_INTR_F, t1.FRS_RPRC_DT, t1.RPRC_PRD, t1.PART_RDM_F, t1.PART_RDM_RTO, t1.CHC_RIT, " +
				"t1.XCS_RIT_COND_CMT, t1.LVRG_TOT_FEE, t1.LVRG_PRJ_BLG_IDT, t1.CHANNEL_NO, t1.CHANNEL_NAME, " +
				"t1.IS_CHANNEL, t1.PAY_PLAN, t1.REPAY_PLAN, t1.MM_ACTUAL_DIRECT, t1.FRS_FIX_XCS_DT, t1.CBND_FRS_CTG," +
				"  t1.CC_INDUSTRY_ISSUER, t1.ISU_ORG_TYP_SCALE_SIZ, t1.GG_CBC_TRD_TYPE, t1.GG_CBC_SUB_TYPE, " +
				"t1.GG_CBC_TYPE, t1.PBNK_FRS_CTG, t1.PBNK_SCD_CTG, t1.PBNK_TRD_CTG, t1.PBNK_FUR_CTG, t1.PBNK_INDUSTRY_ISSUER," +
				" t1.ISU_ORG_TYP_SIZ, t1.CMT, t1.VERSION  from ods_nstd_bas_inf  t1  where 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getScrCd())){
			sql += " and t1.SCR_CD = $S{scrCd} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getValDtStart())){
			sql += " and t1.val_dt >= $S{valDtStart} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getValDtEnd())){
			sql += " and t1.val_dt <= $S{valDtEnd} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getMtuDtStart())){
			sql += " and t1.mtu_dt >= $S{mtuDtStart} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getMtuDtEnd())){
			sql += " and t1.mtu_dt <= $S{mtuDtEnd} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getPbnkScdCtg())){
			sql += " and t1.pbnk_scd_ctg = $S{pbnkScdCtg} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getPbnkTrdCtg())){
			sql += " and t1.pbnk_trd_ctg = $S{pbnkTrdCtg} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getCbndScdCtg())){
			sql += " and t1.cbnd_scd_ctg = $S{cbndScdCtg} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getVersion())){
			sql += " and t1.version = $S{version} ";
		}
		return super.findRows(sql, DataSourceProperty.PUB,params);
	}

	public UpdateResult addNonStandInfoModel(SqlParam<NonStandInfoModel> params) throws Exception {
		return super.update("insert into ods_nstd_bas_inf (SCR_ID,SCR_CD,SCR_NM," +
						"CBND_FRS_CTG,CBND_SCD_CTG,PAYINTEREST_FREQ,ASSET_TYPE,TRX_MKT,TRX_PLA,AMT," +
						"VAL_DT,MTU_DT,EXPE_RAT_F,YLD,INTR_TYP,RUL_PAY_INTR_F,PAY_INTR_FRQ,PAY_PRCP_INTR_STS_CMT," +
						"INTR_BAS,BCHM_RAT_TYP,SPRD,INS_PAY_PRCP_F,LVRG,LVRG_IN_CRD_RAT,OUT_RAT_ORG_AND_LVRG_RAT," +
						"LVRG_TYP_SIZ,LVRG_TYP_TCHNO,LVRG_TYP_ECN,LVRG_BLG_IDT,GRNT_MTH,PLG_TYP,PLG_VAL,GRNT_CHR," +
						"GRNT_LVRG_REL,GRNT_MAIN_RAT,EMB_OPT_TYP,XCS_RIT_MTH,FIX_XCS_RIT_DT,FRS_PAY_INTR_DT," +
						"INC_BEN_RIT_TYP, " +
						"BUY_BACK_F, " +
						"LOT_PAR_VAL, " +
						"STA_MTU_DT, " +
						"INTR_ALC_MTH, " +
						"FLT_FCT_F, " +
						"FLT_FCT, " +
						"STRC_GRD, " +
						"PAY_PRCP_MTH, " +
						"BAS_AST_TYP, " +
						"EXS_ERN_ALC_RTO, " +
						"LVRG_PRJ, " +
						"PRJ_BLG_KEY_MNT_IDT, " +
						"KEY_MNT_IDT_TYP, " +
						"KEY_MNT_IDT_TYP_CMT, " +
						"GRNT_STS_CMT, " +
						"AST_IN_RAT, " +
						"AST_OUT_RAT, " +
						"XCS_RIT_PRD, " +
						"PERP_TYP, " +
						"INTR_PPN_TYP, " +
						"PPN_INTR_INTR_F, " +
						"FRS_RPRC_DT, " +
						"RPRC_PRD, " +
						"PART_RDM_F, " +
						"PART_RDM_RTO, " +
						"CHC_RIT, " +
						"XCS_RIT_COND_CMT, " +
						"LVRG_TOT_FEE, " +
						"LVRG_PRJ_BLG_IDT, " +
						"CHANNEL_NO, " +
						"CHANNEL_NAME, " +
						"IS_CHANNEL, " +
						"PAY_PLAN, " +
						"REPAY_PLAN, " +
						"MM_ACTUAL_DIRECT, " +
						"FRS_FIX_XCS_DT, " +
						"CC_INDUSTRY_ISSUER, " +
						"ISU_ORG_TYP_SCALE_SIZ, " +
						"GG_CBC_TRD_TYPE, " +
						"GG_CBC_SUB_TYPE, " +
						"GG_CBC_TYPE, " +
						"PBNK_FRS_CTG, " +
						"PBNK_SCD_CTG, " +
						"PBNK_TRD_CTG, " +
						"PBNK_FUR_CTG, " +
						"PBNK_INDUSTRY_ISSUER, " +
						"ISU_ORG_TYP_SIZ, " +
						"CMT, VERSION," +
						"XCS_RIT_PRC,LVRG_BLG_ZON,LVRG_ORG_ORG_CD,INC_CRD_ORG_CD,INC_CRD_ORG_NM,CCY,CRT_DATE," +
						"CRT_TIME,CRT_USER,DEAL_DATE)values ($S{scrId},$S{scrCd}," +
						"$S{scrNm},$S{cbndFrsCtg},$S{cbndScdCtg},$S{payinterestFreq},$S{assetType},$S{trxMkt}," +
						"$S{trxPla},nullif($S{amt},''),$S{valDt},$S{mtuDt},$S{expeRatF}," +
						"nullif($S{yld},''),$S{intrTyp},$S{rulPayIntrF},$S{payIntrFrq},$S{payPrcpIntrStsCmt},$S{intrBas}," +
						"$S{bchmRatTyp},nullif($S{sprd},''),$S{insPayPrcpF},$S{lvrg},$S{lvrgInCrdRat},$S{outRatOrgAndLvrgRat}," +
						"$S{lvrgTypSiz},$S{lvrgTypTchno},$S{lvrgTypEcn},$S{lvrgBlgIdt},$S{grntMth},$S{plgTyp}," +
						"nullif($S{plgVal},''),$S{grntChr},$S{grntLvrgRel},$S{grntMainRat},$S{embOptTyp},$S{xcsRitMth}," +
						"$S{fixXcsRitDt},$S{frsPayIntrDt}," +
						"$S{incBenRitTyp}, " +
						"$S{buyBackF}, " +
						"nullif($S{lotParVal},''), " +
						"$S{staMtuDt}, " +
						"$S{intrAlcMth}, " +
						"$S{fltFctF}, " +
						"nullif($S{fltFct},''), " +
						"$S{strcGrd}, " +
						"$S{payPrcpMth}, " +
						"$S{basAstTyp}, " +
						"nullif($S{exsErnAlcRto},''), " +
						"$S{lvrgPrj}, " +
						"$S{prjBlgKeyMntIdt}, " +
						"$S{keyMntIdtTyp}, " +
						"$S{keyMntIdtTypCmt}, " +
						"$S{grntStsCmt}, " +
						"$S{astInRat}, " +
						"$S{astOutRat}, " +
						"nullif($S{xcsRitPrd},''), " +
						"$S{perpTyp}, " +
						"$S{intrPpnTyp}, " +
						"$S{ppnIntrIntrF}, " +
						"$S{frsRprcDt}, " +
						"nullif($S{rprcPrd},''), " +
						"$S{partRdmF}, " +
						"nullif($S{partRdmRto},''), " +
						"$S{chcRit}, " +
						"$S{xcsRitCondCmt}, " +
						"nullif($S{lvrgTotFee},''), " +
						"$S{lvrgPrjBlgIdt}, " +
						"$S{channelNo}, " +
						"$S{channelName}, " +
						"$S{isChannel}, " +
						"$S{payPlan}, " +
						"$S{repayPlan}, " +
						"$S{mmActualDirect}, " +
						"$S{frsFixXcsDt}, " +
						"$S{ccIndustryIssuer}, " +
						"$S{isuOrgTypScaleSiz}, " +
						"$S{ggCbcTrdType}, " +
						"$S{ggCbcSubType}, " +
						"$S{ggCbcType}, " +
						"$S{pbnkFrsCtg}, " +
						"$S{pbnkScdCtg}, " +
						"$S{pbnkTrdCtg}, " +
						"$S{pbnkFurCtg}, " +
						"$S{pbnkIndustryIssuer}, " +
						"$S{isuOrgTypSiz}, " +
						"$S{cmt}, $S{version}," +
						"nullif($S{xcsRitPrc},''),$S{lvrgBlgZon},$S{lvrgOrgOrgCd},$S{incCrdOrgCd}," +
						"$S{incCrdOrgNm},$S{ccy},$S{crtDate},$S{crtTime},$S{crtUser},$S{dealDate})",
				DataSourceProperty.PUB, params.getModel());
	}
	
	public UpdateResult updateNonStandInfoModel(SqlParam<NonStandInfoModel> params) throws Exception {
		return super.update("update ods_nstd_bas_inf set " +
						"SCR_NM = $S{scrNm},ASSET_TYPE = $S{assetType},CBND_SCD_CTG = $S{cbndScdCtg}," +
						"AMT = nullif($S{amt},''),EXPE_RAT_F = $S{expeRatF}," +
						"INTR_TYP = $S{intrTyp},RUL_PAY_INTR_F = $S{rulPayIntrF},PAY_INTR_FRQ = $S{payIntrFrq}," +
						"PAY_PRCP_INTR_STS_CMT = $S{payPrcpIntrStsCmt}," +
						"CBND_FRS_CTG = $S{cbndFrsCtg}," +
						"BCHM_RAT_TYP = $S{bchmRatTyp},PBNK_FUR_CTG = $S{pbnkFurCtg}," +
						"GG_CBC_TRD_TYPE = $S{ggCbcTrdType},GG_CBC_SUB_TYPE = $S{ggCbcSubType},GG_CBC_TYPE = $S{ggCbcType}," +
						"PBNK_FRS_CTG = $S{pbnkFrsCtg},PBNK_SCD_CTG = $S{pbnkScdCtg},PBNK_TRD_CTG = $S{pbnkTrdCtg}," +
						"UPD_DATE = $S{updDate},UPD_TIME = $S{updTime},UPD_USER = $S{updUser} where SCR_ID = $S{scrId}",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult updateNonStandInfoModelBl(SqlParam<NonStandInfoModel> params) throws Exception {
		return super.update("update ods_nstd_bas_inf set INC_BEN_RIT_TYP = $S{incBenRitTyp},TRX_MKT = $S{trxMkt}," +
						"TRX_PLA = $S{trxPla},LVRG = $S{lvrg},PAYINTEREST_FREQ=$S{payinterestFreq}," +
						"BUY_BACK_F = $S{buyBackF},VAL_DT = $S{valDt},MTU_DT = $S{mtuDt},CCY = $S{ccy}," +
						"YLD = nullif($S{yld},''),INTR_BAS = $S{intrBas},SPRD = nullif($S{sprd},''),INS_PAY_PRCP_F = $S{insPayPrcpF}," +
						"LOT_PAR_VAL = nullif($S{lotParVal},''),STA_MTU_DT = $S{staMtuDt},INTR_ALC_MTH = $S{intrAlcMth}," +
						"FLT_FCT_F = $S{fltFctF},FLT_FCT = nullif($S{fltFct},''),STRC_GRD = $S{strcGrd},PAY_PRCP_MTH = $S{payPrcpMth}," +
						"BAS_AST_TYP = $S{basAstTyp},EXS_ERN_ALC_RTO = nullif($S{exsErnAlcRto},''),LVRG_PRJ = $S{lvrgPrj}," +
						"PRJ_BLG_KEY_MNT_IDT = $S{prjBlgKeyMntIdt},KEY_MNT_IDT_TYP = $S{keyMntIdtTyp}," +
						"LVRG_IN_CRD_RAT = $S{lvrgInCrdRat},OUT_RAT_ORG_AND_LVRG_RAT = $S{outRatOrgAndLvrgRat}," +
						"LVRG_TYP_SIZ = $S{lvrgTypSiz},LVRG_TYP_TCHNO = $S{lvrgTypTchno},LVRG_TYP_ECN = $S{lvrgTypEcn}," +
						"LVRG_BLG_IDT = $S{lvrgBlgIdt},GRNT_MTH = $S{grntMth},PLG_TYP = $S{plgTyp},PLG_VAL = nullif($S{plgVal},'')," +
						"GRNT_CHR = $S{grntChr},GRNT_LVRG_REL = $S{grntLvrgRel},GRNT_MAIN_RAT = $S{grntMainRat}," +
						"EMB_OPT_TYP = $S{embOptTyp},XCS_RIT_MTH = $S{xcsRitMth},FIX_XCS_RIT_DT = $S{fixXcsRitDt}," +
						"FRS_PAY_INTR_DT = $S{frsPayIntrDt},XCS_RIT_PRC = nullif($S{xcsRitPrc},''),LVRG_BLG_ZON = $S{lvrgBlgZon}," +
						"LVRG_ORG_ORG_CD = $S{lvrgOrgOrgCd},INC_CRD_ORG_CD = $S{incCrdOrgCd},INC_CRD_ORG_NM = $S{incCrdOrgNm}," +
						"KEY_MNT_IDT_TYP_CMT = $S{keyMntIdtTypCmt},GRNT_STS_CMT = $S{grntStsCmt},AST_IN_RAT = $S{astInRat}," +
						"AST_OUT_RAT = $S{astOutRat},XCS_RIT_PRD = nullif($S{xcsRitPrd},''),PERP_TYP = $S{perpTyp}," +
						"INTR_PPN_TYP = $S{intrPpnTyp},PPN_INTR_INTR_F = $S{ppnIntrIntrF},FRS_RPRC_DT = $S{frsRprcDt}," +
						"RPRC_PRD = nullif($S{rprcPrd},''),PART_RDM_F = $S{partRdmF},PART_RDM_RTO = nullif($S{partRdmRto},'')," +
						"CHC_RIT = $S{chcRit},XCS_RIT_COND_CMT = $S{xcsRitCondCmt},LVRG_TOT_FEE = nullif($S{lvrgTotFee},'')," +
						"LVRG_PRJ_BLG_IDT = $S{lvrgPrjBlgIdt},CHANNEL_NO = $S{channelNo},CHANNEL_NAME = $S{channelName}," +
						"IS_CHANNEL = $S{isChannel},PAY_PLAN = $S{payPlan},REPAY_PLAN = $S{repayPlan}," +
						"MM_ACTUAL_DIRECT = $S{mmActualDirect},FRS_FIX_XCS_DT = $S{frsFixXcsDt}," +
						"CC_INDUSTRY_ISSUER = $S{ccIndustryIssuer},ISU_ORG_TYP_SCALE_SIZ = $S{isuOrgTypScaleSiz}," +
						"PBNK_INDUSTRY_ISSUER = $S{pbnkIndustryIssuer}," +
						"ISU_ORG_TYP_SIZ = $S{isuOrgTypSiz},UPD_DATE = $S{updDate},UPD_TIME = $S{updTime}," +
						"UPD_USER = $S{updUser},CMT = $S{cmt},VERSION = $S{version} where SCR_ID = $S{scrId}",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult deleteNonStandInfoModel(SqlParam<NonStandInfoModel> params) throws Exception {
		return super.update("DELETE FROM ods_nstd_bas_inf WHERE SCR_ID = $S{scrId}", DataSourceProperty.PUB,
				params.getModel());
	}



	public List<SqlRow> getXPTypeByDocType(String doc_type) throws Exception {
		return super.findRows("SELECT itemkey VALUE,  " +
				"itemval TEXT  " +
				"FROM sys_dict_item  " +
				"WHERE dict = 'cbndScdCtg' " +
				"AND itemkey LIKE '$U{doc_type}%' " +
				"ORDER BY itemkey+0",DataSourceProperty.PUB,doc_type);
	}

	public  List<SqlRow>  getUPDTypeByDocType(Object params) throws Exception {
		List<SqlRow> s = super.findRows("SELECT itemkey VALUE,  " +
				"itemval TEXT  " +
				"FROM sys_dict_item  " +
				"WHERE dict = 'cbndScdCtg' " +
				"AND itemorder = $S{cbndFrsCtg} " +
			/**	"AND itemkey != '1201' " +
				"AND itemkey != '1210' " +
				"AND itemkey != '2101' " +
				"AND itemkey != '1299' " +*/
				"ORDER BY itemkey+0",DataSourceProperty.PUB,params);
		return s;
	}

    public SqlRow existNonStandInfo(SqlParam<NonStandInfoModel> params) throws Exception {
		return super.findRow("select count(1) con from ods_ast_nstd_ast_inf where SCR_CD = $S{scrCd} ",
				DataSourceProperty.PUB, params);
    }

	public SqlResult<NonStandInfoModel> findNonStandInfoIdAndNm(SqlParam<NonStandInfoModel> params) throws Exception {
		return super.findRows("SELECT DISTINCT SCR_CD,SCR_NM FROM ods_nstd_bas_inf where SCR_CD like '%$U{scrCd}%' or SCR_NM like '%$U{scrCd}%'  ", DataSourceProperty.PUB, params);
	}

	public  List<SqlRow>  getUPDTypeByDoc(Object params) throws Exception {
		List<SqlRow> s = super.findRows("SELECT itemkey VALUE,  "  +
				"itemval TEXT  "  +
				"FROM sys_dict_item  "  +
				"WHERE dict = 'cbndScdCtg' " +
				"AND itemorder LIKE '$U{cbndFrsCtg}%' " +
				"ORDER BY itemkey+0",DataSourceProperty.PUB,params);
		return s;
	}

	public  List<SqlRow>  getPbnkScdByPbnkFrs(Object params) throws Exception {
		List<SqlRow> s = super.findRows("select itemkey VALUE,itemval TEXT   FROM sys_dict_item   WHERE dict = 'pbnkScdCtg'  and substr(itemkey,1,1) = $S{pbnkFrsCtg}   ORDER BY itemkey+0 ",DataSourceProperty.PUB,params);
		return s;
	}

	public  List<SqlRow>  getPbnkTrdByPbnkScd(Object params) throws Exception {
		List<SqlRow> s = super.findRows("select itemkey VALUE,itemval TEXT   FROM sys_dict_item   WHERE dict = 'pbnkTrdCtg'  and substr(itemkey,1,2) = $S{pbnkScdCtg}  ORDER BY itemkey+0 ",DataSourceProperty.PUB,params);
		return s;
	}

	public  List<SqlRow>  getInvestTypeDict(Object params) throws Exception {
		List<SqlRow> s = super.findRows("select * from base_ex_map where dict='tr_invest_type' and OUT_VALUE = $S{cbndScdCtg} ",DataSourceProperty.PUB,params);
		return s;
	}
}
