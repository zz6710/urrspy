package com.kayak.rpt.email.service;

import com.kayak.context.EmailThreadPoolProperties;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.email.dao.EmailTemplateDao;
import com.kayak.rpt.email.model.EmailAccount;
import com.kayak.rpt.email.model.EmailTemplate;
import com.kayak.rpt.email.util.EmailDict;
import com.kayak.rpt.email.util.EmailUtil;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import com.spire.ms.System.Collections.ArrayList;
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
import com.kayak.rpt.email.dao.EmailLogDao;
import com.kayak.rpt.email.model.EmailLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@APIDefine(desc = "邮件详情记录服务", model = EmailLog.class)
public class EmailLogService {
    private static Logger logger = LogManager.getLogger(EmailLogService.class);

    @Autowired
    private EmailLogDao emailLogDao;

    @Autowired
    private EmailTemplateDao emailTemplateDao;
    @Autowired
    private EmailThreadPoolProperties emailThreadPoolProperties;

    @API(desc = "查询邮件详情记录信息", auth = APIAuth.YES)
    public SqlResult<EmailLog> findEmailLogs(SqlParam<EmailLog> params) throws Exception {
        params.setMakeSql(true);
        return emailLogDao.findEmailLogs(params);
    }

    @API(desc = "添加邮件详情记录", params = "id,email_log_no,business_no,business_type,sender,receiver,cc,email_subject,email_body,attach_name,file_path,fail_reason,send_status,send_date,send_time", auth = APIAuth.NO)
    public int addEmailLog(SqlParam<EmailLog> params) throws Exception {
        params.getModel().setSendDate(DateUtil.getNowDate());
        params.getModel().setSendTime(DateUtil.getNowTime());
        return emailLogDao.addEmailLog(params).getEffect();
    }

    @API(desc = "添加邮件详情记录", params = "id,email_log_no,business_no,business_type,sender,receiver,cc,email_subject,email_body,attach_name,file_path,fail_reason,send_status,send_date,send_time", auth = APIAuth.NO)
    public int addEmailLogByMapParam(Map<String, Object> params) throws Exception {
        return emailLogDao.addEmailLogByMapParam(params).getEffect();
    }

    @API(desc = "修改邮件详情记录", params = "id,email_log_no,business_no,business_type,sender,receiver,cc,email_subject,email_body,attach_name,file_path,fail_reason,send_status,send_date,send_time", auth = APIAuth.NO)
    public int updateEmailLog(SqlParam<EmailLog> params) throws Exception {
        return emailLogDao.updateEmailLog(params).getEffect();
    }

    @API(desc = "删除邮件详情记录", params = "id,email_log_no,business_no,business_type,sender,receiver,cc,email_subject,email_body,attach_name,file_path,fail_reason,send_status,send_date,send_time", auth = APIAuth.NO)
    public int deleteEmailLog(SqlParam<EmailLog> params) throws Exception {
        return emailLogDao.deleteEmailLog(params).getEffect();
    }


