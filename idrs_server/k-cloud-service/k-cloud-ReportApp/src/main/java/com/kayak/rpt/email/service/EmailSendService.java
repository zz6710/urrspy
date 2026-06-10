package com.kayak.rpt.email.service;

import com.alibaba.fastjson.JSONObject;
import com.kayak.context.EmailThreadPoolProperties;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.email.dao.EmailAccountDao;
import com.kayak.rpt.email.dao.EmailTemplateDao;
import com.kayak.rpt.email.model.*;
import com.kayak.rpt.email.dao.EmailLogDao;
import com.kayak.rpt.email.util.EmailDict;
import com.kayak.rpt.email.util.EmailUtil;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

@Service
public class EmailSendService {
    private static Logger logger = LoggerFactory.getLogger(EmailSendService.class);
    @Resource
    private EmailTemplateDao emailTemplateDao;
    @Resource
    private EmailLogDao emailLogDao;
    @Resource
    private EmailAccountDao emailAccountDao;
    @Autowired
    EmailThreadPoolProperties emailThreadPoolProperties;
    private static final String CONFIG_NAME_EMAIL_SENDER_NAME = "mail_sender_name";

    /**
     * 发送邮件，需要拼参数 ;  且有附件的,需要进行文件上传到S3,
     *
     * @param infoMap　需要替换的参数，页面传入/或者说  需要调用方确认后传入(替换subject和content中的内容)
     * @param emailMap
     * @param reportDate 业务发生日期yyyyMMdd
     */
    public String sendEmailByTemplateId(Map<String, String> emailMap, Map<String, String> infoMap,String reportDate) throws Exception {
        String templateId = emailMap.get("templateId");
        EmailTemplate emailTemplate = emailTemplateDao.findEmailTemplateById(templateId);
        if (Objects.nonNull(emailTemplate)) {
            String result = "";
            String subject = EmailUtil.buildContent(emailTemplate.getSubject(), infoMap,reportDate);
            logger.info("邮件发送,新增邮件日志记录 subject:"+subject+"; infoMap:"+infoMap.toString());
            String content = EmailUtil.buildContent(emailTemplate.getContent(), infoMap,reportDate);
            emailMap.put("subject", subject);
            /*if(EmailDict.EmailBizType.TYPE_01.equals(String.valueOf(emailMap.get("bizType")))){
                //特殊定制主题
                emailMap.put("subject", emailMap.get("subjectEx"));
            }*/
            emailMap.put("content", content);
            emailMap.put("reportDate", reportDate);
            if(EmailDict.EffectStatus.ON_1.equals(emailTemplate.getStatus())){
                emailMap.put("receiver", emailTemplate.getReceiver());
                emailMap.put("cc", emailTemplate.getCc());


                //下载S3文件到本地（发邮件前 附件文件必须在本地） 获取S3文件 下载到本地    文件刚生成时，无需下载。
                //emailMap.put("localFilePath", "");
                //此处路径需带文件名
               /* String filePath = emailMap.get("filePath");
                if(StringUtils.isEmpty(filePath)){
                    //
                }else{
                    String[] remoteFilePath = filePath.split(EmailDict.SymbolType.T_SEMICOLON);
                    StringBuilder localFilePaths = new StringBuilder();
                    for (String remoteFile : remoteFilePath) {
                        if(StringUtils.isNotEmpty(remoteFile)){
                            //下载S3文件
                            String localFile = EmailUtil.downloadS3File(remoteFile);
                            if(StringUtils.isNotEmpty(localFile)){
                                localFilePaths.append(localFile) ;
                                localFilePaths.append(EmailDict.SymbolType.T_SEMICOLON);
                            }
                        }
                    }
                    localFilePath = localFilePaths.toString();
                }*/

                result = EmailUtil.sendEmail(emailMap);
                if (EmailUtil.SUCCESS.equals(result)) {
                    emailMap.put("sendStatus", "1");
                } else {
                    emailMap.put("sendStatus", "0");
                }
            }else{
                result = "模板未启用，邮件发送失败！";
                emailMap.put("sendStatus", "0");
                emailMap.put("failReason", "模板未启用，邮件发送失败！");
                //根据配置获取 当前用户信息
                emailMap.put("sender", SysUtil.getSystemParamsByParaid(EmailDict.EmailHostParams.SEND_USER));
            }
            int recordCount = addEmailLog(emailMap);
            logger.info("邮件发送,新增邮件日志记录 count:"+recordCount);
            if (recordCount <= 0) {
                logger.error("新增 邮件日志记录失败，详情:{}",emailMap);
            }
            return result;
        } else {
            return "邮件发送失败！未获取到模板信息";
        }
    }

