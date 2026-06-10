package com.kayak.pms.channelInterface.model;

import com.kayak.graphql.annotation.GraphQLField;
import lombok.Data;

/**
 * @BelongsProject: pms_server
 * @BelongsPackage: com.kayak.pms.channelInterface.model
 * @Author: wangchenglin
 * @CreateTime: 2023/03/06  10:31
 * @Description:
 * @Version: 1.0
 */
@Data
public class ChannelFileInfo {
    @GraphQLField
    private String id;
    @GraphQLField
    private String taskProdId;
    @GraphQLField
    private String interfaceNo;
    @GraphQLField
    private String status;
    @GraphQLField
    private String taskFlag;
    @GraphQLField
    private String params;
    @GraphQLField
    private String selectSql;
    @GraphQLField
    private String fullQueryCondition;
    @GraphQLField
    private String incrementalQueryCondition;
    @GraphQLField
    private String fileNameSql;
    @GraphQLField
    private String filePath;
    @GraphQLField
    private String channelSeqType;

}
