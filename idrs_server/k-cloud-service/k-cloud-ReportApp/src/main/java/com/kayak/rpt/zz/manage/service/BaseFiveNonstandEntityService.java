package com.kayak.rpt.zz.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.BaseFiveNonstandEntityDao;
import com.kayak.rpt.zz.manage.model.BaseFiveNonstandEntity;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "非标融资主体主题表服务", model = BaseFiveNonstandEntity.class)
public class BaseFiveNonstandEntityService {

	@Autowired
	private BaseFiveNonstandEntityDao baseFiveNonstandEntityDao;

	@API(desc = "查询非标融资主体主题表信息", auth = APIAuth.YES)
	public SqlResult<BaseFiveNonstandEntity> findBaseFiveNonstandEntitys(SqlParam<BaseFiveNonstandEntity> params) throws Exception {
		params.setMakeSql(true);
		return baseFiveNonstandEntityDao.findBaseFiveNonstandEntitys(params);
	}

	@API(desc = "添加非标融资主体主题表", params = "id,windid,companycode,companyname,usci,regioncode,region,isfintech,isgreen,isinclusive,ispension,isdigital,ishightech,issrdi,istechsme,isothertech,ishightechm,ishightechs,isstraemer,isintellectual,issmme,companyscale,isprivate,businessnature,isagriculture,isdigiindustry,isdigitalization,ispollution,period", auth = APIAuth.NO)
	public int addBaseFiveNonstandEntity(SqlParam<BaseFiveNonstandEntity> params) throws Exception {
		return baseFiveNonstandEntityDao.addBaseFiveNonstandEntity(params).getEffect();
	}
	
	@API(desc = "修改非标融资主体主题表", params = "id,windid,companycode,companyname,usci,regioncode,region,isfintech,isgreen,isinclusive,ispension,isdigital,ishightech,issrdi,istechsme,isothertech,ishightechm,ishightechs,isstraemer,isintellectual,issmme,companyscale,isprivate,businessnature,isagriculture,isdigiindustry,isdigitalization,ispollution,period", auth = APIAuth.NO)
	public int updateBaseFiveNonstandEntity(SqlParam<BaseFiveNonstandEntity> params) throws Exception {
		return baseFiveNonstandEntityDao.updateBaseFiveNonstandEntity(params).getEffect();
	}
	
	@API(desc = "删除非标融资主体主题表", params = "id,windid,companycode,companyname,usci,regioncode,region,isfintech,isgreen,isinclusive,ispension,isdigital,ishightech,issrdi,istechsme,isothertech,ishightechm,ishightechs,isstraemer,isintellectual,issmme,companyscale,isprivate,businessnature,isagriculture,isdigiindustry,isdigitalization,ispollution,period", auth = APIAuth.NO)
	public int deleteBaseFiveNonstandEntity(SqlParam<BaseFiveNonstandEntity> params) throws Exception {
		return baseFiveNonstandEntityDao.deleteBaseFiveNonstandEntity(params).getEffect();
	}

	@API(desc = "导入", params = "id,windid,companycode,companyname,usci,regioncode,region,isfintech,isgreen,isinclusive,ispension,isdigital,ishightech,issrdi,istechsme,isothertech,ishightechm,ishightechs,isstraemer,isintellectual,issmme,companyscale,isprivate,businessnature,isagriculture,isdigiindustry,isdigitalization,ispollution,period", auth = APIAuth.YES)
	public void importBaseFiveNonstandEntity(List<BaseFiveNonstandEntity> baseFiveNonstandEntities, Map<String, Object> params) throws Exception {
		baseFiveNonstandEntityDao.importBaseFiveNonstandEntity(baseFiveNonstandEntities, params);
	}

}
