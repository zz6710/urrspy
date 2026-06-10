package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "systemAuthSetService", table = "sys_auth_set")
public class SystemAuthSet {
    //表字段

    @GraphQLField(key = true, label = "服务id", sql = "a.server = $S{server}" ,field = "server")
    private String server;

    @GraphQLField(key = true, label = "角色id",sql = "a.roleid = $S{roleid}", field = "roleid")
    private String roleid;

    @GraphQLField(kkhtml = "KFieldText", label = "备注", kkhtmlDefault = true, sql = "a.remark LIKE '%$U{remark}'", field = "remark")
    private String remark;

    //新增字段

    @GraphQLField(field = "servername")
    private String servername;

    @GraphQLField(field = "rolename")
    private String rolename;
}
