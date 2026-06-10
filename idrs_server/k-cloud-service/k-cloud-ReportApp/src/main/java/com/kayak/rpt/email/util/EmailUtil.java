package com.kayak.rpt.email.util;

import com.kayak.context.EmailThreadPoolProperties;
import com.kayak.core.system.SysUtil;
import com.kayak.rpt.email.service.EmailSendService;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.*;

public class EmailUtil {
    public static final String SUCCESS = "SUCCESS";
    public static final String CHARSET_DEFAULT = "UTF-8";

    private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);
    @Autowired
    private EmailThreadPoolProperties emailThreadPoolProperties;
    /**
     * 发送邮件，字段处理   邮件主题和正文参数在外部组装好后才到此处
     *
     * @param emailMap （包含
     *                 必选：emailAccountName  receiver  cc subject  content  localFilePath有附件必填(本地文件路径 含文件名，本地需存在文件)
     *                 可选：  sender-不填取系统配置  sendSecret-不填取系统配置）
     * @return
     * @throws Exception
     */
    public static String sendEmail(Map<String, String> emailMap){
        logger.info("邮件发送请求参数：{}", emailMap.toString());
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
        //附件路径 emailMap.get("localFilePath");
        String[] localFilePathArr = {};
        //修改接口传入  参数key值包含(emailAccountName,receiveMailAccount,cc,subject,content,localFilePathArr)
        try {
            // 根据系统参数配置获取 当前发件ip信息
            myEmailSMTPHost = SysUtil.getSystemParamsByParaid(EmailDict.EmailHostParams.SEND_HOST);
            //根据配置获取 当前发件port信息 commonParamUtils.getSysParamConfig("smtp_port")
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
            //本地文件名（发邮件需要先下载S3文件到本地）
            String localFilePaths = emailMap.get("localFilePath");
            logger.info("localFilePaths:" +localFilePaths);
            if (StringUtils.isNotEmpty(localFilePaths)) {
                localFilePathArr = localFilePaths.split(EmailDict.SymbolType.T_SEMICOLON);
            } else {
                localFilePathArr = new String[]{};
            }
            logger.info("邮件host--->{},prot----->{},收件人{},本地文件路径{}", myEmailSMTPHost, smtpPort, receiver,localFilePaths);
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
            MimeMessage message = createMimeMessage(session, sender, tos, toc, emailAccountName, subject, content, localFilePathArr);
            Transport transport = session.getTransport();
            try {
                transport.connect(sender, sendSecret);
                transport.sendMessage(message, message.getAllRecipients());
                message.saveChanges();
                emailMap.put("sendStatus", EmailDict.EffectStatus.ON_1);
                return EmailDict.SUCCESS;
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
    }

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
     * @param filePath 本地文件路径 必须包含文件名
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessage(Session session, String sendMail, Address[] tos, Address[] toc, String emailAcctnoName, String subject, String content, String[] filePath) throws Exception {
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
            //文本内容
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
                    String fileName = getFileNameByPath(filePath[i]);
                    logger.info("fileName:"+fileName);
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
    }

    /**
     * 创建接收人地址集合
     */
    public static Address[] createTos(String receiver) throws AddressException {
        Address[] tos = null;

        if (!StringUtils.isEmpty(receiver)) {
            if( receiver.endsWith(";")){
                receiver = receiver.substring(0,receiver.length()-1);
            }
            String[] toAarr = receiver.split(";");
            if (toAarr.length > 0) {
                tos = new InternetAddress[toAarr.length];
                for (int i = 0; i < toAarr.length; i++) {
                    tos[i] = new InternetAddress(toAarr[i]);
                }
            }
        }
        return tos;
    }

    /**
     * 替换模板中的内容
     *
     * @param content
     * @param replaceMap
     * @param reportDate  20250831 特殊处理业务对应的年月日
     * @return
     */
    public static String buildContent(String content, Map<String, String> replaceMap,String reportDate) {
        StringBuffer buffer = new StringBuffer(content);
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            String realKey = "#{" + entry.getKey() + "}";
            int index = buffer.indexOf(realKey);
            while (index != -1) {
                if(StringUtils.isNotEmpty(reportDate) && reportDate.length()==8){
                    if("reportYear".equals(entry.getKey())){
                        buffer.replace(index, index + realKey.length(), reportDate.substring(0,4));
                    }else if("reportMonth".equals(entry.getKey())){
                        buffer.replace(index, index + realKey.length(), reportDate.substring(4,6));
                    }else if("reportDay".equals(entry.getKey())){
                        buffer.replace(index, index + realKey.length(), reportDate.substring(6,8));
                    }else{
                        buffer.replace(index, index + realKey.length(), String.valueOf(entry.getValue()));
                    }
                }else{
                        buffer.replace(index, index + realKey.length(), String.valueOf(entry.getValue()));
                }

                index = buffer.indexOf(realKey, index + String.valueOf(entry.getValue()).length());
            }
        }
        return buffer.toString();
    }

    /**
     * 根据文件路径（含文件名）  获取单个文件名称
     * @param FilePath   本地文件路径（可能含"/"或”\\“） 或者 远程文件路径(含”/“)
     * @return 文件名称
     */
    private static String getFileNameByPath(String FilePath){
        if(FilePath.contains(EmailDict.SymbolType.T_D_SEPARATOR)){
            return FilePath.substring(FilePath.lastIndexOf(EmailDict.SymbolType.T_D_SEPARATOR) + 1);
        }
        return FilePath.substring(FilePath.lastIndexOf(EmailDict.SymbolType.T_SEPARATOR) + 1);
    }

    /**
     * 根据 页面配置参数 获取配置的key和value   方便后续替换邮件主题和正文中的参数
     * @param params  页面配置参数（包含#{}）   参数配置：#{user1:u1}#{user2:u2}#{param3:p3}
     * @return Map<String,String>
     */
    public static Map<String,String> getTemplatePageParams(String params) throws Exception{
        try{
            if(StringUtils.isEmpty(params)){
                Map<String,String> paramMap = new HashMap<>();
                return paramMap;
            }
            String[] strArr = params.split(EmailDict.SymbolType.T_SINGLE_SEP);
            Map<String,String> paramMap = new HashMap<>();
            for(int i=1;i<strArr.length;i++){
//            logger.debug("拆分的单个参数："+strArr[i]);
                String strele = strArr[i];
                String key = strele.substring(1,strele.lastIndexOf(EmailDict.SymbolType.T_COLON));
                String value = strele.substring(strele.lastIndexOf(EmailDict.SymbolType.T_COLON)+1,strele.length()-1);
                paramMap.put(key,value);
            }
            return paramMap;
        }catch (Exception e){
           throw new Exception("参数不合法; params:"+params+";Exception"+e.getMessage());
        }
    }


    /**
     * 从S3上下载邮件附件  单个文件
     * @param remoteFilePath S3文件路径 带文件名
     * @return 下载到本地服务器文件的路径  带文件名
     */
    public static String downloadS3File(String remoteFilePath,String localFilePath) throws Exception{
        if(StringUtils.isEmpty(remoteFilePath)){
            return "";
        }
        String tempLocalPath =  localFilePath+ getFileNameByPath(remoteFilePath);
        logger.info("remoteFilePath :"+remoteFilePath +"; localFilePath:"+localFilePath+"; tempLocalPath:"+tempLocalPath);
        try {
            FileTransfer transfer = FileTransferHelpler.getTransfer();
            transfer.downloadFileAndDisconnect(remoteFilePath,tempLocalPath);
        } catch (Exception e) {
            logger.error("组装邮件附件时，从S3下载附件失败。S3路径为【{}】",remoteFilePath+"; Exception:"+e.getMessage());
            return "";
        }
        return tempLocalPath;
    }


}
