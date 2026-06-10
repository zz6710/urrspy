package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * com.kayak.pms.prod.model
 * user:rennannan
 * date:2021/3/15 14:09
 * function:产品进度表实体
 */
@GraphQLModel(fetcher = "prodScheduleService",table = "t8_prod_schedule_info")
@Data
public class ProdSchedule {

    @GraphQLField(label = "id",field = "id",sql="id=$S{id}")
    private String id;
    @GraphQLField(label = "产品id",field = "t8_prod_info_id",sql="t8_prod_info_id=$S{t8ProdInfoId}")
    private String t8ProdInfoId;
    @GraphQLField(label = "产品代码",field = "prod_code",sql="prod_code=$S{prodCode}")
    private String prodCode;
    @GraphQLField(label = "产品节点",field = "node_id",sql="node_id=$S{nodeId}")
    private String nodeId;
    @GraphQLField(label = "业务日期",field = "business_date",sql="business_date=$S{businessDate}")
    private String businessDate;
    @GraphQLField(label = "操作日期",field = "crt_date",sql="crt_date=$S{crtDate}")
    private String crtDate;
    @GraphQLField(label = "操作时间",field = "crt_time",sql="crt_time=$S{crtTime}")
    private String crtTime;
    @GraphQLField(label = "操作人",field = "crt_user",sql="crt_user=$S{crtUser}")
    private String crtUser;
    @GraphQLField(label = "修改日期",field = "upd_date",sql="upd_date=$S{updDate}")
    private String updDate;
    @GraphQLField(label = "修改时间",field = "upd_time",sql="upd_time=$S{updTime}")
    private String updTime;
    @GraphQLField(label = "修改人",field = "upd_user",sql="upd_user=$S{updUser}")
    private String updUser;

}
