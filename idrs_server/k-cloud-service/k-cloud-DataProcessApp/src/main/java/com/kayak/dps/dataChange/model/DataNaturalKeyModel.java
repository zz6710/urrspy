package com.kayak.dps.dataChange.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;

@Data
@GraphQLModel(fetcher = "dataNaturalKeyService",table = "rem_datanatural_key")
public class DataNaturalKeyModel {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "序号", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "所属层级", sql = "hierarchy = $S{hierarchy}" ,field = "hierarchy")
    private String hierarchy;
    @GraphQLField(kkhtml = "KFieldText", label = "数据库表名", sql = "table_name = $S{tableName}" ,field = "table_name")
    private String tableName;
    @GraphQLField(kkhtml = "KFieldText", label = "业务主键", sql = "natural_key = $S{naturalKey}" ,field = "natural_key")
    private String naturalKey;
    @GraphQLField(kkhtml = "KFieldText", label = "业务主键名",field = "natural_key_name")
    private String naturalKeyName;
    @GraphQLField(kkhtml = "KFieldText", label = "标准主键", sql = "standard_key = $S{standardKey}" ,field = "standard_key")
    private String standardKey;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
    private String updDt;
    @GraphQLField(kkhtml = "KFieldText", label = "外部数据字典", sql = "out_dict = $S{outDict}" ,field = "out_dict")
    private String outDict;

    @GraphQLField(kkhtml = "KFieldText", label = "数据表名" ,field = "tables")
    private String tables;
    @GraphQLField(kkhtml = "KFieldText", label = "数据表注释" ,field = "tablesName")
    private String tablesName;
    @GraphQLField(kkhtml = "KFieldText", label = "主键集合")
    private List<DataNaturalKeyModel> naturalKeyGridData;
    @GraphQLField(kkhtml = "KFieldText", label = "数据库表集合")
    private List<DataNaturalKeyModel> tableNames;
}