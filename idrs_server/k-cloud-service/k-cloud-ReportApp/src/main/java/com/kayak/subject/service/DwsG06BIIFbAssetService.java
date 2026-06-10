package com.kayak.subject.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.subject.dao.DwsG06BIIFbAssetInfoDao;
import com.kayak.subject.model.DwsG06BIIFbAssetInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@APIDefine(desc = "G06BII非标资产明细中间表服务", model = DwsG06BIIFbAssetInfo.class)
@Slf4j
public class DwsG06BIIFbAssetService {

	@Autowired
	private DwsG06BIIFbAssetInfoDao dwsG06BIIFbAssetInfoDao;

	@API(desc = "查询G06BII非标资产明细中间表信息", auth = APIAuth.YES)
	public SqlResult<DwsG06BIIFbAssetInfo> findDwsG06BIIFbAssetInfo(SqlParam<DwsG06BIIFbAssetInfo> params) throws Exception {
		return dwsG06BIIFbAssetInfoDao.findDwsG06BIIFbAssetInfo(params);
	}
	
	@API(desc = "修改G06BII非标资产明细中间表信息", auth = APIAuth.YES)
	public String updateDwsG06BIIFbAssetInfo(SqlParam<DwsG06BIIFbAssetInfo> params) throws Exception {
		try{
			dwsG06BIIFbAssetInfoDao.updateDwsG06BIIFbAssetInfo(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		}catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
		}
	}
	
	@API(desc = "删除G06BII非标资产明细中间表信息", auth = APIAuth.YES)
	public String deleteDwsG06BIIFbAssetInfo(SqlParam<DwsG06BIIFbAssetInfo> params) throws Exception {
		try {
			dwsG06BIIFbAssetInfoDao.deleteDwsG06BIIFbAssetInfo(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
		}catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
		}
	}

}
