package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@GraphQLModel(fetcher = "roleAuthorityService")
public class RoleServer {

    @GraphQLField(sql = "roleid = $S{roleid}", field = "roleid")
    private String roleid;

    @GraphQLField(sql = "server = $S{server}", field = "server")
    private String server;

}


