package com.kayak.pms.opFlow.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "opfLogService", table = "opf_log")
public class OpfLog {
    /**
     * 主键
     */
    private String id;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 操作时间
     */
    private String createDate;
    /**
     * 主流程实例
     */
    @GraphQLField(label = "流程实例", kkhtml = "KFieldText", field = "processInstanceId")
    private String processInstanceId;
    /**
     * 关联子流程实例
     */
    private String childrenProcessInstanceId;
    /**
     * 关联任务
     */
    private String taskId;
    /**
     * 功能id
     */
    private String busiId;
    /**
     * 备注描述
     */
    private String remark;
    /**
     * 任务名称
     */
    private String displayName;
    /**
     * 关联表单
     */
    private String formDataId;
    /**
     * 下一个节点
     */
    private String nextNode;
    /**
     * 操作人所属部门，用于展示
     */
    private String deptname;

    private String username;//待操作节点的人员

    @GraphQLField
    private String prodCode;//产品代码

    public OpfLog() {}
}
