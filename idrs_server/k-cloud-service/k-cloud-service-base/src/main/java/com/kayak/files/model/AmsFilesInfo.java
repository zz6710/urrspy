package com.kayak.files.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "amsFilesInfoService",table = "ams_files_info")
@Data
public class AmsFilesInfo {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "文件名", sql = "file_name = $S{fileName}" ,field = "file_name")
   private String fileName;
   @GraphQLField(kkhtml = "KFieldText", label = "oss路径", sql = "oss_path = $S{ossPath}" ,field = "oss_path")
   private String ossPath;
   @GraphQLField(kkhtml = "KFieldText", label = "文件类型", sql = "file_type = $S{fileType}" ,field = "file_type")
   private String fileType;
   @GraphQLField(kkhtml = "KFieldText", label = "上传时间", sql = "upload_time = $S{uploadTime}" ,field = "upload_time")
   private String uploadTime;
   @GraphQLField(kkhtml = "KFieldText", label = "服务器路径", sql = "server_path = $S{serverPath}" ,field = "server_path")
   private String serverPath;

}