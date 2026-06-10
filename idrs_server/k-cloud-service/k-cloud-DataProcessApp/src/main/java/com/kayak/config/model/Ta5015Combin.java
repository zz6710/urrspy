package com.kayak.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: lfzh
 * @date: 2021-01-26 19:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@GraphQLModel(fetcher = "ta5015CombinService")
public class Ta5015Combin {

    @GraphQLField(label = "清算组集合", field = "clearGroupListStr")
    private List<Ta5015> clearGroupListStr;
}
