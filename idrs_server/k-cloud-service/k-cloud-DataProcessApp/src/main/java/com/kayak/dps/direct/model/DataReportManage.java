package com.kayak.dps.direct.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "dataReportManageService",table = "app_zz_file")
public class DataReportManage {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "报备日期", sql = "workdate=$S{workdate}" ,field = "workdate")
    private String workdate;
    @GraphQLField(kkhtml = "KFieldText", label = "文件类型", sql = "filetype=$S{filetype}" ,field = "filetype")
    private String filetype;
    @GraphQLField(kkhtml = "KFieldText", label = "报文类型", sql = "msgtype=$S{msgtype}" ,field = "msgtype")
    private String msgtype;
    @GraphQLField(kkhtml = "KFieldText", label = "文件名称", sql = "origfilename=$S{origfilename}" ,field = "origfilename")
    private String origfilename;
    @GraphQLField(kkhtml = "KFieldText", label = "状态", sql = "status=$S{status}" ,field = "status")
    private String status;

    @GraphQLField(kkhtml = "KFieldText", label = "反馈文件ID", sql = "fileid=$S{fileid}" ,field = "fileid")
    private String fileid ;
    @GraphQLField(kkhtml = "KFieldText", label = "反馈文件名称", sql = "filename=$S{filename}" ,field = "filename")
    private String filename ;
    @GraphQLField(kkhtml = "KFieldText", label = "成功数量", sql = "successcount=$S{successcount}" ,field = "successcount")
    private String successcount;
    @GraphQLField(kkhtml = "KFieldText", label = "失败数量", sql = "failedcount=$S{failedcount}" ,field = "failedcount")
    private String failedcount ;
    @GraphQLField(kkhtml = "KFieldText", label = "总数量", sql = "totalcount=$S{totalcount}" ,field = "totalcount")
    private String totalcount ;
    @GraphQLField(kkhtml = "KFieldText", label = "返回码", sql = "errorcode=$S{errorcode}" ,field = "errorcode")
    private String errorcode ;
    @GraphQLField(kkhtml = "KFieldText", label = "返回信息", sql = "errortext=$S{errortext}" ,field = "errortext")
    private String errortext ;
    @GraphQLField(kkhtml = "KFieldText", label = "返回信息", sql = "crt_time=$S{crtTime}" ,field = "crt_time")
    private String crtTime ;

}