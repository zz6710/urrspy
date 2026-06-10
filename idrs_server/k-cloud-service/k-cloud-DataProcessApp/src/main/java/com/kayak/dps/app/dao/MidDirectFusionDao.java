package com.kayak.dps.app.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;

@Repository
public class MidDirectFusionDao extends ComnDao {

	public SqlResult<MidDirectFusion> findCity(SqlParam<MidDirectFusion> params) throws Exception {
		return super.findRows("select itemval scr_nm ,itemkey scr_cd from sys_dict_item where dict = 'pbc_city_area_det' and itemkey like '$U{scrCd}%'", params);
	}

	public SqlResult<MidDirectFusion> findScrCd(SqlParam<MidDirectFusion> params) throws Exception {
		return super.findRows("SELECT DISTINCT scr_cd,scr_nm FROM ods_direct_bas_inf where SCR_CD like '%$U{scrCd}%' or SCR_NM like '%$U{scrCd}%'", params);
	}

	public String getFusionSql (){
		return "select t1.SCR_ID,t1.SCR_CD,t1.SCR_NM,t1.SCR_FULL_NM,t1.TRX_PLA,t1.TRX_MKT,t1.SPC_TYPE,t1.ISS_MOD,t1.SUB_LEVEL,t1.ISSUER,t1.PUBLISHER_TRADE,t1.ISU_ORG_TYP_TCHNO,t1.ISU_ORG_TYP_ECN,t1.ISU_ORG_TYP_SCALE_SIZ,t1.REG_TRST_ORG,t1.REG_TRST_ORG_CMT,t1.DEAL_DATE,t1.CRT_DATE,t1.CRT_TIME,t1.CRT_USER,t1.UPD_DATE,t1.UPD_TIME,t1.UPD_USER,t1.ISSUE_PRICE,t1.ISSUE_VOLUME,t1.GUAR_TYPE,t1.IS_EXERCISE,t1.IS_REPAID,t1.BEGIN_DATE,t1.END_DATE,t1.PAY_FREQ,t1.INTEREST_BASE,t1.INTEREST_MODE,t1.INTEREST_TYPE,t1.COUPON_RATE,t1.BOND_SPREAD,t1.GUARANTEER,t1.ISU_BND_RAT,t1.BOND_CREDIT,t1.GRNT_RAT,t1.CBND_FRS_CTG,t1.CBND_SCD_CTG,t1.GG_CBC_SUB_TYPE,t1.GG_CBC_TYPE,t1.PBNK_FRS_CTG,t1.PBNK_SCD_CTG,t1.PBNK_TRD_CTG,t1.PUBLISHER_TRADE_PB,t1.PUBLISHER_SCALE_PB,t1.VERSION,t1.CMT from ods_direct_bas_inf t1 where 1=1 ";
	}
	public int findAssetCount(SqlParam<MidDirectFusion> params) throws Exception {
		String sql="SELECT count(1) cnt " +
				"   FROM ods_direct_bas_inf t1 "  +
				"	WHERE t1.SCR_CD ='"+params.getParams().get("scrCd")+"'"  ;
		SqlRow s= super.findRow(sql, null);
		return s.getInteger("cnt");
	}

	public SqlResult<MidDirectFusion> findMidDirectFusions(SqlParam<MidDirectFusion> params) throws Exception {
		String sql = getFusionSql();

		MidDirectFusion m = params.getModel();

		if (StringUtils.isNotBlank(m.getScrCd())){
			sql += "and t1.scr_cd = $S{scrCd} ";
		}

		if (StringUtils.isNotBlank(m.getValDtStart())){
			sql += " and t1.begin_date >= $S{valDtStart} ";
		}
		if (StringUtils.isNotBlank(m.getValDtEnd())){
			sql += " and t1.begin_date <= $S{valDtEnd} ";
		}
		if (StringUtils.isNotBlank(m.getMtuDtStart())){
			sql += " and t1.end_date >= $S{mtuDtStart} ";
		}
		if (StringUtils.isNotBlank(m.getMtuDtEnd())){
			sql += " and t1.end_date <= $S{mtuDtEnd} ";
		}
		return super.findRows(sql, params);
	}



