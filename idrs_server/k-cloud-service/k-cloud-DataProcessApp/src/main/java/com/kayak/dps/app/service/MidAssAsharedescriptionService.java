package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.dao.MidAssAsharedescriptionDao;
import com.kayak.dps.app.model.MidAssAsharedescription;
import com.kayak.dps.check.util.NextVersionUtil;
import com.kayak.dps.ods.service.AssetCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 股票
 * axin
 * 20230612
 */
@Service
@APIDefine(desc = "股票信息服务", model = MidAssAsharedescription.class)
public class MidAssAsharedescriptionService {

	@Autowired
	private MidAssAsharedescriptionDao midAssAsharedescriptionDao;
	@Resource(name = "assetCollectionService")
	private AssetCollectionService assetCollectionService;

	@API(desc = "查询股票代码信息", auth = APIAuth.NO)
	public SqlResult<MidAssAsharedescription> findFndCd(SqlParam<MidAssAsharedescription> params) throws Exception {
		params.setMakeSql(false);
		return midAssAsharedescriptionDao.findFndCd(params);
	}
	@API(desc = "查询股票代码信息", auth = APIAuth.NO)
	public SqlResult<MidAssAsharedescription> findScrCd(SqlParam<MidAssAsharedescription> params) throws Exception {
		params.setMakeSql(false);
		return midAssAsharedescriptionDao.findScrCd(params);
	}

	@API(desc = "查询融资企业名称", auth = APIAuth.NO)
	public SqlResult<MidAssAsharedescription> findCompanyName(SqlParam<MidAssAsharedescription> params) throws Exception {
		params.setMakeSql(false);
		return midAssAsharedescriptionDao.findCompanyName(params);
	}

	@API(desc = "查询股票信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<MidAssAsharedescription> findMidAssAsharedescriptions(SqlParam<MidAssAsharedescription> params) throws Exception {
		return midAssAsharedescriptionDao.findMidAssAsharedescriptions(params);
	}

	@API(desc = "新增股票信息", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public String addMidAssAsharedescription(SqlParam<MidAssAsharedescription> params) throws Exception {
		try {
			//校验唯一性
			Map<String, Object> param = new HashMap<>();
			param.put("checkTableName","ods_asharede_bas_inf");
			param.put("scrId",params.getModel().getScrId());
			if(assetCollectionService.isOnlyOne(param)>0){
				return RequestSupport.updateReturnJson(false,  "已存在有相同的股票信息，新增失败！", null).toString();
			}
//			int n=midAssAsharedescriptionDao.findAssetCount(params);
//			if(n>0){
//				return RequestSupport.updateReturnJson(false, "已存在有相同的股票信息，新增失败", null).toString();
//			}
			params.getModel().setCrtDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setCrtTime(DateUtil.getNowTime());
			params.getModel().setCrtUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setDealDate(DateUtil.getNowDate());
			midAssAsharedescriptionDao.addMidAssAsharedescription(params.getModel()).getEffect();
			return RequestSupport.updateReturnJson(true, "新增成功！", null).toString();
		}catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "新增失败！", null).toString();
		}
	}
	
	@API(desc = "修改股票信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateMidAssAsharedescription(SqlParam<MidAssAsharedescription> params) throws Exception {
		try {
			params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdTime(DateUtil.getNowTime());
			params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setVersion(NextVersionUtil.getNextVersion(params.getModel().getVersion()));
			params.getModel().setDealDate(DateUtil.getNowDate());
			midAssAsharedescriptionDao.updateMidAssAsharedescription(params.getModel()).getEffect();
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		}catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败！", null).toString();
		}
	}
	@API(desc = "补录股票信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateMidAssSupplyAsharedescription(SqlParam<MidAssAsharedescription> params) throws Exception {
		try {
			params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdTime(DateUtil.getNowTime());
			params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setVersion(NextVersionUtil.getNextVersion(params.getModel().getVersion()));
			params.getModel().setDealDate(DateUtil.getNowDate());
			midAssAsharedescriptionDao.updateMidAssAsharedescriptionBl(params.getModel()).getEffect();
			return RequestSupport.updateReturnJson(true,  "补录成功！", null).toString();
		}catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "补录失败！", null).toString();
		}
	}
	
	@API(desc = "删除股票信息", auth = APIAuth.YES, operation = APIOperation.DELETE)
	public String deleteMidAssAsharedescription(SqlParam<MidAssAsharedescription> params) throws Exception {
		try {
			midAssAsharedescriptionDao.deleteMidAssAsharedescription(params.getModel()).getEffect();
			return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
		}catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "删除失败！", null).toString();
		}
	}


	@API(desc = "导出股票信息", auth = APIAuth.YES,operation = APIOperation.UPDATE)
	public void ashExcelDownloadAction(SqlParam<MidAssAsharedescription> params) throws Exception {}

}
