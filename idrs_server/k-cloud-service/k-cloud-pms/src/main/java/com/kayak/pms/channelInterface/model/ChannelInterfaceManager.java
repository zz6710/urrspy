package com.kayak.pms.channelInterface.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "channelInterfaceManagerService",table = "t8_channel_interface_manager")
@Data
public class ChannelInterfaceManager {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "渠道编码", sql = "channel_no = $S{channelNo}" ,field = "channel_no")
   private String channelNo;
   @GraphQLField(kkhtml = "KFieldText", label = "渠道名称", sql = "channel_name = $S{channelName}" ,field = "channel_name")
   private String channelName;
   @GraphQLField(kkhtml = "KFieldText", label = "接口编码", sql = "interface_no = $S{interfaceNo}" ,field = "interface_no")
   private String interfaceNo;
   @GraphQLField(kkhtml = "KFieldText", label = "接口名称", sql = "interface_name = $S{interfaceName}" ,field = "interface_name")
   private String interfaceName;
   @GraphQLField(kkhtml = "KFieldText", label = "文件路径", sql = "file_path = $S{filePath}" ,field = "file_path")
   private String filePath;
   @GraphQLField(kkhtml = "KFieldText", label = "数据源sql", sql = "select_sql = $S{selectSql}" ,field = "select_sql")
   private String selectSql;
   @GraphQLField(kkhtml = "KFieldText", label = "接口执行方法", sql = "interface_action = $S{interfaceAction}" ,field = "interface_action")
   private String interfaceAction;
   @GraphQLField(kkhtml = "KFieldText", label = "接口回执方法", sql = "interface_receive_action = $S{interfaceReceiveAction}" ,field = "interface_receive_action")
   private String interfaceReceiveAction;
   @GraphQLField(kkhtml = "KFieldText", label = "全量查询条件", sql = "full_query_condition = $S{fullQueryCondition}" ,field = "full_query_condition")
   private String fullQueryCondition;
   @GraphQLField(kkhtml = "KFieldText", label = "增量查询条件", sql = "incremental_query_condition = $S{incrementalQueryCondition}" ,field = "incremental_query_condition")
   private String incrementalQueryCondition;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
   private String crtUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "文件序列自增方式", sql = "channel_seq_type = $S{channelSeqType}" ,field = "channel_seq_type")
   private String channelSeqType;
   @GraphQLField(kkhtml = "KFieldText", label = "文件名称sql", sql = "file_name_sql = $S{fileNameSql}" ,field = "file_name_sql")
   private String fileNameSql;
   @GraphQLField
   private  String paramModel;

}