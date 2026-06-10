package com.kayak.dps.app.service;

import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.dao.RmsOdsZyG06bDao;
import com.kayak.dps.app.model.RmsOdsZyG06b;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Service
@Slf4j
@APIDefine(desc = "g06b期末余额手工维护服务", model = RmsOdsZyG06b.class)
public class RmsOdsZyG06bService {

	@Autowired
	private RmsOdsZyG06bDao rmsOdsZyG06bDao;

	@API(desc = "查询g06b期末余额手工维护信息", auth = APIAuth.YES)
	public SqlResult<RmsOdsZyG06b> findRmsOdsZyG06bs(SqlParam<RmsOdsZyG06b> params) throws Exception {
		params.setMakeSql(true);
		return rmsOdsZyG06bDao.findRmsOdsZyG06bs(params);
	}

	@API(desc = "添加g06b期末余额手工维护", params = "id,data_date,b09,b10,b11,b12", auth = APIAuth.NO)
	public String addRmsOdsZyG06b(SqlParam<RmsOdsZyG06b> params) throws Exception {
		int count = rmsOdsZyG06bDao.countByDataDate(params);
		if (count > 0) {
			return RequestSupport.updateReturnJson(false, "新增失败:已经存在数据日期为" + params.getModel().getDataDate() + "的数据", null).toString();
		}
		int effect = rmsOdsZyG06bDao.addRmsOdsZyG06b(params).getEffect();
		log.info("新增成功:{}",effect);
		return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
	}
	
	@API(desc = "修改g06b期末余额手工维护", params = "id,data_date,b09,b10,b11,b12", auth = APIAuth.NO)
	public String updateRmsOdsZyG06b(SqlParam<RmsOdsZyG06b> params) throws Exception {
		if(rmsOdsZyG06bDao.updateRmsOdsZyG06b(params).getEffect() > 0) {
			return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
		}
		return RequestSupport.updateReturnJson(false, "修改失败", null).toString();
	}
	
	@API(desc = "删除g06b期末余额手工维护", params = "id,data_date,b09,b10,b11,b12", auth = APIAuth.NO)
	public String deleteRmsOdsZyG06b(SqlParam<RmsOdsZyG06b> params) throws Exception {
		if(rmsOdsZyG06bDao.deleteRmsOdsZyG06b(params).getEffect() > 0) {
			return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
		}
		return RequestSupport.updateReturnJson(false, "删除失败", null).toString();
	}

}
