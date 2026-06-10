package com.kayak.web.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kayak.common.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("base_report_export_log")
public class BaseReportExportLog extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String userid;
    private String reportId;
    private String reportName;
    private String applyTime;
    private String dataTime;
    private String processInstanceId;
    private String filePath;
    private String fileStatus;
}
