package com.kayak.rpt.zz.feedback.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.feedback.dao.TrFileresultsDao;
import com.kayak.rpt.zz.feedback.model.TrFileresults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "中债服务", model = TrFileresults.class)
public class TrFileresultsService {

	@Autowired
	private TrFileresultsDao trFileresultsDao;

	@API(desc = "查询中债信息", auth = APIAuth.YES)
	public SqlResult<TrFileresults> findTrFileresultss(SqlParam<TrFileresults> params) throws Exception {
		params.setMakeSql(true);
		return trFileresultsDao.findTrFileresultss(params);
	}

	@API(desc = "添加中债", params = "register_date,file_type,filename,fileno,register_serno,errormsg,errorcode", auth = APIAuth.NO)
	public int addTrFileresults(SqlParam<TrFileresults> params) throws Exception {
		return trFileresultsDao.addTrFileresults(params).getEffect();
	}
	
	@API(desc = "修改中债", params = "register_date,file_type,filename,fileno,register_serno,errormsg,errorcode", auth = APIAuth.NO)
	public int updateTrFileresults(SqlParam<TrFileresults> params) throws Exception {
		return trFileresultsDao.updateTrFileresults(params).getEffect();
	}
	
	@API(desc = "删除中债", params = "register_date,file_type,filename,fileno,register_serno,errormsg,errorcode", auth = APIAuth.NO)
	public int deleteTrFileresults(SqlParam<TrFileresults> params) throws Exception {
		return trFileresultsDao.deleteTrFileresults(params).getEffect();
	}

}
