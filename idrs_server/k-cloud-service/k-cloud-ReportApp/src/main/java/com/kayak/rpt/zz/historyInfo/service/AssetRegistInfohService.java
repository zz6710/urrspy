package com.kayak.rpt.zz.historyInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.historyInfo.dao.AssetRegistInfohDao;
import com.kayak.rpt.zz.historyInfo.model.AssetRegistInfoh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "底层资产历史信息服务", model = AssetRegistInfoh.class)
public class AssetRegistInfohService {

	@Autowired
	private AssetRegistInfohDao assetRegistInfohDao;

	@API(desc = "查询底层资产历史信息信息", auth = APIAuth.YES)
	public SqlResult<AssetRegistInfoh> findAssetRegistInfos(SqlParam<AssetRegistInfoh> params) throws Exception {
		params.setMakeSql(true);
		return assetRegistInfohDao.findAssetRegistInfohs(params);
	}

}
