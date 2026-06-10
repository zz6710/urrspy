package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.dao.DaoService;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.ProdStateRegistInfo;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import com.kayak.rpt.zz.operate.service.SubseqSubscrRegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.SubseqSubscrRegistInfoDao;
import com.kayak.rpt.zz.manage.model.SubseqSubscrRegistInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "产品存续期登记管理服务", model = SubseqSubscrRegistInfo.class)
public class SubseqSubscrRegistInfoService {

	@Autowired
	private SubseqSubscrRegistInfoDao subseqSubscrRegistInfoDao;

	@Autowired
	private SubseqSubscrRegistService subseqSubscrRegistService;

	@Autowired
	protected DaoService daoService;
	@Autowired
	CheckDataForVueService checkDataForVueService;

	CheckDataParams checkDataParams = new CheckDataParams();

	@API(desc = "查询产品存续期登记管理信息", auth = APIAuth.YES)
	public SqlResult<SubseqSubscrRegistInfo> findSubseqSubscrRegistInfos(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
//		params.setMakeSql(true);
		return subseqSubscrRegistInfoDao.findSubseqSubscrRegistInfos(params);
	}

	@API(desc = "添加产品存续期登记管理", auth = APIAuth.YES)
	public int addSubseqSubscrRegistInfo(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		// 操作记录
//		subseqSubscrRegistService.addSubseqSubscrRegist(params, OperatorEnum.CREATE.getVal());
		return subseqSubscrRegistInfoDao.addSubseqSubscrRegistInfo(params).getEffect();
	}
	
	@API(desc = "修改产品存续期登记管理",  auth = APIAuth.YES)
	public String updateSubseqSubscrRegistInfo(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		try {
			checkDataParams.initDataNoDict();
			String whiteregex = CheckDataParams.whiteregex;
			String whitereForCode = CheckDataParams.whitereForCode;
			String checkErr = checkDataForVueService.subseqSubscrRegistInfoCheckForVue(whiteregex,whitereForCode,params.getModel());
			if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
				return RequestSupport.updateReturnJson(false,  "修改失败！错误信息为：\n"+checkErr, null).toString();
			}
			// 操作记录
			Map paramMap = new HashMap<>();
			paramMap.put("registerSerno",params.getModel().getRegisterSerno());
			SqlParam<SubseqSubscrRegistInfo> oldParams =  new FetcherData<>(paramMap,SubseqSubscrRegistInfo.class);
			SqlResult<SubseqSubscrRegistInfo> originParams =  subseqSubscrRegistInfoDao.findSubseqSubscrRegistInfos(oldParams);
			if(originParams.getRows().size()>0){
				SubseqSubscrRegistInfo param = 	originParams.getRows().get(0);
				paramMap = BeanUtil.beanToMap(param);
				oldParams =  new FetcherData<>(paramMap,SubseqSubscrRegistInfo.class);
			}
			subseqSubscrRegistService.addSubseqSubscrRegist(oldParams, OperatorEnum.UPDATE.getVal());
			subseqSubscrRegistInfoDao.updateSubseqSubscrRegistInfo(params);
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败，数据库错误信息为："+e.getMessage(), null).toString();
		}
	}

	@API(desc = "查询报送状态为0,1的数据", auth = APIAuth.NO)
	public String getAbnormalData(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		try {
			int  recordCnt = subseqSubscrRegistInfoDao.findSubseqSubscrRegistInfosCount(params);
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认并导出的数据，请检查！", null).toString();
			}
			int unreadyCnt= subseqSubscrRegistInfoDao.findSubseqSubscrRegistInfoFailStatus(params);
			if (unreadyCnt > 0) {
				return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后导出！", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "查询失败，请检查", null).toString();
		}
	}

	@API(desc = "确认并导出存续",  auth = APIAuth.YES)
	public String updateSubseqSubscrRegistInfoStatus(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		try {
			daoService.doTrans(() -> {
				subseqSubscrRegistInfoDao.updateSubseqSubscrRegistInfoStatus(params);
				subseqSubscrRegistInfoDao.updateBaseReportResultInfo(params);
			});
			return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "操作失败，请检查!", null).toString();
		}
	}
	
	@API(desc = "删除产品存续期登记管理", auth = APIAuth.YES)
	public int deleteSubseqSubscrRegistInfo(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		// 操作记录
		subseqSubscrRegistService.addSubseqSubscrRegist(params, OperatorEnum.DELETE.getVal());
		return subseqSubscrRegistInfoDao.deleteSubseqSubscrRegistInfo(params).getEffect();
	}

	public void subseqSubscrRegistInfoService(List<SubseqSubscrRegistInfo> subseqSubscrRegistInfos,Map<String, Object> params) throws Exception {
		// 添加至操作记录
		subseqSubscrRegistInfoDao.deleteImportSubseqSubscrRegistInfo(params);
		for (SubseqSubscrRegistInfo subseqSubscrRegistInfo : subseqSubscrRegistInfos) {
			Map<String, Object> map = BeanUtil.beanToMap(subseqSubscrRegistInfo);
//			subseqSubscrRegistService.addImportSubseqSubscrRegist(subseqSubscrRegistInfo,OperatorEnum.IMPORT.getVal());
			subseqSubscrRegistInfoDao.addImportSubseqSubscrRegistInfo(map);
		}
	}
}
