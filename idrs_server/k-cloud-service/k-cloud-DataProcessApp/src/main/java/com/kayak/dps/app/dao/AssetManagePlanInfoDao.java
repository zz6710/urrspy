package com.kayak.dps.app.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.AssetManagePlanInfo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class AssetManagePlanInfoDao extends ComnDao {

	public SqlResult<AssetManagePlanInfo> findAssetManagePlanInfos(SqlParam<AssetManagePlanInfo> params) throws Exception {
		String sql = "SELECT distinct t.scr_cd,t1.SCR_NM FROM dwd_ast_mng_plan_val_inf t left join  DWD_AST_AST_MNG_PLAN_INF t1 on t.SCR_ID = t1.SCR_ID where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getScrCd())) {
			sql += " and t.scr_cd like '%$U{scrCd}%'";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public SqlResult<AssetManagePlanInfo> findAssetManagePlanInfosByScrCd(SqlParam<AssetManagePlanInfo> params) throws Exception {
		String sql = "SELECT t.SCR_ID,t.scr_cd,t.val_dt,t.unt_val,t1.SCR_NM FROM dwd_ast_mng_plan_val_inf t left join DWD_AST_AST_MNG_PLAN_INF t1 on t.SCR_ID = t1.SCR_ID where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(t.VAL_DT) >= DATE($S{startDate}) and DATE(t.VAL_DT) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getScrCd())) {
			sql += " and t.SCR_CD = $S{scrCd}";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addAssetManagePlanInfo(SqlParam<AssetManagePlanInfo> params) throws Exception {
		return super.update("INSERT INTO dwd_ast_mng_plan_val_inf(scr_id,scr_cd,val_dt,unt_val,crt_dt,upd_dt) VALUES($S{scrId},$S{scrCd},$S{valDt},$D{untVal},$S{SYSDATE},$S{SYSDATE})",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult updateAssetManagePlanInfo(SqlParam<AssetManagePlanInfo> params) throws Exception {
		return super.update("UPDATE dwd_ast_mng_plan_val_inf SET scr_id=$S{scrId} ,scr_cd=$S{scrCd} ,val_dt=$S{valDt} ,unt_val=$D{untVal}  ,upd_dt=$S{SYSDATE}  WHERE scr_cd=$S{scrCd} and val_dt=$S{valDt}  ",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteAssetManagePlanInfo(SqlParam<AssetManagePlanInfo> params) throws Exception {
		return super.update("DELETE FROM dwd_ast_mng_plan_val_inf WHERE scr_cd=$S{scrCd} and val_dt=$S{valDt} ",
				DataSourceProperty.PUB,params.getModel());
	}

    public SqlRow findCounts(SqlParam<AssetManagePlanInfo> params) throws Exception {
		return super.findRow("select count(1) con from dwd_ast_mng_plan_val_inf where SCR_CD = $S{scrCd}  and val_dt=$S{valDt} ", DataSourceProperty.PUB, params.getModel());

	}
}
