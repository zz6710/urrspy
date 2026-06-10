package com.kayak.bak.model.po;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@GraphQLModel(fetcher = "bakConfigAction",table = "sys_bak_config")
@Accessors(chain = true)
public class SysBakConfigPO {

    /**
     * 主键id
     */
    @GraphQLField(key = true, sql = "id = $S{id}", field = "id")
    private String id;

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
     * 备份目标表数据日期标识字段
     */
    @GraphQLField(sql = "target_field = $S{targetField}", field = "target_field")
    private String targetField;

    /**
     * 目标库已备份数据量
     */
    @GraphQLField(sql = "table_bak_num = $S{tableBakNum}", field = "table_bak_num")
    private Integer tableBakNum;

    /**
     * 备份方式 1: 按天数 2：按自然月（一号备份）
     *
     */
    @GraphQLField(sql = "type = $S{type}", field = "type")
    private String type;

    /**
     * 自动建表 1: 自动建表 2：手动建表
     */
    @GraphQLField(sql = "type = $S{autoTable}", field = "auto_table")
    private String autoTable;

    /**
     * 频率：备份方式选择按天数备份则为天数；按自然月备份则表示频率月份
     */
    @GraphQLField(sql = "frequency = $S{frequency}", field = "frequency")
    private Integer frequency;

    /**
     * 分表阈值：单表备份的默认数据值
     */
    @GraphQLField(sql = "threshold = $S{threshold}", field = "threshold")
    private Integer threshold;

    /**
     * 归档数据是否保留元数据 源数据 1：删除  2：保留
     */
    @GraphQLField(sql = "source_data = $S{sourceData}", field = "source_data")
    private String sourceData;

    /**
     * 字段变换是否适配 1：自动  2：非自动
     */
    @GraphQLField(sql = "field_change = $S{fieldChange}", field = "field_change")
    private String fieldChange;

    /**
     * 生效日期，格式示例：20230101，按天数备份为第二天开始备份；按自然月为下个月一号开始备份
     */
    @GraphQLField(sql = "begin_date = $S{beginDate}", field = "begin_date")
    private String beginDate;

    /**
     * 上次备份日期
     */
    @GraphQLField(sql = "last_date = $S{lastDate}", field = "last_date")
    private String lastDate;

    /**
     * 下次备份日期
     */
    @GraphQLField(sql = "next_Date = $S{nextDate}", field = "next_date")
    private String nextDate;

    /**
     * 上次备份起始日期区间
     */
    @GraphQLField(sql = "last_left_date = $S{lastLeftDate}", field = "last_left_date")
    private String lastLeftDate;

    /**
     * 上次备份终止日期区间
     */
    @GraphQLField(sql = "last_right_date = $S{lastRightDate}", field = "last_right_date")
    private String lastRightDate;

    /**
     * 下次预计备份日期起始区间
     */
    @GraphQLField(sql = "next_left_date = $S{nextLeftDate}", field = "next_left_date")
    private String nextLeftDate;

    /**
     * 下次预计备份日期终止区间
     */
    @GraphQLField(sql = "next_right_date = $S{nextRightDate}", field = "next_right_date")
    private String nextRightDate;

    /**
     * 创建日期
     */
    @GraphQLField(sql = "create_time = $S{createTime}", field = "create_time")
    private String createTime;

    /**
     * 修改日期
     */
    @GraphQLField(sql = "update_time = $S{updateTime}", field = "update_time")
    private String updateTime;

    /**
     * 删除起始日期
     */
    @GraphQLField(sql = "delete_end_date = $S{deleteStartDate}", field = "delete_end_date")
    private String deleteStartDate;

    /**
     * 删除结束日期
     */
    @GraphQLField(sql = "delete_end_date = $S{deleteEndDate}", field = "delete_end_date")
    private String deleteEndDate;

    /**
     * 删除方式 1按天数2按自然月3按周
     */
    @GraphQLField(sql = "delete_type = $S{deleteType}", field = "delete_type")
    private String deleteType;

    /**
     * 删除频率：备份方式按天数则为天数，按自然月备份则表示月份
     */
    @GraphQLField(sql = "delete_frequency = $S{deleteFrequency}", field = "delete_frequency")
    private String deleteFrequency;

}
