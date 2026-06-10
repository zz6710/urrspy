package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "announceService", table = "sys_announce")
@Data
public class Announce {

    @GraphQLField(key = true, sql = "annid = $S{annid}", field = "annid")
    private String annid;

    @GraphQLField(kkhtml = "KFieldText", label = "公告标题", sql = "title like '%$U{title}%'", field = "title")
    private String title;

    @GraphQLField(kkhtml = "KFieldText", label = "公告内容", sql = "content like '%$U{content}%'", field = "content")
    private String content;

    @GraphQLField(sql = "createdate = $S{createdate}", field = "createdate")
    private String createdate;

    @GraphQLField(sql = "createtime = $S{createtime}", field = "createtime")
    private String createtime;

    @GraphQLField(sql = "createuserid = $S{createuserid}", field = "createuserid")
    private String createuserid;

    @GraphQLField(sql = "u.username = $S{createuserName}", field = "createuserName")
    private String createuserName;

    @GraphQLField(sql = "editdate = $S{editdate}", field = "editdate")
    private String editdate;

    @GraphQLField(sql = "edittime = $S{edittime}", field = "edittime")
    private String edittime;

    @GraphQLField(sql = "edituserid = $S{edituserid}", field = "edituserid")
    private String edituserid;

    @GraphQLField(sql = "annfilepath = $S{annfilepath}", field = "annfilepath")
    private String annfilepath;

    @GraphQLField(sql = "annfilename = $S{annfilename}", field = "annfilename")
    private String annfilename;

    @GraphQLField(sql = "annfilecode = $S{annfilecode}", field = "annfilecode")
    private String annfilecode;

    @GraphQLField(sql = "effective_date = $S{effectiveDate}", field = "effective_date")
    private String effectiveDate;

    @GraphQLField(sql = "invalid_date = $S{invalidDate}", field = "invalid_date")
    private String invalidDate;

    @GraphQLField(sql = "orgno = $S{orgno}", field = "orgno")
    private String orgno;

    @GraphQLField(sql = "process_instance_id = $S{processInstanceId}", field = "process_instance_id")
    private String processInstanceId;

    @GraphQLField(sql = "process_status = $S{processStatus}", field = "process_status")
    private String processStatus;

    @GraphQLField(sql = "data_status = $S{dataStatus}", field = "data_status")
    private String dataStatus;

}