    /**
     * 重发邮件   TODO 如果重新发送失败 或者 成功  页面都需要提示失败或成功 , 所有日志记录都需要支持重新发送-日志记录中需要记录对应的规则id
     * 需要是已经发过文件的，S3有文件生成的，先从S3下载文件到本地，再从本地发送邮件添加附件
     */
    public void reSendEmail(SqlParam<EmailLog> params) throws Exception {
        if (params == null) {
            throw new RuntimeException("邮件重发失败，未获取到原始邮件信息！");
        }
        String id = params.getModel().getId();
        if (StringUtils.isEmpty(id)) {
            throw new RuntimeException("邮件重发失败，原始邮件信息不存在！");
        }
        //判断上次发送状态,成功的,重发走日志重发; 失败的,重发走模板重发.
        EmailLog emailLog = emailLogDao.findEmailLogById(id);
        String sendStatus = emailLog.getSendStatus();
        if (EmailDict.SendStatus.SUCCESS_00.equals(sendStatus)) {
            reSendEmailByLogId(emailLog);
        } else if(EmailDict.SendStatus.FAIL_02.equals(sendStatus)){
            //TODO 失败邮件, 暂不支持，需要拿到本地文件路径,模板Id   相当于 业务-日志-模板 关联     日志表需要添加 模板Id字段
            logger.info("失败邮件，重发，先校验信息完整性");
            String remoteFilePaths = "";
            String templateId = "";
            reSendEmailByRuleTickId(emailLog,remoteFilePaths,templateId);
        }
    }


    /**
     * 根据日志记录Id 重发邮件  所有信息都从日志记录中获取  [文件附件已生成]
     * 外部情况: 若模板修改- 新增 修改  删除  联系人    重发无法覆盖   失败的邮件无法重新发送[可根据记录状态判断,失败的走模板重发方法]
     *
     * @param emailLog
     * @return
     */
    public void reSendEmailByLogId(EmailLog emailLog) throws Exception {
//		Map<String, String> infoMap = new HashMap<>();
//		infoMap.put("", "");
        Map<String, String> emailMap = new HashMap<>();
        emailMap.put("subject", emailLog.getEmailSubject());
        emailMap.put("content", emailLog.getEmailBody());
        emailMap.put("receiver", emailLog.getReceiver());
        emailMap.put("cc", emailLog.getCc());
        emailMap.put("filePath", emailLog.getFilePath());
        //发邮件  todo 需将远程文件下载到本地。
        emailMap.put("localFilePath","");
        String result = EmailUtil.sendEmail(emailMap);
        if (EmailUtil.SUCCESS.equals(result)) {
            emailLog.setSendStatus("1");
        } else {
            emailLog.setSendStatus("0");
        }
        emailLog.setId("");
        int addCount = emailLogDao.addEmailLogByModel(emailLog).getEffect();
        logger.info("根据邮件日志 重发邮件,addCount:" + addCount + "; SendStatus:" + result + "; preEmailLogId:" + emailLog.getId());
    }