    /**
     * 重发邮件  如果重新发送失败 或者 成功  页面都需要提示失败或成功 , 所有日志记录都需要支持重新发送-日志记录中需要记录对应的规则id
     * 需要是已经发过文件的，S3有文件生成的，先从S3下载文件到本地，再从本地发送邮件添加附件
     */
    @API(desc = "重新发送邮件", auth = APIAuth.NO)
    public String reSendEmail(SqlParam<EmailLog> params) throws Exception {
        if (params == null) {
//            throw new RuntimeException("邮件重发失败，未获取到原始邮件信息！");
            return RequestSupport.updateReturnJson(false, "邮件重发失败，附件文件不存在！", null).toString();
        }
        String id = params.getModel().getId();
        if (StringUtils.isEmpty(id)) {
//            throw new RuntimeException("邮件重发失败，原始邮件信息不存在！");
            return RequestSupport.updateReturnJson(false, "邮件重发失败，原始邮件信息不存在！", null).toString();
        }
        //判断上次发送状态,成功的,重发走日志重发; 失败的,重发走模板重发.
        EmailLog emailLog = emailLogDao.findEmailLogById(id);
        String sendStatus = emailLog.getSendStatus();
        //文件路径不存在，不可重发邮件。
        String filePath = emailLog.getFilePath();
        if (StringUtils.isEmpty(filePath)) {
//            throw new RuntimeException("邮件重发失败，附件文件不存在！");
            return RequestSupport.updateReturnJson(false, "邮件重发失败，附件文件不存在！", null).toString();
        }
        String result = reSendEmailByLogId(emailLog);
        if (EmailUtil.SUCCESS.equals(result)) {
            return RequestSupport.updateReturnJson(true, "重新发送邮件成功", null).toString();
        }
        return RequestSupport.updateReturnJson(false, "重新发送邮件失败", null).toString();

//        if (EmailDict.SendStatus.SUCCESS_00.equals(sendStatus)) {
//         reSendEmailByLogId(emailLog);

//        } else if(EmailDict.SendStatus.FAIL_02.equals(sendStatus)){
//            // 失败邮件,需要拿到本地文件路径,模板Id        日志表需要添加 模板Id字段
//            logger.info("失败邮件，重发，先校验信息完整性");
//            String localFilePaths = "";
//            String templateId = "";
//            reSendEmailByRuleTickId(emailLog,localFilePaths,templateId);
//        }
    }

    /**
     * 根据日志记录Id 重发邮件  所有信息都从日志记录中获取  [文件附件已生成]    若文件未生成，不发邮件。remotepath为空的，文件未生成。
     * 外部情况: 若模板修改- 新增 修改  删除  联系人    重发无法覆盖   失败的邮件无法重新发送[可根据记录状态判断,失败的走模板重发方法]
     *
     * @param emailLog
     * @return
     */
    public String reSendEmailByLogId(EmailLog emailLog) throws Exception {
//		Map<String, String> infoMap = new HashMap<>();
//		infoMap.put("", "");
        String preEmailLogId = emailLog.getId();
        emailLog.setEmailLogNo(DateUtil.getTimestamp17());
        Map<String, String> emailMap = new HashMap<>();
        emailMap.put("subject", emailLog.getEmailSubject());
        emailMap.put("content", emailLog.getEmailBody());
        emailMap.put("receiver", emailLog.getReceiver());
        emailMap.put("cc", emailLog.getCc());
        emailMap.put("filePath", emailLog.getFilePath());
        //"SUCCESS"发送成功，其他失败
        String result = "";
        //获取文件，发邮件
        try {
            //获取S3文件 下载到本地
            String localFilePath = "";
            //此处路径需带文件名
            String filePath = emailLog.getFilePath();
            if(StringUtils.isEmpty(filePath)){
                emailLog.setFilePath("");
            }else{
                String[] remoteFilePath = filePath.split(EmailDict.SymbolType.T_SEMICOLON);
                StringBuilder localFilePaths = new StringBuilder();
                for (String remoteFile : remoteFilePath) {
                    if(StringUtils.isNotEmpty(remoteFile)){
                        //下载S3文件
                        String tempFilePath =  emailThreadPoolProperties.getRptEmailtempLocalFilePath(); //temp不带文件名
                        logger.info("remoteFile:"+remoteFile  +";tempFilePath:"+tempFilePath);
                        String localFile = EmailUtil.downloadS3File(remoteFile,tempFilePath);
                        if(StringUtils.isNotEmpty(localFile)){
                            localFilePaths.append(localFile) ;
                            localFilePaths.append(EmailDict.SymbolType.T_SEMICOLON);
                        }
                    }
                }
                localFilePath = localFilePaths.toString();
            }
            emailMap.put("localFilePath", localFilePath);
            result = EmailUtil.sendEmail(emailMap);
            if (EmailUtil.SUCCESS.equals(result)) {
                emailLog.setSendStatus("1");
            } else {
                emailLog.setSendStatus("0");
            }
            //一般已成功的日志 再次发送,除id变更外,其他不变.
            emailLog.setId("");
            emailLog.setSendTime(DateUtil.getNowTime());
            emailLog.setSendDate(DateUtil.getNowDate());
            int addCount = emailLogDao.addEmailLogByModel(emailLog).getEffect();
            logger.info("根据邮件日志 重发邮件,addCount:" + addCount + "; SendStatus:" + result + "; preEmailLogId:" + preEmailLogId);
            return result;
//            Map<String,Object> res = new HashMap<>();
//            res.put("result",result);
//            return RequestSupport.updateReturnJson(true, "邮件发送成功！", res).toString();
        } catch (Exception e) {
            emailLog.setSendStatus("0");
            emailLog.setId("");
            int addCount = emailLogDao.addEmailLogByModel(emailLog).getEffect();
            logger.info("根据邮件日志 重发邮件失败,addCount:" + addCount + "; SendStatus:0 ; preEmailLogId:" + preEmailLogId+"; Exception:", e.getMessage());
            return result;
//            Map<String,Object> res = new HashMap<>();
//            res.put("result",result);
//            return RequestSupport.updateReturnJson(false, "邮件发送失败！", res).toString();
        }
    }

