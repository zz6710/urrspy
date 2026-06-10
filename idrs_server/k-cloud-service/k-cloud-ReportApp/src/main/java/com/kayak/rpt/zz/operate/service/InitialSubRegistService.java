package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.InitialSubRegistInfo;
import com.kayak.rpt.zz.operate.model.AppraiseRegist;
import com.kayak.rpt.zz.operate.model.ProdIssuanceRegist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.operate.dao.InitialSubRegistDao;
import com.kayak.rpt.zz.operate.model.InitialSubRegist;

@Service
@APIDefine(desc = "募集总量操作记录服务", model = InitialSubRegist.class)
public class InitialSubRegistService {

	@Autowired
	private InitialSubRegistDao initialSubRegistDao;

	@API(desc = "查询募集总量操作记录信息", auth = APIAuth.YES)
	public SqlResult<InitialSubRegist> findInitialSubRegists(SqlParam<InitialSubRegist> params) throws Exception {
		params.setMakeSql(true);
		return initialSubRegistDao.findInitialSubRegists(params);
	}

	@API(desc = "添加募集总量操作记录", params = "bank_code,prod_code,number_indiv_invest,number_corpor_invest,number_ucor_invest,other_distribut_agents,details,register_serno,imp_date,register_date,register_status,actual_subscribed_amt,subscribed_vol,amt_other_db_agents,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int addInitialSubRegist(SqlParam<InitialSubRegistInfo> params, String opType) throws Exception {
		InitialSubRegist initialSubRegist = BeanUtil.copyProperties(params.getModel(), InitialSubRegist.class);
		initialSubRegist.setOpType(opType);
		initialSubRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return initialSubRegistDao.addInitialSubRegist(initialSubRegist).getEffect();
	}
	@API(desc = "添加募集总量操作记录", params = "bank_code,prod_code,number_indiv_invest,number_corpor_invest,number_ucor_invest,other_distribut_agents,details,register_serno,imp_date,register_date,register_status,actual_subscribed_amt,subscribed_vol,amt_other_db_agents,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int addOldInitialSubRegist(InitialSubRegistInfo initialSubRegistInfo, String opType) throws Exception {
		InitialSubRegist initialSubRegist = BeanUtil.copyProperties(initialSubRegistInfo, InitialSubRegist.class);
		initialSubRegist.setOpType(opType);
		initialSubRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return initialSubRegistDao.addInitialSubRegist(initialSubRegist).getEffect();
	}
	
	@API(desc = "修改募集总量操作记录", params = "bank_code,prod_code,number_indiv_invest,number_corpor_invest,number_ucor_invest,other_distribut_agents,details,register_serno,imp_date,register_date,register_status,actual_subscribed_amt,subscribed_vol,amt_other_db_agents,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int updateInitialSubRegist(SqlParam<InitialSubRegist> params) throws Exception {
		return initialSubRegistDao.updateInitialSubRegist(params).getEffect();
	}
	
	@API(desc = "删除募集总量操作记录", params = "bank_code,prod_code,number_indiv_invest,number_corpor_invest,number_ucor_invest,other_distribut_agents,details,register_serno,imp_date,register_date,register_status,actual_subscribed_amt,subscribed_vol,amt_other_db_agents,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int deleteInitialSubRegist(SqlParam<InitialSubRegist> params) throws Exception {
		return initialSubRegistDao.deleteInitialSubRegist(params).getEffect();
	}

	public int addImportInitialSubRegist(InitialSubRegistInfo initialSubRegistInfo, String opType) throws Exception {
		InitialSubRegist initialSubRegist = BeanUtil.copyProperties(initialSubRegistInfo, InitialSubRegist.class);
		initialSubRegist.setOpType(opType);
		initialSubRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return initialSubRegistDao.addInitialSubRegist(initialSubRegist).getEffect();
	}
}