	public UpdateResult addMidDirectFusion(MidDirectFusion params) throws Exception {
		return super.update("INSERT INTO ods_direct_bas_inf (SCR_ID,SCR_CD,SCR_NM,SCR_FULL_NM,TRX_PLA,TRX_MKT,SPC_TYPE," +
						"ISS_MOD,SUB_LEVEL,ISSUER,PUBLISHER_TRADE,ISU_ORG_TYP_TCHNO,ISU_ORG_TYP_ECN,ISU_ORG_TYP_SCALE_SIZ," +
						"REG_TRST_ORG,REG_TRST_ORG_CMT,DEAL_DATE,CRT_DATE,CRT_TIME,CRT_USER," +
						"ISSUE_PRICE,ISSUE_VOLUME,GUAR_TYPE, " +
						"IS_EXERCISE,IS_REPAID,BEGIN_DATE,END_DATE,PAY_FREQ,INTEREST_BASE, " +
						"INTEREST_MODE,INTEREST_TYPE,COUPON_RATE,BOND_SPREAD,GUARANTEER, " +
						"ISU_BND_RAT,BOND_CREDIT,GRNT_RAT,CBND_FRS_CTG,CBND_SCD_CTG, " +
						"GG_CBC_SUB_TYPE,GG_CBC_TYPE,PBNK_FRS_CTG,PBNK_SCD_CTG,PBNK_TRD_CTG, " +
						"PUBLISHER_TRADE_PB,PUBLISHER_SCALE_PB,VERSION,CMT" +
						")values($S{scrId},$S{scrCd},$S{scrNm},$S{scrFullNm},$S{trxPla},$S{trxMkt},$S{spcType},$S{issMod}," +
						"$S{subLevel},$S{issuer},$S{publisherTrade},$S{isuOrgTypTchno},$S{isuOrgTypEcn},$S{isuOrgTypScaleSiz}," +
						"$S{regTrstOrg},$S{regTrstOrgCmt},$S{dealDate},$S{crtDate},$S{crtTime},$S{crtUser}," +
						"nullif($S{issuePrice},''), " +
						"nullif($S{issueVolume},''),$S{guarType},$S{isExercise},$S{isRepaid},$S{beginDate}, " +
						"$S{endDate},$S{payFreq},$S{interestBase},$S{interestMode},$S{interestType}, " +
						"nullif($S{couponRate},''),nullif($S{bondSpread},''),$S{guaranteer},$S{isuBndRat},$S{bondCredit}, " +
						"$S{grntRat},$S{cbndFrsCtg},$S{cbndScdCtg},$S{ggCbcSubType},$S{ggCbcType}, " +
						"$S{pbnkFrsCtg},$S{pbnkScdCtg},$S{pbnkTrdCtg},$S{publisherTradePb},$S{publisherScalePb}, " +
						"$S{version},$S{cmt}" +
						")",
				params);
	}



	public UpdateResult deleteMidDirectFusion(String scrId) throws Exception {
		return super.update("DELETE FROM ods_direct_bas_inf WHERE  scr_id=$S{scrId} ", scrId);
	}


	public UpdateResult updateMidDirectFusion(MidDirectFusion params) throws Exception {
		return super.update("update ods_direct_bas_inf set  " +
						"SCR_CD = $S{scrCd}, " +
						"SCR_NM = $S{scrNm}, " +
						"SCR_FULL_NM = $S{scrFullNm}, " +
						"TRX_PLA = $S{trxPla}, " +
						"TRX_MKT = $S{trxMkt}, " +
						"ISS_MOD = $S{issMod}, " +
						"SUB_LEVEL = $S{subLevel}, " +
						"ISSUER = $S{issuer}, " +
						"PUBLISHER_TRADE = $S{publisherTrade}, " +
						"ISU_ORG_TYP_TCHNO = $S{isuOrgTypTchno}, " +
						"ISU_ORG_TYP_ECN = $S{isuOrgTypEcn}, " +
						"ISU_ORG_TYP_SCALE_SIZ = $S{isuOrgTypScaleSiz}, " +
						"REG_TRST_ORG = $S{regTrstOrg}, " +
						"REG_TRST_ORG_CMT = $S{regTrstOrgCmt}, " +
						"UPD_DATE = $S{updDate}, " +
						"UPD_TIME = $S{updTime}, " +
						"UPD_USER = $S{updUser} " +
						"where SCR_ID = $S{scrId}",
				params);
	}

	public UpdateResult updateMidDirectFusionBl(MidDirectFusion params) throws Exception {
		return super.update("update ods_direct_bas_inf set  " +
						"ISSUE_PRICE = nullif($S{issuePrice},''), " +
						"ISSUE_VOLUME = nullif($S{issueVolume},''), " +
						"GUAR_TYPE = $S{guarType}, " +
						"IS_EXERCISE = $S{isExercise}, " +
						"IS_REPAID = $S{isRepaid}, " +
						"BEGIN_DATE = $S{beginDate}, " +
						"END_DATE = $S{endDate}, " +
						"SPC_TYPE = $S{spcType}, " +
						"PAY_FREQ = $S{payFreq}, " +
						"INTEREST_BASE = $S{interestBase}, " +
						"INTEREST_MODE = $S{interestMode}, " +
						"INTEREST_TYPE = $S{interestType}, " +
						"COUPON_RATE = nullif($S{couponRate},''), " +
						"BOND_SPREAD = nullif($S{bondSpread},''), " +
						"GUARANTEER = $S{guaranteer}, " +
						"ISU_BND_RAT = $S{isuBndRat}, " +
						"BOND_CREDIT = $S{bondCredit}, " +
						"GRNT_RAT = $S{grntRat}, " +
						"CBND_FRS_CTG = $S{cbndFrsCtg}, " +
						"CBND_SCD_CTG = $S{cbndScdCtg}, " +
						"GG_CBC_SUB_TYPE = $S{ggCbcSubType}, " +
						"GG_CBC_TYPE = $S{ggCbcType}, " +
						"PBNK_FRS_CTG = $S{pbnkFrsCtg}, " +
						"PBNK_SCD_CTG = $S{pbnkScdCtg}, " +
						"PBNK_TRD_CTG = $S{pbnkTrdCtg}, " +
						"PUBLISHER_TRADE_PB = $S{publisherTradePb}, " +
						"PUBLISHER_SCALE_PB = $S{publisherScalePb}, " +
						"VERSION = $S{version}, " +
						"CMT = $S{cmt}, " +
						"UPD_DATE = $S{updDate}, " +
						"UPD_TIME = $S{updTime}, " +
						"UPD_USER = $S{updUser} " +
						"where SCR_ID = $S{scrId}",
				params);
	}


