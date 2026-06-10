package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.SubseqSubscrRegistInfo;
import com.kayak.rpt.zz.operate.model.ProdTransRegist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.operate.dao.SubseqSubscrRegistDao;
import com.kayak.rpt.zz.operate.model.SubseqSubscrRegist;

@Service
@APIDefine(desc = "产品存续期登记操作记录服务", model = SubseqSubscrRegist.class)
public class SubseqSubscrRegistService {

	@Autowired
	private SubseqSubscrRegistDao subseqSubscrRegistDao;

	@API(desc = "查询产品存续期登记操作记录信息", auth = APIAuth.YES)
	public SqlResult<SubseqSubscrRegist> findSubseqSubscrRegists(SqlParam<SubseqSubscrRegist> params) throws Exception {
		params.setMakeSql(true);
		return subseqSubscrRegistDao.findSubseqSubscrRegists(params);
	}

	@API(desc = "添加产品存续期登记操作记录", params = "bank_code,prod_code,initial_nav,nav,aggregate_nav,nav_cur,convert_rmb_nav,convert_rmb_agg_nav,realized_annual_return,expected_annual_return,inconme_bank,business_start_date,business_end_date,subscribed_latest_vol,redeemed_latest_vol,units_bonus,cash_bonus,prod_amt,prod_vol,details,register_serno,imp_date,register_date,register_status,convert_initial_nav,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int addSubseqSubscrRegist(SqlParam<SubseqSubscrRegistInfo> params, String opType) throws Exception {
		SubseqSubscrRegist subseqSubscrRegist = BeanUtil.copyProperties(params.getModel(), SubseqSubscrRegist.class);
		subseqSubscrRegist.setOpType(opType);
		subseqSubscrRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return subseqSubscrRegistDao.addSubseqSubscrRegist(subseqSubscrRegist).getEffect();
	}
	
	@API(desc = "修改产品存续期登记操作记录", params = "bank_code,prod_code,initial_nav,nav,aggregate_nav,nav_cur,convert_rmb_nav,convert_rmb_agg_nav,realized_annual_return,expected_annual_return,inconme_bank,business_start_date,business_end_date,subscribed_latest_vol,redeemed_latest_vol,units_bonus,cash_bonus,prod_amt,prod_vol,details,register_serno,imp_date,register_date,register_status,convert_initial_nav,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int updateSubseqSubscrRegist(SqlParam<SubseqSubscrRegist> params) throws Exception {
		return subseqSubscrRegistDao.updateSubseqSubscrRegist(params).getEffect();
	}
	
	@API(desc = "删除产品存续期登记操作记录", params = "bank_code,prod_code,initial_nav,nav,aggregate_nav,nav_cur,convert_rmb_nav,convert_rmb_agg_nav,realized_annual_return,expected_annual_return,inconme_bank,business_start_date,business_end_date,subscribed_latest_vol,redeemed_latest_vol,units_bonus,cash_bonus,prod_amt,prod_vol,details,register_serno,imp_date,register_date,register_status,convert_initial_nav,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int deleteSubseqSubscrRegist(SqlParam<SubseqSubscrRegist> params) throws Exception {
		return subseqSubscrRegistDao.deleteSubseqSubscrRegist(params).getEffect();
	}

	public void addImportSubseqSubscrRegist(SubseqSubscrRegistInfo subseqSubscrRegistInfo, String opType) throws Exception {
		SubseqSubscrRegist subseqSubscrRegist = BeanUtil.copyProperties(subseqSubscrRegistInfo, SubseqSubscrRegist.class);
		subseqSubscrRegist.setOpType(opType);
		subseqSubscrRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		subseqSubscrRegistDao.addSubseqSubscrRegist(subseqSubscrRegist).getEffect();
	}
}
