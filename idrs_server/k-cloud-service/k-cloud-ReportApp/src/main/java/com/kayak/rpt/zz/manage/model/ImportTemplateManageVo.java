package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import lombok.Data;

@Data
public class ImportTemplateManageVo {
    @GraphQLField(kkhtml = "KFieldText", label = "起始行", sql = "row_start = $S{rowStart}" ,field = "row_start")
    private String rowStart; //有几行模板  起始行对应几 ，相当于list下标
    @GraphQLField(kkhtml = "KFieldText", label = "系统表名", sql = "system_table_name = $S{systemTableName}" ,field = "system_table_name")
    private String systemTableName;
}
