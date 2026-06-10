package com.kayak.rpt.rhjrjgtj.service;

import com.kayak.core.sql.SqlRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.rhjrjgtj.dao.AppAssetA1413DepStrucDao;
import com.kayak.rpt.rhjrjgtj.model.AppAssetA1413DepStruc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "A1413存款期限结构及相关业务情况表服务", model = AppAssetA1413DepStruc.class)
public class AppAssetA1413DepStrucService {

	@Autowired
	private AppAssetA1413DepStrucDao appAssetA1413DepStrucDao;

	@API(desc = "查询A1413存款期限结构及相关业务情况表信息", auth = APIAuth.YES)
	public SqlResult<AppAssetA1413DepStruc> findAppAssetA1413DepStrucs(SqlParam<AppAssetA1413DepStruc> params) throws Exception {
		return appAssetA1413DepStrucDao.findAppAssetA1413DepStrucs(params);
	}

	@API(desc = "下载报送文件", auth = APIAuth.YES)
	public List<SqlRow> findAppAssetA1413DepStrucsByExeId(String reportDate, String exeId) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("reportDate", reportDate);
		return appAssetA1413DepStrucDao.findAppAssetA1413DepStrucsByExeId(params, exeId);
	}

	@API(desc = "添加A1413存款期限结构及相关业务情况表", params = "met_code,met_name,met_bane,report_date", auth = APIAuth.NO)
	public int addAppAssetA1413DepStruc(SqlParam<AppAssetA1413DepStruc> params) throws Exception {
		return appAssetA1413DepStrucDao.addAppAssetA1413DepStruc(params).getEffect();
	}
	
	@API(desc = "修改A1413存款期限结构及相关业务情况表", params = "met_code,met_name,met_bane,report_date", auth = APIAuth.NO)
	public int updateAppAssetA1413DepStruc(SqlParam<AppAssetA1413DepStruc> params) throws Exception {
		return appAssetA1413DepStrucDao.updateAppAssetA1413DepStruc(params).getEffect();
	}
	
	@API(desc = "删除A1413存款期限结构及相关业务情况表", params = "met_code,met_name,met_bane,report_date", auth = APIAuth.NO)
	public int deleteAppAssetA1413DepStruc(SqlParam<AppAssetA1413DepStruc> params) throws Exception {
		return appAssetA1413DepStrucDao.deleteAppAssetA1413DepStruc(params).getEffect();
	}

}