	/**
	 * 查浮息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<MidDirectFusion> findBondFX(SqlParam<MidDirectFusion> params) throws Exception {

		String sql = "SELECT SCR_ID,FL_BEGIN_DATE,BASE_RATE,FL_END_DATE,CRT_DATE,CRT_TIME,CRT_USER,UPD_DATE,UPD_TIME," +
				"UPD_USER,DEAL_DATE FROM ODS_ASS_FLOAT_RATE";

		return super.findRows( sql, params);
	}

	/**
	 * 查行权
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<MidDirectFusion> findBondXQ(SqlParam<MidDirectFusion> params) throws Exception {

		String sql = "SELECT SCR_ID,EXERCISE_DATE,EX_COUPON_RATE,CRT_DATE,CRT_TIME,CRT_USER,UPD_DATE,UPD_TIME,UPD_USER," +
				"DEAL_DATE FROM ODS_ASS_FUSION_EXERCISE";

		return super.findRows( sql, params);
	}

	/**
	 * 查还本
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<MidDirectFusion> findBondHB(SqlParam<MidDirectFusion> params) throws Exception {

		String sql = "SELECT SCR_ID,REPAY_DATE,UNIT_PRINCIPAL,CRT_DATE,CRT_TIME,CRT_USER,UPD_DATE,UPD_TIME,UPD_USER," +
				"DEAL_DATE FROM ODS_ASS_ADVANCE_REPAY";

		return super.findRows( sql, params);
	}

	/**
	 * 保存浮息信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult insertBondFX(MidDirectFusion params) throws Exception {
		String sql = "INSERT INTO ODS_ASS_FLOAT_RATE (SCR_ID,FL_BEGIN_DATE,BASE_RATE,FL_END_DATE,CRT_DATE,CRT_TIME," +
				"CRT_USER,UPD_DATE,UPD_TIME,UPD_USER,DEAL_DATE) VALUES ($S{scrId},$S{flBeginDate},nullif($S{baseRate},'')," +
				"$S{flEndDate},$S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser},$S{dealDate}) ";
		return super.update(sql, DataSourceProperty.PUB, params);
	}

	/**
	 * 保存行权信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult insertBondXQ(MidDirectFusion params) throws Exception {
		String sql = "INSERT INTO ODS_ASS_FUSION_EXERCISE (SCR_ID,EXERCISE_DATE,EX_COUPON_RATE,CRT_DATE,CRT_TIME,CRT_USER,UPD_DATE,UPD_TIME," +
				"UPD_USER,DEAL_DATE)VALUES($S{scrId},$S{exerciseDate},nullif($S{exCouponRate},''),$S{crtDate},$S{crtTime}," +
				"$S{crtUser},$S{updDate},$S{updTime},$S{updUser},$S{dealDate}) ";
		return super.update(sql,DataSourceProperty.PUB, params);
	}

	/**
	 * 保存还本信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult insertBondHB(MidDirectFusion params) throws Exception {
		String sql = "INSERT INTO ODS_ASS_ADVANCE_REPAY(SCR_ID,REPAY_DATE,UNIT_PRINCIPAL,CRT_DATE,CRT_TIME,CRT_USER," +
				"UPD_DATE,UPD_TIME,UPD_USER,DEAL_DATE)VALUES($S{scrId},$S{repayDate},nullif($S{unitPrincipal},''),$S{crtDate}," +
				"$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser},$S{dealDate})";
		return super.update(sql,DataSourceProperty.PUB, params);
	}


	/**
	 * 删债券浮息信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult deleteBondFX(MidDirectFusion params) throws Exception {
		return super.update("DELETE FROM ODS_ASS_FLOAT_RATE WHERE SCR_ID = $S{scrId}",DataSourceProperty.PUB, params);
	}

	/**
	 * 删债券行权信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult deleteBondXQ(MidDirectFusion params) throws Exception {
		return super.update("DELETE FROM ODS_ASS_ADVANCE_REPAY WHERE SCR_ID = $S{scrId}",DataSourceProperty.PUB, params);
	}

	/**
	 * 删债券还本信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult deleteBondHB(MidDirectFusion params) throws Exception {
		return super.update("DELETE FROM ODS_ASS_FUSION_EXERCISE WHERE SCR_ID = $S{scrId}",DataSourceProperty.PUB, params);
	}



}
