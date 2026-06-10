package com.kayak.rpt.email.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "emailLogService",table = "email_log")
@Data
public class EmailLog {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "流水号", sql = "email_log_no = $S{emailLogNo}" ,field = "email_log_no")
   private String emailLogNo;
   @GraphQLField(kkhtml = "KFieldText", label = "对应业务的流水号", sql = "business_no = $S{businessNo}" ,field = "business_no")
   private String businessNo;
   @GraphQLField(kkhtml = "KFieldText", label = "邮件业务类型", sql = "business_type = $S{businessType}" ,field = "business_type")
   private String businessType;
   @GraphQLField(kkhtml = "KFieldText", label = "邮件发生日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "发件人", sql = "sender = $S{sender}" ,field = "sender")
   private String sender;
   @GraphQLField(kkhtml = "KFieldText", label = "收件人", sql = "receiver = $S{receiver}" ,field = "receiver")
   private String receiver;
   @GraphQLField(kkhtml = "KFieldText", label = "抄送人", sql = "cc = $S{cc}" ,field = "cc")
   private String cc;
   @GraphQLField(kkhtml = "KFieldText", label = "邮件主题", sql = "email_subject = $S{emailSubject}" ,field = "email_subject")
   private String emailSubject;
   @GraphQLField(kkhtml = "KFieldText", label = "邮件正文", sql = "email_body = $S{emailBody}" ,field = "email_body")
   private String emailBody;
   @GraphQLField(kkhtml = "KFieldText", label = "附件", sql = "attach_name = $S{attachName}" ,field = "attach_name")
   private String attachName;
   @GraphQLField(kkhtml = "KFieldText", label = "本地文件路径", sql = "file_path = $S{filePath}" ,field = "file_path")
   private String filePath;
   @GraphQLField(kkhtml = "KFieldText", label = "失败原因", sql = "fail_reason = $S{failReason}" ,field = "fail_reason")
   private String failReason;
   @GraphQLField(kkhtml = "KFieldText", label = "发送状态", sql = "send_status = $S{sendStatus}" ,field = "send_status")
   private String sendStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "发送日期", sql = "send_date = $S{sendDate}" ,field = "send_date")
   private String sendDate;
   @GraphQLField(kkhtml = "KFieldText", label = "发送时间", sql = "send_time = $S{sendTime}" ,field = "send_time")
   private String sendTime;
  /* @GraphQLField(kkhtml = "KFieldText", label = "发送人id", sql = "send_user_id = $S{sendUserId}" ,field = "send_user_id")
   private String sendUserId;
   @GraphQLField(kkhtml = "KFieldText", label = "发送人名称", sql = "send_user_name = $S{sendUserName}" ,field = "send_user_name")
   private String sendUserName;
   @GraphQLField(kkhtml = "KFieldText", label = "流程状态", sql = "effect_flag = $S{effectFlag}" ,field = "effect_flag")
   private String effectFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "create_user = $S{createUser}" ,field = "create_user")
   private String createUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "update_user = $S{updateUser}" ,field = "update_user")
   private String updateUser;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "update_time = $S{updateTime}" ,field = "update_time")
   private String updateTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "update_date = $S{updateDate}" ,field = "update_date")
   private String updateDate;*/

}