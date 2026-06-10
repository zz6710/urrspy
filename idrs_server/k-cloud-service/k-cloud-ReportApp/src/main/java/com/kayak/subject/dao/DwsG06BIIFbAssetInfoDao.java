package com.kayak.subject.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsCounterPartyInfo;
import com.kayak.subject.model.DwsG06BIIFbAssetInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DwsG06BIIFbAssetInfoDao extends ComnDao {

	public SqlResult<DwsG06BIIFbAssetInfo> findDwsG06BIIFbAssetInfo(SqlParam<DwsG06BIIFbAssetInfo> params) throws Exception {
	    String sql = "select id,prod_code,asset_code,asset_thr_code,asset_name,net_value,g06_type,non_place,fin_ent,fin_ent_cd,fin_ent_rt,fin_ent_dt,if_ple, \n" +
				     "       per_gua_rat,if_full_ple,if_war,if_full_war,war_nm,war_cd,war_rt,war_dt,if_cre,non_asset_type,report_date  \n" +
				     "  from dws_g06b_non_info gn  \n" +
				     " where 1 = 1 ";
		if (Tools.isNotEmpty(params.getModel().getReportDate())) {
			sql += " and report_date like '" + params.getModel().getReportDate() + "%'";
		}
		if (Tools.isNotEmpty(params.getModel().getProdCode())) {
            sql += " and prod_code like '%" + params.getModel().getProdCode() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getAssetCode())) {
			sql += " and asset_code = '" + params.getModel().getAssetCode() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getG06Type())) {
			sql += " and g06_type = '" + params.getModel().getG06Type() + "'";
		}
		sql += " order by report_date desc ";
        return super.findRows(sql, params);
	}

	
	public UpdateResult updateDwsG06BIIFbAssetInfo(SqlParam<DwsG06BIIFbAssetInfo> params) throws Exception {
		return super.update("update dws_g06b_non_info " +
						" set prod_code = $S{prodCode}, \n" +
						"     asset_code = $S{assetCode}, \n" +
						"     asset_thr_code = $S{assetThrCode}, \n" +
						"     asset_name = $S{assetName}, \n" +
						"     net_value = $S{netValue}, \n" +
						"     g06_type = $S{g06Type}, \n" +
						"     non_place = $S{nonPlace}, \n" +
						"     fin_ent = $S{finEnt}, \n" +
						"     fin_ent_cd = $S{finEntCd}, \n" +
						"     fin_ent_rt = $S{finEntRt}, \n" +
						"     fin_ent_dt = $S{finEntDt}, \n" +
						"     if_ple = $S{ifPle}, \n" +
						"     per_gua_rat = $S{perGuaRat}, \n" +
						"     if_full_ple = $S{ifFullPle}, \n" +
						"     if_war = $S{ifWar}, \n" +
						"     if_full_war = $S{ifFullWar}, \n" +
						"     war_nm = $S{warNm}, \n" +
						"     war_cd = $S{warCd}, \n" +
						"     war_rt = $S{warRt}, \n" +
						"     war_dt = $S{warDt}, \n" +
						"     if_cre = $S{ifCre}, \n" +
						"     non_asset_type = $S{nonAssetType} \n" +
						" where id = $S{id}",
				params.getModel());
	}
	
	public UpdateResult deleteDwsG06BIIFbAssetInfo(SqlParam<DwsG06BIIFbAssetInfo> params) throws Exception {
		return super.update("DELETE FROM dws_g06b_non_info WHERE id = $S{id}", params.getModel());
	}

}
