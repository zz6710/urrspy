package com.kayak.dps.valtabimp.service;


import com.kayak.dps.valtabimp.repository.ValReportTabParsetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.valtabimp.model.ValReportTabParset;

@Service
@APIDefine(desc = "配置编辑服务", model = ValReportTabParset.class)
public class ValReportTabParsetService {

	@Autowired
	private ValReportTabParsetDao valReportTabParsetDao;

	@API(desc = "查询配置编辑信息", auth = APIAuth.NO)
	public SqlResult<ValReportTabParset> findValReportTabParsets(SqlParam<ValReportTabParset> params) throws Exception {
		params.setMakeSql(false);
		return valReportTabParsetDao.findValReportTabParsets(params);
	}

	@API(desc = "添加配置编辑", params = "id,t8_val_reporttab_id,param_type,order_num,param_code,param_name,param_data_type,param_value,param_condition,note,inputuser,crt_date,crt_time", auth = APIAuth.NO)
	public int addValReportTabParset(SqlParam<ValReportTabParset> params) throws Exception {
		return valReportTabParsetDao.addValReportTabParset(params).getEffect();
	}
	
	@API(desc = "修改配置编辑", params = "id,t8_val_reporttab_id,param_type,order_num,param_code,param_name,param_data_type,param_value,param_condition,note,inputuser,crt_date,crt_time", auth = APIAuth.NO)
	public int updateValReportTabParset(SqlParam<ValReportTabParset> params) throws Exception {
		return valReportTabParsetDao.updateValReportTabParset(params).getEffect();
	}
	
	@API(desc = "删除配置编辑", params = "id,t8_val_reporttab_id,param_type,order_num,param_code,param_name,param_data_type,param_value,param_condition,note,inputuser,crt_date,crt_time", auth = APIAuth.NO)
	public int deleteValReportTabParset(SqlParam<ValReportTabParset> params) throws Exception {
		return valReportTabParsetDao.deleteValReportTabParset(params).getEffect();
	}

}
