package com.kayak.pms.indexInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher="desktopIndexService")
public class DesktopIndex {

    @GraphQLField
    private String rownum;
    @GraphQLField
    private String menuid;
    @GraphQLField
    private String menuname;
    @GraphQLField
    private String userid;
    @GraphQLField
    private String url;
    @GraphQLField
    private String id;
    @GraphQLField
    private String isopen;

}
