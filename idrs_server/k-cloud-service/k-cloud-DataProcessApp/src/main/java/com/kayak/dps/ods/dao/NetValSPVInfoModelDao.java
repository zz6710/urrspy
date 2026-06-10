package com.kayak.dps.ods.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.BondInfoModel;
import com.kayak.dps.app.model.NetValSPVInfoModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;

@Repository
public class NetValSPVInfoModelDao extends ComnDao {

	public SqlResult<NetValSPVInfoModel> findNetValSPVInfoModels(SqlParam<NetValSPVInfoModel> params) throws Exception {
		String sql = "select  " +
				"t1.SCR_ID, " +
				"t1.SCR_CD, " +
				"t1.SCR_NM, " +
				"t1.TRX_MKT, " +
				"t1.TRX_PLA, " +
				"t1.INVEST_WAY,"+
				"t1.CCY, " +
				"t1.SPV_ORG_ENC, " +
				"t1.SPV_PROD_REG_ENC, " +
				"t1.MNG, " +
				"t1.CSTD, " +
				"t1.FND_ACTL_DIR, " +
				"t1.FND_CRRY_IDT, " +
				"t1.SET_UP_DT, " +
				"t1.MTU_DT, " +
				"t1.EXPE_RAT_F, " +
				"t1.CRT_DATE, " +
				"t1.CRT_TIME, " +
				"t1.CRT_USER, " +
				"t1.UPD_DATE, " +
				"t1.UPD_TIME, " +
				"t1.UPD_USER, " +
				"t1.DEAL_DATE, " +
				"t1.BNK_INV_PROD_F, " +
				"t1.PROD_REG_ENC, " +
				"t1.FIN_AST_INV_CMP_ISU_F, " +
				"t1.IS_FIN_ISU_F, " +
				"t1.AMT, " +
				"t1.FND_CRRY_MTH, " +
				"t1.AST_MNG_PLAN_PRPT, " +
				"t1.EXPE_MAX_RAT, " +
				"t1.EXPE_MIN_RAT, " +
				"t1.BUY_STRC, " +
				"t1.MNG_MTH, " +
				"t1.MNG_FEE_TAT, " +
				"t1.TRST_FEE_TAT, " +
				"t1.TRX_REL_SMR_FEE_RAT, " +
				"t1.MED_AGN_SRV_ORG_SMR_FEE_RAT, " +
				"t1.OTH_SMR_FEE_RAT, " +
				"t1.ISU_ORG_ENC, " +
				"t1.CBND_FRS_CTG, " +
				"t1.CBND_SCD_CTG, " +
				"t1.GG_CBC_SUB_TYPE, " +
				"t1.GG_CBC_TYPE, " +
				"t1.PBNK_FRS_CTG, " +
				"t1.PBNK_SCD_CTG, " +
				"t1.PBNK_TRD_CTG, " +
				"t1.PBNK_FUR_CTG, " +
				"t1.CMT, " +
				"t1.VERSION," +
				"org.SAM_BUS_ORG_TYP " +
				"from  ods_mng_plan_bas_inf t1 " +
				"left join  ods_org_info org on org.ORG_NBR_EXT =t1.MNG" +
				" where 1=1 ";

		if (StringUtils.isNotBlank(params.getModel().getScrCd())){
			sql += "and t1.scr_cd = $S{scrCd} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getValDtStart())){
			sql += " and t1.SET_UP_DT >= $S{valDtStart} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getValDtEnd())){
			sql += " and t1.SET_UP_DT <= $S{valDtEnd} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getCbndScdCtg())){
			sql += " and t1.cbnd_scd_ctg = $S{cbndScdCtg} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getPbnkTrdCtg())){
			sql += " and t1.pbnk_trd_ctg = $S{pbnkTrdCtg} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getInvestWay())){
			sql += " and t1.INVEST_WAY = $S{investWay} ";
		}

 		return super.findRows(sql, params);
	}

