package com.kayak.flowwork.model;

import com.kayak.core.system.constants.ServerMethodType;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @author libo
 */
@Data
@GraphQLModel(fetcher = "businessConfigurationService", table = "wf_busi_config")
public class WfBusinessConfig {

    /**
     * 对应sys_server_method表的server字段
     */
    @GraphQLField(key = true, kkhtml = "KFieldSelect",
            kkhtmlExt = "{\"data-action\": \"ServerMethod.find\"," +
                    "\"data-params\": \"{type:" + ServerMethodType.CHILD + "}\"," +
                    "\"data-value-field\": \"server\"," +
                    "\"data-display-field\": \"name\"}",
            label = "业务操作", sql = "bc.server = $S{server}", field = "server")
    private String server;

    /**
     * 工作流流程名称
     */
    @GraphQLField(kkhtml = "KFieldSelect",
            kkhtmlExt = "{\"data-url\": \"/wf/process/listAllProcess.json\"," +
                    "\"data-params\": \"{start:0,limit:" + Integer.MAX_VALUE + "}\"," +
                    "\"data-value-field\": \"name\"," +
                    "\"data-display-field\": \"displayName\"}",
            label = "流程", sql = "bc.process_name = $S{processName}", field = "processName")
    private String processName;

    /**
     * 业务的数据库主键名称集合，多个主键用逗号分割
     */
    @GraphQLField(sql = "bc.bus_keys = $S{busKeys}", field = "busKeys")
    private String busKeys;

    /**
     * 业务的数据库名称
     */
    @GraphQLField(sql = "bc.table_name = $S{tableName}", field = "tableName")
    private String tableName;
    /*
     * 业务的数据库名称
     */
    @GraphQLField(sql = "bc.app_display = $S{appDisplay}", field = "appDisplay",kkhtml = "KFieldSelect",label = "是否在移动端展示",kkhtmlDefault = true, kkhtmlExt="{\"data-dict\":\"1yes0no\"}")
    private String appDisplay;

    private String serverName;

    private String processDisplayName;
    
    @GraphQLField(sql = "status = $S{status}", field = "status")
    private String status;
}
