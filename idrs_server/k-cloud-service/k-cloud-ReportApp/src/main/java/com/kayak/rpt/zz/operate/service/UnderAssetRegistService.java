package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.UnderAssetRegistInfo;
import com.kayak.rpt.zz.operate.model.SubseqSubscrRegist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.operate.dao.UnderAssetRegistDao;
import com.kayak.rpt.zz.operate.model.UnderAssetRegist;

@Service
@APIDefine(desc = "底层资产持仓操作记录服务", model = UnderAssetRegist.class)
public class UnderAssetRegistService {

	@Autowired
	private UnderAssetRegistDao underAssetRegistDao;

	@API(desc = "查询底层资产持仓操作记录信息", auth = APIAuth.YES)
	public SqlResult<UnderAssetRegist> findUnderAssetRegists(SqlParam<UnderAssetRegist> params) throws Exception {
		params.setMakeSql(true);
		return underAssetRegistDao.findUnderAssetRegists(params);
	}

	@API(desc = "添加底层资产持仓操作记录", params = "bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int addUnderAssetRegist(SqlParam<UnderAssetRegistInfo> params,String opType) throws Exception {
		UnderAssetRegist underAssetRegist = BeanUtil.copyProperties(params.getModel(), UnderAssetRegist.class);
		underAssetRegist.setOpType(opType);
		underAssetRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return underAssetRegistDao.addUnderAssetRegist(underAssetRegist).getEffect();
	}
	
	@API(desc = "修改底层资产持仓操作记录", params = "bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int updateUnderAssetRegist(SqlParam<UnderAssetRegist> params) throws Exception {
		return underAssetRegistDao.updateUnderAssetRegist(params).getEffect();
	}
	
	@API(desc = "删除底层资产持仓操作记录", params = "bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int deleteUnderAssetRegist(SqlParam<UnderAssetRegist> params) throws Exception {
		return underAssetRegistDao.deleteUnderAssetRegist(params).getEffect();
	}

	public void addImportUnderAssetRegist(UnderAssetRegistInfo underAssetRegistInfo, String opType) throws Exception {
		UnderAssetRegist underAssetRegist = BeanUtil.copyProperties(underAssetRegistInfo, UnderAssetRegist.class);
		underAssetRegist.setOpType(opType);
		underAssetRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		underAssetRegistDao.addImportUnderAssetRegist(underAssetRegist).getEffect();
	}
}
