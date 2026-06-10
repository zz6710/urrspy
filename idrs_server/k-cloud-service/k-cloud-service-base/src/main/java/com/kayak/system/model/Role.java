package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.*;

/**
 * @author yinwanxiong
 * @date 2020/4/9 16:25
 * @description
 */

@GraphQLModel(fetcher = "roleService", table = "sys_role")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements Comparable<Role>{

    @EqualsAndHashCode.Include
    @GraphQLField(key = true, sql = "roleid = $S{roleid}", field = "roleid")
    private String roleid;

    @GraphQLField(kkhtml = "KFieldSelect", label = "父角色名称", sql = "parentroleid = $S{parentroleid}", field = "parentroleid", kkhtmlExt = "{\"data-action\":\"Role.findParents\",\"data-display-field\":\"rolename\",\"data-value-field\":\"roleid\"}")
    private String parentroleid;

    @GraphQLField
	private String parentRoleName;
   

    @GraphQLField(kkhtml = "KFieldText", label = "角色名称",kkhtmlDefault = true,sql = "rolename like '%$U{rolename}%'", field = "rolename")
    private String rolename;
    @GraphQLField(kkhtml = "KFieldText", label = "用户名称",kkhtmlDefault = true,sql = "username like '%$U{username}%'", field = "username")
    private String username;
    @GraphQLField(kkhtml = "KFieldText", label = "用户id",kkhtmlDefault = true,sql = "userid =$S{userid}", field = "userid")
    private String userid;

    @GraphQLField(sql = "roletype = $S{roletype}", field = "roletype")
    private String roletype;

    @GraphQLField(kkhtml = "KFieldText", label = "角色描述", kkhtmlDefault = true,sql = "descript like '%$U{descript}%'", field = "descript")
    private String descript;

    @GraphQLField
    private String roleids;
    @GraphQLField
    private String flowtemplateid;

    @GraphQLField(sql = "docTypes = $S{doc_types}", field = "docTypes")
    private String docTypes;

    @GraphQLField(sql = "docReTypes = $S{doc_re_types}", field = "docReTypes")
    private String docReTypes;

    @Override
    public int compareTo(Role o) {
        return this.getRoleid().compareTo(o.getRoleid());
    }
}
