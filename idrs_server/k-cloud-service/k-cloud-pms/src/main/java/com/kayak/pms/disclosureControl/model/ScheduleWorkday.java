package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "scheduleWorkdayService", table = "sys_workday_set")
@Data
public class ScheduleWorkday {

    @GraphQLField(key = true, kkhtml = "KFieldText", label = "方案编号", sql = "pgmno = $S{pgmno}", field = "pgmno")
    private String pgmno;

    @GraphQLField(key = true, kkhtml = "KFieldText", label = "工作日期", sql = "workday = $S{workday}", field = "workday")
    private String workday;

    @GraphQLField
    private String maxWorkday;

    @GraphQLField
    private String minWorkday;
    
    
    @GraphQLField
    private String prodId;
}
