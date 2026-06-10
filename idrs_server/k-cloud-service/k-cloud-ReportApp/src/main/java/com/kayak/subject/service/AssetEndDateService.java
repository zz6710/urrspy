package com.kayak.subject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.dao.AssetEndDateDao;
import com.kayak.subject.model.AssetEndDate;

@Service
@APIDefine(desc = "资产到期日期清单服务", model = AssetEndDate.class)
public class AssetEndDateService {

	@Autowired
	private AssetEndDateDao assetEndDateDao;

	@API(desc = "查询资产到期日期清单信息", auth = APIAuth.YES)
	public SqlResult<AssetEndDate> findAssetEndDates(SqlParam<AssetEndDate> params) throws Exception {
		return assetEndDateDao.findAssetEndDates(params);
	}

	@API(desc = "添加资产到期日期清单", params = "id,i_code,i_name,asset_third_type,asset_end_date,asset_term_pj,statistic_date,act_dt,deal_date", auth = APIAuth.NO)
	public int addAssetEndDate(SqlParam<AssetEndDate> params) throws Exception {
		return assetEndDateDao.addAssetEndDate(params).getEffect();
	}
	
	@API(desc = "修改资产到期日期清单", params = "id,i_code,i_name,asset_third_type,asset_end_date,asset_term_pj,statistic_date,act_dt,deal_date", auth = APIAuth.NO)
	public int updateAssetEndDate(SqlParam<AssetEndDate> params) throws Exception {
		return assetEndDateDao.updateAssetEndDate(params).getEffect();
	}
	
	@API(desc = "删除资产到期日期清单", params = "id,i_code,i_name,asset_third_type,asset_end_date,asset_term_pj,statistic_date,act_dt,deal_date", auth = APIAuth.NO)
	public int deleteAssetEndDate(SqlParam<AssetEndDate> params) throws Exception {
		return assetEndDateDao.deleteAssetEndDate(params).getEffect();
	}

}
