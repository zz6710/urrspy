package com.kayak.pms.printTemp.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @program: k-cloud
 * @description: 静态模板类
 * @author: WangZhenXin
 * @create: 2021-01-02 10:05
 * @memo 备注信息
 */
@Data
@GraphQLModel(fetcher = "staticTempService",table = "t8_static_temp")
public class StaticTemp {
    @GraphQLField(key = true, sql = "id = $S{id}", field = "id")
    private String id;

    @GraphQLField(sql = " temp_name like '%$U{tempName}%' ", field = "temp_name")
    private String tempName;

    @GraphQLField(sql = " temp_type = $S{tempType} ", field = "temp_type")
    private String tempType;

    @GraphQLField(sql = " doc_type = $S{docType} ", field = "doc_type")
    private String docType;

    @GraphQLField(sql = " remark like '%$U{remark}%' ", field = "remark")
    private String remark;

    @GraphQLField(sql = " distributor_code = $S{distributorCode} ", field = "distributor_code")
    private String distributorCode;

    @GraphQLField(sql = " t8_trutee_info_id = $S{t8TruteeInfoId} ", field = "t8_trutee_info_id")
    private String t8TruteeInfoId;
    
    private String distributorTruteeName;

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
}
