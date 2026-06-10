package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "workdayItemSaveService", table = "sys_workday_set")
@Data
public class WorkdayItemSave {

    @GraphQLField(kkhtml = "KFieldText", label = "方案编号", field = "pgmno")
    private String pgmno;

    @GraphQLField(kkhtml = "KFieldText", label = "工作日期", field = "workdays")
    private String workdays;

    @GraphQLField(kkhtml = "KFieldText", label = "保存的年份", field = "year")
    private String year;
}
