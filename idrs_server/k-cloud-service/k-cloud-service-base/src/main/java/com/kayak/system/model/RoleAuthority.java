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
public class RoleAuthority {

    @GraphQLField(field = "id")
    private String id;

    @GraphQLField(field = "parentId")
    private String parentId;

    @GraphQLField(field = "type")
    private String type;

    @GraphQLField(field = "name")
    private String name;

}


