package com.kayak.pms.T8ProdDeal.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.system.SysUtil;
import com.kayak.pms.T8ProdDeal.dao.T8ProdSalesInfoDistributorDao;
import com.kayak.pms.T8ProdDeal.model.T8ProdSalesInfoDistributor;
import com.kayak.utils.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Service
@APIDefine(desc = "销售商份额服务", model = T8ProdSalesInfoDistributor.class)
public class T8ProdSalesInfoDistributorService {

	@Autowired
	private T8ProdSalesInfoDistributorDao t8ProdSalesInfoDistributorDao;

	@API(desc = "查询销售商份额信息", auth = APIAuth.NO)
	public SqlResult<T8ProdSalesInfoDistributor> findT8ProdSalesInfoDistributors(SqlParam<T8ProdSalesInfoDistributor> params) throws Exception {
		params.setMakeSql(true);
		return t8ProdSalesInfoDistributorDao.findT8ProdSalesInfoDistributors(params);
	}

	@API(desc = "添加销售商份额", auth = APIAuth.NO, operation = APIOperation.INSTER)
	public int addT8ProdSalesInfoDistributor(SqlParam<T8ProdSalesInfoDistributor> params) throws Exception {
		String userId = SysUtil.getSysUserParamValue("sys_user_userid").toString();
		String time = DateHelper.getCurrentTime();
		String date = DateHelper.getCurrentDate();
		params.getModel().setCrtUser(userId);
		params.getModel().setCrtDate(date);
		params.getModel().setCrtTime(time);
		params.getModel().setUpdUser(userId);
		params.getModel().setUpdDate(date);
		params.getModel().setUpdTime(time);
		return t8ProdSalesInfoDistributorDao.addT8ProdSalesInfoDistributor(params).getEffect();
	}
	
	@API(desc = "修改销售商份额",operation = APIOperation.INSTER, auth = APIAuth.NO)
	public int updateT8ProdSalesInfoDistributor(SqlParam<T8ProdSalesInfoDistributor> params) throws Exception {
		String userId = SysUtil.getSysUserParamValue("sys_user_userid").toString();
		String time = DateHelper.getCurrentTime();
		String date = DateHelper.getCurrentDate();
		params.getModel().setUpdUser(userId);
		params.getModel().setUpdDate(date);
		params.getModel().setUpdTime(time);
		return t8ProdSalesInfoDistributorDao.updateT8ProdSalesInfoDistributor(params).getEffect();
	}
	
	@API(desc = "删除销售商份额",operation = APIOperation.INSTER, auth = APIAuth.NO)
	public int deleteT8ProdSalesInfoDistributor(SqlParam<T8ProdSalesInfoDistributor> params) throws Exception {
		return t8ProdSalesInfoDistributorDao.deleteT8ProdSalesInfoDistributor(params).getEffect();
	}

}
