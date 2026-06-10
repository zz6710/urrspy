package com.kayak.clear.req;

import lombok.Data;

/**
 * 报表执行计划结果
 */
@Data
public class ReportTimeExecPlanOutput {

    /**
     * 报表类型
     */
    private String reportType;

    /**
     * 报表名称
     */
    private String reportTable;

    /**
     * 报表中文名称
     */
    private String reportTableName;

    /**
     * 工作日
     */
    private String workDate;

    /**
     * 数据统计基准日期
     */
    private String baseLineDate;

    /**
     * 监管要求报送的开始日期
     */
    private String startDate;

    /**
     * 监管要求报送的结束日期
     */
    private String endDate;


    /**
     * 执行状态
     */
    private String execStatus;
}
