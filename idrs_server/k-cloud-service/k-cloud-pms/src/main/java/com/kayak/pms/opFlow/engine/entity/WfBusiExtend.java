package com.kayak.pms.opFlow.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WfBusiExtend {

    private String extendId;
    private String server;
    private String keysValue;
    private String processId;
    private String processInstanceId;
    private String processStatus;
    private String busStatus;
    private String userid;
    private String startDate;
    private String startTime;
    private String updateDate;
    private String updateTime;
    private String appName;
    private String url;
}