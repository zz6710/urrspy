package com.kayak.dps.direct.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.direct.dao.DataReportManageDao;
import com.kayak.dps.direct.model.DataReportManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "报备管理信息", model = DataReportManage.class)
public class DataReportManageService {

	@Autowired
	private DataReportManageDao dataReportManageDao;

	@API(desc = "查询报备管理信息", auth = APIAuth.NO)
	public SqlResult<DataReportManage> findDataReportManages(SqlParam<DataReportManage> params) throws Exception {
		return dataReportManageDao.findDataReportManages(params);
	}

}
