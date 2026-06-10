package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.AppNavInfoReg;
import com.kayak.rpt.zz.operate.dao.AppNavRegDao;
import com.kayak.rpt.zz.operate.model.AppNavReg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "净值信息登记操作记录信息服务", model = AppNavReg.class)
public class AppNavRegService {

	@Autowired
	private AppNavRegDao appNavRegDao;

	@API(desc = "查询净值信息登记操作记录信息", auth = APIAuth.YES)
	public SqlResult<AppNavReg> findAppNavInfoRegs(SqlParam<AppNavReg> params) throws Exception {
		return appNavRegDao.findAppNavRegs(params);
	}

	@API(desc = "添加净值信息登记操作记录", params = "bank_code,prod_reg_enc,nav_reg_type,son_share_code,cny,nav,rmb_nav,dj_nav,total_nav,rmb_total_nav,fq_nav,rmb_fq_nav,nav_cal_type,share,nav_date,disclosure_date,remain_bal,rmb_remain_bal,details,register_date,register_serno,register_status,theory_report_start_date,theory_report_end_date,report_date,data_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time,summit_user,create_date,create_time,op_type", auth = APIAuth.YES)
	public int addAppNavInfoReg(SqlParam<AppNavInfoReg> params, String opType) throws Exception {
		AppNavReg appNavReg = BeanUtil.copyProperties(params.getModel(), AppNavReg.class);
		appNavReg.setOpType(opType);
		appNavReg.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return appNavRegDao.addAppNavReg(appNavReg).getEffect();
	}

	@API(desc = "修改净值信息登记操作记录", params = "bank_code,prod_reg_enc,nav_reg_type,son_share_code,cny,nav,rmb_nav,dj_nav,total_nav,rmb_total_nav,fq_nav,rmb_fq_nav,nav_cal_type,share,nav_date,disclosure_date,remain_bal,rmb_remain_bal,details,register_date,register_serno,register_status,theory_report_start_date,theory_report_end_date,report_date,data_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time,summit_user,create_date,create_time,op_type", auth = APIAuth.YES)
	public int updateAppNavInfoReg(SqlParam<AppNavReg> params) throws Exception {
		return appNavRegDao.updateAppNavReg(params).getEffect();
	}

	@API(desc = "删除净值信息登记操作记录", params = "bank_code,prod_reg_enc,nav_reg_type,son_share_code,cny,nav,rmb_nav,dj_nav,total_nav,rmb_total_nav,fq_nav,rmb_fq_nav,nav_cal_type,share,nav_date,disclosure_date,remain_bal,rmb_remain_bal,details,register_date,register_serno,register_status,theory_report_start_date,theory_report_end_date,report_date,data_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time,summit_user,create_date,create_time,op_type", auth = APIAuth.YES)
	public int deleteAppNavInfoReg(SqlParam<AppNavReg> params) throws Exception {
		return appNavRegDao.deleteAppNavReg(params).getEffect();
	}

	public int addImportAppNavInfoReg(AppNavInfoReg params, String opType) throws Exception {
		AppNavReg appNavReg = BeanUtil.copyProperties(params, AppNavReg.class);
		appNavReg.setOpType(opType);
		appNavReg.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return appNavRegDao.addAppNavReg(appNavReg).getEffect();
	}

}
