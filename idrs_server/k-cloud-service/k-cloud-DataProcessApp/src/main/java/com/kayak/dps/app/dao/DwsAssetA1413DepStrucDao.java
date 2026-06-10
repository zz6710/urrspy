package com.kayak.dps.app.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.DwsAssetA1413DepStruc;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwsAssetA1413DepStrucDao extends ComnDao {

	public SqlResult<DwsAssetA1413DepStruc> findDwsAssetA1413DepStrucs(SqlParam<DwsAssetA1413DepStruc> params) throws Exception {
		return super.findRows("SELECT id,org_one,org_one_two,org_two_thr,org_thr,dom_one,dom_one_two,dom_two_thr,dom_thr,cra_dep,cra_inv,act_dt,deal_date FROM dws_asset_a1413_dep_struc", params);
	}

	public SqlResult<DwsAssetA1413DepStruc> findDwsAssetA1413DepStrucsByActDt(SqlParam<DwsAssetA1413DepStruc> params) throws Exception {
		return super.findRows("SELECT id,org_one,org_one_two,org_two_thr,org_thr,dom_one,dom_one_two,dom_two_thr,dom_thr,cra_dep,cra_inv,act_dt,deal_date FROM dws_asset_a1413_dep_struc where act_dt=$S{actDt}", params);
	}

	public SqlResult<DwsAssetA1413DepStruc> findBaseDate(SqlParam<DwsAssetA1413DepStruc> params) throws Exception {
		return super.findRows("select date_format(last_day(date_add(concat($S{actDt}, '02'), interval 1 month)), '%Y%m%02') as act_dt", params);
	}

	public UpdateResult addDwsAssetA1413DepStruc(SqlParam<DwsAssetA1413DepStruc> params) throws Exception {
		return super.update("INSERT INTO dws_asset_a1413_dep_struc(id,org_one,org_one_two,org_two_thr,org_thr,dom_one,dom_one_two,dom_two_thr,dom_thr,cra_dep,cra_inv,act_dt,deal_date) VALUES($AUTOIDI{id},$D{orgOne},$D{orgOneTwo},$D{orgTwoThr},$D{orgThr},$D{domOne},$D{domOneTwo},$D{domTwoThr},$D{domThr},$D{craDep},$D{craInv},$S{actDt},$S{dealDate})",
				params.getModel());
	}
	
	public UpdateResult updateDwsAssetA1413DepStruc(SqlParam<DwsAssetA1413DepStruc> params) throws Exception {
		return super.update("UPDATE dws_asset_a1413_dep_struc SET org_one=$D{orgOne} ,org_one_two=$D{orgOneTwo} ,org_two_thr=$D{orgTwoThr} ,org_thr=$D{orgThr} ,dom_one=$D{domOne} ,dom_one_two=$D{domOneTwo} ,dom_two_thr=$D{domTwoThr} ,dom_thr=$D{domThr} ,cra_dep=$D{craDep} ,cra_inv=$D{craInv} ,act_dt=$S{actDt} ,deal_date=$S{dealDate}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsAssetA1413DepStruc(SqlParam<DwsAssetA1413DepStruc> params) throws Exception {
		return super.update("DELETE FROM dws_asset_a1413_dep_struc WHERE  id=$I{id} ",
				params.getModel());
	}

}
