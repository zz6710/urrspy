package com.kayak.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @author: lfzh
 * @date: 2020-12-24 14:29
 */
@Data
@GraphQLModel(fetcher = "ta5014Service")
public class Ta5015GroupList {

    @GraphQLField(label = "清算批次" ,field = "task_group")
    private String taskGroup;

    @GraphQLField(label = "依赖的上一个清算任务组", field = "last_task_group")
    private String lastTaskGroup;

}
