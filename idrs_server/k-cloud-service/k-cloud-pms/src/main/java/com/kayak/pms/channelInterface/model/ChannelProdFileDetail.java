package com.kayak.pms.channelInterface.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "channelProdFileDetailService",table = "t8_channel_prod_file_detail")
@Data
public class ChannelProdFileDetail {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "对接文件表ID", sql = "channel_interface_file_id = $S{channelInterfaceFileId}" ,field = "channel_interface_file_id")
   private String channelInterfaceFileId;
   @GraphQLField(kkhtml = "KFieldText", label = "自增序号，从1开始", sql = "data_row_num = $S{dataRowNum}" ,field = "data_row_num")
   private String dataRowNum;
   @GraphQLField(kkhtml = "KFieldText", label = "回执状态", sql = "status = $S{status}" ,field = "status")
   private String status;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
   private String crtUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
   private String updTime;
   @GraphQLField(kkhtml = "KFieldText", label = "返回信息", sql = "return_desc = $S{returnDesc}" ,field = "return_desc")
   private String returnDesc;

   @GraphQLField
   private String fileName;

}