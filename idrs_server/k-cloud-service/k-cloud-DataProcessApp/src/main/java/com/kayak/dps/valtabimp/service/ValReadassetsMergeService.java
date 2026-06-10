package com.kayak.dps.valtabimp.service;

import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.valtabimp.model.ValReadassetsMerge;
import com.kayak.dps.valtabimp.repository.ValReadassetsMergeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "解析配置合并服务", model = ValReadassetsMerge.class)
public class ValReadassetsMergeService {

	@Autowired
	private ValReadassetsMergeDao valReadassetsMergeDao;

	@API(desc = "查询资产信息", auth = APIAuth.NO)
	public SqlResult<ValReadassetsMerge> findAssetCode(SqlParam<ValReadassetsMerge> params) throws Exception {
		params.setMakeSql(false);
		return valReadassetsMergeDao.findAssetCode(params);
	}

	@API(desc = "查询关联类型信息", auth = APIAuth.NO)
	public SqlResult<ValReadassetsMerge> findisprodorassetList(SqlParam<ValReadassetsMerge> params) throws Exception {
		params.setMakeSql(false);
		return valReadassetsMergeDao.findisprodorassetList(params);
	}
	@API(desc = "查询解析配置合并信息", auth = APIAuth.NO)
	public SqlResult<ValReadassetsMerge> findValReadassetsMerges(SqlParam<ValReadassetsMerge> params) throws Exception {
		params.setMakeSql(true);
		return valReadassetsMergeDao.findValReadassetsMerges(params);
	}

	@API(desc = "添加解析配置合并", params = "id,t8_prod_base_id,change_date,ftool_code,ftool_name,t8_sys_adtype_id,market,account_type,positionbln,principalbln,interestbln,accruedincomebln,npamountbln,feepaybln,fairvaluebln,taxfeebln,pay_taxbln,accruedpaybln,securitiesliquidationbln,jrjs_value,inputuser,crt_date,crt_time,isprodorasset,prod_name,balance", auth = APIAuth.NO)
	public int addValReadassetsMerge(SqlParam<ValReadassetsMerge> params) throws Exception {
		return valReadassetsMergeDao.addValReadassetsMerge(params).getEffect();
	}
	
	@API(desc = "修改解析配置合并", params = "id,t8_prod_base_id,change_date,ftool_code,ftool_name,t8_sys_adtype_id,market,account_type,positionbln,principalbln,interestbln,accruedincomebln,npamountbln,feepaybln,fairvaluebln,taxfeebln,pay_taxbln,accruedpaybln,securitiesliquidationbln,jrjs_value,inputuser,crt_date,crt_time,isprodorasset,prod_name,balance", auth = APIAuth.NO)
	public int updateValReadassetsMerge(SqlParam<ValReadassetsMerge> params) throws Exception {
		return valReadassetsMergeDao.updateValReadassetsMerge(params).getEffect();
	}
	
	@API(desc = "删除解析配置合并", params = "id,t8_prod_base_id,change_date,ftool_code,ftool_name,t8_sys_adtype_id,market,account_type,positionbln,principalbln,interestbln,accruedincomebln,npamountbln,feepaybln,fairvaluebln,taxfeebln,pay_taxbln,accruedpaybln,securitiesliquidationbln,jrjs_value,inputuser,crt_date,crt_time,isprodorasset,prod_name,balance", auth = APIAuth.NO)
	public int deleteValReadassetsMerge(SqlParam<ValReadassetsMerge> params) throws Exception {
		return valReadassetsMergeDao.deleteValReadassetsMerge(params).getEffect();
	}
	@API(desc = "批量删除", auth = APIAuth.YES)
	public String batchDeleteValReadassetsMerge(SqlParam<Object> object) throws Exception {
		Map<String, Object> paramsDirect = RequestSupport.getParameters();
		String obj = (String) paramsDirect.get("list");
		List<Map<String,Object>> pubInfoList = new ArrayList<>();
		List<Map> jsonList = JSONObject.parseArray(obj, Map.class);
		Map<String, Object> params=new HashMap<String, Object>();
		for (int i = 0; i <jsonList.size() ; i++) {
			params.put("id",jsonList.get(i).get("id"));
			valReadassetsMergeDao.batchDeleteValReadassetsMerge(params);
		}
		return RequestSupport.updateReturnJson(true, "操作成功!", null).toString();
	}

	@API(desc = "导入", auth = APIAuth.YES)
	public void dataImport() throws Exception {}

	@API(desc = "导出", auth = APIAuth.YES)
	public void dataExport() throws Exception {}

}
