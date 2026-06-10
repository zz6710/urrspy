package com.kayak.dps.app.service;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.dao.RmsOdsZyKb02Dao;
import com.kayak.dps.app.model.RmsOdsZyKb02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;

@Service
@APIDefine(desc = "KB02.主要经济指标表（企月快02表）补录表服务", model = RmsOdsZyKb02.class)
public class RmsOdsZyKb02Service {

	@Autowired
	private RmsOdsZyKb02Dao rmsOdsZyKb02Dao;

	@API(desc = "查询KB02.主要经济指标表（企月快02表）补录表信息", auth = APIAuth.YES)
	public SqlResult<RmsOdsZyKb02> findRmsOdsZyKb02s(SqlParam<RmsOdsZyKb02> params) throws Exception {
		params.setMakeSql(true);
		return rmsOdsZyKb02Dao.findRmsOdsZyKb02s(params);
	}

	@API(desc = "添加KB02.主要经济指标表（企月快02表）补录表", params = "id,data_date,d01", auth = APIAuth.NO)
	public String addRmsOdsZyKb02(SqlParam<RmsOdsZyKb02> params) throws Exception {
		if (rmsOdsZyKb02Dao.addRmsOdsZyKb02(params).getEffect() > 0) {
			return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
		}
		return RequestSupport.updateReturnJson(false, "新增失败", null).toString();
	}
	
	@API(desc = "修改KB02.主要经济指标表（企月快02表）补录表", params = "id,data_date,d01", auth = APIAuth.NO)
	public String updateRmsOdsZyKb02(SqlParam<RmsOdsZyKb02> params) throws Exception {
		if (rmsOdsZyKb02Dao.updateRmsOdsZyKb02(params).getEffect() > 0) {
			return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
		}
		return RequestSupport.updateReturnJson(false, "修改失败", null).toString();
	}
	
	@API(desc = "删除KB02.主要经济指标表（企月快02表）补录表", params = "id,data_date,d01", auth = APIAuth.NO)
	public String deleteRmsOdsZyKb02(SqlParam<RmsOdsZyKb02> params) throws Exception {
		if (rmsOdsZyKb02Dao.deleteRmsOdsZyKb02(params).getEffect() > 0) {
			return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
		}
		return RequestSupport.updateReturnJson(false, "删除失败", null).toString();
	}

}
