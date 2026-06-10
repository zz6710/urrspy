package com.kayak.rpt.zz.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.BaseFiveNonstandFintechDao;
import com.kayak.rpt.zz.manage.model.BaseFiveNonstandFintech;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "非标融资主体行业表服务", model = BaseFiveNonstandFintech.class)
public class BaseFiveNonstandFintechService {

	@Autowired
	private BaseFiveNonstandFintechDao baseFiveNonstandFintechDao;

	@API(desc = "查询非标融资主体行业表信息", auth = APIAuth.YES)
	public SqlResult<BaseFiveNonstandFintech> findBaseFiveNonstandFintechs(SqlParam<BaseFiveNonstandFintech> params) throws Exception {
		params.setMakeSql(true);
		return baseFiveNonstandFintechDao.findBaseFiveNonstandFintechs(params);
	}

	@API(desc = "添加非标融资主体行业表", params = "id,windid,companycode,ssxyfield,ssxycode,ssxyname,nbstypecode,nbstypename,icneacode,icneaname,confidence,period", auth = APIAuth.NO)
	public int addBaseFiveNonstandFintech(SqlParam<BaseFiveNonstandFintech> params) throws Exception {
		return baseFiveNonstandFintechDao.addBaseFiveNonstandFintech(params).getEffect();
	}
	
	@API(desc = "修改非标融资主体行业表", params = "id,windid,companycode,ssxyfield,ssxycode,ssxyname,nbstypecode,nbstypename,icneacode,icneaname,confidence,period", auth = APIAuth.NO)
	public int updateBaseFiveNonstandFintech(SqlParam<BaseFiveNonstandFintech> params) throws Exception {
		return baseFiveNonstandFintechDao.updateBaseFiveNonstandFintech(params).getEffect();
	}
	
	@API(desc = "删除非标融资主体行业表", params = "id,windid,companycode,ssxyfield,ssxycode,ssxyname,nbstypecode,nbstypename,icneacode,icneaname,confidence,period", auth = APIAuth.NO)
	public int deleteBaseFiveNonstandFintech(SqlParam<BaseFiveNonstandFintech> params) throws Exception {
		return baseFiveNonstandFintechDao.deleteBaseFiveNonstandFintech(params).getEffect();
	}

	@API(desc = "导入", params = "id,windid,companycode,ssxyfield,ssxycode,ssxyname,nbstypecode,nbstypename,icneacode,icneaname,confidence,period", auth = APIAuth.YES)
	public void importBaseFiveNonstandFintech(List<BaseFiveNonstandFintech> baseFiveNonstandFinteches, Map<String, Object> params) throws Exception {
		baseFiveNonstandFintechDao.importBaseFiveNonstandFintech(baseFiveNonstandFinteches, params);
	}

}
