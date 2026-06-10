package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "reportTimeConfigInfoImportService",table = "report_time_type_info")
public class ReportTimeConfigInfo {

    @GraphQLField(kkhtml = "KFieldText", label = "报表大类")
    private String reportType;

    @GraphQLField(kkhtml = "KFieldText", label = "报表名称")
    private String reportTable;

    @GraphQLField(kkhtml = "KFieldText", label = "基准日期")
    private String baseType;

    @GraphQLField(kkhtml = "KFieldText", label = "1月")
    private String month01;

    @GraphQLField(kkhtml = "KFieldText", label = "2月")
    private String month02;

    @GraphQLField(kkhtml = "KFieldText", label = "3月")
    private String month03;

    @GraphQLField(kkhtml = "KFieldText", label = "4月")
    private String month04;

    @GraphQLField(kkhtml = "KFieldText", label = "5月")
    private String month05;

    @GraphQLField(kkhtml = "KFieldText", label = "6月")
    private String month06;

    @GraphQLField(kkhtml = "KFieldText", label = "7月")
    private String month07;

    @GraphQLField(kkhtml = "KFieldText", label = "8月")
    private String month08;

    @GraphQLField(kkhtml = "KFieldText", label = "9月")
    private String month09;

    @GraphQLField(kkhtml = "KFieldText", label = "10月")
    private String month10;

    @GraphQLField(kkhtml = "KFieldText", label = "11月")
    private String month11;

    @GraphQLField(kkhtml = "KFieldText", label = "12月")
    private String month12;
}
