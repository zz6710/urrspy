package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.dao.MidTrmDpsInfDao;
import com.kayak.dps.app.model.MidTrmDpsInf;
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
 * 存款
 * axin
 * 20230612
 */

@Service
@APIDefine(desc = "定期存款服务", model = MidTrmDpsInf.class)
public class MidTrmDpsInfService {

	@Autowired
	private MidTrmDpsInfDao midTrmDpsInfDao;

	@Resource(name = "assetCollectionService")
	private AssetCollectionService assetCollectionService;

	@API(desc = "查询资产代码", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<MidTrmDpsInf> findScrCd(SqlParam<MidTrmDpsInf> params) throws Exception {
		params.setMakeSql(false);
		return midTrmDpsInfDao.findScrCd(params);
	}

	@API(desc = "查询定期存款", auth = APIAuth.YES,operation = APIOperation.SELECT)
	public SqlResult<MidTrmDpsInf> findMidTrmDpsInfs(SqlParam<MidTrmDpsInf> params) throws Exception {
		return midTrmDpsInfDao.findMidTrmDpsInfs(params);
	}

	@API(desc = "新增定期存款", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public String addMidTrmDpsInf(SqlParam<MidTrmDpsInf> params) throws Exception {
		try {
			//校验唯一性
			Map<String, Object> param = new HashMap<>();
			param.put("checkTableName","ods_trm_bas_inf");
			param.put("scrId",params.getModel().getScrId());
			if(assetCollectionService.isOnlyOne(param)>0){
				return RequestSupport.updateReturnJson(false,  "该定期存款已存在！", null).toString();
			}
			params.getModel().setCrtDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setCrtTime(DateUtil.getNowTime());
			params.getModel().setCrtUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setDealDate(DateUtil.getNowDate());
			midTrmDpsInfDao.addMidTrmDpsInf(params.getModel());
			return RequestSupport.updateReturnJson(true,"新增成功",null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,"新增失败！" + e.getMessage() ,null).toString();
		}
	}
	
	@API(desc = "修改定期存款", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateMidTrmDpsInf(SqlParam<MidTrmDpsInf> params) throws Exception {
		try {
			params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdTime(DateUtil.getNowTime());
			params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setDealDate(DateUtil.getNowDate());
			midTrmDpsInfDao.updateMidTrmDpsInf(params.getModel());
			return RequestSupport.updateReturnJson(true,"修改成功",null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,"修改失败！" + e.getMessage() ,null).toString();
		}
	}

	@API(desc = "补录定期存款", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateMidTrmSupplyDpsInf(SqlParam<MidTrmDpsInf> params) throws Exception {
		try {
			params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdTime(DateUtil.getNowTime());
			params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setVersion(NextVersionUtil.getNextVersion(params.getModel().getVersion()));
			params.getModel().setDealDate(DateUtil.getNowDate());
			midTrmDpsInfDao.updateMidTrmDpsInfBl(params.getModel());
			return RequestSupport.updateReturnJson(true,"补录成功",null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,"补录失败！" + e.getMessage() ,null).toString();
		}
	}
	
	@API(desc = "删除定期存款", auth = APIAuth.YES, operation = APIOperation.DELETE)
	public String deleteMidTrmDpsInf(SqlParam<MidTrmDpsInf> params) throws Exception {
		try {
			midTrmDpsInfDao.deleteMidTrmDpsInf(params.getModel());
			return RequestSupport.updateReturnJson(true,"删除成功",null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,"删除失败！" + e.getMessage() ,null).toString();
		}
	}
	@API(desc = "导入定期存款", auth = APIAuth.YES,operation = APIOperation.UPDATE)
	public void trmExcelUploadAction(SqlParam<MidTrmDpsInf> params) throws Exception {}
	@API(desc = "导出定期存款", auth = APIAuth.YES,operation = APIOperation.UPDATE)
	public void trmExcelDownloadAction(SqlParam<MidTrmDpsInf> params) throws Exception {}

}