	public UpdateResult addNetValSPVInfoModel(SqlParam<NetValSPVInfoModel> params) throws Exception {
		return super.update("insert into ods_mng_plan_bas_inf ( " +
						"SCR_ID, " +
						"SCR_CD, " +
						"SCR_NM, " +
						"TRX_MKT, " +
						"TRX_PLA, " +
				        "INVEST_WAY,"+
						"CCY, " +
						"MNG, " +
						"CSTD, " +
						"FND_ACTL_DIR, " +
						"FND_CRRY_IDT, " +
						"SET_UP_DT, " +
						"MTU_DT, " +
						"EXPE_RAT_F, " +
						"BNK_INV_PROD_F, " +
						"PROD_REG_ENC, " +
						"FIN_AST_INV_CMP_ISU_F, " +
						"IS_FIN_ISU_F, " +
						"AMT, " +
						"FND_CRRY_MTH, " +
						"AST_MNG_PLAN_PRPT, " +
						"EXPE_MAX_RAT, " +
						"EXPE_MIN_RAT, " +
						"BUY_STRC, " +
						"MNG_MTH, " +
						"MNG_FEE_TAT, " +
						"TRST_FEE_TAT, " +
						"TRX_REL_SMR_FEE_RAT, " +
						"MED_AGN_SRV_ORG_SMR_FEE_RAT, " +
						"OTH_SMR_FEE_RAT, " +
						"ISU_ORG_ENC, " +
						"CBND_FRS_CTG," +
						"CBND_SCD_CTG, " +
						"GG_CBC_SUB_TYPE, " +
						"GG_CBC_TYPE, " +
						"PBNK_FRS_CTG, " +
						"PBNK_SCD_CTG, " +
						"PBNK_TRD_CTG, " +
						"PBNK_FUR_CTG, " +
						"CMT, " +
						"VERSION, " +
						"CRT_DATE, " +
						"CRT_TIME, " +
						"CRT_USER, " +
						"DEAL_DATE " +
						")values( " +
						"$S{scrId}, " +
						"$S{scrCd}, " +
						"$S{scrNm}, " +
						"$S{trxMkt}, " +
						"$S{trxPla}, " +
						"$S{investWay},"+
						"$S{ccy}, " +
						"$S{mng}, " +
						"$S{cstd}, " +
						"$S{fndActlDir}, " +
						"$S{fndCrryIdt}, " +
						"$S{setUpDt}, " +
						"$S{mtuDt}, " +
						"$S{expeRatF}, " +
						"$S{bnkInvProdF}, " +
						"$S{prodRegEnc}, " +
						"$S{finAstInvCmpIsuF}, " +
						"$S{isFinIsuF}, " +
						"nullif($S{amt},''), " +
						"$S{fndCrryMth}, " +
						"$S{astMngPlanPrpt}, " +
						"nullif($S{expeMaxRat},''), " +
						"nullif($S{expeMinRat},''), " +
						"$S{buyStrc}, " +
						"$S{mngMth}, " +
						"nullif($S{mngFeeTat},''), " +
						"nullif($S{trstFeeTat},''), " +
						"nullif($S{trxRelSmrFeeRat},''), " +
						"nullif($S{medAgnSrvOrgSmrFeeRat},''), " +
						"nullif($S{othSmrFeeRat},''), " +
						"$S{isuOrgEnc}, " +
						"$S{cbndFrsCtg}, " +
						"$S{cbndScdCtg}, " +
						"$S{ggCbcSubType}, " +
						"$S{ggCbcType}, " +
						"$S{pbnkFrsCtg}, " +
						"$S{pbnkScdCtg}, " +
						"$S{pbnkTrdCtg}, " +
						"$S{pbnkFurCtg}, " +
						"$S{cmt}, " +
						"$S{version}, " +
						"$S{crtDate}, " +
						"$S{crtTime}, " +
						"$S{crtUser}, " +
						"$S{dealDate})",
				DataSourceProperty.PUB,
				params.getModel());
	}
	
