package com.kayak.jimureport.report.entity;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "jmReportService")
public class JmReportMenuInfo {

    @GraphQLField(label = "报表菜单ID" ,field = "menuid")
    private String menuid;

    @GraphQLField(label = "报表菜单名" ,field = "menuname")
    private String menuname;

    @GraphQLField(label = "上级菜单ID" ,field = "upperid")
    private String upperid;

    @GraphQLField(label = "用于连接积木报表系统" ,field = "jimu_report_id")
    private String jimuReportId;

}
