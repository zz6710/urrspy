package com.kayak.model.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "modelService", table = "sys_server_model")
public class Model {

    @GraphQLField(key = true,label = "模型名", sql = "model_name = $S{model_name}", field = "model_name")
    private String modelName;

    @GraphQLField(label = "应用名", sql = "app_name = $S{app_name}", field = "app_name")
    private String appName;

    @GraphQLField(label = "完整类名", sql = "model_full_name = $S{model_full_name}", field = "model_full_name")
    private String modelFullName;

    @GraphQLField(label = "字段", sql = "model_field = $S{model_field}", field = "model_field")
    private String modelField;

    @GraphQLField(label = "服务名", sql = "server_name = $S{server_name}", field = "server_name")
    private String serverName;

    @GraphQLField(label = "是否加密", sql = "is_encrypt = $S{is_encrypt}", field = "is_encrypt")
    private String isEncrypt;

    @GraphQLField(label = "加密字段", sql = "encrypt_field = $S{encrypt_field}", field = "encrypt_field")
    private String encryptField;

    @GraphQLField(label = "主键字段", sql = "model_keys = $S{model_keys}", field = "model_keys")
    private String modelKeys;

    @GraphQLField(label = "模型名称", sql = "model_label = $S{model_label}", field = "model_label")
    private String modelLabel;

    @GraphQLField(label = "模型表名", sql = "model_table = $S{model_table}", field = "model_table")
    private String modelTable;

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

    public String getModelFullName() {
        return modelFullName;
    }

    public void setModelFullName(String modelFullName) {
        this.modelFullName = modelFullName;
    }

    public String getModelField() {
        return modelField;
    }

    public void setModelField(String modelField) {
        this.modelField = modelField;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getIsEncrypt() {
        return isEncrypt;
    }

    public void setIsEncrypt(String isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public String getEncryptField() {
        return encryptField;
    }

    public void setEncryptField(String encryptField) {
        this.encryptField = encryptField;
    }

    public String getModelKeys() {
        return modelKeys;
    }

    public void setModelKeys(String modelKeys) {
        this.modelKeys = modelKeys;
    }

    public String getModelLabel() {
        return modelLabel;
    }

    public void setModelLabel(String modelLabel) {
        this.modelLabel = modelLabel;
    }

    public String getModelTable() {
        return modelTable;
    }

    public void setModelTable(String modelTable) {
        this.modelTable = modelTable;
    }
}
