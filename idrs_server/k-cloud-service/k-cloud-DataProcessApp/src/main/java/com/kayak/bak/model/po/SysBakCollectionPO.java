package com.kayak.bak.model.po;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@GraphQLModel(fetcher = "bakCollectionAction",table = "sys_bak_collection")
@Accessors(chain = true)
public class SysBakCollectionPO {

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
     * 备份目标库
     */
    @GraphQLField(sql = "target_db = $S{targetDb}", field = "target_db")
    private String targetDb;

    /**
     * 备份目标表名
     */
    @GraphQLField(sql = "target_table = $S{targetTable}", field = "target_table")
    private String targetTable;

    /**
     * 仓库表命名排序号
     */
    @GraphQLField(sql = "bak_sort = $S{bakSort}", field = "bak_sort")
    private Integer bakSort;

    /**
     * 仓库表名（备份表命名排序号_备份目标表名）
     */
    @GraphQLField(sql = "bak_table = $S{bakTable}", field = "bak_table")
    private String bakTable;

    /**
     * 仓库表数据量
     */
    @GraphQLField(sql = "bak_num = $S{bakNum}", field = "bak_num")
    private String bakNum;

    /**
     * 仓库表数据起始日期
     */
    @GraphQLField(sql = "start_date = $S{startDate}", field = "start_date")
    private String startDate;

    /**
     * 仓库表数据结束日期
     */
    @GraphQLField(sql = "end_date = $S{endDate}", field = "end_date")
    private String endDate;

    /**
     * 最新备份日期
     */
    @GraphQLField(sql = "new_date = $S{newDate}", field = "new_date")
    private String newDate;

    /**
     * 创建时间
     */
    @GraphQLField(sql = "create_time = $S{createTime}", field = "create_time")
    private String createTime;

    /**
     * 修改时间
     */
    @GraphQLField(sql = "update_time = $S{updateTime}", field = "update_time")
    private String updateTime;
}