    /**
     * 根据日志记录Id+规则Id 完全重发邮件  默认日志不可靠,完全走规则-模板重发 [需重新下载文件]
     * 失败的邮件无法重新发送 [可根据记录状态判断,失败的走规则-关联业务文件的方法,重新生成文件，重新取规则-模板信息]
     *
     * @param emailLog
     * @return
     */
    public void reSendEmailByRuleTickId(EmailLog emailLog,String localFilePaths,String templateId) throws Exception {
        Map<String,String> emailMap = new HashMap<>();
//        emailMap.put("sender","");
//        emailMap.put("senderSecret","");
//        emailMap.put("filePath",remoteFilePaths);
        emailMap.put("templateId",templateId);
        emailMap.put("reportDate",emailLog.getReportDate());
        //根据配置获取 当前用户信息
        emailMap.put("emailAcctnoName", SysUtil.getSystemParamsByParaid(EmailDict.EmailHostParams.SEND_USER_NAME));
//        emailMap.put("emailType", EmailDict.EmailType.NORMAL_M1);
        //需要替换的模板中的参数  主要是为了复用邮件正文   一般是 替换业务参数 及 包含的人员信息
        Map<String, String> infoMap = new HashMap<>();
        //邮件主题
        infoMap.putAll(emailMap);
        sendEmailByTemplateId(emailMap,infoMap,emailLog.getReportDate());
    }

    /**
     * 发送邮件，字段处理
     *
     * @param emailMap
     * @return
     * @throws Exception
     */
    /*public String sendEmail(Map<String, String> emailMap){
        logger.info("邮件发送参数：{}", emailMap.toString());
        //发件人邮箱账号
        String sender = "";
        //发件人邮箱密码
        String sendSecret = "";
        //邮箱服务器地址
        String myEmailSMTPHost = "";
        //邮箱服务器端口
        String smtpPort = "";
        //发件人账号中文名称
        String emailAccountName = "";
        //收件人邮箱
        String receiver = "";
        //抄送地址
        String cc = "";
        //邮件主题
        String subject = "";
        //文本内容
        String content = "";
        //附件路径
        String[] filePath = {};
        //修改接口传入  参数key值包含(emailAccountName,receiveMailAccount,cc,subject,content,filePath)
        try {
            // 根据系统参数配置获取 当前发件ip信息
            myEmailSMTPHost = SysUtil.getSystemParamsByParaid(EmailDict.EmailHostParams.SEND_HOST);
            // TODO 根据配置获取 当前发件port信息 commonParamUtils.getSysParamConfig("smtp_port")
            smtpPort = SysUtil.getSystemParamsByParaid(EmailDict.EmailHostParams.SEND_PORT);
            //emailMap中有配置的发件人和密码则用emailMap配置的
            if(StringUtils.isNotEmpty(emailMap.get("sender")) && StringUtils.isNotEmpty(emailMap.get("sendSecret"))){
                sender = emailMap.get("sender");
                sendSecret = emailMap.get("sendSecret");
            }else{
                //  根据配置获取 当前发件mail_user信息
                sender = SysUtil.getSystemParamsByParaid(EmailDict.EmailHostParams.SEND_USER);
                //  根据配置获取 当前发件mail_password信息
                sendSecret = SysUtil.getSystemParamsByParaid(EmailDict.EmailHostParams.SEND_SECRET);
            }
            emailAccountName = emailMap.get("emailAccountName");
            receiver = emailMap.get("receiver");
            emailMap.put("sender", sender);
            cc = emailMap.get("cc");
            subject = emailMap.get("subject");
            content = emailMap.get("content");
            String filePathStr = "";
            //TODO 确认是否本地文件地址
            filePathStr = emailMap.get("filePath");
            if (StringUtils.isNotEmpty(filePathStr)) {
                filePath = emailMap.get("filePath").split(";");
            } else {
                filePath = new String[]{};
            }
            logger.info("邮件host--->{},prot----->{},收件人{}", myEmailSMTPHost, smtpPort, receiver,filePath);
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.host", myEmailSMTPHost);
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.port", smtpPort);
            props.setProperty("mail.mime.splitlongparameters", "false");
            System.setProperty("mail.mime.splitlongparameters","false");
            Session session = Session.getDefaultInstance(props);
            Address[] tos = createTos(receiver);
            if (Objects.isNull(tos)){
                throw new Exception("收件人为空！");
            }
            Address[] toc = createTos(cc);
            MimeMessage message = createMimeMessage(session, sender, tos, toc, emailAccountName, subject, content, filePath);
            Transport transport = session.getTransport();
            try {
                transport.connect(sender, sendSecret);
                transport.sendMessage(message, message.getAllRecipients());
                message.saveChanges();
                emailMap.put("sendStatus", EmailDict.EffectStatus.ON_1);
                return "SUCCESS";
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                emailMap.put("failReason", e.getMessage());
                emailMap.put("sendStatus", EmailDict.EffectStatus.OFF_0);
                return e.getMessage();
            } finally {
                transport.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            emailMap.put("failReason", e.getMessage());
            emailMap.put("sendStatus", EmailDict.EffectStatus.OFF_0);
            return e.getMessage();
        }
    }*/

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session  和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param tos      收件人邮箱
     * @param toc      抄送人
     * @param emailAcctnoName 发件账户名
     * @param subject 主题
     * @param content 正文内容
     * @param filePath 本地文件路径
     * @return
     * @throws Exception
     */
   /* public static MimeMessage createMimeMessage(Session session, String sendMail, Address[] tos, Address[] toc, String emailAcctnoName, String subject, String content, String[] filePath) throws Exception {
        // 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        try {
            // MimeMessageHelper helper = buildHelper(tos, "", "", message, cc);
            //  From: 发件人  (XX理财有限公司)
            message.setFrom(new InternetAddress(sendMail, emailAcctnoName, CHARSET_DEFAULT));
            //  To: 收件人
            message.setRecipients(MimeMessage.RecipientType.TO, tos);
            // To: 抄送人
            message.setRecipients(MimeMessage.RecipientType.CC, toc);
            // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
            message.setSubject(subject, CHARSET_DEFAULT);
            //附件
            MimeMultipart msgMultipart = new MimeMultipart("mixed");
            MimeBodyPart messageBodyPart = null;
            //文本内容 TODO 确认是否支持Excel
            if (!content.equals("")) {
                messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(content, "text/html;charset=" + CHARSET_DEFAULT); //内容
                msgMultipart.addBodyPart(messageBodyPart);
            }
            for (int i = 0; i < filePath.length; i++) {
                if (!filePath[i].isEmpty()) {
                    messageBodyPart = new MimeBodyPart();
                    FileDataSource fileDataSource = new FileDataSource(filePath[i]);
                    messageBodyPart.setDataHandler(new DataHandler(fileDataSource));
                    String fileName = filePath[i].substring(filePath[i].lastIndexOf(File.separator) + 1);
                    //String fileName = MimeUtility.encodeText(filePath[i].substring(filePath[i].lastIndexOf(File.separator) + 1), "GBK", "B");
                    BASE64Encoder encoder = new BASE64Encoder();
                    String fileNameEncode = "=?GBK?B?" + encoder.encode(fileName.getBytes("GBK")) + "?=";
                    logger.info("fileNameEncode:"+fileNameEncode);
                    messageBodyPart.setFileName(fileNameEncode);
                    msgMultipart.addBodyPart(messageBodyPart);
                }
            }
            if (null != messageBodyPart) {
                message.setContent(msgMultipart,"text/html;charset=" + CHARSET_DEFAULT);
            }
            message.setSentDate(new Date());
            message.saveChanges();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception("邮件创建失败");
        }
        return message;
    }*/

