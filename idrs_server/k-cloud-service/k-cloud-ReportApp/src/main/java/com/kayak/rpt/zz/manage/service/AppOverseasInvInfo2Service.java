package com.kayak.rpt.zz.manage.service;

import com.kayak.core.sql.SqlRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.AppOverseasInvInfo2Dao;
import com.kayak.rpt.zz.manage.model.AppOverseasInvInfo2;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "境外投资情况明细表总体收益情况2服务", model = AppOverseasInvInfo2.class)
public class AppOverseasInvInfo2Service {

	@Autowired
	private AppOverseasInvInfo2Dao appOverseasInvInfo2Dao;

	@API(desc = "查询境外投资情况明细表总体收益情况2信息", auth = APIAuth.NO)
	public SqlResult<AppOverseasInvInfo2> findAppOverseasInvInfo2s(SqlParam<AppOverseasInvInfo2> params) throws Exception {
		params.setMakeSql(true);
		return appOverseasInvInfo2Dao.findAppOverseasInvInfo2s(params);
	}

	public List<SqlRow> findAppOverseasInvInfo2s(Map<String, Object> params) throws Exception {
		return appOverseasInvInfo2Dao.findAppOverseasInvInfo2s(params);
	}

	@API(desc = "添加境外投资情况明细表总体收益情况2", params = "report_date,comp_nm,prod_type,inv_asst_mkt,gro_rate", auth = APIAuth.NO)
	public int addAppOverseasInvInfo2(SqlParam<AppOverseasInvInfo2> params) throws Exception {
		return appOverseasInvInfo2Dao.addAppOverseasInvInfo2(params).getEffect();
	}
	
	@API(desc = "修改境外投资情况明细表总体收益情况2", params = "report_date,comp_nm,prod_type,inv_asst_mkt,gro_rate", auth = APIAuth.NO)
	public int updateAppOverseasInvInfo2(SqlParam<AppOverseasInvInfo2> params) throws Exception {
		return appOverseasInvInfo2Dao.updateAppOverseasInvInfo2(params).getEffect();
	}
	
	@API(desc = "删除境外投资情况明细表总体收益情况2", params = "report_date,comp_nm,prod_type,inv_asst_mkt,gro_rate", auth = APIAuth.NO)
	public int deleteAppOverseasInvInfo2(SqlParam<AppOverseasInvInfo2> params) throws Exception {
		return appOverseasInvInfo2Dao.deleteAppOverseasInvInfo2(params).getEffect();
	}

}
