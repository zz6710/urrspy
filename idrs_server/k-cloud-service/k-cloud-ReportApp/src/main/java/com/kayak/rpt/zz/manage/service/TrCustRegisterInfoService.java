package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.zz.manage.dao.TrCustRegisterInfoCompareDao;
import com.kayak.rpt.zz.manage.dao.TrCustRegisterInfoDao;
import com.kayak.rpt.zz.manage.enums.ExcelEnum;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.*;
import com.kayak.rpt.zz.operate.service.CustRegistMarkService;
import com.kayak.server.ServerUtil;
import com.kayak.utils.ExcelWriter;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
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
@APIDefine(desc = "投资者身份信息登记服务", model = TrCustRegisterInfo.class)
@Slf4j
public class TrCustRegisterInfoService {

    @Autowired
    private TrCustRegisterInfoDao trCustRegisterInfoDao;
    @Autowired
    private TrCustRegisterInfoCompareDao trCustRegisterInfoCompareDao;
    @Autowired
    private ExcelToMapService excelToMapService;
    @Autowired
    private CustRegistMarkService custRegistMarkService;
    @Autowired
    BaseReportExportLogService baseReportExportLogService;
    @Autowired
    ExcelWriter excelWriter;

    @API(desc = "查询投资者身份登记信息", auth = APIAuth.YES)
    public SqlResult<TrCustRegisterInfo> findTrCustRegisterInfos1(SqlParam<TrCustRegisterInfo> params) throws Exception {
//		params.setMakeSql(true);
        return trCustRegisterInfoDao.findTrCustRegisterInfos(params);
    }

    @API(desc = "查询投资者身份登记信息及字段变更标识", auth = APIAuth.YES)
    public SqlResult<TrCustRegisterInfo> findTrCustRegisterInfos(SqlParam<TrCustRegisterInfo> params) throws Exception {
        SqlResult<TrCustRegisterInfo> r1 = trCustRegisterInfoDao.findTrCustRegisterInfos(params);
        if (org.apache.commons.lang3.StringUtils.equals(params.getModel().getQueryStartDate(), params.getModel().getQueryEndDate())) {
            List<TrCustRegisterInfo> returnList = new ArrayList<>();
            if (r1 != null && r1.getRows() != null && r1.getRows().size() > 0) {
                List<TrCustRegisterInfo> list0 = r1.getRows();//原始数据
                StringBuffer custNos = new StringBuffer();
                for (int i = 0; i < list0.size(); i++) {
                    TrCustRegisterInfo TrCustRegisterInfo = list0.get(i);
                    if (i == list0.size() - 1) {
                        custNos.append("'" + TrCustRegisterInfo.getCustNo() + "'");
                    } else {
                        custNos.append("'" + TrCustRegisterInfo.getCustNo() + "',");
                    }
                }

                params.getModel().setReportDate(params.getModel().getQueryStartDate());
                List<TrCustRegisterInfo> list1 = trCustRegisterInfoDao.findAllCustByCustNo(String.valueOf(custNos), params).getRows();//指定标识的全量身份身份信息数据集

                for (int i = 0; i < list0.size(); i++) {
                    TrCustRegisterInfo TrCustRegisterInfo = list0.get(i);
                    TrCustRegisterInfo prodIssuance2 = trCustRegisterInfoCompareDao.compareFlag(TrCustRegisterInfo, list1);
                    returnList.add(prodIssuance2);
                }
            }
            r1.setRowsList1(returnList);
        }
        return r1;
    }


	@API(desc = "查询身份信息登记信息", auth = APIAuth.NO)
	public SqlResult<TrCustRegisterInfo> findTrCustRegisterInfosAndIsError(SqlParam<TrCustRegisterInfo> params) throws Exception {
		params.setMakeSql(true);
		return trCustRegisterInfoDao.findTrCustRegisterInfosAndIsError(params);
	}

