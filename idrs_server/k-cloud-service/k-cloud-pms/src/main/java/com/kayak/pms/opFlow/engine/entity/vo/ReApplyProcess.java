package com.kayak.pms.opFlow.engine.entity.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 25/08/2017.
 * 需要重新进行申请的流程名
 */
@Data
@Alias("reApplyProcess")
public class ReApplyProcess {
    private String processName;
    private String processInstanceId;
    private String taskId;

}
