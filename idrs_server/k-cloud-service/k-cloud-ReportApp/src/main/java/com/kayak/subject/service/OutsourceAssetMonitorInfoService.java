package com.kayak.subject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.dao.OutsourceAssetMonitorInfoDao;
import com.kayak.subject.model.OutsourceAssetMonitorInfo;

@Service
@APIDefine(desc = "委外资产检测表服务", model = OutsourceAssetMonitorInfo.class)
public class OutsourceAssetMonitorInfoService {

	@Autowired
	private OutsourceAssetMonitorInfoDao outsourceAssetMonitorInfoDao;

	@API(desc = "查询委外资产检测表信息", auth = APIAuth.YES)
	public SqlResult<OutsourceAssetMonitorInfo> findOutsourceAssetMonitorInfos(SqlParam<OutsourceAssetMonitorInfo> params) throws Exception {
		params.setMakeSql(true);
		return outsourceAssetMonitorInfoDao.findOutsourceAssetMonitorInfos(params);
	}

	@API(desc = "添加委外资产检测表", params = "id,asset_code,asset_name,combined_code,a_type,sponsor_linkman,is_join_data,no_join_reason,moni_result,deviate_amt,input_date,calc_date,deal_date", auth = APIAuth.NO)
	public int addOutsourceAssetMonitorInfo(SqlParam<OutsourceAssetMonitorInfo> params) throws Exception {
		return outsourceAssetMonitorInfoDao.addOutsourceAssetMonitorInfo(params).getEffect();
	}
	
	@API(desc = "修改委外资产检测表", params = "id,asset_code,asset_name,combined_code,a_type,sponsor_linkman,is_join_data,no_join_reason,moni_result,deviate_amt,input_date,calc_date,deal_date", auth = APIAuth.NO)
	public int updateOutsourceAssetMonitorInfo(SqlParam<OutsourceAssetMonitorInfo> params) throws Exception {
		return outsourceAssetMonitorInfoDao.updateOutsourceAssetMonitorInfo(params).getEffect();
	}
	
	@API(desc = "删除委外资产检测表", params = "id,asset_code,asset_name,combined_code,a_type,sponsor_linkman,is_join_data,no_join_reason,moni_result,deviate_amt,input_date,calc_date,deal_date", auth = APIAuth.NO)
	public int deleteOutsourceAssetMonitorInfo(SqlParam<OutsourceAssetMonitorInfo> params) throws Exception {
		return outsourceAssetMonitorInfoDao.deleteOutsourceAssetMonitorInfo(params).getEffect();
	}

}
