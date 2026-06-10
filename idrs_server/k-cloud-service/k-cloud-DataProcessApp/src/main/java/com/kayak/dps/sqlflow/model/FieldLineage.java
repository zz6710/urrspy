package com.kayak.dps.sqlflow.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.Objects;

@GraphQLModel(fetcher = "fieldLineageService",table = "rms_table_lineage_dependency")
@Data
public class FieldLineage {

   @GraphQLField(kkhtml = "KFieldText", label = "目标表id", sql = "to_table_info_id = $S{toTableInfoId}" ,field = "to_table_info_id")
   private String toTableInfoId;
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "目标字段id", sql = "to_table_field_id = $S{toTableFieldId}" ,field = "to_table_field_id")
   private String toTableFieldId;
   @GraphQLField(kkhtml = "KFieldText", label = "源表id", sql = "from_table_info_id = $S{fromTableInfoId}" ,field = "from_table_info_id")
   private String fromTableInfoId;
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "源字段id", sql = "from_table_field_id = $S{fromTableFieldId}" ,field = "from_table_field_id")
   private String fromTableFieldId;
   @GraphQLField(kkhtml = "KFieldText", label = "全依赖关系（全字段关系）", sql = "all_dependency = $S{allDependency}" ,field = "all_dependency")
   private String allDependency;
   @GraphQLField(kkhtml = "KFieldText", label = "全上级字段", sql = "all_superior = $S{allSuperior}" ,field = "all_superior")
   private String allSuperior;
   @GraphQLField(kkhtml = "KFieldText", label = "手工维护标志（0系统1手工）", sql = "manual_flag = $S{manualFlag}" ,field = "manual_flag")
   private String manualFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "生成顺序", sql = "sequence = $S{sequence}" ,field = "sequence")
   private String sequence;
   @GraphQLField
   private String toFieldComment;
   @GraphQLField
   private String fromFieldComment;


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      FieldLineage that = (FieldLineage) o;
      return Objects.equals(toTableFieldId, that.toTableFieldId) && Objects.equals(fromTableFieldId, that.fromTableFieldId);
   }

   @Override
   public int hashCode() {
      return Objects.hash(toTableFieldId, fromTableFieldId);
   }
}