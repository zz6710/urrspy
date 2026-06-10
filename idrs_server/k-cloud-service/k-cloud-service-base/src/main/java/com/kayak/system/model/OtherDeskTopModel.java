package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "otherDeskTopService", table = "")
@Data
public class OtherDeskTopModel {
    @GraphQLField(key = true , label = "提醒唯一标识" ,field = "keyword")
    private String keyword;
    @GraphQLField(label = "源表名称" ,field = "source_table")
    private String sourceTable;
    @GraphQLField(label = "数据日期" ,field = "deal_date")
    private String dealDate;
    @GraphQLField(label = "处理状态" ,field = "remind_msg")
    private String remindMsg;
    @GraphQLField(label = "办结状态" ,field = "remind_status")
    private String remindStatus;
    @GraphQLField(label = "旧数据" ,field = "old_data")
    private String oldData;
    @GraphQLField(label = "新数据" ,field = "new_data")
    private String newData;
    @GraphQLField(label = "备注信息" ,field = "remark")
    private String remark;
    @GraphQLField(label = "办结日期" ,field = "crt_date")
    private String crtDate;
    @GraphQLField(label = "办结时间" ,field = "crt_time")
    private String crtTime;
    @GraphQLField
    private String startDate;
    @GraphQLField
    private String endDate;
}
