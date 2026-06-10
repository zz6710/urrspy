package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@GraphQLModel(fetcher = "announceRoleService", table = "sys_announce_role")
public class AnnounceRole {

    @GraphQLField(sql = "annid = $S{annid}", field = "annid")
    private String annid;

    @GraphQLField(sql = "roleid = $S{roleid}", field = "roleid")
    private String roleid;

}
