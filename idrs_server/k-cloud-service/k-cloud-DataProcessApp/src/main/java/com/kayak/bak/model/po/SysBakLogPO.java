package com.kayak.bak.model.po;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@GraphQLModel(fetcher = "bakLogAction",table = "sys_bak_log")
@Accessors(chain = true)
public class SysBakLogPO {

    /**
     * 主键id
     */
    @GraphQLField(key = true, sql = "id = $S{id}", field = "id")
    private String id;


    /**
     * 归档配置表id
     */
    @GraphQLField(sql = "bak_config_id = $S{bakConfigId}", field = "bak_config_id")
    private String bakConfigId;

    /**
     * 归档操作描述
     */
    @GraphQLField(sql = "operate_desc = $S{operateDesc}", field = "operate_desc")
    private String operateDesc;

    /**
     * 1:归档，2:建表  3:还原  4:删除
     */
    @GraphQLField(sql = "type = $S{type}", field = "type")
    private String type;

    /**
     * 操作日期
     */
    @GraphQLField(sql = "operate_date = $S{operateDate}", field = "operate_date")
    private String operateDate;
    /**
     * 用时（秒）
     */
    @GraphQLField(sql = "duration = $S{duration}", field = "duration")
    private String duration;
    /**
     * 归档配置表id
     */
    @GraphQLField(sql = "create_time = $S{createTime}", field = "create_time")
    private String createTime;

    /**
     * 归档配置表id
     */
    @GraphQLField(sql = "update_time = $S{updateTime}", field = "update_time")
    private String updateTime;
}
