package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "deptService", table = "sys_dept")
public class Dept {

    @GraphQLField(sql = "deptno != $S{excDeptno}", field = "deptno")
    private String excDeptno;

    @GraphQLField(key = true, kkhtml = "KFieldText", label = "部门代码", kkhtmlDefault = true, sql = "deptno LIKE '%$U{deptno}%'", field = "deptno", kkhtmlExt = "{\"data-max-length\":20}")
    private String deptno;

    @GraphQLField(sql = "deptid = $S{deptid}", field = "deptid")
    private String deptid;

    @GraphQLField(kkhtml = "KFieldText", label = "部门名称",  kkhtmlDefault = true,sql = "deptname LIKE '%$U{deptname}%'", field = "deptname", kkhtmlExt = "{\"data-max-length\":256}")
    private String deptname;

    @GraphQLField(label = "部门级别",sql = "deptlevel = $S{deptlevel}", field = "deptlevel")
    private String deptlevel;

    @GraphQLField(sql = "depttype = $S{depttype}", field = "depttype")
    private String depttype;

    @GraphQLField
    private String parentdeptno;

    @GraphQLField
    private String address;

    @GraphQLField
    private String contect;

    @GraphQLField
    private String telno;
    
    
    @GraphQLField(kkhtml = "KFieldText", label = "上级部门代码",sql = "parentdeptno in  (select deptno  from sys_dept where deptname LIKE '%$U{parentDeptName}%' )", field = "parentDeptName", kkhtmlExt = "{\"data-max-length\":20}")
    private String parentDeptName;

    @GraphQLField(kkhtml = "KFieldSelect", label = "部门状态", kkhtmlExt="{\"data-dict\": \"dept_status\"}",
            sql = "dept_status = $S{deptStatus}", field = "dept_status")
    private String deptStatus;

    public Integer count;
}
