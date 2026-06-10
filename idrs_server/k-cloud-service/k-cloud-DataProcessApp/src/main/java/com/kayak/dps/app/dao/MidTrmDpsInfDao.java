package com.kayak.dps.app.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.MidTrmDpsInf;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class MidTrmDpsInfDao extends ComnDao {

	public SqlResult<MidTrmDpsInf> findScrCd(SqlParam<MidTrmDpsInf> params) throws Exception {
		return super.findRows("SELECT DISTINCT scr_cd,scr_nm FROM ods_trm_bas_inf where SCR_CD like '%$U{scrCd}%' or SCR_NM like '%$U{scrCd}%'", params);
	}

	public SqlResult<MidTrmDpsInf> findMidTrmDpsInfs(SqlParam<MidTrmDpsInf> params) throws Exception {

		String sql = "select  " +
				"t1.SCR_ID, " +
				"t1.ASSET_DEBT_TYPE, " +
				"t1.SCR_CD, " +
				"t1.SCR_NM, " +
				"t1.DPS_BNK, " +
				"t1.DPS_ACT_NBR, " +
				"t1.DPS_AMT, " +
				"t1.VAL_DT, " +
				"t1.MTU_DT, " +
				"t1.ANL_YLD, " +
				"t1.INTR_BAS, " +
				"t1.DPS_TYP, " +
				"t1.TRX_MKT, " +
				"t1.CCY, " +
				"t1.CRT_DATE, " +
				"t1.CRT_TIME, " +
				"t1.CRT_USER, " +
				"t1.UPD_DATE, " +
				"t1.UPD_TIME, " +
				"t1.UPD_USER, " +
				"t1.DEAL_DATE, " +
				"t1.LNK_SBJ_MAT_TYP, " +
				"t1.LNK_SBJ_MAT, " +
				"t1.PAYINTEREST_FREQ, " +
				"t1.CBND_FRS_CTG, " +
				"t1.CBND_SCD_CTG, " +
				"t1.GG_CBC_SUB_TYPE, " +
				"t1.GG_CBC_TYPE, " +
				"t1.PBNK_FRS_CTG, " +
				"t1.PBNK_SCD_CTG, " +
				"t1.PBNK_TRD_CTG, " +
				"t1.PBNK_FUR_CTG, " +
				"t1.CMT, " +
				"t1.VERSION  " +
				"from ods_trm_bas_inf t1  where 1=1 ";

		MidTrmDpsInf m = params.getModel();
		if (StringUtils.isNotBlank(m.getScrCd())){
			sql += " and t1.scr_cd = $S{scrCd} ";
		}
		if (StringUtils.isNotBlank(m.getValDtStart())){
			sql += " and t1.val_dt >= $S{valDtStart} ";
		}
		if (StringUtils.isNotBlank(m.getValDtEnd())){
			sql += " and t1.val_dt <= $S{valDtEnd} ";
		}
		if (StringUtils.isNotBlank(m.getMtuDtStart())){
			sql += " and t1.mtu_dt >= $S{mtuDtStart} ";
		}
		if (StringUtils.isNotBlank(m.getMtuDtEnd())){
			sql += " and t1.mtu_dt <= $S{mtuDtEnd} ";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addMidTrmDpsInf(MidTrmDpsInf params) throws Exception {
		return super.update("insert into ods_trm_bas_inf( " +
						"SCR_ID, " +
						"ASSET_DEBT_TYPE, " +
						"SCR_CD, " +
						"SCR_NM, " +
						"DPS_BNK, " +
						"DPS_ACT_NBR, " +
						"DPS_AMT, " +
						"VAL_DT, " +
						"MTU_DT, " +
						"ANL_YLD, " +
						"INTR_BAS, " +
						"DPS_TYP, " +
						"TRX_MKT, " +
						"CCY,LNK_SBJ_MAT_TYP,LNK_SBJ_MAT,PAYINTEREST_FREQ, " +
						"CBND_FRS_CTG,CBND_SCD_CTG,GG_CBC_SUB_TYPE,GG_CBC_TYPE, " +
						"PBNK_FRS_CTG,PBNK_SCD_CTG,PBNK_TRD_CTG,PBNK_FUR_CTG,CMT,VERSION, " +
						"CRT_DATE, " +
						"CRT_TIME, " +
						"CRT_USER, " +
						"DEAL_DATE)values( " +
						"$S{scrId}, " +
						"$S{assetDebtType}, " +
						"$S{scrCd}, " +
						"$S{scrNm}, " +
						"$S{dpsBnk}, " +
						"$S{dpsActNbr}, " +
						"nullif($S{dpsAmt},''), " +
						"$S{valDt}, " +
						"$S{mtuDt}, " +
						"nullif($S{anlYld},''), " +
						"$S{intrBas}, " +
						"$S{dpsTyp}, " +
						"$S{trxMkt}, " +
						"$S{ccy},$S{lnkSbjMatTyp}," +
						"$S{lnkSbjMat},$S{payinterestFreq}, " +
						"$S{cbndFrsCtg},$S{cbndScdCtg},$S{ggCbcSubType}, " +
						"$S{ggCbcType},$S{pbnkFrsCtg},$S{pbnkScdCtg},$S{pbnkTrdCtg},$S{pbnkFurCtg},$S{cmt},$S{version}," +
						"$S{crtDate}, " +
						"$S{crtTime}, " +
						"$S{crtUser}, " +
						"$S{dealDate})",
				params);
	}

	public UpdateResult updateMidTrmDpsInf(MidTrmDpsInf params) throws Exception {
		return super.update(
						"update ods_trm_bas_inf set  " +
								"ASSET_DEBT_TYPE = $S{assetDebtType}, " +
								"SCR_NM = $S{scrNm}, " +
								"DPS_BNK = $S{dpsBnk}, " +
								"DPS_ACT_NBR = $S{dpsActNbr}, " +
								"DPS_AMT = nullif($S{dpsAmt},''), " +
								"VAL_DT = $S{valDt}, " +
								"MTU_DT = $S{mtuDt}, " +
								"ANL_YLD = nullif($S{anlYld},''), " +
								"INTR_BAS = $S{intrBas}, " +
								"DPS_TYP = $S{dpsTyp}, " +
								"TRX_MKT = $S{trxMkt}, " +
								"CBND_FRS_CTG = $S{cbndFrsCtg}, " +
								"CBND_SCD_CTG = $S{cbndScdCtg}, " +
								"GG_CBC_SUB_TYPE = $S{ggCbcSubType}, " +
								"GG_CBC_TYPE = $S{ggCbcType}, " +
								"PBNK_FRS_CTG = $S{pbnkFrsCtg}, " +
								"PBNK_SCD_CTG = $S{pbnkScdCtg}, " +
								"PBNK_TRD_CTG = $S{pbnkTrdCtg}, " +
								"PBNK_FUR_CTG = $S{pbnkFurCtg}, " +
								"CCY = $S{ccy}, " +
								"UPD_DATE = $S{updDate}, " +
								"UPD_TIME = $S{updTime}, " +
								"UPD_USER = $S{updUser} " +
								"where SCR_ID = $S{scrId}",
				params);
	}

	public UpdateResult updateMidTrmDpsInfBl(MidTrmDpsInf params) throws Exception {
		return super.update(
						"update ods_trm_bas_inf set " +
								"LNK_SBJ_MAT_TYP = $S{lnkSbjMatTyp}, " +
								"LNK_SBJ_MAT = $S{lnkSbjMat}, " +
								"PAYINTEREST_FREQ = $S{payinterestFreq}, " +
								"CMT = $S{cmt}, " +
								"VERSION = $S{version}, " +
								"UPD_DATE = $S{updDate}, " +
								"UPD_TIME = $S{updTime}, " +
								"UPD_USER = $S{updUser} " +
								"where SCR_ID = $S{scrId}",
				params);
	}


	public UpdateResult updateMidTrmDpsInfBlField(MidTrmDpsInf params) throws Exception {
		return super.update(
				"update ods_trm_bas_inf set " +
						" GG_CBC_SUB_TYPE = $S{ggCbcSubType} " +
						" where SCR_ID = $S{scrId}",
				params);
	}
	
	public UpdateResult deleteMidTrmDpsInf(MidTrmDpsInf params) throws Exception {
		return super.update("DELETE FROM ods_trm_bas_inf WHERE  scr_id=$S{scrId} ",
				params);
	}

}
