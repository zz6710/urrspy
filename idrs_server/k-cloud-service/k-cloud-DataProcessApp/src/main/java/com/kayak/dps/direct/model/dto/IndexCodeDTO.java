package com.kayak.dps.direct.model.dto;

import com.kayak.dps.app.model.CheckIndexModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 指标校验基本信息
 */
@Data
@Accessors(chain = true)
public class IndexCodeDTO {

    /**
     * 指标校验数据日期
     */
    private String dealDate;

    /**
     * 指标校验数据日期（支持现有指标格式）
     */
    private String deal_date;

    /**
     * 上月末数据日期
     */
    private String lastDealDate;

    /**
     * 上月末数据日期（支持现有指标格式）
     */
    private String last_deal_date;

    /**
     * 指标编号
     */
    private String indexCode;

    /**
     * 校验所属报表
     */
    private String reportTable;

    /**
     * 校验数据查询语句
     */
    private String relationTables;

    /**
     * 查询语句关联关系
     */
    private String tableRelationships;


    public static IndexCodeDTO dto() {
        return new IndexCodeDTO();
    }

    /**
     * 初始化reportTable, relationTables, tableRelationships的数据
     * @param model
     * @return
     */
    public IndexCodeDTO initIndexCodeDto(CheckIndexModel model) {
        return this.setReportTable(model.getReportTable())
                .setRelationTables(model.getRelationTables())
                .setTableRelationships(model.getTablesRelationships())
                .setIndexCode(model.getIndexCode());
    }

    /**
     * 兼容入库语句方法的双日期字段设置
     * @param dealDate
     * @return
     */
    public IndexCodeDTO setDealDateRe(String dealDate) {
        this.dealDate = dealDate;
        this.deal_date = dealDate;
        return this;
    }

    /**
     * 兼容入库语句方法的双日期字段设置
     * @param lastDealData
     * @return
     */
    public IndexCodeDTO setLastDealDateRe(String lastDealData) {
        this.lastDealDate = lastDealData;
        this.last_deal_date = lastDealData;
        return this;
    }
}
