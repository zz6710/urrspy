package com.kayak.rpt.email.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.context.EmailThreadPoolProperties;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.excel.action.ColumnWidthStyleStrategy;
import com.kayak.rpt.email.dao.EmailBizTaskDao;
import com.kayak.rpt.email.dao.EmailTemplateDao;
import com.kayak.rpt.email.dao.EmailTickRuleDao;
import com.kayak.rpt.email.model.AppProdBondPropertyInfoVo;
import com.kayak.rpt.email.model.EmailBizTask;
import com.kayak.rpt.email.model.EmailTemplate;
import com.kayak.rpt.email.model.EmailTickRule;
import com.kayak.rpt.email.util.EmailDict;
import com.kayak.rpt.email.util.EmailUtil;
import com.kayak.rpt.email.util.ExcelUtil;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import com.spire.ms.System.Collections.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.annotations.EvaluateAllEndpoints;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.zip.ZipOutputStream;

@Service
@APIDefine(desc = "业务任务发邮件表服务", model = EmailBizTask.class)
@Component
@Scope("prototype")
public class EmailBizTaskService {

    private static Logger logger = LogManager.getLogger(EmailBizTaskService.class);

    @Autowired
    private EmailBizTaskDao emailBizTaskDao;
    @Autowired
    private EmailTickRuleDao emailTickRuleDao;
    @Autowired
    private EmailTemplateDao emailTemplateDao;
    @Autowired
    EmailTickRuleService emailTickRuleService;
    @Autowired
    EmailSendService emailSendService;
    @Autowired
    private EmailThreadPoolProperties emailThreadPoolProperties;
//    @Value("${email.scheduled.localFilePath}")
//    private String rptEmailtempLocalFilePath;

