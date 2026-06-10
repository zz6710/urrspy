package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.AsharedescriptionModel;
import com.kayak.dps.app.model.NonStandInfoModel;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AsharedescriptionDao extends ComnDao {

	public SqlResult<AsharedescriptionModel> findAsharedesriptionInfo(SqlParam<AsharedescriptionModel> params) throws Exception {
		String sql = "SELECT `SCR_ID`, `SCR_CD`, `SCR_NM`, `COMPANY_NAME`, `TRX_MKT`, `PLATE_TYPE`, `CCY`, `CBND_FRS_CTG`, `CBND_SCD_CTG`, `PBNK_FRS_CTG`, `PBNK_SCD_CTG`, `PBNK_TRD_CTG`, `STOCK_TYPE`, `INVESTMENT_TYPE`, `SHAREHOLD`, `ISU_ORG_TYP_SIZ`, `ISU_ORG_TYP_TCHNO`, `ISU_ORG_TYP_ECN`, `INDUSTRY_ISSUER`, `PLEDGED_FINACE`, `DEBT_EQUITY_SWAP`, `REMARK`, `DEAL_DATE`, `VERSION` from ods_supply_asharedescription  ";
		return super.findRows(sql, DataSourceProperty.PUB,params);
	}


	public SqlResult<AsharedescriptionModel> findAshareInfoIdAndNm(SqlParam<AsharedescriptionModel> params) throws Exception {
		return super.findRows("SELECT DISTINCT SCR_CD,SCR_NM FROM ods_supply_asharedescription where SCR_CD like '%$U{scrCd}%' or SCR_NM like '%$U{scrCd}%'  ", DataSourceProperty.PUB, params);
	}

    public UpdateResult addAsharedescriptionInfo(SqlParam<AsharedescriptionModel> params) throws Exception {
		return super.update("update  \n" +
						"ods_supply_asharedescription\n" +
						"set\n" +
						"cbnd_scd_ctg = $S{cbndScdCtg},\n" +
						"pbnk_trd_ctg = $S{pbnkTrdCtg},\n" +
						"stock_type = $S{stockType},\n" +
						"investment_type = $S{investmentType},\n" +
						"sharehold = $S{sharehold},\n" +
						"isu_org_typ_siz = $S{isuOrgTypSiz},\n" +
						"isu_org_typ_tchno = $S{isuOrgTypTchno},\n" +
						"isu_org_typ_ecn = $S{isuOrgTypEcn},\n" +
						"industry_issuer = $S{industryIssuer},\n" +
						"pledged_finace = $S{pledgedFinace},\n" +
						"debt_equity_swap = $S{debtEquitySwap},\n" +
						"remark = $S{remark},\n" +
						"version = version + 1,\n" +
						"upd_dt = DATE_FORMAT(NOW(), '%Y%m%d')\n" +
						"where \n" +
						"scr_id = $S{scrId} and scr_cd = $S{scrCd}  and scr_nm = $S{scrNm} ",
				DataSourceProperty.PUB, params.getModel());
	}
}
