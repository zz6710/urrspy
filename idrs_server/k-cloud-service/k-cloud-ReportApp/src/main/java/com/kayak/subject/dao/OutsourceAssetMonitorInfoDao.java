package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.subject.model.OutsourceAssetMonitorInfo;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class OutsourceAssetMonitorInfoDao extends ComnDao {

	public SqlResult<OutsourceAssetMonitorInfo> findOutsourceAssetMonitorInfos(SqlParam<OutsourceAssetMonitorInfo> params) throws Exception {
		return super.findRows("SELECT id,asset_code,asset_name,combined_code,a_type as atype,sponsor_linkman,is_join_data,no_join_reason,moni_result,deviate_amt,input_date,calc_date,deal_date FROM outsource_asset_monitor_info", params);
	}

	public UpdateResult addOutsourceAssetMonitorInfo(SqlParam<OutsourceAssetMonitorInfo> params) throws Exception {
		return super.update("INSERT INTO outsource_asset_monitor_info(id,asset_code,asset_name,combined_code,a_type,sponsor_linkman,is_join_data,no_join_reason,moni_result,deviate_amt,input_date,calc_date,deal_date) VALUES($AUTOIDS{id},$S{assetCode},$S{assetName},$S{combinedCode},$S{atype},$S{sponsorLinkman},$S{isJoinData},$S{noJoinReason},$D{moniResult},$D{deviateAmt},$S{inputDate},$S{calcDate},$S{dealDate})",
				params.getModel());
	}
	
	public UpdateResult updateOutsourceAssetMonitorInfo(SqlParam<OutsourceAssetMonitorInfo> params) throws Exception {
		return super.update("UPDATE outsource_asset_monitor_info SET asset_code=$S{assetCode} ,asset_name=$S{assetName} ,combined_code=$S{combinedCode} ,a_type=$S{atype} ,sponsor_linkman=$S{sponsorLinkman} ,is_join_data=$S{isJoinData} ,no_join_reason=$S{noJoinReason} ,moni_result=$D{moniResult} ,deviate_amt=$D{deviateAmt} ,input_date=$S{inputDate} ,calc_date=$S{calcDate} ,deal_date=$S{dealDate}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteOutsourceAssetMonitorInfo(SqlParam<OutsourceAssetMonitorInfo> params) throws Exception {
		return super.update("DELETE FROM outsource_asset_monitor_info WHERE  id=$S{id} ",
				params.getModel());
	}

}
