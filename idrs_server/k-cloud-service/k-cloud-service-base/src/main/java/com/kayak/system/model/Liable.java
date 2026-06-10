package com.kayak.system.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "liableService", table = "sys_liable")
@Data
public class Liable {


    @GraphQLField(sql = "liableid = $S{liableid}", field = "liableid")
    private String liableid;

    @GraphQLField(sql = "deptno LIKE '%$U{deptno}%'", field = "deptno")
    private String deptno;

    @GraphQLField(kkhtml = "KFieldText", label = "部门名称", kkhtmlExt = "{\"data-max-length\":100}",  kkhtmlDefault = true,sql = "deptname LIKE '%$U{deptname}%'", field = "deptname")
    private String deptname;

    @GraphQLField(kkhtml = "KFieldText" ,label = "部门责任人", kkhtmlExt = "{\"data-max-length\":16}", kkhtmlDefault = true, sql = "username LIKE '%$U{username}%'",  field = "username" )
    private String username;

    @GraphQLField(kkhtml = "KFieldText", label = "分管领导",kkhtmlExt = "{\"data-max-length\":16}", kkhtmlDefault = true,sql = "leadername LIKE '%$U{leadername}%'", field = "leadername")
    private String leadername;

    @GraphQLField
    private String remarks;

    @GraphQLField(sql = "userid = $S{userid}", field = "userid")
    private String userid;

    @GraphQLField
    private String leaderid;
}
