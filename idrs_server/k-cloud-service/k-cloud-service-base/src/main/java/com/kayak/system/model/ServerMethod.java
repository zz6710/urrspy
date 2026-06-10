package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@GraphQLModel(fetcher = "serverMethodService", table = "sys_server_method")
public class ServerMethod {

    private String server;
    private String upper;
    private String name;
    private String modelName;

    @GraphQLField(field = "type", sql = "type = $S{type}")
    private String type;
    private String appName;
    private String needAuth;
    private String serverDesc;
    private String serverParams;

    /**
     * @see com.kayak.aspect.annotations.APIOperation
     */
    private String operation;

}


