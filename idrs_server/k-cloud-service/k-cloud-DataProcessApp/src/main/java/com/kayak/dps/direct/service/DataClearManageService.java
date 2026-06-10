package com.kayak.dps.direct.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.direct.dao.DataClearManageDao;
import com.kayak.dps.direct.model.DataClearManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "报备管理信息", model = DataClearManage.class)
public class DataClearManageService {

	@Autowired
	private DataClearManageDao dataClearManageDao;

	@API(desc = "查询报备运营信息", auth = APIAuth.NO)
	public SqlResult<DataClearManage> findDataClearManages(SqlParam<DataClearManage> params) throws Exception {
		return dataClearManageDao.findDataClearManages(params);
	}

	@API(desc = "重跑报送任务", auth = APIAuth.NO)
	public String reBatchClearManage(SqlParam<DataClearManage> params) throws Exception {
		try {
			dataClearManageDao.reBatchClearManage(params);
			return RequestSupport.updateReturnJson(true,  "重跑任务启动成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
		}
	}

}
