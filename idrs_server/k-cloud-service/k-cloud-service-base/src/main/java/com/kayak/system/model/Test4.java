package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "test4Service", table = "test4")
public class Test4 {

    @GraphQLField(sql = "id = $S{id}", field = "id")
    private String id;

    @GraphQLField(kkhtml = "KFieldText", label = "名称",sql = "name = $S{name}", field = "name")
    private String name;
}
