package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
public class DisclosureWorkday {

    @GraphQLField(key = true, kkhtml = "KFieldText", label = "方案编号", sql = "pgmno = $S{pgmno}", field = "pgmno")
    private String pgmno;

    @GraphQLField(key = true, kkhtml = "KFieldText", label = "工作日期", sql = "workday = $S{workday}", field = "workday")
    private String workday;

    @GraphQLField
    private String maxWorkday;

    @GraphQLField
    private String minWorkday;


    @GraphQLField(label = "产品id")
    private String prodId;
    @GraphQLField(label = "封闭起始日", field = "close_start_date")
    private String closeStartDate;
    @GraphQLField(label = "封闭结束日", field = "close_end_date")
    private String closeEndDate;

}
