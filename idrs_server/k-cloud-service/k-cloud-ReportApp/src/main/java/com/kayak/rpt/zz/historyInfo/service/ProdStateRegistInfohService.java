package com.kayak.rpt.zz.historyInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.historyInfo.dao.ProdStateRegistInfohDao;
import com.kayak.rpt.zz.historyInfo.model.ProdStateRegistInfoh;
import com.kayak.rpt.zz.historyInfo.model.ProdTransRegistInfoh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "产品状态信息历史服务", model = ProdStateRegistInfoh.class)
public class ProdStateRegistInfohService {

	@Autowired
	private ProdStateRegistInfohDao prodStateRegistInfohDao;

	@API(desc = "查询产品状态历史信息", auth = APIAuth.YES)
	public SqlResult<ProdStateRegistInfoh> findProdStateRegistInfos(SqlParam<ProdStateRegistInfoh> params) throws Exception {
		params.setMakeSql(true);
		return prodStateRegistInfohDao.findProdStateRegistInfohs(params);
	}

}
