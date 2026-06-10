package com.kayak.subject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.dao.DwsZyShcommonCustDao;
import com.kayak.subject.model.DwsZyShcommonCust;

@Service
@APIDefine(desc = "上海国际集团共同客户名录服务", model = DwsZyShcommonCust.class)
public class DwsZyShcommonCustService {

	@Autowired
	private DwsZyShcommonCustDao dwsZyShcommonCustDao;

	@API(desc = "查询上海国际集团共同客户名录信息", auth = APIAuth.YES)
	public SqlResult<DwsZyShcommonCust> findDwsZyShcommonCusts(SqlParam<DwsZyShcommonCust> params) throws Exception {
		return dwsZyShcommonCustDao.findDwsZyShcommonCusts(params);
	}

	@API(desc = "添加上海国际集团共同客户名录", params = "id,cust_name,registernumber,s_info_org_code,s_info_oth_code,s_info_oth_type,cust_number,ne_ind_code,ne_ind_type,s_relevance,act_dt,deal_date", auth = APIAuth.NO)
	public int addDwsZyShcommonCust(SqlParam<DwsZyShcommonCust> params) throws Exception {
		return dwsZyShcommonCustDao.addDwsZyShcommonCust(params).getEffect();
	}

	@API(desc = "修改上海国际集团共同客户名录", params = "id,cust_name,registernumber,s_info_org_code,s_info_oth_code,s_info_oth_type,cust_number,ne_ind_code,ne_ind_type,s_relevance,act_dt,deal_date", auth = APIAuth.NO)
	public int updateDwsZyShcommonCust(SqlParam<DwsZyShcommonCust> params) throws Exception {
		return dwsZyShcommonCustDao.updateDwsZyShcommonCust(params).getEffect();
	}

	@API(desc = "删除上海国际集团共同客户名录", params = "id,cust_name,registernumber,s_info_org_code,s_info_oth_code,s_info_oth_type,cust_number,ne_ind_code,ne_ind_type,s_relevance,act_dt,deal_date", auth = APIAuth.NO)
	public int deleteDwsZyShcommonCust(SqlParam<DwsZyShcommonCust> params) throws Exception {
		return dwsZyShcommonCustDao.deleteDwsZyShcommonCust(params).getEffect();
	}

}
