package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.ProdStateRegistInfo;
import com.kayak.rpt.zz.operate.dao.ProdStateRegistDao;
import com.kayak.rpt.zz.operate.model.ProdStateRegist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "产品状态操作记录服务", model = ProdStateRegist.class)
public class ProdStateRegistService {

	@Autowired
	private ProdStateRegistDao ProdStateRegistDao;

	@API(desc = "查询产品状态操作记录信息", auth = APIAuth.YES)
	public SqlResult<ProdStateRegist> findProdStateRegists(SqlParam<ProdStateRegist> params) throws Exception {
		params.setMakeSql(true);
		return ProdStateRegistDao.findProdStateRegists(params);
	}

	@API(desc = "添加产品状态操作记录", params = "account_code,asset_code,bank_code,cny,create_date,create_time,details,fair_value,fair_value_cny,fl_valuation,holding_date,holding_type,imp_date,invested_amount,invested_amount_cny,invested_asset,mezzanine_asset_code,mezzanine_number,net_valuation,op_type,prod_reg_enc,quantity,register_date,register_serno,register_status,summit_user", auth = APIAuth.NO)
	public int addProdStateRegist(SqlParam<ProdStateRegistInfo> params,String opType) throws Exception {
		ProdStateRegist ProdStateRegist = BeanUtil.copyProperties(params.getModel(), ProdStateRegist.class);
		ProdStateRegist.setOpType(opType);
		ProdStateRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return ProdStateRegistDao.addProdStateRegist(ProdStateRegist).getEffect();
	}

	public void addImportProdStateRegist(ProdStateRegistInfo ProdStateRegistInfo, String opType) throws Exception {
		ProdStateRegist ProdStateRegist = BeanUtil.copyProperties(ProdStateRegistInfo, ProdStateRegist.class);
		ProdStateRegist.setOpType(opType);
		ProdStateRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		ProdStateRegistDao.addImportProdStateRegist(ProdStateRegist).getEffect();
	}
}
