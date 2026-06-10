package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureProdTaskService", table = "idb_notice_grid_config_source")
public class IdbNoticeGridConfigSource {
    @GraphQLField(label = "id", field = "id")
    private String id;

    @GraphQLField(label = "信披类型", field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(label = "信披子类型", field = "disclosure_son_type")
    private String disclosureSonType;
    @GraphQLField(label = "替换字符串", field = "replace_str")
    private String replaceStr;
    @GraphQLField(label = "字段名称", field = "column_name")
    private String columnName;
    @GraphQLField(label = "所处行数", field = "row_order")
    private String rowOrder;
    @GraphQLField(label = "所处列数", field = "column_order")
    private String columnOrder;
    @GraphQLField(label = "替换字符取值表名", field = "value_table_name")
    private String valueTableName;
    @GraphQLField(label = "替换字符取值代码", field = "value_column_code")
    private String valueColumnCode;
    @GraphQLField(label = "执行语句id", field = "exeid")
    private String exeid;
    @GraphQLField(label = "需合并行数", field = "merge_row_num")
    private String mergeRowNum;
    @GraphQLField(label = "需合并列数", field = "merge_column_num")
    private String mergeColumnNum;
    @GraphQLField(label = "生效日期", field = "effect_date")
    private String effectDate;
    @GraphQLField(label = "失效日期", field = "expiry_date")
    private String expiryDate;
    @GraphQLField(label = "基础表入数据表语句id", field = "data_execute_id")
    private String dataExecuteId;
    @GraphQLField(kkhtml = "KFieldText", label = "模板版本id", field = "disclosure_mod_version_id")
    private String disclosureModVersionId;
    @GraphQLField(label = "备注", field = "remark")
    private String remark;
}
