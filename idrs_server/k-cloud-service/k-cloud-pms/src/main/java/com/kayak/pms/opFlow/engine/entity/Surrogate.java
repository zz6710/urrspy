package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 07/06/2017.
 */
@Data
@Alias("surrogate")
public class Surrogate {

    private String id;
    private String processDisplayName;
    private String processName;
    private String startDate;
    private String endDate;
    private String creator;
    private String surrogate;
    private String surrogateName;
    private String createDate;
    private String createTime;
    private String updateDate;
    private String updateTime;
    private String status;

    private String creatorName;

}
