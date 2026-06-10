package com.kayak.dps.app.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.BondInfoModel;
import com.kayak.dps.app.model.ChinaDebtValuation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ChinaDebtValuationDao extends ComnDao {

	public SqlResult<ChinaDebtValuation> findChinaDebtValuations(SqlParam<ChinaDebtValuation> params) throws Exception {
		String sql = "SELECT d1.*,d2.SCR_NM,d2.SCR_SHT_NM FROM dwd_ast_cnbd_val_inf d1  JOIN  DWD_AST_BND_BAS_INF d2 ON d1.SCR_CD= d2.SCR_CD where 1=1";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(d1.trx_dt) >= DATE($S{startDate}) and DATE(d1.trx_dt) <= DATE($S{endDate})";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addChinaDebtValuation(SqlParam<ChinaDebtValuation> params) throws Exception {
		return super.update("INSERT INTO dwd_ast_cnbd_val_inf(scr_id,scr_cd,trx_mkt,trx_dt,eval_full_prc,acr_intr,eval_net_prc,crt_dt,upd_dt) VALUES" +
						" ('$U{scrCd}.$U{trxMkt}.$U{trxDt}',$S{scrCd},$S{trxMkt},$S{trxDt},$D{evalFullPrc},$D{acrIntr},$D{evalNetPrc},$S{crtDt},$S{updDt})",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult updateChinaDebtValuation(SqlParam<ChinaDebtValuation> params) throws Exception {
		return super.update("UPDATE dwd_ast_cnbd_val_inf SET scr_id='$U{scrCd}.$U{trxMkt}.$U{trxDt}' ,scr_cd=$S{scrCd} ,trx_mkt=$S{trxMkt} ,trx_dt=$S{trxDt} ,cps_prd=$D{cpsPrd} ,eval_full_prc=$D{evalFullPrc} ,acr_intr=$D{acrIntr} ,eval_net_prc=$D{evalNetPrc} ,crt_dt=$S{crtDt} ,upd_dt=$S{updDt}  WHERE scr_id = $S{scrId}",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteChinaDebtValuation(SqlParam<ChinaDebtValuation> params) throws Exception {
		return super.update("DELETE FROM dwd_ast_cnbd_val_inf WHERE scr_id = $S{scrId}",DataSourceProperty.PUB,
				params.getModel());
	}

	// 查询有无中债登估值信息
	public SqlRow findCounts(SqlParam<ChinaDebtValuation> params) throws Exception {
		return super.findRow("select count(1) con from dwd_ast_cnbd_val_inf where SCR_CD = $S{scrCd}  and TRX_MKT = $S{trxMkt} and trx_dt=$S{trxDt} ", DataSourceProperty.PUB, params.getModel());
	}


	public SqlResult<ChinaDebtValuation> findBondInfoCdAndNm(SqlParam<ChinaDebtValuation> params) throws Exception {
		String sql = "SELECT DISTINCT d1.SCR_CD,d2.SCR_SHT_NM FROM dwd_ast_cnbd_val_inf d1  JOIN  DWD_AST_BND_BAS_INF d2 ON d1.SCR_CD= d2.SCR_CD where d1.SCR_CD like '%$U{scrCd}%' or d2.SCR_SHT_NM like '%$U{scrCd}%'";
		return super.findRows(sql,DataSourceProperty.PUB, params);

	}
}
