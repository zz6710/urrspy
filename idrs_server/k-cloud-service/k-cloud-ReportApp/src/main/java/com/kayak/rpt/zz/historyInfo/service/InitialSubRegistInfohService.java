package com.kayak.rpt.zz.historyInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.historyInfo.dao.InitialSubRegistInfohDao;
import com.kayak.rpt.zz.historyInfo.model.InitialSubRegistInfoh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "产品募集总量历史信息服务", model = InitialSubRegistInfoh.class)
public class InitialSubRegistInfohService {

	@Autowired
	private InitialSubRegistInfohDao initialSubRegistInfohDao;

	@API(desc = "查询产品募集总量历史信息信息", auth = APIAuth.YES)
	public SqlResult<InitialSubRegistInfoh> findInitialSubRegistInfos(SqlParam<InitialSubRegistInfoh> params) throws Exception {
		params.setMakeSql(true);
		return initialSubRegistInfohDao.findInitialSubRegistInfohs(params);
	}

	@API(desc = "添加产品募集总量历史信息", params = "bank_code,prod_code,number_indiv_invest,number_corpor_invest,number_ucor_invest,other_distribut_agents,details,register_serno,imp_date,register_date,register_status,actual_subscribed_amt,subscribed_vol,amt_other_db_agents", auth = APIAuth.NO)
	public int addInitialSubRegistInfo(SqlParam<InitialSubRegistInfoh> params) throws Exception {
		return initialSubRegistInfohDao.addInitialSubRegistInfoh(params).getEffect();
	}
	
	@API(desc = "修改产品募集总量历史信息", params = "bank_code,prod_code,number_indiv_invest,number_corpor_invest,number_ucor_invest,other_distribut_agents,details,register_serno,imp_date,register_date,register_status,actual_subscribed_amt,subscribed_vol,amt_other_db_agents", auth = APIAuth.NO)
	public int updateInitialSubRegistInfo(SqlParam<InitialSubRegistInfoh> params) throws Exception {
		return initialSubRegistInfohDao.updateInitialSubRegistInfoh(params).getEffect();
	}
	
	@API(desc = "删除产品募集总量历史信息", params = "bank_code,prod_code,number_indiv_invest,number_corpor_invest,number_ucor_invest,other_distribut_agents,details,register_serno,imp_date,register_date,register_status,actual_subscribed_amt,subscribed_vol,amt_other_db_agents", auth = APIAuth.NO)
	public int deleteInitialSubRegistInfo(SqlParam<InitialSubRegistInfoh> params) throws Exception {
		return initialSubRegistInfohDao.deleteInitialSubRegistInfoh(params).getEffect();
	}

}