    /**
     * 创建接收人地址集合
     */
   /* private Address[] createTos(String receiver) throws AddressException {
        Address[] tos = null;
        if (!StringUtils.isEmpty(receiver)) {
            String[] toAarr = receiver.split(";");
            if (toAarr.length > 0) {
                tos = new InternetAddress[toAarr.length];
                for (int i = 0; i < toAarr.length; i++) {
                    tos[i] = new InternetAddress(toAarr[i]);
                }
            }
        }
        return tos;
    }*/

    /**
     * 替换模板中的内容
     *
     * @param content
     * @param replaceMap
     * @return
     */
   /* private String buildContent(String content, Map<String, String> replaceMap) {
        StringBuffer buffer = new StringBuffer(content);
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            String realKey = "#{" + entry.getKey() + "}";
            int index = buffer.indexOf(realKey);
            while (index != -1) {
                buffer.replace(index, index + realKey.length(), String.valueOf(entry.getValue()));
                index = buffer.indexOf(realKey, index + String.valueOf(entry.getValue()).length());
            }
        }
        return buffer.toString();
    }*/

    /**
     * 邮件日志记录
     *
     * @param emailMap
     */
    public int addEmailLog(Map<String, String> emailMap) throws Exception {
        EmailLog emailLog = new EmailLog();
        String logNo = DateUtil.getTimestamp17();
        emailLog.setEmailLogNo(logNo);
        emailLog.setBusinessNo(emailMap.get("businessNo"));
        emailLog.setBusinessType(emailMap.get("businessType"));
        emailLog.setReportDate(emailMap.get("reportDate"));
        // 确认此处入库的日志记录，主题和内容是否需要替换字段，是否影响后续的邮件重发。
        emailLog.setEmailSubject(emailMap.get("subject"));
        emailLog.setEmailBody(emailMap.get("content"));
        emailLog.setReceiver(emailMap.get("receiver"));
        emailLog.setCc(emailMap.get("cc"));
        emailLog.setSender(emailMap.get("sender"));
        emailLog.setAttachName(emailMap.get("attachName"));
        //S3远程文件路径，多个以分号分割
        emailLog.setFilePath(emailMap.get("filePath"));
        emailLog.setFailReason(emailMap.get("failReason"));
        emailLog.setSendStatus(emailMap.get("sendStatus"));
        emailLog.setSendTime(DateUtil.getNowTime());
        emailLog.setSendDate(DateUtil.getNowDate());
        Map<String,Object> emailLogMap = JSONObject.parseObject(JSONObject.toJSONString(emailLog),HashMap.class);
        return emailLogDao.addEmailLogByMapParam(emailLogMap).getEffect();
    }

