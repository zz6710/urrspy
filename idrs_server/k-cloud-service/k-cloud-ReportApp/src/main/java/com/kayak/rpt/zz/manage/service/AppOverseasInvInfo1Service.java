package com.kayak.rpt.zz.manage.service;

import com.kayak.core.sql.SqlRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.AppOverseasInvInfo1Dao;
import com.kayak.rpt.zz.manage.model.AppOverseasInvInfo1;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "境外投资情况明细表总体收益情况1服务", model = AppOverseasInvInfo1.class)
public class AppOverseasInvInfo1Service {

	@Autowired
	private AppOverseasInvInfo1Dao appOverseasInvInfo1Dao;

	@API(desc = "查询境外投资情况明细表总体收益情况1信息", auth = APIAuth.NO)
	public SqlResult<AppOverseasInvInfo1> findAppOverseasInvInfo1s(SqlParam<AppOverseasInvInfo1> params) throws Exception {
		params.setMakeSql(true);
		return appOverseasInvInfo1Dao.findAppOverseasInvInfo1s(params);
	}

	public List<SqlRow> findAppOverseasInvInfo1s(Map<String, Object> params) throws Exception {
		return appOverseasInvInfo1Dao.findAppOverseasInvInfo1s(params);
	}

	@API(desc = "添加境外投资情况明细表总体收益情况1", params = "report_date,comp_nm,fund_type,inv_asst_mkt,gro_rate", auth = APIAuth.NO)
	public int addAppOverseasInvInfo1(SqlParam<AppOverseasInvInfo1> params) throws Exception {
		return appOverseasInvInfo1Dao.addAppOverseasInvInfo1(params).getEffect();
	}
	
	@API(desc = "修改境外投资情况明细表总体收益情况1", params = "report_date,comp_nm,fund_type,inv_asst_mkt,gro_rate", auth = APIAuth.NO)
	public int updateAppOverseasInvInfo1(SqlParam<AppOverseasInvInfo1> params) throws Exception {
		return appOverseasInvInfo1Dao.updateAppOverseasInvInfo1(params).getEffect();
	}
	
	@API(desc = "删除境外投资情况明细表总体收益情况1", params = "report_date,comp_nm,fund_type,inv_asst_mkt,gro_rate", auth = APIAuth.NO)
	public int deleteAppOverseasInvInfo1(SqlParam<AppOverseasInvInfo1> params) throws Exception {
		return appOverseasInvInfo1Dao.deleteAppOverseasInvInfo1(params).getEffect();
	}

}
