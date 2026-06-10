package com.kayak.dps.check.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8PortLogHandleService",table = "base_port_file_log")
public class PortLogInfoModel {
    @GraphQLField(key = true , label = "id" ,field = "id")
    private String id;
    @GraphQLField(label = "接口代码" ,field = "port_code")
    private String portCode;
    @GraphQLField(label = "接口名称" ,field = "port_name")
    private String portName;
    @GraphQLField(label = "接口类型" ,field = "port_type")
    private String portType;
    @GraphQLField(label = "接口方向" ,field = "port_dir")
    private String portDir;
    @GraphQLField(label = "处理日期" ,field = "deal_date")
    private String dealDate;
    @GraphQLField(label = "处理人" ,field = "user_name")
    private String userName;
    @GraphQLField(label = "处理状态" ,field = "file_state")
    private String fileState;
    @GraphQLField(label = "处理结果" ,field = "exec_message")
    private String execMessage;
    @GraphQLField(label = "总笔数" ,field = "total_num")
    private String totalNum;
    @GraphQLField(label = "开始日期" ,field = "crt_date")
    private String crtDate;
    @GraphQLField(label = "开始时间" ,field = "crt_time")
    private String crtTime;
    @GraphQLField(label = "结束日期" ,field = "upd_date")
    private String updDate;
    @GraphQLField(label = "结束时间" ,field = "upd_time")
    private String updTime;
    @GraphQLField(label = "数据开始日期" ,field = "start_crt_date")
    private String startCrtDate;
    @GraphQLField(label = "数据结束日期" ,field = "end_crt_date")
    private String endCrtDate;
    @GraphQLField(label = "同步开始日期" ,field = "start_deal_date")
    private String startDealDate;
    @GraphQLField(label = "同步结束日期" ,field = "end_deal_date")
    private String endDealDate;
}
