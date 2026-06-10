package com.kayak.pms.opFlow.engine.entity;

import com.kayak.graphql.annotation.GraphQLField;
import lombok.Data;

import java.util.List;

/**
 * Created by daniel on 13/04/2017.
 */
@Data
public class Approval {
    @GraphQLField(label = "审批id", field = "id")
    private String id;

    @GraphQLField
    private String result;
    @GraphQLField
    private String opinion;
    @GraphQLField
    private String processId;
    @GraphQLField
    private String processInstanceId;
    @GraphQLField
    private String taskId;
    @GraphQLField
    private String operator;
    private String createDate;
    private String createTime;
    private String modifiedData;
    @GraphQLField
    private String type;
    @GraphQLField
    private String taskDisplayName;
    @GraphQLField
    private String taskName;
    @GraphQLField
    private String resultText;
    @GraphQLField
    private String submitParamsId;
    @GraphQLField
    private String extraData;
    /**
     * 是否代理类型
     */
    @GraphQLField
    private Boolean surrogate;
    @GraphQLField
    private List<String> fieldNames;
}
