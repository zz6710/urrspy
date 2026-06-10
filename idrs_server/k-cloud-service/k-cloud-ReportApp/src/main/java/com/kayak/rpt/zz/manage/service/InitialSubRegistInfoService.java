package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.zz.manage.dao.InitialSubRegistCompareDao;
import com.kayak.rpt.zz.manage.dao.InitialSubRegistInfoDao;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.InitialSubRegistInfo;
import com.kayak.rpt.zz.manage.model.ProdRegistFilingInfo;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import com.kayak.rpt.zz.operate.service.InitialSubRegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "募集总量登记管理服务", model = InitialSubRegistInfo.class)
public class InitialSubRegistInfoService {

	@Autowired
	private InitialSubRegistInfoDao initialSubRegistInfoDao;

	@Autowired
	private InitialSubRegistService initialSubRegistService;

	@Autowired
	private ZonClcInfoService zonClcInfoService;
	@Autowired
	private InitialSubRegistCompareDao initialSubRegistCompareDao;
	@Autowired
	protected DaoService daoService;
	@Autowired
	CheckDataForVueService checkDataForVueService;

	CheckDataParams checkDataParams = new CheckDataParams();
	@API(desc = "查询募集总量登记管理信息", auth = APIAuth.YES)
	public SqlResult<InitialSubRegistInfo> findInitialSubRegistInfos1(SqlParam<InitialSubRegistInfo> params) throws Exception {
//		params.setMakeSql(true);
		return initialSubRegistInfoDao.findInitialSubRegistInfos(params);
	}

	@API(desc = "查询募集总量登记管理信息及字段变更标识", auth = APIAuth.YES)
	public SqlResult<InitialSubRegistInfo> findInitialSubRegistInfos(SqlParam<InitialSubRegistInfo> params) throws Exception {
		SqlResult<InitialSubRegistInfo> r1=  initialSubRegistInfoDao.findInitialSubRegistInfos(params);
		List<InitialSubRegistInfo> returnList = new ArrayList<>();
		if(r1 != null && r1.getRows() != null &&r1.getRows().size() > 0){
			List<InitialSubRegistInfo> list0 = new ArrayList<>(),list1 = new ArrayList<>();
			list0 = r1.getRows();//原始数据
			StringBuffer prods = new StringBuffer();
			for(int i = 0;i<list0.size();i++){
				InitialSubRegistInfo initialSubRegistInfo = list0.get(i);
				if(i == list0.size()-1){
					prods.append("'"+initialSubRegistInfo.getProdCode()+"'");
				}else{
					prods.append("'"+initialSubRegistInfo.getProdCode()+"',");
				}
			}

			list1 = initialSubRegistInfoDao.findInitialSubRegistByprod(String.valueOf(prods),params).getRows();//指定产品的数据集

			for(int i = 0;i<list0.size();i++){
				InitialSubRegistInfo initialSubRegistInfo = list0.get(i);
				InitialSubRegistInfo prodIssuance2 = initialSubRegistCompareDao.compareFlag(initialSubRegistInfo,list1);
				returnList.add(prodIssuance2);
			}
		}
		r1.setRowsList1(returnList);
		return r1;
	}


	@API(desc = "添加募集总量登记管理", auth = APIAuth.YES)
	public int addInitialSubRegistInfo(SqlParam<InitialSubRegistInfo> params) throws Exception {
		// 增加操作记录
//		initialSubRegistService.addInitialSubRegist(params, OperatorEnum.CREATE.getVal());
		return initialSubRegistInfoDao.addInitialSubRegistInfo(params).getEffect();
	}
	
	@API(desc = "修改募集总量登记管理",auth = APIAuth.YES)
	public String updateInitialSubRegistInfo(SqlParam<InitialSubRegistInfo> params) throws Exception {
		try {
			checkDataParams.initDataNoDict();
			String whiteregex = CheckDataParams.whiteregex;
			String whitereForCode = CheckDataParams.whitereForCode;
			String checkErr = checkDataForVueService.initialSubRegistInfoCheckForVue(whiteregex,whitereForCode,params.getModel());
			if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
				return RequestSupport.updateReturnJson(false,  "修改失败！错误信息为：\n"+checkErr, null).toString();
			}
			// 操作记录  列表查询中需要有id
			SqlResult<InitialSubRegistInfo> sqlResult = initialSubRegistInfoDao.findInitialSubRegistInfoSingleById(params);
			initialSubRegistService.addOldInitialSubRegist(sqlResult.getRows().get(0), OperatorEnum.UPDATE.getVal());
//			initialSubRegistService.addInitialSubRegist(params, OperatorEnum.UPDATE.getVal());
			initialSubRegistInfoDao.updateInitialSubRegistInfo(params);
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败，数据库错误信息为："+e.getMessage(), null).toString();
		}
	}

	@API(desc = "查询报送状态为0,1的数据", auth = APIAuth.NO)
	public String getAbnormalData(SqlParam<InitialSubRegistInfo> params) throws Exception {
		try {
			int  recordCnt = initialSubRegistInfoDao.findInitialSubRegistInfosCount(params);
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认并导出的数据，请检查！", null).toString();
			}
			int unreadyCnt= initialSubRegistInfoDao.findInitialSubRegistInfoFailStatus(params);
			if (unreadyCnt > 0) {
				return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后导出！", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "查询失败，请检查", null).toString();
		}
	}

	@API(desc = "确认并导出募集",auth = APIAuth.YES)
	public String updateInitialSubRegistInfoStatus(SqlParam<InitialSubRegistInfo> params) throws Exception {
		try {
			String prod_code = initialSubRegistInfoDao.getProdCode(params);
			if (!StringUtils.isEmpty(prod_code)) {
				daoService.doTrans(() -> {
					initialSubRegistInfoDao.updateProdStat(params, prod_code);
					initialSubRegistInfoDao.updateInitialSubRegistInfoStatus(params, prod_code);
					initialSubRegistInfoDao.updateBaseReportResultInfo(params);
					initialSubRegistInfoDao.updateProdDataFlag(params, prod_code);
				});
			} else {
				return RequestSupport.updateReturnJson(false,  "操作失败，产品集合为空!", null).toString();
			}

			return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "操作失败，请检查!", null).toString();
		}
	}
	
	@API(desc = "删除募集总量登记管理", auth = APIAuth.YES)
	public int deleteInitialSubRegistInfo(SqlParam<InitialSubRegistInfo> params) throws Exception {
		initialSubRegistService.addInitialSubRegist(params, OperatorEnum.DELETE.getVal());
		return initialSubRegistInfoDao.deleteInitialSubRegistInfo(params).getEffect();
	}

	@API(desc = "导入募集总量登记管理",auth = APIAuth.YES)
	public void importInitialSubRegistInfo(List<InitialSubRegistInfo> initialSubRegistInfos,Map<String, Object> params) throws Exception {
		initialSubRegistInfoDao.deleteImportInitialSubRegistInfo(params);
		for (InitialSubRegistInfo initialSubRegistInfo : initialSubRegistInfos) {
			Map<String, Object> map = BeanUtil.beanToMap(initialSubRegistInfo);
			// 添加至操作记录
//			initialSubRegistService.addImportInitialSubRegist(initialSubRegistInfo,OperatorEnum.IMPORT.getVal());
			initialSubRegistInfoDao.addImportInitialSubRegistInfo(map);
			//zonClcInfoService.addImportZonClcInfo(initialSubRegistInfo);
		}
	}

}
