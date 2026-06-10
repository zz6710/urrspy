package com.kayak.rpt.zz.errorInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.AppNavInfoRegErrDao;
import com.kayak.rpt.zz.errorInfo.model.AppNavInfoRegErr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "净值信息登记错误信息服务", model = AppNavInfoRegErr.class)
public class AppNavInfoRegErrService {

	@Autowired
	private AppNavInfoRegErrDao appNavInfoRegErrDao;

	@API(desc = "查询净值信息登记错误信息", auth = APIAuth.YES)
	public SqlResult<AppNavInfoRegErr> findAppNavInfoRegs(SqlParam<AppNavInfoRegErr> params) throws Exception {
		return appNavInfoRegErrDao.findAppNavInfoRegErrs(params);
	}

	@API(desc = "添加净值信息登记错误", params = "bank_code,prod_reg_enc,nav_reg_type,son_share_code,cny,nav,rmb_nav,dj_nav,total_nav,rmb_total_nav,fq_nav,rmb_fq_nav,nav_cal_type,share,nav_date,disclosure_date,remain_bal,rmb_remain_bal,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,report_date,data_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time", auth = APIAuth.YES)
	public int addAppNavInfoReg(SqlParam<AppNavInfoRegErr> params) throws Exception {
		return appNavInfoRegErrDao.addAppNavInfoRegErr(params).getEffect();
	}

	@API(desc = "修改净值信息登记错误", params = "bank_code,prod_reg_enc,nav_reg_type,son_share_code,cny,nav,rmb_nav,dj_nav,total_nav,rmb_total_nav,fq_nav,rmb_fq_nav,nav_cal_type,share,nav_date,disclosure_date,remain_bal,rmb_remain_bal,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,report_date,data_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time", auth = APIAuth.YES)
	public int updateAppNavInfoReg(SqlParam<AppNavInfoRegErr> params) throws Exception {
		return appNavInfoRegErrDao.updateAppNavInfoRegErr(params).getEffect();
	}

	@API(desc = "删除净值信息登记错误", params = "bank_code,prod_reg_enc,nav_reg_type,son_share_code,cny,nav,rmb_nav,dj_nav,total_nav,rmb_total_nav,fq_nav,rmb_fq_nav,nav_cal_type,share,nav_date,disclosure_date,remain_bal,rmb_remain_bal,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,report_date,data_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time", auth = APIAuth.YES)
	public int deleteAppNavInfoReg(SqlParam<AppNavInfoRegErr> params) throws Exception {
		return appNavInfoRegErrDao.deleteAppNavInfoRegErr(params).getEffect();
	}

}
