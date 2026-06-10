package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * com.kayak.pms.prod.model
 * user:rennannan
 * date:2021/2/19 13:54
 * function:产品状态调整信息记录
 */
@GraphQLModel(fetcher = "prodStatusChangeService",table = "t8_prod_adjust")
@Data
public class ProdStatusChange {
    //id
    @GraphQLField(label = "id",sql = "id=$S{id}",field = "id")
    private String id;
    //产品id
    @GraphQLField(label = "产品id",sql = "t8_prod_info_id=$S{t8ProdInfoId}",field = "t8_prod_info_id")
    private String t8ProdInfoId;
    //产品代码
    @GraphQLField(label = "产品代码",sql = "prod_code=$S{prodCode}",field = "prod_code")
    private String prodCode;
    //调整类型 1发行失败 2产品终止
    @GraphQLField(label = "调整类型",sql = "adjust_type=$S{adjustType}",field = "adjust_type")
    private String adjustType;
    //调整原因
    @GraphQLField(label = "调整原因",sql = "adjust_cause=$S{adjustCause}",field = "adjust_cause")
    private String adjustCause;
    //操作日期
    @GraphQLField(label = "操作日期",sql = "crt_date=$S{crtDate}",field = "crt_date")
    private String crtDate;
    //操作时间
    @GraphQLField(label = "操作时间",sql = "crt_time=$S{crtTime}",field = "crt_time")
    private String crtTime;
    //操作人
    @GraphQLField(label = "操作人",sql = "crt_user=$S{crtUser}",field = "crt_user")
    private String crtUser;
    /************以下字段用于接收产品状态字段**************/
    @GraphQLField(label = "产品状态")
    private String prodStatus; //产品状态
    @GraphQLField(label = "产品子状态")
    private String prodSonStatus; //产品子状态

    @GraphQLField
    private String assemblyDesc;
    @GraphQLField
    private String prodName;
    //产品代码
    @GraphQLField(label = "原产品代码",sql = "prod_code_old=$S{prodCodeOld}",field = "prod_code_old")
    private String prodCodeOld;
    //产品代码
    @GraphQLField(label = "新产品代码",sql = "prod_code_new=$S{prodCodeNew}",field = "prod_code_new")
    private String prodCodeNew;
    //产品代码
    @GraphQLField(label = "新产品名称",sql = "prod_name_new=$S{prodNameNew}",field = "prod_name_new")
    private String prodNameNew;
    @GraphQLField(label = "是否回收代码",sql = "is_recycle=$S{isRecycle}",field = "is_recycle")
    private String isRecycle;
}
