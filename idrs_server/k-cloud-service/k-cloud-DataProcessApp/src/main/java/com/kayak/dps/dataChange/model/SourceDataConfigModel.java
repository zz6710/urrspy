package com.kayak.dps.dataChange.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;

@Data
@GraphQLModel(fetcher = "sourceDataConfigService",table = "rem_sourcedata_config")
public class SourceDataConfigModel {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "序号", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "数据表", sql = "table_name = $S{tableName}" ,field = "table_name")
    private String tableName;
    @GraphQLField(kkhtml = "KFieldText", label = "提醒字段", sql = "remind_field = $S{remindField}" ,field = "remind_field")
    private String remindField;
    @GraphQLField(kkhtml = "KFieldText", label = "数字提醒规则", sql = "remind_type = $S{remindType}" ,field = "remind_type")
    private String remindType;
    @GraphQLField(kkhtml = "KFieldText", label = "对比规则", sql = "comparison_rules = $S{comparisonRules}" ,field = "comparison_rules")
    private String comparisonRules;
    @GraphQLField(kkhtml = "KFieldText", label = "涉及报表", sql = "related_report = $S{relatedReport}" ,field = "related_report")
    private String relatedReport;
    @GraphQLField(kkhtml = "KFieldText", label = "关联任务组", sql = "task_group = $S{taskGroup}" ,field = "task_group")
    private String taskGroup;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
    private String crtDt;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
    private String updDt;
    @GraphQLField(kkhtml = "KFieldText", label = "字段类型",sql = "field_type = $S{fieldType}" ,field = "field_type")
    private String fieldType;
    @GraphQLField(kkhtml = "KFieldText", label = "字段数据字典",sql = "out_dict = $S{outDict}" ,field = "out_dict")
    private String outDict;
    @GraphQLField(kkhtml = "KFieldText", label = "字段注释" ,sql = "remindfields_name = $S{remindfieldsname}" ,field = "remindfields_name")
    private String remindfieldsName;

    @GraphQLField(kkhtml = "KFieldText", label = "数据表名" ,field = "tables")
    private String tables;
    @GraphQLField(kkhtml = "KFieldText", label = "数据表注释" ,field = "tablesName")
    private String tablesName;
    @GraphQLField(kkhtml = "KFieldText", label = "字段名" ,field = "remindfields")
    private String remindfields;
    @GraphQLField(kkhtml = "KFieldText", label = "涉及报表名称",field = "report_name")
    private String reportName;
    @GraphQLField(kkhtml = "KFieldText", label = "提醒字段集合")
    private List<SourceDataConfigModel> fieldCodeGridData;
    @GraphQLField(kkhtml = "KFieldText", label = "是否是修改")
    private String isEdit;
    @GraphQLField(kkhtml = "KFieldText", label = "清算组名称")
    private String taskGroupName;
}
