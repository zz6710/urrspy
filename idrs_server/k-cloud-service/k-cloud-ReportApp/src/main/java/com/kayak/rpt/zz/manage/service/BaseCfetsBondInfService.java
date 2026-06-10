package com.kayak.rpt.zz.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.BaseCfetsBondInfDao;
import com.kayak.rpt.zz.manage.model.BaseCfetsBondInf;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "CFETS债券指数样本券数据服务", model = BaseCfetsBondInf.class)
public class BaseCfetsBondInfService {

	@Autowired
	private BaseCfetsBondInfDao baseCfetsBondInfDao;

	@API(desc = "查询CFETS债券指数样本券数据信息", auth = APIAuth.YES)
	public SqlResult<BaseCfetsBondInf> findBaseCfetsBondInfs(SqlParam<BaseCfetsBondInf> params) throws Exception {
		params.setMakeSql(true);
		return baseCfetsBondInfDao.findBaseCfetsBondInfs(params);
	}

	@API(desc = "添加CFETS债券指数样本券数据", params = "id,report_date,index_nm,bond_code,bond_name,index_weight,crt_dt", auth = APIAuth.NO)
	public int addBaseCfetsBondInf(SqlParam<BaseCfetsBondInf> params) throws Exception {
		return baseCfetsBondInfDao.addBaseCfetsBondInf(params).getEffect();
	}
	
	@API(desc = "修改CFETS债券指数样本券数据", params = "id,report_date,index_nm,bond_code,bond_name,index_weight,crt_dt", auth = APIAuth.NO)
	public int updateBaseCfetsBondInf(SqlParam<BaseCfetsBondInf> params) throws Exception {
		return baseCfetsBondInfDao.updateBaseCfetsBondInf(params).getEffect();
	}
	
	@API(desc = "删除CFETS债券指数样本券数据", params = "id,report_date,index_nm,bond_code,bond_name,index_weight,crt_dt", auth = APIAuth.NO)
	public int deleteBaseCfetsBondInf(SqlParam<BaseCfetsBondInf> params) throws Exception {
		return baseCfetsBondInfDao.deleteBaseCfetsBondInf(params).getEffect();
	}

	@API(desc = "导入", params = "id,report_date,index_nm,bond_code,bond_name,index_weight,crt_dt", auth = APIAuth.YES)
	public void importBaseCfetsBondInf(List<BaseCfetsBondInf> baseCfetsBondInfs, Map<String, Object> params) throws Exception {
		baseCfetsBondInfDao.importBaseCfetsBondInf(baseCfetsBondInfs, params);
	}

}
