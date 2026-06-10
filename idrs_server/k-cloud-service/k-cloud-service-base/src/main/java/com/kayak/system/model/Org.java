package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "orgService", table = "sys_org")
@Data
public class Org {

    @GraphQLField(sql = "orgno != $S{excOrgno}", field = "orgno")
    private String excOrgno;

    //匹配模糊查询
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "机构代码", sql = "orgno LIKE '%$U{orgno}%'", field = "orgno")
    private String orgno;

    /*@GraphQLField(key = true, kkhtml = "KFieldText", label = "机构代码", sql = "orgno = $S{orgno}", field = "orgno")
    private String orgno;*/

    @GraphQLField(sql = "orgid = $S{orgid}", field = "orgid")
    private String orgid;

    @GraphQLField(kkhtml = "KFieldText", label = "机构名称", sql = "orgname LIKE '%$U{orgname}%'", field = "orgname")
    private String orgname;

    @GraphQLField(kkhtml = "KFieldSelect", label = "机构级别",kkhtmlDefault = true,kkhtmlExt="{\"data-dict\": \"orglevel\"}",
            sql = "orglevel = $S{orglevel}", field = "orglevel")
    private String orglevel;

    @GraphQLField(sql = "orgtype = $S{orgtype}", field = "orgtype")
    private String orgtype;

    @GraphQLField(kkhtml = "KFieldText", label = "上级机构代码",
            sql = "parentorgno = $S{parentorgno}", field = "parentorgno")
    private String parentorgno;

    @GraphQLField
    private String address;

    @GraphQLField
    private String contect;

    @GraphQLField
    private String telno;

    @GraphQLField(kkhtml = "KFieldSelect", label = "机构状态", kkhtmlExt="{\"data-dict\": \"org_status\"}",
            sql = "org_status = $S{orgStatus}", field = "org_status")
    private String orgStatus;
}
