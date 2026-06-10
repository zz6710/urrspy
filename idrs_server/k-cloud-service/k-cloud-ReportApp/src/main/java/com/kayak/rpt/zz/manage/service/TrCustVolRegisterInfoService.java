package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.zz.manage.dao.TrCustVolRegisterInfoDao;
import com.kayak.rpt.zz.manage.enums.ExcelEnum;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.BaseReportExportLog;
import com.kayak.rpt.zz.manage.model.ExcelToMapInfo;
import com.kayak.rpt.zz.manage.model.TrCustTransInfo;
import com.kayak.rpt.zz.manage.model.TrCustVolRegisterInfo;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import com.kayak.rpt.zz.operate.service.CustVolRegisterService;
import com.kayak.utils.InvExcelWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "投资者持有信息服务", model = TrCustVolRegisterInfo.class)
@Slf4j
public class TrCustVolRegisterInfoService {

    @Autowired
    private TrCustVolRegisterInfoDao trCustVolRegisterInfoDao;

    @Autowired
    private ExcelToMapService excelToMapService;

    @Autowired
    private CustVolRegisterService custVolRegisterService;

	@Autowired
	InvExcelWriter invExcelWriter;

    @Autowired
    CheckDataForVueService checkDataForVueService;

    CheckDataParams checkDataParams = new CheckDataParams();
    @Autowired
    BaseReportExportLogService baseReportExportLogService;