    /**
     * 单条邮件记录重发
     * @param params 前端传递的参数
     * @return 重发结果
     */
    public String reSendEmail(Map<String,Object> params){
        if(params == null || params.isEmpty()){
            return RequestSupport.updateReturnJson(false, "邮件重发失败，未获取到原始邮件信息！", null).toString();
        }
        String id = params.getOrDefault("id","").toString();
        if(StringUtils.isEmpty(id)){
            //和前端响应信息的交互
//          throw new RuntimeException("邮件重发失败，未获取到原始邮件信息！");
            return RequestSupport.updateReturnJson(false, "邮件重发失败，未获取到原始邮件信息！", null).toString();
        }
        String result = "";
        try{
            //根据日志记录id重发邮件 仅成功生成文件的
            result = reSendEmailById(id);
            return RequestSupport.updateReturnJson(true, result, null).toString();
        }catch (Exception e){
            return RequestSupport.updateReturnJson(false, e.getMessage(), null).toString();
        }
    }

    /**
     * 批量邮件重发
     * @param params 前端传递的参数
     * @return 发送结果
     */
    public String reSendEmailBatch(Map<String,Object> params) throws Exception{
        if(params == null || params.isEmpty()){
            throw new RuntimeException("邮件重发失败，未获取到原始邮件信息！");
        }
        List<Map<String,Object>> list = (List<Map<String,Object>>) params.get("selectedItem");
        if(list == null || list.size() <= 0){
            return "无需要重新发送的邮件！";
        }
        StringBuilder ids = new StringBuilder("");
        for (Map<String,Object> tempMap: list) {
            ids.append((String) tempMap.get("id"));
            ids.append(EmailDict.SymbolType.T_COMMA);
        }
        return reSendEmailById(ids.toString());
    }

