package com.kayak.rpt.rhjrjgtj.dao;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.ExeQuery;
import com.kayak.rpt.rhjrjgtj.model.AppAssetA1413DepStruc;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;
import java.util.Map;

@Repository
public class AppAssetA1413DepStrucDao extends ComnDao {

	public SqlResult<AppAssetA1413DepStruc> findAppAssetA1413DepStrucs(SqlParam<AppAssetA1413DepStruc> params) throws Exception {
		return super.findRows("SELECT met_code,met_name,met_bane,report_date FROM app_asset_a1413_dep_struc", params);
	}

	public List<SqlRow> findAppAssetA1413DepStrucsByExeId(Map<String, Object> params, String exeId) throws Exception {
		String sql = ExeQuery.queryExeId(exeId);
		return super.findRows(sql, params);
	}

	public UpdateResult addAppAssetA1413DepStruc(SqlParam<AppAssetA1413DepStruc> params) throws Exception {
		return super.update("INSERT INTO app_asset_a1413_dep_struc(met_code,met_name,met_bane,report_date) VALUES($S{metCode},$S{metName},$D{metBane},$S{reportDate})",
				params.getModel());
	}
	
	public UpdateResult updateAppAssetA1413DepStruc(SqlParam<AppAssetA1413DepStruc> params) throws Exception {
		return super.update("UPDATE app_asset_a1413_dep_struc SET met_code=$S{metCode} ,met_name=$S{metName} ,met_bane=$D{metBane} ,report_date=$S{reportDate}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteAppAssetA1413DepStruc(SqlParam<AppAssetA1413DepStruc> params) throws Exception {
		return super.update("DELETE FROM app_asset_a1413_dep_struc WHERE ",
				params.getModel());
	}

}