	public UpdateResult updateNetValSPVInfoModel(SqlParam<NetValSPVInfoModel> params) throws Exception {
		return super.update("update ods_mng_plan_bas_inf set  " +
						"SCR_NM=$S{scrNm}, " +
						"INVEST_WAY=$S{investWay},"+
						"TRX_MKT=$S{trxMkt}, " +
						"TRX_PLA=$S{trxPla}, " +
						"CCY=$S{ccy}, " +
						"MNG=$S{mng}, " +
						"CSTD=$S{cstd}, " +
						"FND_ACTL_DIR=$S{fndActlDir}, " +
						"FND_CRRY_IDT=$S{fndCrryIdt}, " +
						"SET_UP_DT=$S{setUpDt}, " +
						"MTU_DT=$S{mtuDt}, " +
						"EXPE_RAT_F=$S{expeRatF}, " +
						"UPD_DATE=$S{updDate}, " +
						"UPD_TIME=$S{updTime}, " +
						"UPD_USER=$S{updUser}, " +
						"DEAL_DATE=$S{dealDate} " +
						"where SCR_ID=$S{scrId}",
				DataSourceProperty.PUB,
				params.getModel());
	}

	public UpdateResult updateNetValSPVInfoModelBl(SqlParam<NetValSPVInfoModel> params) throws Exception {
		return super.update("update ods_mng_plan_bas_inf set  " +
						"SCR_NM=$S{scrNm}, " +
						"INVEST_WAY=$S{investWay},"+
						"TRX_MKT=$S{trxMkt}, " +
						"TRX_PLA=$S{trxPla}, " +
						"CCY=$S{ccy}, " +
						"MNG=$S{mng}, " +
						"CSTD=$S{cstd}, " +
						"FND_ACTL_DIR=$S{fndActlDir}, " +
						"FND_CRRY_IDT=$S{fndCrryIdt}, " +
						"SET_UP_DT=$S{setUpDt}, " +
						"MTU_DT=$S{mtuDt}, " +
						"EXPE_RAT_F=$S{expeRatF}, " +
						"BNK_INV_PROD_F = $S{bnkInvProdF}, " +
						"PROD_REG_ENC = $S{prodRegEnc}, " +
						"CBND_FRS_CTG = $S{cbndFrsCtg}, " +
						"CBND_SCD_CTG = $S{cbndScdCtg}, " +
						"FIN_AST_INV_CMP_ISU_F = $S{finAstInvCmpIsuF}, " +
						"IS_FIN_ISU_F = $S{isFinIsuF}, " +
						"AMT = nullif($S{amt},''), " +
						"FND_CRRY_MTH = $S{fndCrryMth}, " +
						"AST_MNG_PLAN_PRPT = $S{astMngPlanPrpt}, " +
						"EXPE_MAX_RAT = nullif($S{expeMaxRat},''), " +
						"EXPE_MIN_RAT = nullif($S{expeMinRat},''), " +
						"BUY_STRC = $S{buyStrc}, " +
						"MNG_MTH = $S{mngMth}, " +
						"MNG_FEE_TAT = nullif($S{mngFeeTat},''), " +
						"TRST_FEE_TAT = nullif($S{trstFeeTat},''), " +
						"TRX_REL_SMR_FEE_RAT = nullif($S{trxRelSmrFeeRat},''), " +
						"MED_AGN_SRV_ORG_SMR_FEE_RAT = nullif($S{medAgnSrvOrgSmrFeeRat},''), " +
						"OTH_SMR_FEE_RAT = nullif($S{othSmrFeeRat},''), " +
						"ISU_ORG_ENC = $S{isuOrgEnc}, " +
						"GG_CBC_SUB_TYPE = $S{ggCbcSubType}, " +
						"GG_CBC_TYPE = $S{ggCbcType}, " +
						"PBNK_FRS_CTG = $S{pbnkFrsCtg}, " +
						"PBNK_SCD_CTG = $S{pbnkScdCtg}, " +
						"PBNK_TRD_CTG = $S{pbnkTrdCtg}, " +
						"PBNK_FUR_CTG = $S{pbnkFurCtg}, " +
						"SPV_ORG_ENC=$S{spvOrgEnc}, " +
						"SPV_PROD_REG_ENC=$S{spvProdRegEnc}, " +
						"CMT = $S{cmt}, " +
						"VERSION = $S{version}, " +
						"UPD_DATE = $S{updDate}, " +
						"UPD_TIME = $S{updTime}, " +
						"UPD_USER = $S{updUser} " +
						"where SCR_ID = $S{scrId}",
				DataSourceProperty.PUB,
				params.getModel());
	}
	