    /**
     * 附件上传到S3 [新生成的要发邮件的报表文件]
     */

    /**
     * S3附件下载到本地 【待发送邮件附件的文件】
     */

    /**
     * 根据前端传递参数获取邮件附件列表    实际下载是根据远程S3文件路径，将文件下载到用户电脑上。
     * @param params 前端参数
     * @return 文件列表
     */
    public SqlResult getFileList(SqlParam<EmailLog> params){
        if(params == null || params.getModel().getId().isEmpty()){
            logger.error("查询邮件日志记录对应的附件文件列表，请求参数不能为空");
            return null;
        }
        String id = params.getModel().getId();
        if(StringUtils.isEmpty(id)){
            logger.error("查询邮件日志记录对应的附件文件列表，日志记录不存在");
//            return RequestSupport.updateReturnJson(false, "邮件日志记录不存在", null).toString();
            return null;
        }
        //查询对应记录的 邮件附件列表
        String filePathStr = "";
        SqlResult<Map<String,String>> sqlResult = new SqlResult<>();
        try{
            List<Map<String,String>> mapList = new java.util.ArrayList<>();
            EmailLog emailLog = emailLogDao.findEmailLogById(id);
            filePathStr= emailLog.getFilePath();
            if(StringUtils.isEmpty(filePathStr)){
                logger.error("查询邮件日志记录对应的附件文件列表，附件记录不存在");
                return null;
            }
            String[] remoteFilePath = filePathStr.split(EmailDict.SymbolType.T_SEMICOLON);
            for (String remoteFile : remoteFilePath){
                if(StringUtils.isNotEmpty(remoteFile)){
                    Map<String,String> tempMap = new HashMap<>();
                    tempMap.put("filePath",remoteFile);
                    if(remoteFile.contains(EmailDict.SymbolType.T_D_SEPARATOR)){
                        tempMap.put("fileName",remoteFile.substring(remoteFile.lastIndexOf(EmailDict.SymbolType.T_D_SEPARATOR) + 1));
                    }else{
                        tempMap.put("fileName",remoteFile.substring(remoteFile.lastIndexOf(EmailDict.SymbolType.T_SEPARATOR) + 1));
                    }
                    tempMap.put("sendDate",emailLog.getSendDate());
                    mapList.add(tempMap);
                }
            }
            sqlResult.setRows(mapList);
            sqlResult.setResults(mapList.size());
        }catch (Exception e){
            logger.error("获取邮件日志记录的文件路径失败 excepiton{}",e.getMessage());
        }
        return sqlResult;
    }

    /**
     * 根据文件路径获取文件名称
     * @param remoteFilePath S3上文件路径
     * @return 文件名称
     */
    private String getFileNameByPath(String remoteFilePath){
        return remoteFilePath.substring(remoteFilePath.lastIndexOf(EmailDict.SymbolType.T_SEPARATOR) + 1);
    }
}
