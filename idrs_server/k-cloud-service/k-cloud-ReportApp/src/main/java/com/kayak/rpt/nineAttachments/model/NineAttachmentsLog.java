package com.kayak.rpt.nineAttachments.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;


@Data
@GraphQLModel(fetcher = "nineAttachmentsLogService",table = "nine_attachments_file_log")
public class NineAttachmentsLog {
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
    private String prodCd;
    @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_nm = $S{prodNm}" ,field = "prod_nm")
    private String prodNm;
    @GraphQLField(kkhtml = "KFieldText", label = "文件名", sql = "zip_nm = $S{zipNm}" ,field = "zip_nm")
    private String zipNm;
    @GraphQLField(kkhtml = "KFieldText", label = "文件路径", sql = "zip_dir = $S{zipDir}" ,field = "zip_dir")
    private String zipDir;
    @GraphQLField(kkhtml = "KFieldText", label = "直连文件名", sql = "direct_zip_nm = $S{directZipNm}" ,field = "direct_zip_nm")
    private String directZipNm;
    @GraphQLField(kkhtml = "KFieldText", label = "直连文件路径", sql = "direct_zip_dir = $S{directZipDir}" ,field = "direct_zip_dir")
    private String directZipDir;
    @GraphQLField(kkhtml = "KFieldText", label = "报送日期", sql = "report_send_date = $S{reportSendDate}" ,field = "report_send_date")
    private String reportSendDate;
    @GraphQLField(kkhtml = "KFieldText", label = "报送时间", sql = "report_send_time = $S{reportSendTime}" ,field = "report_send_time")
    private String reportSendTime;
    @GraphQLField(kkhtml = "KFieldText", label = "文件获取日期", sql = "get_file_date = $S{getFileDate}" ,field = "get_file_date")
    private String getFileDate;
    @GraphQLField(kkhtml = "KFieldText", label = "文件获取时间", sql = "get_file_time = $S{getFileTime}" ,field = "get_file_time")
    private String getFileTime;
    @GraphQLField(kkhtml = "KFieldText", label = "附件状态", sql = "status = $S{status}" ,field = "status")
    private String status;
    @GraphQLField(kkhtml = "KFieldText", label = "附件状态", sql = "is_effective = $S{isEffective}" ,field = "is_effective")
    private String isEffective;

}
