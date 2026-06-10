package com.kayak.dps.dataChange.model;

import com.kayak.dps.app.model.BondInfoModel;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;

@Data
@GraphQLModel(fetcher = "sourceDataChgInfoService",table = "rem_sourcedata_chginfo")
public class SourceDataChgInfoModel {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "序号", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "数据涉及报表",field = "report_name")
    private String reportName;
    @GraphQLField(kkhtml = "KFieldText", label = "数据涉及报表",field = "reports_name")
    private String reportsName;
    @GraphQLField(kkhtml = "KFieldText", label = "涉及字段", sql = "change_field = $S{changeField}" ,field = "change_field")
    private String changeField;
    @GraphQLField(kkhtml = "KFieldText", label = "涉及字段名称")
    private String changeFieldName;
    @GraphQLField(kkhtml = "KFieldText", label = "主键信息", sql = "natural_keys = $S{naturalKeys}" ,field = "natural_keys")
    private String naturalKeys;
    @GraphQLField(kkhtml = "KFieldText", label = "主键信息名", field = "natural_keys_name")
    private String naturalKeysName;
    @GraphQLField(kkhtml = "KFieldText", label = "数据库表", sql = "tables = $S{tables}" ,field = "tables")
    private String tables;
    @GraphQLField(kkhtml = "KFieldText", label = "数据库表名", sql = "table_name = $S{tableName}" ,field = "table_name")
    private String tableName;
    @GraphQLField(kkhtml = "KFieldText", label = "原数据", sql = "field_old = $S{fieldOld}" ,field = "field_old")
    private String fieldOld;
    @GraphQLField(kkhtml = "KFieldText", label = "新数据", sql = "field_new = $S{fieldNew}" ,field = "field_new")
    private String fieldNew;
    @GraphQLField(kkhtml = "KFieldText", label = "数据状态", sql = "status = $S{status}" ,field = "status")
    private String status;
    @GraphQLField(kkhtml = "KFieldText", label = "修改状态", sql = "editstatus = $S{editstatus}" ,field = "editstatus")
    private String editstatus;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
    private String crtDt;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
    private String updDt;
    @GraphQLField(kkhtml = "KFieldText", label = "旧数据ID", sql = "oldid = $S{oldid}" ,field = "oldid")
    private String oldid;
    @GraphQLField(kkhtml = "KFieldText", label = "新数据ID", sql = "newid = $S{newid}" ,field = "newid")
    private String newid;
    @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
    private String dealDate;
    @GraphQLField(label = "源数据集")
    private List<SourceDataChgInfoModel> sourceData;
    @GraphQLField(kkhtml = "KFieldText", label = "外部数据字典")
    private String outDict;
    @GraphQLField(kkhtml = "KFieldText", label = "旧数据显示")
    private String dictOld;
    @GraphQLField(kkhtml = "KFieldText", label = "新数据显示")
    private String dictNew;
    @GraphQLField(kkhtml = "KFieldText", label = "清算数据组")
    private String taskGroup;
    @GraphQLField
    private String theoryReportStartDate;
    @GraphQLField
    private String theoryReportEndDate;
}