    /**
     * 执行具体的业务流程
     * 根据业务任务记录，拿到业务表，执行业务取数的方法，生成文件
     *
     * @param emailBizTask
     */
    public void execute(EmailBizTask emailBizTask) throws Exception {
        //取数对应的业务类型
        String bizType = emailBizTask.getBizType();
        //取数对应的业务表
        String bizTable = emailBizTask.getBizTable();
        //取数对应的业务方法 预留，若需要重新sql取数
//        String bizTableMethod = emailBizTask.getBizTableMethod();
        //取数的业务日期  月报要取上月的数据
        String reportDate = emailBizTask.getBizDate();
        //获取对应邮件规则配置 若有多条取最近的一条  规则表的业务类型做唯一限定
        EmailTickRule reqParam = new EmailTickRule();
        reqParam.setBizTable(bizType);

        reqParam.setRuleStatus("1");
        EmailTemplate emailTemplate = null;
        EmailTickRule etrule = emailTickRuleDao.findEmailTickRuleSingle(reqParam);
        if (etrule == null || StringUtils.isEmpty(etrule.getTemplateId())) {
            logger.info("邮件规则信息不存在或未启用; bizType：" + bizType + "; reportDate:" + reportDate);
            //获取对应邮件模板配置 若有多条取最近的一条  模板表的业务类型做唯一限定
            emailTemplate = emailTemplateDao.findEmailTemplateByBizType(bizType);
        } else {
            emailTemplate = emailTemplateDao.findEmailTemplateById(etrule.getTemplateId());
        }

        if (emailTemplate == null || StringUtils.isEmpty(emailTemplate.getId())) {
            logger.error("未找到对应的邮件模板信息 不发邮件。 bizType：" + bizType + "; reportDate:" + reportDate);
            return;
        }
        //文件名  后续需调整设计  把本地文件和远程文件名区分开来，并做一一对应关系
        String fileName = getAttachFileName(bizType, reportDate);
        String tempPath = emailThreadPoolProperties.getRptEmailtempLocalFilePath();
        String tempPathFile = tempPath + fileName;
        //远程(S3)文件地址
        String remotePath = emailBizTask.getRemotePath();
        if (StringUtils.isEmpty(remotePath)) {
            //临时替代，不用配置
            remotePath = "urrs/email/";
        }
        //是否生成了附件文件
        boolean createFileFlag = false;
        //业务文件生成， TYPE_01为打标房产债券明细数据
        if (EmailDict.EmailBizType.TYPE_01.equals(bizType)) {
            //生成excel文件
            createFileFlag = generateBizFile(reportDate, bizType, tempPath, fileName);
            if (!createFileFlag) {
                logger.error("生成文件失败，不发邮件 bizType：" + bizType + ";reportDate:" + reportDate + ";fileName:" + fileName);
                return;
            }
        } else {
            //其他 暂无附件文件
        }

        if (createFileFlag) {
            //有附件的， 文件上传S3服务器  测试验证 文件路径  和 文件名
//			String str = emailTickRuleService.uploadFileToS3orObs("S3", reportDate, fileName, tempPathFile, remotePath);
            String remotePathFile = remotePath + fileName;
            try {
                FileTransfer transfer = FileTransferHelpler.getTransfer();
                //此处路径需带文件名  全路径
                transfer.uploadFileAndDisconnect(tempPathFile, remotePathFile);
            } catch (Exception e) {
//				e.printStackTrace();
                logger.error("上传 打标为房地产行业的债券明细数据 文件 失败; bizTable：" + bizTable + "; reportDate:" + reportDate + "; tempPathFile:" + tempPathFile + "; remotePathFile:" + remotePathFile + "; Exception:{}", e.getMessage());
            }
        }
        //发邮件 本地磁盘取文件，传附件
        // 后台参数处理-将infoMap中的参数替换到  邮件主题和正文中  主题和正文中的参数不可重复，重复会以正文参数为准。
        try {
            Map<String, String> emailMap = new HashMap<>();
            emailMap.put("templateId", emailTemplate.getId());
            emailMap.put("reportDate", reportDate);
            //文件路径 要含文件名
            emailMap.put("filePath", remotePath + fileName);
//            emailMap.put("subjectEx", fileName.substring(0,fileName.length()-5)); //移除文件后缀名".xlsx"
            emailMap.put("bizType", bizType);
            emailMap.put("localFilePath",tempPathFile);//temp路径带文件名
            //获取邮件模板页面配置参数及其值
            String dynamicParams = emailTemplate.getDynamicParams();
            Map<String, String> infoMap = EmailUtil.getTemplatePageParams(dynamicParams);
            String result = emailSendService.sendEmailByTemplateId(emailMap, infoMap, reportDate);
            logger.info("邮件 打标为房地产行业的债券明细数据 文件 发邮件结果result:" + result + "; bizTable：" + bizTable + "; reportDate:" + reportDate + "; tempPathFile:" + tempPathFile + "; remotePath:" + remotePath);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("邮件 打标为房地产行业的债券明细数据 文件 发邮件失败 邮件信息不全; bizTable：" + bizTable + "; reportDate:" + reportDate + "; tempPathFile:" + tempPathFile + "; remotePath:" + remotePath);
        }
        //删除本地文件 以及 任务记录
        try {
            //删除文件
            emailSendService.deleteLocalFile(tempPathFile);
            //删除任务表记录 避免重复执行
            emailBizTaskDao.deleteEmailBizTaskById(emailBizTask.getId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("邮件 打标为房地产行业的债券明细数据 删除本地文件失败 ; bizTable：" + bizTable + "; reportDate:" + reportDate + "; tempPathFile:" + tempPathFile + "; remotePath:" + remotePath);
        }

    }


    //从业务表取数 生成文件  房地厂打标
    public boolean generateBizFile(String reportDate, String bizType, String tempPath, String fileName) throws Exception {
        //执行任务 取数，生成本地EXCEL文件
        AppProdBondPropertyInfoVo params = new AppProdBondPropertyInfoVo();
        params.setReportDate(reportDate);
        //注意验证 查询返回参数，最终能否顺序写入excel
        List<AppProdBondPropertyInfoVo> datas = new ArrayList();
        try {
            datas = emailBizTaskDao.getAppProdBondPropertyInfoVoList(params);
            logger.info("获取 打标为房地产行业的债券明细数据 datas.size():" + datas.size() + "; reportDate:" + reportDate);
            if (datas == null || datas.size() == 0) {
                logger.info("无数据,不发邮件. 获取 打标为房地产行业的债券明细数据 汇总表为空;未获取到数据 bizType：" + bizType + "; reportDate:" + reportDate);
                return false;
            }
        } catch (Exception e) {
//				e.printStackTrace();
            logger.error("获取 打标为房地产行业的债券明细数据 失败;未获取到数据 bizType：" + bizType + "; reportDate:" + reportDate + " Exception:{}", e.getMessage());
        }

        File dir = new File(tempPath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdirs();
        }
        // 生成临时文件
//			String tempPathFile = tempPath + "/" + fileName;
        String tempPathFile = tempPath + fileName;
        File temExcelFile = new File(tempPathFile);

        if (!temExcelFile.exists()) {
            temExcelFile.createNewFile();
        }

        FileOutputStream out = new FileOutputStream(temExcelFile);
        ExcelWriterSheetBuilder excelBuilder = EasyExcel.write(out)
                .registerWriteHandler(new ColumnWidthStyleStrategy()).sheet("打标为房地产行业的债券明细数据");
//			String strAssetRegistColumns = "债券wind代码,债券持仓数量,成本,发行人,额度占用方,是否投向房地产行业,产品名称/委外专户名称,投资估值表比例,券面金额（万元）"; //文件头
        String strAssetRegistColumns = SysUtil.getSystemParamsByParaid(EmailDict.EmailBizTableFileName.APP_PROD_BOND_PROPERTY_INFO_CLOUNMS); //文件头
        List<List<String>> excelHeaders = new java.util.ArrayList<>();
        List<String> head = new java.util.ArrayList<>();
        List<String> headKeys = new java.util.ArrayList<>();
        String[] arrAppendCols = strAssetRegistColumns.split(",");
        for (String col : arrAppendCols) {
            head = new java.util.ArrayList<>();
            headKeys.add(col);
            head.add(col);
            excelHeaders.add(head);
        }
        // 添加头部信息
        excelBuilder.head(excelHeaders);
        int start = 0;
        //注意单个sheet的最大数据条数。 本次业务不涉及分文件下载。
        int end = datas.size();
        File tempFile;
        List<String> headKeyss = Arrays.asList("bondCode", "bondQnttXp", "bondQntt", "issrNm", "quotaOcc", "moneyofproperty", "prodName", "rate", "bondFace");
        List<String> headKeyExs = new java.util.ArrayList<>();
        headKeyExs.addAll(headKeyss);
        Map<String, Map<String, Object>> colMaps = new HashMap<>();
        List<List<String>> excelDatas = new java.util.ArrayList<List<String>>();
        for (int i = start; i < end; i++) {
            Object data = datas.get(i);
            List<String> _datas = new java.util.ArrayList<String>();
            String value = null;
            for (String perHeadKey : headKeyExs) {
                if (data instanceof SqlRow) {// SqlRow对象
                    SqlRow sqlRowData = (SqlRow) data;
                    value = sqlRowData.getString(perHeadKey);
                } else {// model对象
                    Object valueObj = getFieldValueByName(perHeadKey, data);
                    value = Tools.obj2Str(valueObj);
                }
                _datas.add(value);
            }
            excelDatas.add(_datas);
        }
        try {
            // 测试需要核对本地文件和 服务器上的文件路径， 目前临时文件名，是以随机数占用的。
            excelBuilder.doWrite(excelDatas);

//				tempFile = ExcelUtil.makeExcel(datas,excelHeaders, headKeys, colMaps, start, end, paras, tempPathFile);
            logger.info("生成 打标为房地产行业的债券明细数据文件; bizType：" + bizType + "; reportDate:" + reportDate + "; datas:" + datas.size());
        } catch (Exception e) {
//				e.printStackTrace();
            logger.error("生成 打标为房地产行业的债券明细数据文件失败; bizType：" + bizType + "; reportDate:" + reportDate + "; datas:" + datas.size() + " Exception:{}", e.getMessage());
            return false;
        }
        return true;
    }


    //获取文件名  本地-远程-附件名   自定义 或者 按系统提供的进行，系统自动取 bizType(模板的type,规则的bizType)作为文件名，拼接业务日期
    private String getAttachFileName(String bizType, String reportDate) throws Exception {
        String fileName = "";
        //本地临时文件目录  后续需调整设计  把本地文件和远程文件名区分开来，并做一一对应关系  调整模板和 任务记录表强关联（根据唯一业务id匹配）
        if (EmailDict.EmailBizType.TYPE_01.equals(bizType)) {
            fileName = new StringBuilder("截至").append(reportDate.substring(0, 4)).append("年")
                    .append(reportDate.substring(4, 6)).append("月末")
                    .append(SysUtil.getSystemParamsByParaid(EmailDict.EmailBizTableFileName.APP_PROD_BOND_PROPERTY_INFO))
                    .append(".xlsx").toString();
        } else {
            // 需要定义文件后缀名，生成多种文件 目前暂定excel文件
            fileName = bizType + "_" + reportDate + ".xlsx";
        }
        return fileName;
    }


    private String typeConvert(Map<String, Map<String, Object>> colMaps, String value, String headKey) {
        String type = (String) colMaps.get(headKey).get("type");
        if (type.equals("date")) {
            if (value.split("").length == 6) {
                value = value.substring(0, 4) + "-" + value.substring(4, 6);
            }
            if (value.split("").length == 8) {
                value = value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8);
            }
        }
        if (type.equals("time")) {
            if (value.split("").length == 6) {
                value = value.substring(0, 2) + ":" + value.substring(2, 4) + ":" + value.substring(4, 6);
            }
        }
        return value;
    }

    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    @API(desc = "查询业务任务发邮件表信息", auth = APIAuth.YES)
    public SqlResult<EmailBizTask> findEmailBizTasks(SqlParam<EmailBizTask> params) throws Exception {
        params.setMakeSql(true);
        return emailBizTaskDao.findEmailBizTasks(params);
    }

    @API(desc = "添加业务任务发邮件表", params = "id,biz_type,biz_name,biz_date,temp_path,remote_path,biz_table,biz_table_method,biz_status,task_flag,status,create_time,create_date", auth = APIAuth.NO)
    public int addEmailBizTask(SqlParam<EmailBizTask> params) throws Exception {
        return emailBizTaskDao.addEmailBizTask(params).getEffect();
    }

    @API(desc = "修改业务任务发邮件表", params = "id,biz_type,biz_name,biz_date,temp_path,remote_path,biz_table,biz_table_method,biz_status,task_flag,status,create_time,create_date", auth = APIAuth.NO)
    public int updateEmailBizTask(SqlParam<EmailBizTask> params) throws Exception {
        return emailBizTaskDao.updateEmailBizTask(params).getEffect();
    }

    @API(desc = "删除业务任务发邮件表记录", params = "id,biz_type,biz_name,biz_date,temp_path,remote_path,biz_table,biz_table_method,biz_status,task_flag,status,create_time,create_date", auth = APIAuth.NO)
    public int deleteEmailBizTask(SqlParam<EmailBizTask> params) throws Exception {
        return emailBizTaskDao.deleteEmailBizTask(params).getEffect();
    }

}
