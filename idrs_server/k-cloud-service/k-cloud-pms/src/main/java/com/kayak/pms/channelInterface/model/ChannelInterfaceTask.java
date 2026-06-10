package com.kayak.pms.channelInterface.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "channelInterfaceTaskService",table = "t8_channel_interface_task")
@Data
public class ChannelInterfaceTask {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "任务代码", sql = "task_code = $S{taskCode}" ,field = "task_code")
   private String taskCode;
   @GraphQLField(kkhtml = "KFieldText", label = "任务名称", sql = "task_name = $S{taskName}" ,field = "task_name")
   private String taskName;
   @GraphQLField(kkhtml = "KFieldText", label = "渠道编码", sql = "channel_no = $S{channelNo}" ,field = "channel_no")
   private String channelNo;
   @GraphQLField(kkhtml = "KFieldText", label = "渠道名称", sql = "channel_name = $S{channelName}" ,field = "channel_name")
   private String channelName;
   @GraphQLField(kkhtml = "KFieldText", label = "接口编码", sql = "interface_no = $S{interfaceNo}" ,field = "interface_no")
   private String interfaceNo;
   @GraphQLField(kkhtml = "KFieldText", label = "是否启用", sql = "is_enabled = $S{isEnabled}" ,field = "is_enabled")
   private String isEnabled;
   @GraphQLField(kkhtml = "KFieldText", label = "跳转url", sql = "url = $S{url}" ,field = "url")
   private String url;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
   private String crtUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "param_model = $S{paramModel}" ,field = "param_model")
   private String paramModel;
   @GraphQLField(kkhtml = "KFieldText", label = "下发方式", sql = "issue_type = $S{issueType}" ,field = "issue_type")
   private String issueType;

   @GraphQLField
   private String interfaceName;


   @GraphQLField
   private String channelNoList;
}