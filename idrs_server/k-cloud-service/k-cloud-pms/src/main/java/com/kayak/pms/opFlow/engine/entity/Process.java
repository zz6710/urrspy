package com.kayak.pms.opFlow.engine.entity;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import com.kayak.pms.opFlow.engine.model.ProcessModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程定义实体
 * Created by daniel on 20/03/2017.
 */
@Data
@GraphQLModel(fetcher = "processService",table = "opf_process")
public class Process implements Serializable {
    private static final long serialVersionUID = -2797429216098306855L;
    @GraphQLField(label = "流程id")
    private String processId;
    @GraphQLField(label = "版本号")
    private Integer version;
    @GraphQLField(label = "流程英文名", field="name", sql="name like '%$U{name}%'", kkhtml = "KFieldText")
    private String name;
    @GraphQLField(label = "流程中文名", field="displayName", sql="display_name like '%$U{displayName}%'", kkhtml = "KFieldText")
    private String displayName;
    @GraphQLField(label = "流程节点数据的json串")
    private String json;
    @GraphQLField(label = "操作流类型")
    private String processType;
    @GraphQLField(label = "入库或不入库")
    private String type;
    /**
     * 是否有效
     */
    private String status;
    private String deployStatus;

    /**
     * 主键ID
     */
    private String id;

    private String createUser;
    private String updateUser;

    private String createTime;

    private String updateTime;

    private String contextId;

    /**
     * 创建人
     */
    private String creator;
    /**
     * 流程定义模型
     */
    private ProcessModel processModel;

    private String envId;

    private String validateSql;
    private String validateType;

    private String creatorName;

    public void setProcessModel(ProcessModel processModel) {
        this.processModel = processModel;
        this.name = processModel.getName();
        this.displayName = processModel.getDisplayName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Process(id=").append(this.id);
        sb.append("Process(processId=").append(this.processId);
        sb.append(",name=").append(this.name);
        sb.append(",displayName=").append(this.displayName);
        sb.append(",type=").append(this.type);
        return sb.toString();
    }

}