	public UpdateResult deleteNetValSPVInfoModel(SqlParam<NetValSPVInfoModel> params) throws Exception {
		return super.update("DELETE FROM ods_mng_plan_bas_inf WHERE SCR_ID = $S{scrId}",
				DataSourceProperty.PUB,
				params.getModel());
	}

	public SqlRow existNetValInfo(SqlParam<NetValSPVInfoModel> params) throws Exception {
		return super.findRow("select count(1) con from ods_ast_ast_mng_plan_inf where SCR_CD = $S{scrCd} ",
				DataSourceProperty.PUB, params);
	}

	public List<SqlRow> getXPTypeByDocType(String doc_type) throws Exception {
		return super.findRows("SELECT itemkey VALUE,  " +
				"itemval TEXT  " +
				"FROM sys_dict_item  " +
				"WHERE dict = 'tr_cbnd_scd_ctg' " +
				"AND itemkey LIKE '$U{doc_type}%' " +
				"ORDER BY itemkey+0",DataSourceProperty.PUB,doc_type);
	}

	public  List<SqlRow>  getUPDTypeByDocType(Object params) throws Exception {
		List<SqlRow> s = super.findRows("SELECT itemkey VALUE,  "  +
				"itemval TEXT  "  +
				"FROM sys_dict_item  "  +
				"WHERE dict = 'cbndScdCtg' " +
				"AND itemorder LIKE '$U{cbndFrsCtg}%' " +
				"ORDER BY itemkey+0",DataSourceProperty.PUB,params);
		return s;
	}

	public SqlResult<NetValSPVInfoModel> findNetValSPVInfoModelsCdAndNm(SqlParam<NetValSPVInfoModel> params) throws Exception {
		String sql = "SELECT DISTINCT SCR_ID,SCR_CD,SCR_NM FROM ods_mng_plan_bas_inf where SCR_CD like '%$U{scrCd}%' or SCR_NM like '%$U{scrCd}%' ";
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}


	public List<SqlRow> findNetValNm(String scrId) throws Exception {
		return super.findRows("select SCR_NM from DWD_AST_AST_MNG_PLAN_INF where SCR_ID =$S{scrId}",DataSourceProperty.PUB, scrId);
	}

	public  List<SqlRow>  getPbnkScdByPbnkFrs(Object params) throws Exception {
		List<SqlRow> s = super.findRows("select itemkey VALUE,itemval TEXT   FROM sys_dict_item   WHERE dict = 'pbnkScdCtg'  and substr(itemkey,1,1) = $S{pbnkFrsCtg} " +
				"ORDER BY itemkey+0 ",DataSourceProperty.PUB,params);
		return s;
	}

	public  List<SqlRow>  getPbnkTrdByPbnkScd(Object params) throws Exception {
		List<SqlRow> s = super.findRows("select itemkey VALUE,itemval TEXT   FROM sys_dict_item   WHERE dict = 'pbnkTrdCtg'  and substr(itemkey,1,2) = $S{pbnkScdCtg} " +
				"ORDER BY itemkey+0 ",DataSourceProperty.PUB,params);
		return s;
	}

	public  List<SqlRow>  getBredCdDict(Object params) throws Exception {
		List<SqlRow> s = super.findRows("SELECT itemkey VALUE,  "  +
				"itemval TEXT  "  +
				"FROM sys_dict_item  "  +
				"WHERE dict = 'bred_cd' " +
				"AND itemkey in ('10','11') " +
				"ORDER BY itemkey+0",DataSourceProperty.PUB,params);
		return s;
	}
	public  SqlResult<NetValSPVInfoModel>   getMngType(SqlParam<NetValSPVInfoModel>  params) throws Exception {
		String sql ="select t.SAM_BUS_ORG_TYP CBND_SCD_CTG from ods_org_info t where t.ORG_NBR_EXT = '"+params.getParams().get("mng")+"'";
		return super.findRows(sql,DataSourceProperty.PUB,params);
	}


}
