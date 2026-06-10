package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.rpt.zz.manage.dao.AppraiseRegistInfoDao;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.AppraiseRegistInfo;
import com.kayak.rpt.zz.operate.service.AppraiseRegistService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "估值信息登记管理服务", model = AppraiseRegistInfo.class)
public class AppraiseRegistInfoService {

	@Autowired
	private AppraiseRegistInfoDao appraiseRegistInfoDao;

	// 操作记录
	@Autowired
	private AppraiseRegistService appraiseRegistService;

	@API(desc = "查询估值信息登记管理信息", auth = APIAuth.YES)
	public SqlResult<AppraiseRegistInfo> findAppraiseRegistInfos(SqlParam<AppraiseRegistInfo> params) throws Exception {
		params.setMakeSql(true);
		return appraiseRegistInfoDao.findAppraiseRegistInfos(params);
	}

	@API(desc = "添加估值信息登记管理",  auth = APIAuth.YES)
	public String addAppraiseRegistInfo(SqlParam<AppraiseRegistInfo> params) throws Exception {
		try {
			// 添加至操作记录
			appraiseRegistService.addAppraiseRegist(params,OperatorEnum.CREATE.getVal());
			appraiseRegistInfoDao.addAppraiseRegistInfo(params);
			return RequestSupport.updateReturnJson(true,  "添加成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "添加失败！", null).toString();
		}
	}

	@API(desc = "修改估值信息登记管理", auth = APIAuth.YES)
	public int updateAppraiseRegistInfo(SqlParam<AppraiseRegistInfo> params) throws Exception {
		// 添加至操作记录
		appraiseRegistService.addAppraiseRegist(params,OperatorEnum.UPDATE.getVal());
		return appraiseRegistInfoDao.updateAppraiseRegistInfo(params).getEffect();
	}

	@API(desc = "删除估值信息登记管理",  auth = APIAuth.YES)
	public int deleteAppraiseRegistInfo(SqlParam<AppraiseRegistInfo> params) throws Exception {
		// 添加至操作记录
		appraiseRegistService.addAppraiseRegist(params,OperatorEnum.DELETE.getVal());
		return appraiseRegistInfoDao.deleteAppraiseRegistInfo(params).getEffect();
	}


	@API(desc = "导入估值信息登记管理", auth = APIAuth.YES)
	public void importAppraiseRegistInfo(List<AppraiseRegistInfo> reportAppraise,Map<String, Object> params) throws Exception {
		// 添加至操作记录
		appraiseRegistInfoDao.deleteImportAppraiseRegistInfo(params);
		for (AppraiseRegistInfo appraiseRegistInfo : reportAppraise) {
			Map<String, Object> map = BeanUtil.beanToMap(appraiseRegistInfo);
			map.put("valuationDate", StringUtils.isEmpty(appraiseRegistInfo.getValuationDate())?null:appraiseRegistInfo.getValuationDate().replace("-", ""));
			appraiseRegistService.addImportAppraiseRegist(appraiseRegistInfo,OperatorEnum.IMPORT.getVal());
			appraiseRegistInfoDao.addImportAppraiseRegistInfo(map);
		}
	}


	@API(desc = "获取发行机构代码", auth = APIAuth.NO)
	public String findBankCode(SqlParam<AppraiseRegistInfo> params) throws Exception {
		String systemParamsByParaid = SysUtil.getSystemParamsByParaid("80000047");
		Map map = new HashMap();
		map.put("bankCode", systemParamsByParaid);
		return RequestSupport.updateReturnJson(true, "操作成功", map).toString();
	}

}
