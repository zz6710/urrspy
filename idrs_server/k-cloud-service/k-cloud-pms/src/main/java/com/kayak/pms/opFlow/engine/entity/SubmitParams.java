package com.kayak.pms.opFlow.engine.entity;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 27/06/2017.
 */
@Data
@Alias("submitParams")
@GraphQLModel(fetcher = "submitParamsService")
public class SubmitParams {

    @GraphQLField
    private String taskId;

    private String id;
    private String processId;
    private String processInstanceId;
    private String submitParams;
    private String createDate;
    private String createTime;

}
