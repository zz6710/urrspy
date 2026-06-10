package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "disclosureGridConfigService",table = "idb_notice_grid_config")
public class DisclosureGridConfig {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "信披类型", field = "disclosure_type")
   private String disclosureType;
   @GraphQLField(kkhtml = "KFieldText", label = "信披子类型", field = "disclosure_son_type")
   private String disclosureSonType;
   @GraphQLField(kkhtml = "KFieldText", label = "模板版本id", sql = "disclosure_mod_version_id = $S{disclosureModVersionId}" ,field = "disclosure_mod_version_id")
   private String disclosureModVersionId;
   @GraphQLField(kkhtml = "KFieldText", label = "模板替换字符", sql = "replace_str = $S{replaceStr}" ,field = "replace_str")
   private String replaceStr;
   @GraphQLField(kkhtml = "KFieldText", label = "表格字段名", sql = "column_name = $S{columnName}" ,field = "column_name")
   private String columnName;
   @GraphQLField(kkhtml = "KFieldText", label = "字段顺序", sql = "row_order = $S{rowOrder}" ,field = "row_order")
   private String rowOrder;
   @GraphQLField(kkhtml = "KFieldText", label = "字段顺序", sql = "column_order = $S{columnOrder}" ,field = "column_order")
   private String columnOrder;
   @GraphQLField(kkhtml = "KFieldText", label = "取值表名", sql = "value_table_name = $S{valueTableName}" ,field = "value_table_name")
   private String valueTableName;
   @GraphQLField(kkhtml = "KFieldText", label = "取值字段代码", sql = "value_column_code = $S{valueColumnCode}" ,field = "value_column_code")
   private String valueColumnCode;
   @GraphQLField(kkhtml = "KFieldText", label = "表格字段名", sql = "exeid = $S{exeid}" ,field = "exeid")
   private String exeid;
   @GraphQLField(kkhtml = "KFieldText", label = "合并水平单元格数", sql = "merge_row_num = $S{mergeRowNum}" ,field = "merge_row_num")
   private String mergeRowNum;
   @GraphQLField(kkhtml = "KFieldText", label = "合并垂直单元格数", sql = "merge_column_num = $S{mergeColumnNum}" ,field = "merge_column_num")
   private String mergeColumnNum;
   
  	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
  	public String getDisclosureModVersionId() {
        return disclosureModVersionId;
    }

    public void setDisclosureModVersionId(String disclosureModVersionId) {
        this.disclosureModVersionId = disclosureModVersionId;
    }
    public String getDisclosureType() {
        return disclosureType;
    }

    public void setDisclosureType(String disclosureType) {
        this.disclosureType = disclosureType;
    }
    public String getDisclosureSonType() {
        return disclosureSonType;
    }

    public void setDisclosureSonType(String disclosureSonType) {
        this.disclosureSonType = disclosureSonType;
    }
  	public String getReplaceStr() {
        return replaceStr;
    }

    public void setReplaceStr(String replaceStr) {
        this.replaceStr = replaceStr;
    }
  	public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
  	public String getRowOrder() {
        return rowOrder;
    }

    public void setRowOrder(String rowOrder) {
        this.rowOrder = rowOrder;
    }
  	public String getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(String columnOrder) {
        this.columnOrder = columnOrder;
    }
  	public String getValueTableName() {
        return valueTableName;
    }

    public void setValueTableName(String valueTableName) {
        this.valueTableName = valueTableName;
    }
  	public String getValueColumnCode() {
        return valueColumnCode;
    }

    public void setValueColumnCode(String valueColumnCode) {
        this.valueColumnCode = valueColumnCode;
    }
  	public String getExeid() {
        return exeid;
    }

    public void setExeid(String exeid) {
        this.exeid = exeid;
    }
  	public String getMergeRowNum() {
        return mergeRowNum;
    }

    public void setMergeRowNum(String mergeRowNum) {
        this.mergeRowNum = mergeRowNum;
    }
  	public String getMergeColumnNum() {
        return mergeColumnNum;
    }

    public void setMergeColumnNum(String mergeColumnNum) {
        this.mergeColumnNum = mergeColumnNum;
    }

}