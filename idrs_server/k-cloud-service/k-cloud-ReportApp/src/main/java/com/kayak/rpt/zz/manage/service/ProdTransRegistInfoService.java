package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.FileUtil;
import com.kayak.core.util.PublicUtils;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.rhzj.util.ExcelParse;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.UnderAssetRegistInfo;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import com.kayak.rpt.zz.operate.service.ProdTransRegistService;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.ProdTransRegistInfoDao;
import com.kayak.rpt.zz.manage.model.ProdTransRegistInfo;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
@APIDefine(desc = "交易信息登记管理服务", model = ProdTransRegistInfo.class)
public class ProdTransRegistInfoService {

	@Value("${zg.query.reflect.upd}")
	private String reflectUpdSql;
	@Value("${zg.query.org_info.upd}")
	private String orgInfoUpdSql;
	@Value("${zg.query.asset_info.upd}")
	private String assetInfoUpdSql;
	@Value("${zg.query.memo.upd1}")
	private String detailsUpdSql1;
	@Value("${zg.query.memo.upd2}")
	private String detailsUpdSql2;

	@Autowired
	private ProdTransRegistInfoDao prodTransRegistInfoDao;
	@Autowired
	CheckDataForVueService checkDataForVueService;
	@Autowired
	protected DaoService daoService;
	CheckDataParams checkDataParams = new CheckDataParams();

	@Autowired
	private ProdTransRegistService prodTransRegistService;
	@API(desc = "查询交易信息登记管理信息", auth = APIAuth.YES)
	public SqlResult<ProdTransRegistInfo> findProdTransRegistInfos(SqlParam<ProdTransRegistInfo> params) throws Exception {
		/*String isUpdate = SysUtil.getSystemParamsByParaid("is_zg_upd");//是否更新交易信息登记
		if("1".equals(isUpdate)){
			updateTradeInfoReportData();//更新交易信息登记
		}*/
//		params.setMakeSql(true);
		return prodTransRegistInfoDao.findProdTransRegistInfos(params);
	}

	@API(desc = "添加交易信息登记管理", auth = APIAuth.YES)
	public int addProdTransRegistInfo(SqlParam<ProdTransRegistInfo> params) throws Exception {
		// 操作记录
//		prodTransRegistService.addProdTransRegist(params, OperatorEnum.CREATE.getVal());
		return prodTransRegistInfoDao.addProdTransRegistInfo(params).getEffect();
	}
	
