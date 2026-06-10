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
public class RoleAuthoritySave {

    @GraphQLField(field = "id")
    private String id;

    @GraphQLField(field = "type")
    private String type;

    @GraphQLField(field = "roleId")
    private String roleId;

}


