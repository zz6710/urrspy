package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.TrusteeModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class TrusteeDao extends ComnDao {

	public SqlResult<TrusteeModel> findTrustee(SqlParam<TrusteeModel> params) throws Exception {
		String sql="SELECT t1.ID, t1.TRUSTEE_CODE, t1.TRUSTEE_NAME, t1.ACCT_NAME, t1.ACCT_NO, t1.TRUSTEE_COUNTRY, t1.TRUSTEE_COUNTRY_NAME, " +
				"	t1.TRUSTEE_ACCT_NAME, t1.TRUSTEE_ACCT_CODE, t1.TRUSTEE_PROPERTY, t1.CRT_USER, t1.UPD_USER, t1.CRT_DT, t1.UPD_DT, t1.DEAL_DATE " +
				" 	FROM ODS_TRUSTEE_BAS_INF t1 WHERE 1=1 " ;
		if (StringUtils.isNotBlank(params.getModel().getTrusteeCode())) {
			sql += " AND t1.TRUSTEE_CODE like '%$U{trusteeCode}%' ";
		}
		if (StringUtils.isNotBlank(params.getModel().getTrusteeName())) {
			sql += " AND t1.TRUSTEE_NAME like '%$U{trusteeName}%' ";
		}
		return super.findRows(sql, params);
	}


	public UpdateResult addTrustee(SqlParam<TrusteeModel> params) throws Exception {
		return super.update("INSERT INTO ods_trustee_bas_inf ( TRUSTEE_CODE, TRUSTEE_NAME, ACCT_NAME, ACCT_NO," +
						" TRUSTEE_COUNTRY, TRUSTEE_COUNTRY_NAME, TRUSTEE_ACCT_NAME, TRUSTEE_ACCT_CODE, TRUSTEE_PROPERTY," +
						" CRT_USER, UPD_USER, CRT_DT, UPD_DT, DEAL_DATE) VALUES (" +
						"$S{trusteeCode},$S{trusteeName},$S{acctName},$S{acctNo},$S{trusteeCountry},$S{trusteeCountryName}," +
						"$S{trusteeAcctName},$S{trusteeAcctCode},$S{trusteeProperty},$S{crtUser},$S{updUser},$S{crtDt}," +
						"$S{updDt},$S{dealDate})",
				params.getModel());
	}

	public UpdateResult updateTrustee(SqlParam<TrusteeModel> params) throws Exception {
		return super.update("UPDATE ods_trustee_bas_inf SET ACCT_NAME = $S{acctName},ACCT_NO = $S{acctNo}," +
						"TRUSTEE_COUNTRY = $S{trusteeCountry},TRUSTEE_COUNTRY_NAME = $S{trusteeCountryName}," +
						"TRUSTEE_ACCT_NAME = $S{trusteeAcctName},TRUSTEE_ACCT_CODE = $S{trusteeAcctCode}," +
						"TRUSTEE_PROPERTY = $S{trusteeProperty},UPD_USER = $S{updUser},UPD_DT = $S{updDt} WHERE ID = $S{id}",
				params.getModel());
	}
	
	public UpdateResult deleteTrustee(SqlParam<TrusteeModel> params) throws Exception {
		return super.update("DELETE FROM ods_trustee_bas_inf WHERE ID=$S{id} ",
				params.getModel());
	}

}
