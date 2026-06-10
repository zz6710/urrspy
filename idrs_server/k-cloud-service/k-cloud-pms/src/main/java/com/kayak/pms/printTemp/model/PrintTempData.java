package com.kayak.pms.printTemp.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @program: k-cloud
 * @description: 文档模板数据源实体
 * @author: WangZhenXin
 * @create: 2020-12-29 08:59
 * @memo 备注信息
 */

@Data
@GraphQLModel(fetcher = "printTempDataService",table = "t8_print_temp_data")
public class PrintTempData {

    @GraphQLField(key = true, sql = "id = $S{id}", field = "id")
    private String id;

    @GraphQLField(sql = " doc_type = $S{docType} ", field = "doc_type")
    private String docType;

    @GraphQLField(sql = " temp_type = $S{tempType} ", field = "temp_type")
    private String tempType;

    @GraphQLField(sql = " data_type = $S{dataType} ", field = "data_type")
    private String dataType;

    @GraphQLField(sql = "data_group_name = $S{dataGroupName} ", field = "dataGroupName")
    private String dataGroupName;

    @GraphQLField(sql = "sql_info = $S{sqlInfo} ", field = "sql_info")
    private String sqlInfo;

    @GraphQLField(sql = "xp_doc_type = $S{xpDocType} ", field = "xp_doc_type")
    private String xpDocType;

    @GraphQLField(sql = "is_xp_data = $S{isXpData} ", field = "is_xp_data")
    private String isXpData;

    @GraphQLField(field = "create_date")
    private String createDate;

    @GraphQLField(field = "crt_time")
    private String crtTime;

    @GraphQLField(field = "update_date")
    private String updateDate;

    @GraphQLField( field = "upd_time")
    private String updTime;

    @GraphQLField( field = "crt_user")
    private String crtUser;

    @GraphQLField( field = "upd_user")
    private String updUser;

}
