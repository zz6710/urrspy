package com.kayak.clear.req;

import lombok.Data;

@Data
public class ReportTimeExecPlanInput {

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
     * 基准类型
     */
    private String baseType;

    /**
     * 日期类型
     */
    private String dataType;

    /**
     * 行内报送时点要求
     */
    private int innerSubmissionTimeRequire;

    /**
     * 监管要求时间点配置
     */
    private int superviseSubmissionTime;

    /**
     * 报送数据生成日期
     */
    private int dataGenerTimeRequire;

    /**
     * 系统工作日
     */
    private String workDate;

    /**
     * 机器时间，规则为workDate + 1 (监管报送系统一般规则是工作日是机器日期的前一天)
     */
    private String macDate;

    /**
     * 时点类型 0 规则配置; 1 非规则配置
     */
    private String timeType;
}
