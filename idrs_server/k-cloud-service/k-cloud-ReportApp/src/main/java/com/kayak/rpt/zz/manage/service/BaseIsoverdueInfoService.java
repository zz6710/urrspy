package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.zz.manage.model.BaseIsoverdueInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.BaseIsoverdueInfoDao;

import java.util.HashMap;
import java.util.Map;

@Service
@APIDefine(desc = "逾期资产维护服务", model = BaseIsoverdueInfo.class)
public class BaseIsoverdueInfoService {

	@Autowired
	private BaseIsoverdueInfoDao baseIsoverdueInfoDao;

	@API(desc = "查询逾期资产维护信息", auth = APIAuth.YES)
	public SqlResult<BaseIsoverdueInfo> findBaseIsoverdueInfos(SqlParam<BaseIsoverdueInfo> params) throws Exception {
		params.setMakeSql(true);
		return baseIsoverdueInfoDao.findBaseIsoverdueInfos(params);
	}

	@API(desc = "添加逾期资产维护", params = "id,bond_code,bond_name,asset_type,bond_mkt,create_dt", auth = APIAuth.YES)
	public String addBaseIsoverdueInfo(SqlParam<BaseIsoverdueInfo> params) throws Exception {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("bondCode", params.getModel().getBondCode());
		queryParams.put("bondName", params.getModel().getBondName());
		queryParams.put("bondMkt", params.getModel().getBondMkt());
		SqlParam<BaseIsoverdueInfo> findParams = new FetcherData<>(queryParams, BaseIsoverdueInfo.class);
		SqlResult<BaseIsoverdueInfo> sqlResult = findBaseIsoverdueInfos(findParams);
		if (CollectionUtil.isNotEmpty(sqlResult.getRows())) {
			return RequestSupport.updateReturnJson(false, "当前添加的【资产代码】、【资产名称】、【交易市场】已存在，请检查", null).toString();
		}
		baseIsoverdueInfoDao.addBaseIsoverdueInfo(params).getEffect();
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}
	
	@API(desc = "修改逾期资产维护", params = "id,bond_code,bond_name,asset_type,bond_mkt,create_dt", auth = APIAuth.YES)
	public int updateBaseIsoverdueInfo(SqlParam<BaseIsoverdueInfo> params) throws Exception {
		return baseIsoverdueInfoDao.updateBaseIsoverdueInfo(params).getEffect();
	}
	
	@API(desc = "删除逾期资产维护", params = "id,bond_code,bond_name,asset_type,bond_mkt,create_dt", auth = APIAuth.YES)
	public int deleteBaseIsoverdueInfo(SqlParam<BaseIsoverdueInfo> params) throws Exception {
		return baseIsoverdueInfoDao.deleteBaseIsoverdueInfo(params).getEffect();
	}

	@API(desc = "查询逾期资产名称", auth = APIAuth.NO)
	public String getBondName(SqlParam<BaseIsoverdueInfo> params) throws Exception {
		Map<String, Object> returnData = new HashMap<>();
		returnData.put("bondName", "");
		SqlResult<BaseIsoverdueInfo> sqlResult = baseIsoverdueInfoDao.findBondName(params);
		if (CollectionUtil.isNotEmpty(sqlResult.getRows())) {
			returnData.put("bondName", sqlResult.getRows().get(0).getBondName());
		}
		return RequestSupport.updateReturnJson(true, "查询逾期资产名称", returnData).toString();
	}

}
