package com.kayak.pms.printTemp.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @program: k-cloud
 * @description: 文档模板版本
 * @author: WangZhenXin
 * @create: 2020-12-28 09:32
 * @memo 备注信息
 */

@Data
@GraphQLModel(fetcher = "printTempVersionService",table = "t8_print_temp_version")
public class PrintTempVersion {

    @GraphQLField(key = true, sql = "id in ($U{id})", field = "id")
    private String id;

    @GraphQLField(sql = " t8_print_temp_id = $S{t8PrintTempId} ", field = "t8_print_temp_id")
    private String t8PrintTempId;

    @GraphQLField(sql = " version = $S{version} ", field = "version")
    private String version;

    @GraphQLField(sql = " temp_name = $S{tempName} ", field = "temp_name")
    private String tempName;

    @GraphQLField(sql = " temp_html = $S{tempHtml} ", field = "temp_html")
    private String tempHtml;
    
    @GraphQLField
    private String remark;

    @GraphQLField(sql = " create_date = $S{createDate} ", field = "create_date")
    private String createDate;

    @GraphQLField(sql = " create_time = $S{createTime} ", field = "create_time")
    private String createTime;

    @GraphQLField(sql = " update_date = $S{updateDate} ", field = "update_date")
    private String updateDate;

    @GraphQLField(sql = " create_user_id = $S{createUserId} ", field = "create_user_id")
    private String createUserId;

    @GraphQLField(sql = " create_user_name = $S{createUserName} ", field = "create_user_name")
    private String createUserName;

    @GraphQLField(sql = " status = $S{status} ", field = "status")
    private String status;



    @GraphQLField(sql = " temp_type = $S{tempType} ", field = "temp_type")
    private String tempType;

    @GraphQLField(sql = " doc_version = $S{docVersion} ", field = "doc_version")
    private String docVersion;

    @GraphQLField(sql = " distributor_code = $S{distributorCode} ", field = "distributor_code")
    private String distributorCode;

    @GraphQLField(field="processInstanceId")
    private String processInstanceId;

    @GraphQLField(field="effectiveDate")
    private String effectiveDate;
    @GraphQLField(field="expirationDate")
    private String expirationDate;
    
    @GraphQLField(field="risk_num")
    private String riskNum;

}
