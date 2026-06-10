package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @author XueJ
 * @version 1.0.0
 * @ClassName ImportTemplateManage.java
 * @Description TODO
 * @createTime 2023年08月11日 19:30:00
 */
@Data
@GraphQLModel(fetcher = "importTemplateManageService",table = "import_template_manage")
public class ImportTemplateManage {
    @GraphQLField(kkhtml = "KFieldText", label = "证券编号", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "系统表名", sql = "system_table_name = $S{systemTableName}" ,field = "system_table_name")
    private String systemTableName;
    @GraphQLField(kkhtml = "KFieldText", label = "模板名称", sql = "template_name = $S{templateName}" ,field = "template_name")
    private String templateName;
    @GraphQLField(kkhtml = "KFieldText", label = "模板本地文件名称", sql = "template_file_name = $S{templateFileName}" ,field = "template_file_name")
    private String templateFileName;
    @GraphQLField(kkhtml = "KFieldText", label = "模板本地文件路径", sql = "template_file_path = $S{templateFilePath}" ,field = "template_file_path")
    private String templateFilePath;
    @GraphQLField(kkhtml = "KFieldText", label = "导入类型", sql = "import_type = $S{importType}" ,field = "import_type")
    private String importType;
    @GraphQLField(kkhtml = "KFieldText", label = "版本号", sql = "version = $S{version}" ,field = "version")
    private String version;
    @GraphQLField(kkhtml = "KFieldText", label = "模板状态", sql = "template_status = $S{templateStatus}" ,field = "template_status")
    private String templateStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "导入人员", sql = "imp_usr = $S{impUsr}" ,field = "imp_usr")
    private String impUsr;
    @GraphQLField(kkhtml = "KFieldText", label = "导入日期（yyyyMMdd）", sql = "imp_date = $S{impDate}" ,field = "imp_date")
    private String impDate;
    @GraphQLField(kkhtml = "KFieldText", label = "导入时间（HHmmss）", sql = "imp_time = $S{impTime}" ,field = "imp_time")
    private String impTime;
    @GraphQLField(kkhtml = "KFieldText", label = "模板文件oss路径", sql = "oss_file_path = $S{ossFilePath}" ,field = "oss_file_path")
    private String ossFilePath;

    /*数据状态*/
    private  String sysDataStatus;
    /*数据日期*/
    private  String sysDataDate;
    /*数据日期*/
    private  String itemkey;
    /*数据版本*/
    private  String sysDataVersion;
    /*表名*/
    private  String tableName;

    /*数据源*/
    private  String sysDataSource;

    @GraphQLField
    private String rowStart;
    @GraphQLField
    private String columnStart;
    @GraphQLField
    private String skipColumn;
}
