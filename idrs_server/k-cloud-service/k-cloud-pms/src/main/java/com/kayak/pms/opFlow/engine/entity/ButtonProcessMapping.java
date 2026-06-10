package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 20/06/2017.
 */
@Data
@Alias("buttonProcessMapping")
public class ButtonProcessMapping {

    private String id;
    private String buttonId;
    private String buttonName;
    private String processId;
    private String processName;
    private String processDisplayName;
    private String status;

}
