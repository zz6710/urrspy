package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.FundNavInfoModel;
import com.kayak.dps.app.model.T8OrgSheet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class T8OrgSheetDao extends ComnDao {

	public SqlResult<T8OrgSheet> findT8OrgSheets(SqlParam<T8OrgSheet> params) throws Exception {
		String sql= "select " +
				"t1.CRT_DATE, " +
				"t1.CRT_USER, " +
				"t1.CSLD_SOC_CRD_CD, " +
				"t1.DEAL_DATE, " +
				"t1.ID, " +
				"t1.ORG_FULL_NAME, " +
				"t1.ORG_NBR_EXT, " +
				"t1.ORG_SHT_NM, " +
				"t1.UPD_DATE, " +
				"t1.UPD_USER, " +
				"t1.CC_INDUSTRY_ISSUER, " +
				"t1.CC_INSTITUTE_TYPE_SCALE, " +
				"t1.IS_PLAT_FORM_CH, " +
				"t1.IS_PLAT_FORM_SG, " +
				"t1.ISU_ORG_TYP_ECN, " +
				"t1.ISU_ORG_TYP_SCALE_SIZ, " +
				"t1.ISU_ORG_TYP_TCHNO, " +
				"t1.ORG_BLG_ZON, " +
				"t1.ORG_FRS_CTG, " +
				"t1.ORG_IN_RAT, " +
				"t1.ORG_OUT_RAT, " +
				"t1.ORG_SEC_CTG, " +
				"t1.ORG_TYP, " +
				"t1.REMARK, " +
				"t1.SAM_BUS_ORG_TYP, " +
				"t1.VERSION " +
				"from ODS_ORG_INFO t1 WHERE 1=1" ;
		if (StringUtils.isNotBlank(params.getModel().getOrgFullName())) {
			sql += " AND t1.ORG_FULL_NAME like '%$U{orgFullName}%' ";
		}
		if (StringUtils.isNotBlank(params.getModel().getOrgTyp())) {
			sql += " AND t1.ORG_TYP = $S{orgTyp} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getCsldSocCrdCd())) {
			sql += " AND t1.CSLD_SOC_CRD_CD like '%$U{csldSocCrdCd}%'";
		}
		return super.findRows(sql, params);
	}

	public SqlResult<T8OrgSheet> findOrgNm(SqlParam<T8OrgSheet> params) throws Exception {
		String sql = "SELECT t1.ORG_NBR,t1.ORG_NM " +
				"FROM dwd_pty_org_bas_inf t1 "+
				" where 1=1 and t1.ORG_NBR = $S{orgNbr}" ;
		return super.findRows(sql, params);
	}
	public SqlResult<T8OrgSheet> findOrgNmAll(SqlParam<T8OrgSheet> params) throws Exception {
		String sql = "select tt.ORG_NBR_EXT,tt.ORG_FULL_NAME from (SELECT t1.ORG_NBR_EXT,t1.ORG_FULL_NAME " +
				"  FROM ODS_ORG_INFO t1 WHERE 1=1";
		if (StringUtils.isNotBlank(params.getModel().getOrgFullName())) {
			sql += " AND (t1.ORG_FULL_NAME like '%$U{orgFullName}%' or t1.ORG_NBR_EXT like '%$U{orgFullName}%')";
		}
		sql+= " )tt limit 500";
		return super.findRows(sql, params);
	}

	public SqlResult<T8OrgSheet> findOrgInfo(SqlParam<T8OrgSheet> params) throws Exception {
		String sql = "SELECT a1.* FROM ODS_ORG_INFO a1 WHERE 1=1 AND a1.ORG_NBR_EXT = $S{orgNbrExt}";
		return super.findRows(sql, params);
	}

	public UpdateResult addT8OrgSheet(SqlParam<T8OrgSheet> params) throws Exception {
		return super.update("INSERT INTO ODS_ORG_INFO (ORG_NBR_EXT, ORG_SHT_NM, ORG_FULL_NAME, CSLD_SOC_CRD_CD, " +
						"ORG_TYP, SAM_BUS_ORG_TYP, " +
						"ORG_BLG_ZON, ORG_OUT_RAT, ORG_IN_RAT, IS_PLAT_FORM_CH, IS_PLAT_FORM_SG, CC_INDUSTRY_ISSUER, " +
						"ISU_ORG_TYP_SCALE_SIZ, ISU_ORG_TYP_TCHNO, ISU_ORG_TYP_ECN, CC_INSTITUTE_TYPE_SCALE, " +
						"ORG_FRS_CTG, ORG_SEC_CTG, VERSION,REMARK," +
						"CRT_USER,  CRT_DATE, UPD_DATE, DEAL_DATE) VALUES ( $S{orgNbrExt}, $S{orgShtNm}, " +
						"$S{orgFullName}, $S{csldSocCrdCd}, " +
						"$S{orgTyp}, $S{samBusOrgTyp}, " +
						"$S{orgBlgZon}, $S{orgOutRat}, $S{orgInRat}, $S{isPlatFormCh}, $S{isPlatFormSg}, " +
						"$S{ccIndustryIssuer}, $S{isuOrgTypScaleSiz}, $S{isuOrgTypTchno}, $S{isuOrgTypEcn}, " +
						"$S{ccInstituteTypeScale}, $S{orgFrsCtg}, $S{orgSecCtg}, $S{version}, $S{remark}," +
						"$S{crtUser}, $S{crtDate}, $S{updDate}, $S{dealDate})",
				params.getModel());
	}

	public UpdateResult updateT8OrgBaseSheet(SqlParam<T8OrgSheet> params) throws Exception {
		return super.update("UPDATE ODS_ORG_INFO SET ORG_SHT_NM = $S{orgShtNm}, ORG_FULL_NAME = $S{orgFullName}, CSLD_SOC_CRD_CD = $S{csldSocCrdCd}, UPD_DATE = $S{updDate}, UPD_USER = $S{updUser} WHERE ORG_NBR_EXT=$S{orgNbrExt}",
				params.getModel());
	}

	public UpdateResult updateT8OrgBaseSheetBl(SqlParam<T8OrgSheet> params) throws Exception {
		return super.update("update ODS_ORG_INFO set  " +
						"CC_INDUSTRY_ISSUER  = $S{ccIndustryIssuer}, " +
						"CC_INSTITUTE_TYPE_SCALE  = $S{ccInstituteTypeScale}, " +
						"IS_PLAT_FORM_CH  = $S{isPlatFormCh}, " +
						"IS_PLAT_FORM_SG  = $S{isPlatFormSg}, " +
						"ISU_ORG_TYP_ECN  = $S{isuOrgTypEcn}, " +
						"ISU_ORG_TYP_SCALE_SIZ  = $S{isuOrgTypScaleSiz}, " +
						"ISU_ORG_TYP_TCHNO  = $S{isuOrgTypTchno}, " +
						"ORG_BLG_ZON  = $S{orgBlgZon}, " +
						"ORG_FRS_CTG  = $S{orgFrsCtg}, " +
						"ORG_IN_RAT  = $S{orgInRat}, " +
						"ORG_OUT_RAT  = $S{orgOutRat}, " +
						"ORG_SEC_CTG  = $S{orgSecCtg}, " +
						"ORG_TYP  = $S{orgTyp}, " +
						"REMARK  = $S{remark}, " +
						"SAM_BUS_ORG_TYP  = $S{samBusOrgTyp}, " +
						"UPD_DATE  = $S{updDate}, " +
						"UPD_USER  = $S{updUser}, " +
						"VERSION  = $S{version}  " +
						"where ORG_NBR_EXT  = $S{orgNbrExt}",
				params.getModel());
	}

	public UpdateResult deleteT8OrgSheet(SqlParam<T8OrgSheet> params) throws Exception {
		return super.update("DELETE FROM ODS_ORG_INFO WHERE  ORG_NBR_EXT=$S{orgNbrExt} ",
				params.getModel());
	}

}
