package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import com.kayak.rpt.zz.operate.model.ProdIssuanceRegist;
import org.apache.commons.lang3.StringUtils;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.zz.manage.dao.ProdIssRegistCompareDao;
import com.kayak.rpt.zz.manage.dao.ProdIssuanceRegistInfoDao;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.ProdIssuanceRegistInfo;
import com.kayak.rpt.zz.operate.service.ProdIssuanceRegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "产品发行登记信息管理服务", model = ProdIssuanceRegistInfo.class)
public class ProdIssuanceRegistInfoService {

	@Autowired
	private ProdIssuanceRegistInfoDao prodIssuanceRegistInfoDao;

	@Autowired
	private ProdIssRegistCompareDao prodIssRegistCompareDao;

	@Autowired
	private ProdIssuanceRegistService prodIssuanceRegistService;
	@Autowired
	protected DaoService daoService;
	@Autowired
	CheckDataForVueService checkDataForVueService;

	CheckDataParams checkDataParams = new CheckDataParams();

	@API(desc = "查询产品发行登记信息管理信息", auth = APIAuth.YES)
	public SqlResult<ProdIssuanceRegistInfo> findProdIssuanceRegistInfos1(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
//		params.setMakeSql(true);
		return prodIssuanceRegistInfoDao.findProdIssuanceRegistInfos(params);
	}

	@API(desc = "查询发行登记数据及字段变更标识", auth = APIAuth.YES)
	public SqlResult<ProdIssuanceRegistInfo> findProdIssuanceRegistInfos(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		SqlResult<ProdIssuanceRegistInfo> r1=  prodIssuanceRegistInfoDao.findProdIssuanceRegistInfos(params);
		List<ProdIssuanceRegistInfo> returnList = new ArrayList<>();
		if(r1 != null && r1.getRows() != null &&r1.getRows().size() > 0){
			List<ProdIssuanceRegistInfo> list0 = new ArrayList<>(),list1 = new ArrayList<>();
			list0 = r1.getRows();//原始数据
			StringBuffer prods = new StringBuffer();
			for(int i = 0;i<list0.size();i++){
				ProdIssuanceRegistInfo prodIssuanceRegistInfo = list0.get(i);
				if(i == list0.size()-1){
					prods.append("'"+prodIssuanceRegistInfo.getProdCode()+"'");
				}else{
					prods.append("'"+prodIssuanceRegistInfo.getProdCode()+"',");
				}
			}

			list1 = prodIssuanceRegistInfoDao.findProdIssuanceRegistInfosByprod(String.valueOf(prods),params).getRows();//指定产品的数据集

			for(int i = 0;i<list0.size();i++){
				ProdIssuanceRegistInfo prodIssuanceRegistInfo = list0.get(i);
				ProdIssuanceRegistInfo prodIssuance2 = prodIssRegistCompareDao.compareFlag(prodIssuanceRegistInfo,list1);
				returnList.add(prodIssuance2);
			}
		}
		r1.setRowsList1(returnList);
		return r1;
	}


	@API(desc = "添加产品发行登记信息管理", auth = APIAuth.YES)
	public int addProdIssuanceRegistInfo(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		// 操作记录 （只有修改、删除插入操作记录，导入导出不添加操作记录；修改插入的操作记录-登记的信息是操作前的）
//		prodIssuanceRegistService.addProdIssuanceRegist(params, OperatorEnum.CREATE.getVal());
		return prodIssuanceRegistInfoDao.addProdIssuanceRegistInfo(params).getEffect();
	}
	
	@API(desc = "修改产品发行登记信息管理",  auth = APIAuth.YES)
	public String updateProdIssuanceRegistInfo(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		try {
			checkDataParams.initDataNoDict();
			String whiteregex = CheckDataParams.whiteregex;
			String whitereForCode = CheckDataParams.whitereForCode;
			String checkErr = checkDataForVueService.prodIssuanceRegistInfoCheckForVue(whiteregex,whitereForCode,params.getModel());
			if (StringUtils.isNotBlank(checkErr)) {
				return RequestSupport.updateReturnJson(false,  "修改失败！错误信息为：\n"+checkErr, null).toString();
			}
			// 操作记录 （插入的是修改前的数据，需先查询获取修改前的登记信息）   根据主键查询唯一值，需要前期列表查询中有id
			SqlResult<ProdIssuanceRegistInfo> sqlResultOne = prodIssuanceRegistInfoDao.findOldProdIssuanceRegistInfoById(params);
			prodIssuanceRegistService.addOldProdIssuanceRegist(sqlResultOne.getRows().get(0), OperatorEnum.UPDATE.getVal());
//			prodIssuanceRegistService.addProdIssuanceRegist(params, OperatorEnum.UPDATE.getVal());
			prodIssuanceRegistInfoDao.updateProdIssuanceRegistInfo(params);
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败，数据库错误信息为："+e.getMessage(), null).toString();
		}
	}
	@API(desc = "查询报送状态为0,1的数据", auth = APIAuth.NO)
	public String getAbnormalData(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		try {
			int  recordCnt = prodIssuanceRegistInfoDao.findProdIssuanceRegistInfosCount(params);
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认并导出的数据，请检查！", null).toString();
			}
			int unreadyCnt= prodIssuanceRegistInfoDao.findProdIssuanceRegistInfosStatus(params);
			if (unreadyCnt > 0) {
				return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后导出！", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "查询失败，请检查", null).toString();
		}
	}
	@API(desc = "确认并导出发行",  auth = APIAuth.YES)
	public String updateProdIssuanceRegistInfoStatus(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		try {
			String prod_code = prodIssuanceRegistInfoDao.getProdCode(params);
			if(StringUtils.isNotBlank(prod_code)){
				daoService.doTrans(() -> {
					prodIssuanceRegistInfoDao.updateProdDataFlag(params,prod_code);
					prodIssuanceRegistInfoDao.updateProdStat(params,prod_code);
					prodIssuanceRegistInfoDao.updateProdIssuanceRegistInfoStatus(params,prod_code);
					prodIssuanceRegistInfoDao.updateBaseReportResultInfo(params);

				});
			}else{
				return RequestSupport.updateReturnJson(false,  "操作失败，产品代码集合为空!", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "操作失败，请检查!", null).toString();
		}
	}
	
	@API(desc = "删除产品发行登记信息管理", auth = APIAuth.YES)
	public int deleteProdIssuanceRegistInfo(SqlParam<ProdIssuanceRegistInfo> params) throws Exception {
		// 操作记录
		prodIssuanceRegistService.addProdIssuanceRegist(params, OperatorEnum.DELETE.getVal());
		return prodIssuanceRegistInfoDao.deleteProdIssuanceRegistInfo(params).getEffect();
	}
	@API(desc = "导入产品发行登记信息管理信息", auth = APIAuth.YES)
	public void importProdIssuanceRegistInfo(List<ProdIssuanceRegistInfo> prodIssuanceRegistInfos,Map<String, Object> params) throws Exception {
//		prodIssuanceRegistInfoDao.deleteImportProdIssuanceRegistInfo(params);
		for (ProdIssuanceRegistInfo prodIssuanceRegistInfo : prodIssuanceRegistInfos) {
			Map<String, Object> map = BeanUtil.beanToMap(prodIssuanceRegistInfo);
			// 添加至操作记录 （只有修改删除插入操作记录，导入导出不添加操作记录）
//			prodIssuanceRegistService.addImportProdIssuanceRegist(prodIssuanceRegistInfo,OperatorEnum.IMPORT.getVal());
			prodIssuanceRegistInfoDao.addImportProdIssuanceRegistInfo(map);
		}
	}
}
