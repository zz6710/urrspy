package com.kayak.pms.printTemp.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @program: k-cloud
 * @description: 静态模板版本控制对象
 * @author: WangZhenXin
 * @create: 2021-01-02 10:08
 * @memo 备注信息
 */

@Data
@GraphQLModel(fetcher = "staticTempVersionService",table = "t8_static_temp_version")
public class StaticTempVersion {

    @GraphQLField(key = true, sql = "id = $S{id}", field = "id")
    private String id;

    @GraphQLField(sql = " t8_static_temp_id = $S{t8StaticTempId} ", field = "t8_static_temp_id")
    private String t8StaticTempId;

    @GraphQLField(sql = " version = $S{version} ", field = "version")
    private String version;

    @GraphQLField(sql = " file_path = $S{filePath} ", field = "file_path")
    private String filePath;

    @GraphQLField(sql = " temp_name = $S{tempName} ", field = "temp_name")
    private String tempName;

    private String remark;

    @GraphQLField(sql = " create_date = $S{createDate} ", field = "create_date")
    private String createDate;

    @GraphQLField(sql = " create_time = $S{createTime} ", field = "create_time")
    private String createTime;

    @GraphQLField(sql = " update_date = $S{updateDate} ", field = "update_date")
    private String updateDate;

    @GraphQLField(sql = " update_time = $S{updateTime} ", field = "update_time")
    private String updateTime;

    @GraphQLField(sql = " create_user_id = $S{createUserId} ", field = "create_user_id")
    private String createUserId;

    @GraphQLField(sql = " create_user_name = $S{createUserName} ", field = "create_user_name")
    private String createUserName;

    @GraphQLField(sql = " status = $S{status} ", field = "status")
    private String status;

}
