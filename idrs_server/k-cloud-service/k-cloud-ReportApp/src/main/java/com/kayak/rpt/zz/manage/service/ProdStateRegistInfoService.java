package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.zz.manage.dao.ProdStateRegistInfoDao;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.AssetRegistInfo;
import com.kayak.rpt.zz.manage.model.ProdStateRegistInfo;
import com.kayak.rpt.zz.manage.model.UnderAssetRegistInfo;
import com.kayak.rpt.zz.operate.service.ProdStateRegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "产品状态管理服务", model = ProdStateRegistInfo.class)
public class ProdStateRegistInfoService {

	@Autowired
	private ProdStateRegistInfoDao ProdStateRegistInfoDao;

	@Autowired
	private ProdStateRegistService prodStateRegist;
	@Autowired
	protected DaoService daoService;

	@API(desc = "查询产品状态管理信息", auth = APIAuth.YES)
	public SqlResult<ProdStateRegistInfo> findProdStateRegistInfos(SqlParam<ProdStateRegistInfo> params) throws Exception {
//		params.setMakeSql(true);
		return ProdStateRegistInfoDao.findProdStateRegistInfos(params);
	}

	@API(desc = "添加产品状态管理", auth = APIAuth.YES)
	public int addProdStateRegistInfo(SqlParam<ProdStateRegistInfo> params) throws Exception {
		// 操作记录
//		prodStateRegist.addProdStateRegist(params, OperatorEnum.CREATE.getVal());
		return ProdStateRegistInfoDao.addProdStateRegistInfo(params).getEffect();
	}

	@API(desc = "修改产品状态管理",auth = APIAuth.YES)
	public int updateProdStateRegistInfo(SqlParam<ProdStateRegistInfo> params) throws Exception {
		// 操作记录
		Map paramMap = new HashMap<>();
		paramMap.put("registerSerno",params.getModel().getRegisterSerno());
		SqlParam<ProdStateRegistInfo> oldParams =  new FetcherData<>(paramMap,ProdStateRegistInfo.class);
		SqlResult<ProdStateRegistInfo> originParams =  ProdStateRegistInfoDao.findProdStateRegistInfos(oldParams);
		if(originParams.getRows().size()>0){
			ProdStateRegistInfo param = 	originParams.getRows().get(0);
			paramMap = BeanUtil.beanToMap(param);
			oldParams =  new FetcherData<>(paramMap,ProdStateRegistInfo.class);
		}

		prodStateRegist.addProdStateRegist(oldParams, OperatorEnum.UPDATE.getVal());
		return ProdStateRegistInfoDao.updateProdStateRegistInfo(params).getEffect();
	}

	@API(desc = "删除产品状态管理", auth = APIAuth.YES)
	public int deleteProdStateRegistInfo(SqlParam<ProdStateRegistInfo> params) throws Exception {
		// 操作记录

		prodStateRegist.addProdStateRegist(params, OperatorEnum.DELETE.getVal());
		return ProdStateRegistInfoDao.deleteProdStateRegistInfo(params).getEffect();
	}

	public void importProdStateRegistInfo(List<ProdStateRegistInfo> ProdStateRegistInfos,Map<String, Object> params) throws Exception {
		// 添加至操作记录
		ProdStateRegistInfoDao.deleteImportProdStateRegistInfo(params);
		for (ProdStateRegistInfo prodStateRegistInfo : ProdStateRegistInfos) {
			Map<String, Object> map = BeanUtil.beanToMap(prodStateRegistInfo);
//			prodStateRegist.addImportProdStateRegist(prodStateRegistInfo,OperatorEnum.IMPORT.getVal());
			ProdStateRegistInfoDao.addImportProdStateRegistInfo(map);
		}
	}
	@API(desc = "查询产品状态登记信息", auth = APIAuth.NO)
	public SqlResult<ProdStateRegistInfo> selectProdStateRegistInfos(SqlParam<ProdStateRegistInfo> params) throws Exception {

		return ProdStateRegistInfoDao.selectProdStateRegistInfos(params);
	}
	@API(desc = "查询报送状态为0,1的数据", auth = APIAuth.NO)
	public String getAbnormalData(SqlParam<ProdStateRegistInfo> params) throws Exception {
		try {
			int  recordCnt = ProdStateRegistInfoDao.findProdStateRegistInfosCount(params);
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认并导出的数据，请检查！", null).toString();
			}
			int unreadyCnt= ProdStateRegistInfoDao.findProdStateRegistInfoFailStatus(params);
			if (unreadyCnt > 0) {
				return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后导出！", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "查询失败，请检查", null).toString();
		}
	}

	@API(desc = "确认并导出产品状态登记",  auth = APIAuth.YES)
	public String updateProdStateRegistInfoStatus(SqlParam<ProdStateRegistInfo> params) throws Exception {
		try {
			daoService.doTrans(() -> {
				ProdStateRegistInfoDao.updateProdStateRegistInfoStatus(params);
				ProdStateRegistInfoDao.updateBaseReportResultInfo(params);
			});
			return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "操作失败，请检查!", null).toString();
		}
	}
}
