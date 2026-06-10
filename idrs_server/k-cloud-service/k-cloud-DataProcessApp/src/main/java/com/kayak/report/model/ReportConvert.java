package com.kayak.report.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "reportConvertService",table = "base_rpt_convert_info")
@Data
public class ReportConvert {

    @GraphQLField(kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
    private String id;

    @GraphQLField(kkhtml = "KFieldText", label = "报表转换ID", sql = "report_id = $S{reportId}" ,field = "report_id")
    private String reportId;

    @GraphQLField(kkhtml = "KFieldText", label = "报表名称", field = "report_name")
    private String reportName;

    @GraphQLField(kkhtml = "KFieldText", label = "操作员编号", sql = "operaterno = $S{operaterno}" ,field = "operaterno")
    private String operaterno;

    @GraphQLField(kkhtml = "KFieldText", label = "操作员名称", sql = "operatername = $S{operatername}" ,field = "operatername")
    private String operatername;

    @GraphQLField(kkhtml = "KFieldText", label = "转换日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
    private String crtDate;

    @GraphQLField(kkhtml = "KFieldText", label = "转换时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;

    @GraphQLField(kkhtml = "KFieldText", label = "上传的文件名", sql = "up_filename = $S{upFilename}" ,field = "up_filename")
    private String upFilename;

    @GraphQLField(kkhtml = "KFieldText", label = "转换后的文件名", sql = "convert_filename = $S{convertFilename}" ,field = "convert_filename")
    private String convertFilename;

    @GraphQLField(kkhtml = "KFieldText", label = "转换后的存储路径", sql = "convert_filepath = $S{convertFilepath}" ,field = "convert_filepath")
    private String convertFilepath;
}
