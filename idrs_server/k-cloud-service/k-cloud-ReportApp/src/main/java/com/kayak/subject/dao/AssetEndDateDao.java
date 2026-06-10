package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.AssetEndDate;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class AssetEndDateDao extends ComnDao {

	public SqlResult<AssetEndDate> findAssetEndDates(SqlParam<AssetEndDate> params) throws Exception {
		String sql = "SELECT id,i_code as icode,i_name as iname,asset_third_type,asset_end_date,asset_term_pj,statistic_date,act_dt,deal_date FROM dws_asset_end_date where 1=1 ";
		if (Tools.isNotEmpty(params.getModel().getStatisticDate())) {
			sql += " and statistic_date = '" + params.getModel().getStatisticDate() +"'";
		}
		if (Tools.isNotEmpty(params.getModel().getIcode())) {
			sql += " and i_code like '%" + params.getModel().getIcode() +"%'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addAssetEndDate(SqlParam<AssetEndDate> params) throws Exception {
		return super.update("INSERT INTO dws_asset_end_date(id,i_code,i_name,asset_third_type,asset_end_date,asset_term_pj,statistic_date,act_dt,deal_date) VALUES($AUTOIDI{id},$S{icode},$S{iname},$S{assetThirdType},$S{assetEndDate},$S{assetTermPj},$S{statisticDate},$S{actDt},$S{dealDate})",
				params.getModel());
	}
	
	public UpdateResult updateAssetEndDate(SqlParam<AssetEndDate> params) throws Exception {
		return super.update("UPDATE dws_asset_end_date SET i_code=$S{icode} ,i_name=$S{iname} ,asset_third_type=$S{assetThirdType} ,asset_end_date=$S{assetEndDate} ,asset_term_pj=$S{assetTermPj} ,statistic_date=$S{statisticDate} ,act_dt=$S{actDt} ,deal_date=$S{dealDate} ,data_status='02'  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteAssetEndDate(SqlParam<AssetEndDate> params) throws Exception {
		return super.update("DELETE FROM dws_asset_end_date WHERE  id=$I{id} ",
				params.getModel());
	}

}
