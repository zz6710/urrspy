package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "systemAuthRoleCheckService", table = "sys_auth_role_check")
public class SystemAuthRoleCheck {
    //表字段
    @GraphQLField(key = true, sql = "id = $S{id}" ,field = "id")
    private String id;

    //表字段
    @GraphQLField(label = "角色id", sql = "roleid = $S{roleid}" ,field = "roleid")
    private String roleid;

    @GraphQLField(label = "服务方法", sql = "server = $S{server}" ,field = "server")
    private String server;

    @GraphQLField(label = "字段",sql = "field = $S{field}", field = "field")
    private String field;

    @GraphQLField(label = "字段类型",sql = "fieldtype LIKE '%$U{fieldtype}'", field = "fieldtype")
    private String fieldtype;

    @GraphQLField(label = "字段名称",sql = "fieldname LIKE '%$U{fieldname}'", field = "fieldname")
    private String fieldname;

    @GraphQLField(label = "逻辑判断符",sql = "logic = $S{logic}", field = "logic")
    private String logic;

    @GraphQLField(label = "判断值",sql = "value = $S{value}", field = "value")
    private String value;

    @GraphQLField(label = "逻辑链接符",sql = "opjoin = $S{opjoin}", field = "opjoin")
    private String opjoin;

    @GraphQLField(label = "逻辑链接符",sql = "descript like '$U{descript}'", field = "descript")
    private String descript;
}
