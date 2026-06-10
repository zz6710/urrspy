package com.kayak.jimureport.report.entity;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "jmSqlDictService")
public class JmSqlDictInfo {

    @GraphQLField(label = "数据源sql" ,field = "report_sql")
    private String reportSql;

    @GraphQLField(label = "菜单id" ,field = "menuid")
    private String menuid;

    @GraphQLField(label = "菜单名称" ,field = "menuname")
    private String menuname;

    @GraphQLField(label = "更新时间" ,field = "upttime")
    private String upttime;

    @GraphQLField(label = "用于连接积木报表系统" ,field = "jimu_report_id")
    private String jimuReportId;

}
