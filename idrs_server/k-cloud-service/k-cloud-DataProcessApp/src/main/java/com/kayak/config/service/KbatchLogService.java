package com.kayak.config.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.config.dao.KbatchLogDao;
import com.kayak.config.model.KbatchLog;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.app.model.FundInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件名: Ta5004Service.java
 * 描述:  清算日志
 * 创建人: zengzt
 * 创建时间:2020年4月27日上午11:04:37
 */
@Service
@APIDefine(desc = "清算日志", model = KbatchLog.class)
public class KbatchLogService {

	@Autowired
	private KbatchLogDao kbatchLogDao;

	@API(desc = "查询清算日志信息", auth = APIAuth.YES)
	public SqlResult<KbatchLog> findBatchLog(SqlParam<KbatchLog> params) throws Exception {
		
		//是否自动追加参数
		params.setMakeSql(false);
		return kbatchLogDao.queryBatchLog(params);
		
	}
	
}
