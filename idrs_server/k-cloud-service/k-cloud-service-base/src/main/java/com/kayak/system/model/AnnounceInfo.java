package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;

@GraphQLModel(fetcher = "announceService", table = "sys_announce")
@Data
public class AnnounceInfo {

    @GraphQLField(field = "roleIds")
    private List<String> roleIds;

    @GraphQLField(field = "roleid")
    private String roleid;

    @GraphQLField(field = "annid")
    private String annid;

    @GraphQLField(field = "title")
    private String title;

    @GraphQLField(field = "content")
    private String content;

    @GraphQLField(field = "createdate")
    private String createdate;

    @GraphQLField(field = "createtime")
    private String createtime;

    @GraphQLField(field = "createuserid")
    private String createuserid;

    @GraphQLField(field = "createuserName")
    private String createuserName;

    @GraphQLField(field = "editdate")
    private String editdate;

    @GraphQLField(field = "edittime")
    private String edittime;

    @GraphQLField(field = "edituserid")
    private String edituserid;

    @GraphQLField(field = "annfilepath")
    private String annfilepath;

    @GraphQLField(field = "annfilename")
    private String annfilename;

    @GraphQLField(field = "annfilecode")
    private String annfilecode;

    @GraphQLField(field = "effectiveDate")
    private String effectiveDate;

    @GraphQLField(field = "invalidDate")
    private String invalidDate;

    @GraphQLField(field = "orgno")
    private String orgno;

}
