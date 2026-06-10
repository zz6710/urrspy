package com.kayak.rpt.zz.errorInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.ProdStateRgInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.ProdStateRgInfoErr;
import com.kayak.rpt.zz.errorInfo.model.ProdTransRgInfoErr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "交易信息错误服务", model = ProdStateRgInfoErr.class)
public class ProdStateRgInfoErrService {

	@Autowired
	private ProdStateRgInfoErrDao prodStateRgInfoErrDao;

	@API(desc = "查询产品状态错误信息", auth = APIAuth.YES)
	public SqlResult<ProdStateRgInfoErr> findProdStateRgInfoErrs(SqlParam<ProdStateRgInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return prodStateRgInfoErrDao.findProdStateRgInfoErrs(params);
	}

}
