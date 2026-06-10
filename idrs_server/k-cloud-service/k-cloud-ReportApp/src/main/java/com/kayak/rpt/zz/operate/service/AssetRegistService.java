package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.AssetRegistInfo;
import com.kayak.rpt.zz.operate.dao.AssetRegistDao;
import com.kayak.rpt.zz.operate.model.AssetRegist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "资产持仓持仓操作记录服务", model = AssetRegist.class)
public class AssetRegistService {

	@Autowired
	private AssetRegistDao assetRegistDao;

	@API(desc = "查询资产持仓持仓操作记录信息", auth = APIAuth.YES)
	public SqlResult<AssetRegist> findAssetRegists(SqlParam<AssetRegist> params) throws Exception {
		params.setMakeSql(true);
		return assetRegistDao.findAssetRegists(params);
	}

	@API(desc = "添加资产持仓持仓操作记录", params = "account_code,asset_code,bank_code,cny,create_date,create_time,details,fair_value,fair_value_cny,fl_valuation,holding_date,holding_type,imp_date,invested_amount,invested_amount_cny,invested_asset,mezzanine_asset_code,mezzanine_number,net_valuation,op_type,prod_reg_enc,quantity,register_date,register_serno,register_status,summit_user", auth = APIAuth.NO)
	public int addAssetRegist(SqlParam<AssetRegistInfo> params,String opType) throws Exception {
		AssetRegist AssetRegist = BeanUtil.copyProperties(params.getModel(), AssetRegist.class);
		AssetRegist.setOpType(opType);
		AssetRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return assetRegistDao.addAssetRegist(AssetRegist).getEffect();
	}

	public void addImportAssetRegist(AssetRegistInfo AssetRegistInfo, String opType) throws Exception {
		AssetRegist AssetRegist = BeanUtil.copyProperties(AssetRegistInfo, AssetRegist.class);
		AssetRegist.setOpType(opType);
		AssetRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		assetRegistDao.addImportAssetRegist(AssetRegist).getEffect();
	}
}
