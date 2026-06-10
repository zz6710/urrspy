package com.kayak.subject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.dao.ProdConfigurationScaleDao;
import com.kayak.subject.model.ProdConfigurationScale;

@Service
@APIDefine(desc = "理财产品专户配置比例表服务", model = ProdConfigurationScale.class)
public class ProdConfigurationScaleService {

	@Autowired
	private ProdConfigurationScaleDao prodConfigurationScaleDao;

	@API(desc = "查询理财产品专户配置比例表信息", auth = APIAuth.YES)
	public SqlResult<ProdConfigurationScale> findProdConfigurationScales(SqlParam<ProdConfigurationScale> params) throws Exception {
		return prodConfigurationScaleDao.findProdConfigurationScales(params);
	}

	@API(desc = "添加理财产品专户配置比例表", params = "id,i_code,investmonamount,investownamount,investcountamount,investmonrate,investownrate,investcountrate,act_dt,deal_date", auth = APIAuth.NO)
	public int addProdConfigurationScale(SqlParam<ProdConfigurationScale> params) throws Exception {
		return prodConfigurationScaleDao.addProdConfigurationScale(params).getEffect();
	}
	
	@API(desc = "修改理财产品专户配置比例表", params = "id,i_code,investmonamount,investownamount,investcountamount,investmonrate,investownrate,investcountrate,act_dt,deal_date", auth = APIAuth.NO)
	public int updateProdConfigurationScale(SqlParam<ProdConfigurationScale> params) throws Exception {
		return prodConfigurationScaleDao.updateProdConfigurationScale(params).getEffect();
	}
	
	@API(desc = "删除理财产品专户配置比例表", params = "id,i_code,investmonamount,investownamount,investcountamount,investmonrate,investownrate,investcountrate,act_dt,deal_date", auth = APIAuth.NO)
	public int deleteProdConfigurationScale(SqlParam<ProdConfigurationScale> params) throws Exception {
		return prodConfigurationScaleDao.deleteProdConfigurationScale(params).getEffect();
	}

}
