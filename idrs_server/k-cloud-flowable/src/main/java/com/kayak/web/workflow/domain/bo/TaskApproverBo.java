package com.kayak.web.workflow.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yuanjinqiao
 * @description 任务的审批人
 * @create 2023-03-09 15:45
 **/
@Data
public class TaskApproverBo {
    /**
     * 任务的id
     */
    String id;
    /**
     * 任务的审批人
     */
    List<String> approvers;
}
