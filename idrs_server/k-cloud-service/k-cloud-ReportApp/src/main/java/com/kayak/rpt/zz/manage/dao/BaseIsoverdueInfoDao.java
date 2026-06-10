package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.BaseIsoverdueInfo;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class BaseIsoverdueInfoDao extends ComnDao {

	public SqlResult<BaseIsoverdueInfo> findBaseIsoverdueInfos(SqlParam<BaseIsoverdueInfo> params) throws Exception {
		return super.findRows("SELECT id,bond_code,bond_name,asset_type,bond_mkt,create_dt FROM base_isoverdue_info", params);
	}

	public UpdateResult addBaseIsoverdueInfo(SqlParam<BaseIsoverdueInfo> params) throws Exception {
		return super.update("INSERT INTO base_isoverdue_info(bond_code,bond_name,asset_type,bond_mkt,create_dt) VALUES($S{bondCode},$S{bondName},$S{assetType},$S{bondMkt},DATE_FORMAT(now(),'%Y%m%d'))",
				params.getModel());
	}
	
	public UpdateResult updateBaseIsoverdueInfo(SqlParam<BaseIsoverdueInfo> params) throws Exception {
		return super.update("UPDATE base_isoverdue_info SET bond_code=$S{bondCode} ,bond_name=$S{bondName} ,asset_type=$S{assetType} ,bond_mkt=$S{bondMkt} ,create_dt=DATE_FORMAT(now(),'%Y%m%d')  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteBaseIsoverdueInfo(SqlParam<BaseIsoverdueInfo> params) throws Exception {
		return super.update("DELETE FROM base_isoverdue_info WHERE  id=$I{id} ",
				params.getModel());
	}

	public SqlResult<BaseIsoverdueInfo> findBondName(SqlParam<BaseIsoverdueInfo> params) throws Exception {
		StringBuffer sql = new StringBuffer();
		if ("01".equals(params.getModel().getAssetType())) {
			sql.append("SELECT bnds_nm AS bond_name FROM stg_t10_cn_bond_desc_h WHERE orgn_wind_cd = CONCAT($S{bondCode}, '.', $S{bondMkt}) LIMIT 1");
		} else if ("02".equals(params.getModel().getAssetType())) {
			sql.append("SELECT fnd_fll_nm AS bond_name FROM stg_t10_cn_mtl_fund_desc_h WHERE wind_cd = CONCAT($S{bondCode}, '.', $S{bondMkt}) LIMIT 1");
		} else if ("03".equals(params.getModel().getAssetType())) {
			sql.append("SELECT stck_chns_fll_nm AS bond_name FROM stg_t10_cn_hk_share_desc_h WHERE wind_cd = CONCAT($S{bondCode}, '.', $S{bondMkt}) LIMIT 1");
		} else {
			sql.append("SELECT CASE WHEN ass_debt_type IN ('1002', '1003', '1004', '1005') THEN bb_deposit_bank " + // 现金及银行存款
					"               WHEN ass_debt_type IN ('2501', '2502') THEN cc_name " + // 同业存单
					"               WHEN ass_debt_type IN ('1201', '1210', '1202', '1203', '1204', '1205', '1206', '1207', '1208', '1209', '1211', '1212', '1213', '2101', '2202', '1299') THEN ee_name " + // 非标准化债权类资产
					"               WHEN ass_debt_type IN ('1301', '1399') THEN gg_name " + // 权益类资产
					"               WHEN ass_debt_type IN ('1401', '1402', '1403', '1404', '1405', '1406', '1499') THEN hh_name " + // 金融衍生品
					"               WHEN ass_debt_type IN ('1501') THEN ii_bond_name " + // QDII债券资产
					"               WHEN ass_debt_type IN ('1507', '1508', '1509', '1505', '1502') THEN kk_name " + // QDII股票/基金类资产
					"               WHEN ass_debt_type IN ('1503', '1599') THEN ll_contract_name " + // QDII结构性票据类资产
					"               WHEN ass_debt_type IN ('1701', '1703', '1704', '1706', '1702', '1705') THEN mm_manage_plan_name " + // 资产管理产品
					"           END AS bond_name FROM app_asset_debt_register_info WHERE asset_code = $S{bondCode} LIMIT 1");
		}
		return super.findRows(sql.toString(), params);
	}

}
