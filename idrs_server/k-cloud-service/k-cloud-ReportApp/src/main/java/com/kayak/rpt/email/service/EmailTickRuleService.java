package com.kayak.rpt.email.service;

import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.email.dao.EmailTemplateDao;
import com.kayak.rpt.email.dao.EmailTickRuleDao;
import com.kayak.rpt.email.model.EmailTemplate;
import com.kayak.rpt.email.model.EmailTickRule;
import com.kayak.rpt.email.util.EmailDict;
import com.kayak.subject.service.JCConfigService;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.config.FileTransferConfig;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.HashMap;
import java.util.Map;

@Service
@APIDefine(desc = "邮件发送时间频次服务", model = EmailTickRule.class)
public class EmailTickRuleService {
    private static Logger logger = LogManager.getLogger(EmailTickRuleService.class);

    @Autowired
    EmailSendService emailSendService;
    @Autowired
    private EmailTickRuleDao emailTickRuleDao;
    @Autowired
    private EmailTemplateDao emailTemplateDao;


    @Autowired
    private JCConfigService jcConfigService;

    @API(desc = "查询邮件发送时间频次信息", auth = APIAuth.YES, params = "id,rule_name,biz_table,biz_name,template_id,template_name,rule_unit,rule_seprate,rule_tick,rule_status")
    public SqlResult<EmailTickRule> findEmailTickRules(SqlParam<EmailTickRule> params) throws Exception {
        params.setMakeSql(true);
        return emailTickRuleDao.findEmailTickRules(params);
    }

    @API(desc = "添加邮件发送时间频次", params = "id,rule_name,biz_table,biz_name,template_id,template_name,rule_unit,rule_seprate,rule_tick,rule_status", auth = APIAuth.YES)
    public String addEmailTickRule(SqlParam<EmailTickRule> params) throws Exception {
        if(StringUtils.isEmpty(params.getModel().getTemplateId())){
            return RequestSupport.updateReturnJson(false, "邮件模板编号不能为空！", null).toString();
        }
        EmailTemplate template = emailTemplateDao.findEmailTemplateById(params.getModel().getTemplateId());
        params.getModel().setTemplateName(template.getName());
        params.getModel().setBizTable(template.getType());
        params.getModel().setBizName(template.getTypeName());
        emailTickRuleDao.addEmailTickRule(params).toString();
        return  RequestSupport.updateReturnJson(true, "添加成功！", null).toString();
    }

    @API(desc = "修改邮件发送时间频次", params = "id,rule_name,biz_table,biz_name,template_id,template_name,rule_unit,rule_seprate,rule_tick,rule_status", auth = APIAuth.YES)
    public int updateEmailTickRule(SqlParam<EmailTickRule> params) throws Exception {
        //修改页面控制不让改模板，模板关联了业务表带入规则表。 可以新增或者删除记录。
        return emailTickRuleDao.updateEmailTickRule(params).getEffect();
    }
    @API(desc = "修改邮件发送时间频次状态为启用", params = "id,rule_name,rule_status", auth = APIAuth.YES)
    public int updateEmailTickRuleStatusOn(SqlParam<EmailTickRule> params) throws Exception {
        params.getModel().setRuleStatus(EmailDict.EffectStatus.ON_1);
        return emailTickRuleDao.updateEmailTickRule(params).getEffect();
    }
    @API(desc = "修改邮件发送时间频次状态为停用", params = "id,rule_name,rule_status", auth = APIAuth.YES)
    public int updateEmailTickRuleStatusOff(SqlParam<EmailTickRule> params) throws Exception {
        params.getModel().setRuleStatus(EmailDict.EffectStatus.OFF_0);
        return emailTickRuleDao.updateEmailTickRule(params).getEffect();
    }
    @API(desc = "删除邮件发送时间频次", params = "id,rule_name,biz_table,biz_name,template_id,template_name,rule_unit,rule_seprate,rule_tick,rule_status", auth = APIAuth.YES)
    public int deleteEmailTickRule(SqlParam<EmailTickRule> params) throws Exception {
        return emailTickRuleDao.deleteEmailTickRule(params).getEffect();
    }

