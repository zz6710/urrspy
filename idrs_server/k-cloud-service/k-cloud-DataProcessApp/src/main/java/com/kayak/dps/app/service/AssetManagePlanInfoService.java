package com.kayak.dps.app.service;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.ods.dao.NetValSPVInfoModelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.app.dao.AssetManagePlanInfoDao;
import com.kayak.dps.app.model.AssetManagePlanInfo;

import java.util.List;

@Service
@APIDefine(desc = "净值SPV估值信息服务", model = AssetManagePlanInfo.class)
public class AssetManagePlanInfoService {
	@Autowired
	private AssetManagePlanInfoDao assetManagePlanInfoDao;
	@Autowired
	private NetValSPVInfoModelDao netValSPVInfoModelDao;


	public SqlResult<AssetManagePlanInfo> findAssetManagePlanInfos(SqlParam<AssetManagePlanInfo> params) throws Exception {
		params.setMakeSql(false);
		SqlResult<AssetManagePlanInfo> result = assetManagePlanInfoDao.findAssetManagePlanInfos(params);
		return result;
	}

	@API(desc = "查询净值SPV估值信息信息", auth = APIAuth.YES)
	public SqlResult<AssetManagePlanInfo> findAssetManagePlanInfosByScrCd(SqlParam<AssetManagePlanInfo> params) throws Exception {
		params.setMakeSql(false);
		SqlResult<AssetManagePlanInfo> result = assetManagePlanInfoDao.findAssetManagePlanInfosByScrCd(params);
		return result;
	}

	@API(desc = "添加净值SPV估值信息", params = "scr_id,scr_cd,val_dt,unt_val,crt_dt,upd_dt", auth = APIAuth.YES)
	public String addAssetManagePlanInfo(SqlParam<AssetManagePlanInfo> params) throws Exception {
		try {
			assetManagePlanInfoDao.addAssetManagePlanInfo(params);
			return RequestSupport.updateReturnJson(true,  "添加成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "添加失败,有相同净值SPV估值信息！", null).toString();
		}
	}



	@API(desc = "修改净值SPV估值信息", params = "scr_id,scr_cd,val_dt,unt_val,crt_dt,upd_dt", auth = APIAuth.YES)
	public String updateAssetManagePlanInfo(SqlParam<AssetManagePlanInfo> params) throws Exception {
		try {
			assetManagePlanInfoDao.updateAssetManagePlanInfo(params);
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败,有相同净值SPV估值信息！", null).toString();
		}
	}
	
	@API(desc = "删除净值SPV估值信息", params = "scr_id,scr_cd,val_dt,unt_val,crt_dt,upd_dt", auth = APIAuth.YES)
	public int deleteAssetManagePlanInfo(SqlParam<AssetManagePlanInfo> params) throws Exception {
		return assetManagePlanInfoDao.deleteAssetManagePlanInfo(params).getEffect();
	}

}
