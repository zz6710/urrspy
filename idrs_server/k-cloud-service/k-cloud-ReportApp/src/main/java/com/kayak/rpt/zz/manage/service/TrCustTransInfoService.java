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
import com.kayak.rpt.zz.manage.dao.TrCustTransInfoDao;
import com.kayak.rpt.zz.manage.enums.ExcelEnum;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.*;
import com.kayak.rpt.zz.operate.model.CustVolRegister;
import com.kayak.rpt.zz.operate.service.CustTransMarkService;
import com.kayak.utils.InvExcelWriter;
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
@APIDefine(desc = "投资者明细信息登记表服务", model = TrCustTransInfo.class)
@Slf4j
public class TrCustTransInfoService {

    @Autowired
    private TrCustTransInfoDao trCustTransInfoDao;

    @Autowired
    private ExcelToMapService excelToMapService;

    @Autowired
    InvExcelWriter invExcelWriter;

    @Autowired
    BaseReportExportLogService baseReportExportLogService;

    @Autowired
    private CustTransMarkService custTransMarkService;

    @API(desc = "查询投资者明细信息登记表信息", auth = APIAuth.YES)
    public SqlResult<TrCustTransInfo> findTrCustTransInfos(SqlParam<TrCustTransInfo> params) throws Exception {
        SqlResult<TrCustTransInfo> handledResult;
        try {
            handledResult = trCustTransInfoDao.findTrCustTransInfos(params);
            //判断是否进行文档下载数据脱敏操作
            if ("true".equals(params.getModel().getIsDesensitization())) {
                int maxCount = handledResult.getRows().size();
                for (int i = 0; i < maxCount; i++) {
                    TrCustTransInfo customer = handledResult.getRows().get(i);
                    deSensitiveModel(customer);
                }
            }
            return handledResult;
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("app_cust_trans_info_")) {
                handledResult = new SqlResult<>();
                List<TrCustTransInfo> list = new ArrayList<>();
                handledResult.setRows(list);
                return handledResult;
            }
            throw e;
        }
    }

    public SqlResult<TrCustTransInfo> findTrCustTransInfosID(SqlParam<TrCustTransInfo> params) throws Exception {
        SqlResult<TrCustTransInfo> handledResult = trCustTransInfoDao.findTrCustTransInfosID(params);

        //判断是否进行文档下载数据脱敏操作
        if ("true".equals(params.getModel().getIsDesensitization())) {
            int maxCount = handledResult.getRows().size();
            for (int i = 0; i < maxCount; i++) {
                TrCustTransInfo customer = handledResult.getRows().get(i);
                deSensitiveModel(customer);
            }
        }

        return handledResult;
    }

    @API(desc = "查询投资者明细信息登记表信息", auth = APIAuth.NO)
    public SqlResult<TrCustTransInfo> findTrCustTransInfosAndIsError(SqlParam<TrCustTransInfo> params) throws Exception {
        params.setMakeSql(true);
        return trCustTransInfoDao.findTrCustTransInfosAndIsError(params);
    }

    @API(desc = "校验失败详情", auth = APIAuth.YES)
    public SqlResult<TrCustTransInfo> findValidateInfos(SqlParam<TrCustTransInfo> params) throws Exception {
        return trCustTransInfoDao.findValidateInfos(params);
    }

    @API(desc = "添加投资者明细信息登记表", params = "bank_code,trans_serno,contract_no,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,acct_no,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,acct_bank_no,acct_bank_name,deal_no", auth = APIAuth.YES)
    public String addTrCustTransInfo(SqlParam<TrCustTransInfo> params) throws Exception {
        try {
            // 操作记录
//			custTransMarkService.addCustTransMark(params, OperatorEnum.CREATE.getVal());
            trCustTransInfoDao.addTrCustTransInfo(params);
            return RequestSupport.updateReturnJson(true, "添加成功！", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "添加失败！", null).toString();
        }
    }

    @API(desc = "修改投资者明细信息登记表", params = "bank_code,trans_serno,contract_no,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,acct_no,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,acct_bank_no,acct_bank_name,deal_no", operation = APIOperation.UPDATE, auth = APIAuth.YES)
    public int updateTrCustTransInfo(SqlParam<TrCustTransInfo> params) throws Exception {
        // 操作记录
        Map paramMap = new HashMap<>();
        paramMap.put("registerSerno", params.getModel().getRegisterSerno());
        paramMap.put("reportDate", params.getModel().getReportDate());
        SqlParam<TrCustTransInfo> oldParams = new FetcherData<>(paramMap, TrCustTransInfo.class);
        SqlResult<TrCustTransInfo> originParams = trCustTransInfoDao.findTrCustTransInfos(oldParams);
        if (originParams.getRows().size() > 0) {
            TrCustTransInfo param = originParams.getRows().get(0);
            paramMap = BeanUtil.beanToMap(param);
            oldParams = new FetcherData<>(paramMap, TrCustTransInfo.class);
        }
        custTransMarkService.addCustTransMark(oldParams, OperatorEnum.UPDATE.getVal());
        return trCustTransInfoDao.updateTrCustTransInfo(params).getEffect();
    }

    @API(desc = "删除投资者明细信息登记表", params = "bank_code,trans_serno,contract_no,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,acct_no,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,act_bank_no,acct_bank_name,deal_no", auth = APIAuth.YES)
    public int deleteTrCustTransInfo(SqlParam<TrCustTransInfo> params) throws Exception {
        // 操作记录
        custTransMarkService.addCustTransMark(params, OperatorEnum.DELETE.getVal());
        return trCustTransInfoDao.deleteTrCustTransInfo(params).getEffect();
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
        bankCode.setFieldName("登记机构代码");
        bankCode.setField("bankCode"); //字段
        bankCode.setFieldType(ExcelEnum.TEXT); //数据类型
        bankCode.setLength(0); //长度校验 0 不校验
        bankCode.setNotNULL(true); // 是否非空
        list.add(bankCode);

        ExcelToMapInfo contractNo = new ExcelToMapInfo();
        contractNo.setFieldIndex(1); // EXCEL列位置
        contractNo.setFieldName("销售合同号");
        contractNo.setField("contractNo"); //字段
        contractNo.setFieldType(ExcelEnum.TEXT); //数据类型
        contractNo.setLength(0); //长度校验 0 不校验
        contractNo.setNotNULL(false); // 是否非空
        list.add(contractNo);

        ExcelToMapInfo transSerno = new ExcelToMapInfo();
        transSerno.setFieldIndex(2);// EXCEL列位置
        transSerno.setFieldName("核心交易流水号");
        transSerno.setField("transSerno"); //字段
        transSerno.setFieldType(ExcelEnum.TEXT);//数据类型
        transSerno.setLength(0); //长度校验 0 不校验
        transSerno.setNotNULL(true);// 是否非空
        list.add(transSerno);

        ExcelToMapInfo fncTransAcctNo = new ExcelToMapInfo();
        fncTransAcctNo.setFieldIndex(3); // EXCEL列位置
        fncTransAcctNo.setFieldName("理财账号");
        fncTransAcctNo.setField("fncTransAcctNo"); //字段
        fncTransAcctNo.setFieldType(ExcelEnum.TEXT); //数据类型
        fncTransAcctNo.setLength(0); //长度校验 0 不校验
        fncTransAcctNo.setNotNULL(true); // 是否非空
        list.add(fncTransAcctNo);

        ExcelToMapInfo hostCustNo = new ExcelToMapInfo();
        hostCustNo.setFieldIndex(4); // EXCEL列位置
        hostCustNo.setFieldName("客户统一编号");
        hostCustNo.setField("hostCustNo"); //字段
        hostCustNo.setFieldType(ExcelEnum.TEXT); //数据类型
        hostCustNo.setLength(0); //长度校验 0 不校验
        hostCustNo.setNotNULL(true); // 是否非空
        list.add(hostCustNo);

        ExcelToMapInfo custNo = new ExcelToMapInfo();
        custNo.setFieldIndex(5); // EXCEL列位置
        custNo.setFieldName("识别标识");
        custNo.setField("custNo"); //字段
        custNo.setFieldType(ExcelEnum.TEXT); //数据类型
        custNo.setLength(0); //长度校验 0 不校验
        custNo.setNotNULL(true); // 是否非空
        list.add(custNo);

        ExcelToMapInfo dealNo = new ExcelToMapInfo();
        dealNo.setFieldIndex(6); // EXCEL列位置
        dealNo.setFieldName("交易序列号");
        dealNo.setField("custNo"); //字段
        dealNo.setFieldType(ExcelEnum.TEXT); //数据类型
        dealNo.setLength(0); //长度校验 0 不校验
        dealNo.setNotNULL(true); // 是否非空
        list.add(dealNo);

        ExcelToMapInfo acctNo = new ExcelToMapInfo();
        acctNo.setFieldIndex(7); // EXCEL列位置
        acctNo.setFieldName("关联活期存款账号");
        acctNo.setField("acctNo"); //字段
        acctNo.setFieldType(ExcelEnum.TEXT); //数据类型
        acctNo.setLength(0); //长度校验 0 不校验
        acctNo.setNotNULL(true); // 是否非空
        list.add(acctNo);

        ExcelToMapInfo acctBankNo = new ExcelToMapInfo();
        acctBankNo.setFieldIndex(8); // EXCEL列位置
        acctBankNo.setFieldName("关联活期存款账号开户行代码");
        acctBankNo.setField("acctBankNo"); //字段
        acctBankNo.setFieldType(ExcelEnum.TEXT); //数据类型
        acctBankNo.setLength(0); //长度校验 0 不校验
        acctBankNo.setNotNULL(true); // 是否非空
        list.add(acctBankNo);

        ExcelToMapInfo acctBankName = new ExcelToMapInfo();
        acctBankName.setFieldIndex(9); // EXCEL列位置
        acctBankName.setFieldName("关联活期存款账号开户行名称");
        acctBankName.setField("acctBankName"); //字段
        acctBankName.setFieldType(ExcelEnum.TEXT); //数据类型
        acctBankName.setLength(0); //长度校验 0 不校验
        acctBankName.setNotNULL(true); // 是否非空
        list.add(acctBankName);

        ExcelToMapInfo acctLocCode = new ExcelToMapInfo();
        acctLocCode.setDict("tr_buy_place");  //字典值
        acctLocCode.setFieldIndex(10); // EXCEL列位置
        acctLocCode.setFieldName("关联账号开户所在地");
        acctLocCode.setField("acctLocCode"); //字段
        acctLocCode.setFieldType(ExcelEnum.ENUM); //数据类型
        acctLocCode.setLength(0); //长度校验 0 不校验
        acctLocCode.setNotNULL(true); // 是否非空
        list.add(acctLocCode);

        ExcelToMapInfo isAgent = new ExcelToMapInfo();
        isAgent.setDict("tr_is_belong");  //字典值
        isAgent.setFieldIndex(11); // EXCEL列位置
        isAgent.setFieldName("是否代销");
        isAgent.setField("isAgent"); //字段
        isAgent.setFieldType(ExcelEnum.ENUM); //数据类型
        isAgent.setLength(0); //长度校验 0 不校验
        isAgent.setNotNULL(true); // 是否非空
        list.add(isAgent);

        ExcelToMapInfo agentBankCode = new ExcelToMapInfo();
        agentBankCode.setFieldIndex(12); // EXCEL列位置
        agentBankCode.setFieldName("销售机构代码");
        agentBankCode.setField("agentBankCode"); //字段
        agentBankCode.setFieldType(ExcelEnum.TEXT); //数据类型
        agentBankCode.setLength(0); //长度校验 0 不校验
        agentBankCode.setNotNULL(true); // 是否非空
        list.add(agentBankCode);

        ExcelToMapInfo agentBankName = new ExcelToMapInfo();
        agentBankName.setFieldIndex(13); // EXCEL列位置
        agentBankName.setFieldName("销售机构名称");
        agentBankName.setField("agentBankName"); //字段
        agentBankName.setFieldType(ExcelEnum.TEXT); //数据类型
        agentBankName.setLength(0); //长度校验 0 不校验
        agentBankName.setNotNULL(true); // 是否非空
        list.add(agentBankName);

        ExcelToMapInfo agentReguCode = new ExcelToMapInfo();
        agentReguCode.setDict("tr_agent_regu_code");  //字典值
        agentReguCode.setFieldIndex(14); // EXCEL列位置
        agentReguCode.setFieldName("销售机构所属监管机构");
        agentReguCode.setField("agentReguCode"); //字段
        agentReguCode.setFieldType(ExcelEnum.ENUM); //数据类型
        agentReguCode.setLength(0); //长度校验 0 不校验
        agentReguCode.setNotNULL(true); // 是否非空
        list.add(agentReguCode);

        ExcelToMapInfo prodCode = new ExcelToMapInfo();
        prodCode.setFieldIndex(15); // EXCEL列位置
        prodCode.setFieldName("产品登记编码");
        prodCode.setField("prodCode"); //字段
        prodCode.setFieldType(ExcelEnum.TEXT); //数据类型
        prodCode.setLength(0); //长度校验 0 不校验
        prodCode.setNotNULL(true); // 是否非空
        list.add(prodCode);

        ExcelToMapInfo sonShareCode = new ExcelToMapInfo();
        sonShareCode.setFieldIndex(16); // EXCEL列位置
        sonShareCode.setFieldName("子份额代码");
        sonShareCode.setField("sonShareCode"); //字段
        sonShareCode.setFieldType(ExcelEnum.TEXT); //数据类型
        sonShareCode.setLength(0); //长度校验 0 不校验
        sonShareCode.setNotNULL(true); // 是否非空
        list.add(sonShareCode);

        ExcelToMapInfo busiCode = new ExcelToMapInfo();
        busiCode.setDict("tr_busi_code");  //字典值
        busiCode.setFieldIndex(17); // EXCEL列位置
        busiCode.setFieldName("业务种类");
        busiCode.setField("busiCode"); //字段
        busiCode.setFieldType(ExcelEnum.ENUM); //数据类型
        busiCode.setLength(0); //长度校验 0 不校验
        busiCode.setNotNULL(true); // 是否非空
        list.add(busiCode);

        ExcelToMapInfo busiReguCode = new ExcelToMapInfo();
        busiReguCode.setDict("tr_agent_regu_code");  //字典值
        busiReguCode.setFieldIndex(18); // EXCEL列位置
        busiReguCode.setFieldName("业务发生地所属监管");
        busiReguCode.setField("busiReguCode"); //字段
        busiReguCode.setFieldType(ExcelEnum.ENUM); //数据类型
        busiReguCode.setLength(0); //长度校验 0 不校验
        busiReguCode.setNotNULL(true); // 是否非空
        list.add(busiReguCode);

        ExcelToMapInfo ackDate = new ExcelToMapInfo();
        ackDate.setFieldIndex(19); // EXCEL列位置
        ackDate.setFieldName("业务确认日期");
        ackDate.setField("ackDate"); //字段
        ackDate.setFieldType(ExcelEnum.DATE); //数据类型
        ackDate.setLength(0); //长度校验 0 不校验
        ackDate.setNotNULL(true); // 是否非空
        list.add(ackDate);

        ExcelToMapInfo ackTime = new ExcelToMapInfo();
        ackTime.setFieldIndex(20); // EXCEL列位置
        ackTime.setFieldName("业务确认时间");
        ackTime.setField("ackTime"); //字段
        ackTime.setFieldType(ExcelEnum.DATE); //数据类型
        ackTime.setLength(0); //长度校验 0 不校验
        ackTime.setNotNULL(true); // 是否非空
        list.add(ackTime);

        ExcelToMapInfo cur = new ExcelToMapInfo();
        cur.setDict("tr_cur");  //字典值
        cur.setFieldIndex(21); // EXCEL列位置
        cur.setFieldName("币种");
        cur.setField("cur"); //字段
        cur.setFieldType(ExcelEnum.ENUM); //数据类型
        cur.setLength(0); //长度校验 0 不校验
        cur.setNotNULL(true); // 是否非空
        list.add(cur);

        ExcelToMapInfo speChannelFlag = new ExcelToMapInfo();
        speChannelFlag.setDict("subm_tr_spe_channel_flag_z");  //字典值
        speChannelFlag.setFieldIndex(22); // EXCEL列位置
        speChannelFlag.setFieldName("特殊渠道");
        speChannelFlag.setField("speChannelFlag"); //字段
        speChannelFlag.setFieldType(ExcelEnum.ENUM); //数据类型
        speChannelFlag.setLength(0); //长度校验 0 不校验
        speChannelFlag.setNotNULL(true); // 是否非空
        list.add(speChannelFlag);

        ExcelToMapInfo ackAmt = new ExcelToMapInfo();
        ackAmt.setFieldIndex(23); // EXCEL列位置
        ackAmt.setFieldName("金额");
        ackAmt.setField("ackAmt"); //字段
        ackAmt.setFieldType(ExcelEnum.TEXT); //数据类型
        ackAmt.setLength(0); //长度校验 0 不校验
        ackAmt.setNotNULL(true); // 是否非空
        list.add(ackAmt);

        ExcelToMapInfo convertRmb = new ExcelToMapInfo();
        convertRmb.setFieldIndex(24); // EXCEL列位置
        convertRmb.setFieldName("折算人民币金额");
        convertRmb.setField("convertRmb"); //字段
        convertRmb.setFieldType(ExcelEnum.TEXT); //数据类型
        convertRmb.setLength(0); //长度校验 0 不校验
        convertRmb.setNotNULL(true); // 是否非空
        list.add(convertRmb);

        ExcelToMapInfo nav = new ExcelToMapInfo();
        nav.setFieldIndex(25); // EXCEL列位置
        nav.setFieldName("确认净值");
        nav.setField("nav"); //字段
        nav.setFieldType(ExcelEnum.TEXT); //数据类型
        nav.setLength(0); //长度校验 0 不校验
        nav.setNotNULL(true); // 是否非空
        list.add(nav);

        ExcelToMapInfo ackVol = new ExcelToMapInfo();
        ackVol.setFieldIndex(26); // EXCEL列位置
        ackVol.setFieldName("份额");
        ackVol.setField("ackVol"); //字段
        ackVol.setFieldType(ExcelEnum.TEXT); //数据类型
        ackVol.setLength(0); //长度校验 0 不校验
        ackVol.setNotNULL(true); // 是否非空
        list.add(ackVol);

        ExcelToMapInfo feeAmt = new ExcelToMapInfo();
        feeAmt.setFieldIndex(27); // EXCEL列位置
        feeAmt.setFieldName("费用");
        feeAmt.setField("feeAmt"); //字段
        feeAmt.setFieldType(ExcelEnum.TEXT); //数据类型
        feeAmt.setLength(0); //长度校验 0 不校验
        feeAmt.setNotNULL(true); // 是否非空
        list.add(feeAmt);

        ExcelToMapInfo channelFlag = new ExcelToMapInfo();
        channelFlag.setDict("tr_channel_flag_z");  //字典值
        channelFlag.setFieldIndex(28); // EXCEL列位置
        channelFlag.setFieldName("渠道");
        channelFlag.setField("channelFlag"); //字段
        channelFlag.setFieldType(ExcelEnum.ENUM); //数据类型
        channelFlag.setLength(0); //长度校验 0 不校验
        channelFlag.setNotNULL(true); // 是否非空
        list.add(channelFlag);

        ExcelToMapInfo inputuser = new ExcelToMapInfo();
        inputuser.setFieldIndex(29); // EXCEL列位置
        inputuser.setFieldName("交易柜员号");
        inputuser.setField("inputuser"); //字段
        inputuser.setFieldType(ExcelEnum.TEXT); //数据类型
        inputuser.setLength(0); //长度校验 0 不校验
        inputuser.setNotNULL(true); // 是否非空
        list.add(inputuser);

        ExcelToMapInfo remark = new ExcelToMapInfo();
        remark.setFieldIndex(30); // EXCEL列位置
        remark.setFieldName("备注");
        remark.setField("remark"); //字段
        remark.setFieldType(ExcelEnum.TEXT); //数据类型
        remark.setLength(0); //长度校验 0 不校验
        remark.setNotNULL(true); // 是否非空
        list.add(remark);

        Map<String, Object> map = excelToMapService.toMapAndCheck(list, sheet);
        boolean isError = (boolean) map.get("isError");

        if (isError) {
            return RequestSupport.updateReturnJson(false, map.get("msg").toString(), null).toString();
        }
        List<Map<String, Object>> resList = (List<Map<String, Object>>) map.get("list");
        trCustTransInfoDao.addTrCustTransInfofoBatch(resList);
        return RequestSupport.updateReturnJson(true, map.get("msg").toString(), null).toString();
    }


    public void importCustTransInfo(List<TrCustTransInfo> custTransInfos, Map<String, Object> params) throws Exception {
        // 添加至操作记录
        //trCustTransInfoDao.deleteImportCustTransInfo(params); //不进行删除操作  程晓鹏 2024.11.27 modify
        //页面导入不选日期，默认取系统当前工作日
        String reportDate = DateUtil.getSysWordDay();
        for (TrCustTransInfo custTransInfo : custTransInfos) {
            custTransInfo.setReportDate(reportDate);
            custTransInfo.setTheoryReportStartDate(reportDate);
            Map<String, Object> map = BeanUtil.beanToMap(custTransInfo);
            //导入不插入操作记录
//			custTransMarkService.addImportCustTrans(custTransInfo,OperatorEnum.IMPORT.getVal());
            trCustTransInfoDao.addImportCustTransInfo(map);
        }
    }


    public void updateImportCustTransInfo(List<TrCustTransInfo> custTransInfos, Map<String, Object> params) throws Exception {
        // 添加至操作记录
        //trCustTransInfoDao.deleteImportCustTransInfo(params); //不进行删除操作  程晓鹏 2024.11.27 modify
        for (TrCustTransInfo custTransInfo : custTransInfos) {
            custTransInfo.setReportDate((String) params.get("reportDate"));
            Map<String, Object> map = BeanUtil.beanToMap(custTransInfo);
            //导入不插入操作记录
//			custTransMarkService.addImportCustTrans(custTransInfo,OperatorEnum.IMPORT.getVal());
            //trCustTransInfoDao.addImportCustTransInfo(map);
            trCustTransInfoDao.updateImportCustTransInfoByCustNo(map); //对导入的数据进行更新操作 程晓鹏 2024.11.27 modify
        }
    }

    //手动更新确认未报送成功的数据为报送成功
    @API(desc = "确认报送状态为成功", auth = APIAuth.YES)
    public String updateCustTransInfoRegistStatusSuccess(SqlParam<TrCustTransInfo> params) throws Exception {
        try {
            //查询 0 初始化 或 1 校验失败的数据。存在需提前处理
            int  recordCnt = trCustTransInfoDao.findTrCustTransInfosCount(params);
            if (recordCnt == 0) {
                return RequestSupport.updateReturnJson(false,  "没有需要确认报送状态的数据，请检查！", null).toString();
            }
            int unreadyCnt= trCustTransInfoDao.findTrCustTransInfosFailStatus(params);
            if (unreadyCnt > 0) {
                return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后确认报送状态！", null).toString();
            }
//			Map<String, Object> map = BeanUtil.beanToMap(TrCustTransInfo);
//            if(params==null || params.getModel().getReportDate()==null){
//                return RequestSupport.updateReturnJson(false, "请先选择日期！", null).toString();
//            }
            trCustTransInfoDao.updateCustTransInfoRegistStatusSuccess(params);
            return RequestSupport.updateReturnJson(true, "成功！", null).toString();
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("app_cust_trans_info_")) {
                return RequestSupport.updateReturnJson(false, "未找到所选日期对应的数据表，请先确认！", null).toString();
            }else{
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "失败！", null).toString();
            }
        }
    }

    public void importModifyCustTransInfo(List<TrCustTransInfo> custTransInfos) throws Exception {
        // 删除当天的导入变更记录
        Map<String, Object> params = new HashMap<>();
        params.put("createDate", DateUtil.getNowDate());
        trCustTransInfoDao.deleteImportModifyCustTransInfo(params);
        for (TrCustTransInfo custTransInfo : custTransInfos) {
            Map<String, Object> map = BeanUtil.beanToMap(custTransInfo);
            //导入不加操作记录
//			custTransMarkService.addImportCustTrans(custTransInfo,OperatorEnum.MODIFY.getVal());
            trCustTransInfoDao.addImportModifyCustTransInfo(map);
        }
    }

	@API(desc = "投资者明细信息下载", operation = APIOperation.UPDATE,auth = APIAuth.YES)
	public String download(SqlParam<TrCustTransInfo> params) throws Exception {
		try {
			Map<String, Object> param = params.getParamsDirect();
			String processInstanceId = (String) param.get("processInstanceId");
			param.put("modelClassName", TrCustTransInfo.class.getName());
			param.put("action", "findTrCustTransInfos");
			String actionParamsStr = Tools.obj2Str(param.get("action_params"));
			param.put("action_params", Tools.str2Json(Tools.obj2Str(actionParamsStr.replace("}",",\"isDesensitization\":\"true\"}"))));//是否进行数据脱敏操作标识,请勿随意改动
			String reportDate = JSONObject.parseObject(actionParamsStr).getString("reportDate");
			Runnable runnable = () -> {
				// 如果是多天，需要分别生成文件并更新更新文件路径
				String[] reportDates = reportDate.split(",");
				for (String date : reportDates) {
					try {
						JSONObject jsonObject = JSONObject.parseObject(actionParamsStr);
						Map<String, String> repMap = new HashMap<>();
						repMap.put("reportDate", date);
						repMap.put("isDesensitization", "true");
						if (StringUtils.isNotBlank(jsonObject.getString("hostCustNo"))) {
							repMap.put("hostCustNo", jsonObject.getString("hostCustNo"));
						}
						if (StringUtils.isNotBlank(jsonObject.getString("custNo"))) {
							repMap.put("custNo", jsonObject.getString("custNo"));
						}
						if (StringUtils.isNotBlank(jsonObject.getString("busiCode"))) {
							repMap.put("busiCode", jsonObject.getString("busiCode"));
						}
						if (StringUtils.isNotBlank(jsonObject.getString("prodCode"))) {
							repMap.put("prodCode", jsonObject.getString("prodCode"));
						}
						if (StringUtils.isNotBlank(jsonObject.getString("agentBankCode"))) {
							repMap.put("agentBankCode", jsonObject.getString("agentBankCode"));
						}
						if (StringUtils.isNotBlank(jsonObject.getString("agentBankName"))) {
							repMap.put("agentBankName", jsonObject.getString("agentBankName"));
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

						log.info("投资者明细信息下载{}文件生成成功，更新文件路径成功！", date);
					} catch (Exception e) {
						log.error("投资者明细信息下载{}文件生成失败，更新文件路径失败！", date, e);
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

	/**
	 * 中债三期脱敏规则如括号(对于三个汉字以内（包含三个汉字）的名称保留最后一个字，其余长度的名称不变形)
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	private TrCustTransInfo deSensitiveModel(TrCustTransInfo customer) throws Exception {
		String finalCustomerName = customer.getCustName();
		if(StringUtils.isNotBlank(finalCustomerName) && finalCustomerName.codePoints().count() <= 3) {
			customer.setCustName(getLastUnicodeChar(finalCustomerName));
		}
		return customer;
	}

    public static String getLastUnicodeChar(String str) {
        if (str == null || str.isEmpty()) return "";

        int endIndex = str.length();
        int lastCodePoint = str.codePointBefore(endIndex);
        return new String(Character.toChars(lastCodePoint));
    }

}
