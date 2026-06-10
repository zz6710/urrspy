package com.kayak.rpt.zz.manage.model;

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
@GraphQLModel(fetcher = "importTemplateManageField01Service",table = "import_template_manage_field_01")
public class ImportTemplateManageFieldFst {

    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "系统表名", sql = "system_table_name = $S{systemTableName}" ,field = "system_table_name")
    private String systemTableName;
    @GraphQLField(kkhtml = "KFieldText", label = "数据库列字段", sql = "database_column_code = $S{databaseColumnCode}" ,field = "database_column_code")
    private String databaseColumnCode;
    @GraphQLField(kkhtml = "KFieldText", label = "数据库列名", sql = "database_column_name = $S{databaseColumnName}" ,field = "database_column_name")
    private String databaseColumnName;
    @GraphQLField(kkhtml = "KFieldText", label = "数据类型", sql = "column_type = $S{columnType}" ,field = "column_type")
    private String columnType;
    @GraphQLField(kkhtml = "KFieldText", label = "数据单位", sql = "column_unit = $S{columnUnit}" ,field = "column_unit")
    private String columnUnit;
    @GraphQLField(kkhtml = "KFieldText", label = "模板列序号", sql = "template_column_serial = $S{templateColumnSerial}" ,field = "template_column_serial")
    private String templateColumnSerial;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人员", sql = "upt_usr = $S{uptUsr}" ,field = "upt_usr")
    private String uptUsr;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upt_date = $S{uptDate}" ,field = "upt_date")
    private String uptDate;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upt_time = $S{uptTime}" ,field = "upt_time")
    private String uptTime;
    private String tableComment;
    @GraphQLField(label = "字段名")
    private String columnName;
    @GraphQLField(label = "字段注释")
    private String columnComment;
    private  String tableName;
}