    @API(desc = "查询投资者持有信息", auth = APIAuth.YES)
    public SqlResult<TrCustVolRegisterInfo> findTrCustVolRegisterInfos(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
//		params.setMakeSql(true);
        try {
            return trCustVolRegisterInfoDao.findTrCustVolRegisterInfos(params);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("app_cust_vol_register_info_")) {
                SqlResult<TrCustVolRegisterInfo> sqlResult = new SqlResult<>();
                List<TrCustVolRegisterInfo> list = new ArrayList<>();
                sqlResult.setRows(list);
                return sqlResult;
            }
            throw e;
        }

    }

    public SqlResult<TrCustVolRegisterInfo> findTrCustVolRegisterInfosID(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        return trCustVolRegisterInfoDao.findTrCustVolRegisterInfosID(params);
    }

    @API(desc = "查询投资者持有数据信息", auth = APIAuth.YES)
    public SqlResult<TrCustVolRegisterInfo> findTrCustVolRegisterInfosAndIsError(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        params.setMakeSql(true);
        return trCustVolRegisterInfoDao.findTrCustVolRegisterInfosAndIsError(params);
    }

    @API(desc = "校验失败详情", auth = APIAuth.YES)
    public SqlResult<TrCustVolRegisterInfo> findValidateInfos(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        return trCustVolRegisterInfoDao.findValidateInfos(params);
    }

    @API(desc = "添加投资者持有信息", params = "bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,id", auth = APIAuth.YES)
    public String addTrCustVolRegisterInfo(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        try {
            checkDataParams.initDataNoDict();
            String whiteregex = CheckDataParams.whiteregex;
            String whitereForCode = CheckDataParams.whitereForCode;
            String checkErr = checkDataForVueService.trCustVolRegisterInfoCheckForVue(whiteregex, whitereForCode, params.getModel());
            if (StringUtils.isNotBlank(checkErr)) {
                return RequestSupport.updateReturnJson(false, "添加失败！错误信息为：\n" + checkErr, null).toString();
            }
            // 新增不插入操作记录
//			custVolRegisterService.addCustVolRegister(params, OperatorEnum.CREATE.getVal());
            trCustVolRegisterInfoDao.addTrCustVolRegisterInfo(params);
            return RequestSupport.updateReturnJson(true, "添加成功！", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "添加失败！", null).toString();
        }
    }

    @API(desc = "修改投资者持有信息", params = "bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,id", operation = APIOperation.UPDATE, auth = APIAuth.YES)
    public String updateTrCustVolRegisterInfo(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        try {
            checkDataParams.initDataNoDict();
            String whiteregex = CheckDataParams.whiteregex;
            String whitereForCode = CheckDataParams.whitereForCode;
            String checkErr = checkDataForVueService.trCustVolRegisterInfoCheckForVue(whiteregex, whitereForCode, params.getModel());
            if (StringUtils.isNotBlank(checkErr)) {
                return RequestSupport.updateReturnJson(false, "修改失败！错误信息为：\n" + checkErr, null).toString();
            }
            // 操作记录
            Map paramMap = new HashMap<>();
            paramMap.put("registerSerno", params.getModel().getRegisterSerno());
            paramMap.put("reportDate", params.getModel().getReportDate());
            SqlParam<TrCustVolRegisterInfo> oldParams = new FetcherData<>(paramMap, TrCustVolRegisterInfo.class);
            SqlResult<TrCustVolRegisterInfo> originParams = trCustVolRegisterInfoDao.findTrCustVolRegisterInfos(oldParams);
            if (originParams.getRows().size() > 0) {
                TrCustVolRegisterInfo param = originParams.getRows().get(0);
                paramMap = BeanUtil.beanToMap(param);
                oldParams = new FetcherData<>(paramMap, TrCustVolRegisterInfo.class);
            }
            custVolRegisterService.addCustVolRegister(oldParams, OperatorEnum.UPDATE.getVal());
            trCustVolRegisterInfoDao.updateTrCustVolRegisterInfo(params);
            return RequestSupport.updateReturnJson(true, "修改成功！", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "修改失败，数据库错误信息为：" + e.getMessage(), null).toString();
        }

    }

    @API(desc = "删除投资者持有信息", params = "bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,id", auth = APIAuth.YES)
    public int deleteTrCustVolRegisterInfo(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        // 操作记录
        custVolRegisterService.addCustVolRegister(params, OperatorEnum.DELETE.getVal());
        return trCustVolRegisterInfoDao.deleteTrCustVolRegisterInfo(params).getEffect();
    }

    /**
     * 投资者持有导入不可选时间，默认系统当前工作日
     *
     * @return
     * @throws Exception
     */
    @API(desc = "查询系统日期")
    public String findSysDate() throws Exception {
        String sysDate = DateUtil.getSysWordDay();//系统工作日
        return sysDate;
    }


    @API(desc = "批量导入数据", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String batchImport(String fileName, MultipartFile file) throws Exception {

        // 返回提示
        String resResult = "";
        Boolean lastResult = false;

        // 数据流转表格
        Workbook wb = new XSSFWorkbook(file.getInputStream());

        // 获取第一页签
        Sheet sheet = wb.getSheetAt(0);


        List<ExcelToMapInfo> list = new ArrayList<ExcelToMapInfo>();


        ExcelToMapInfo bankCode = new ExcelToMapInfo();

        bankCode.setFieldIndex(0); // EXCEL列位置
        bankCode.setFieldName("登记银行代码");
        bankCode.setField("bankCode"); //字段
        bankCode.setFieldType(ExcelEnum.TEXT); //数据类型
        bankCode.setLength(0); //长度校验 0 不校验
        bankCode.setNotNULL(true); // 是否非空
        list.add(bankCode);

        ExcelToMapInfo prodCode = new ExcelToMapInfo();
        prodCode.setDict("tr_data_type");  //字典值
        prodCode.setFieldIndex(1);// EXCEL列位置
        prodCode.setFieldName("产品登记编码");
        prodCode.setField("prodCode"); //字段
        prodCode.setFieldType(ExcelEnum.TEXT);//数据类型
        prodCode.setLength(0); //长度校验 0 不校验
        prodCode.setNotNULL(true);// 是否非空
        list.add(prodCode);

        ExcelToMapInfo custNo = new ExcelToMapInfo();
        custNo.setFieldIndex(2); // EXCEL列位置
        custNo.setFieldName("识别标识");
        custNo.setField("custNo"); //字段
        custNo.setFieldType(ExcelEnum.TEXT); //数据类型
        custNo.setLength(0); //长度校验 0 不校验
        custNo.setNotNULL(false); // 是否非空
        list.add(custNo);

        ExcelToMapInfo holdDate = new ExcelToMapInfo();
        holdDate.setFieldIndex(3); // EXCEL列位置
        holdDate.setFieldName("持有日期");
        holdDate.setField("holdDate"); //字段
        holdDate.setFieldType(ExcelEnum.DATE); //数据类型
        holdDate.setLength(0); //长度校验 0 不校验
        holdDate.setNotNULL(true); // 是否非空
        list.add(holdDate);

        ExcelToMapInfo cur = new ExcelToMapInfo();
        cur.setDict("tr_cur");  //字典值
        cur.setFieldIndex(4); // EXCEL列位置
        cur.setFieldName("币种");
        cur.setField("cur"); //字段
        cur.setFieldType(ExcelEnum.ENUM); //数据类型
        cur.setLength(0); //长度校验 0 不校验
        cur.setNotNULL(true); // 是否非空
        list.add(cur);


        ExcelToMapInfo holdVol = new ExcelToMapInfo();
        holdVol.setFieldIndex(5); // EXCEL列位置
        holdVol.setFieldName("持有份额");
        holdVol.setField("holdVol"); //字段
        holdVol.setFieldType(ExcelEnum.TEXT); //数据类型
        holdVol.setLength(0); //长度校验 0 不校验
        holdVol.setNotNULL(true); // 是否非空
        list.add(holdVol);

        ExcelToMapInfo holdAmt = new ExcelToMapInfo();
        holdAmt.setFieldIndex(6); // EXCEL列位置
        holdAmt.setFieldName("持有金额");
        holdAmt.setField("holdAmt"); //字段
        holdAmt.setFieldType(ExcelEnum.TEXT); //数据类型
        holdAmt.setLength(0); //长度校验 0 不校验
        holdAmt.setNotNULL(true); // 是否非空
        list.add(holdAmt);

        ExcelToMapInfo convertRmb = new ExcelToMapInfo();
        convertRmb.setFieldIndex(7); // EXCEL列位置
        convertRmb.setFieldName("折算人民币金额");
        convertRmb.setField("convertRmb"); //字段
        convertRmb.setFieldType(ExcelEnum.TEXT); //数据类型
        convertRmb.setLength(0); //长度校验 0 不校验
        convertRmb.setNotNULL(true); // 是否非空
        list.add(convertRmb);

        ExcelToMapInfo registerDate = new ExcelToMapInfo();
        registerDate.setFieldIndex(8); // EXCEL列位置
        registerDate.setFieldName("业务登记日期");
        registerDate.setField("registerDate"); //字段
        registerDate.setFieldType(ExcelEnum.DATE); //数据类型
        registerDate.setLength(0); //长度校验 0 不校验
        registerDate.setNotNULL(true); // 是否非空
        list.add(registerDate);


        Map<String, Object> map = excelToMapService.toMapAndCheck(list, sheet);

        boolean isError = (boolean) map.get("isError");

        if (isError) {
            return RequestSupport.updateReturnJson(false, map.get("msg").toString(), null).toString();
        }
        List<Map<String, Object>> resList = (List<Map<String, Object>>) map.get("list");

        trCustVolRegisterInfoDao.addTrCustVolRegisterInfofoBatch(resList);

        return RequestSupport.updateReturnJson(true, map.get("msg").toString(), null).toString();

    }

    /**
     * 导入投资者持有信息
     *
     * @param custVolRegisterInfos
     * @param params
     * @throws Exception
     */
    public void importCustVolRegisterInfo(List<TrCustVolRegisterInfo> custVolRegisterInfos, Map<String, Object> params) throws Exception {
        // 添加至操作记录
        //trCustVolRegisterInfoDao.deleteImportCustVolRegisterInfo(params); //不执行删除操作 程晓鹏 2024.11.27 modify
        for (TrCustVolRegisterInfo custVolRegisterInfo : custVolRegisterInfos) {
            Map<String, Object> map = BeanUtil.beanToMap(custVolRegisterInfo);
            //导入操作不添加操作记录
//			custVolRegisterService.addImportCustVolRegister(custVolRegisterInfo,OperatorEnum.IMPORT.getVal());
            trCustVolRegisterInfoDao.addImportCustVolRegisterInfo(map);//新增导入记录
        }
    }

    /**
     * 根据识别标识，产品登记代码进行数据更新
     *
     * @param custVolRegisterInfos
     * @param params
     * @throws Exception
     */
    public void updateImportCustVolRegisterInfo(List<TrCustVolRegisterInfo> custVolRegisterInfos, Map<String, Object> params) throws Exception {
        // 添加至操作记录
        //trCustVolRegisterInfoDao.deleteImportCustVolRegisterInfo(params); //不执行删除操作 程晓鹏 2024.11.27 modify
        for (TrCustVolRegisterInfo custVolRegisterInfo : custVolRegisterInfos) {
            custVolRegisterInfo.setReportDate((String) params.get("reportDate"));
            Map<String, Object> map = BeanUtil.beanToMap(custVolRegisterInfo);
            //导入操作不添加操作记录
//			custVolRegisterService.addImportCustVolRegister(custVolRegisterInfo,OperatorEnum.IMPORT.getVal());
            //trCustVolRegisterInfoDao.addImportCustVolRegisterInfo(map);
            trCustVolRegisterInfoDao.updateTrCustVolRegisterInfoByCustNo(map); //根据识别标识，产品登记代码进行数据更新 程晓鹏 2024.11.27 modify
        }
    }

    //手动更新确认未报送成功的数据为报送成功
    @API(desc = "确认报送状态为成功", auth = APIAuth.YES)
    public String updateCustVolRegisterInfoRegistStatusSuccess(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        try {
            //查询 0 初始化 或 1 校验失败的数据。存在需提前处理
            int  recordCnt = trCustVolRegisterInfoDao.findTrCustVolRegisterInfosCount(params);
            if (recordCnt == 0) {
                return RequestSupport.updateReturnJson(false,  "没有需要确认报送状态的数据，请检查！", null).toString();
            }
            int unreadyCnt= trCustVolRegisterInfoDao.findTrCustVolRegisterInfosFailStatus(params);
            if (unreadyCnt > 0) {
                return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后确认报送状态！", null).toString();
            }
//			Map<String, Object> map = BeanUtil.beanToMap(TrCustVolRegisterInfo);
//            if(params==null || params.getModel().getReportDate()==null ){
//                return RequestSupport.updateReturnJson(false, "请先选择日期！", null).toString();
//            }
            trCustVolRegisterInfoDao.updateTrCustVolRegisterInfoRegistStatusSuccess(params);
            return RequestSupport.updateReturnJson(true, "成功！", null).toString();
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("app_cust_vol_register_info_")) {
                return RequestSupport.updateReturnJson(false, "未找到所选日期对应的数据表，请先确认！", null).toString();
            }else{
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "失败！", null).toString();
            }
        }
    }

    public void importModifyCustVolRegisterInfo(List<TrCustVolRegisterInfo> custVolRegisterInfos) throws Exception {
        // 删除当天的导入变更记录
        Map<String, Object> params = new HashMap<>();
        params.put("createDate", DateUtil.getNowDate());
        trCustVolRegisterInfoDao.deleteImportModifyCustVolRegisterInfo(params);
        for (TrCustVolRegisterInfo custVolRegisterInfo : custVolRegisterInfos) {
            Map<String, Object> map = BeanUtil.beanToMap(custVolRegisterInfo);
            //导入不加操作记录
//			custVolRegisterService.addImportCustVolRegister(custVolRegisterInfo,OperatorEnum.MODIFY.getVal());
            trCustVolRegisterInfoDao.addImportModifyCustVolRegisterInfo(map);
        }
    }

	@API(desc = "投资者持有信息下载", operation = APIOperation.UPDATE,auth = APIAuth.YES)
	public String download(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
		try {
			Map<String, Object> param = params.getParamsDirect();
			String processInstanceId = (String) param.get("processInstanceId");
			param.put("modelClassName", TrCustVolRegisterInfo.class.getName());
			param.put("action", "findTrCustVolRegisterInfos");
			String actionParamsStr = Tools.obj2Str(param.get("action_params"));
			String reportDate = JSONObject.parseObject(actionParamsStr).getString("reportDate");
			if (StringUtils.isNotEmpty(reportDate)) {
				Runnable runnable = () -> {
					// 如果是多天，需要分别生成文件并更新更新文件路径
					String[] reportDates = reportDate.split(",");
					for (String date : reportDates) {
						try {
							JSONObject jsonObject = JSONObject.parseObject(actionParamsStr);
							Map<String, String> repMap = new HashMap<>();
							repMap.put("reportDate", date);
							if (StringUtils.isNotBlank(jsonObject.getString("custNo"))) {
								repMap.put("custNo", jsonObject.getString("custNo"));
							}
							if (StringUtils.isNotBlank(jsonObject.getString("prodCode"))) {
								repMap.put("prodCode", jsonObject.getString("prodCode"));
							}
							if (StringUtils.isNotBlank(jsonObject.getString("cur"))) {
								repMap.put("cur", jsonObject.getString("cur"));
							}
							if (StringUtils.isNotBlank(jsonObject.getString("registerStatus"))) {
								repMap.put("registerStatus", jsonObject.getString("registerStatus"));
							}
							param.put("action_params", JSONObject.toJSON(repMap));
							Map<String, String> resMap = invExcelWriter.downloadEx(param);

							BaseReportExportLog exportLog = new BaseReportExportLog();
							exportLog.setFilePath(resMap.get("local"));
							exportLog.setRemotePath(resMap.get("remote"));
							exportLog.setDataTime(date);
							exportLog.setProcessInstanceId(processInstanceId);
							exportLog.setFileStatus("2");
							baseReportExportLogService.updateDataTimePath(exportLog);

							log.info("投资者持有信息下载{}文件生成成功，更新文件路径成功！", date);
						} catch (Exception e) {
							log.error("投资者持有信息下载{}文件生成失败，更新文件路径失败！", date, e);
						}
					}
				};
				Thread thread = new Thread(runnable);
				thread.start();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return RequestSupport.updateReturnJson(true, "后台处理中，请在【中债直连】【导出文件下载】菜单查看", null).toString();
	}
}
