package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @author yinwanxiong
 * @date 2020/4/9 16:25
 * @description
 */

@GraphQLModel(fetcher = "userRoleService", table = "sys_user_role")
@Data
public class UserRole {

    @GraphQLField(key = true, kkhtml = "defaultText", label = "用户ID", sql = "userid = $S{userid}", field = "userid")
    private String userid;

    @GraphQLField(key = true, kkhtml = "defaultText", label = "角色ID", sql = "roleid = $S{roleid}", field = "roleid")
    private String roleid;

    @GraphQLField
    private String roleids;
}