    /**
     * 从规则限定表中，查询已有的邮件模板信息
     */
    @API(desc = "查询规则表中已有的邮件模板信息", params = "id,rule_name,biz_table,biz_name,template_id,template_name,rule_unit,rule_seprate,rule_tick,rule_status", auth = APIAuth.NO)
    public SqlResult<EmailTickRule> getEmailTemplateNames(SqlParam<EmailTickRule> params) throws Exception {
        params.setMakeSql(true);
        return emailTickRuleDao.findEmailTemplateInfo(params);
    }

    /**
     * 测试邮件发送流程   test方法不動，仅作测试
     *
     * @param paramss
     * @return
     */
    /*public String sendEmailTest(SqlParam<EmailTickRule> paramss) {
        logger.info("****************开始 测试邮件*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            Map<String, String> emailMap = new HashMap<>();
            //测试数据，数据库第一条模板数据
            emailMap.put("templateId", "0");
            emailMap.put("reportDate", "20250831");
            emailMap.put("receiver", "lc-yangr@spdbdev.com");
            //文件名,可按分号分割为多个
            emailMap.put("filePath", "D:\\tempFilePlace\\testemail\\test1.txt;D:\\tempFilePlace\\testemail\\test01.xlsx");

            //TODO 此处定义了 subjectTitle 和 content内容中，自定义的字段-需要页面配置，后台可以进行参数替换。测试暂不填
            Map<String, String> infoMap = new HashMap<>();
//            infoMap.put("noticeTitle","");
            infoMap.put("", "");
            emailSendService.sendEmailByTemplateId(emailMap, infoMap,reportDate);
//            if (CollectionUtil.isNotEmpty(list)) {
//                return RequestSupport.updateReturnJson(false,"测试邮件, 请重新输入！",null).toString();
//            }

            return RequestSupport.updateReturnJson(true, "测试邮件完成！", null).toString();
        } catch (Exception e) {
            logger.error("测试邮件异常: ", e);
            logger.info("****************测试邮件 处理结束*********************");
            return RequestSupport.updateReturnJson(false, e.getMessage(), null).toString();
        }
    }*/


    /**
     * 生成业务文件,并上传S3,返回S3文件名, 多个文件的以;分号分割
     * @return remoteFilePaths
     */
    public String generateBizFile(){

        //根据业务生成文件  或多个文件

        //上传文件到S3

        //拼接远程文件路径,含文件名

        //删除本地文件  localFilePaths 多个以逗号分割
        delLocalBizFile("");

        String str= new String();

        //返回拼接后的远程附件路径  remoteFilePaths
        return str = "";
    }


    /**
     * 从S3上下载,生成本地业务文件,返回本地文件名-含路径, 多个文件的以;分号分割
     * @return localFilePaths
     */
    public String downLoadBizFile(){

        //根据业务的远程路径  下载一个或多个文件到本地

        //拼接本地文件名-含路径,多个以逗号分割


        String str= new String();

        //返回拼接后的本地附件路径
        return str = "";
    }

    /**
     * 删除本地文件 可多个(循环)
     * @param filePaths 多个以逗号分割
     * @return filePath
     */
    public void delLocalBizFile(String filePaths){

        //循环删除文件

    }














    /**
     * 上传文件到S3测试  TODO 仅做测试验证
     *
     * @param paramss
     * @return
     */
    public String uploadS3FileTest(SqlParam<EmailTickRule> paramss) {
        // 根据存储系统判断获取配置
        String send = "S3";
        String value = StringUtils.equals("S3", send) ? "" : "_" + send;
        try {
            Map<String, Object> configMap = jcConfigService.getConfigInfo(EmailDict.ConfigContentType.S3_COMMON);
            String fileName = "test1.dat";
            String taskDate = "20250908";

//            FileTransferConfig config = new FileTransferConfig();
//            config.setProtocol(send);
//            config.setAmazonEndpointUrl((String) configMap.get("ENDPOINT_URL" + value));
//            config.setAmazonAwsAccessKey((String) configMap.get("ACCESS_KEY" + value));
//            config.setAmazonAwsSecretKey((String) configMap.get("SECRET_KEY" + value));
//            config.setAmazonAwsBucketName((String) configMap.get("BUCKET_NAME" + value));
//            logger.info("****************测试S3上传 FileTransferConfig:" + JSONObject.toJSONString(config));

            FileTransfer transfer = new FileTransferHelpler().getTransfer();
            String localPath = (String) configMap.get("LOCAL_PATH" + value);
            String remotePath = (String) configMap.get("REMOTE_PATH"+ value);
            String localFile = localPath+fileName;
            String remoteFile;
            if (!remotePath.endsWith("\\") && !remotePath.endsWith("/")) {
                remoteFile = remotePath + "/" + fileName;
            } else {
                remoteFile = remotePath + fileName;
            }
            remoteFile = remoteFile.replace("[deal_date]", taskDate);
            logger.info("****************测试S3上传 remoteFile:" + remoteFile + "**********localFile:" + localFile + "*******fileName:" + fileName + "****");
            transfer.uploadFileAndDisconnect(localFile,remoteFile);
            return RequestSupport.updateReturnJson(true, "测试S3上传文件完成！", null).toString();
        } catch (Exception e) {
            logger.error("测试S3上传文件异常: ", e);
            logger.info("****************测试S3上传 处理结束*********************");
            return RequestSupport.updateReturnJson(false, e.getMessage(), null).toString();
        }
    }

