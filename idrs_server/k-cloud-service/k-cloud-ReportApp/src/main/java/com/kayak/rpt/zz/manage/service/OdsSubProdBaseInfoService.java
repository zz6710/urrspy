package com.kayak.rpt.zz.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.OdsSubProdBaseInfoDao;
import com.kayak.rpt.zz.manage.model.OdsSubProdBaseInfo;

@Service
@APIDefine(desc = "子产品信息服务", model = OdsSubProdBaseInfo.class)
public class OdsSubProdBaseInfoService {

	@Autowired
	private OdsSubProdBaseInfoDao odsSubProdBaseInfoDao;

	@API(desc = "查询子产品信息信息", auth = APIAuth.YES)
	public SqlResult<OdsSubProdBaseInfo> findOdsSubProdBaseInfos(SqlParam<OdsSubProdBaseInfo> params) throws Exception {
		params.setMakeSql(true);
		return odsSubProdBaseInfoDao.findOdsSubProdBaseInfos(params);
	}

}
