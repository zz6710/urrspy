package com.kayak.dps.app.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.AssetCollection;
import com.kayak.dps.app.model.MidAssAsharedescription;
import com.kayak.dps.app.model.MidTrmDpsInf;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class MidAssAsharedescriptionDao extends ComnDao {

	public SqlResult<MidAssAsharedescription> findFndCd(SqlParam<MidAssAsharedescription> params) throws Exception {
		return super.findRows("select t.scr_cd,t.scr_nm from mid_ast_fnd_bas_inf t where t.bred_cd = '9' ", params);
	}

	public SqlResult<MidAssAsharedescription> findScrCd(SqlParam<MidAssAsharedescription> params) throws Exception {
		return super.findRows("SELECT DISTINCT scr_cd,scr_nm FROM ods_asharede_bas_inf where SCR_CD like '%$U{scrCd}%' or SCR_NM like '%$U{scrCd}%' ", params);
	}
	public SqlResult<MidAssAsharedescription> findCompanyName(SqlParam<MidAssAsharedescription> params) throws Exception {
		return super.findRows("SELECT DISTINCT company_name FROM ods_ass_asharedescription ", params);
	}
	public int findAssetCount(SqlParam<MidAssAsharedescription> params) throws Exception {
		String sql="SELECT count(1) cnt " +
				"   FROM ods_asharede_bas_inf t1 "  +
				"	WHERE t1.SCR_ID ='"+params.getParams().get("scrId")+"'";
		return super.findRow(sql, null).getInteger("cnt");
	}
	public SqlResult<MidAssAsharedescription> findMidAssAsharedescriptions(SqlParam<MidAssAsharedescription> params) throws Exception {
		String sql = "select  " +
				"t1.SCR_ID, " +
				"t1.SCR_CD, " +
				"t1.SCR_NM, " +
				"t1.CCY, " +
				"t1.TRX_MKT, " +
				"t1.TRX_PLA, " +
				"t1.COMPANY_NAME, " +
				"t1.PLATE_TYPE, " +
				"t1.CRT_DATE, " +
				"t1.CRT_TIME, " +
				"t1.CRT_USER, " +
				"t1.UPD_DATE, " +
				"t1.UPD_TIME, " +
				"t1.UPD_USER, " +
				"t1.DEAL_DATE, " +
				"t1.INVESTMENT_TYPE, " +
				"t1.SHAREHOLD, " +
				"t1.PLEDGED_FINACE, " +
				"t1.DEBT_EQUITY_SWAP, " +
				"t1.STOCK_TYPE, " +
				"t1.INDUSTRY_ISSUER, " +
				"t1.ISU_ORG_TYP_SIZ, " +
				"t1.ISU_ORG_TYP_TCHNO, " +
				"t1.ISU_ORG_TYP_ECN, " +
				"t1.CMT, " +
				"t1.ASS_INF_CLASS, " +
				"t1.CBND_FRS_CTG, " +
				"t1.CBND_SCD_CTG, " +
				"t1.GG_CBC_SUB_TYPE, " +
				"t1.GG_CBC_TYPE, " +
				"t1.PBNK_FRS_CTG, " +
				"t1.PBNK_SCD_CTG, " +
				"t1.PBNK_TRD_CTG, " +
				"t1.VERSION  " +
				"from ods_asharede_bas_inf t1 where 1=1 ";
		MidAssAsharedescription m = params.getModel();
		if (StringUtils.isNotBlank(m.getScrCd())){
			sql += " and t1.scr_cd = $S{scrCd} ";
		}
		if (StringUtils.isNotBlank(m.getTrxMkt())){
			sql += " and t1.trx_mkt = $S{trxMkt} ";
		}
		if (StringUtils.isNotBlank(m.getStockType())){
			sql += " and t1.stock_type = $S{stockType} ";
		}
		if (StringUtils.isNotBlank(m.getCbndScdCtg())){
			sql += " and t1.cbnd_scd_ctg = $S{cbndScdCtg} ";
		}
		if (StringUtils.isNotBlank(m.getPbnkTrdCtg())){
			sql += " and t1.pbnk_trd_ctg = $S{pbnkTrdCtg} ";
		}
		if (StringUtils.isNotBlank(m.getVersion())){
			sql += " and t1.version = $S{version} ";
		}
		return super.findRows(sql ,params);
	}

	public UpdateResult addMidAssAsharedescription(MidAssAsharedescription params) throws Exception {
		return super.update("insert into ods_asharede_bas_inf( " +
						"SCR_ID, " +
						"SCR_CD, " +
						"SCR_NM, " +
						"CCY, " +
						"TRX_MKT, " +
						"TRX_PLA, " +
						"COMPANY_NAME, " +
						"PLATE_TYPE, " +
						"INVESTMENT_TYPE, " +
						"SHAREHOLD, " +
						"PLEDGED_FINACE, " +
						"DEBT_EQUITY_SWAP, " +
						"STOCK_TYPE, " +
						"INDUSTRY_ISSUER, " +
						"ISU_ORG_TYP_SIZ, " +
						"ISU_ORG_TYP_TCHNO, " +
						"ISU_ORG_TYP_ECN, " +
						"CMT, " +
						"ASS_INF_CLASS, " +
						"CBND_FRS_CTG, " +
						"CBND_SCD_CTG, " +
						"GG_CBC_SUB_TYPE, " +
						"GG_CBC_TYPE, " +
						"PBNK_FRS_CTG, " +
						"PBNK_SCD_CTG, " +
						"PBNK_TRD_CTG, " +
						"VERSION, " +
						"CRT_DATE, " +
						"CRT_TIME, " +
						"CRT_USER, " +
						"DEAL_DATE " +
						")values( " +
						"$S{scrId}, " +
						"$S{scrCd}, " +
						"$S{scrNm}, " +
						"$S{ccy}, " +
						"$S{trxMkt}, " +
						"$S{trxPla}, " +
						"$S{companyName}, " +
						"$S{plateType}, " +
						"$S{investmentType}, " +
						"$S{sharehold}, " +
						"$S{pledgedFinace}, " +
						"$S{debtEquitySwap}, " +
						"$S{stockType}, " +
						"$S{industryIssuer}, " +
						"$S{isuOrgTypSiz}, " +
						"$S{isuOrgTypTchno}, " +
						"$S{isuOrgTypEcn}, " +
						"$S{cmt}, " +
						"$S{assInfClass}, " +
						"$S{cbndFrsCtg}, " +
						"$S{cbndScdCtg}, " +
						"$S{ggCbcSubType}, " +
						"$S{ggCbcType}, " +
						"$S{pbnkFrsCtg}, " +
						"$S{pbnkScdCtg}, " +
						"$S{pbnkTrdCtg}, " +
						"$S{version}, " +
						"$S{crtDate}, " +
						"$S{crtTime}, " +
						"$S{crtUser}, " +
						"$S{dealDate})",
				params);
	}

	public UpdateResult updateMidAssAsharedescription(MidAssAsharedescription params) throws Exception {
		return super.update("update ods_asharede_bas_inf set  " +
						"SCR_NM = $S{scrNm}, " +
						"CCY = $S{ccy}, " +
						"TRX_MKT = $S{trxMkt}, " +
						"TRX_PLA = $S{trxPla}, " +
						"COMPANY_NAME = $S{companyName}, " +
						"PLATE_TYPE = $S{plateType}, " +
						"INVESTMENT_TYPE=$S{investmentType}, " +
						"SHAREHOLD=$S{sharehold}, " +
						"PLEDGED_FINACE=$S{pledgedFinace}, " +
						"DEBT_EQUITY_SWAP=$S{debtEquitySwap}, " +
						"STOCK_TYPE=$S{stockType}, " +
						"INDUSTRY_ISSUER=$S{industryIssuer}, " +
						"ISU_ORG_TYP_SIZ=$S{isuOrgTypSiz}, " +
						"ISU_ORG_TYP_TCHNO=$S{isuOrgTypTchno}, " +
						"ISU_ORG_TYP_ECN=$S{isuOrgTypEcn}, " +
						"CMT=$S{cmt}, " +
						"ASS_INF_CLASS=$S{assInfClass}, " +
						"CBND_FRS_CTG=$S{cbndFrsCtg}, " +
						"CBND_SCD_CTG=$S{cbndScdCtg}, " +
						"GG_CBC_SUB_TYPE=$S{ggCbcSubType}, " +
						"GG_CBC_TYPE=$S{ggCbcType}, " +
						"PBNK_FRS_CTG=$S{pbnkFrsCtg}, " +
						"PBNK_SCD_CTG=$S{pbnkScdCtg}, " +
						"PBNK_TRD_CTG=$S{pbnkTrdCtg}, " +
						"VERSION=$S{version}, " +
						"UPD_DATE = $S{updDate}, " +
						"UPD_TIME = $S{updTime}, " +
						"UPD_USER = $S{updUser} " +
						"where SCR_ID = $S{scrId} ",
				params);
	}

	public UpdateResult updateMidAssAsharedescriptionBl(MidAssAsharedescription params) throws Exception {
		return super.update("update ods_asharede_bas_inf set " +
						"INVESTMENT_TYPE=$S{investmentType}, " +
						"SHAREHOLD=$S{sharehold}, " +
						"PLEDGED_FINACE=$S{pledgedFinace}, " +
						"DEBT_EQUITY_SWAP=$S{debtEquitySwap}, " +
						"STOCK_TYPE=$S{stockType}, " +
						"INDUSTRY_ISSUER=$S{industryIssuer}, " +
						"ISU_ORG_TYP_SIZ=$S{isuOrgTypSiz}, " +
						"ISU_ORG_TYP_TCHNO=$S{isuOrgTypTchno}, " +
						"ISU_ORG_TYP_ECN=$S{isuOrgTypEcn}, " +
						"CMT=$S{cmt}, " +
						"ASS_INF_CLASS=$S{assInfClass}, " +
						"CBND_FRS_CTG=$S{cbndFrsCtg}, " +
						"CBND_SCD_CTG=$S{cbndScdCtg}, " +
						"GG_CBC_SUB_TYPE=$S{ggCbcSubType}, " +
						"GG_CBC_TYPE=$S{ggCbcType}, " +
						"PBNK_FRS_CTG=$S{pbnkFrsCtg}, " +
						"PBNK_SCD_CTG=$S{pbnkScdCtg}, " +
						"PBNK_TRD_CTG=$S{pbnkTrdCtg}, " +
						"VERSION=$S{version}, " +
						"UPD_DATE=$S{updDate}, " +
						"UPD_TIME=$S{updTime}, " +
						"UPD_USER=$S{updUser} " +
						"where SCR_ID=$S{scrId}",
				params);
	}
	
	public UpdateResult deleteMidAssAsharedescription(MidAssAsharedescription params) throws Exception {
		return super.update("DELETE FROM ods_asharede_bas_inf WHERE  scr_id=$S{scrId} ",
				params);
	}

}
