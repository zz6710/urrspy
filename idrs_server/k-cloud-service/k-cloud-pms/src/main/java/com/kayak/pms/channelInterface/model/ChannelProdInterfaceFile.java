package com.kayak.pms.channelInterface.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "channelProdInterfaceFileService",table = "t8_channel_prod_interface_file")
@Data
public class ChannelProdInterfaceFile {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品参数任务表id", sql = "task_prod_id = $S{taskProdId}" ,field = "task_prod_id")
   private String taskProdId;
   @GraphQLField(kkhtml = "KFieldText", label = "接口编码", sql = "interface_no = $S{interfaceNo}" ,field = "interface_no")
   private String interfaceNo;
   @GraphQLField(kkhtml = "KFieldText", label = "对接文件名称", sql = "file_name = $S{fileName}" ,field = "file_name")
   private String fileName;
   @GraphQLField(kkhtml = "KFieldText", label = "对接文件路径", sql = "file_path = $S{filePath}" ,field = "file_path")
   private String filePath;
   @GraphQLField(kkhtml = "KFieldText", label = "回执信息", sql = "return_desc = $S{returnDesc}" ,field = "return_desc")
   private String returnDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "文件状态", sql = "status = $S{status}" ,field = "status")
   private String status;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
   private String crtUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "任务标识", sql = "task_flag = $S{taskFlag}" ,field = "task_flag")
   private String taskFlag;

   @GraphQLField
    private String prodCode;
   @GraphQLField
    private String prodName;
   @GraphQLField
    private String taskName;
   @GraphQLField
    private String interfaceName;
   @GraphQLField
   private String taskCode;
}