	@API(desc = "修改交易信息登记管理", auth = APIAuth.YES)
	public String updateProdTransRegistInfo(SqlParam<ProdTransRegistInfo> params) throws Exception {
		try {
		checkDataParams.initDataNoDict();
		String whiteregex = CheckDataParams.whiteregex;
		String whitereForCode = CheckDataParams.whitereForCode;
		String checkErr = checkDataForVueService.prodTransRegistInfoCheckForVue(whiteregex,whitereForCode,params.getModel());
		if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
			return RequestSupport.updateReturnJson(false,  "修改失败！错误信息为：\n"+checkErr, null).toString();
		}
		// 操作记录
		Map paramMap = new HashMap<>();
		paramMap.put("registerSerno",params.getModel().getRegisterSerno());
		SqlParam<ProdTransRegistInfo> oldParams =  new FetcherData<>(paramMap,ProdTransRegistInfo.class);
		SqlResult<ProdTransRegistInfo> originParams = prodTransRegistInfoDao.findProdTransRegistInfos(oldParams);
		if(originParams.getRows().size()>0){
			ProdTransRegistInfo param = 	originParams.getRows().get(0);
			paramMap = BeanUtil.beanToMap(param);
			oldParams =  new FetcherData<>(paramMap,ProdTransRegistInfo.class);
		}
		prodTransRegistService.addProdTransRegist(oldParams, OperatorEnum.UPDATE.getVal());
		prodTransRegistInfoDao.updateProdTransRegistInfo(params);
		return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e) {
		e.printStackTrace();
		return RequestSupport.updateReturnJson(false,  "修改失败，数据库错误信息为："+e.getMessage(), null).toString();
		}
	}
	
	@API(desc = "删除交易信息登记管理", auth = APIAuth.YES)
	public int deleteProdTransRegistInfo(SqlParam<ProdTransRegistInfo> params) throws Exception {
		// 操作记录
		prodTransRegistService.addProdTransRegist(params, OperatorEnum.DELETE.getVal());
		return prodTransRegistInfoDao.deleteProdTransRegistInfo(params).getEffect();
	}

	public String fileStatusQuery(SqlParam<ProdTransRegistInfo> params) throws Exception {
		Map<String, Object> returndata = new HashMap<>();
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("status", "0");
			List<SqlRow> sqlRows = prodTransRegistInfoDao.findImportMenuFileManage(param);
			if (CollectionUtil.isNotEmpty(sqlRows)) {
				returndata.put("flag", "2");
				return RequestSupport.updateReturnJson(true, "审批流程中存在未审核的批量修改导入文件，请审批后重新导入！", returndata).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			returndata.put("flag", "1");
			return RequestSupport.updateReturnJson(false, e.getMessage(), returndata).toString();
		}
		returndata.put("flag", "0");
		return RequestSupport.updateReturnJson(true, "", returndata).toString();
	}

	@API(desc = "批量修改导入", auth = APIAuth.YES)
	public String prodTransRegistImport(SqlParam<ProdTransRegistInfo> params) throws Exception {
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("id", params.getModel().getId());
			List<SqlRow> sqlRows = prodTransRegistInfoDao.findImportMenuFileManage(param);
			if (CollectionUtil.isNotEmpty(sqlRows)) {
				String fileName = (String) sqlRows.get(0).get("file_name");
				String localFilePath = (String) sqlRows.get(0).get("local_file_path");
				// 从oss下载
				File tmpFile = File.createTempFile("prefix", fileName);
				File file = new File(tmpFile.getParent() + File.separator + fileName);
				FileTransfer fileTransfer = new FileTransferHelpler().getTransfer();
				fileTransfer.downloadFileAndDisconnect(localFilePath, file.getAbsolutePath());
				// 解析文件
				List<ProdTransRegistInfo> prodTransRegistInfos = ExcelParse.readExcelData(new FileInputStream(file), 0, 0, 0, ProdTransRegistInfo.class, true, null);
				// 移除第一标题行
				prodTransRegistInfos.remove(0);
				// 处理数据
				for (ProdTransRegistInfo prodTransRegistInfo : prodTransRegistInfos) {
					prodTransRegistInfo.setCashType(StringUtils.isEmpty(prodTransRegistInfo.getCashType())?null:prodTransRegistInfo.getCashType().split(" ")[0]);
					prodTransRegistInfo.setCur(StringUtils.isEmpty(prodTransRegistInfo.getCur())?null:prodTransRegistInfo.getCur().split(" ")[0]);
					prodTransRegistInfo.setRelatedPartyTrans(StringUtils.isEmpty(prodTransRegistInfo.getRelatedPartyTrans())?null:prodTransRegistInfo.getRelatedPartyTrans().split(" ")[0]);
					prodTransRegistInfo.setCounterType(StringUtils.isEmpty(prodTransRegistInfo.getCounterType())?null:prodTransRegistInfo.getCounterType().split(" ")[0]);
					prodTransRegistInfo.setQuantity(StringUtils.isEmpty(prodTransRegistInfo.getQuantity()) ?"0":reverseToString(prodTransRegistInfo.getQuantity()));
					prodTransRegistInfo.setMethodAssetMeasure(StringUtils.isEmpty(prodTransRegistInfo.getMethodAssetMeasure())?null:prodTransRegistInfo.getMethodAssetMeasure().split(" ")[0]);
					prodTransRegistInfo.setConvertRmb(StringUtils.isEmpty(prodTransRegistInfo.getConvertRmb()) ?"0":reverseToString(prodTransRegistInfo.getConvertRmb()));
					prodTransRegistInfo.setAmt(StringUtils.isEmpty(prodTransRegistInfo.getAmt()) ?"0":reverseToString(prodTransRegistInfo.getAmt()));
					prodTransRegistInfo.setUnitPriceFull(StringUtils.isEmpty(prodTransRegistInfo.getUnitPriceFull()) ?"0":reverseToString(prodTransRegistInfo.getUnitPriceFull()));
					prodTransRegistInfo.setUnitPriceNet(StringUtils.isEmpty(prodTransRegistInfo.getUnitPriceNet()) ?"0":reverseToString(prodTransRegistInfo.getUnitPriceNet()));
					prodTransRegistInfo.setRateAnnualReturn(StringUtils.isEmpty(prodTransRegistInfo.getRateAnnualReturnStr()) ?0:Double.valueOf(reverseToString(prodTransRegistInfo.getRateAnnualReturnStr())));
					prodTransRegistInfo.setTheoryReportStartDate(StringUtils.isEmpty(prodTransRegistInfo.getTradeDate())?null:prodTransRegistInfo.getTradeDate().replace("-",""));
					prodTransRegistInfo.setIsCover(params.getModel().getIsCover());
//					prodTransRegistInfo.setRegisterStatus("0");
				}
				importProdTransRegistInfo(prodTransRegistInfos);
				Map<String, Object> param1 = new HashMap<>();
				param1.put("id", params.getModel().getId());
				param1.put("status", "1");
				prodTransRegistInfoDao.updImportMenuFileManage(param1);
				FileUtil.delFile(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "批量修改导入失败"+e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "批量修改导入成功", null).toString();
	}

	public List<SqlRow> findImportMenuFileManageId(Map<String, Object> params) throws Exception {
		return prodTransRegistInfoDao.findImportMenuFileManageId(params);
	}

	public List<SqlRow> findImportMenuFileManage(Map<String, Object> params) throws Exception {
		return prodTransRegistInfoDao.findImportMenuFileManage(params);
	}

	public int addImportMenuFileManage(Map<String, Object> params) throws Exception {
		return prodTransRegistInfoDao.addImportMenuFileManage(params).getEffect();
	}

	public int updImportMenuFileManage(Map<String, Object> params) throws Exception {
		return prodTransRegistInfoDao.updImportMenuFileManage(params).getEffect();
	}

	// 读取到科学计数法转化
	private String reverseToString(String actualProdTerDate) {
		BigDecimal bd = new BigDecimal(actualProdTerDate);
		return bd.toPlainString();
	}
    //批量修改导入
	public void importProdTransRegistInfo(List<ProdTransRegistInfo> prodTransRegistInfos) throws Exception {
        // 添加至操作记录
		for (ProdTransRegistInfo prodTransRegistInfo : prodTransRegistInfos) {
			Map<String, Object> map = BeanUtil.beanToMap(prodTransRegistInfo);
//			prodTransRegistService.addimportProdTransRegist(prodTransRegistInfo,OperatorEnum.UPDATE.getVal());
			prodTransRegistInfoDao.updateimportProdTransRegistInfo(map);
		}
	}

	/**
	 * 更新交易登记报表
	 * @throws Exception
	 */
	public void updateTradeInfoReportData () throws Exception {
		String workday = PublicUtils.getSysWordDay();
		List<String> updSqlList = new ArrayList<>(Arrays.asList(reflectUpdSql,orgInfoUpdSql,assetInfoUpdSql,detailsUpdSql1,detailsUpdSql2));
		Map<String, Object> params = new HashMap<>();
		params.put("workday", workday);
		prodTransRegistInfoDao.executeUpdSql(updSqlList, params);
	}
	@API(desc = "查询报送状态为0,1的数据", auth = APIAuth.NO)
	public String getAbnormalData(SqlParam<ProdTransRegistInfo> params) throws Exception {
		try {
			int  recordCnt = prodTransRegistInfoDao.findProdTransRegistInfosCount(params);
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认并导出的数据，请检查！", null).toString();
			}
			int unreadyCnt= prodTransRegistInfoDao.findProdTransRegistInfoFailStatus(params);
			if (unreadyCnt > 0) {
				return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后导出！", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "查询失败，请检查", null).toString();
		}
	}

	@API(desc = "确认并导出",  auth = APIAuth.YES)
	public String updateProdTransRegistInfoStatus(SqlParam<ProdTransRegistInfo> params) throws Exception {
		try {
			daoService.doTrans(() -> {
				prodTransRegistInfoDao.updateProdTransRegistInfoStatus(params);
				prodTransRegistInfoDao.updateBaseReportResultInfo(params);
			});
			return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "操作失败，请检查!", null).toString();
		}
	}
	//导入
	public void importAddProdTransRegistInfo(List<ProdTransRegistInfo> prodTransRegistInfos, Map<String, Object> params) throws Exception {
		// 添加至操作记录
		for (ProdTransRegistInfo prodTransRegistInfo : prodTransRegistInfos) {
			Map<String, Object> map = BeanUtil.beanToMap(prodTransRegistInfo);
			SqlParam<ProdTransRegistInfo> param =  new FetcherData<>(map,ProdTransRegistInfo.class);
//			prodTransRegistService.addProdTransRegist(param, OperatorEnum.IMPORT.getVal());
			prodTransRegistInfoDao.addImportProdTransRegistInfo(map);
		}
	}
}
