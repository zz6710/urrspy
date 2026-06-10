package com.kayak.dps.sqlflow.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "tableLineageService",table = "rms_table_lineage")
@Data
public class TableLineage {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "exeid", sql = "exeid = $S{exeid}" ,field = "exeid")
   private String exeid;
   @GraphQLField(kkhtml = "KFieldText", label = "目标表id", sql = "to_table_info_id = $S{toTableInfoId}" ,field = "to_table_info_id")
   private String toTableInfoId;
   @GraphQLField(kkhtml = "KFieldText", label = "源表id", sql = "from_table_info_id = $S{fromTableInfoId}" ,field = "from_table_info_id")
   private String fromTableInfoId;
   @GraphQLField(kkhtml = "KFieldText", label = "手工维护标志（0系统1手工）", sql = "manual_flag = $S{manualFlag}" ,field = "manual_flag")
   private String manualFlag;

}