    /**
     * 邮件重发功能主入口
     * 根据ID查询原邮件日志数据
     * 根据原邮件日志数据组装新邮件并发送。
     * @param ids 原邮件ID（有多个ID时，拼接成字符串）
     * @return 发送结果
     * @throws Exception 发送异常，S3文件下载、
     */
    private String reSendEmailById(String ids) throws Exception {
        if(StringUtils.isEmpty(ids)){
            throw new RuntimeException("邮件重发失败，未获取到原始邮件信息！");
        }
        List<EmailLog> emailLogs = emailLogDao.findEmailLogByIds(ids);
        if(emailLogs == null || emailLogs.isEmpty()){
            return "邮件重发失败，未获取到原始邮件信息！";
        }
        int count = 0;
        int successCount = 0;
        for (EmailLog entity : emailLogs) {
            if(checkOldEmail(entity)){
                EmailLog newEmail = getNewLog(entity);
                //获取参数，并下载S3文件(内部有操作)
                Map<String,String> params = getEmailParamsByEntity(newEmail);
                if(count++ > 15){
                    Thread.sleep(60000);
                    count = 0;
                }
                if (EmailUtil.SUCCESS.equals(EmailUtil.sendEmail(params))) {
                    newEmail.setSendStatus(EmailDict.EffectStatus.ON_1);
                } else {
                    newEmail.setSendStatus(EmailDict.EffectStatus.OFF_0);
                }
//                newEmail.setSender(params.getOrDefault("sender",""));
                newEmail.setFailReason(params.getOrDefault("failReason",""));
                //项目中很少看到JSONObject序列化的  全局来看此处无安全隐患
                Map<String,Object> emailLogMap = JSONObject.parseObject(JSONObject.toJSONString(newEmail),HashMap.class);
                emailLogDao.addEmailLogByMapParam(emailLogMap);
                //确认邮件发送完毕后 需要删除本地文件    TODO 验证先核对然后再删除
                deleteLocalFile(params.getOrDefault("localFilePath",""));
                successCount++;
            }
        }
        return "共" + emailLogs.size() + "封邮件需要重发，" + successCount + "封邮件重发成功！";
    }

    /**
     * 邮件发送完毕后删除本地文件  多个拼接
     * @param localFilePath  本地文件路径字符串
     */
    public void deleteLocalFile(String localFilePath) throws Exception {
        if(StringUtils.isEmpty(localFilePath)){
            logger.info("删除本地文件 本地文件不存在");
            return;
        }
        String[] localFileArray = localFilePath.split(EmailDict.SymbolType.T_SEMICOLON);
        try{
            for (String localFile : localFileArray) {
                if(StringUtils.isNotEmpty(localFile)){
                    //删除本地文件
                    File temExcelFile = new File(localFile);
                    if(temExcelFile.exists()){
                        temExcelFile.delete();
                    }
                    //删除S3远程文件
//                    FileTransfer transfer = FileTransferHelpler.getTransfer();
//                    transfer.deleteFileAndDisconnect(localFile);
                }
            }
        }catch (Exception e){
             logger.error("删除本地文件 失败 ;localFilePath:"+localFilePath+";Exception:{}",e.getMessage());
        }

    }

    /**
     * 校验旧邮件数据中是否存在异常字段，比如收件人为空或流水号为空等
     * @param entity 旧邮件数据
     * @return 校验结果，false表示该旧数据不能重发邮件
     */
    private boolean checkOldEmail(EmailLog entity){
        if(entity == null){
            return false;
        }
        if(StringUtils.isEmpty(entity.getId())){
            return false;
        }
        if(StringUtils.isEmpty(entity.getEmailLogNo())){
            return false;
        }
        //dis_email_resend_check_sender
        // 判断是否需要判断发件人，
        //  若需要判断，则只能与当前配置发件人一致的邮件可重发，
        //  若不需要判断，则所有邮件均可按当前配置账户重新发送
//        if(EmailDict.EffectStatus.ON_1.equals("email_resend_check_sender")){
//            if(StringUtils.isEmpty(entity.getSender())
//                    || !entity.getSender().equals("mail_user")){
//                //因为有些系统要求邮件发送需做隔离，即指定业务场景只能使用对应的邮箱账户，因此原发送方为空，或与系统发送人不一致时不能重发
//                return false;
//            }
//        }
        return StringUtils.isNotEmpty(entity.getReceiver());
    }

