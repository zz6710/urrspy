package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.app.model.TrusteeModel;
import com.kayak.dps.ods.dao.TrusteeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@APIDefine(desc = "托管行信息服务", model = TrusteeModel.class)
public class TrusteeService {

	@Autowired
	private TrusteeDao trusteeDao;

	@Resource(name = "assetCollectionService")
	private AssetCollectionService assetCollectionService;

	@API(desc = "查询托管行信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<TrusteeModel> findTrustee(SqlParam<TrusteeModel> params) throws Exception {
		return trusteeDao.findTrustee(params);
	}

	@API(desc = "新增托管行信息", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public String addTrustee(SqlParam<TrusteeModel> params) throws Exception {
		try {
			//校验唯一性
			Map<String, Object> param = new HashMap<>();
			param.put("checkTableName","ods_trustee_bas_inf");
			param.put("checkValue",params.getModel().getTrusteeCode());
			param.put("checkKey","TRUSTEE_CODE");
			if(assetCollectionService.isOnlyForOne(param)>0){
				return RequestSupport.updateReturnJson(false,  "该托管信息已存在！", null).toString();
			}
			params.getModel().setCrtDt(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setCrtUser((String) SysUtil.getSysUserParamValue("sys_user_userid"));
			trusteeDao.addTrustee(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "新增成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "新增失败！", null).toString();
		}
	}
	
	@API(desc = "修改托管行信息",  auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateTrustee(SqlParam<TrusteeModel> params) throws Exception {
		try {
			params.getModel().setUpdDt(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdUser((String) SysUtil.getSysUserParamValue("sys_user_userid"));
			trusteeDao.updateTrustee(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败！", null).toString();
		}
	}
	
	@API(desc = "删除托管行信息", auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int deleteTrustee(SqlParam<TrusteeModel> params) throws Exception {
		return trusteeDao.deleteTrustee(params).getEffect();
	}

}
