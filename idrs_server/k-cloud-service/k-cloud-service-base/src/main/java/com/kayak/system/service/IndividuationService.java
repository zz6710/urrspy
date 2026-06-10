package com.kayak.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.system.dao.IndividuationDao;
import com.kayak.system.model.Individuation;

@Service
@APIDefine(desc = "个性化配置服务", model = Individuation.class)
public class IndividuationService {

	@Autowired
	private IndividuationDao individuationDao;

	@API(desc = "查询个性化配置信息", auth = APIAuth.NO)
	public SqlResult<Individuation> findIndividuations(SqlParam<Individuation> params) throws Exception {
		params.getModel().setUserid(SysUtil.getLoginUserid());
		return individuationDao.findIndividuations(params);
	}

	@API(desc = "修改个性化配置", params = "userid,theme_color,menu_color,menu_min,menu_bg_show,menu_bg", auth = APIAuth.NO)
	public int updateIndividuation(SqlParam<Individuation> params) throws Exception {
		params.getModel().setUserid(SysUtil.getLoginUserid());
		individuationDao.updateIndividuation(params);
		return 1;
	}
}
