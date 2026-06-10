package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * 产品创意实体类
 */
@Data
@GraphQLModel(fetcher = "prodOriginalityService", table = "t8_prod_creative_project")
public class ProdOriginality {
    @GraphQLField(label = "id")
    private String id;
    @GraphQLField(label = "seminar_id")
    private String seminarId;
    @GraphQLField(label = "originality_name")
    private String originalityName;
    @GraphQLField(label = "originality_type")
    private String originalityType;
    @GraphQLField(label = "inventor")
    private String inventor;
    @GraphQLField(label = "status")
    private String status;
    @GraphQLField(label = "update_date")
    private String updateDate;
    @GraphQLField(label = "update_time")
    private String updateTime;
    @GraphQLField(label = "inputuser")
    private String inputuser;

}