	@API(desc = "添加身份信息登记",  auth = APIAuth.YES)
	public String addTrCustRegisterInfo(SqlParam<TrCustRegisterInfo> params) throws Exception {
		// 操作记录
		try {
			custRegistMarkService.addCustRegistMark(params, OperatorEnum.CREATE.getVal());
			trCustRegisterInfoDao.addTrCustRegisterInfo(params).getEffect();
			return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "新增失败，请检查标识是否重复", null).toString();
		}
	}

	@API(desc = "修改身份信息登记", auth = APIAuth.YES)
	public int updateTrCustRegisterInfo(SqlParam<TrCustRegisterInfo> params) throws Exception {
		// 操作记录
		Map paramMap = new HashMap<>();
		paramMap.put("id", params.getModel().getId());
		SqlParam<TrCustRegisterInfo> oldParams = new FetcherData<>(paramMap, TrCustRegisterInfo.class);
		SqlResult<TrCustRegisterInfo> originParams = trCustRegisterInfoDao.findTrCustRegisterInfos(oldParams);
		if (originParams.getRows().size() > 0) {
			TrCustRegisterInfo param = originParams.getRows().get(0);
			paramMap = BeanUtil.beanToMap(param);
		}
		oldParams = new FetcherData<>(paramMap, TrCustRegisterInfo.class);
		custRegistMarkService.addCustRegistMark(oldParams, OperatorEnum.UPDATE.getVal());
        // 更新投资者身份
        int state = trCustRegisterInfoDao.updateTrCustRegisterInfo(params).getEffect();
        // 指标校验
        Map<String, Object> param = new HashMap();
        param.put("reportType", "01");//报表大类
        param.put("reportTable", "app_cust_register_info");//报表名称
        param.put("settleDate", params.getModel().getReportDate());//数据日期
        param.put("id", params.getModel().getId());//ID
        param.put("custNo", params.getModel().getCustNo());//custNo
        Object strResult = ServerUtil.requestPostJson("DpsApp", "/handleReportDataTask.action", param);
        JSONObject result = JSON.parseObject(strResult.toString());
        if (result != null && (Boolean) result.get("success")) {
            List<SqlRow> sqlRows = trCustRegisterInfoDao.findTrCustRegisterStatus(params);
            if (CollectionUtil.isNotEmpty(sqlRows)) {
                // 如果该指标检验成功，更新全量投资者身份、投资者持有、投资者持有（子产品）、投资者明细
                if ("2".equals(sqlRows.get(0).get("register_status"))) {
                    trCustRegisterInfoDao.updateTrCustInfo(params).getEffect();
                    // 更新完投资者持有、子产品、明细数据后，调用指标校验
                    Map<String, Object> param1 = new HashMap();
                    param1.put("reportType", "01");//报表大类
                    param1.put("settleDate", params.getModel().getReportDate());//数据日期
                    param1.put("taId", params.getModel().getTaId());//taId
                    param1.put("custNo", params.getModel().getCustNo());//custNo

                    String[] reportTables = new String[]{"app_cust_vol_register_info", "app_cust_vol_register_sub_info", "app_cust_trans_info"};
                    for (String reportTable : reportTables) {
                        param1.put("reportTable", reportTable);//报表名称
                        ServerUtil.requestPostJson("DpsApp", "/handleReportDataTask.action", param1);
                    }
                }
            }
        }
        return state;
    }

    @API(desc = "删除投资者身份登记信息", auth = APIAuth.YES)
    public String deleteTrCustRegisterInfo(SqlParam<TrCustRegisterInfo> params) throws Exception {
//        if(params!=null && params.getModel()!=null && (params.getModel().getCustNo()==null||"".equals(params.getModel().getCustNo()))){
        // 操作记录
        custRegistMarkService.addCustRegistMark(params, OperatorEnum.DELETE.getVal());
        trCustRegisterInfoDao.deleteTrCustRegisterInfo(params).getEffect();
        return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
//        }
        //根据识别标识字段，检查投资者明细和投资者持有，是否存在数据，若存在数据，弹出“该投资者已存在投资者明细信息或投资者持有信息，不允许删除！”
//        int custVolEffect = trCustRegisterInfoDao.findProdTrCustVolEffective(params);
//        if(custVolEffect>0){
//            return RequestSupport.updateReturnJson(false,  "该投资者已存在投资者持有信息，不允许删除！", null).toString();
//        }
//        int custTransEffect = trCustRegisterInfoDao.findProdTrCustTransEffective(params);
//        if(custTransEffect>0){
//            return RequestSupport.updateReturnJson(false,  "该投资者已存在投资者明细信息，不允许删除！", null).toString();
//        }
        // 操作记录
//        custRegistMarkService.addCustRegistMark(params, OperatorEnum.DELETE.getVal());
//        trCustRegisterInfoDao.deleteTrCustRegisterInfo(params).getEffect();
//        return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
    }

    public int findProdTrCustVolEffective(TrCustRegisterInfo params) throws Exception {
        int custVolEffect = trCustRegisterInfoDao.findProdTrCustVolEffective(params);
        return custVolEffect;
    }
    public int findProdTrCustTransEffective(TrCustRegisterInfo params) throws Exception {
        int custTransEffect = trCustRegisterInfoDao.findProdTrCustTransEffective(params);
        return custTransEffect;
    }

    @API(desc = "校验失败详情", auth = APIAuth.YES)
    public SqlResult<TrCustRegisterInfo> findValidateInfos(SqlParam<TrCustRegisterInfo> params) throws Exception {
        return trCustRegisterInfoDao.findValidateInfos(params);
    }

    @API(desc = "联动查询个人证件类别字典", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getPersonalIdTypeDict(SqlParam<TrCustRegisterInfo> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("inOutSign", param.getModel().getInOutSign());
        List<SqlRow> tempTypeByDocType = trCustRegisterInfoDao.getPersonalIdTypeDict(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);
        ;
        return sqlRowSqlResult;
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

        ExcelToMapInfo dataType = new ExcelToMapInfo();
        dataType.setDict("tr_data_type");  //字典值
        dataType.setFieldIndex(1);// EXCEL列位置
        dataType.setFieldName("数据类型");
        dataType.setField("dataType"); //字段
        dataType.setFieldType(ExcelEnum.ENUM);//数据类型
        dataType.setLength(0); //长度校验 0 不校验
        dataType.setNotNULL(true);// 是否非空
        list.add(dataType);

        ExcelToMapInfo custNo = new ExcelToMapInfo();
        custNo.setFieldIndex(2); // EXCEL列位置
        custNo.setFieldName("识别标识");
        custNo.setField("custNo"); //字段
        custNo.setFieldType(ExcelEnum.TEXT); //数据类型
        custNo.setLength(0); //长度校验 0 不校验
        custNo.setNotNULL(false); // 是否非空
        list.add(custNo);

        ExcelToMapInfo isBelong = new ExcelToMapInfo();
        isBelong.setDict("tr_is_belong");  //字典值
        isBelong.setFieldIndex(3); // EXCEL列位置
        isBelong.setFieldName("投资者是否属于本行");
        isBelong.setField("isBelong"); //字段
        isBelong.setFieldType(ExcelEnum.ENUM); //数据类型
        isBelong.setLength(0); //长度校验 0 不校验
        isBelong.setNotNULL(true); // 是否非空
        list.add(isBelong);

        ExcelToMapInfo issBankName = new ExcelToMapInfo();
        issBankName.setFieldIndex(4); // EXCEL列位置
        issBankName.setFieldName("投资者所属银行名称");
        issBankName.setField("issBankName"); //字段
        issBankName.setFieldType(ExcelEnum.TEXT); //数据类型
        issBankName.setLength(0); //长度校验 0 不校验
        issBankName.setNotNULL(false); // 是否非空
        list.add(issBankName);


        ExcelToMapInfo issBankCode = new ExcelToMapInfo();
        issBankCode.setFieldIndex(5); // EXCEL列位置
        issBankCode.setFieldName("投资者所属银行代码");
        issBankCode.setField("issBankCode"); //字段
        issBankCode.setFieldType(ExcelEnum.TEXT); //数据类型
        issBankCode.setLength(0); //长度校验 0 不校验
        issBankCode.setNotNULL(false); // 是否非空
        list.add(issBankCode);

        ExcelToMapInfo inOutSign = new ExcelToMapInfo();
        inOutSign.setDict("tr_in_out_sign");  //字典值
        inOutSign.setFieldIndex(6); // EXCEL列位置
        inOutSign.setFieldName("投资者境内外标识");
        inOutSign.setField("inOutSign"); //字段
        inOutSign.setFieldType(ExcelEnum.ENUM); //数据类型
        inOutSign.setLength(0); //长度校验 0 不校验
        inOutSign.setNotNULL(true); // 是否非空
        list.add(inOutSign);

        ExcelToMapInfo issCountry = new ExcelToMapInfo();
        issCountry.setDict("tr_iss_country");  //字典值
        issCountry.setFieldIndex(7); // EXCEL列位置
        issCountry.setFieldName("投资者所属国家或地区");
        issCountry.setField("issCountry"); //字段
        issCountry.setFieldType(ExcelEnum.ENUM); //数据类型
        issCountry.setLength(0); //长度校验 0 不校验
        issCountry.setNotNULL(true); // 是否非空
        list.add(issCountry);

        ExcelToMapInfo custType = new ExcelToMapInfo();
        custType.setDict("tr_cust_type");  //字典值
        custType.setFieldIndex(8); // EXCEL列位置
        custType.setFieldName("投资者类别");
        custType.setField("custType"); //字段
        custType.setFieldType(ExcelEnum.ENUM); //数据类型
        custType.setLength(0); //长度校验 0 不校验
        custType.setNotNULL(true); // 是否非空
        list.add(custType);

        ExcelToMapInfo personalIdType = new ExcelToMapInfo();
        personalIdType.setDict("tr_personal_id_type");  //字典值
        personalIdType.setFieldIndex(9); // EXCEL列位置
        personalIdType.setFieldName("个人证件类别");
        personalIdType.setField("personalIdType"); //字段
        personalIdType.setFieldType(ExcelEnum.ENUM); //数据类型
        personalIdType.setLength(0); //长度校验 0 不校验
        personalIdType.setNotNULL(true); // 是否非空
        list.add(personalIdType);


        ExcelToMapInfo organizationIdType = new ExcelToMapInfo();
        organizationIdType.setDict("tr_organization_id_type");  //字典值
        organizationIdType.setFieldIndex(10); // EXCEL列位置
        organizationIdType.setFieldName("机构证件类别");
        organizationIdType.setField("organizationIdType"); //字段
        organizationIdType.setFieldType(ExcelEnum.ENUM); //数据类型
        organizationIdType.setLength(0); //长度校验 0 不校验
        organizationIdType.setNotNULL(true); // 是否非空
        list.add(organizationIdType);


        ExcelToMapInfo otherIdName = new ExcelToMapInfo();
        otherIdName.setFieldIndex(11); // EXCEL列位置
        otherIdName.setFieldName("其他证件名称");
        otherIdName.setField("otherIdName"); //字段
        otherIdName.setFieldType(ExcelEnum.TEXT); //数据类型
        otherIdName.setLength(0); //长度校验 0 不校验
        otherIdName.setNotNULL(true); // 是否非空
        list.add(otherIdName);

        ExcelToMapInfo idCode = new ExcelToMapInfo();
        idCode.setFieldIndex(12); // EXCEL列位置
        idCode.setFieldName("证件号码");
        idCode.setField("idCode"); //字段
        idCode.setFieldType(ExcelEnum.TEXT); //数据类型
        idCode.setLength(0); //长度校验 0 不校验
        idCode.setNotNULL(true); // 是否非空
        list.add(idCode);


        ExcelToMapInfo spvOpenBank = new ExcelToMapInfo();
        spvOpenBank.setDict("tr_spv_open_bank");  //字典值
        spvOpenBank.setFieldIndex(13); // EXCEL列位置
        spvOpenBank.setFieldName("SPV资金托管账户开户行");
        spvOpenBank.setField("spvOpenBank"); //字段
        spvOpenBank.setFieldType(ExcelEnum.ENUM); //数据类型
        spvOpenBank.setLength(0); //长度校验 0 不校验
        spvOpenBank.setNotNULL(true); // 是否非空
        list.add(spvOpenBank);

        ExcelToMapInfo otherOpenBank = new ExcelToMapInfo();
        otherOpenBank.setFieldIndex(14); // EXCEL列位置
        otherOpenBank.setFieldName("其他资金托管账户开户行");
        otherOpenBank.setField("otherOpenBank"); //字段
        otherOpenBank.setFieldType(ExcelEnum.TEXT); //数据类型
        otherOpenBank.setLength(0); //长度校验 0 不校验
        otherOpenBank.setNotNULL(false); // 是否非空
        list.add(otherOpenBank);

        ExcelToMapInfo custName = new ExcelToMapInfo();
        custName.setFieldIndex(15); // EXCEL列位置
        custName.setFieldName("投资者名称");
        custName.setField("custName"); //字段
        custName.setFieldType(ExcelEnum.TEXT); //数据类型
        custName.setLength(0); //长度校验 0 不校验
        custName.setNotNULL(true); // 是否非空
        list.add(custName);

        ExcelToMapInfo sex = new ExcelToMapInfo();
        sex.setDict("tr_sex");  //字典值
        sex.setFieldIndex(16); // EXCEL列位置
        sex.setFieldName("性别");
        sex.setField("sex"); //字段
        sex.setFieldType(ExcelEnum.ENUM); //数据类型
        sex.setLength(0); //长度校验 0 不校验
        sex.setNotNULL(true); // 是否非空
        list.add(sex);

        ExcelToMapInfo riskLevel = new ExcelToMapInfo();
        riskLevel.setDict("tr_risk_level");  //字典值
        riskLevel.setFieldIndex(17); // EXCEL列位置
        riskLevel.setFieldName("风险偏好");
        riskLevel.setField("riskLevel"); //字段
        riskLevel.setFieldType(ExcelEnum.ENUM); //数据类型
        riskLevel.setLength(0); //长度校验 0 不校验
        riskLevel.setNotNULL(true); // 是否非空
        list.add(riskLevel);

		ExcelToMapInfo moble =  new ExcelToMapInfo();
		moble.setFieldIndex(18); // EXCEL列位置
		moble.setFieldName("手机号码");
		moble.setField("moble"); //字段
		moble.setFieldType(ExcelEnum.TEXT); //数据类型
		moble.setLength(0); //长度校验 0 不校验
		moble.setNotNULL(false); // 是否非空
		list.add(moble);

		ExcelToMapInfo telPhone =  new ExcelToMapInfo();
		telPhone.setFieldIndex(19); // EXCEL列位置
		telPhone.setFieldName("固定电话");
		telPhone.setField("telPhone"); //字段
		telPhone.setFieldType(ExcelEnum.TEXT); //数据类型
		telPhone.setLength(0); //长度校验 0 不校验
		telPhone.setNotNULL(false); // 是否非空
		list.add(telPhone);

		ExcelToMapInfo email =  new ExcelToMapInfo();
		email.setFieldIndex(20); // EXCEL列位置
		email.setFieldName("电子邮箱");
		email.setField("email"); //字段
		email.setFieldType(ExcelEnum.TEXT); //数据类型
		email.setLength(0); //长度校验 0 不校验
		email.setNotNULL(false); // 是否非空
		list.add(email);

		ExcelToMapInfo registerDate =  new ExcelToMapInfo();
		registerDate.setFieldIndex(21); // EXCEL列位置
		registerDate.setFieldName("投资者登记日期");
		registerDate.setField("registerDate"); //字段
		registerDate.setFieldType(ExcelEnum.DATE); //数据类型
		registerDate.setLength(0); //长度校验 0 不校验
		registerDate.setNotNULL(true); // 是否非空
		list.add(registerDate);

		ExcelToMapInfo remark =  new ExcelToMapInfo();
		remark.setFieldIndex(22); // EXCEL列位置
		remark.setFieldName("备注");
		remark.setField("remark"); //字段
		remark.setFieldType(ExcelEnum.TEXT); //数据类型
		remark.setLength(0); //长度校验 0 不校验
		remark.setNotNULL(false); // 是否非空
		list.add(remark);


		Map<String,Object> map  = excelToMapService.toMapAndCheck(list,sheet);

		boolean isError = (boolean) map.get("isError");

		if(isError){
			return RequestSupport.updateReturnJson(false, map.get("msg").toString(), null).toString();
		}
		List<Map<String,Object>> resList = (List<Map<String, Object>>) map.get("list");

		trCustRegisterInfoDao.addTrCustRegisterInfoBatch(resList);

		return RequestSupport.updateReturnJson(true, map.get("msg").toString(), null).toString();

	}


    public void importCustRegisterInfo(List<TrCustRegisterInfo> custRegisterInfos, Map<String, Object> params) throws Exception {
        // 添加至操作记录
        //trCustRegisterInfoDao.deleteImportSubseqSubscrRegistInfo(params);  //导入操作，不执行删除操作 程晓鹏 2024.11.26 modify
        for (TrCustRegisterInfo custRegisterInfo : custRegisterInfos) {
            Map<String, Object> map = BeanUtil.beanToMap(custRegisterInfo);
            //导入不加操作记录
//			custRegistMarkService.addImportSubseqSubscrRegist(custRegisterInfo,OperatorEnum.IMPORT.getVal());
            trCustRegisterInfoDao.addImportTrCustRegisterInfo(map);
        }
    }

    public void updateImportCustRegisterInfo(List<TrCustRegisterInfo> custRegisterInfos, Map<String, Object> params) throws Exception {
        // 添加至操作记录
        //trCustRegisterInfoDao.deleteImportSubseqSubscrRegistInfo(params);  //导入操作，不执行删除操作 程晓鹏 2024.11.26 modify
        for (TrCustRegisterInfo custRegisterInfo : custRegisterInfos) {
            Map<String, Object> map = BeanUtil.beanToMap(custRegisterInfo);
            //导入不加操作记录
//			custRegistMarkService.addImportSubseqSubscrRegist(custRegisterInfo,OperatorEnum.IMPORT.getVal());
            //trCustRegisterInfoDao.addImportSubseqSubscrRegistInfo(map);
            trCustRegisterInfoDao.updateRegisterInfoByCustNo(map); //变更为根据cust_no, report_date 进行数据更新  程晓鹏 2024.11.26 modify
        }
    }

    //手动更新确认未报送成功的数据为报送成功
    @API(desc = "确认报送状态为成功", auth = APIAuth.YES)
    public String updateTrCustRegisterInfoRegistStatusSuccess(SqlParam<TrCustRegisterInfo> params) throws Exception {
        try {
            //查询 0 初始化 或 1 校验失败的数据。存在需提前处理
            int  recordCnt = trCustRegisterInfoDao.findTrCustRegisterInfosCount(params);
            if (recordCnt == 0) {
                return RequestSupport.updateReturnJson(false,  "没有需要确认报送状态的数据，请检查！", null).toString();
            }
            int unreadyCnt= trCustRegisterInfoDao.findTrCustRegisterInfosFailStatus(params);
            if (unreadyCnt > 0) {
                return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后确认报送状态！", null).toString();
            }
//			Map<String, Object> map = BeanUtil.beanToMap(TrCustRegisterInfo);
//            if(params==null || (params.getModel().getReportDate()==null && params.getModel().getQueryStartDate()==null)){
//                return RequestSupport.updateReturnJson(false, "请先选择日期！", null).toString();
//            }
            trCustRegisterInfoDao.updateRegisterInfoRegisterStatusSuccess(params);
            return RequestSupport.updateReturnJson(true, "成功！", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "失败！", null).toString();
        }
    }

    @API(desc = "投资者身份信息下载", operation = APIOperation.UPDATE, auth = APIAuth.YES)
    public String download(SqlParam<TrCustRegisterInfo> params) throws Exception {
        try {
            Map<String, Object> param = params.getParamsDirect();
            String processInstanceId = (String) param.get("processInstanceId");
            param.put("modelClassName", TrCustRegisterInfo.class.getName());
            param.put("action", "findTrCustRegisterInfos1");
            String actionParamsStr = Tools.obj2Str(param.get("action_params"));
            String reportDate = JSONObject.parseObject(actionParamsStr).getString("reportDate");
            Runnable runnable = () -> {
                // 如果是多天，需要分别生成文件并更新更新文件路径
                String[] reportDates = reportDate.split(",");
                for (String date : reportDates) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(actionParamsStr);
                        Map<String, String> repMap = new HashMap<>();
                        repMap.put("reportDate", date);
                        if (StringUtils.isNotBlank(jsonObject.getString("personalIdType"))) {
                            repMap.put("personalIdType", jsonObject.getString("personalIdType"));
                        }
                        if (StringUtils.isNotBlank(jsonObject.getString("organizationIdType"))) {
                            repMap.put("organizationIdType", jsonObject.getString("organizationIdType"));
                        }
                        if (StringUtils.isNotBlank(jsonObject.getString("dataType"))) {
                            repMap.put("dataType", jsonObject.getString("dataType"));
                        }
                        if (StringUtils.isNotBlank(jsonObject.getString("custNo"))) {
                            repMap.put("custNo", jsonObject.getString("custNo"));
                        }
                        if (StringUtils.isNotBlank(jsonObject.getString("taId"))) {
                            repMap.put("taId", jsonObject.getString("taId"));
                        }
                        if (StringUtils.isNotBlank(jsonObject.getString("custType"))) {
                            repMap.put("custType", jsonObject.getString("custType"));
                        }
                        if (StringUtils.isNotBlank(jsonObject.getString("idCode"))) {
                            repMap.put("idCode", jsonObject.getString("idCode"));
                        }
                        if (StringUtils.isNotBlank(jsonObject.getString("registerStatus"))) {
                            repMap.put("registerStatus", jsonObject.getString("registerStatus"));
                        }
                        param.put("action_params", JSONObject.toJSON(repMap));
                        Map<String, String> resMap = excelWriter.downloadEx(param);

						BaseReportExportLog exportLog = new BaseReportExportLog();
						exportLog.setFilePath(resMap.get("local"));
						exportLog.setRemotePath(resMap.get("remote"));
						exportLog.setDataTime(date);
						exportLog.setProcessInstanceId(processInstanceId);
						exportLog.setFileStatus("2");
						baseReportExportLogService.updateDataTimePath(exportLog);

						log.info("投资者身份信息下载{}文件生成成功，更新文件路径成功！", date);
					} catch (Exception e) {
						log.error("投资者身份信息下载{}文件生成失败，更新文件路径失败！", date, e);
					}
				}
			};
			Thread thread = new Thread(runnable);
			thread.start();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return RequestSupport.updateReturnJson(true, "后台处理中，请在【中债直连】【导出文件下载】菜单查看", null).toString();
	}
}
