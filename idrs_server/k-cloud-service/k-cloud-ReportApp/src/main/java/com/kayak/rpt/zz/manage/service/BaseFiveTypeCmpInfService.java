package com.kayak.rpt.zz.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.BaseFiveTypeCmpInfDao;
import com.kayak.rpt.zz.manage.model.BaseFiveTypeCmpInf;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "额外打标主体名单表服务", model = BaseFiveTypeCmpInf.class)
public class BaseFiveTypeCmpInfService {

	@Autowired
	private BaseFiveTypeCmpInfDao baseFiveTypeCmpInfDao;

	@API(desc = "查询额外打标主体名单表信息", auth = APIAuth.YES)
	public SqlResult<BaseFiveTypeCmpInf> findBaseFiveTypeCmpInfs(SqlParam<BaseFiveTypeCmpInf> params) throws Exception {
		params.setMakeSql(true);
		return baseFiveTypeCmpInfDao.findBaseFiveTypeCmpInfs(params);
	}

	@API(desc = "添加额外打标主体名单表", params = "id,cmp_nm,creditid,five_type,crt_dt", auth = APIAuth.NO)
	public int addBaseFiveTypeCmpInf(SqlParam<BaseFiveTypeCmpInf> params) throws Exception {
		return baseFiveTypeCmpInfDao.addBaseFiveTypeCmpInf(params).getEffect();
	}
	
	@API(desc = "修改额外打标主体名单表", params = "id,cmp_nm,creditid,five_type,crt_dt", auth = APIAuth.NO)
	public int updateBaseFiveTypeCmpInf(SqlParam<BaseFiveTypeCmpInf> params) throws Exception {
		return baseFiveTypeCmpInfDao.updateBaseFiveTypeCmpInf(params).getEffect();
	}
	
	@API(desc = "删除额外打标主体名单表", params = "id,cmp_nm,creditid,five_type,crt_dt", auth = APIAuth.NO)
	public int deleteBaseFiveTypeCmpInf(SqlParam<BaseFiveTypeCmpInf> params) throws Exception {
		return baseFiveTypeCmpInfDao.deleteBaseFiveTypeCmpInf(params).getEffect();
	}

	@API(desc = "导入", params = "id,cmp_nm,creditid,five_type,crt_dt", auth = APIAuth.YES)
	public void importBaseFiveTypeCmpInf(List<BaseFiveTypeCmpInf> baseFiveTypeCmpInfs, Map<String, Object> params) throws Exception {
		baseFiveTypeCmpInfDao.importBaseFiveTypeCmpInf(baseFiveTypeCmpInfs, params);
	}

}
