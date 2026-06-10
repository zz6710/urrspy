package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;

import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.zz.manage.dao.TrTerminationRegistInfoDao;
import com.kayak.rpt.zz.manage.enums.ExcelEnum;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.*;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import com.kayak.rpt.zz.operate.service.TerminationRegistService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "终止登记要素服务", model = TrTerminationRegistInfo.class)
public class TrTerminationRegistInfoService {

	@Autowired
	private TrTerminationRegistInfoDao trTerminationRegistInfoDao;

	@Autowired
	private TerminationRegistService terminationRegistService;
	@Autowired
	private ExcelToMapService excelToMapService;
	@Autowired
	protected DaoService daoService;
	@Autowired
	CheckDataForVueService checkDataForVueService;

	CheckDataParams checkDataParams = new CheckDataParams();

	@API(desc = "查询终止登记要素信息", auth = APIAuth.YES)
	public SqlResult<TrTerminationRegistInfo> findTrTerminationRegistInfos(SqlParam<TrTerminationRegistInfo> params) throws Exception {
//		params.setMakeSql(true);
		return trTerminationRegistInfoDao.findTrTerminationRegistInfos(params);
	}

	@API(desc = "添加终止登记要素", auth = APIAuth.YES)
	public int addTrTerminationRegistInfo(SqlParam<TrTerminationRegistInfo> params) throws Exception {
		// 操作记录
//		terminationRegistService.addTerminationRegist(params, OperatorEnum.CREATE.getVal());
		return trTerminationRegistInfoDao.addTrTerminationRegistInfo(params).getEffect();
	}

