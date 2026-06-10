package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "importTemplateDataLogService",table = "import_template_data_log")
@Data
public class ImportTemplateDataLog {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "模板id", sql = "import_template_manage_id = $S{importTemplateManageId}" ,field = "import_template_manage_id")
   private String importTemplateManageId;
   @GraphQLField(kkhtml = "KFieldText", label = "基准日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "数据版本号", sql = "sys_data_version = $S{sysDataVersion}" ,field = "sys_data_version")
   private String sysDataVersion;
   @GraphQLField(kkhtml = "KFieldText", label = "导入时间", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField
   private String templateName;
   @GraphQLField
   private String tableName;

}