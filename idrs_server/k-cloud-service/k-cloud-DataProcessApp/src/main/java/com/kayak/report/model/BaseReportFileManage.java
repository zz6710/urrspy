package com.kayak.report.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "baseReportFileManageService",table = "base_report_file_manage")
@Data
public class BaseReportFileManage {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "文件名称", sql = "file_name = $S{fileName}" ,field = "file_name")
   private String fileName;
   @GraphQLField(kkhtml = "KFieldText", label = "文件类型", sql = "file_type = $S{fileType}" ,field = "file_type")
   private String fileType;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
   private String prodCd;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_nm_fu = $S{prodNmFu}" ,field = "prod_nm_fu")
   private String prodNmFu;
   @GraphQLField(kkhtml = "KFieldText", label = "上传压缩文件名称", sql = "zipfilename = $S{zipfilename}" ,field = "zipfilename")
   private String zipfilename;
   @GraphQLField(kkhtml = "KFieldText", label = "oss文件路径", sql = "remote_file = $S{remoteFile}" ,field = "remote_file")
   private String remoteFile;
   @GraphQLField(kkhtml = "KFieldText", label = "操作员编号", sql = "operaterno = $S{operaterno}" ,field = "operaterno")
   private String operaterno;
   @GraphQLField(kkhtml = "KFieldText", label = "操作员名称", sql = "operatername = $S{operatername}" ,field = "operatername")
   private String operatername;
   @GraphQLField(kkhtml = "KFieldText", label = "上传日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "上传时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}" ,field = "upd_date")
   private String updDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
   private String updTime;

   @GraphQLField(kkhtml = "KFieldText", label = "产品运作模式", sql = "operation_mode = $S{operationMode}" ,field = "operation_mode")
   private String operationMode;

}