    /**
     * 根据原邮件数据获取新的邮件对象，就邮件流水号作为新数据的业务流水号
     * @param entity 旧邮件数据
     * @return 新邮件对象
     */
    private EmailLog getNewLog(EmailLog entity){
        EmailLog newEntity = new EmailLog();
        //UUID需确认是否有分布式重复风险 数据库主键是否取分布式id或者采用自增要好一点
//        newEntity.setId(Tools.getStringRandom(32));
        newEntity.setEmailLogNo(DateUtil.getTimestamp17());
        newEntity.setBusinessNo(entity.getEmailLogNo());
        newEntity.setBusinessType(entity.getBusinessType());
        newEntity.setSender(entity.getSender());
        newEntity.setReceiver(entity.getReceiver());
        newEntity.setCc(entity.getCc());
        newEntity.setEmailSubject(entity.getEmailSubject());
        newEntity.setEmailBody(entity.getEmailBody());
        newEntity.setAttachName(entity.getAttachName());
        newEntity.setFilePath(entity.getFilePath());
//        newEntity.setSendStatus("");
//        newEntity.setFailReason("");
        newEntity.setSendDate(DateUtil.getNowDate());
        newEntity.setSendTime(DateUtil.getNowTime());
//        // 确认用户信息
//        newEntity.setSendUserId(SysUtil.getUserInfo().get("userId").toString());
//        newEntity.setSendUserName("");
//        newEntity.setEffectFlag(EmailDict.DataStatus.USE);
//        // 确认用户信息
//        newEntity.setCreateUser(SysUtil.getUserInfo().get("userId").toString());
//        newEntity.setUpdateUser("");
//        //yyyyMMdd
//        String createDate = DateUtil.getNowDate();
//        newEntity.setCreateDate(createDate);
//        newEntity.setUpdateDate(createDate);
//        //HHMMSS
//        String createTime = DateUtil.getNowTime();
//        newEntity.setCreateTime(createTime);
//        newEntity.setUpdateTime(createTime);
        return newEntity;
    }

    /**
     * 组装邮件发送参数
     * @param entity 邮件日志数据对象
     * @return 邮件发送参数MAP
     */
    private Map<String,String> getEmailParamsByEntity(EmailLog entity) throws Exception{
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("emailAcctnoName",SysUtil.getSystemParamsByParaid(EmailDict.EmailHostParams.SEND_USER_NAME));
        paramsMap.put("receiveMailAccount",entity.getReceiver());
        paramsMap.put("cc",entity.getCc());
        paramsMap.put("subject",entity.getEmailSubject());
        paramsMap.put("content",entity.getEmailBody());
        //此处远程S3文件路径       而本地临时路径在配置中，不入库
        String filePath = entity.getFilePath();
        if(StringUtils.isEmpty(filePath)){
            paramsMap.put("filePath","");
            paramsMap.put("localFilePath","");
        }else{
            String[] remoteFilePath = filePath.split(EmailDict.SymbolType.T_SEMICOLON);
            StringBuilder localFilePath = new StringBuilder();
            for (String remoteFile : remoteFilePath) {
                if(StringUtils.isNotEmpty(remoteFile)){
                    //下载S3文件
                    String tempFilePath = emailThreadPoolProperties.getRptEmailtempLocalFilePath();//temp不带文件名
                    String localFile = EmailUtil.downloadS3File(remoteFile,tempFilePath);//localFile带文件名
                    if(StringUtils.isNotEmpty(localFile)){
                        localFilePath.append(localFile) ;
                        localFilePath.append(EmailDict.SymbolType.T_SEMICOLON);
                    }
                }
            }
            paramsMap.put("localFilePath",localFilePath.toString());
        }

        return paramsMap;
    }

    /**
     * 根据文件路径（含文件名）  获取文件名称
     * @param FilePath   本地文件路径 或者 S3上文件路径
     * @return 文件名称
     */
    private String getFileNameByPath(String FilePath){
        if(FilePath.contains(EmailDict.SymbolType.T_D_SEPARATOR)){
           return FilePath.substring(FilePath.lastIndexOf(EmailDict.SymbolType.T_D_SEPARATOR) + 1);
        }
        return FilePath.substring(FilePath.lastIndexOf(EmailDict.SymbolType.T_SEPARATOR) + 1);
    }


}
