package com.kayak.method.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "methodService", table = "sys_server_method")
public class Method {

    @GraphQLField(key = true,label = "服务", sql = "server = $S{server}", field = "server")
    private String server;

    @GraphQLField(label = "上级服务", sql = "upper = $S{upper}", field = "upper")
    private String upper;

    @GraphQLField(label = "服务名", sql = "name = $S{name}", field = "name")
    private String name;

    @GraphQLField(label = "模型名称", sql = "model_name = $S{model_name}", field = "model_name")
    private String modelName;

    @GraphQLField(label = "类型", sql = "type = $S{type}", field = "type")
    private String type;

    @GraphQLField(label = "服务名", sql = "app_name = $S{app_name}", field = "app_name")
    private String appName;

    @GraphQLField(label = "是否需要授权", sql = "need_auth = $S{need_auth}", field = "need_auth")
    private String needAuth;

    @GraphQLField(label = "备注", sql = "server_desc = $S{server_desc}", field = "server_desc")
    private String serverDesc;

    @GraphQLField(label = "服务参数", sql = "server_params = $S{server_params}", field = "server_params")
    private String serverParams;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUpper() {
        return upper;
    }

    public void setUpper(String upper) {
        this.upper = upper;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getNeedAuth() {
        return needAuth;
    }

    public void setNeedAuth(String needAuth) {
        this.needAuth = needAuth;
    }

    public String getServerDesc() {
        return serverDesc;
    }

    public void setServerDesc(String serverDesc) {
        this.serverDesc = serverDesc;
    }

    public String getServerParams() {
        return serverParams;
    }

    public void setServerParams(String serverParams) {
        this.serverParams = serverParams;
    }
}
