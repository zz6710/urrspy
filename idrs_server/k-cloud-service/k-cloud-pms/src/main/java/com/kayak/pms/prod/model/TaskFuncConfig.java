package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//产品流程功能配置
@GraphQLModel(fetcher = "taskFuncConfigService")
@Data
public class TaskFuncConfig {

    @GraphQLField(label = "id",field = "id")
    private String id;//ID
    @GraphQLField(kkhtml = "KFieldText", kkhtmlDefault = true, label = "功能名称",field = "name", sql = "name like '%$U{name}%'")
    private String name;//功能名称
    @GraphQLField(label = "功能指向路径",field = "url")
    private String url;//功能指向路径
    @GraphQLField(label = "准入检查语句",field = "entry_checksql")
    private String entryChecksql;//准入检查语句
    @GraphQLField(label = "准入检查条件",field = "entry_conditions")
    private String entryConditions;//准入检查条件
    @GraphQLField(label = "检查语句",field = "checksql")
    private String checksql;//检查语句
    @GraphQLField(label = "判断条件",field = "conditions")
    private String conditions;//判断条件
    @GraphQLField(label = "需要隐藏的按钮",field = "hide_button_ids")
    private String hideButtonIds;
    @GraphQLField
    private String prod_code;//产品代码
    private String json;//流程图json字符串
    private String node_id;//流程节点ID
    private String t8_task_func_info_id;//功能ID
    private String t8_prod_task_nodes_info_id;//任务节点ID
    private String wf_flow_template_item_id;//流程模板ID
    @GraphQLField
    private String wf_flow_template_id;//流程模板ID
    private String roleid;
    private String rolename;


    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getWf_flow_template_id() {
        return wf_flow_template_id;
    }

    public void setWf_flow_template_id(String wf_flow_template_id) {
        this.wf_flow_template_id = wf_flow_template_id;
    }

    public String getWf_flow_template_item_id() {
        return wf_flow_template_item_id;
    }

    public void setWf_flow_template_item_id(String wf_flow_template_item_id) {
        this.wf_flow_template_item_id = wf_flow_template_item_id;
    }

    public String getT8_prod_task_nodes_info_id() {
        return t8_prod_task_nodes_info_id;
    }

    public void setT8_prod_task_nodes_info_id(String t8_prod_task_nodes_info_id) {
        this.t8_prod_task_nodes_info_id = t8_prod_task_nodes_info_id;
    }


    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public String getT8_task_func_info_id() {
        return t8_task_func_info_id;
    }

    public void setT8_task_func_info_id(String t8_task_func_info_id) {
        this.t8_task_func_info_id = t8_task_func_info_id;
    }

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChecksql() {
        return checksql;
    }

    public void setChecksql(String checksql) {
        this.checksql = checksql;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
}
