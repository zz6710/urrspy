package com.kayak.rpt.email.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import com.kayak.rpt.email.util.EmailDict;
import com.spire.ms.System.Collections.ArrayList;
import lombok.Data;

import java.util.List;

@GraphQLModel(fetcher = "emailTemplateService",table = "email_template")
@Data
public class EmailTemplate {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "名称", sql = "name like '%$U{name}%'" ,field = "name")
   private String name;
   @GraphQLField(kkhtml = "KFieldText", label = "类型", sql = "type = $S{type}" ,field = "type")
   private String type;
   @GraphQLField(kkhtml = "KFieldText", label = "类型名称", sql = "type_name = $S{typeName}" ,field = "type_name")
   private String typeName;
   @GraphQLField(kkhtml = "KFieldText", label = "主题", sql = "subject like '%$U{subject}%'" ,field = "subject")
   private String subject;
   @GraphQLField(kkhtml = "KFieldText", label = "内容", sql = "content like '%$U{content}%'" ,field = "content")
   private String content;
   @GraphQLField(kkhtml = "KFieldText", label = "配置参数", sql = "dynamic_params like '%$U{dynamicParams}%'" ,field = "subject")
   private String dynamicParams;
   @GraphQLField(kkhtml = "KFieldText", label = "收件人", sql = "content = $S{receiver}" ,field = "receiver")
   private String receiver;
   @GraphQLField(kkhtml = "KFieldText", label = "抄送人", sql = "content = $S{cc}" ,field = "cc")
   private String cc;
   @GraphQLField(kkhtml = "KFieldText", label = "启用状态，0-未启用，1-已启用", sql = "status = $S{status}" ,field = "status")
   private String status;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间HHmmss", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期yyyyMMdd", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
//   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "create_user = $S{createUser}" ,field = "create_user")
//   private String createUser;
//   @GraphQLField(kkhtml = "KFieldText", label = "更新时间HHmmss", sql = "update_time = $S{updateTime}" ,field = "update_time")
//   private String updateTime;
//   @GraphQLField(kkhtml = "KFieldText", label = "更新日期yyyyMMdd", sql = "update_date = $S{updateDate}" ,field = "update_date")
//   private String updateDate;
//   @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "update_user = $S{updateUser}" ,field = "update_user")
//   private String updateUser;


   @GraphQLField(kkhtml = "KFieldText", label = "启用状态名称")
   private String statusName;
}