    /**
     *  单文件 上传至S3或者华为OBS存储
     *
     * @param sendType 传输服务器类型  S3 或 OBS
     * @param taskDate 不能 仅标注任务执行日期，如果可以，需要关联到 业务日期。
     * @param fileName  本地文件名
     * @param localFilePath 本地路径
     * @param remoteFilePath 远程路径
     * @return
     */
	public String uploadFileToS3orObs(String sendType, String taskDate,  String fileName, String localFilePath, String remoteFilePath)  {
//    public String uploadFileToS3orObs(SqlParam<EmailTickRule> paramss) {
        try {
            FileTransfer transfer = new FileTransferHelpler().getTransfer();
            String localFile = localFilePath+fileName;
            String remoteFile;
            if (!remoteFilePath.endsWith("\\") && !remoteFilePath.endsWith("/")) {
                remoteFile = remoteFilePath + "/" + fileName;
            } else {
                remoteFile = remoteFilePath + fileName;
            }
            //如果有 dealDate， 存在没有日期的情况吗？
            remoteFile = remoteFile.replace("[deal_date]", taskDate);
            logger.info("****************"+sendType+"上传 remoteFile:" + remoteFile + "**********localFile:" + localFile + "*******fileName:" + fileName + "****");
            transfer.uploadFileAndDisconnect(localFile,remoteFile);
            return RequestSupport.updateReturnJson(true, sendType+"上传文件完成！",null).toString();
        } catch (Exception e) {
            logger.error(sendType+"上传文件异常: ", e);
            logger.info("****************"+sendType+"上传 处理结束*********************");
            return RequestSupport.updateReturnJson(false, e.getMessage(), null).toString();
        }
    }


    /**
     * 下载文件到本地测试  发附件前   TODO 需获取对应的服务地址和链接密钥  验证方法
     @param sendType 传输服务器类型  S3 或 OBS
     @param taskDate 不能 仅标注任务执行日期，如果可以，需要关联到 业务日期。
     @param fileName  本地文件名
     @param localFilePath 本地路径
     @param remoteFilePath 远程路径
     * @return
     */
//	public String  downLoadFileFromS3orObs(Map<String, Object> configMap, String send, String taskDate, String localFile, String fileName) {
    public String downLoadFileFromS3orObs(String sendType, String taskDate,  String fileName, String localFilePath, String remoteFilePath) {
        logger.info("****************开始 文件从"+sendType+"下载 *********************");
        try {
            //TODO 临时赋值验证方法
            FileTransfer transfer = new FileTransferHelpler().getTransfer();
            String localFile = localFilePath+fileName;
            String remoteFile;
            if (!remoteFilePath.endsWith("\\") && !remoteFilePath.endsWith("/")) {
                remoteFile = remoteFilePath + "/" + fileName;
            } else {
            remoteFilePath = remoteFilePath + fileName;
            }
            remoteFile = remoteFilePath.replace("[deal_date]", taskDate);
            logger.info("****************"+sendType+"下载 remoteFile:" + remoteFile + "**********localFile:" + localFile + "***********");
            transfer.downloadFileAndDisconnect(remoteFile, localFile);

            return RequestSupport.updateReturnJson(true, sendType+"下载文件完成！", null).toString();
        } catch (Exception e) {
            logger.error(sendType+"载文件异常: ", e);
            logger.info("****************"+sendType+"下载 处理结束*********************");
            return RequestSupport.updateReturnJson(false, e.getMessage(), null).toString();
        }

    }

}