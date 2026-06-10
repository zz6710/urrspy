package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * 区域信息实体类
 */
@Data
@GraphQLModel(fetcher = "districtService",table = "t8_district")
public class District {
    @GraphQLField(field = "id")
    private Integer id;
    @GraphQLField(field = "pid")
    private Integer pid;
    @GraphQLField(field = "district_name")
    private String districtName;
    @GraphQLField(field = "type")
    private String type;
    @GraphQLField(field = "hierarchy")
    private String hierarchy;
    @GraphQLField(field = "district_sqe")
    private String districtSqe;

}
