package com.kayak.dps.app.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.ChinaDebtValuation;
import com.kayak.dps.app.model.SecuritiesValuationInformation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class SecuritiesValuationInformationDao extends ComnDao {

	public SqlResult<SecuritiesValuationInformation> findSecuritiesValuationInformations(SqlParam<SecuritiesValuationInformation> params) throws Exception {
		String sql = "SELECT d1.*,d2.SCR_NM,d2.SCR_SHT_NM FROM dwd_ast_csi_val_inf d1 JOIN  DWD_AST_BND_BAS_INF d2 ON d1.SCR_CD= d2.SCR_CD where 1=1";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(d1.trx_dt) >= DATE($S{startDate}) and DATE(d1.trx_dt) <= DATE($S{endDate})";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addSecuritiesValuationInformation(SqlParam<SecuritiesValuationInformation> params) throws Exception {
		return super.update("INSERT INTO dwd_ast_csi_val_inf(scr_id,scr_cd,trx_mkt,trx_dt,calc_prc,net_prc,acr_intr,crt_dt,upd_dt) VALUES('$U{scrCd}.$U{trxMkt}.$U{trxDt}',$S{scrCd},$S{trxMkt},$S{trxDt},$D{calcPrc},$D{netPrc},$D{acrIntr},$S{crtDt},$S{updDt})",
				params.getModel());
	}
	
	public UpdateResult updateSecuritiesValuationInformation(SqlParam<SecuritiesValuationInformation> params) throws Exception {
		return super.update("UPDATE dwd_ast_csi_val_inf SET scr_id='$U{scrCd}.$U{trxMkt}.$U{trxDt}' ,scr_cd=$S{scrCd} ,trx_mkt=$S{trxMkt} ,trx_dt=$S{trxDt} ,calc_prc=$D{calcPrc} ,net_prc=$D{netPrc} ,acr_intr=$D{acrIntr} ,crt_dt=$S{crtDt} ,upd_dt=$S{updDt}  WHERE  scr_id=$S{scrId} ",
				params.getModel());
	}
	
	public UpdateResult deleteSecuritiesValuationInformation(SqlParam<SecuritiesValuationInformation> params) throws Exception {
		return super.update("DELETE FROM dwd_ast_csi_val_inf WHERE scr_id=$S{scrId}  ",
				params.getModel());
	}

	// 查询有无中证估值信息
	public SqlRow findCounts(SqlParam<SecuritiesValuationInformation> params) throws Exception {
		return super.findRow("select count(1) con from dwd_ast_csi_val_inf where SCR_CD = $S{scrCd}  and TRX_MKT = $S{trxMkt} and trx_dt=$S{trxDt} ", DataSourceProperty.PUB, params.getModel());
	}

	public SqlResult<SecuritiesValuationInformation> findBondInfoCdAndNm(SqlParam<SecuritiesValuationInformation> params) throws Exception {
		String sql = "SELECT DISTINCT d1.SCR_CD,d2.SCR_SHT_NM FROM dwd_ast_csi_val_inf d1  JOIN  DWD_AST_BND_BAS_INF d2 ON d1.SCR_CD= d2.SCR_CD where d1.SCR_CD like '%$U{scrCd}%' or d2.SCR_SHT_NM like '%$U{scrCd}%'";
		return super.findRows(sql,DataSourceProperty.PUB, params);

	}

}