	@API(desc = "修改终止登记要素", auth = APIAuth.YES)
	public String updateTrTerminationRegistInfo(SqlParam<TrTerminationRegistInfo> params) throws Exception {
		try {
			checkDataParams.initDataNoDict();
			String whiteregex = CheckDataParams.whiteregex;
			String whitereForCode = CheckDataParams.whitereForCode;
			String checkErr = checkDataForVueService.terminationRegistInfoCheckForVue(whiteregex,whitereForCode,params.getModel());
			if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
				return RequestSupport.updateReturnJson(false,  "修改失败！错误信息为：\n"+checkErr, null).toString();
			}
			// 操作记录
			// 操作记录
			Map paramMap = new HashMap<>();
			paramMap.put("registerSerno",params.getModel().getRegisterSerno());
			SqlParam<TrTerminationRegistInfo> oldParams =  new FetcherData<>(paramMap,ProdTransRegistInfo.class);
			SqlResult<TrTerminationRegistInfo> originParams = trTerminationRegistInfoDao.findTrTerminationRegistInfos(params);
			if(originParams.getRows().size()>0){
				TrTerminationRegistInfo param = 	originParams.getRows().get(0);
				paramMap = BeanUtil.beanToMap(param);
				oldParams =  new FetcherData<>(paramMap,TrTerminationRegistInfo.class);
			}
			terminationRegistService.addTerminationRegist(oldParams, OperatorEnum.UPDATE.getVal());
			trTerminationRegistInfoDao.updateTrTerminationRegistInfo(params);
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败，数据库错误信息为："+e.getMessage(), null).toString();
		}
	}

	@API(desc = "查询报送状态为0,1的数据", auth = APIAuth.NO)
	public String getAbnormalData(SqlParam<TrTerminationRegistInfo> params) throws Exception {
		try {
			int  recordCnt = trTerminationRegistInfoDao.findTrTerminationRegistInfosCount(params);
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认并导出的数据，请检查！", null).toString();
			}
			int unreadyCnt= trTerminationRegistInfoDao.findTrTerminationRegistInfoFailStatus(params);
			if (unreadyCnt > 0) {
				return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后导出！", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "查询失败，请检查", null).toString();
		}
	}

	@API(desc = "确认并导出终止", auth = APIAuth.YES)
	public String updateTrTerminationRegistInfoStatus(SqlParam<TrTerminationRegistInfo> params) throws Exception {
		try {
			String prod_code = trTerminationRegistInfoDao.getProdCode(params);
			if (!StringUtils.isEmpty(prod_code)) {
				daoService.doTrans(() -> {
					trTerminationRegistInfoDao.updateProdStat(params, prod_code);
					trTerminationRegistInfoDao.updateTrTerminationRegistInfoStatus(params, prod_code);
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

	@API(desc = "删除终止登记要素",  auth = APIAuth.YES)
	public int deleteTrTerminationRegistInfo(SqlParam<TrTerminationRegistInfo> params) throws Exception {
		// 操作记录
		terminationRegistService.addTerminationRegist(params, OperatorEnum.DELETE.getVal());
		return trTerminationRegistInfoDao.deleteTrTerminationRegistInfo(params).getEffect();
	}







	@API(desc = "批量导入数据", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String batchImport(String fileName, MultipartFile file) throws Exception {

		// 返回提示
		String resResult = "";
		Boolean lastResult = false;

		// 数据流转表格
		Workbook wb =  new XSSFWorkbook(file.getInputStream());

		// 获取第一页签
		Sheet sheet = wb.getSheetAt(0);


		List<ExcelToMapInfo> list =  new ArrayList<ExcelToMapInfo>();



		ExcelToMapInfo prodCode =  new ExcelToMapInfo();

		prodCode.setFieldIndex(0); // EXCEL列位置
		prodCode.setFieldName("产品登记编码");
		prodCode.setField("prodCode"); //字段
		prodCode.setFieldType(ExcelEnum.TEXT); //数据类型
		prodCode.setLength(0); //长度校验 0 不校验
		prodCode.setNotNULL(true); // 是否非空
		list.add(prodCode);

		ExcelToMapInfo bankCode =  new ExcelToMapInfo();
		bankCode.setFieldIndex(1);// EXCEL列位置
		bankCode.setFieldName("发行机构代码");
		bankCode.setField("bankCode"); //字段
		bankCode.setFieldType(ExcelEnum.TEXT);//数据类型
		bankCode.setLength(0); //长度校验 0 不校验
		bankCode.setNotNULL(true);// 是否非空
		list.add(bankCode);

		ExcelToMapInfo actualProdTerDate =  new ExcelToMapInfo();
		actualProdTerDate.setFieldIndex(2); // EXCEL列位置
		actualProdTerDate.setFieldName("理财产品实际终止日期");
		actualProdTerDate.setField("actualProdTerDate"); //字段
		actualProdTerDate.setFieldType(ExcelEnum.DATE); //数据类型
		actualProdTerDate.setLength(0); //长度校验 0 不校验
		actualProdTerDate.setNotNULL(true); // 是否非空
		list.add(actualProdTerDate);


		ExcelToMapInfo realizedBankIncome =  new ExcelToMapInfo();
		realizedBankIncome.setFieldIndex(3); // EXCEL列位置
		realizedBankIncome.setFieldName("银行实际实现收入");
		realizedBankIncome.setField("realizedBankIncome"); //字段
		realizedBankIncome.setFieldType(ExcelEnum.TEXT); //数据类型
		realizedBankIncome.setLength(0); //长度校验 0 不校验
		realizedBankIncome.setNotNULL(true); // 是否非空
		list.add(realizedBankIncome);

		ExcelToMapInfo interestPayment =  new ExcelToMapInfo();
		interestPayment.setFieldIndex(4); // EXCEL列位置
		interestPayment.setFieldName("兑付客户收益");
		interestPayment.setField("interestPayment"); //字段
		interestPayment.setFieldType(ExcelEnum.TEXT); //数据类型
		interestPayment.setLength(0); //长度校验 0 不校验
		interestPayment.setNotNULL(true); // 是否非空
		list.add(interestPayment);


		ExcelToMapInfo payment =  new ExcelToMapInfo();
		payment.setFieldIndex(5); // EXCEL列位置
		payment.setFieldName("兑付客户总金额");
		payment.setField("payment"); //字段
		payment.setFieldType(ExcelEnum.TEXT); //数据类型
		payment.setLength(0); //长度校验 0 不校验
		payment.setNotNULL(true); // 是否非空
		list.add(payment);

		ExcelToMapInfo deliveredVol =  new ExcelToMapInfo();
		deliveredVol.setFieldIndex(6); // EXCEL列位置
		deliveredVol.setFieldName("兑付总份额");
		deliveredVol.setField("deliveredVol"); //字段
		deliveredVol.setFieldType(ExcelEnum.TEXT); //数据类型
		deliveredVol.setLength(0); //长度校验 0 不校验
		deliveredVol.setNotNULL(true); // 是否非空
		list.add(deliveredVol);

		ExcelToMapInfo inCustodianFee =  new ExcelToMapInfo();
		inCustodianFee.setFieldIndex(7); // EXCEL列位置
		inCustodianFee.setFieldName("本机构托管费");
		inCustodianFee.setField("inCustodianFee"); //字段
		inCustodianFee.setFieldType(ExcelEnum.TEXT); //数据类型
		inCustodianFee.setLength(0); //长度校验 0 不校验
		inCustodianFee.setNotNULL(true); // 是否非空
		list.add(inCustodianFee);

		ExcelToMapInfo inManageFee =  new ExcelToMapInfo();
		inManageFee.setDict("tr_buy_place");  //字典值
		inManageFee.setFieldIndex(8); // EXCEL列位置
		inManageFee.setFieldName("本机构管理费");
		inManageFee.setField("inManageFee"); //字段
		inManageFee.setFieldType(ExcelEnum.TEXT); //数据类型
		inManageFee.setLength(0); //长度校验 0 不校验
		inManageFee.setNotNULL(true); // 是否非空
		list.add(inManageFee);


		ExcelToMapInfo inSalesCommision =  new ExcelToMapInfo();
		inSalesCommision.setDict("tr_is_belong");  //字典值
		inSalesCommision.setFieldIndex(9); // EXCEL列位置
		inSalesCommision.setFieldName("本机构销售手续费");
		inSalesCommision.setField("inSalesCommision"); //字段
		inSalesCommision.setFieldType(ExcelEnum.TEXT); //数据类型
		inSalesCommision.setLength(0); //长度校验 0 不校验
		inSalesCommision.setNotNULL(true); // 是否非空
		list.add(inSalesCommision);


		ExcelToMapInfo inOtherProdFee =  new ExcelToMapInfo();
		inOtherProdFee.setDict("tr_is_belong");  //字典值
		inOtherProdFee.setFieldIndex(10); // EXCEL列位置
		inOtherProdFee.setFieldName("本机构其他产品配用");
		inOtherProdFee.setField("inOtherProdFee"); //字段
		inOtherProdFee.setFieldType(ExcelEnum.TEXT); //数据类型
		inOtherProdFee.setLength(0); //长度校验 0 不校验
		inOtherProdFee.setNotNULL(true); // 是否非空
		list.add(inOtherProdFee);


		ExcelToMapInfo otherCustodianFee =  new ExcelToMapInfo();
		otherCustodianFee.setDict("tr_is_belong");  //字典值
		otherCustodianFee.setFieldIndex(11); // EXCEL列位置
		otherCustodianFee.setFieldName("其他机构托管费");
		otherCustodianFee.setField("otherCustodianFee"); //字段
		otherCustodianFee.setFieldType(ExcelEnum.TEXT); //数据类型
		otherCustodianFee.setLength(0); //长度校验 0 不校验
		otherCustodianFee.setNotNULL(true); // 是否非空
		list.add(otherCustodianFee);



		ExcelToMapInfo otherManageFee =  new ExcelToMapInfo();
		otherManageFee.setDict("tr_agent_regu_code");  //字典值
		otherManageFee.setFieldIndex(12); // EXCEL列位置
		otherManageFee.setFieldName("其他机构管理费");
		otherManageFee.setField("otherManageFee"); //字段
		otherManageFee.setFieldType(ExcelEnum.TEXT); //数据类型
		otherManageFee.setLength(0); //长度校验 0 不校验
		otherManageFee.setNotNULL(true); // 是否非空
		list.add(otherManageFee);

		ExcelToMapInfo otherSalesComm =  new ExcelToMapInfo();
		otherSalesComm.setDict("tr_agent_regu_code");  //字典值
		otherSalesComm.setFieldIndex(13); // EXCEL列位置
		otherSalesComm.setFieldName("其他机构销售手续费");
		otherSalesComm.setField("otherSalesComm"); //字段
		otherSalesComm.setFieldType(ExcelEnum.TEXT); //数据类型
		otherSalesComm.setLength(0); //长度校验 0 不校验
		otherSalesComm.setNotNULL(true); // 是否非空
		list.add(otherSalesComm);


		ExcelToMapInfo consultFee =  new ExcelToMapInfo();
		consultFee.setDict("tr_busi_code");  //字典值
		consultFee.setFieldIndex(14); // EXCEL列位置
		consultFee.setFieldName("投资顾问费用");
		consultFee.setField("consultFee"); //字段
		consultFee.setFieldType(ExcelEnum.TEXT); //数据类型
		consultFee.setLength(0); //长度校验 0 不校验
		consultFee.setNotNULL(true); // 是否非空
		list.add(consultFee);


		ExcelToMapInfo otherProdFee =  new ExcelToMapInfo();
		otherProdFee.setDict("tr_agent_regu_code");  //字典值
		otherProdFee.setFieldIndex(15); // EXCEL列位置
		otherProdFee.setFieldName("其他机构其他产品费用");
		otherProdFee.setField("otherProdFee"); //字段
		otherProdFee.setFieldType(ExcelEnum.TEXT); //数据类型
		otherProdFee.setLength(0); //长度校验 0 不校验
		otherProdFee.setNotNULL(true); // 是否非空
		list.add(otherProdFee);



		ExcelToMapInfo annualReturnClient =  new ExcelToMapInfo();
		annualReturnClient.setDict("tr_agent_regu_code");  //字典值
		annualReturnClient.setFieldIndex(16); // EXCEL列位置
		annualReturnClient.setFieldName("客户实际年化收益率%");
		annualReturnClient.setField("annualReturnClient"); //字段
		annualReturnClient.setFieldType(ExcelEnum.TEXT); //数据类型
		annualReturnClient.setLength(0); //长度校验 0 不校验
		annualReturnClient.setNotNULL(true); // 是否非空
		list.add(annualReturnClient);


		ExcelToMapInfo annualReturnProd =  new ExcelToMapInfo();
		annualReturnProd.setDict("tr_agent_regu_code");  //字典值
		annualReturnProd.setFieldIndex(17); // EXCEL列位置
		annualReturnProd.setFieldName("产品实际年化收益率%");
		annualReturnProd.setField("annualReturnProd"); //字段
		annualReturnProd.setFieldType(ExcelEnum.TEXT); //数据类型
		annualReturnProd.setLength(0); //长度校验 0 不校验
		annualReturnProd.setNotNULL(true); // 是否非空
		list.add(annualReturnProd);



		ExcelToMapInfo registerDate =  new ExcelToMapInfo();
		registerDate.setDict("tr_cur");  //字典值
		registerDate.setFieldIndex(18); // EXCEL列位置
		registerDate.setFieldName("投资者登记日期");
		registerDate.setField("registerDate"); //字段
		registerDate.setFieldType(ExcelEnum.DATE); //数据类型
		registerDate.setLength(0); //长度校验 0 不校验
		registerDate.setNotNULL(true); // 是否非空
		list.add(registerDate);





		Map<String,Object> map  = excelToMapService.toMapAndCheck(list,sheet);

		boolean isError = (boolean) map.get("isError");

		if(isError){
			return RequestSupport.updateReturnJson(false, map.get("msg").toString(), null).toString();
		}
		List<Map<String,Object>> resList = (List<Map<String, Object>>) map.get("list");

		trTerminationRegistInfoDao.addTrTerminationRegistInfoForBatch(resList);

		return RequestSupport.updateReturnJson(true, map.get("msg").toString(), null).toString();

	}

	public void importTrTerminationRegistInfo(List<TrTerminationRegistInfo> terminationRegistInfos,Map<String, Object> params) throws Exception {
		// 添加至操作记录
		trTerminationRegistInfoDao.deleteImportTrTerminationRegistInfo(params);
		for (TrTerminationRegistInfo terminationRegistInfo : terminationRegistInfos) {
			Map<String, Object> map = BeanUtil.beanToMap(terminationRegistInfo);
//			terminationRegistService.addImportTrTerminationRegist(terminationRegistInfo,OperatorEnum.IMPORT.getVal());
			trTerminationRegistInfoDao.addImportTrTerminationRegistInfo(map);
		}
	}
}
