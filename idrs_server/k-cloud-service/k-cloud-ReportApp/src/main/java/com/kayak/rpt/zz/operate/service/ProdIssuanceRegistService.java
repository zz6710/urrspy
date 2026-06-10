package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.ProdIssuanceRegistInfo;
import com.kayak.rpt.zz.operate.model.InitialSubRegist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.operate.dao.ProdIssuanceRegistDao;
import com.kayak.rpt.zz.operate.model.ProdIssuanceRegist;

@Service
@APIDefine(desc = "产品发行登记操作记录服务", model = ProdIssuanceRegist.class)
public class ProdIssuanceRegistService {

	@Autowired
	private ProdIssuanceRegistDao prodIssuanceRegistDao;

	@API(desc = "查询产品发行登记操作记录信息", auth = APIAuth.YES)
	public SqlResult<ProdIssuanceRegist> findProdIssuanceRegists(SqlParam<ProdIssuanceRegist> params) throws Exception {
		params.setMakeSql(true);
		return prodIssuanceRegistDao.findProdIssuanceRegists(params);
	}

	@API(desc = "添加 操作前的 产品发行登记 操作记录", params = "prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method,structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate,regular_open_period,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period,custody_acct_no,custody_acct_name,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int addOldProdIssuanceRegist(ProdIssuanceRegistInfo registInfo, String opType) throws Exception {
		ProdIssuanceRegist prodIssuanceRegist = BeanUtil.copyProperties(registInfo, ProdIssuanceRegist.class);
		prodIssuanceRegist.setOpType(opType);
		prodIssuanceRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return prodIssuanceRegistDao.addProdIssuanceRegist(prodIssuanceRegist).getEffect();
	}

	@API(desc = "添加产品发行登记操作记录", params = "prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method,structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate,regular_open_period,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period,custody_acct_no,custody_acct_name,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int addProdIssuanceRegist(SqlParam<ProdIssuanceRegistInfo> params, String opType) throws Exception {
		ProdIssuanceRegist prodIssuanceRegist = BeanUtil.copyProperties(params.getModel(), ProdIssuanceRegist.class);
		prodIssuanceRegist.setOpType(opType);
		prodIssuanceRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return prodIssuanceRegistDao.addProdIssuanceRegist(prodIssuanceRegist).getEffect();
	}
	
	@API(desc = "修改产品发行登记操作记录", params = "prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method,structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate,regular_open_period,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period,custody_acct_no,custody_acct_name,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int updateProdIssuanceRegist(SqlParam<ProdIssuanceRegist> params) throws Exception {
		return prodIssuanceRegistDao.updateProdIssuanceRegist(params).getEffect();
	}
	
	@API(desc = "删除产品发行登记操作记录", params = "prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method,structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate,regular_open_period,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period,custody_acct_no,custody_acct_name,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int deleteProdIssuanceRegist(SqlParam<ProdIssuanceRegist> params) throws Exception {
		return prodIssuanceRegistDao.deleteProdIssuanceRegist(params).getEffect();
	}

	public int addImportProdIssuanceRegist(ProdIssuanceRegistInfo prodIssuanceRegistInfo, String opType) throws Exception {
		ProdIssuanceRegist prodIssuanceRegist = BeanUtil.copyProperties(prodIssuanceRegistInfo, ProdIssuanceRegist.class);
		prodIssuanceRegist.setOpType(opType);
		prodIssuanceRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return prodIssuanceRegistDao.addImportProdIssuanceRegist(prodIssuanceRegist).getEffect();
	}
}
