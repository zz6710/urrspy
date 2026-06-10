package com.kayak.pms.channelInterface.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "channelProdTaskService",table = "t8_channel_prod_task")
@Data
public class ChannelProdTask {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "任务代码", sql = "task_code = $S{taskCode}" ,field = "task_code")
   private String taskCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name = $S{prodName}" ,field = "prod_name")
   private String prodName;
   @GraphQLField(kkhtml = "KFieldText", label = "任务状态", sql = "task_status = $S{taskStatus}" ,field = "task_status")
   private String taskStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "产品标识", sql = "prod_flag = $S{prodFlag}" ,field = "prod_flag")
   private String prodFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "参数下发代码", sql = "param_code = $S{paramCode}" ,field = "param_code")
   private String paramCode;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人id", sql = "crt_user_id = $S{crtUserId}" ,field = "crt_user_id")
   private String crtUserId;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
   private String crtUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "json格式的请求参数", sql = "params = $S{params}" ,field = "params")
   private String params;

   @GraphQLField
   private String taskName;
   @GraphQLField
   private String channelName;

   @GraphQLField
   private String paramModel;

}