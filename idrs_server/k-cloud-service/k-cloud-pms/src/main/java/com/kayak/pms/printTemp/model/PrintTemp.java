package com.kayak.pms.printTemp.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @program: k-cloud
 * @description: 文档模板信息
 * @author: WangZhenXin
 * @create: 2020-12-26 11:22
 * @memo 备注信息
 */
@Data
@GraphQLModel(fetcher = "printTempService",table = "t8_print_temp")
public class PrintTemp {

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

    @GraphQLField(sql = " t8_meet_create_id = $S{t8MeetCreateId} ", field = "t8_meet_create_id")
    private String t8MeetCreateId;

    @GraphQLField(sql = " create_date = $S{createDate} ", field = "create_date")
    private String createDate;

    @GraphQLField(sql = " update_date = $S{updateDate} ", field = "update_date")
    private String updateDate;

    @GraphQLField(field="processInstanceId")
    private String processInstanceId;

    @GraphQLField
    private String status;

    @GraphQLField
    private String tempVersionUpdateDate;

    @GraphQLField
    private String tempVersionUpdateTime;

}
