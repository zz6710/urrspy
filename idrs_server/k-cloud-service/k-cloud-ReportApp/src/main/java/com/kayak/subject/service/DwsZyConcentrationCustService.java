package com.kayak.subject.service;

import com.kayak.subject.dao.DwsZyConcentrationCustDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.model.DwsZyConcentrationCust;

@Service
@APIDefine(desc = "客户集中度排序服务", model = DwsZyConcentrationCust.class)
public class DwsZyConcentrationCustService {

	@Autowired
	private DwsZyConcentrationCustDao dwsZyConcentrationCustDao;

	@API(desc = "查询客户集中度排序信息", auth = APIAuth.YES)
	public SqlResult<DwsZyConcentrationCust> findDwsZyConcentrationCusts(SqlParam<DwsZyConcentrationCust> params) throws Exception {
		return dwsZyConcentrationCustDao.findDwsZyConcentrationCusts(params);
	}

	@API(desc = "添加客户集中度排序", params = "id,xh,cust_name,s_type,act_dt,deal_date,indu_name,region_name", auth = APIAuth.NO)
	public int addDwsZyConcentrationCust(SqlParam<DwsZyConcentrationCust> params) throws Exception {
		return dwsZyConcentrationCustDao.addDwsZyConcentrationCust(params).getEffect();
	}

	@API(desc = "修改客户集中度排序", params = "id,xh,cust_name,s_type,act_dt,deal_date,indu_name,region_name", auth = APIAuth.NO)
	public int updateDwsZyConcentrationCust(SqlParam<DwsZyConcentrationCust> params) throws Exception {
		return dwsZyConcentrationCustDao.updateDwsZyConcentrationCust(params).getEffect();
	}

	@API(desc = "删除客户集中度排序", params = "id,xh,cust_name,s_type,act_dt,deal_date,indu_name,region_name", auth = APIAuth.NO)
	public int deleteDwsZyConcentrationCust(SqlParam<DwsZyConcentrationCust> params) throws Exception {
		return dwsZyConcentrationCustDao.deleteDwsZyConcentrationCust(params).getEffect();
	}

}
