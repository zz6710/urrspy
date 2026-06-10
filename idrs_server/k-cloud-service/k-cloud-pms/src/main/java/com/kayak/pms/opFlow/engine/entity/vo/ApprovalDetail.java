package com.kayak.pms.opFlow.engine.entity.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 08/06/2017.
 */
@Data
@Alias("approvalDetail")
public class ApprovalDetail {
    private String processDisplayName;
    private String taskDisplayName;
    private String opinion;
    private String result;
    private String resultText;
    private String approvalDate;
    private String approvalTime;

}
