package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.ProdTransRegistInfo;
import com.kayak.rpt.zz.operate.model.ProdRegistFiling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.operate.dao.ProdTransRegistDao;
import com.kayak.rpt.zz.operate.model.ProdTransRegist;

@Service
@APIDefine(desc = "交易信息登记操作记录服务", model = ProdTransRegist.class)
public class ProdTransRegistService {

	@Autowired
	private ProdTransRegistDao prodTransRegistDao;

	@API(desc = "查询交易信息登记操作记录信息", auth = APIAuth.YES)
	public SqlResult<ProdTransRegist> findProdTransRegists(SqlParam<ProdTransRegist> params) throws Exception {
		params.setMakeSql(true);
		return prodTransRegistDao.findProdTransRegists(params);
	}

	@API(desc = "添加交易信息登记操作记录", params = "bank_code,prod_code,trans_code,asset_code,cur,amt,convert_rmb,quantity,method_asset_measure,cash_type,detail_cash_type,trade_date,trade_counter,counter_type,unit_price_full,unit_price_net,rate_annual_return,trans_ident_code,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int addProdTransRegist(SqlParam<ProdTransRegistInfo> params,String opType) throws Exception {
		ProdTransRegist prodTransRegist = BeanUtil.copyProperties(params.getModel(), ProdTransRegist.class);
		prodTransRegist.setOpType(opType);
		prodTransRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return prodTransRegistDao.addProdTransRegist(prodTransRegist).getEffect();
	}
	
	@API(desc = "修改交易信息登记操作记录", params = "bank_code,prod_code,trans_code,asset_code,cur,amt,convert_rmb,quantity,method_asset_measure,cash_type,detail_cash_type,trade_date,trade_counter,counter_type,unit_price_full,unit_price_net,rate_annual_return,trans_ident_code,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int updateProdTransRegist(SqlParam<ProdTransRegist> params) throws Exception {
		return prodTransRegistDao.updateProdTransRegist(params).getEffect();
	}
	
	@API(desc = "删除交易信息登记操作记录", params = "bank_code,prod_code,trans_code,asset_code,cur,amt,convert_rmb,quantity,method_asset_measure,cash_type,detail_cash_type,trade_date,trade_counter,counter_type,unit_price_full,unit_price_net,rate_annual_return,trans_ident_code,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int deleteProdTransRegist(SqlParam<ProdTransRegist> params) throws Exception {
		return prodTransRegistDao.deleteProdTransRegist(params).getEffect();
	}

	public void addimportProdTransRegist(ProdTransRegistInfo prodTransRegistInfo, String opType) throws Exception {
		ProdTransRegist prodTransRegist = BeanUtil.copyProperties(prodTransRegistInfo, ProdTransRegist.class);
		prodTransRegist.setOpType(opType);
		prodTransRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		prodTransRegistDao.addImportProdTransRegist(prodTransRegist).getEffect();
	}
}
